import org.mockito.InOrder;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ShortestJobFirstTest {
    @Test
    public void testRun() throws Exception {
        ArrayList<Job> jobs = new ArrayList<>();
        JobManager jobManager = spy(new JobManager());
        Job job1 = new Job(5, 0);
        Job job2 = new Job(3, 0);
        jobs.add(job1);
        jobs.add(job2);
        jobManager.setJobs(jobs);
        new ShortestJobFirst(jobManager).run();

        InOrder inOrder = inOrder(jobManager);
        inOrder.verify(jobManager).runJob(job2, Integer.MAX_VALUE);
        inOrder.verify(jobManager).runJob(job1, Integer.MAX_VALUE);
    }
}