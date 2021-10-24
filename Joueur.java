import java.util.ArrayList;
import java.util.HashMap;

public class Joueur {
	private String pseudo;
	private int id;
	private int xp;
	private HashMap<String, Integer> stats;
	private ArrayList<Consommable> inventaire;

	public Joueur(String pseudo, int id, int xp, int pv, int attaque, int vitesse, ArrayList<Consommable> inventaire) {
		this.id = id;
		this.pseudo = pseudo;
		this.xp = xp;
		this.stats = new HashMap<String, Integer>();
		stats.put("pv", pv);
		stats.put("attaque", attaque);
		stats.put("vitesse", vitesse);
		this.inventaire = inventaire;
	}
	
	public Joueur(String pseudo, int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire) {
		this.id = id;
		this.pseudo = pseudo;
		this.xp = xp;
		this.stats = stats;
		this.inventaire = inventaire;
	}
	
	
	
	/*
	 * Getters et setters
	 */

	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getXp() {
		return xp;
	}
	public void setXp(int xp) {
		this.xp = xp;
	}
	
	public HashMap<String, Integer> getStats() {
		return stats;
	}

	public void setStats(HashMap<String, Integer> stats) {
		this.stats = stats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Consommable> getInventaire() {
		return inventaire;
	}

	public void setInventaire(ArrayList<Consommable> inventaire) {
		this.inventaire = inventaire;
	}
	
	
	/*
	 * Fonctions classiques
	 */

	@Override
	public String toString() {
		return 	" pseudo : " 	+ pseudo +
				" xp : " 		+ xp +
				" pv : " 		+ stats.get("pv") +
				" vitesse : " 	+ stats.get("vitesse") +
				" attaque : " 	+ stats.get("attaque") +
				" objets : "	+ inventaire;
	}
}
