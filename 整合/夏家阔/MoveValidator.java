package service;

import model.GameState;
import model.TileType;

public class MoveValidator {
    /*
     * 负责人: 夏佳阔
     * 功能: 判断玩家是否可以向指定方向移动
     * 根据地图数据判断移动是否合法（是否撞墙、是否能推箱子等）
     * 参数:
     * direction: 移动方向 (0: 上, 1: 下, 2: 左, 3: 右)
     * 返回值:
     * false表示不能移动
     * true表示可以移动
     */
    public static boolean canMove(GameState state, int direction) {
        /*
         * 根据输入方向判断这个方向上人物前方是否可以移动
         * 选择方向进行判断
         * 返回相关结果
         */
        int a = state.player.x;
        int b = state.player.y;// 用ab表示玩家坐标
        // 使用 state.map 判断墙与目标（值 1、3），应使用 state.base 判断静态地形;无边界判断，存在越界风险（如 b+2/a-2 ）(26-48)
        // 方向与坐标不匹配：约定为 0:上(-1,0) 1:下(+1,0) 2:左(0,-1) 3:右(0,+1) ；代码中 case 0 用 b+1 是“右移”，整体方向映射错误(26-36)
        // 默认返回 true ，即使前面不匹配任何合法条件，也会放行(52)
        switch (direction) { // 选方向
            case 0:
                if (state.map[a][b + 1] == 0 || state.map[a][b + 1] == 3
                        || (state.map[a][b + 1] == 2 && state.map[a][b + 2] == 0)
                        || (state.map[a][b + 1] == 5 && state.map[a][b + 2] == 0) ||
                        (state.map[a][b + 1] == 2 && state.map[a][b + 2] == 3)
                        || (state.map[a][b + 1] == 5 && state.map[a][b + 2] == 3)) {
                    return true;
                }
                if (state.map[a][b + 1] == 1 || (state.map[a][b + 1] == 2 && state.map[a][b + 2] == 1)
                        || (state.map[a][b + 2] == 1 && state.map[a][b + 1] == 5)
                        || (state.map[a][b + 1] == 2 && state.map[a][b + 2] == 2) ||
                        (state.map[a][b + 1] == 5 && state.map[a][b + 2] == 5)
                        || (state.map[a][b + 1] == 2 && state.map[a][b + 2] == 5)
                        || (state.map[a][b + 1] == 5 && state.map[a][b + 2] == 2)) {
                    return false;
                }

            case 1:
                if (state.map[a][b - 1] == 0 || state.map[a][b - 1] == 3
                        || (state.map[a][b - 1] == 2 && state.map[a][b - 2] == 0)
                        || (state.map[a][b - 1] == 5 && state.map[a][b - 2] == 0) ||
                        (state.map[a][b - 1] == 2 && state.map[a][b - 2] == 3)
                        || (state.map[a][b - 1] == 5 && state.map[a][b - 2] == 3)) {
                    return true;
                }
                if (state.map[a][b - 1] == 1 || (state.map[a][b - 1] == 2 && state.map[a][b - 2] == 1)
                        || (state.map[a][b - 2] == 1 && state.map[a][b - 1] == 5)
                        || (state.map[a][b - 1] == 2 && state.map[a][b - 2] == 2) ||
                        (state.map[a][b - 1] == 5 && state.map[a][b - 2] == 5)
                        || (state.map[a][b - 1] == 2 && state.map[a][b - 2] == 5)
                        || (state.map[a][b - 1] == 5 && state.map[a][b - 2] == 2)) {
                    return false;
                }

            case 2:
                if (state.map[a - 1][b] == 0 || state.map[a - 1][b] == 3
                        || (state.map[a - 1][b] == 2 && state.map[a - 2][b] == 0)
                        || (state.map[a - 1][b] == 5 && state.map[a - 2][b] == 0) ||
                        (state.map[a - 1][b] == 2 && state.map[a - 2][b] == 3)
                        || (state.map[a - 1][b] == 5 && state.map[a - 2][b] == 3)) {
                    return true;
                }
                if (state.map[a - 1][b] == 1 || (state.map[a - 1][b] == 2 && state.map[a - 2][b] == 1)
                        || (state.map[a - 2][b] == 1 && state.map[a - 1][b] == 5)
                        || (state.map[a - 1][b] == 2 && state.map[a - 2][b] == 2) ||
                        (state.map[a - 1][b] == 5 && state.map[a - 2][b] == 5)
                        || (state.map[a - 1][b] == 2 && state.map[a - 2][b] == 5)
                        || (state.map[a - 1][b] == 5 && state.map[a - 2][b] == 2)) {
                    return false;
                }
            case 3:
                if (state.map[a + 1][b] == 0 || state.map[a + 1][b] == 3
                        || (state.map[a + 1][b] == 2 && state.map[a + 2][b] == 0)
                        || (state.map[a + 1][b] == 5 && state.map[a + 2][b] == 0)
                        || (state.map[a + 1][b] == 2 && state.map[a + 2][b] == 3)
                        || (state.map[a + 1][b] == 5 && state.map[a + 2][b] == 3)) {
                    return true;
                }
                if (state.map[a + 1][b] == 1 || (state.map[a + 1][b] == 2 && state.map[a + 2][b] == 1)
                        || (state.map[a + 2][b] == 1 && state.map[a + 1][b] == 5)
                        || (state.map[a + 1][b] == 2 && state.map[a + 2][b] == 2)
                        || (state.map[a + 1][b] == 5 && state.map[a + 2][b] == 5)
                        || (state.map[a + 1][b] == 2 && state.map[a + 2][b] == 5)
                        || (state.map[a + 1][b] == 5 && state.map[a + 2][b] == 2)) {
                    return false;
                }
            default:
                break;
        }
        return true;
    }
}