public class Node<T> {
	public T data;
	public int ECU;
	public Node<T> next;

	public Node () {
		data = null;
		next = null;
	}
	public Node (T val, int ECU) {
		data = val;
		this.ECU = ECU;
		next = null;
	}	
//======================================================================	
	public int getECU() {
		return ECU;
	}
	public T getData() {
		return data;
	}
}
