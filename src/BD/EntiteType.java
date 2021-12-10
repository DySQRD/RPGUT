package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntiteType {
	public final int entiteTypeId;
	public final String nom;
	public final Stats stats;
	public final int xpLoot;
	
	public EntiteType(int entiteTypeId, String nom, Stats stats, int xpLoot) {
		super();
		this.entiteTypeId = entiteTypeId;
		this.nom = nom;
		this.stats = stats;
		this.xpLoot = xpLoot;
	}

	public EntiteType(ResultSet table) throws SQLException {
		this(table.getInt("entite_type_id"), table.getString("nom"), new Stats(table), table.getInt("xp_loot"));
	}
	
}
