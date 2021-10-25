import java.util.ArrayList;
import java.util.HashMap;

public class Joueur extends Entite {
	private String pseudo;
	
	public Joueur(String pseudo, int id, int xp, int pv, int attaque, int vitesse, ArrayList<Consommable> inventaire) {
		this.id = id;
		this.pseudo = pseudo;
		this.xp = xp;
		this.stats = new HashMap<String, Integer>();
		this.inventaire = inventaire;
	}
	
	public Joueur(String pseudo, int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire) {
		this.id = id;
		this.pseudo = pseudo;
		this.xp = xp;
		this.stats = stats;
		this.inventaire = inventaire;
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
