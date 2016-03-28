public class Job implements Statistics {
    private int duration;
    private int remaining;
    private int waitingTime;
    private int priority;

    public Job(int duration, int priority) {
        this.duration = duration;
        this.priority = priority;
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

    public double getTurnAroundTime() {
        if (!isFinished()) {
            throw new Error("getTurnAroundTime can be only called when job is finished");
        }
        return waitingTime + duration;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isFinished() {
        return getRemaining() == 0;
    }
}
