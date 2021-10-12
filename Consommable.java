
public class Consommable extends Objet {
	/**
	 * Nombre d'utilisations restantes de l'objet.
	 * S'il est null, nombre d'utilisations infini.
	 * S'il est inferieur a 0, il ne disparait pas mais est inutilisable.
	 */
	private int nbUtilisations;
	
	
	public Consommable(int nbUtilisations) {
		setNbUtilisations(nbUtilisations);
	}
	
	public void utiliser() {
		//code incomplet ici
		setNbUtilisations(getNbUtilisations() - 1);
	}

	public int getNbUtilisations() {
		return nbUtilisations;
	}

	public void setNbUtilisations(int nbUtilisations) {
		this.nbUtilisations = nbUtilisations;
	}
}
