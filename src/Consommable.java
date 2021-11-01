import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Consommable {
	/**
	 * Identifiant de l'instance du consommable dans la BD.
	 */
	private int id;
	/**
	 * Nombre d'utilisations restantes de l'instance du consommable.<br>
	 * S'il est null, nombre d'utilisations infini.<br>
	 * S'il est inferieur a 0, il ne disparait pas mais est inutilisable.
	 * Supposé disparaître à 0.
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
	 * Effets de tous les consommables existants dans la BD.
	 */
	private static ArrayList<ArrayList<EFFET>> effets = null;
	
	
	/**
	 * Activer tous les effets de l'objet, puis décrémenter la durabilité.
	 */
	public void utiliser() {
		//Renvoyer les effets du consommable.
		ArrayList<EFFET> effets = EFFET.getEffets().get(id);
		
		//Activer tous les effets du consommable.
		for(int i = 0; i < effets.size(); i++) {
			effets.get(i).activer();
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
	public static ArrayList<ArrayList<EFFET>> getEffets() {
		return effets;
	}
	
}
