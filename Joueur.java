import java.util.ArrayList;

public class Joueur {
	private String pseudo;
	private int pv;
	private float xp;
	private int vitesse;
	private int degat;
	ArrayList<Objet> objet = new ArrayList<Objet>();

	public Joueur(String pseudo, int pv, float xp, int vitesse ,int degat) {
		this.pseudo = pseudo;
		this.pv = pv;
		this.vitesse = vitesse;
		this.xp = xp;
		this.degat =degat ;
	}

	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}

	public float getXp() {
		return xp;
	}
	public void setXp(float xp) {
		this.xp = xp;
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
	}
	
	public void utilisé(Objet objet) {
		
	}
	
	public int attaque(Joueur joueur) {
		return degat;
	}
	
	

	public String résuméjoueur() {
		return "pseudo : " +pseudo +
				" pv : " +pv +
				" xp :" +xp +
				" vitesse : " +vitesse +
				" degat : " + degat;
	}
}
