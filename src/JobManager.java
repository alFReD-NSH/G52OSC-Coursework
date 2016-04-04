import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * It will contain a list of jobs and this class is in charge running a job so it will
 * make the jobs properly update the statistics.
 */
public class JobManager extends Statistics {
    private final static Logger LOGGER = Logger.getLogger(JobManager.class.getName());
    private List<Job> jobs;
    private OnJobRunCallback onJobRunCallback;
    private V2_AsciiTableRenderer rend;

    public JobManager(V2_AsciiTableRenderer rend) {
        this.rend = rend;
    }

    public JobManager() {

    }


    interface OnJobRunCallback {
        void call(Job job, int processedTicks, int maxTicks);
    }

    public JobManager generateRandomJobs(int number, int maxDuration, int maxPriority) {
        if (jobs == null) {
            jobs = new ArrayList<>();
        }
        for (int i = 0; i < number; i++) {
            jobs.add(new Job(
                    (int) (Math.random() * maxDuration) + 1,
                    (int) (Math.random() * maxPriority) + 1));
        }
        setJobs(jobs);
        return this;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        jobs.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
    }

    /**
     * Will run a given job with a specified amount of max ticks.
     * @param job
     * @param maxTicks
     * @return returns the number of ticks that was processed.
     */
    public int runJob(Job job, int maxTicks) {
        LOGGER.setLevel(Level.FINER);
        int processed = job.run(maxTicks);
        for (Job j : jobs) {
            if (j != job) {
                j.onWait(processed);
            }
        }
        if (onJobRunCallback != null) {
            onJobRunCallback.call(job, processed, maxTicks);
        }
        LOGGER.fine("Job " + job.getId() + " ran for " + processed + " ticks");

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

    @Override
    public void reset() {
        jobs.forEach(Job::reset);
    }

    public List<Result> getResults(String name) {
        return jobs.stream()
                .map(job -> job.getResult(name))
                .collect(Collectors.toList());
    }
}
