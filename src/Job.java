public class Job {
    private int duration;
    private int remaining;
    private int waitingTime;

    public Job(int duration) {
        this.duration = duration;
        remaining = duration;
    }

    /**
     * Called when the scheduler decides it's time for this job to be running for a
     * specified number of ticks
     * @return the number of ticks to be processed.
     */
    public int run(int maxTicks) {
        if (remaining < maxTicks) {
            maxTicks = remaining;
        }
        remaining -= maxTicks;
        return maxTicks;
    }

    /**
     * Must be called when the job was waiting for a certain number of ticks
     * @return the number of ticks to be processed.
     */
    public void onWait(int ticks) {
        if (remaining != 0) {
            waitingTime += ticks;
        }
    }

    public int getDuration() {
        return duration;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getTurnaroundTime() {
        if (remaining != 0) {
            throw new Error("getTurnaroundTime can be only called when job is finished");
        }
        return waitingTime + duration;
    }
}
