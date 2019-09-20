import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Process extends Thread {
	
//	private ArrayList<Process> registry = new ArrayList<Process>();
	Map<String, Process> registry = new HashMap<String, Process>();
	public String name;
	Map<Integer, Channel> list = new HashMap<Integer, Channel>(); // Associated each used port to the Channel
	Map<Integer, Process> connexion_request = new HashMap<Integer, Process>(); // List of process that have request a connexion on a port of this one
	
	
	/*
	 * Constructor, given name must be uniquely
	 */
	 protected Process(String name){
		 this.name = name;
	 }
	 
	 
	 /*
	 * Blocking call, until another process connects.
	 * The returned channel is between this process
	 * and the one that connected.
	 * This method can be called on any thread, but
	 * for different ports.
	 */
	 protected Channel accept(int port) throws InterruptedException {
		 System.out.println("Process " + this.name + " is waiting for a connexion request on port " + port);
		 
		 while(!connexion_request.containsKey(port)) { // While there is no connexion request
			 wait();
		 }
		 
		 connexion_request.remove(port);
		 notifyAll();
		 Channel c = new Channel();
		 list.put(port, c);
		 
		 System.out.println("Process " + this.name + " is connected to someone on port " + port);
		 
		 return c;
	 }
	 
	 
	 /*
	 * Blocking call, until another the named process does
	 * a corresponding accept on the given port.
	 * The returned channel is between this process
	 * and the one that accepted the connect.
	 * This method can be called on any thread, but
	 * for different ports.
	 */
	 protected Channel connect(String name, int port) throws InterruptedException {
		 System.out.println("Process " + this.name + " send a connexion request to " + name + ":" + port);
		 while(!this.registry.containsKey(name)) {
			 wait();
		 }
		 Process p = this.registry.get(name);
		 p.connexion_request.put(port, this);
		 p.notify();
		 
		 while(this.connexion_request.containsKey(port)) {
			 wait();
		 }
		 
		 Channel c = new Channel();
		 
		 int my_port = 1;
		 while(this.list.containsKey(my_port) | this.connexion_request.containsKey(my_port)) {
			 my_port++;
		 }
		 list.put(my_port, c);
		 
		 System.out.println("Process " + this.name + " is connected to " + name + ":" + port);
		 
		 return c;
	 }
	 
	 
	 public void set_registry(Map<String, Process> registry) {
		 this.registry = registry;
	 }
	 
	 
	 public void run() {
		 
	 }
}
