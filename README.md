# 推箱子（Sokoban）控制台版

一个基于 Java 的控制台推箱子小游戏，支持关卡选择、WASD 操作、目标点统计与屏幕刷新。项目以简化分层组织：入口（主循环）+ 视图 + 服务 + 模型 + 工具。

## 目录结构
- `src/`
  - `SokobanApp.java` 程序入口与主循环
  - `model/` 游戏数据模型（`GameState`、`Position`、`TileType`）
  - `service/` 核心逻辑（`LevelLoader`、`MoveValidator`、`GameEngine`）
  - `view/` 控制台渲染与菜单（`ConsoleGameView`、`ConsoleMenuView`）
- `util/` 控制台编码初始化（`ConsoleEncoding`）、渲染字符映射（`Renderer`）
- `map/` 关卡文件（`level1.txt`、`level2.txt` ...）
- `installer/` 安装脚本（`installer.iss`、`installer.nsi`）
- `update_dist.ps1` 一键更新脚本（编译→覆盖→打包）
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
   java -Dfile.encoding=UTF-8 -cp bin SokobanApp
   ```
4) 编码说明：程序会自动检测控制台代码页（UTF-8/GBK）并设置输出/输入编码，一般无需手动切到 UTF-8。
   - 如仍出现乱码，可手动：CMD `chcp 65001` 或 PowerShell `[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new()`

## 操作说明
- 主菜单
  - `1` 开始游戏（进入第一个未通关关卡）
  - `2` 选择关卡
  - `3` 退出
  - `4` 团队介绍
  - `5` 如何游玩
  - `6` 设置
- 游戏内
  - `W` 上、`S` 下、`A` 左、`D` 右
  - `R` 重新开始当前关卡
  - `Q` 返回主菜单

## 关卡文件规范
- 路径与命名：`map/level{N}.txt`（例如 `map/level3.txt`）
- 行宽一致：所有行长度必须一致，用于构建矩阵
- 地图大小不受 10×10 限制：只需保证每行长度一致即可
- 字符约定：
  - `#` 墙
  - `○` 目标点
  - `■` 箱子
  - `☑` 箱子在目标点上
  - `☺` 玩家
  - `空格` 空地
- 文件编码：请使用 UTF-8 保存关卡文件以避免 Unicode 字符读取异常
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
 - `util/ConsoleEncoding`
   - 检测 Windows 控制台代码页并设置输出/输入编码，返回编码供 `Scanner` 使用

## 打包发布（Windows）
- 当前安装包版本：`2.0.0`

推荐使用一键脚本：

```powershell
powershell -ExecutionPolicy Bypass -File .\update_dist.ps1 [-NoInstallCopy]
```

- 行为：编译源码（Java 17）→ 生成 `dist\Sokoban.jar` → 可选覆盖 `Sokoban\app\Sokoban.jar` → 运行 NSIS/Inno 生成安装包到 `installer\dist`
- 依赖：
  - NSIS：`D:\Program Files (x86)\NSIS\makensis.exe`
  - Inno Setup：`D:\Program Files (x86)\Inno Setup 6\ISCC.exe`
- 可选参数：`-NoInstallCopy` 跳过覆盖已安装目录的 JAR
- 手动方式仍可参考下述命令：
1) 生成可运行 JAR（需有 `build/manifest.mf` 指定 `Main-Class: SokobanApp`）：
   ```powershell
   jar --create --file dist\Sokoban.jar --manifest build\manifest.mf -C bin .
   ```
2) 生成便携应用镜像（内置 JRE，UTF-8 输出）：
   ```powershell
   jpackage --input dist --name Sokoban --main-jar Sokoban.jar --main-class SokobanApp --win-console --type app-image --dest dist\app --java-options "-Dfile.encoding=UTF-8"
   ```
   - 运行：`dist\app\Sokoban\Sokoban.exe`
3) 生成安装包：
   - Inno Setup：
     ```powershell
     iscc installer\installer.iss
     ```
     输出目录：`installer\dist`
   - NSIS：
     ```powershell
     makensis installer\installer.nsi
     ```
     输出目录：`installer\dist`

## 屏幕刷新与编码
- 清屏/置顶光标（ANSI 序列）：`ESC[2J`、`ESC[3J`、`ESC[H`
 - 程序启动时自动适配 UTF-8/GBK；若终端字体不支持 Unicode，可将玩家字符改为 `@` 等 ASCII。

## 常见问题
- 中文/图形字符显示为 `?`：程序已自动检测并设置编码；如仍异常，手动切到 UTF-8，并使用支持 Unicode 的终端字体（如 Segoe UI Symbol/Consolas/Cascadia Mono）。

## 扩展与开发分工
- 注释采用“负责人/功能/内容/异常与边界/参数/返回值”的详细风格，便于多人协作。
- 新增关卡：在 `map/` 放入 `level{N}.txt`，系统自动统计总关卡数。
- 可扩展项：
  - 增加设置项（难度、符号集、刷新策略）
  - 丰富菜单（玩法说明、团队信息）

## 许可证
- 本项目采用 MIT 许可证，详情见 `LICENSE`。