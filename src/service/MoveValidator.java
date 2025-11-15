package service;

import model.GameState;
import model.TileType;
import view.ConsoleGameView;
import view.ConsoleMenuView;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.List;

public class MoveValidator {
    /*
     * 负责人: 夏佳阔
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

    /*
     * 负责人: 刘航宇
     * 功能: 统一处理方向键移动与胜利流程
     * 内容：
     * 1. 校验是否可移动，合法则压栈快照并执行移动
     * 2. 若胜利：保存昵称与留言、写入排行榜、展示前10名并等待回车
     * 3. 若不可移动：根据目标格类型输出提示（墙/箱子无法推动）
     * 4. 每次操作后重新渲染当前状态
     * 参数:
     * - scanner：输入流，用于读取昵称/留言与等待回车
     * - state：当前游戏状态
     * - dir：移动方向（0上/1下/2左/3右）
     * - undoStack：悔步栈，保存操作前快照
     * - levelIndex：关卡索引（0起），用于排行榜读写
     * 返回值:
     * - boolean：若本次操作导致胜利返回 true，否则返回 false
     */
    public static boolean handleMove(Scanner scanner, GameState state, int dir, ArrayDeque<UndoUtil.Snapshot> undoStack, int levelIndex) {
        try {
            // 检查是否可以移动
            if (canMove(state, dir)) {
                undoStack.push(new UndoUtil.Snapshot(state.map, state.player.x, state.player.y));
                GameEngine.move(state, dir);

                // 检查是否胜利
                if (GameEngine.isWin(state)) {
                    ConsoleGameView.win();

                    // 输入昵称与留言并保存到排行榜
                    System.out.print("请输入昵称: ");
                    String name = scanner.nextLine();
                    System.out.print("请输入留言: ");
                    String msg = scanner.nextLine();
                    Leaderboard.save(levelIndex, name, state.steps, msg);
                    List<model.LeaderboardEntry> list = Leaderboard.readTop(levelIndex, 10);
                    ConsoleMenuView.showLeaderboard(levelIndex, list);

                    // 等待用户回车返回主菜单
                    ConsoleMenuView.printReturnHint();
                    scanner.nextLine();
                    return true;
                }
            }
            // 检查是否无法移动
            else {
                int dx = 0, dy = 0;
                switch (dir) {
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
                int nx = state.player.x + dx;
                int ny = state.player.y + dy;

                // 检查目标格是否为箱子或已完成目标点
                int t = state.map[nx][ny];
                if (nx >= 0 && ny >= 0 && nx < state.map.length && ny < state.map[0].length) {
                    // 检查前方是否为墙
                    if (state.base[nx][ny] == TileType.WALL.code) {
                        System.out.println("无法移动，前方是墙");
                    }
                    // 检查前方是否为箱子或已完成目标点
                    else if (t == TileType.BOX.code || t == TileType.BOX_ON_GOAL.code) {
                        int bx = nx + dx;
                        int by = ny + dy;
                        if (bx >= 0 && by >= 0 && bx < state.map.length && by < state.map[0].length) {
                            int behindDyn = state.map[bx][by];
                            int behindBase = state.base[bx][by];
                            // 检查箱子推动目标格是否为墙或其他箱子
                            if (behindBase == TileType.WALL.code || behindDyn == TileType.BOX.code
                                    || behindDyn == TileType.BOX_ON_GOAL.code) {
                                System.out.println("箱子无法推动");
                            }
                        }
                    }
                }
            }
            
            // 渲染当前状态
            ConsoleGameView.render(state);
        } catch (Exception ignored) {
        }
        
        return false;
    }
}