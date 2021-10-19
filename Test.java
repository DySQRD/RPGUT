import java.sql.SQLException;

import Exceptions.JoueurExisteException;

public class Test {

	public static void main(String[] args) throws SQLException, JoueurExisteException {
		BD bd = new BD();
		bd.inscrire("Woodman", "thenicest");
	}
	
}
