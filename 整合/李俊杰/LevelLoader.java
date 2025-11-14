package service;

import model.GameState;
import model.Position;
import model.TileType;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.BufferedReader;

public class LevelLoader {
    /*
     * 负责人: 李俊杰
     * 功能: 根据关卡编号加载并初始化游戏状态
     * 内容：
     * 1. 统计关卡总数：扫描目录中的关卡文件用于 UI 展示（totalLevels）
     * 2. 构建关卡路径：按规则拼接路径 `map/level{index+1}.txt`
     * 3. 读取关卡文件：尝试读取全部行到列表（失败时进入回退逻辑）
     * 4. 基本维度计算：`h=行数`，`w=首行长度`（空文件时 `w=0`）并分配二维数组
     * 5. 字符解析与映射：逐行逐列解析字符到底层(base)与动态层(map)
     * - `#` → 墙（base=WALL）
     * - `○` → 目标点（base=GOAL）
     * - `■` → 箱子（map=BOX）
     * - `☑` → 箱子在目标（map=BOX_ON_GOAL，同时 base=GOAL）
     * - `☺` → 玩家（map=PLAYER，记录玩家坐标为当前格）
     * - 其它字符或空格 → 空地（base=0，map=0）
     * 6. 玩家初始坐标：默认 (0,0)，遇到 `☺` 时更新为该格坐标
     * 7. 构造状态并返回：`new GameState(base, map, player, levelIndex, total)`
     * 异常与边界：
     * - 文件缺失/读取异常：调用 `fallback()` 返回示例关卡，保证流程不中断
     * - 空文件：`h=0` 时宽度设为 `w=0`，数组尺寸为 0×0
     * - 行宽要求：当前以首行长度为准，关卡文件每行长度需一致，否则可能导致索引越界
     * 参数:
     * - levelIndex: 关卡编号（0 起），建议范围 `0 <= levelIndex < total`
     * 返回值:
     * - GameState：包含底层(base)、动态层(map)、玩家坐标(player)、当前关卡(levelIndex)、总关卡(total)
     */
    public static GameState load(int levelIndex) {
        int total = countLevels();
        File folder = new File(String.format("map/level%d.txt", levelIndex + 1));

        // 读取关卡文件内容到列表
        List<String> mapTxt = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(folder.toPath())) {
            String line;
            while ((line = br.readLine()) != null) {
                mapTxt.add(line);
            }
        } catch (Exception e) {
            // 读取失败时使用回退关卡
            mapTxt = fallback();
        }

        int h = mapTxt.size();
        int w = (h > 0) ? mapTxt.get(0).length() : 0;

        int[][] base = new int[h][w];
        int[][] map = new int[h][w];
        Position player = new Position(0, 0);

        for (int i = 0; i < h; i++) {
            String line = mapTxt.get(i);
            for (int j = 0; j < w; j++) {
                char ch = line.charAt(j);
                switch (ch) {
                    case '#':
                        base[i][j] = TileType.WALL.code;
                        break;
                    case '○':
                        base[i][j] = TileType.GOAL.code;
                        break;
                    case '■':
                        map[i][j] = TileType.BOX.code;
                        break;
                    case '☑':
                        base[i][j] = TileType.GOAL.code;
                        map[i][j] = TileType.BOX_ON_GOAL.code;
                        break;
                    case '☺':
                        map[i][j] = TileType.PLAYER.code;
                        player = new Position(i, j);
                        break;
                    default:
                        // 空地，保持默认值 0
                        break;
                }
            }
        }

        GameState gameState = new GameState(base, map, player, levelIndex, total);
        //System.out.println(gameState);

        // 初始化游戏状态
        return gameState;
    }


    /*
     * 负责人: 
     * 功能: 统计关卡数量
     * 内容：
     * 1. 扫描 `map` 目录：匹配形如 `level*.txt` 的关卡文件
     * 2. 过滤无效项：仅计算普通文件，忽略目录与不可读文件
     * 3. 数量用于 UI：作为总关卡数在主菜单与选择关卡处展示
     * 异常与边界：
     * - 目录不存在/权限异常：返回 1，以保证有至少一个回退关卡
     * - 命名不规范：不计入总数，建议统一命名 `level1.txt`、`level2.txt`...
     * 参数:
     * - 无
     * 返回值:
     * - int：关卡总数
     */
    public static int countLevels() {
        final String dirPath = "map";
        File folder = new File(dirPath);

        if (folder.exists() && folder.isDirectory()) {
            String[] files = folder.list((dir, name) -> name.matches("level\\d+\\.txt"));
            if (files != null) {
                return files.length;
            }
        }
        return 1; // 默认至少有一个回退关卡
    }

    /*
     * 负责人: 刘航宇
     * 功能: 回退示例关卡
     * 内容：
     * 1. 在关卡文件缺失或读取异常时提供内置示例关卡
     * 2. 使用项目统一字符规范：`#` 墙、`☺` 玩家、`■` 箱子、`○` 目标
     * 3. 保证每行长度一致，便于 `load` 按行列解析
     * 异常与边界：
     * - 返回列表非空且行宽一致，避免后续维度计算出错
     * 参数:
     * - 无
     * 返回值:
     * - List<String>：每元素代表地图的一行
     */
    private static List<String> fallback() {
        List<String> list = new ArrayList<>();
        list.add("##########");
        list.add("#☺       #");
        list.add("#  ■     #");
        list.add("#  ○     #");
        list.add("#        #");
        list.add("#     ■  #");
        list.add("#     ○  #");
        list.add("#        #");
        list.add("#        #");
        list.add("##########");
        return list;
    }
}