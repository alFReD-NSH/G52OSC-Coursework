import java.util.ArrayList;

public class ShortestJobFirst extends Algorithm {
    public ShortestJobFirst(JobManager jobManager) {
        super(jobManager);
    }

    @Override
    public void run() {
        ArrayList<Job> jobs = new ArrayList<>(jobManager.getJobs());
        jobs.sort((o1, o2) -> o1.getDuration() - o2.getDuration());
        for (Job job : jobs) {
            jobManager.runJob(job, Integer.MAX_VALUE);
        }
    }

    @Override
    public String getName() {
        return "shortest-job-first";
    }
}
