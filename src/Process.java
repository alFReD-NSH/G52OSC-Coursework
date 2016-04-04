import java.util.ArrayList;
import java.util.Arrays;

public class Process {
    private static int lastId = 0;
    private Allocator allocator;

    // Maps resource type to the number of resources that it wants from that resource
    private int[] wanted;

    // Maps resource type to the number of resources that it has from that resource
    private int[] held;

    // The resource that the process is waiting for currently. -1 indicates it's not
    // waiting for any resource.
    private int waitingFor = -1;

    private boolean killed;

    private int id = lastId++;

    public Process(Allocator allocator, int resourceTypes, int resources) {
        this.allocator = allocator;
        wanted = new int[resourceTypes];
        held = new int[resourceTypes];
        for (int i = 0; i < wanted.length; i++) {
            wanted[i] = (int) Math.round(Math.random() * resources);
        }
    }

    /**
     * Will tell the process that to try to allocate a resource
     */
    public void allocate() {
        ArrayList<Integer> resourceTypes = new ArrayList<>();
        for (int i = 0; i < wanted.length; i++) {
            if (wanted[i] > 0) {
                resourceTypes.add(i);
            }
        }
        int resource = Utils.sample(resourceTypes);
        if (allocator.allocate(resource)) {
            System.out.println("Process " + id  + " allocated one of resource "
                    + resource);
            // Allocation was successful
            wanted[resource]--;
            held[resource]++;
        } else {
            System.out.println("Process " + id  + " waiting for resource " + resource);
            waitingFor = resource;
        }
    }

    /**
     * Will kill the process and the process will free all of it's held resource.
     */
    public void kill() {
        for (int i = 0; i < held.length; i++) {
            if (held[i] > 0) {
                allocator.free(i, held[i]);
            }
        }
        killed = true;
    }

    public boolean isWaiting() {
        return waitingFor != -1;
    }

    /**
     * @return will return true if the process wants to allocate or not.
     */
    public boolean wants() {
        return !killed && !isWaiting() &&
                Arrays.stream(wanted).anyMatch(value -> value > 0);
    }

    public boolean fullfilled() {
        return killed || Arrays.stream(wanted).allMatch(value -> value == 0);
    }

    /**
     * @return if the process is waiting for any resource type, it will return the
     * number else it will return -1.
     */
    public int getWaitingFor() {
        return waitingFor;
    }

    public int getId() {
        return id;
    }

    public int[] getWanted() {
        return wanted;
    }

    public int[] getHeld() {
        return held;
    }

    public boolean isKilled() {
        return killed;
    }
}
