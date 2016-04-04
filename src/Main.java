import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class Main {
    final static V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
    final static DecimalFormat decimalFormat = new DecimalFormat("#.#####");

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

    @Parameter(names = "--quantum", description = "Time quantum for round roubin " +
            "algorithm")
    public int quantum = 3;

    @Parameter(names = "--help", help = true)
    private boolean help;

    public static void main(String[] args) {
        Main main = new Main();

        main.run(new JCommander(main, args));
    }

    private void run(JCommander jCommander) {
        rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
        rend.setWidth(new WidthAbsoluteEven(80));

        JobManager jobManager = new JobManager(rend);
        jobManager.setJobs(jobs);

        if (random) {
            jobManager.generateRandomJobs(totalJobs, maxDuration, maxPriority);
        }

        if (jobs.size() == 0) {
            jCommander.usage();
            return;
        }

        ExecutionChartCreator executionChartCreator = new ExecutionChartCreator(79);
        jobManager.setOnJobRunCallback(executionChartCreator);

        FirstComeFirstServed firstComeServe = new FirstComeFirstServed(jobManager);
        ShortestJobFirst shortestJobFirst = new ShortestJobFirst(jobManager);
        RoundRobin roundRobin = new RoundRobin(jobManager);
        roundRobin.setQuantum(quantum);

        List<Algorithm> algorithms = Arrays.asList(firstComeServe,
                shortestJobFirst, roundRobin);

        compareAlgorithms(algorithms.stream().map(algorithm -> {
            System.out.println("Running " + algorithm.getName());
            algorithm.run();
            printStatistics(jobManager);
            Statistics.Result result = jobManager.getResult(algorithm.getName());
            executionChartCreator.print();
            jobManager.reset();
            executionChartCreator.reset();
            return result;
        }).collect(Collectors.toList()));
    }

    private void compareAlgorithms(List<Statistics.Result> results) {
        printAlgorithmTable(results);
        printChart(JobManager::getWaitingTime, "Waiting time.");
    }

    private void printChart(ToDoubleFunction<JobManager> mapper, String a) {

    }

    private void printAlgorithmTable(List<Statistics.Result> results) {
        V2_AsciiTable at = new V2_AsciiTable();
        at.addRule();
        at.addRow("Algorithm", "Average Turn Around Time", "Average Waiting Time");
        for (Statistics.Result result : results) {
            at.addRule();
            at.addRow(result.name, decimalFormat.format(result.turnAroundTime),
                    decimalFormat.format(result.waitingTime));
        }
        at.addRule();

        System.out.println(rend.render(at));
    }

    private void printStatistics(JobManager jobManager) {
        V2_AsciiTable at = new V2_AsciiTable();
        at.addRule();
        at.addRow("Job id", "Duration", "Priority", "Turn Around Time", "Waiting Time");
        for (Job job : jobManager.getJobs()) {
            at.addRule();
            at.addRow(job.getId(), job.getDuration(), job.getPriority(),
                    job.getTurnAroundTime(), job.getWaitingTime());
        }
        at.addRule();
        at.addRule();
        at.addRow("Average", "", "",
                decimalFormat.format(jobManager.getTurnAroundTime()),
                decimalFormat.format(jobManager.getWaitingTime()));
        at.addRule();

        System.out.println(rend.render(at));
    }
}

