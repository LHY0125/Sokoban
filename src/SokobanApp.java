import java.util.Scanner;
import model.GameState;
import model.TileType;
import service.LevelLoader;
import service.MoveValidator;
import service.GameEngine;
import view.ConsoleMenuView;
import view.ConsoleGameView;

/**
 * @file SokobanApp.java
 * @brief 主程序入口与主菜单循环
 * @note 主程序入口与主菜单循环，展示主菜单并处理用户输入
 * @brief 如果想打包，将以下指令复制到powershell
 * @brief makensis installer\installer.nsi
 * @brief iscc installer\installer.iss
 */

public class SokobanApp {
    /*
     * 负责人: 刘航宇
     * 功能: 程序入口与主菜单循环
     * 内容：
     * 1. 初始化输入流 `Scanner`
     * 2. 循环展示主菜单并读取用户输入
     * 3. 输入处理：
     * - "1"：进入关卡1（索引0）游戏循环
     * - "2"：展示选关提示，读取关卡编号并进入对应关卡
     * - "3"：退出循环并关闭输入
     * 4. 资源释放：退出时关闭 `Scanner`
     * 异常与边界：
     * - 非法输入（空/非数字）安全忽略或捕获异常
     * - 选关编号小于等于0返回主菜单
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void main(String[] args) {
        // // 设置控制台字符编码为UTF-8，避免中文乱码
        // try {
        //     System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        //     System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));
        // } catch (Exception e) {
        // }
        
        Scanner scanner = new Scanner(System.in);
        // 主循环，展示主菜单并处理用户输入
        mainLoop: while (true) {
            // 展示主菜单
            ConsoleMenuView.showMain();
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // 进入关卡1（索引0）游戏循环
                    gameLoop(scanner, 0);
                    break;
                case "2":
                    // 展示选关提示，读取关卡编号并进入对应关卡
                    int total = LevelLoader.totalLevels();
                    ConsoleMenuView.showSelectLevel(total);

                    // 读取用户输入的关卡编号
                    String lv = scanner.nextLine();
                    if ("0".equals(lv)) {
                        break;
                    }
                    
                    // 解析用户输入的关卡编号，转换为索引（减1）
                    try {
                        int level = Integer.parseInt(lv);
                        gameLoop(scanner, Math.max(0, level - 1));
                    } catch (Exception ignored) {
                    }
                    break;
                case "3":
                    // 退出主循环
                    break mainLoop;
                case "4":
                    // 展示团队信息
                    ConsoleMenuView.showTeam();
                    ConsoleMenuView.printReturnHint();
                    scanner.nextLine();
                    break;
                case "5":
                    // 展示游戏操作说明
                    ConsoleMenuView.showHowToPlay();
                    ConsoleMenuView.printReturnHint();
                    scanner.nextLine();
                    break;
                case "6":
                    // 展示游戏设置
                    ConsoleMenuView.showSettings();
                    ConsoleMenuView.printReturnHint();
                    scanner.nextLine();
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }

    /*
     * 负责人: 刘航宇
     * 功能: 控制单关卡的游戏主循环
     * 内容：
     * 1. 加载并渲染初始状态
     * 2. 循环读取用户输入（取首字符，忽略大小写）
     * 3. 功能键：
     * - Q：返回主菜单
     * - R：重开当前关卡
     * 4. 移动键：W/S/A/D 映射为方向 0/1/2/3
     * 5. 合法性校验：调用 `MoveValidator.canMove`
     * 6. 执行移动与胜利判断：`GameEngine.move`、`GameEngine.isWin`
     * 7. 每次操作后重新渲染状态
     * 异常与边界：
     * - 空输入直接继续循环
     * - 越界与障碍提示在校验分支内输出
     * 参数:
     * - scanner：输入流
     * - levelIndex：关卡索引（0起）
     * 返回值:
     * - 无
     */
    private static void gameLoop(Scanner scanner, int levelIndex) {
        GameState state = LevelLoader.load(levelIndex);
        ConsoleGameView.render(state);

        while (true) {
            // 渲染游戏状态
            ConsoleGameView.render(state);
            // 处理用户输入
            String input = scanner.nextLine();
            if (input == null || input.isEmpty()) {
                continue;
            }

            // 转换为小写字母
            char c = Character.toLowerCase(input.charAt(0));
            if (c == 'q') {
                break;
            }
            if (c == 'r') {
                state = LevelLoader.load(levelIndex);
                ConsoleGameView.render(state);
                continue;
            }
            // 转换为移动方向
            try {
                int dir = -1;
                switch (c) {
                    case 'w':
                        dir = 0;
                        break;
                    case 's':
                        dir = 1;
                        break;
                    case 'a':
                        dir = 2;
                        break;
                    case 'd':
                        dir = 3;
                        break;
                    default:
                        break;
                }
                if (dir == -1) {
                    continue;
                }
                // 检查移动是否合法
                if (MoveValidator.canMove(state, dir)) {
                    GameEngine.move(state, dir);
                    if (GameEngine.isWin(state)) {
                        ConsoleGameView.win();
                        break;
                    }
                }
                // 检查移动是否会导致箱子被推到墙上
                else {
                    int dx = (dir == 0 ? -1 : dir == 1 ? 1 : 0);
                    int dy = (dir == 2 ? -1 : dir == 3 ? 1 : 0);
                    int nx = state.player.x + dx;
                    int ny = state.player.y + dy;
                    int t = state.map[nx][ny];

                    // 检查移动是否会导致箱子被推到墙上
                    if (nx >= 0 && ny >= 0 && nx < state.map.length && ny < state.map[0].length) {
                        if (state.base[nx][ny] == TileType.WALL.code) {
                            System.out.println("无法移动，前方是墙");
                        }

                        else if (t == TileType.BOX.code || t == TileType.BOX_ON_GOAL.code) {
                            int bx = nx + dx;
                            int by = ny + dy;
                            if (bx >= 0 && by >= 0 && bx < state.map.length && by < state.map[0].length) {
                                int behindDyn = state.map[bx][by];
                                int behindBase = state.base[bx][by];
                                if (behindBase == TileType.WALL.code || behindDyn == TileType.BOX.code || behindDyn == TileType.BOX_ON_GOAL.code) {
                                    System.out.println("箱子无法推动");
                                }
                            }
                        }
                    }
                }
                // 渲染游戏状态
                ConsoleGameView.render(state);
            } catch (Exception ignored) {
            }
        }
    }
}