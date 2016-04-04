import java.util.ArrayList;

public class ExecutionChartCreator implements JobManager.OnJobRunCallback {
    private int width;
    private boolean verbose;
    ArrayList<GanttChartCreator.Entry> entries = new ArrayList<>();


    public ExecutionChartCreator(int width, boolean verbose) {
        this.width = width;
        this.verbose = verbose;
    }


    @Override
    public void call(Job job, int processedTicks, int maxTicks) {
        String text = verbose ?
                "Job " + job.getId() + ", " + processedTicks + " Ticks" :
                job.getId() + "";
        entries.add(new GanttChartCreator.Entry(text, processedTicks));
    }

    public void reset() {
        entries = new ArrayList<>();
    }

    public void print() {
        System.out.println("Job Execution Chart:");
        new GanttChartCreator(width).print(entries);
    }
}
