import java.util.ArrayList;
import java.util.HashMap;

public class Joueur extends Entite {

	private String pseudo;
	private int id;
	private int xp;
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private ArrayList<Consommable> inventaire = new ArrayList<Consommable>();

	public Joueur(int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire) {
		super(id, xp, stats, inventaire);
		// TODO Auto-generated constructor stub
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
