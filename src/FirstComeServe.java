package src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;




public class FirstComeServe {
    
    int duration;
    int remaining;
    int priority;
    double avgWaitingTime;
    double avgTurnAroundTime;
    
    
    public void run() {
        
    }
    
    public void run(List<Job> jobList) {
        int count = 0;
        System.out.println("============================================ ");
        System.out.println("Process ID | Turnaround time | Waiting time ");
        System.out.println("============================================ ");
        for(Job job:jobList){
            if(count==0){
                job.remaining = job.remaining();
                job.duration = job.remaining()+job.getCpuTime();
                }else{
                job.remaining = temp-job.remaining();
                job.duration = temp+job.getCpuTime();
            }
            
            temp = job.duration;
            job.priority = temp-job.remaining();
            job.waitingTime = job.priority-job.getCpuTime();
            count++;
            
            avgWaitingTime =  avgWaitingTime+job.waitingTime;
            avgTurnAroundTime = avgTurnAroundTime+job.priority;
            System.out.println("   "+job.getProcessId()+"  | "+"   "+job.priority+"  | "+"   "+job.waitingTime+" ");
            System.out.println("----------------------------------------");
        }
        System.out.println("===============================================");
        System.out.println("Avg waiting time:"+avgWaitingTime/jobList.size());
        System.out.println("===============================================");
        System.out.println("Avg turn around time:"+avgTurnAroundTime/jobList.size());
        System.out.println("===============================================");
        
    }
    
}
