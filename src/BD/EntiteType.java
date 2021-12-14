package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntiteType {
	public final int entiteTypeId;
	public final String nom;
	public final Stats stats;
	public final int xpLoot;
	public final int xHitbox;
	public final int yHitbox;
	public final Movepool movepool;
	
	EntiteType(int entiteTypeId, String nom, Stats stats, int xpLoot, int xHitbox, int yHitbox, Movepool movepool) {
		super();
		this.entiteTypeId = entiteTypeId;
		this.nom = nom;
		this.stats = stats;
		this.xpLoot = xpLoot;
		this.xHitbox = xHitbox;
		this.yHitbox = yHitbox;
		this.movepool = movepool;
	}

	EntiteType(ResultSet table) throws SQLException {
		this(
			table.getInt("entite_type_id"),
			table.getString("nom"),
			new Stats(table),
			table.getInt("xp_loot"),
			table.getInt("xHitbox"),
			table.getInt("yHitbox"),
			BD.getMovepools().get(table.getInt("movepool_id"))
		);
	}
	
	static void telecharger() throws SQLException {
		ResultSet entiteTypeTable = BD.querir("SELECT * FROM entite_type NATURAL JOIN stats");
		while(entiteTypeTable.next()) {
			int entiteType = entiteTypeTable.getInt("entite_type_id");
			
			BD.getEntiteTypes().put(
				entiteType,
				new EntiteType(
					entiteType,
					entiteTypeTable.getString("nom"),
					new Stats(
						entiteTypeTable.getInt("pv_max"),
						entiteTypeTable.getInt("attaque"),
						entiteTypeTable.getInt("defense")
					),
					entiteTypeTable.getInt("xp_loot"),
					entiteTypeTable.getInt("xHitbox"),
					entiteTypeTable.getInt("yHitbox"),
					BD.getMovepools().get(entiteTypeTable.getInt("movepool_id"))
				)
			);
		}
	}
	
}
