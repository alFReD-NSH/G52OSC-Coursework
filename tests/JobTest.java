import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class JobTest {

    @org.testng.annotations.Test
    public void testProcessTicks() throws Exception {
        Job job = new Job(2, 0);

        assertEquals(job.getRemaining(), 2);

        job.run(1);
        assertEquals(job.getRemaining(), 1);

        boolean thrown = false;
        try {
            job.getTurnAroundTime();
        } catch (Error e) {
            thrown = true;
        }
        assertTrue(thrown);

        job.run(1);
        assertEquals(job.getTurnAroundTime(), 2.0);
        assertEquals(job.getRemaining(), 0);

        job.run(1);
        assertEquals(job.getRemaining(), 0);

        job.run(2);
        assertEquals(job.getRemaining(), 0);
    }

    @org.testng.annotations.Test
    public void testgetCurrentCPUUsage() throws Exception {
        Job job = new Job(20, 0);

        job.run(5);
        job.onWait(5);
        assertEquals(50.0, job.collectCPUUsage());
        assertEquals(50.0, job.getTotalCPUUsage());
        job.run(5);
        assertEquals(100.0, job.collectCPUUsage());
    }
}