import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ChartCreator {
    private double width;

    public ChartCreator(int width) {
        this.width = width;
    }

    public void print(List<Entry> entries) {
        StringBuilder top    = new StringBuilder("|");
        StringBuilder middle = new StringBuilder("|");
        StringBuilder bottom = new StringBuilder("|");

        int biggestText = 0;
        int smallestWidth = Integer.MAX_VALUE;
        double totalTicks = 0;
        for (ChartCreator.Entry entry : entries) {
            biggestText = Math.max(biggestText, entry.text.length());
            smallestWidth = Math.min(smallestWidth, entry.width);
            totalTicks += entry.width;
        }
        double cellInChars = Math.max(smallestWidth, biggestText);
        double cellTicks = smallestWidth;
        for (Entry entry : entries) {
            int chars = (int) Math.round(((double) entry.width) *
                    cellInChars / cellTicks);
            assert entry.text.length() <= chars;
            System.out.println(chars);
            for (int i = 0; i < (chars + 1); i++) {
                top.append("─");
                bottom.append("─");
            }
            middle.append(StringUtils.center(entry.text, chars))
                  .append("|");
            System.out.println(StringUtils.center(entry.text, chars).length());
        }
        do {
            int len = Math.min(top.length(), (int) width);
            System.out.println(top.substring(0, len));
            top.delete(0, len);
            System.out.println(middle.substring(0, len));
            middle.delete(0, len);
            System.out.println(bottom.substring(0, len));
            bottom.delete(0, len);
        } while (top.length() > 0);
    }

    public static class Entry {
        String text;
        int width;

        public Entry(String text, int width) {
            this.text = text;
            this.width = width;
        }
    }
}
