package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetType {
	final int objetTypeId;
	final String nom;
	final int durabiliteMax;
	
	public ObjetType(int objetTypeId, String nom, int durabiliteMax) {
		this.objetTypeId = objetTypeId;
		this.nom = nom;
		this.durabiliteMax = durabiliteMax;
	}
	
	ObjetType(ResultSet table) throws SQLException {
		this(table.getInt("objet_id"), table.getString("nom"), table.getInt("durabilite_max"));
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
					objetTypeTable.getInt("durabilite_max")
				)
			);
		}
	}
	
}
