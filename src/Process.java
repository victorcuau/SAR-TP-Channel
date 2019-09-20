import java.util.HashMap;
import java.util.Map;

public class Process extends Thread {
	
	public String name;
	Map<String, Process> registry = new HashMap<String, Process>();
	Map<Integer, Channel> list = new HashMap<Integer, Channel>(); // Associate each used port to the Channel
	Map<Integer, Process> connexion_request = new HashMap<Integer, Process>(); // Associate each port that have receive a connexion request with the Process that asked for
	
	
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
		 
		 while(!connexion_request.containsKey(port)) { // While there is no connexion request on this port
			 wait();
		 }
		 
		 connexion_request.remove(port);
		 notifyAll();
		 Channel c = new Channel();
		 list.put(port, c);  // Add the port and the associate Channel to the list of used ports
		 
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
		 
		 // Wait until the distant Process exists
		 while(!this.registry.containsKey(name)) {
			 wait();
		 }
		 Process p = this.registry.get(name);
		 
		 // Subscribe to the list of connexion requests on the distant Process
		 p.connexion_request.put(port, this);
		 p.notify();
		 
		 // Wait until the distant Process remove it from the list (meaning that it will accept the connexion)
		 while(this.connexion_request.containsKey(port)) {
			 wait();
		 }
		 
		 Channel c = new Channel();
		 
		 // Choose the port to use on THIS Process
		 int my_port = 1;
		 while(this.list.containsKey(my_port) | this.connexion_request.containsKey(my_port)) {
			 my_port++;
		 }
		 list.put(my_port, c);  // Add the port and the associate Channel to the list of used ports
		 
		 System.out.println("Process " + this.name + " is connected to " + name + ":" + port);
		 
		 return c;
	 }
	 
	 
	 /*
		* Set the copy of the registry on this process
		*/
	 public void set_registry(Map<String, Process> registry) {
		 this.registry = registry;
	 }
	 
	 
	 public void run() {
		 
	 }
}
