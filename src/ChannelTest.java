import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ChannelTest {
	
	Map<String, Process> registry = new HashMap<String, Process>();
	private int nbProcess = 2;
	
	
	public void start() throws InvalidPropertiesFormatException, IOException {
		for (int i = 1 ; i <= nbProcess ; i++) {
			Process p = (new Process("P" + nbProcess));
			p.set_registry(registry);
			p.setDaemon(true);
			this.registry.put(p.name, p);
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