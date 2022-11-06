import com.google.gson.JsonObject;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

	Socket socket;
	public static ArrayList<JsonObject> clients = new ArrayList<>();

	public static ArrayList<JsonObject> getClients() {
		return clients;
	}

	public Client(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		super.run();

		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			os.write("HTTP 1/0 OK\r\n\r\n".getBytes());
			int r;
			String res = "";
			while((r=is.read())!=-1 && !res.contains("\n")) {
				res+=(char)r;
			}

			res = res.replaceFirst("GET ", "").replace(" HTTP/1.1", "").trim();
			if(!res.contains("favicon.ico")) {
				String[] s = res.split("\\?(?!\\?)");
				os.write((Main.req.use(s[0].replace("/", ""), s[1])+"\r\n").getBytes());
			}

			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
