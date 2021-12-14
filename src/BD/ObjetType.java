package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * Pour les objets (Type Objets, c'est à dire les objets et NON les instances d'objets), elles seront créés dans cette classe.<br>
 * Un type objet possède un identifiant, un nom, une durabilité max, et une capacité (pour simplifier l'utilisation d'un objet.
 * 
 * @author Dylan Toledano
 *
 */
public class ObjetType {
	public final int objetTypeId;
	public final String nom;
	public final int durabiliteMax;
	public final Capacite capacite;
	/**
	 * Constructeur d'ObjetType.
	 * @param objetTypeId
	 * Identifiant de l'ObjetType
	 * @param nom
	 * Nom de l'ObjetType
	 * @param durabiliteMax
	 * Durabilité de l'ObjetType
	 * @param capacite
	 * Capacité assigné à l'ObjetType
	 */
	public ObjetType(int objetTypeId, String nom, int durabiliteMax, Capacite capacite) {
		this.objetTypeId = objetTypeId;
		this.nom = nom;
		this.durabiliteMax = durabiliteMax;
		this.capacite = capacite;
	}
	
	/**
	 * Constructeur pour ObjetType à partir d'une table SQL.
	 * @param table
	 * Table à choisir.
	 * @throws SQLException
	 * Si le joueur n'arrive pas à se connecter.
	 */
	ObjetType(ResultSet table) throws SQLException {
		this(
			table.getInt("objet_id"),
			table.getString("nom"),
			table.getInt("durabilite_max"),
			BD.getCapacites().get(table.getInt("capacite_id"))
		);
	}
	
	/**
	 * Télécharge les données.
	 * @throws SQLException
	 * Si le joueur n'arrive pas à se connecter
	 */
	public static void telecharger() throws SQLException {
		ResultSet objetTypeTable = BD.telecharger("objet_type");
		while(objetTypeTable.next()) {
			int objetTypeId = objetTypeTable.getInt("objet_type_id");
			
			BD.getObjetTypes().put(
				objetTypeId,
				new ObjetType(
					objetTypeId,
					objetTypeTable.getString("nom"),
					objetTypeTable.getInt("durabilite_max"),
					BD.getCapacites().get(objetTypeTable.getInt("capacite_id"))
				)
			);
		}
	}
	
}
