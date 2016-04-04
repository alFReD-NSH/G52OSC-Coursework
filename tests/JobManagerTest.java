import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class JobManagerTest {

    @Test
    public void testRunJob() throws Exception {
        JobManager jobManager = new JobManager();
        ArrayList<Job> jobs = new ArrayList<>();
        Job job1 = new Job(3, 0);
        Job job2 = new Job(5, 0);
        jobs.add(job1);
        jobs.add(job2);
        jobManager.setJobs(jobs);

        jobManager.runJob(job2, 3);
        assertEquals(job1.getRemaining(), 3);
        assertEquals(job2.getRemaining(), 2);

        jobManager.runJob(job2, 3);
        assertEquals(job2.getRemaining(), 0);
        assertEquals(job2.getTurnAroundTime(), 5.0);
        assertFalse(jobManager.isFinished());

        jobManager.runJob(job1, 4);
        assertEquals(job1.getRemaining(), 0);
        assertEquals(job1.getTurnAroundTime(), 8.0);
        assertEquals(job1.getWaitingTime(), 5.0);
        assertEquals(jobManager.getWaitingTime(), 2.5);
        assertEquals(jobManager.getTurnAroundTime(), 6.5);
        assertTrue(jobManager.isFinished());
    }
}