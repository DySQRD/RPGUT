import java.util.ArrayList;

public class Consommable {
	//Note à moi-même : pas besoin d'attribut nom puisqu'il est partagé par tous les Consommables de la même id.
	//Peut changer si on donne la possibilité de personnaliser les noms.
	/**
	 * Identifiant de l'instance du consommable dans la BD.
	 */
	private int id;
	/**
	 * Nombre d'utilisations restantes de l'instance du consommable.
	 */
	private int durabilite;
	
	/**
	 * Nom de tous les consommables existants dans la BD.
	 */
	private static ArrayList<String> noms = null;
	/**
	 * Durabilité par défaut de tous les consommables existants dans la BD.
	 */
	private static ArrayList<Integer> durabilites = null;
	/**
	 * Actions effectuées lors de l'utilisation de l'objet.
	 */
	private static ArrayList<ArrayList<Action>> actions;
	
	Consommable(int id, int durabilite) {
		this.id = id;
		this.durabilite = durabilite;
	}
	
	/**
	 * Activer tous les effets de l'objet, puis décrémenter la durabilité.
	 */
	public void utiliser() {
		//Renvoyer les effets du consommable.
		ArrayList<Action> actions = getActions().get(id);
		
		//Activer tous les effets du consommable.
		for(int i = 0; i < actions.size(); i++) {
			actions.get(i).utiliser();
		}
		
		//Décrémente la durabilité de l'objet de 1.
		setDurabilite(getDurabilite() - 1);
	}
	
	
	
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
	
	
	
	/*
	 * Getters pour les données STATIQUES de la BD.
	 */

	public static ArrayList<String> getNoms() {
		return noms;
	}
	public static ArrayList<Integer> getDurabilites() {
		return durabilites;
	}
	public static ArrayList<ArrayList<Action>> getActions() {
		return actions;
	}
	
}
