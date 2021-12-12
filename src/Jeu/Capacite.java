package Jeu;

import BD.Stats;

public class Capacite {
	/* Dans nos id�es actuels, une capacit� poss�de un identifiant, un nom,une description, un type, des d�gats, une pr�cision, et peut �liminer
	 * en un coup, d'o� le bool�an "oneshot"*/
	

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
	 * Le constructeur des capacit�s de type offensive.
	 * @param name_
	 * @param description_
	 * @param damage_
	 * @param precision_
	 * @param oneshot_
	 * @param user
	 */
	public Capacite(String name_, String description_, int damage_, int precision_, boolean oneshot_, String target){
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
	 * Le constructeur des capacit�s de type soutien.
	 * @param name_
	 * @param description_
	 * @param precision_
	 * @param soutien
	 * @param up
	 * @param down
	 * @param cible
	 */
	public Capacite(String name_, String description_, int precision_,Categorie soutien, int up, int down,String target){
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
	 * Retourne le nom de la capacit�
	 * @return name
	 * or le nom qu'aura cette capacit�
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Permet de retourner la description de la capacit�.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 *Permet d'obtenir les d�g�ts qu'inflige la capacit� (en cas de capacit� offensive)
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}
	
	/**
	 * Permet d'obtenir la pr�cision de la capacit�.
	 * @return precision
	 */
	public float getPrecision() {
		return precision;
	}
	
	/**
	 * Permet de savoir la capacit� (de type offensive) mettra l'adversaire hors jeu en un coup.
	 * @return oneshot
	 * si true donc elle mettra l'adversaire KO en un coup, sinon non.
	 */
	public boolean isOneshot() {
		return oneshot;
	}
	
	/**
	 * Permet d'obtenir la cat�gorie (Offensive, (Up/Down)(Attack/Defense/Health)
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
	 * permet d'obtenir la r�duction de statistique
	 * @return down
	 */
	public int getDown() {
		return down;
	}
	
	/**
	 * Retourne l'entit� qui sera la cible de la capacit�
	 * @return target
	 * peut �tre soit le personnage contr�l�, soit un mob quelconque.
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * Permet d'utiliser une capacit�, quel soit offensive, ou d�fensive (l'augmentation ou la r�duction de statistiques).
	 */
	public void use(Entity user) {
		Entity mob =FirstApplication.loopManager.getGameLoop().perso.mobVS;
		Entity character = FirstApplication.loopManager.getGameLoop().perso;
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
		} else System.out.println(user.getNom() +" a rat� sa capacit� !");
	}
	
	public boolean equals(Capacite capacite) {
		if(name.equals(capacite.name) && description .equals(capacite.description) && categorie.equals(capacite.categorie)) return true;
		else return false;
	}
}