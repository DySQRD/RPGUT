package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

import Jeu.Capacite;

public class ObjetType {
	final int objetTypeId;
	final String nom;
	final int durabiliteMax;
	final Capacite capacite;
	
	public ObjetType(int objetTypeId, String nom, int durabiliteMax, Capacite capacite) {
		this.objetTypeId = objetTypeId;
		this.nom = nom;
		this.durabiliteMax = durabiliteMax;
		this.capacite = capacite;
	}
	
	ObjetType(ResultSet table) throws SQLException {
		this(
			table.getInt("objet_id"),
			table.getString("nom"),
			table.getInt("durabilite_max"),
			BD.getCapacites().get(table.getInt("capacite_id"))
		);
	}
	
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
