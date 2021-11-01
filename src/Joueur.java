import java.util.ArrayList;
import java.util.HashMap;

public class Joueur {
	private String pseudo;
	private int id;
	private int xp;
	private HashMap<String, Integer> stats;
	private ArrayList<Consommable> inventaire = new ArrayList<Consommable>();

	public Joueur(String pseudo, int id, int xp, int pv, int attaque, int vitesse) {
		this.pseudo = pseudo;
		this.xp = xp;
		stats.put("pv", pv);
		stats.put("attaque", attaque);
		stats.put("vitesse", vitesse);
	}

	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	public String toString() {
		return "pseudo : " + pseudo + super.toString() + " ";
	}
}
