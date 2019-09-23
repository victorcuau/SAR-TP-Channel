import java.util.Random;

public class Client extends Process {

	protected Client(String name) {
		super(name);
	}

	public void run() {
		try {
			for (int i = 0 ; i < 2 ; i++) {
				
				// Find on which host do the connect
				String host;
				host = "P" + new Random().nextInt(this.registry.size());
				
				// Connexion request
				int port = new Random().nextInt(2);
				Channel c = this.connect(host, port);
				
				// Write something on the channel
				byte[] tab = new byte[1];
				tab[0] = 1;
				int nbWrite = c.write(tab, 0, 1);
				System.out.println("Process " + this.name + " have write " + nbWrite + " byte(s)");
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
