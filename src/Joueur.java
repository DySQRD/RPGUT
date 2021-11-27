import java.util.ArrayList;
import java.util.HashMap;

public class Joueur extends Entite {

	private String pseudo;
	private int id;
	private int xp;
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private ArrayList<Objet> inventaire = new ArrayList<Objet>();

	public Joueur(int id, Stats stats, HashMap<Integer, Objet> joueurInventaire) {
		super(id, stats, joueurInventaire);
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
