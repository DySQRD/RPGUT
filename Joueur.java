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
	
	

	public String toString() {
		return 	" pseudo : " 	+ pseudo +
				" pv : " 		+ stats.get("pv") +
				" xp : " 		+ xp +
				" vitesse : " 	+ stats.get("vitesse") +
				" attaque : " 	+ stats.get("attaque");
	}
}
