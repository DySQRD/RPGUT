package BD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
	/**
	 * Chaque entité aura un movepool, qui aurait été créé avec cette classe.
	 * 
	 * @author Dylan TOLEDANO
	 *
	 */
public class Movepool extends HashMap<Integer, Capacite>{
	private static final long serialVersionUID = 386462555382801832L;

	/**
	 * Crée une liste de capacité à attribuer à une entité.
	 * @throws SQLException 
	 * S'il est impossible de se connecter a la BD.
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
