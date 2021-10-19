import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Joueur {
	private String pseudo;
	private int id;
	private int xp;
	private HashMap<String, Integer> stats;
	/*private int pv;
	private int vitesse;
	private int degat;*/
	private ArrayList<Consommable> inventaire = new ArrayList<Consommable>();
	private BD bd;

	public Joueur(String pseudo, int id, int xp, int pv, int attaque, int vitesse) {
		this.pseudo = pseudo;
		this.xp = xp;
		stats.put("pv", pv);
		stats.put("attaque", attaque);
		stats.put("vitesse", vitesse);
		/*this.pv = pv;
		this.vitesse = vitesse;
		this.xp = xp;
		this.degat =degat ;*/
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

	public float getXp() {
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

	/*

	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getVitesse() {
		return vitesse;
	}
	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}
	
	public int getDegat() {
		return degat;
	}
	public void setDegat(int degat) {
		this.degat = degat;
	}*/

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

	public BD getBd() {
		return bd;
	}

	public void setBd(BD bd) {
		this.bd = bd;
	}
	
	
	
	/*
	 * Fonctions classiques
	 */

	public String toString() {
		return 	" pseudo : " 	+ pseudo +
				" xp : " 		+ xp +
				" pv : " 		+ stats.get("pv") +
				" vitesse : " 	+ stats.get("vitesse") +
				" attaque : " 	+ stats.get("attaque");
	}
}
