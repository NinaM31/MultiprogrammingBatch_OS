import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


/** The purpose of this class it simulate an operating system**/
public class OsSimulator {
	private HardDisk hdd;
	private RAM ram;
	
	int noAbnormanl = 0; //jobs completed abnormally.
	int noNormanl = 0 ; //jobs completed normally.
	double noProcessedJobs = 0 ; //Total number of jobs processed.
	double noJobsServiced = 0; // Number of jobs serviced by I/O device
	double avgWT = 0;
	
	LinkedQueue<PCB> IO_Queue;  // I/O queue
	int counter; 
	String TerminationStatus ; // termination status (normal/abnormal)
	
	//Setting up the specification-
	public void setUp(double hardD_size, double ram_size) {
		
		hdd = new HardDisk(hardD_size);
		ram = new RAM(ram_size);
		IO_Queue = new LinkedQueue<>();
		
		//Generate a file filled with Jobs.
    	JobGenerator.creatTextFile();
    	
    	System.out.println("Operating system Set up Completed");
    	System.out.println("Hardware Sepcification:");
    	System.out.println("CPU: " + "1" + " core");
    	System.out.println("HardDisk size: " + Main.hardD_Size + " KB");
    	System.out.println("RAM Size:" + Main.ram_Size + " KB");
    	System.out.println();
	}
	
	//Reading and creating Job Queue
	public void loadToHardDisk() {
		try {
			hdd.createJobQueue();
			
		}catch(Exception e){
			System.out.println("Hard Disk Specification not defined");
		}
	}

	//Where the OS starts its simulation
	public void simulate() {
		if(hdd.space == 0.0 && ram.mSpace == 0.0) {
			System.out.println("Hardware Was not Specified Correctly");
			return;
		}
		System.out.println();
		/*Simulating the CPU 
		 * First we will Admit all the PCBs from NEW state to READY state by: 
		 * admitting them from job queue to ready queue this takes place in JOBSCHEDULE
		 * Then we Dispatch these READY PCBs in by the CPUSCHEDUALER
		 * This will be repeated until there are no more jobs in the Job Queue
		 * Then The I/O Queue will serve the Jobs
		 * Finally at the end a Result text will be generated
		 * */
		
		while(!hdd.isJobQueueEmpty() || !ram.isEmpty()) {
			
			jobSchedule();
			cpuScheduler();
		}
		System.out.println();
		generateResultText(); 		
	}

	//Schedules jobs using SSR policy
	public void jobSchedule() {
		System.out.println("Job Scheduler Addmitting PCBs into Ready Queue....");

	    while (ram.mSpace - 256 > 0 && hdd.noOfProcesses > 0) {//fill the ready queue with processes from Job queue by checking if maximum size will still fit.		
	    	Job job = hdd.dequeue(); 
	    	PCB pcb = new PCB(job);
	    	
		    pcb.setState("Ready");//Update State
		    ram.mSpace = ram.mSpace - pcb.getEMR();
		    
	    	ram.insertToReadyQueue(pcb);
	    	noProcessedJobs++;
		} 
	    System.out.println("The PCB's created and joined the Ready Queue and are in READY STATE.\n");
	}
	
