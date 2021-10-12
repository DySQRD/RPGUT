import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * 
 * @author dydyt
 *
 *
 */
public class Inventaire {
	private ArrayList<Objet> inventaire;
	private Joueur joueur;
	
	public Inventaire(Joueur joueur) {
		CRUD crud = new CRUD();
		
	}
	
	
	
	
	
	
	public ArrayList<Objet> getInventaire() {
		return inventaire;
	}

	public void setInventaire(ArrayList<Objet> inventaire) {
		this.inventaire = inventaire;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	
}
