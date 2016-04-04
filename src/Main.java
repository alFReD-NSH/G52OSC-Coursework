import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    @Parameter(names = "--resource-types", description = "The number of resource types")
    private int resourcesTypes = 5;

    @Parameter(names = "--resources", description = "The number of identical resource " +
            "types")
    private int resources = 5;

    @Parameter(names = "--processes", description = "The number of processes")
    private int totalProcesses = 5;

    @Parameter(names = "--max-sleep", description = "The maximum amount of time in " +
            "seconds the program is sleeping between allocations")
    private int maxSleep = 3;

    @Parameter(names = "--detection-interval", description = "The amount of time " +
            "seconds after which the deadlock detection thread will wake up and " +
            "detects if there's any deadlock or not.")
    private long detectionInterval = 3;

    @Parameter(names = "--help", help = true)
    private boolean help;

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        new JCommander(main, args);
        main.run();
    }

    private void run() throws InterruptedException {
        Allocator allocator = new Allocator(resourcesTypes, resources);
        Process[] processes = new Process[totalProcesses];
        for (int i = 0; i < totalProcesses; i++) {
            processes[i] = new Process(allocator, resourcesTypes, resources);
        }

        printProcessesWants(processes);

        new Thread(new DeadlockDetector(totalProcesses, allocator, processes,
                detectionInterval)).start();

        while (true) {
            ArrayList<Process> allocatableProcesses = new ArrayList<>();
            if (Arrays.stream(processes).allMatch(Process::isKilled)) {
                System.out.println("All processes are killed");
                System.exit(0);
                return;
            } else if (Arrays.stream(processes).allMatch(Process::fullfilled)) {
                System.out.println("All processes are finished");
                System.exit(0);
                return;
            }
            for (Process process : processes) {
                if (process.wants()) {
                    allocatableProcesses.add(process);
                }
            }
            for (Process process : processes) {
                if (process.wants()) {
                    allocatableProcesses.add(process);
                }
            }
            if (allocatableProcesses.size() == 0) {
                System.out.println("No process wants/can allocate anything. Waiting for" +
                        " something else to happen.");
            } else {
                Utils.sample(allocatableProcesses).allocate();
            }
            long sleep = (long) (Math.random() * maxSleep * 1000);
            System.out.println("Sleeping for " + sleep + "ms");
            Thread.sleep(sleep);
        }
    }

    private void printProcessesWants(Process[] processes) {
        System.out.println("Resources that are wanted/requested by the processes");
        V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
        rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
        rend.setWidth(new WidthAbsoluteEven(80));

        V2_AsciiTable at = new V2_AsciiTable();
        at.addRule();
        String[] firstRow = new String[resourcesTypes + 1];
        firstRow[0] = "Process\\Resource";
        for (int i = 0; i < resourcesTypes; i++) {
            firstRow[i + 1] = i + "";
        }
        at.addRow((Object[]) firstRow);
        for (Process process : processes) {
            String[] row = new String[resourcesTypes + 1];
            row[0] = process.getId() + "";
            int[] wanted = process.getWanted();
            for (int i = 0; i < resourcesTypes; i++) {
                row[i + 1] = wanted[i] + "";
            }
            at.addRule();
            at.addRow((Object[]) row);
        }
        at.addRule();
        System.out.println(rend.render(at));
    }
}
