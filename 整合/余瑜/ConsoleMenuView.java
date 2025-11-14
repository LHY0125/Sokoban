
//package view;
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
        System.out.print("请选择: ");
    }

    /*
     * 负责人: 余瑜
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
        System.out.println("*******************************");
        System.out.println("*******************************");
        System.out.println("            敬请期待!            ");
        System.out.println("*******************************");
        System.out.println("*******************************");
        System.out.println("*******************************");
    }

    /*
     * 负责人: 余瑜
     * 功能: 展示选关界面
     * 内容：
     * 1. 打印选择范围：1-<total>
     * 2. 打印提示：输入关卡编号，输入 0 返回
     * 参数:
     * - total：关卡总数，用于展示范围
     * 返回值:
     * - 无
     */
    public static void showSelectLevel(int total)  {
        System.out.println("******************************");
        System.out.println("           请选择关卡:          ");
        System.out.println("            1.关卡一           ");
        System.out.println("            2.关卡二           ");
        System.out.println("            3.关卡三           ");
        System.out.println("            4.关卡四           ");
        System.out.println("            5.关卡五           ");
        System.out.println("        返回至主菜单请输入0       ");
        System.out.println("******************************");
    }
}
