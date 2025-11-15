package service;

import model.GameState;
import model.TileType;

public class MoveValidator {
    /*
     * 负责人: 
     * 功能: 校验玩家按方向移动的合法性
     * 内容：
     * 1. 计算 `(dx, dy)` 并定位前方格
     * 2. 若前方为墙：不可移动
     * 3. 若前方为空地/目标：可移动
     * 4. 若前方为箱子/箱子在目标：检查后方是否为空地/目标以允许推动
     * 异常与边界：
     * - 越界判定：任何越界都视为不可移动
     * 参数:
     * - state：当前游戏状态
     * - direction：0上/1下/2左/3右
     * 返回值:
     * - boolean：是否可以移动
     */
    public static boolean canMove(GameState state, int direction) {
        if (state == null || state.map == null || state.base == null || state.player == null) {
            return false;
        }

        // 计算 dx, dy 并定位前方格
        int dx = 0, dy = 0;
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

        // 定位前方格
        int rows = state.map.length;
        int cols = rows > 0 && state.map[0] != null ? state.map[0].length : 0;
        int px = state.player.x;
        int py = state.player.y;

        int nx = px + dx;
        int ny = py + dy;

        if (nx < 0 || ny < 0 || nx >= rows || ny >= cols) {
            return false;
        }
        if (state.base[nx][ny] == TileType.WALL.code) {
            return false;
        }

        int dynNext = state.map[nx][ny];

        if (dynNext == TileType.EMPTY.code) {
            return true;
        }

        if (dynNext == TileType.BOX.code || dynNext == TileType.BOX_ON_GOAL.code) {
            int bx = nx + dx;
            int by = ny + dy;
            if (bx < 0 || by < 0 || bx >= rows || by >= cols) {
                return false;
            }
            if (state.base[bx][by] == TileType.WALL.code) {
                return false;
            }
            int dynBehind = state.map[bx][by];
            if (dynBehind == TileType.BOX.code || dynBehind == TileType.BOX_ON_GOAL.code) {
                return false;
            }
            return true;
        }

        return false;
    }
}