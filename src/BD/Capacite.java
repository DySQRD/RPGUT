package BD;

import java.sql.ResultSet;
import java.sql.SQLException;

import Jeu.Entity;
import Jeu.FirstApplication;
import Jeu.Mob;
import Jeu.Personnage;
/**
 * 
 * Classe permettant de créer des capacités, leur nom, leur effet, si elle mette ko en un coup, leur dégâts, précisions et la cible.<br>
 * Chaque capacité possède une cible et un lanceur.
 * 
 * @author Nasser AZROU-ISGHI
 *
 */
public class Capacite {
	/* Dans nos idées actuels, une capacité possède un identifiant, un nom,une description, un type, des dégats, une précision, et peut éliminer
	 * en un coup, d'où le booléan "oneshot"*/
	
	private final int capaciteId;
	private final String name;
	private final String description;
	private final Categorie categorie;
	private final int damage;
	private final int precision;
	private final boolean oneshot;
	private final int up;
	private final int down;
	private final String target;
	
	
	/**
	 * Le constructeur des capacités de type offensive.
	 * @param name_
	 * Nom de la capacité.
	 * @param description_
	 * Sa description.
	 * @param damage_
	 * Dégâts à infliger.
	 * @param precision_
	 * Précision de la capacité.	 * 
	 * @param oneshot_
	 * Si la capacité met KO en un coup.
	 * @param target
	 * La cible de la capacité.
	 */
	public Capacite(int capaciteId, String name_, String description_, int damage_, int precision_, boolean oneshot_, String target){
		this.capaciteId = capaciteId;
		name=name_;
		description=description_;
		categorie=Categorie.Offensive;
		damage=damage_;
		precision=precision_;
		oneshot=oneshot_;
		this.target=target;
		up=0;
		down=0;		
	}
	
	/**
	 * Le constructeur des capacités de type soutien.
	 * @param name_
	 * Nom de capacité.
	 * @param description_
	 * Description de chaque capacité.
	 * @param precision_
	 * Précision de chaque capacité.
	 * @param soutien
	 * Catégorie de la capacité de type soutien.
	 * @param up
	 * De combien la statistique sera augmenté.
	 * @param down
	 * De combien la statistique sera réduite.
	 * @param target
	 * Cible de la capacité.
	 */
	public Capacite(int capaciteId, String name_, String description_, int precision_,Categorie soutien, int up, int down,String target){
		this.capaciteId = capaciteId;
		name = name_;
		description=description_;
		categorie=soutien;
		damage=0;
		precision=precision_;
		this.up=up;
		this.down=down;
		oneshot=false;
		this.target=target;
	}
	
	/**
	 * 
	 */
	public int getCapaciteId() {
		return capaciteId;
	}
	
	/**
	 * Retourne le nom de la capacité
	 * @return name
	 * or le nom qu'aura cette capacité
	 */
	public String getName() {
		return name;
	}

	/**
	 * Permet de retourner la description de la capacité.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
		
	/**
	 *Permet d'obtenir les dégâts qu'inflige la capacité (en cas de capacité offensive)
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}
	
	/**
	 * Permet d'obtenir la précision de la capacité.
	 * @return precision
	 */
	public float getPrecision() {
		return precision;
	}
	
	/**
	 * Permet de savoir la capacité (de type offensive) mettra l'adversaire hors jeu en un coup.
	 * @return oneshot
	 * si true donc elle mettra l'adversaire KO en un coup, sinon non.
	 */
	public boolean isOneshot() {
		return oneshot;
	}
	
	/**
	 * Permet d'obtenir la catégorie (Offensive, (Up/Down)(Attack/Defense/Health)
	 * @return categorie
	 */
	public Categorie getCategorie() {
		return categorie;
	}
	
	/**
	 * permet d'obtenir la hausse de statistique
	 * @return up
	 */
	public int getUp() {
		return up;
	}
	
	/**
	 * permet d'obtenir la réduction de statistique
	 * @return down
	 */
	public int getDown() {
		return down;
	}
	
