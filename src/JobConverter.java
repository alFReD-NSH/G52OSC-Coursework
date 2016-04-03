import com.beust.jcommander.IStringConverter;

public class JobConverter implements IStringConverter<Job> {
    @Override
    public Job convert(String value) {
        String[] s = value.split(":");
        int duration = Integer.parseInt(s[0]);
        int priority = (s.length == 1) ? 1 : Integer.parseInt(s[1]);
        return new Job(duration, priority);
    }
}
