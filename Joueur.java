import java.util.ArrayList;
import java.util.HashMap;

public class Joueur extends Entite {
	private String pseudo;
	
	public Joueur(int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire, String pseudo) {
		super(id, xp, stats, inventaire);
		this.pseudo = pseudo;
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
