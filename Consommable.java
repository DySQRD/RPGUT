import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Consommable {
	/**
	 * Identifiant de l'instance du consommable dans la BD.
	 */
	private int id;
	/**
	 * Nombre d'utilisations restantes de l'objet.<br>
	 * S'il est null, nombre d'utilisations infini.<br>
	 * S'il est inferieur a 0, il ne disparait pas mais est inutilisable.
	 * Supposé disparaître à 0.
	 */
	private int durabilite;
	
	private static ArrayList<String> noms = null;
	private static ArrayList<Integer> durabilites = null;
	//EFFET.valueOf(res.getString(0)); a utiliser
	
	
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
