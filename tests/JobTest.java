import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class JobTest {

    @org.testng.annotations.Test
    public void testProcessTicks() throws Exception {
        Job job = new Job(2);

        assertEquals(job.getRemaining(), 2);

        job.run(1);
        assertEquals(job.getRemaining(), 1);

        boolean thrown = false;
        try {
            job.getTurnaroundTime();
        } catch (Error e) {
            thrown = true;
        }
        assertTrue(thrown);

        job.run(1);
        assertEquals(job.getTurnaroundTime(), 2);
        assertEquals(job.getRemaining(), 0);

        job.run(1);
        assertEquals(job.getRemaining(), 0);

        job.run(2);
        assertEquals(job.getRemaining(), 0);
    }
}