	public void cpuScheduler() {
		boolean IOrequest = false;
		System.out.println("Dispatching Processes from the Ready Queue to be processed by CPU..\n");
		while(!ram.isEmpty()) {

			PCB dispatched = ram.dispatch();
			dispatched.setState("Running");//Update status 
			
			System.out.println("PID " + dispatched.getPID() + " is Running");
			System.out.println("Dispatched PID " + dispatched.getPID() );
			
			 
			double interrupt;
			while(true) { 
				dispatched.incrementCUT();

				interrupt = generateInterrupt();
				 if (interrupt <= 0.2){
			         IO_Queue.enqueue(dispatched);
			         dispatched.setState("Waiting");
			         IOrequest = true;
			         System.out.println("PID " + dispatched.getPID() + " has I/O Request\n");
			         //jobSchedule();
			         break;
			     }
				 interrupt = generateInterrupt(); 
				 if (interrupt <= 0.10 && dispatched.getCUT() <= dispatched.getECU() ){
			    	 TerminationStatus = "normally";
			    	 noNormanl++;
			    	 displayTerminatedProcess(dispatched);
			    	 jobSchedule();
			    	 break;
			     }
				 interrupt = generateInterrupt(); 
				 if (interrupt <= 0.05 || dispatched.getCUT() > dispatched.getECU()){
			         TerminationStatus = "abnormally";
			         noAbnormanl++;
			         displayTerminatedProcess(dispatched);
			         jobSchedule();
			         break;
			     }
			}			
		}	
		if(IOrequest)
			IORequest();	
		System.out.println("Disspatched processes\n");
	}
//-----------------------------------------------IO Queue-----------------------------------------
	public void IORequest(){  
		System.out.println("Serving the processes Requesting IO in the IO Queue...\n"); 
		int length = IO_Queue.length();
	    int i;
	    int wt;
	    double sumWT = 0;
	    
	    while (IO_Queue.length() != 0 ){
	    	boolean IO = true;
	    	
	    	PCB dispatched = IO_Queue.serve();
	    	i = dispatched.getIRT();
	    	wt =  dispatched.getWT();
	    	
	    	if(i == 0)
	    		noJobsServiced++;

	    	while (IO){
	    		dispatched.setIRT(++i);
	    		dispatched.setWT(++wt);

	    		//generate interrupt    
	    		double interrupt = generateInterrupt();
	    		
	    		if (interrupt <= 0.20){
	    			IO = false ;
	    			
	    			dispatched.setWT(wt+length-1);
	    			dispatched.setState("Ready");
	    			ram.insertToReadyQueue(dispatched); 
	    		}
	    	} 
	    	sumWT = wt + sumWT;	
	   }
	   avgWT = sumWT/noJobsServiced;
	   System.out.println("\nServed all IO Request they are back in Ready queue");
	}

	//Generates a Random interrupt between 0.0 and 1.0
	public double generateInterrupt(){ 
	     return Math.random();
	 }
//=============Displaying the status of the dispatched process ===================
	public void displayTerminatedProcess(PCB dispatched){
		counter++;
		System.out.println("Process Information: " + counter);  
		System.out.println("Process ID: " + dispatched.getPID());
		System.out.println("Process CUT : " + dispatched.getCUT());
		System.out.println("Process EMR : " + dispatched.getEMR());
		System.out.println("Process IRT : " + dispatched.getIRT());
		System.out.println("Process WT : " + dispatched.getWT());
		System.out.println("Process termination status : "+ TerminationStatus +"\n" );
		
	}
	
//===========================================Results of the Project==================
	public void generateResultText(){
		try (PrintWriter writer = new PrintWriter("Result.txt", "UTF-8")) {
	         writer.println("Total number of jobs processed: "+ noProcessedJobs);
	         writer.println("Number of jobs that completed normally:" + noNormanl);
	         writer.println("Number of jobs that completed abnormally:" + noAbnormanl);
	         writer.println("Average job size in KB: "+ hdd.avg);
	         writer.println("Minimum job size in KB: "+ hdd.MinEMR);
	         writer.println("Maximum job size in KB: "+ hdd.MaxEMR);
	         writer.println("CPU utilization: "+ CPUutilization() + "%" );
	         writer.println("Number of jobs serviced by I/O device: " + noJobsServiced);
	         writer.println("I/O utilization: "+ IOutilization() + "%" );
	         writer.println("Average waiting time: " + avgWT);
	         
	         System.out.print("result has been written in Result.txt");
	     }
		catch(FileNotFoundException e)        {e.printStackTrace();}
		catch(UnsupportedEncodingException e) {e.printStackTrace();}	     
	}
	
	public double CPUutilization() {
		double uti = ((noProcessedJobs-noJobsServiced)/noProcessedJobs) * 100.0;	
		return uti ;
	}
	public double IOutilization() {
		double uti = ((noJobsServiced)/noProcessedJobs) * 100;	
		return uti;
	}
	
	public double presion(String s) {
		double e = Double.parseDouble(s.substring(0,2));
		return e;
	}
}
