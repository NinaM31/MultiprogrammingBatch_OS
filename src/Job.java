
public class Job{
    private int JID; 
    private int ECU ; // Expected CPU usage, The ECU is generated randomly between 16 and 512 units.
    private double EMR ; //The Size of the job

//========================================Setters and getters ====================================
    
    public void setJID(int JID) {this.JID = JID; }
    public void setECU(int ECU) {this.ECU = ECU;}
    public void setEMR(int EMR) {this.EMR = EMR;}
    public int getJID() 		{return JID;}
    public double getECU() 		{return ECU;}
    public double getEMR() 		{return EMR;}
    
//===================================Constructors================================================= 
    public Job() {
        JID = 0;
        ECU = 0;
    }   
    public Job(int JID, int ECU, double EMR) {
        this.JID = JID;
        this.ECU = ECU;
        this.EMR = EMR;
    }      
}
