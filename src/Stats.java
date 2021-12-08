import java.util.HashMap;

public class Stats extends HashMap<String, Integer> {
	private static final long serialVersionUID = 8435193944096939444L;
	private static String[] statsOrdre = {"xp", "pv", "attaque", "defense"};

	public Stats(int... stats) {
		for(int i = 0; i < statsOrdre.length; i++) {
			put(statsOrdre[i], stats[i]);
		}
	}
	public Stats() {
		this(0, 0, 0, 0);
	}
}
