package view;

import model.GameState;
import util.Renderer;
import model.TileType;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import service.Leaderboard;
import model.LeaderboardEntry;

public class ConsoleGameView {
    private static final String DEFAULT_ART = "      ";
    /*
        负责人: 彭依萍
        功能: 渲染当前游戏状态到控制台
        内容：
        1. 清屏并将光标置顶：ESC[2J ESC[3J ESC[H]
        2. 打印状态栏：当前关卡/总关卡、剩余目标点、步数
        3. 遍历行列，根据 `Renderer.toChar` 输出地图
        4. 打印操作提示：W/S/A/D，R，Q
        异常与边界：
        - 状态为空不渲染；行列尺寸必须一致
        - 控制台需支持 UTF-8 与 Unicode 字符显示
        参数:
        - state：当前游戏状态
        返回值:
        - 无
    */
    public static void render(GameState state) {
        // 重定义字符到字符画的映射,美化
        HashMap<Character, String> renderMap = new HashMap<>();
        renderMap.put('☺', "╭@╮╰■╯");    // 玩家
        renderMap.put(' ', "      ");    // 空格
        renderMap.put('☑', "╔═╗╚═╝");    // 箱子在目标点
        renderMap.put('■', "╭─╮╰─╯");    // 箱子
        renderMap.put('#', "┏━┓┗━┛");    // 墙
        renderMap.put('○', "┌─┐└─┘");    // 空目标点

        // 异常与边界处理：
        // 若状态为空不渲染
        if (state == null) {
            return;
        }

        // 校验地图行列尺寸是否一致，不一致则不渲染
        int[][] base = state.base;
        int[][] map = state.map;
        if (base == null || map == null || base.length == 0 || map.length == 0 || base[0] == null || map[0] == null || base.length != map.length || base[0].length != map[0].length) {
            return;
        }

        // 获取基础层地图布局的行数和列数
        int rowCount = base.length;
        int colCount = base[0].length;

        // 清屏并将光标置顶
        System.out.println("\u001B[2J\u001B[3J\u001B[H");

        // 打印状态栏：当前关卡/总关卡、剩余目标点、步数
        // 先计算剩余目标点remainingTargets
        int remainingTargets = remainingGoals(state);

        // 然后打印状态栏：当前关卡/总关卡、剩余目标点、步数
        System.out.printf("当前关卡:%d/总关卡:%d | 剩余目标点:%d | 步数:%d", state.levelIndex + 1, state.totalLevels, remainingTargets, state.steps);
        System.out.println();

        // 遍历行列，根据 `Renderer.toChar` 输出地图
        for (int i = 0; i < rowCount; i++) {
            char[] displayChar = new char[colCount];
            for (int j = 0; j < colCount; j++) {
                // 获取当前格子的显示字符
                displayChar[j] = Renderer.toChar(base[i][j], map[i][j]);
            }

            // 打印两行字符画
            for (char cell : displayChar) {
                String art = renderMap.getOrDefault(cell, DEFAULT_ART);
                System.out.print(art.substring(0, 3));
            }
            System.out.println();
            for (char cell : displayChar) {
                String art = renderMap.getOrDefault(cell, DEFAULT_ART);
                System.out.print(art.substring(3));
            }

            // 每打印完一行地图就换行
            System.out.println();
        }

        ConsoleMenuView.printOperationHint();
    }


    /*
        负责人: 刘航宇
        功能: 显示胜利界面并提示返回主菜单
        内容：
        1. 清屏并置顶光标
        2. 输出“胜利”提示
        参数:
        - 无
        返回值:
        - 无
    */
    /*
     * \u001B 是 ESC 字符（ANSI 控制序列的起始）
     * ESC[2J 清除当前屏幕内容
     * ESC[3J 清除滚动缓冲区（历史输出），避免上下滚动时仍看到旧内容
     * ESC[H 将光标移动到屏幕左上角（行1列1）
     */
    public static void win() {
        System.out.print("\u001B[2J\u001B[3J\u001B[H");
        ConsoleMenuView.showVictory();
    }

    /*
        负责人: 张启亮
        功能: 计算剩余目标点数量
        内容：
        1. 遍历地图，统计 base 为目标点的格数
        2. 减去 map 中已完成（箱子在目标）格数
        3. 返回剩余数量
        参数:
        - state：当前游戏状态
        返回值:
        - int：剩余目标点数量
    */
    private static int remainingGoals(GameState state) {
        // 校验入参合法性，避免空指针异常
        if (state == null || state.base == null || state.map == null) {
            return 0;
        }

        int totalGoals = 0; // 总目标点数量
        int completedGoals = 0; // 已完成（箱子在目标点上）的数量

        // 遍历基础层，统计总目标点数量（base中GOAL对应code为3）
        for (int[] row : state.base) {
            // 跳过空行，避免数组越界
            if (row == null)
            {
                continue;
            }
            for (int tile : row) {
                if (tile == TileType.GOAL.code) {
                    totalGoals++;
                }
            }
        }

        // 遍历动态层，统计已完成的目标点数量（map中BOX_ON_GOAL对应code为5）
        for (int[] row : state.map) {
            // 跳过空行，避免数组越界
            if (row == null)
            {
                continue;
            }
            for (int tile : row) {
                if (tile == TileType.BOX_ON_GOAL.code) {
                    completedGoals++;
                }
            }
        }

        // 计算剩余目标点，确保结果非负（应对异常数据）
        return Math.max(0, totalGoals - completedGoals);
    }

    /*
        负责人: 刘航宇
        功能: 处理排行榜查看命令
        内容：
        1. 解析输入形如 L<编号>
        2. 显示对应关卡的排行榜并等待回车
        参数:
        - lv：用户输入的字符串，形如 L<编号>
        - scanner：输入流，用于等待返回
        返回值:
        - boolean：若已处理返回 true，否则返回 false
    */
    public static boolean tryRank(String lv, Scanner scanner) {
        // 校验入参合法性，避免空指针异常
        if (lv == null || scanner == null || lv.length() < 2)
        {
            return false;
        }

        // 校验输入格式是否为 L<编号>
        char c = lv.charAt(0);
        if (c != 'L' && c != 'l') 
        {
            return false;
        }

        // 解析关卡编号，转换为数组索引（从0开始）
        try {
            // 校验编号是否为有效整数
            int n = Integer.parseInt(lv.substring(1));
            int idx = Math.max(0, n - 1);
            
            // 读取并显示排行榜
            List<LeaderboardEntry> list = Leaderboard.readTop(idx, 10);
            ConsoleMenuView.showLeaderboard(idx, list);
            ConsoleMenuView.printReturnHint();

            // 等待用户回车，返回主菜单
            scanner.nextLine();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}