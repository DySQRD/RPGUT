package Jeu;

import BD.Stats;

public class Capacite {
	/* Dans nos idées actuels, une capacité possède un identifiant, un nom,une description, un type, des dégats, une précision, et peut éliminer
	 * en un coup, d'où le booléan "oneshot"*/
	

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
	 * Le constructeur des capacités de type offensive.
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
	 * Le constructeur des capacités de type soutien.
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
	 * Retourne le nom de la capacité
	 * @return name
	 * or le nom qu'aura cette capacité
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Permet de redéfinir le nom de la capacité
	 * @param name 
	 * le nouveau nom
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Permet de retourner la description de la capacité.
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Permet de redéfinir la description de la capacité
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 *Permet d'obtenir les dégâts qu'inflige la capacité (en cas de capacité offensive)
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}
	
	/**
	 * Permet de redéfinir l'attaque d'une capacité.
	 * @param damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	/**
	 * Permet d'obtenir la précision de la capacité.
	 * @return precision
	 */
	public float getPrecision() {
		return precision;
	}
	
	/**
	 *Permet de redéfinir la précision de la capacité.
	 * @param precision
	 */
	public void setPrecision(float precision) {
		this.precision = precision;
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
	 * Permet de redéfinir le booléen oneshot
	 * @param oneshot
	 */
	public void setOneshot(boolean oneshot) {
		this.oneshot = oneshot;
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
	 * permet de redéfinir la hausse de statistique
	 * @param up
	 */
	public void setUp(int up) {
		this.up = up;
	}
	
	/**
	 * permet d'obtenir la réduction de statistique
	 * @return down
	 */
	public int getDown() {
		return down;
	}
	
	/**
	 * permet de redéfinir la réduction de statistique
	 * @param down
	 */
	public void setDown(int down) {
		this.down = down;
	}
	
	/**
	 * Retourne l'entité qui sera la cible de la capacité
	 * @return target
	 * peut être soit le personnage contrôlé, soit un mob quelconque.
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * Redéfinit la cible de la capacité
	 * @param target
	 * représente cette cible là.
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	/**
	 * Permet d'utiliser une capacité, quel soit offensive, ou défensive (l'augmentation ou la réduction de statistiques).
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
		} else System.out.println(user.getNom() +" a raté sa capacité !");
	}
	
	public boolean equals(Capacite capacite) {
		if(name.equals(capacite.name) && description .equals(capacite.description) && categorie.equals(capacite.categorie)) return true;
		else return false;
	}
}