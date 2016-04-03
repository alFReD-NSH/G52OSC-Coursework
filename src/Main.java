import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    @Parameter(names = "--job", description = "duration[:priority], specifies a job " +
            "with duration given in ticks and a an optional priority which by default " +
            "is 1. You should specify this argument multiple times to have multiple " +
            "jobs. For example: `--job 1:2 --job 3:3`", converter = JobConverter.class)
    public List<Job> jobs = new ArrayList<>();

    @Parameter(names = "--random", description = "Will make the application to randomly" +
            " generate jobs. By default it will generate 10 random jobs, with max " +
            "duration of 10 and max priority of 10. You can customize these with " +
            "--total-jobs, --max-duration and --max-priority options, each expects an " +
            "integer value")
    public boolean random;

    @Parameter(names = "--total-jobs", description = "The number of jobs to be randomly" +
            " generated.")
    public int totalJobs = 10;

    @Parameter(names = "--max-duration", description = "Maximum allowed duration for " +
            "the randomly generated jobs")
    public int maxDuration = 10;

    @Parameter(names = "--max-priority", description = "Maximum priority for the " +
            "randomly generated jobs")
    public int maxPriority = 10;

    public static void main(String[] args) {
        Main main = new Main();
        new JCommander(main, args);
        main.run();
    }

    private void run() {
        JobManager jobManager = new JobManager();
        jobManager.jobs = jobs;

        if (random) {
            jobManager.generateRandomJobs(totalJobs, maxDuration, maxPriority);
        }
    }
}
