import java.util.List;

/**
 * It will contain a list of jobs and this class is in charge running a job so it will
 * make the jobs properly update the statistics.
 */
public class JobManager implements Statistics {
    public List<Job> jobs;

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
        return processed;
    }

    /**
     * @return the average waiting time of the jobs
     */
    public double getWaitingTime() {
        return jobs.stream().mapToDouble(job -> job.getWaitingTime()).sum() / jobs.size();
    }
    /**
     * @return the average turn around time of the jobs
     */
    public double getTurnAroundTime() {
        return jobs.stream().mapToDouble(job -> job.getTurnAroundTime()).sum() / jobs.size();
    }

    public boolean isFinished() {
        return jobs.stream().allMatch(job -> job.isFinished());
    }
}
