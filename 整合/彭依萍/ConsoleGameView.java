package view;

import model.GameState;
import util.Renderer;

public class ConsoleGameView {
    /*
        负责人: 彭依萍
        功能: 渲染当前游戏状态到控制台
        内容：
        1. 清屏并将光标置顶：ESC[2J ESC[3J ESC[H]
        2. 打印状态栏：当前关卡/总关卡、剩余目标点、步数
        3. 遍历行列，根据 `Renderer.toChar` 输出地图
        4. 打印操作提示：W/S/A/D，R，Q
        异常与边界：
        - 状态为空不渲染；行列尺寸必须一致
        - 控制台需支持 UTF-8 与 Unicode 字符显示
        参数:
        - state：当前游戏状态
        返回值:
        - 无
    */
    /*
     * 清屏字符串多了一个 ] 字符，应为 "\u001B[2J\u001B[3J\u001B[H"(42)
     * 剩余目标点计算错误： base[i][j] == 3 && base[i][j] != 5 永远等价于 base[i][j] == 3 ，且未扣除 map中的 BOX_ON_GOAL(5)(48-55)
     */
    public static void render(GameState state) {
        //异常与边界处理：
        //若状态为空不渲染
        if(state == null){
            return;
        }
        //校验地图行列尺寸是否一致，不一致则不渲染
        int[][] base = state.base;
        int[][] map = state.map;
        if(base == null || map == null
                || base.length == 0 || map.length == 0 || base[0] ==  null || map[0] == null
                || base.length != map.length
                || base[0].length != map[0].length){
        return;
        }
         //获取基础层地图布局的行数和列数
        int rowCount = base.length;
        int colCount = base[0].length;
        //清屏并将光标置顶
        System.out.println("\u001B[2J\u001B[3J\u001B[H]");
        //打印状态栏：当前关卡/总关卡、剩余目标点、步数
        //先计算剩余目标点remainingTargets
        int remainingTargets = 0;
        for(int i = 0; i<rowCount; i++){
            for(int j = 0; j<colCount; j++){
                if(base[i][j] == 3 && base[i][j] != 5){
                    remainingTargets++;
                }
            }
        }
        //然后打印状态栏：当前关卡/总关卡、剩余目标点、步数
        System.out.printf("当前关卡:%d/总关卡:%d | 剩余目标点:%d | 步数:%d",
                state.levelIndex,state.totalLevels,remainingTargets,state.steps);
        //遍历行列，根据 `Renderer.toChar` 输出地图
        for(int i = 0; i<rowCount; i++){
            for(int j = 0; j<colCount; j++){
                char newRenderToChar = Renderer.toChar(base[i][j], map[i][j]);
                System.out.print(newRenderToChar);
            }
            //每打印完一行地图就换行
            System.out.println();
        }
        //打印操作提示：W/S/A/D，R，Q
        System.out.println("操作提示：W上，S下，A左，D右，R重新开始，Q返回主菜单");
    }

    /*
        负责人: 刘航宇
        功能: 显示胜利界面并提示返回主菜单
        内容：
        1. 清屏并置顶光标
        2. 输出“胜利”提示
        参数:
        - 无
        返回值:
        - 无
    */
    /*
     * \u001B 是 ESC 字符（ANSI 控制序列的起始）
     * ESC[2J 清除当前屏幕内容
     * ESC[3J 清除滚动缓冲区（历史输出），避免上下滚动时仍看到旧内容
     * ESC[H 将光标移动到屏幕左上角（行1列1）
     */
    public static void win() {
        System.out.print("\u001B[2J\u001B[3J\u001B[H");
        System.out.println("胜利");
    }

    /*
        负责人: 
        功能: 计算剩余目标点数量
        内容：
        1. 遍历地图，统计 base 为目标点的格数
        2. 减去 map 中已完成（箱子在目标）格数
        3. 返回剩余数量
        参数:
        - state：当前游戏状态
        返回值:
        - int：剩余目标点数量
    */
    private static int remainingGoals(GameState state) {
        
        return 1;
    }
}