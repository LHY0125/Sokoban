package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import model.LeaderboardEntry;

public class Leaderboard {
    // 排行榜目录
    private static Path dir() {
        return Paths.get("rank");
    }

    /*
     * 负责人: 彭依萍
     * 功能: 获取排行榜文件路径
     * 内容：
     * 1. 返回 rank/level{N}.csv，N 为关卡索引 + 1
     * 参数:
     * - levelIndex：关卡索引（0 起）
     * 返回值:
     * - Path：文件路径
     */
    private static Path fileFor(int levelIndex) {
        // 获取根目录，即rank文件夹
        Path rankDir = dir();
        // 根据关卡索引来拼接文件名
        String fileName = String.format("level%d.csv", levelIndex + 1);
        // 根据根目录和文件名，返回排行榜文件路径
        return rankDir.resolve(fileName);
    }

    /*
     * 负责人: 张启亮
     * 功能: 保存单条排行榜记录
     * 内容：
     * 1. 确保目录存在：rank/
     * 2. 以 UTF-8 追加写入文件：level{N}.csv（N=levelIndex+1）
     * 3. 写入一行 CSV：name,steps,message（内部已做文本清洗）
     * 异常与边界：
     * - IO 异常被安全忽略，不影响游戏流程；文件不可写时记录将丢失
     * - 大量写入时建议后续引入去重或限流策略（本方法不处理）
     * 复杂度：时间 O(1) 单条追加，空间 O(1)
     * 参数:
     * - levelIndex：关卡索引（0 起）
     * - name：昵称
     * - steps：步数
     * - message：留言
     * 返回值:
     * - 无
     */
    public static void save(int levelIndex, String name, int steps, String message) {
        try {
            Files.createDirectories(dir());
            String n = sanitize(name);
            String m = sanitize(message);
            Path f = fileFor(levelIndex);
            try (BufferedWriter bw = Files.newBufferedWriter(
                    f,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                bw.write(n + "," + steps + "," + m);
                bw.newLine();
            }
        } catch (Exception ignored) {
        }
    }

    /*
     * 负责人: 王宇晗
     * 功能: 读取并按步数升序排序
     * 内容：
     * 1. 按行读取 CSV，解析三列：name,steps,message（仅当列数=3时有效）
     * 2. 对 `steps` 解析失败的行直接忽略，保证数据健壮性
     * 3. 使用升序排序（步数越少越靠前）
     * 异常与边界：
     * - 文件不存在或无法读取会返回空列表
     * - 不做去重与合并，可能存在同名多条记录
     * 复杂度：时间 O(N log N)，空间 O(N)
     * 参数:
     * - levelIndex：关卡索引（0 起）
     * 返回值:
     * - List<LeaderboardEntry>：升序列表
     */
    public static List<LeaderboardEntry> read(int levelIndex) {
        List<LeaderboardEntry> list = new ArrayList<>();
        Path f = fileFor(levelIndex);
        try (BufferedReader br = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    try {
                        int steps = Integer.parseInt(parts[1].trim());
                        list.add(new LeaderboardEntry(parts[0], steps, parts[2]));
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        list.sort((a, b) -> Integer.compare(a.steps, b.steps));

        return list;
    }

    /*
     * 负责人: 于诗鑫
     * 功能: 读取 TopN
     * 内容：
     * 1. 基于已排序结果返回前 N 条记录；当总数不足 N 时返回全部
     * 2. 保持原有顺序（升序），不做额外处理
     * 异常与边界：
     * - limit < 0 视为 0（调用方应保证传入合法）
     * 复杂度：时间 O(N)，空间 O(N)（子列表拷贝）
     * 参数:
     * - levelIndex：关卡索引（0 起）
     * - limit：返回条数上限（0 起）
     * 返回值:
     * - List<LeaderboardEntry>：最多 N 条记录
     */
    public static List<LeaderboardEntry> readTop(int levelIndex, int limit) {
        List<LeaderboardEntry> list = read(levelIndex);
        if (limit < 0) {
            return new ArrayList<>();
        }
        return list.size() > limit ? new ArrayList<>(list.subList(0, limit)) : list;
    }

    /*
     * 负责人: 赵帅尧
     * 功能: 文本清洗
     * 内容：
     * 1. 将换行符（\n/\r）替换为空格，避免破坏 CSV 行结构
     * 2. 将半角逗号替换为中文逗号，避免与列分隔符冲突
     * 异常与边界：
     * - 传入 null 返回空字符串
     * - 不处理引号与转义，CSV 仍采用最简格式
     * 复杂度：时间 O(N)，空间 O(N)（新字符串分配）
     * 参数:
     * - s：原始字符串
     * 返回值:
     * - String：清洗后的字符串
     */
    private static String sanitize(String s) {
        if (s == null) return "";
        StringBuilder result = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\n' || c == '\r') {
                result.append(' ');
            } else if (c == ',') {
                result.append('，');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}