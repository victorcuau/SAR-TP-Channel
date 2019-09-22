import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ChannelTest {
	
	Map<String, Process> registry = new HashMap<String, Process>();
	private int nbProcess = 2; // Number of process to create and run simultaneously (/2)
	
	
	public void start() throws InvalidPropertiesFormatException, IOException {
		for (int i = 1 ; i <= nbProcess ; i++) {
			Server s = new Server("Ps" + i); // Create the process
			this.registry.put(s.name, s); // Add the process to the registry
			System.out.println("Create process " + s.name);
			
			Client c = new Client("Pc" + i); // Create the process
			this.registry.put(c.name, c); // Add the process to the registry
			System.out.println("Create process " + c.name);
		}
		
		for (int i = 1 ; i <= nbProcess ; i++) {
			Process s = this.registry.get("Ps" + i);
			s.set_registry(registry); // Give the complete registry to the process
			s.setDaemon(true);
			s.start();
			
			Process c = this.registry.get("Pc" + i);
			c.set_registry(registry); // Give the complete registry to the process
			c.setDaemon(true);
			c.start();
		}
	}
	
	
	public static void main(String[] args) {
		ChannelTest test = new ChannelTest();
		
		try {
			test.start();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}