# 推箱子（Sokoban）控制台版

一个基于 Java 的控制台推箱子小游戏，支持关卡选择、WASD 操作、目标点统计与屏幕刷新。项目以简化分层组织：入口（主循环）+ 视图 + 服务 + 模型 + 工具。

## 目录结构
- `src/`
  - `SokobanApp.java` 程序入口与主循环
  - `model/` 游戏数据模型（`GameState`、`Position`、`TileType`）
  - `service/` 核心逻辑（`LevelLoader`、`MoveValidator`、`GameEngine`）
  - `view/` 控制台渲染与菜单（`ConsoleGameView`、`ConsoleMenuView`）
  - `util/` 渲染字符映射（`Renderer`）
- `map/` 关卡文件（`level1.txt`、`level2.txt` ...）
- `LICENSE` MIT 许可证

## 快速开始（Windows）
1) 安装并配置 JDK（建议 17+），确保 `java`、`javac` 可用。
2) 在项目根目录编译：
   ```powershell
   mkdir bin
   javac -encoding UTF-8 -d bin src\model\*.java src\service\*.java src\util\*.java src\view\*.java src\SokobanApp.java
   ```
3) 运行：
   ```powershell
   java -cp bin SokobanApp
   ```
4) 如控制台出现乱码或不能显示 Unicode：
   - CMD 执行：`chcp 65001`
   - 或运行时添加：`java -Dfile.encoding=UTF-8 -cp bin SokobanApp`

## 操作说明
- 主菜单
  - `1` 开始游戏（第 1 关）
  - `2` 选择关卡
  - `3` 退出
- 游戏内
  - `W` 上、`S` 下、`A` 左、`D` 右
  - `R` 重新开始当前关卡
  - `Q` 返回主菜单

## 关卡文件规范
- 路径与命名：`map/level{N}.txt`（例如 `map/level3.txt`）
- 行宽一致：所有行长度必须一致，用于构建矩阵
- 字符约定：
  - `#` 墙
  - `○` 目标点
  - `■` 箱子
  - `☑` 箱子在目标点上
  - `☺` 玩家
  - `空格` 空地
- 示例：
  ```
  ##########
  #☺       #
  #  ■     #
  #  ○     #
  #        #
  #     ■  #
  #     ○  #
  #        #
  #        #
  ##########
  ```

## 主要代码说明
- `SokobanApp` 程序入口与主循环
  - 菜单与选关、循环读取操作、调用校验与移动、胜利判断与渲染
- `service/LevelLoader`
  - 统计关卡数量、读取并解析关卡文件、构造 `GameState`
  - 缺失文件时使用示例关卡回退
- `service/MoveValidator`
  - 校验移动合法性（撞墙、推箱是否可行、越界判断）
- `service/GameEngine`
  - 执行移动与箱子推进，维护步数并判断胜利
- `view/ConsoleGameView`
  - 清屏与状态栏、矩阵渲染、胜利界面
- `util/Renderer`
  - 按基础层/动态层输出字符（优先显示动态层）

## 屏幕刷新与编码
- 清屏/置顶光标（ANSI 序列）：`ESC[2J`、`ESC[3J`、`ESC[H`
- 控制台需支持 UTF-8 与 Unicode 字形；不支持时可将玩家字符改为 `@` 等 ASCII。

## 扩展与开发分工
- 注释采用“负责人/功能/内容/异常与边界/参数/返回值”的详细风格，便于多人协作。
- 新增关卡：在 `map/` 放入 `level{N}.txt`，系统自动统计总关卡数。
- 可扩展项：
  - 增加设置项（难度、符号集、刷新策略）
  - 丰富菜单（玩法说明、团队信息）

## 许可证
- 本项目采用 MIT 许可证，详情见 `LICENSE`。