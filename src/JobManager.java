import java.util.List;

/**
 * It will contain a list of jobs and this class is in charge running a job so it will
 * make the jobs properly update the statistics.
 */
public class JobManager implements Statistics {
    public List<Job> jobs;

    public JobManager generateRandomJobs(int number, int maxDuration, int maxPriority) {
        for (int i = 0; i < number; i++) {
            jobs.add(new Job(
                    (int) (Math.random() * maxDuration),
                    (int) (Math.random() * maxPriority)));
        }
        return this;
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
        return processed;
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
