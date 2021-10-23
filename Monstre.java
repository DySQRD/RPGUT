import java.util.ArrayList;
import java.util.List;

public class Monstre {
	private String type; 
	private String nom;
	private int attaque;
	private int pv;
	private int niveau;
	Object objet; //Objet que le mob peut drop
	List<Object> capacit�es = new ArrayList<Object>(); //Liste des capacit� du mob
	
	
	

	public Monstre(String type, String nom, int attaque, int pv, int niveau, Object objet, List<Object> capacit�es) {
		this.type = type;
		this.nom = nom;
		this.attaque = attaque;
		this.pv = pv;
		this.niveau = niveau;
		this.objet = objet;
		this.capacit�es = capacit�es;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}
	
	public List<Object> getCapacit�es() {
		return capacit�es;
	}

	public void setCapacit�es(List<Object> capacit�es) {
		this.capacit�es = capacit�es;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getAttaque() {
		return attaque;
	}
	
	public void setAttaque(int attaque) {
		this.attaque = attaque;
	}
	
	public Object getObjet() {
		return objet;
	}
	
	public void setObjet(Object objet) {
		this.objet = objet;
	}
	
	public void addcapacit�e(Object capa) {
		capacit�es.add(capa);
	}
	
	public boolean dropObjet(int chance) { //Retourne si l'objet est lach� ou non 
		return (Math.random()*100<100);
	}

//	public void useCapacit�e(int i) {    //Utilise la capacit�e dans la liste 
//		capacit�es.get(i).activer()
//	}
}
