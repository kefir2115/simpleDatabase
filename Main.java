import java.awt.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

public class Main {

	/*
		CREATE REQUESTS TO DEFINED GET PROTOCOL
		HOST DATABASE
	 */

	static RequestManager req = RequestManager.get();

	static ServerSocket ss;
	public static void main(String[] args) throws Exception {
		FileManager.load();
		ss = new ServerSocket(6969);
		Desktop.getDesktop().browse(new URI("http://localhost:6969/connect?code=12345&conn=54321"));

		saver();
		req.load();

		while(!ss.isClosed()) {
			Socket s = ss.accept();
			new Client(s).start();
		}

		ss.close();
	}

	public static void saver() {
		FileManager.save();
		new Thread(() -> {
			try {
				Thread.sleep(10000);
				saver();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}
