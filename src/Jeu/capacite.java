package Jeu;

import BD.Stats;

public class Capacite {
	/* Dans nos id�es actuels, une capacit� poss�de un identifiant, un nom,une description, un type, des d�gats, une pr�cision, et peut �liminer
	 * en un coup, d'o� le bool�an "oneshot"*/
	

	private String name;
	private String description;
	private final Categorie categorie;
	private float damage;
	private float precision;
	private boolean oneshot;
	private int up;
	private int down;
	private String target;
	
	/**
	 * Le constructeur des capacit�s de type offensive.
	 * @param name_
	 * @param description_
	 * @param damage_
	 * @param precision_
	 * @param oneshot_
	 * @param user
	 */
	Capacite(String name_, String description_, float damage_, float precision_, boolean oneshot_, String target){
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
	Capacite(String name_, String description_, float precision_,Categorie soutien, int up, int down,String target){
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
	 * Permet de red�finir le nom de la capacit�
	 * @param name 
	 * le nouveau nom
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Permet de retourner la description de la capacit�.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Permet de red�finir la description de la capacit�
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 *Permet d'obtenir les d�g�ts qu'inflige la capacit� (en cas de capacit� offensive)
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}
	
	/**
	 * Permet de red�finir l'attaque d'une capacit�.
	 * @param damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	/**
	 * Permet d'obtenir la pr�cision de la capacit�.
	 * @return precision
	 */
	public float getPrecision() {
		return precision;
	}
	
	/**
	 *Permet de red�finir la pr�cision de la capacit�.
	 * @param precision
	 */
	public void setPrecision(float precision) {
		this.precision = precision;
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
	 * Permet de red�finir le bool�en oneshot
	 * @param oneshot
	 */
	public void setOneshot(boolean oneshot) {
		this.oneshot = oneshot;
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
	 * permet de red�finir la hausse de statistique
	 * @param up
	 */
	public void setUp(int up) {
		this.up = up;
	}
	
	/**
	 * permet d'obtenir la r�duction de statistique
	 * @return down
	 */
	public int getDown() {
		return down;
	}
	
	/**
	 * permet de red�finir la r�duction de statistique
	 * @param down
	 */
	public void setDown(int down) {
		this.down = down;
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
	 * Red�finit la cible de la capacit�
	 * @param target
	 * repr�sente cette cible l�.
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * Permet d'utiliser une capacit�, quel soit offensive, ou d�fensive (l'augmentation ou la r�duction de statistiques).
	 */
	public void use(Entity user) {
		Entity mob = GameLoop.perso.mobVS;
		Entity character = GameLoop.perso;
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