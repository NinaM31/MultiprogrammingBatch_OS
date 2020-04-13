
public class LinkedQueue<T>{

	private Node<T> head, tail;
	private int size;
	
	public LinkedQueue() {
		head = tail = null;
		size = 0;}
	public boolean full() {
		return false;}
	
	public boolean isEmpty() {
		return head == null;
	}
	
	public int length (){
		return size;}

	public void enqueue(T e){
		if(tail == null)
			{head = tail = new Node<>(e, 0);}
		
		else {tail.next = new Node<>(e, 0);
			tail = tail.next;}
			size++;}
	
	public T serve() {
		T x = head.data;
		head = head.next;
		size--;
		if(size == 0)
			tail = null;
		return x;
	}
}
