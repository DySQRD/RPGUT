
public class Objet {
	//Note à moi-même : pas besoin d'attribut nom puisqu'il est partagé par tous les Consommables de la même id.
	//Peut changer si on donne la possibilité de personnaliser les noms.
	/**
	 * Identifiant de l'instance du consommable dans la BD.
	 */
	private int id;
	private String nom;
	/**
	 * Nombre d'utilisations restantes de l'instance du consommable.
	 */
	private int durabilite;
	
	Objet(int id, String nom, int durabilite) {
		this.id = id;
		this.nom = nom;
		this.durabilite = durabilite;
	}
	
	/**
	 * Activer tous les effets de l'objet, puis décrémenter la durabilité.
	 */
	/*public void utiliser() {
		//Renvoyer les effets du consommable.
		ArrayList<Action> actions = getActions().get(id);
		
		//Activer tous les effets du consommable.
		for(int i = 0; i < actions.size(); i++) {
			actions.get(i).utiliser();
		}
		
		//Décrémente la durabilité de l'objet de 1.
		setDurabilite(getDurabilite() - 1);
	}*/
	
	
	
	/*
	 * Getters et setters des instances de consommables.
	 */
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getDurabilite() {
		return durabilite;
	}
	public void setDurabilite(int durabilite) {
		this.durabilite = durabilite;
	}
	
}
