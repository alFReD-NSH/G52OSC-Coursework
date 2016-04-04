import java.util.List;

public class Utils {
    /**
     * @param items
     * @param <T>
     * @return a random item from the given list
     */
    public static <T> T sample(List<T> items) {
        return items.get((int) (Math.random() * items.size()));
    }
}
