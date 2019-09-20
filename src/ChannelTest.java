import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ChannelTest {
	
	Map<String, Process> registry = new HashMap<String, Process>();
	private int nbProcess = 2; // Number of process to create and run simultaneously
	
	
	public void start() throws InvalidPropertiesFormatException, IOException {
		for (int i = 1 ; i <= nbProcess ; i++) {
			Process p = new Process("P" + i); // Create the process
			this.registry.put(p.name, p); // Add the process to the registry
		}
		
		for (int i = 1 ; i <= nbProcess ; i++) {
			Process p = this.registry.get("P" + i);
			p.set_registry(registry); // Give the complete registry to the process
			p.setDaemon(true);
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