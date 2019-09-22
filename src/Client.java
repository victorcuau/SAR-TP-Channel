import java.util.Random;

public class Client extends Process {

	protected Client(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		try {
			for (int i = 0 ; i < 3 ; i++) {
				
				String host;
				if (this.name == "Pc1"){
						host = "Ps2";
				} else {
					host = "Ps1";
				}
				
				this.connect(host, new Random().nextInt(3));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
