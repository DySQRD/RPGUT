import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entite {
	protected int id;
	protected int xp;
	protected HashMap<String, Integer> stats;
	protected ArrayList<Consommable> inventaire;
	
	public Entite(int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire) {
		this.id = id;
		this.xp = xp;
		this.stats = stats;
		this.inventaire = inventaire;
	}
	/*
	 * Getters et setters
	 */

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
		return 	" xp : " 		+ xp +
				" pv : " 		+ stats.get("pv") +
				" vitesse : " 	+ stats.get("vitesse") +
				" attaque : " 	+ stats.get("attaque") +
				" objets : "	+ inventaire;
	}
}
