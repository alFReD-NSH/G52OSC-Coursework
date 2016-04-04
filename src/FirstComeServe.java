public class FirstComeServe extends Algorithm {
    public FirstComeServe(JobManager jobManager) {
        super(jobManager);
    }

    public void run() {
        for (Job job : jobManager.getJobs()) {
            jobManager.runJob(job, Integer.MAX_VALUE);
        }
    }
}
