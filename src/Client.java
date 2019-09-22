import java.util.Random;

public class Client extends Process {

	protected Client(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		try {
			for (int i = 0 ; i < 2 ; i++) {
				
				// Find on which host do the connect
				String host;
				host = "P" + new Random().nextInt(this.registry.size());
				
				this.connect(host, new Random().nextInt(3));
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
