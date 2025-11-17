<#
更新 Sokoban 应用的分布文件

功能流程：
- 编译 Java 源码到 `build`
- 生成 `dist\Sokoban.jar`
- 可选：覆盖安装目录 `Sokoban\app\Sokoban.jar`
- 调用 NSIS/Inno 生成安装包到 `installer\dist`

使用：
powershell -ExecutionPolicy Bypass -File .\update_dist.ps1 [-NoInstallCopy]
#>
param([switch]$NoInstallCopy)
# 编译 Java 源文件
$ErrorActionPreference='Stop'
$buildDir='build'
$distJar='dist\Sokoban.jar'
$installedJar='dist\app\Sokoban\app\Sokoban.jar'
# 定义 NSIS 和 Inno Setup 编译工具路径
$nsis='D:\Program Files (x86)\NSIS\makensis.exe'
$iscc='D:\Program Files (x86)\Inno Setup 6\ISCC.exe'
# 清理旧的构建目录
if (Test-Path $buildDir) { 
    Remove-Item -Recurse -Force $buildDir 
}
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
# 查找所有 Java 源文件（排除 view_synced 目录）
$srcFiles=Get-ChildItem -Recurse -Path 'src' -Filter '*.java' | Where-Object { $_.FullName -notlike '*\view_synced\*' }
if ($srcFiles.Count -eq 0) { 
    throw 'No Java sources found' 
}
# 编译 Java 源文件
& javac --release 17 -encoding UTF-8 -d $buildDir $($srcFiles | ForEach-Object { $_.FullName })
if (!(Test-Path 'dist')) { 
    New-Item -ItemType Directory -Force -Path 'dist' | Out-Null 
}
# 创建 JAR 文件
& jar cf $distJar -C $buildDir .
if (-not $NoInstallCopy) { 
    $installedDir = Split-Path -Parent $installedJar
    if (!(Test-Path $installedDir)) {
        New-Item -ItemType Directory -Force -Path $installedDir | Out-Null
    }
    Copy-Item -Force $distJar $installedJar 
}
# 打包 NSIS 安装程序
& $nsis 'installer\installer.nsi'
if ($LASTEXITCODE -ne 0) { 
    throw "NSIS packaging failed: $LASTEXITCODE" 
}
# 打包 Inno Setup 安装程序
& $iscc 'installer\installer.iss'
if ($LASTEXITCODE -ne 0) { 
    throw "Inno packaging failed: $LASTEXITCODE" 
}
# 显示成功消息
Write-Host "Updated $distJar and installers created in installer\dist" -ForegroundColor Green