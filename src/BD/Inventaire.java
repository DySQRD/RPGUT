package BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
/**
 * 
 * @author Dylan TOLEDANO
 *
 */

public class Inventaire extends HashMap<Integer, Objet> {
	private static final long serialVersionUID = 7469243752520600817L;

	/**
	 * Télécharge l'inventaire du joueur connecté.
	 * @throws SQLException
	 */
	public Inventaire() throws SQLException {
		this(BD.querir("SELECT * FROM objet WHERE joueur_id = ?", BD.getJoueurTable().getInt("joueur_id")));
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
					inventaireTable.getInt("objet_type_id")
			));
		}
	}
	
	/**
	 * Déplace un objet d'un emplacement vers un autre.<br>
	 * Si un objet se trouve déjà à la destination, les deux objets sont intervertis.
	 * @param origine		Emplacement de l'objet à déplacer.
	 * @param destination	Emplacement vers lequel déplacer.
	 */
	public void deplacer(int origine, int destination) {
		put(origine, put(destination, get(origine)));
	}
}
