/*
 * 负责人: 张启亮
 * 功能: 计算剩余目标点数量
 * 内容：
 * 1. 遍历地图基础层(base)，统计所有目标点(GOAL)的总数
 * 2. 遍历地图动态层(map)，统计已完成的目标点(箱子在目标点上，BOX_ON_GOAL)数量
 * 3. 剩余目标点 = 总目标点 - 已完成目标点
 * 参数:
 * - state：当前游戏状态，包含基础层、动态层地图数据
 * 返回值:
 * - int：剩余目标点数量（若状态或地图数据无效，返回0）
 */
private static int remainingGoals(GameState state) {
    // 校验入参合法性，避免空指针异常
    if (state == null || state.base == null || state.map == null) {
        return 0;
    }

    int totalGoals = 0;         // 总目标点数量
    int completedGoals = 0;     // 已完成（箱子在目标点上）的数量

    // 遍历基础层，统计总目标点数量（base中GOAL对应code为3）
    for (int[] row : state.base) {
        // 跳过空行，避免数组越界
        if (row == null) continue;
        for (int tile : row) {
            if (tile == TileType.GOAL.code) {
                totalGoals++;
            }
        }
    }

    // 遍历动态层，统计已完成的目标点数量（map中BOX_ON_GOAL对应code为5）
    for (int[] row : state.map) {
        // 跳过空行，避免数组越界
        if (row == null) continue;
        for (int tile : row) {
            if (tile == TileType.BOX_ON_GOAL.code) {
                completedGoals++;
            }
        }
    }

    // 计算剩余目标点，确保结果非负（应对异常数据）
    return Math.max(0, totalGoals - completedGoals);
}