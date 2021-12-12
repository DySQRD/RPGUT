package BD;

public class Capacite {
	int capaciteId;
	String nom;
	int puissance;
	int precision;
	String cibles;
	int up;
	int down;
	String description;
	Categorie categorie;
	
	public Capacite(int capaciteId, String nom, int puissance, int precision, String cibles, int up, int down,
			String description, Categorie categorie) {
		super();
		this.capaciteId = capaciteId;
		this.nom = nom;
		this.puissance = puissance;
		this.precision = precision;
		this.cibles = cibles;
		this.up = up;
		this.down = down;
		this.description = description;
		this.categorie = categorie;
	}
	
	
}
