package service;

import model.GameState;

public class UndoUtil {
    public static class Snapshot {
        // 动态层快照
        public final int[][] map;
        // 玩家坐标快照
        public final int px;
        public final int py;

        /*
         * 负责人: 刘航宇
         * 功能: 构造快照
         * 内容：
         * 1. 复制动态层数组
         * 2. 保存玩家坐标
         * 参数:
         * - src：当前动态层
         * - px：玩家 x
         * - py：玩家 y
         * 返回值:
         * - 无
         */
        public Snapshot(int[][] src, int px, int py) {
            this.map = copy2D(src);
            this.px = px;
            this.py = py;
        }
    }

    /*
     * 负责人: 刘航宇
     * 功能: 用快照恢复状态
     * 内容：
     * 1. 将快照中的动态层 map 深拷贝回当前状态（逐行复制）
     * 2. 恢复玩家坐标为快照时的 px/py
     * 3. 保持静态层 base 不变（墙/目标点属于静态层，不随操作改变）
     * 异常与边界：
     * - 要求快照与当前 state 的地图尺寸一致；否则可能出现越界或空指针
     * - 对于快照中的 null 行，按原样恢复为 null，保持结构一致
     * 复杂度：
     * - 时间 O(R*C)，空间 O(1) 额外（除被复制出的行外）
     * 参数:
     * - state：当前游戏状态
     * - s：快照
     * 返回值:
     * - 无
     */
    public static void restore(GameState state, Snapshot s) {
        if (state == null || s == null || s.map == null || state.map == null || state.player == null) {
            return;
        }
        int rows = s.map.length;
        if (state.map.length != rows) {
            rows = Math.min(rows, state.map.length);
        }
        for (int i = 0; i < rows; i++) {
            int[] row = s.map[i];
            if (row == null) {
                state.map[i] = null;
            } else {
                state.map[i] = java.util.Arrays.copyOf(row, row.length);
            }
        }
        state.player.x = s.px;
        state.player.y = s.py;
    }

    /*
     * 负责人: 刘航宇
     * 功能: 二维数组深拷贝
     * 内容：
     * 1. 新建顶层数组，并对每一行进行独立复制
     * 2. 对于源数组中的 null 行，按原样保留为 null，保持结构一致
     * 3. 仅复制动态层整数值，不涉及对象引用，避免共享导致的串改
     * 异常与边界：
     * - src 不可为 null；调用方需保证传入合法
     * 复杂度：
     * - 时间 O(R*C)，空间 O(R*C)
     * 参数:
     * - src：源二维数组
     * 返回值:
     * - int[][]：拷贝后的二维数组
     */
    private static int[][] copy2D(int[][] src) {
        if (src == null) return null;
        int[][] out = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            int[] row = src[i];
            out[i] = (row == null) ? null : java.util.Arrays.copyOf(row, row.length);
        }
        return out;
    }
}