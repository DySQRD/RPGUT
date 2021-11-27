import java.util.HashMap;

public abstract class Entite {
	protected int id;
	protected Stats stats;
	protected HashMap<Integer, Objet> inventaire;
	
	public Entite(int id, Stats stats, HashMap<Integer, Objet> inventaire) {
		this.id = id;
		this.stats = stats;
		this.inventaire = inventaire;
	}
	/*
	 * Getters et setters
	 */
	
	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<Integer, Objet> getInventaire() {
		return inventaire;
	}

	public void setInventaire(HashMap<Integer, Objet> inventaire) {
		this.inventaire = inventaire;
	}
	
	
	/*
	 * Fonctions classiques
	 */

	@Override
	public String toString() {
		return 	" xp : " 		+ stats.get("xp") +
				" pv : " 		+ stats.get("pv") +
				" vitesse : " 	+ stats.get("vitesse") +
				" attaque : " 	+ stats.get("attaque") +
				" objets : "	+ inventaire;
	}
}
