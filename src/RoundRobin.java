import java.util.List;

public class RoundRobin extends Algorithm {
    private int quantum = 3;
    public RoundRobin(JobManager jobManager) {
        super(jobManager);
    }

    @Override
    public void run() {
        List<Job> jobs = jobManager.getJobs();
        boolean finished;
        do {
            finished = true;
            for (Job job : jobs) {
                if (!job.isFinished()) {
                    jobManager.runJob(job, quantum);
                }
                if (!job.isFinished()) {
                    finished = false;
                }
            }
        } while (!finished);
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public String getName() {
        return "round-robin";
    }
}