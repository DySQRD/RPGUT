import java.util.HashMap;

public abstract class Entite {
	protected int id;
	protected Stats stats;
	protected Inventaire inventaire;
	
	public Entite(int id, Stats stats, Inventaire inventaire) {
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

	public Inventaire getInventaire() {
		return inventaire;
	}

	public void setInventaire(Inventaire inventaire) {
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
