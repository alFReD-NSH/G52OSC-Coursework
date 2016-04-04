import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class BarChartCreator {
    private int width;

    public BarChartCreator(int width) {
        this.width = width;
    }

    public void print(Map<String, Double> map) {
        int maxKey = map.keySet().stream().mapToInt(String::length).max().getAsInt();
        double maxLen =
                map.values().stream().mapToDouble(value -> value).max().getAsDouble();
        int rest = width - maxKey;
        map.forEach((key, length) -> {
            System.out.print(StringUtils.rightPad(key, maxKey) + " | ");
            int barWidth = (int) Math.round(length / maxLen * rest);
            for (int i = 0; i < barWidth; i++) {
                System.out.print("#");
            }
            System.out.println();
        });
    }
}
