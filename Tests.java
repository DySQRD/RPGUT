import java.sql.SQLException;

import Exceptions.JoueurAlreadyExistsException;

public class Tests {
	
	public static void main(String[] args) throws SQLException, JoueurAlreadyExistsException {
		CRUD test = new CRUD();
		test.inscription("Dydy", "nice");
	}

}
