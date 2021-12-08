import java.util.HashMap;

public class Monstre extends Entite {
	Consommable drop; //Objet que le mob peut drop
	private String type; 
	private String nom;
	private int attaque;
	private int pv;
	private int niveau;
	List<Object> capacitées = new ArrayList<Object>(); //Liste des capacité du mob
	

	public Monstre(String type, String nom, int attaque, int pv, int niveau, Object objet, List<Object> capacitées) {
		this.type = type;
		this.nom = nom;
		this.attaque = attaque;
		this.pv = pv;
		this.niveau = niveau;
		this.objet = objet;
		this.capacitées = capacitées;
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
	
	public List<Object> getCapacitées() {
		return capacitées;
	}

	public void setCapacitées(List<Object> capacitées) {
		this.capacitées = capacitées;
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
	
	public void addcapacitée(Object capa) {
		capacitées.add(capa);
	}

	public Monstre(int id, Stats stats, Inventaire inventaire, Objet drop) {
		super(id, stats, inventaire);
		this.drop = drop;
		// TODO Auto-generated constructor stub
	}
	
	public boolean dropObjet(int chance) { //Retourne si l'objet est lach� ou non 
		return (Math.random()*100<100);
	}
	//ajout d'une methode pour le drop des consomable a partir de l'invetaire 
	//il reste la supression dans la base de donnée
	public void removeOject(int id) {
        this.inventaire.removeIf(consommable -> consommable.getId().equals(id));
    }
}
