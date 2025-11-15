package service;

import model.GameState;
import model.TileType;

public class GameEngine {
    /*
     * 负责人: 于诗鑫
     * 功能: 玩家按方向尝试移动并更新状态
     * 内容：
     * 1. 根据方向计算位移 `(dx, dy)`
     * 2. 检查目标格：
     *    - 墙：移动失败
     *    - 空地/目标：玩家移动成功
     *    - 箱子/箱子在目标：检查箱子后方是否可推（空地/目标）
     * 3. 执行状态更新：
     *    - 更新玩家坐标与 `map`、必要时更新箱子位置
     *    - 维护步数计数 `state.steps++`（如有）
     * 4. 返回移动是否成功
     * 异常与边界：
     * - 越界访问需提前判断
     * - 保持 `base` 与 `map` 尺寸一致
     * 参数:
     * - state：当前游戏状态
     * - direction：0上/1下/2左/3右
     * 返回值:
     * - boolean：是否移动成功
     */
    public static boolean move(GameState state, int direction) {
        // 根据方向计算位移 (dx, dy)：0上/1下/2左/3右
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case 0:
                dx = -1;
                break;
            case 1:
                dx = 1;
                break;
            case 2:
                dy = -1;
                break;
            case 3:
                dy = 1;
                break;
            default:
                return false;
        }

        // 预取维度与玩家当前坐标
        int rows = state.map.length;
        int cols = 0;
        if (rows > 0) {
            cols = state.map[0].length;
        }
        int px = state.player.x;
        int py = state.player.y;

        // 前方格坐标
        int nx = px + dx;
        int ny = py + dy;

        // 边界保护：先判范围再访问数组
        if (nx < 0 || ny < 0 || nx >= rows || ny >= cols) {
            return false;
        }
        // 判断静态地形：前方若是墙不可移动
        if (state.base[nx][ny] == TileType.WALL.code) {
            return false;
        }

        // 前方动态层元素
        int dynNext = state.map[nx][ny];

        // 走入空地（或无实体）：玩家前进一步
        if (dynNext == TileType.EMPTY.code) {
            // 清空玩家原位，玩家进入前方格
            state.map[px][py] = TileType.EMPTY.code;
            state.map[nx][ny] = TileType.PLAYER.code;
            state.player.x = nx;
            state.player.y = ny;
            state.steps++;
            return true;
        }

        // 推箱子：前方为箱子或箱子在目标
        if (dynNext == TileType.BOX.code || dynNext == TileType.BOX_ON_GOAL.code) {
            // 箱子后方格坐标
            int bx = nx + dx;
            int by = ny + dy;
            // 边界保护
            if (bx < 0 || by < 0 || bx >= rows || by >= cols) {
                return false;
            }
            // 静态地形：后方是墙则不可推动
            if (state.base[bx][by] == TileType.WALL.code) {
                return false;
            }
            // 后方动态层：已有箱子也不可推动
            int dynBehind = state.map[bx][by];
            if (dynBehind == TileType.BOX.code || dynBehind == TileType.BOX_ON_GOAL.code) {
                return false;
            }

            // 落点：若后方为目标 → 箱子在目标，否则普通箱子
            if (state.base[bx][by] == TileType.GOAL.code) {
                state.map[bx][by] = TileType.BOX_ON_GOAL.code;
            } else {
                state.map[bx][by] = TileType.BOX.code;
            }

            // 玩家进入箱子原位，原位清空
            state.map[nx][ny] = TileType.PLAYER.code;
            state.map[px][py] = TileType.EMPTY.code;
            state.player.x = nx;
            state.player.y = ny;
            state.steps++;
            return true;
        }

        // 其它实体：不可移动
        return false;
    }

    /*
     * 负责人: 赵帅尧
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
    public static boolean isWin(GameState state) {
        for (int i = 0; i < state.map.length; i++) {
            for (int j = 0; j < state.map[i].length; j++) {
                if (state.map[i][j] == TileType.BOX.code && state.base[i][j] != TileType.GOAL.code) {
                    return false;
                }
            }
        }
        return true;
    }
}