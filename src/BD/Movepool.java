package BD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
	/**
	 * 
	 * @author Dylan TOLEDANO
	 *
	 */
public class Movepool extends HashMap<Integer, Capacite>{
	private static final long serialVersionUID = 386462555382801832L;

	/**
	 * Crée une liste de capacité à attribuer à une entité.
	 * @throws SQLException 
	 */
	Movepool(ResultSet table) throws SQLException{
		while(table.next()) {
			put(
				table.getInt("ordre"),
				BD.getCapacites().get(table.getInt("capacite_id"))
			);
		}
	}

}
