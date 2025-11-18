import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Save {

    /**
     * 负责人：张启亮
     * 功能：保存单条排行记录到CSV文件
     * 内容：
     * 1. 确保存储目录 "rank/" 存在，不存在则自动创建
     * 2. 以 UTF-8 追加写入关卡文件 level{N}.csv（N=levelIndex+1）
     * 3. 写入格式：name,steps,message（已做文本清洗）
     * 异常与边界：
     * - IO 异常被忽略，记录丢失不影响游戏流程
     * - 不处理去重/限流，建议后续扩展
     * 复杂度：时间 O(1)，空间 O(1)
     * 参数：
     * - levelIndex：关卡索引（0 起）
     * - name：玩家昵称
     * - steps：完成步数
     * - message：玩家留言
     * 返回值：无
     */
    public void save(int levelIndex, String name, int steps, String message) {
        // 1. 确保 rank 目录存在
        Path rankDir = Paths.get("rank");
        try {
            Files.createDirectories(rankDir);
        } catch (IOException e) {
            return;
        }

        // 2. 生成目标 CSV 文件路径
        Path targetFile = rankDir.resolve(String.format("level%d.csv", levelIndex + 1));

        // 3. 文本清洗（处理换行符和逗号）
        String cleanName = (name == null ? "" : name.replaceAll("[\n\r]", " ").replace(",", "，"));
        String cleanMessage = (message == null ? "" : message.replaceAll("[\n\r]", " ").replace(",", "，"));

        // 4. 构建并写入 CSV 行
        String csvLine = String.format("%s,%d,%s", cleanName, steps, cleanMessage);
        try (BufferedWriter writer = Files.newBufferedWriter(
                targetFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(csvLine);
            writer.newLine();
        } catch (IOException e) {
            // 无需 return，方法自然结束
        }
    }

    // 测试方法：解决“方法从未使用”的提示（实际项目中可删除）
    public static void main(String[] args) {
        Save saver = new Save();
        saver.save(0, "张启亮", 50, "测试保存功能");
    }
}