package BD;
import java.util.ArrayList;
import java.util.HashMap;

public class Objet {
	/**
	 * Identifiant définissant le type d'objet.
	 */
	public final int objetTypeId;
	/**
	 * Nombre d'utilisations restantes.
	 */
	private int durabilite;
	
	Objet(int objetTypeId, int durabilite) {
		this.objetTypeId = objetTypeId;
		this.durabilite = durabilite;
	}
	
	/**
	 * Crée un objet basé sur les caractéristiques d'un objet de la BD.
	 * @param id
	 */
	Objet(int objetTypeId) {
		ObjetType objetType = BD.getObjetTypes().get(objetTypeId);
		this.objetTypeId = objetType.objetTypeId;
		this.durabilite = objetType.durabilite;
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
	private static int randomId() {
		//Il n'y a pas moyen de récupérer une entrée aléatoirement depuis la HashMap...
		//keySet renvoie donc toutes les ids mais en tant que Set, dont on ne peut extraire un objet.
		//On transforme donc le Set en List, puis on choisit aléatoirement une id d'objet de la BD, qu'on renvoie.
		ArrayList<Integer> ids = new ArrayList<Integer>(BD.getObjetTypes().keySet());
		return ids.get((int)Math.random() * ids.size());
	}
	
	
	
	/*
	 * Getters et setters
	 */

	public int getDurabilite() {
		return durabilite;
	}
	public void setDurabilite(int durabilite) {
		this.durabilite = durabilite;
	}

	public String getNom() {
		return BD.getObjetTypes().get(objetTypeId).nom;
	}
	
}
