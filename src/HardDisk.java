import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * This class is the simulation of the hardware.
 * 
 * **/
public class HardDisk {
	double space ;
	double sizeOfAllProcess;
	int noOfProcesses;
	Node<Job> head;
	
	double MaxEMR;
	double MinEMR;
	double avg;
	
	public HardDisk(double space) {
		this.space = space;
		sizeOfAllProcess  = 0;
		noOfProcesses = 0;
	}
	public boolean isJobQueueEmpty() {
		return head == null;
	}
	
//----------------------------------------HardDisk Storage of Jobs---------------------------------------------		
	public void createJobQueue() {
		System.out.println("Loading Job.txt into the Job Queue....");
		//Reading from a file jobs.txt
		File file = new File("jobs.txt");
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String st;
			int jid ;
			double  emr = 0;
			int ecu ;
			while((st = buffer.readLine()) != null) {
				
				if (st.charAt(0) != '=' ){
					jid = Integer.parseInt(st.substring(st.indexOf(':') + 1));
					st = buffer.readLine();
					ecu = Integer.parseInt(st.substring(st.indexOf(':') + 1));
					st = buffer.readLine();
					emr = Double.parseDouble(st.substring(st.indexOf(':') + 1));
					
					//Generating PCB To admit to the system with a NEW state
					Job job = new Job(jid, ecu, emr);
					enqueueToHDD(job);	
					
				}	
			}
			buffer.close();
			System.out.println("Created Job Queue Succesfully\nJobs are in Job Queue");
			
			avg = sizeOfAllProcess/noOfProcesses; //The average 
			
		}catch (FileNotFoundException e)  {System.out.println("Load Failed, Job.txt not found");}
		catch(IOException e) 			  {System.out.println("Load Failed");e.printStackTrace();}
	}
	
/**
 * The purpose of this method is to Store The jobs according to the Smallest Program size
 * Such that once enqueued from hard disk the shortest job will be removed first
 * BASED ON SSR policy
 * **/
	private void enqueueToHDD(Job e) {
		Node<Job> newNode = new Node<>(e);
		
		//Maybe this process EMR is less than that of the head
	    if ( noOfProcesses == 0 || e.getEMR() < head.data.getEMR() ) {
	    	newNode.next = head;
	        head = newNode;
			MinEMR = e.getEMR();//smallest EMR for a process
	    } else {
	        Node<Job> current = head;
	        Node<Job> prev = null;
	        
	        //To place the process with the smallest EMR before the biggest EMR
	        while ((current != null) && (e.getEMR() >= current.data.getEMR())) {
	            prev = current;
	            current = current.next;
	        }  
	        newNode.next = current;
	        prev.next = newNode;
	    }
        sizeOfAllProcess = sizeOfAllProcess + e.getEMR();//The size of all processes
	    noOfProcesses++;//We added a process	    
	 }	
//-------------------dequeuing the smallest job first <PCB, ECU> pair----------------------
	 public Job dequeue() {
	     Node<Job> node = head;
	     head = head.next;
	     noOfProcesses--;
	     if(noOfProcesses == 0)
			 MaxEMR = node.data.getEMR(); //The Max emr of the processes
	     return node.data;
	 }
}