	/**
	 * Retourne l'entité qui sera la cible de la capacité
	 * @return target
	 * peut être soit le personnage contrôlé soit un mob quelconque.
	 */
	public String getTarget() {
		return target;
	}


	/**
	 * Permet d'utiliser une capacité, quel soit offensive, ou défensive (l'augmentation ou la réduction de statistiques) en fonction du lanceur.
	 * @param user
	 * représente le lanceur.
	 * */
	public void use(Entity user) {
		Mob mob = BD.getPersonnage().getMobVS();
		Personnage character = BD.getPersonnage();
		Entity targetE;
		if(Math.random() <= (precision/100)) {
		if(this.target.equals("adversaire")) {
	        targetE = (user instanceof Personnage) ? mob : character;
	    } else targetE = user;
		
		Stats statBase = targetE.getStats();
		Stats statBonus = targetE.statsBonus;
		
		if(categorie==Categorie.Offensive) { 
			if(this.oneshot) statBase.put("pv_max",0); 
			else targetE.
			setPV(targetE.getPV()-(statBase.get("attaque") + statBonus.get("attaque") - (statBase.get("defense") - statBonus.get("defense"))));
			}
		if(categorie==Categorie.DownAttaque) statBonus.put("attaque",statBonus.get("attaque")-statBase.get("attaque")/down);
		if(categorie==Categorie.DownDefense) statBonus.put("defense", statBonus.get("defense")-statBase.get("defense")/down);
		if(categorie==Categorie.DownHealth) {
			if(statBonus.get("pv_max")-statBase.get("pv_max")/up >= 0) statBonus.put("pv_max",statBonus.get("pv_max")-statBase.get("pv_max")/up);
		else statBonus.put("pv_max",0);
		}
		if(categorie==Categorie.UpAttaque) statBonus.put("pv_max",statBonus.get("attaque")+statBase.get("attaque")/up);
		if(categorie==Categorie.UpDefense) statBonus.put("pv_max",statBonus.get("defense")+statBase.get("defense")/up);
		if(categorie==Categorie.UpHealth) {
			if(statBonus.get("pv_max")+statBase.get("pv_max")/up <= statBase.get("pv_max")) statBonus.put("pv_max",statBonus.get("pv_max")+statBase.get("pv_max")/up);}
		else statBonus.put("pv_max",statBonus.get("pv_max"));

		} else System.out.println(user.getNom() +" a raté sa capacité !");
	}
	
	/**
	 * Vérifie si deux capacités sont égales.
	 * @param capacite
	 * la capacité à sélectionner
	 * @return
	 * Vrai si ces deux capacités sont égales.
	 */
	public boolean equals(Capacite capacite) {
		if(name.equals(capacite.name) && description .equals(capacite.description) && categorie.equals(capacite.categorie)) return true;
		else return false;
	}
	
	static void telecharger() throws SQLException {
		ResultSet capaciteTable = BD.telecharger("capacite");
		while(capaciteTable.next()) {
			Capacite cap;
			if((capaciteTable.getString("categorie")).equals("Offensive")) {
				cap = new Capacite(
					capaciteTable.getInt("capacite_id"),
					capaciteTable.getString("nom"),
					capaciteTable.getString("description"),
					capaciteTable.getInt("puissance"),
					capaciteTable.getInt("precisionn"),
					capaciteTable.getBoolean("oneshot"),
					capaciteTable.getString("cibles")
				);
			} else {
				cap = new Capacite(
					capaciteTable.getInt("capacite_id"),
					capaciteTable.getString("nom"),
					capaciteTable.getString("description"),
					capaciteTable.getInt("precisionn"),
					Categorie.valueOf(capaciteTable.getString("categorie")),
					capaciteTable.getInt("up"),
					capaciteTable.getInt("down"),
					capaciteTable.getString("cibles")
				);
			}
			BD.capacites.put(capaciteTable.getInt("capacite_id"), cap);
		}
	}
}