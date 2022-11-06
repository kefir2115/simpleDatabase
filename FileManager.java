import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {

	static File f = new File("data.json");
	static Gson g = new Gson();

	public static void load() {
		try {
			if(!f.exists()) {
				f.createNewFile();
				return;
			}
			Scanner s = new Scanner(f);

			String r = "";
			while(s.hasNextLine()) {
				r+=s.nextLine();
			}
			if(r.length()>0 && r!=null && !r.isEmpty() && !r.equals("null")) {
				JsonArray o = g.fromJson(r, JsonObject.class).getAsJsonArray("list");
				o.forEach((e) -> {
					Client.clients.add(e.getAsJsonObject());
				});
			}

			s.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			if(!f.exists()) f.createNewFile();
			FileWriter fw = new FileWriter(f);

			JsonObject o = new JsonObject();
			o.add("list", new JsonArray());
			for(JsonObject c : Client.clients) {
				o.get("list").getAsJsonArray().add(c);
			}
			fw.write(g.toJson(o));

			fw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
