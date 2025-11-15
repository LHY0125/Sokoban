import java.util.Scanner;
import model.GameState;
import service.LevelLoader;
import service.MoveValidator;
import service.Settings;
import java.util.ArrayDeque;
import view.ConsoleMenuView;
import view.ConsoleGameView;
import service.UndoUtil;

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

                    // 校验并处理排行榜查看命令
                    if (ConsoleGameView.tryRank(lv, scanner)) {
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
                    String s = scanner.nextLine();
                    if (s != null && !s.isEmpty()) {
                        try {
                            int v = Integer.parseInt(s.trim());
                            Settings.setMaxUndo(v);
                        } catch (Exception ignored) {
                            
                        }
                    }
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

        // 用于存储玩家每一步的快照，用于悔步操作
        ArrayDeque<UndoUtil.Snapshot> undoStack = new ArrayDeque<>();
        int undoUsed = 0;

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
            switch (c) {
                case 'w':
                    if (MoveValidator.handleMove(scanner, state, 0, undoStack, levelIndex)) {
                        return;
                    }
                    continue;
                case 's':
                    if (MoveValidator.handleMove(scanner, state, 1, undoStack, levelIndex)) {
                        return;
                    }
                    continue;
                case 'a':
                    if (MoveValidator.handleMove(scanner, state, 2, undoStack, levelIndex)) {
                        return;
                    }
                    continue;
                case 'd':
                    if (MoveValidator.handleMove(scanner, state, 3, undoStack, levelIndex)) {
                        return;
                    }
                    continue;
                case 'q':
                    // 返回主菜单
                    return;
                case 'r':
                    // 重开当前关卡
                    state = LevelLoader.load(levelIndex);
                    ConsoleGameView.render(state);
                    undoStack.clear();
                    undoUsed = 0;
                    continue;
                case 'z':
                    // 悔步操作
                    ConsoleMenuView.showUndoConfirm();
                    String ans = scanner.nextLine();
                    if (ans != null && !ans.isEmpty() && (ans.charAt(0) == 'y' || ans.charAt(0) == 'Y')) {
                        if (undoStack.isEmpty()) {
                            System.out.println("没有可撤销的步骤");
                        } else if (undoUsed >= Settings.getMaxUndo()) {
                            System.out.println("达到悔步次数上限");
                        } else {
                            UndoUtil.Snapshot s = undoStack.pop();
                            UndoUtil.restore(state, s);
                            if (state.steps > 0) {
                                state.steps -= 1;
                            }
                            undoUsed += 1;
                            ConsoleGameView.render(state);
                        }
                    }
                    continue;
                default:
                    break;
            }
        }
    }  
}