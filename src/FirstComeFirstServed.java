public class FirstComeFirstServed extends Algorithm {
    public FirstComeFirstServed(JobManager jobManager) {
        super(jobManager);
    }

    public void run() {
        for (Job job : jobManager.getJobs()) {
            jobManager.runJob(job, Integer.MAX_VALUE);
        }
    }

    @Override
    public String getName() {
        return "first-come-first-served";
    }
}
