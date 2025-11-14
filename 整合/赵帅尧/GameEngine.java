/*
 * 负责人:
 * 功能: 判断当前关卡是否已完成
 * 内容：
 * 1. 遍历地图，统计未在目标点上的箱子
 * 2. 未完成箱子数为 0 则胜利
 * 异常与边界：
 * - 依赖 `base` 标记目标点，`map` 标记箱子与箱子在目标
 * 参数:
 * - state：当前游戏状态
 * 返回值:
 * - boolean：是否胜利
 */
// 算法不符合约定：胜利判断应“未在目标点上的箱子数为 0”，应遍历网格，检查 map 为 BOX 时对应 base 是否为 GOAL ，而非当前实现
public static boolean isWin(GameState state) {
    int unfinishedbox = 0; // 初始化在目标点上的箱子
    for (int i = 0; i < state.map.length; i++) {     // 遍历地图上的目标点所对应的完成情况
        for (int j = 0; j < state.map[i].length; j++) {
            // 使用赋值 = 而非比较 == ，且将 map 的整数编码与字符 '■' 比较，逻辑错误
            // 如果当前为普通箱子 标记   //原来为方框改为Box
            if (state.map[i][j] == BOX) {
                // 将 base 当坐标列表处理（ state.base[k][0] / [1] ），但 base 是矩阵，错误的结构假设
                if(i<state.base.length&&j<state.base[i].length) {      //箱子是否超出base范围
                    if (state.base[i][j]!=GOAL) {
                        unfinishedbox++;
                    }
                }else {
                    unfinishedbox++;
                }
                    }
                }
            }
    return unfinishedbox == 0;
}