/**
 * This class is the simulation of the RAM.
 * 
 * **/
public class RAM {
	double mSpace;
	Node<PCB> head;
	Node<PCB> tail;
	int noOfProcesses;
	
	public RAM(double space) {
		this.mSpace = space;
	}
	public int readyQueueLength() {
		return noOfProcesses;
	}
	public boolean isEmpty() {
		return head == null;
	}

//-----------------------First come First Serve Algorithm----------------------
	public void insertToReadyQueue(PCB e, int ecu) {
		Node<PCB> newNode = new Node<>(e, ecu);
		if(noOfProcesses == 0) {
			head = newNode;
			tail = head;
		}else {
			tail.next = newNode;
			tail = newNode;
		}
		noOfProcesses++;         	
	}
	
	//Dispatching a process from the RAM
	public Node<PCB> dispatch() {
		Node<PCB> node = head;
	    head = head.next;
	    noOfProcesses--;
	    mSpace = mSpace + node.data.getEMR();
	    return node;
	}	
}
