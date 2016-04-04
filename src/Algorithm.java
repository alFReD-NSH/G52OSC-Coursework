public abstract class Algorithm {
    protected JobManager jobManager;
    public Algorithm(JobManager jobManager) {
        this.jobManager = jobManager;
    }
    public abstract void run();
}
