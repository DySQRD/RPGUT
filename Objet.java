
public abstract class Objet {
	private String nom;
	private Capacite capacite;
	
	public abstract void utiliser();

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Capacite getCapacite() {
		return capacite;
	}

	public void setCapacite(Capacite capacite) {
		this.capacite = capacite;
	}
	
	
	
	
}
