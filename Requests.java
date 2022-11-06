public class Requests {

	@Request
	public String connect(String code, String conn) {
		return code + " : " + conn;
	}
}
