public class Job extends Statistics {
    private static int lastId = 0;
    private int duration;
    private int remaining;
    private int waitingTime;
    private int priority;
    private int id;

    // number of ticks job was running in this period
    private int periodRunning;

    // number of ticks job was waiting in this period
    private int periodWaiting;

    public Job(int duration, int priority) {
        this.duration = duration;
        this.priority = priority;
        remaining = duration;
        id = ++lastId;
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
        periodRunning += maxTicks;
        return maxTicks;
    }

    /**
     * Must be called when the job was waiting for a certain number of ticks
     */
    public void onWait(int ticks) {
        if (remaining != 0) {
            waitingTime += ticks;
            periodWaiting += ticks;
        }
    }

    public int getPriority() {
        return priority;
    }

    public int getDuration() {
        return duration;
    }

    public int getRemaining() {
        return remaining;
    }

    @Override
    public double getTurnAroundTime() {
        if (!isFinished()) {
            throw new Error("getTurnAroundTime can be only called when job is finished");
        }
        return waitingTime + duration;
    }

    @Override
    public double getWaitingTime() {
        return waitingTime;
    }

    @Override
    public boolean isFinished() {
        return getRemaining() == 0;
    }

    /**
     * Will return the CPU from the last time this method was called.
     * You can call this function periodically to get the CPU usage between each period.
     * Calling this method will reset the internal cpu usage counter.
     * @return the CPU usage in percentage
     */
    public double collectCPUUsage() {
        double total = periodWaiting + periodRunning;
        double result = periodRunning / total * 100;
        periodRunning = 0;
        periodWaiting = 0;
        return result;
    }

    @Override
    public double getTotalCPUUsage() {
        double done = duration - remaining;
        return done / (waitingTime + done) * 100;
    }

    @Override
    public void reset() {
        remaining = duration;
        waitingTime = 0;
        periodWaiting = 0;
        periodRunning = 0;
    }

    /**
     * Will return the unique id of the job.
     */
    public int getId() {
        return id;
    }
}
