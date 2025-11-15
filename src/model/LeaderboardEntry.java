package model;

public class LeaderboardEntry {
    // 玩家姓名
    public final String name;
    // 步数
    public final int steps;
    // 游戏结束时的消息
    public final String message;

    /*
     * 负责人: 刘航宇
     * 功能: 创建新的排行榜条目
     * 内容：
     * 1. 赋值昵称、步数、留言到只读字段
     * 参数:
     * - name：昵称
     * - steps：步数
     * - message：留言
     * 返回值:
     * - 无
     */
    public LeaderboardEntry(String name, int steps, String message) {
        this.name = name;
        this.steps = steps;
        this.message = message;
    }
}