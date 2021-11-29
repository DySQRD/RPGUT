import java.util.HashMap;

public class Stats extends HashMap<String, Integer> {
	private static final long serialVersionUID = 8435193944096939444L;

	public Stats(int... stats) {
		put("xp", stats[0]);
		put("pv", stats[1]);
		put("attaque", stats[2]);
		put("defense", stats[3]);
	}
	
	public static void main(String[] args) {
		HashMap<String, Integer> huh = new HashMap<String, Integer>();
		huh.put("huh", 0);
		Stats test = new Stats(1, 2);
	}
}
