package BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Inventaire extends HashMap<Integer, Objet> {
	
	public Inventaire() {
		super();
	}
	
	/**
	 * Construit un Inventaire à partir d'un ResultSet ayant les mêmes attributs que la table inventaire de la BD.
	 * @param inventaireTable
	 * @throws SQLException
	 */
	public Inventaire(ResultSet inventaireTable) throws SQLException {
		while(inventaireTable.next()) {
			this.put(
				inventaireTable.getInt("ordre"),
				new Objet(
					inventaireTable.getInt("objet_id"),
					inventaireTable.getString("nom"),
					inventaireTable.getInt("durabilite")
			));
		}
	}
}
