import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * It will contain a list of jobs and this class is in charge running a job so it will
 * make the jobs properly update the statistics.
 */
public class JobManager implements Statistics {
    private final static Logger LOGGER = Logger.getLogger(JobManager.class.getName());
    private List<Job> jobs;
    private OnJobRunCallback onJobRunCallback;
    interface OnJobRunCallback {
        void call(Job job, int processedTicks, int maxTicks);
    }

    public JobManager generateRandomJobs(int number, int maxDuration, int maxPriority) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        for (int i = 0; i < number; i++) {
            jobs.add(new Job(
                    (int) (Math.random() * maxDuration),
                    (int) (Math.random() * maxPriority)));
        }
        return this;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        jobs.sort((o1, o2) -> o1.getPriority() - o2.getPriority());
    }

    /**
     * Will run a given job with a specified amount of max ticks.
     * @param job
     * @param maxTicks
     * @return returns the number of ticks that was processed.
     */
    public int runJob(Job job, int maxTicks) {
        int processed = job.run(maxTicks);
        for (Job j : jobs) {
            if (j != job) {
                j.onWait(processed);
            }
        }
        if (onJobRunCallback != null) {
            onJobRunCallback.call(job, processed, maxTicks);
        }

        return processed;
    }

    public void setOnJobRunCallback(OnJobRunCallback onJobRunCallback) {
        this.onJobRunCallback = onJobRunCallback;
    }

    /**
     * @return the average waiting time of the jobs
     */
    @Override
    public double getWaitingTime() {
        return jobs.stream().mapToDouble(job -> job.getWaitingTime()).sum() / jobs.size();
    }

    /**
     * @return the average turn around time of the jobs
     */
    @Override
    public double getTurnAroundTime() {
        return jobs.stream().mapToDouble(job -> job.getTurnAroundTime()).sum() / jobs.size();
    }

    @Override
    public boolean isFinished() {
        return jobs.stream().allMatch(job -> job.isFinished());
    }


    public double getCurrentCPUUsage() {
        return 100;
    }

    /**
     * @return the average of total cpu usage of each job.
     */
    @Override
    public double getTotalCPUUsage() {
        return jobs.stream().mapToDouble(job -> job.getTotalCPUUsage()).sum() / jobs.size();
    }
}
