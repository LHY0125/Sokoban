package service;

public class Settings {
    // 最大悔步次数配置
    private static int maxUndo = 5;

    /*
     * 负责人: 刘航宇
     * 功能: 获取悔步次数上限
     * 内容：
     * 1. 返回当前配置的最大悔步次数
     * 参数:
     * - 无
     * 返回值:
     * - int：最大悔步次数
     */
    public static int getMaxUndo() {
        return maxUndo;
    }

    /*
     * 负责人: 刘航宇
     * 功能: 设置悔步次数上限
     * 内容：
     * 1. 小于 0 的输入视为 0
     * 参数:
     * - value：欲设置的次数
     * 返回值:
     * - 无
     */
    public static void setMaxUndo(int value) {
        if (value < 0) value = 0;
        maxUndo = value;
    }
}