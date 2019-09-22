import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ChannelTest {
	
	Map<String, Process> registry = new HashMap<String, Process>();
	private int nbProcess = 4; // Number of process to create and run simultaneously
	
	
	public void start() throws InvalidPropertiesFormatException, IOException {
		for (int i = 0 ; i < nbProcess ; i++) {
			Server s = new Server("P" + i++); // Create the process
			this.registry.put(s.name, s); // Add the process to the registry
			System.out.println("Create process " + s.name);
			
			Client c = new Client("P" + i); // Create the process
			this.registry.put(c.name, c); // Add the process to the registry
			System.out.println("Create process " + c.name);
		}
		
		for (int i = 0 ; i < nbProcess ; i++) {
			Process s = this.registry.get("P" + i);
			s.set_registry(registry); // Give the complete registry to the process
			s.setDaemon(true);
			s.start();
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