package BD;
import java.util.ArrayList;
import java.util.HashMap;

public class Objet {
	/**
	 * Identifiant de l'instance du consommable dans la BD.
	 */
	private int id;
	private String nom;
	private ArrayList<Action> actions = new ArrayList<Action>();
	/**
	 * Nombre d'utilisations restantes de l'instance du consommable.
	 */
	private int durabilite;
	
	Objet(int id, String nom, int durabilite, Action ...actions) {
		this.id = id;
		this.durabilite = durabilite;
		for(Action action : actions) this.actions.add(action);
	}
	
	/**
	 * Crée un objet basé sur les caractéristiques d'un objet de la BD.
	 * @param id
	 */
	Objet(int id) {
		Objet obj = BD.getObjets().get(id);
		this.nom = obj.getNom();
		this.id = obj.getId();
		this.durabilite = obj.getDurabilite();
		this.actions = obj.getActions();
	}
	
	/**
	 * Crée un objet aléatoire de la BD.
	 */
	Objet() {
		this(randomId());
	}
	
	/**
	 * Renvoie une id d'un objet de la BD.<br>
	 * Cette méthode est utilisée pour générer un objet aléatoire lors d'un drop par un mob.
	 * @return	Une id d'un objet de la BD.
	 */
	public static int randomId() {
		//Il n'y a pas moyen de récupérer une entrée aléatoirement depuis la HashMap...
		//keySet renvoie donc toutes les ids mais en tant que Set, dont on ne peut extraire un objet.
		//On transforme donc le Set en List, puis on choisit aléatoirement une id d'objet de la BD, qu'on renvoie.
		ArrayList<Integer> ids = new ArrayList<Integer>(BD.getObjets().keySet());
		return ids.get((int)Math.random() * ids.size());
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
}
