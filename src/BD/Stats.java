package BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Stats extends HashMap<String, Integer> {
	private static final long serialVersionUID = 8435193944096939444L;
	private static String[] statsOrdre = {"xp", "pv", "attaque", "defense"};
	
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
		this(0, 0, 0, 0);
	}
}
