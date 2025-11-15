package view;

public class ConsoleMenuView {
    /*
     * 负责人: 刘航宇
     * 功能: 展示主菜单
     * 内容：
     * 1. 打印标题与主选项（开始/选关/退出）
     * 2. 打印输入提示“请选择: ”
     * 3. 与入口配合：由入口读取输入并分支处理
     * 异常与边界：
     * - 纯输出方法，不读取输入；适用于任何编码的控制台
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showMain() {
        System.out.println("==== 推箱子 ====");
        System.out.println("1. 开始游戏");
        System.out.println("2. 选择关卡");
        System.out.println("3. 退出游戏");
        System.out.println("4. 团队介绍");
        System.out.println("5. 如何游玩");
        System.out.println("6. 设置");
        System.out.print("请选择: ");
    }

    /*
     * 负责人: 余渝
     * 功能: 展示团队介绍
     * 内容：
     * 1. 打印团队名称与成员列表
     * 2. 可扩展：展示职责分工、联系方式
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showTeam() {
        System.out.println("*****************************");
        System.out.println("*****************************");
        System.out.println("           添砖加瓦            ");
        System.out.println("          组长 刘宇航          ");
        System.out.println("         副组长 赵帅尧         ");
        System.out.println("         信息官 王宇晗         ");
        System.out.println("         信息官 于诗鑫         ");
        System.out.println("        产品经理 彭依萍         ");
        System.out.println("        产品经理 夏佳阔         ");
        System.out.println("         技术官 李俊杰         ");
        System.out.println("         技术官 张启亮         ");
        System.out.println("         技术官 余瑜           ");
        System.out.println("*****************************");
        System.out.println("*****************************");
    }

    /*
     * 负责人: 余瑜
     * 功能: 展示玩法说明
     * 内容：
     * 1. 打印操作键位：W/S/A/D 移动，R 重开，Q 返回
     * 2. 规则说明：推箱到目标点，全部到达即胜利
     * 3. 失败条件：无硬性失败；可随时返回或重开
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showHowToPlay() {
        System.out.println("*****************************");
        System.out.println("*****************************");
        System.out.println("         移动:W/S/A/D         ");
        System.out.println("         R 重开，Q 返回        ");
        System.out.println("    推箱到目标点，全部到达即胜利! ");
        System.out.println("     对现状不满意?按R重开局面    ");
        System.out.println("        不想玩了?按Q退出        ");
        System.out.println("*****************************");
        System.out.println("*****************************");
    }

    /*
     * 负责人: 余瑜
     * 功能: 展示设置界面
     * 内容：
     * 1. 打印设置占位文案（敬请期待）
     * 2. 可扩展：难度、符号集、显示刷新方式等
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showSettings() {
        System.out.println("*******************************");
        System.out.println("   设置悔步次数上限(回车保持不变)   ");
        System.out.println("*******************************");
    }

    /*
     * 负责人: 余瑜
     * 功能: 打印返回主菜单提示
     * 内容：
     * 1. 打印提示：“按回车返回主菜单”
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void printReturnHint() {
        System.out.print("按回车返回主菜单");
    }

    /*
     * 负责人: 彭依萍
     * 功能: 打印操作提示
     * 内容：
     * 1. 打印提示：W上，S下，A左，D右，R重新开始，Q返回主菜单
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void printOperationHint() {
        System.out.println("操作提示：W上，S下，A左，D右，R重新开始，Z悔步，Q返回主菜单");
    }

    /*
     * 负责人: 彭依萍
     * 功能: 展示胜利界面
     * 内容：
     * 1. 打印胜利文案：“胜利”
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showVictory() {
        System.out.println("胜利");
    }

    /*
     * 负责人: 余瑜
     * 功能: 展示选关界面
     * 内容：
     * 1. 打印选择范围：1-<total>
     * 2. 打印提示：输入关卡编号，输入 0 返回
     * 3. 与入口配合：由入口读取输入并跳转
     * 参数:
     * - total：关卡总数，用于展示范围
     * 返回值:
     * - 无
     */
    public static void showSelectLevel(int total) {
        System.out.println("******************************");
        for (int i = 1; i <= total; i++) {
            System.out.printf("%d.关卡%d\n", i, i);
        }
        System.out.println("请选择关卡:");
        System.out.println("返回至主菜单请输入0");
        System.out.println("查看排行榜请输入 L+编号 (如 L3)");
        System.out.println("******************************");
    }

    /*
     * 负责人: 刘航宇
     * 功能: 展示悔步确认界面
     * 内容：
     * 1. 打印提示：“确认悔步? 输入 Y 确认 / N 取消”
     * 参数:
     * - 无
     * 返回值:
     * - 无
     */
    public static void showUndoConfirm() {
        System.out.println("确认悔步? 输入 Y 确认 / N 取消");
    }

    /*
     * 负责人: 余瑜
     * 功能: 展示排行榜
     * 内容：
     * 1. 打印排行榜标题：“关卡X 排行榜”
     * 2. 遍历排行榜条目，按步数排序，展示前10名
     * 3. 若排行榜为空，打印“暂无记录”
     * 参数:
     * - levelIndex：关卡索引，用于打印标题
     * - list：排行榜条目列表，按步数排序
     * 返回值:
     * - 无
     */
    public static void showLeaderboard(int levelIndex, java.util.List<model.LeaderboardEntry> list) {
        System.out.println("******************************");
        System.out.printf("关卡%d 排行榜\n", levelIndex + 1);
        if (list == null || list.isEmpty()) {
            System.out.println("暂无记录");
        }
        else {
            int rank = 1;
            for (model.LeaderboardEntry e : list) {
                System.out.printf("%d. %s - 步数:%d - 留言:%s\n", rank++, e.name, e.steps, e.message);
                if (rank > 10) break;
            }
        }
        System.out.println("******************************");
    }
}