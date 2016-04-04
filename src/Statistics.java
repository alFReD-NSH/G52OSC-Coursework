public abstract class Statistics {
    public abstract double getWaitingTime();
    public abstract double getTurnAroundTime();
    public abstract boolean isFinished();
    public abstract double getTotalCPUUsage();
    public abstract void reset();

    public Result getResult(String name) {
        Result result = new Result();
        result.name = name;
        result.waitingTime = getWaitingTime();
        result.turnAroundTime = getTurnAroundTime();
        return result;
    }

    public static class Result {
        public String name;
        public double waitingTime;
        public double turnAroundTime;
    }
}
