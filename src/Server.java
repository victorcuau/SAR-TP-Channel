import java.util.Random;

public class Server extends Process {

	protected Server(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		try {
			for (int i = 0 ; i < 3 ; i++) {
				this.accept(new Random().nextInt(3));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}