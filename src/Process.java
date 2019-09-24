import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Process extends Thread {

	public String					name;
	Map<String, Process>	registry					= new HashMap<String, Process>();
	Map<Integer, Channel>	list							= new HashMap<Integer, Channel>();	// Associate each used port to the Channel
	Map<Integer, Process>	connexion_request	= new HashMap<Integer, Process>();	// Associate each port that have receive a connexion request with the Process that asked for
	public int						my_port						= 1;
	
	/*
	 * Constructor, given name must be uniquely
	 */
	protected Process(String name) {
		this.name = name;
	}

	
	/*
	 * Blocking call, until another process connects. The returned channel is
	 * between this process and the one that connected. This method can be called on
	 * any thread, but for different ports.
	 */
	protected synchronized Channel accept(int port) throws InterruptedException {
		// Can't do an accept() on a port that is already used. In this case, raise an exception.
		if (this.list.containsKey(port)) {
			throw new InterruptedException("Port " + port + "is already used on process " + this.name);
		}
		
		System.out.println("Process " + this.name + " is waiting for a connexion request on port " + port);

	  // While there is no connexion request on this port
		while (!this.connexion_request.containsKey(port)) {
			System.out.println("   " + this.name + " wait()");
			wait();
			System.out.println("   " + this.name + " wake up");
		}

		Process p = this.connexion_request.get(port);
		this.connexion_request.remove(port);
		synchronized (p) {
			System.out.println("   " + this.name + " notifyAll()");
			p.notifyAll();
		}
		Channel c = new Channel(p, p.my_port);
		list.put(port, c); // Add the port and the associate Channel to the list of used ports

		System.out.println("Process " + this.name + " is connected to " + p.name + ":" + p.my_port + " on port " + port);

		return c;
	}

	
	/*
	 * Blocking call, until another the named process does a corresponding accept on
	 * the given port. The returned channel is between this process and the one that
	 * accepted the connect. This method can be called on any thread, but for
	 * different ports.
	 */
	protected synchronized Channel connect(String name, int port) throws InterruptedException {
		System.out.println("Process " + this.name + " send a connexion request to " + name + ":" + port);

		// Wait until the distant Process exists
		while (!this.registry.containsKey(name)) {
			System.out.println("   " + this.name + " wait()");
			wait();
			System.out.println("   " + this.name + " wake up");
		}
		Process p = this.registry.get(name);

		// Subscribe to the list of connexion requests on the distant Process
		p.connexion_request.put(port, this);
		synchronized (p) {
			System.out.println("   " + this.name + " notify()");
			p.notifyAll();
		}
		
		// Wait until the distant Process remove it from the list (meaning that it will accept the connexion)
		while (p.connexion_request.containsKey(port)) {
			System.out.println("   " + this.name + " wait()");
			wait();
			System.out.println("   " + this.name + " wake up");
		}

		// Choose the port to use on THIS Process
		while (this.list.containsKey(this.my_port) | this.connexion_request.containsKey(this.my_port)) {
			this.my_port++;
		}
		Channel c = new Channel(p, port);
		list.put(this.my_port, c); // Add the port and the associate Channel to the list of used ports

		System.out.println("Process " + this.name + " is connected to " + name + ":" + port + " on port " + this.my_port);

		return c;
	}

	
	/*
	 * Set the copy of the registry on this process
	 */
	public void set_registry(Map<String, Process> registry) {
		this.registry = registry;
	}
	
	
	public void run() {
		int number = Integer.parseInt(this.name.substring(1));
		
		if (number%2 == 0) {
			try {
				Channel c = this.accept(new Random().nextInt(2));
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		else {
			String host;
			host = "P" + new Random().nextInt(this.registry.size());
			try {
				Channel c = this.connect(host, new Random().nextInt(2));
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
