package BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Inventaire extends HashMap<Integer, Objet> {
	private static final long serialVersionUID = 7469243752520600817L;

	public Inventaire(int joueurId) throws SQLException {
		this(BD.querir("SELECT * FROM objet WHERE joueur_id = ?", joueurId));
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
					inventaireTable.getInt("objet_type_id"),
					inventaireTable.getInt("durabilite")
			));
		}
	}
}
