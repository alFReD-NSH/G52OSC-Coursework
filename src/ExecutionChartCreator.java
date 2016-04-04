import java.util.ArrayList;

public class ExecutionChartCreator implements JobManager.OnJobRunCallback {
    private int width;
    ArrayList<ChartCreator.Entry> entries = new ArrayList<>();


    public ExecutionChartCreator(int width) {
        this.width = width;
    }


    @Override
    public void call(Job job, int processedTicks, int maxTicks) {
        String text = "Job " + job.getId() + ", " + processedTicks + " Ticks";
        entries.add(new ChartCreator.Entry(text, processedTicks));
    }

    public void reset() {
        entries = new ArrayList<>();
    }

    public void print() {
        new ChartCreator(width).print(entries);
    }
}
