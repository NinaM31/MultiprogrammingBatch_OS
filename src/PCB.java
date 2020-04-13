public class PCB {
    private int CUT; //the amount of CPU units consumed by the process.
    
    private int PID;  //(same as JID)
    private String state; //(New, Ready, Waiting, Terminated, Running)
    private double EMR; //Memory in KB allocated (equals to EMR)
    
    private int IRT ; // The time (in units) I/O was consumed by the process
    private int WT =0; // The time (in units) process spent in the I/O queue. 

//=========================Constructors =============================================    
        
	//PID = JID hence it will be assigned to PCB
	public PCB(Job p) {
		PID = p.getJID();
		state = "New";
		EMR = p.getEMR();
		IRT = 0;
		WT = 0;
	}
	
    public void incrementCUT() {
    	CUT++;
    }
    
//============================== Setters And Getters =====================================   
    public double getEMR() 			   {return EMR;}
    public int getIRT() 			   {return IRT;}
    public int getWT()				   {return WT;}
    public int getCUT()			  	   {return CUT;}
    public int getPID() 			   {return PID;}
    public String getState() 		   {return state;}
    public void setEMR(double EMR)	   {this.EMR = EMR;}
    public void setIRT(int IRT) 	   {this.IRT = IRT;}
    public void setWT(int WT) 		   {this.WT = WT;}
    public void setState(String state) {this.state = state;}
}
 
 
