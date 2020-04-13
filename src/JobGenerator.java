import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * This Class is responsible for generating a Job.txt file
 * Storing jobs until the Hard Disk is full
 * 
 * **/
public class JobGenerator {
	
//----------------------Used for testing purposes-------------------------------------
	public static void creatTextFile() {
		int max = 512; 
		int min = 16; 
			 
		int rangeECU = max - min + 1; 
		int rangeEMR = 256 - 16 +1; 
			        
		File file = new File("jobs.txt");		    	
		try {
			FileWriter writer = new FileWriter(file);
			for (int i=1; i<= Main.hardD_Size/100; i++) {   		
				//This will generate between 16 and 512 units
				int rand1 = (int)(Math.random() * rangeECU) + min;
				int rand2 = (int)(Math.random() * rangeEMR) + 16;
					
				//Writing into the file
				writer.write("JID:" + i);
				writer.write(System.getProperty("line.separator"));
				writer.write("ECU:" + rand1);
				writer.write(System.getProperty("line.separator"));
				writer.write("EMR:" + rand2);
				writer.write(System.getProperty("line.separator"));
				writer.write("====================================================================");
				writer.write(System.getProperty("line.separator"));	
			} 
			
			writer.close();
			System.out.println("Jobs.txt file Generated Successfully\n");
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found Job.txt\nLocation of error in HardDisk");
		}catch(IOException e) {
			System.out.println("Exception when writing into Job.txt\nLocation of error in HardDisk");
		}	
	}
	
//-------------------------------------Generating to fill in the HDD----------------------------------------------
		public static void creatTextFile(double space) {
			System.out.println("Generated Job.txt ");
			try {
				File file = new File("jobs.txt");
				FileWriter writer = new FileWriter(file);
				
				//We are taking minus 16 the minimum range to check if even the smallest process could fit into the File and not overfit 
				while(space - 16 > 0 ) {
					Job a = new Job();
							
					//Writing into the file
					writer.write("JID:" + a.getJID());
					writer.write(System.getProperty("line.separator"));
					writer.write("ECU:" + a.getECU());
					writer.write(System.getProperty("line.separator"));
					writer.write("EMR:" +a.getEMR());
					writer.write(System.getProperty("line.separator"));
					writer.write("====================================================================");
		            writer.write(System.getProperty("line.separator"));	
		            
		            space = space - a.getEMR();
				}
				
				writer.close();// always close
				System.out.println("Jobs.txt file Generated Successfully");
				
			} catch (FileNotFoundException e) {
				System.out.println("File not found Job.txt\nLocation of error in HardDisk");
			}catch(IOException e) {
				System.out.println("Exception when writing into Job.txt\nLocation of error in HardDisk");
			}	
		}
}
