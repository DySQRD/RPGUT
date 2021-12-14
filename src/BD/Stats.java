package BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Stats extends HashMap<String, Integer> {
	private static final long serialVersionUID = 8435193944096939444L;
	public static final String[] statsOrdre = {"pv_max", "attaque", "defense"};
	
	/**
	 * Construit les stats à partir d'un ResultSet ayant les mêmes attributs que la table stats de la BD.
	 * @param statsTable
	 * @throws SQLException
	 */
	public Stats(ResultSet statsTable) throws SQLException {
		for(int i = 0; i < statsOrdre.length; i++) {
			put(statsOrdre[i], statsTable.getInt(statsOrdre[i]));
		}
	}

	public Stats(int... stats) {
		for(int i = 0; i < statsOrdre.length; i++) {
			put(statsOrdre[i], stats[i]);
		}
	}
	public Stats() {
		this(0, 0, 0);
	}
	
	static void sauvegarder() throws SQLException {
		Stats stats = BD.personnage.getStats();
		BD.informer("UPDATE stats SET pv_max = ?, attaque = ?, defense = ? WHERE stats_id = ?",
			stats.get("pv_max"),
			stats.get("attaque"),
			stats.get("defense"),
			BD.joueurTable.getInt("stats_id")
		);
	}
}
