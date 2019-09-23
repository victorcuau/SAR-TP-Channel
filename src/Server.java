import java.util.Random;

public class Server extends Process {

	protected Server(String name) {
		super(name);
	}
	
	public void run() {
		try {
			for (int i = 0 ; i < 2 ; i++) {
				Channel c = this.accept(new Random().nextInt(2));
				
				// Read something on the channel
				byte[] tab = new byte[1];
				int nbRead = c.read(tab, 0, 1);
				System.out.println("Process " + this.name + " have read " + nbRead + " byte(s)");
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}