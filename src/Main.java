class Main {
	//Hardware Specification
	static double ram_Size = 198;
	static double hardD_Size = 2000;
    
	public static void main(String[]args){
    	OsSimulator os = new OsSimulator();
    	
    	//Setting up hardware Specification + generate Job.txt
    	os.setUp(hardD_Size, ram_Size); 
    	
    	//Read and Admit jobs into the hard disk by creating job Queue
    	os.loadToHardDisk();
    	
    	//Start the Simulations
    	os.simulate();
    }   
}
