package BD;
import java.util.ArrayList;

public class Objet {
	/**
	 * Identifiant définissant le type d'objet.
	 */
	public final ObjetType objetType;
	/**
	 * Nombre d'utilisations restantes.
	 */
	private int durabilite;
	
	Objet(ObjetType objetType) {
		this.objetType = objetType;
		this.durabilite = objetType.durabiliteMax;
	}
	
	/**
	 * Crée un objet basé sur les caractéristiques d'un objet de la BD.
	 * @param id
	 */
	Objet(int objetTypeId) {
		this(BD.getObjetTypes().get(objetTypeId));
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
	
	public int getDurabiliteMax() {
		return objetType.durabiliteMax;
	}

	public int getDurabilite() {
		return durabilite;
	}
	
	/**
	 * Si l'argument est inférieur à 0, la durabilité devient 0.<br>
	 * Si l'argument est supérieur à la durabilité maximale de l'objet, la durabilité devient la durabilité maximale.
	 * @param durabilite
	 */
	public void setDurabilite(int durabilite) {
		if(durabilite < 0) this.durabilite = 0;
		else if(durabilite > objetType.durabiliteMax) this.durabilite = objetType.durabiliteMax;
		else this.durabilite = durabilite;
	}
	
	/**
	 * Un raccourci d'écriture pour simuler += tout en conservant l'encapsulation.
	 * @param ajustement	La valeur à ajouter à durabilite.
	 * @see	setDurabilite
	 */
	public void ajusterDurabilite(int ajustement) {
		setDurabilite(getDurabilite() + ajustement);
	}

	public String getNom() {
		return objetType.nom;
	}
	
}
