package service;

import model.GameState;
import model.TileType;
import model.Position;
import service.MoveValidator;

import static service.MoveValidator.canMove;

public class GameEngine {
    /*
     * 负责人: 
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
        boolean ysxmove =canMove(state,direction);
        //往前一步的坐标ysxp
        Position ysxp = new Position(0,0);
        //往前二步的坐标ysxp2
        Position ysxp2 = new Position(0,0);
        //创建一个新的GameState便于直接替换state
        GameState ysxstate = new GameState;
        ysxstate = state;
        if (ysxmove==false) {}
        if (ysxmove==true) {
            //根据direction确定判定的二维坐标
            switch(direction) {
                case 0:
                    ysxp.x=state.player.x-1;
                    ysxp2.x=state.player.x-1-1;
                    ysxp.y=state.player.y;
                    ysxp2.y=state.player.y;
                    break;
                case 1:
                    ysxp.x=state.player.x+1;
                    ysxp2.x=state.player.x+1+1;
                    ysxp.y=state.player.y;
                    ysxp2.y=state.player.y;
                    break;
                case 2:
                    ysxp.x=state.player.x;
                    ysxp2.x=state.player.x;
                    ysxp.y=state.player.y-1;
                    ysxp2.y=state.player.y-1-1;
                    break;
                case 3:
                    ysxp.x=state.player.x;
                    ysxp2.x=state.player.x;
                    ysxp.y=state.player.y+1;
                    ysxp2.y=state.player.y+1+1;
                    break;
            }

            //分波次:判断有没有箱子
            //有箱子但在目标点上
            if (state.map[ysxp.x][ysxp.y]==5) {
                //move player
                ysxstate.player = ysxp;
                ysxstate.map[ysxp.x][ysxp.y]=4;
                ysxstate.map[state.player.x][state.player.y]=0;
                ysxstate.map[ysxp2.x][ysxp2.y]=2;
            //有箱子但不在目标点，要判断移动后箱子会不会到目标点上
            }else if (state.map[ysxp.x][ysxp.y]==2) {
                //move player
                ysxstate.player = ysxp;
                ysxstate.map[ysxp.x][ysxp.y]=4;
                ysxstate.map[state.player.x][state.player.y]=0;
                //如果移动箱子后会到目标点
                if(ysxstate.base[ysxp2.x][ysxp2.y]==3){
                    ysxstate.map[ysxp2.x][ysxp2.y]=5;
                //如果移动箱子后不会到目标点
                }else ysxstate.map[ysxp2.x][ysxp2.y]=2;

            //没有箱子，但已经过Validator方法判断可移动，则直接移动玩家坐标
            }else{ysxstate.player = ysxp;
                ysxstate.map[ysxp.x][ysxp.y]=4;
                ysxstate.map[state.player.x][state.player.y]=0;
            }
            ysxstate.steps++;
            state = ysxstate;
            return true;
            }
        return false;
        }


        return true;
    }

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
    public static boolean isWin(GameState state) {
        
        return true;
    }
}