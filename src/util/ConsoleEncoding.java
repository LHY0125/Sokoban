package util;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public final class ConsoleEncoding {
    private ConsoleEncoding() {
    }
    
    /**
     * @brief 初始化控制台编码
     * @details 1. 调用 `detect()` 方法检测系统默认文件编码
     *          2. 设置系统输出流 `System.out` 和 `System.err` 的编码为检测到的编码
     * @return 系统默认文件编码（GBK 或 UTF-8）
     */
    public static String init() {
        // 检测系统默认文件编码
        String enc = detect();
        try {
            // 设置系统输出流和错误流的编码
            PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true, enc);
            PrintStream err = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.err)), true, enc);
            System.setOut(out);
            System.setErr(err);
        } catch (Exception ignored) {
        }
        return enc;
    }

    /**
     * @brief 检测并返回系统默认文件编码
     * @details 1. 检查系统属性 `file.encoding`，如果是 GBK 或 UTF-8 则直接返回
     *          2. 否则执行 `chcp` 命令获取当前控制台编码，判断是否为 GBK 或 UTF-8
     *          3. 如果都不是，则返回 UTF-8 作为默认编码
     * @return 系统默认文件编码（GBK 或 UTF-8）
     */
    public static String detect() {
        // 检查系统属性 `file.encoding`，如果是 GBK 或 UTF-8 则直接返回
        String enc = System.getProperty("file.encoding");
        try {
            // 执行 `chcp` 命令获取当前控制台编码
            Process p = new ProcessBuilder("cmd.exe", "/c", "chcp").redirectErrorStream(true).start();
            p.waitFor();
            // 解析 `chcp` 命令输出，判断是否为 GBK 或 UTF-8
            String out = new String(p.getInputStream().readAllBytes());
            if (out != null) {
                String u = out.toUpperCase();
                if (u.contains("65001") || u.contains("UTF-8")) return "UTF-8";
                if (u.contains("936") || u.contains("GBK")) return "GBK";
            }
        } catch (Exception ignored) {
        }

        // 如果都不是，则返回 UTF-8 作为默认编码
        if (enc != null && !enc.isEmpty()) {
            return enc;
        }
        return "UTF-8";
    }
}