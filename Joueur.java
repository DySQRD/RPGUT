import java.util.ArrayList;
import java.util.HashMap;

public class Joueur {
	private String pseudo;
	private int id;
	private int xp;
	private Stats stats;
	private Inventaire inventaire;

	public Joueur(String pseudo, int id, int xp, int pv, int attaque, int vitesse, Inventaire inventaire) {
		this.id = id;
		this.pseudo = pseudo;
		this.xp = xp;
		this.stats = new Stats(pv, attaque, vitesse);
		this.inventaire = inventaire;
	}
	
	public Joueur(String pseudo, int id, int xp, Stats stats, Inventaire inventaire) {
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

	public void setStats(Stats stats) {
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

	public void setInventaire(Inventaire inventaire) {
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
