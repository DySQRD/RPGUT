package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetType {
	final int objetTypeId;
	final String nom;
	final int durabilite;
	
	public ObjetType(int objetTypeId, String nom, int durabilite) {
		this.objetTypeId = objetTypeId;
		this.nom = nom;
		this.durabilite = durabilite;
	}
	
	ObjetType(ResultSet table) throws SQLException {
		this(table.getInt("objet_id"), table.getString("nom"), table.getInt("durabilite"));
	}
}
