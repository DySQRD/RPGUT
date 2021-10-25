import java.util.HashMap;

public class Stats extends HashMap<String, Integer> {
	public Stats(int pv, int attaque, int vitesse) {
		put("pv", pv);
		put("attaque", attaque);
		put("vitesse", vitesse);
	}
}
