package service;

import model.GameState;
import model.Position;
import model.TileType;
import java.nio.file.*;
import java.util.*;

public class LevelLoader {
    /*
     * 负责人: 
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

        // 初始化游戏状态
        return new GameState(base, map, player, levelIndex, total);
    }


    /*
     * 负责人: 王宇晗
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
    private static int countLevels() {
        // 1. 定位到项目内的map文件夹（相对项目根目录的路径）
        String levelDirPath = "map/";
        File mapDir = new File(levelDirPath);

        // 2. 校验map文件夹是否存在
        if (!mapDir.exists() || !mapDir.isDirectory()) {
            System.err.println("关卡目录不存在：" + mapDir.getAbsolutePath());
            return 1;
        }

        // 3. 筛选map文件夹中以"level"开头、".txt"结尾的文件
        File[] levelFiles = mapDir.listFiles(file -> {
            String fileName = file.getName();
            return fileName.startsWith("level") && fileName.endsWith(".txt");
        });

        // 4. 返回有效关卡文件的数量（至少能识别到level1~level5这5个文件）
        return (levelFiles != null) ? levelFiles.length : 0;
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