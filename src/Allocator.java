import java.util.Arrays;

public class Allocator {
    // Maps resource type
    private int[] freeResources;

    public Allocator(int resourceTypes, int resources) {
        freeResources = new int[resourceTypes];
        Arrays.fill(freeResources, resources);
    }

    public boolean allocate(int i) {
        if (freeResources[i] > 0) {
            freeResources[i]--;
            return true;
        } else {
            return false;
        }
    }

    public void free(int resourceType, int resources) {
        freeResources[resourceType] += resources;
    }

    public int[] getFreeResources() {
        return freeResources;
    }

    public boolean canAllocate(int[] wanted) {
        for (int j = 0; j < freeResources.length; j++) {
            if (wanted[j] > freeResources[j]) {
                return false;
            }
        }
        return true;
    }
}
