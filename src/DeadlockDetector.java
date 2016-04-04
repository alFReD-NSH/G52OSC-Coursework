public class DeadlockDetector implements Runnable {
    public DeadlockDetector(int totalProcesses, Allocator allocator, Process[]
            processes, long detectionInterval) {
        this.totalProcesses = totalProcesses;
        this.allocator = allocator;
        this.processes = processes;
        this.detectionInterval = detectionInterval;
    }

    private int totalProcesses;
    private Allocator allocator;
    private Process[] processes;
    private long detectionInterval;

    @Override
    public void run() {
        while (true) {
            System.out.println("Checking for deadlock");
            boolean marked[] = new boolean[totalProcesses];

            // Since we are going to modify the available array, we should clone it,
            // since it's only to detect deadlock.
            int available[] = allocator.getFreeResources().clone();

            // Killed processes can't create deadlocks. Mark them as done.
            for (int i = 0; i < totalProcesses; i++) {
                marked[i] = processes[i].isKilled();
            }

            while (true) {
                int process = findAllocatableProcess(marked);
                if (process == -1) {
                    for (int i = 0; i < marked.length; i++) {
                        if (!marked[i]) {
                            System.out.println(i + " is deadlocked. Killing it.");
                            processes[i].kill();
                        }
                    }
                    break;
                } else {
                    marked[process] = true;
                    int allocated[] = processes[process].getHeld();
                    for (int i = 0; i < available.length; i++) {
                        available[i] += allocated[i];
                    }
                }
            }
            try {
                Thread.sleep(detectionInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Will search for a process that can allocate(wanting less resources than the
     * available resources).
     * @param marked
     * @return It will return the id of the process. If it didn't find anything it will
     * return -1
     */
    private int findAllocatableProcess(boolean marked[]) {
        for (int i = 0; i < totalProcesses; i++) {
            if (!marked[i] && allocator.canAllocate(processes[i].getWanted())) {
                return i;
            }
        }
        return -1;
    }
}
