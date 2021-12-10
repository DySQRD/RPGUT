package Jeu;

import BD.Inventaire;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
	//Id d'entité dans la BD
	protected int id;
	
	//Nom de l'entité
    protected String nom;
	
    //Position à l'écran (0,0 = en haut à gauche)
    protected double posX, posY;

    //Image de l'entité
    protected ImageView imageV;

    //Vitesse de déplacement à l'écran
    protected int velocity;

    //Hitbox représentée par un rectangle
    protected Rectangle hitbox;
    protected int dxHitbox, dyHitbox;

    //Collision de l'hitbox avec une autre hitbox
    protected boolean collision;
    
    //Inventaire et Stats de l'entité
    protected Inventaire inventaire;
    protected Stats stats;

    //Caractéristiques
    protected int health_base_max;
    protected int health_bonus_max;
    protected int actual_health_max;
    protected int actual_health;

    protected int atk_base;
    protected int atk_bonus;
    protected int actual_atk;
    protected int defense_base;
    protected int defense_bonus;
    protected int actual_defense;
    protected int lvl;
    protected int totalXp = 0;

	public Entity(int id, String nom, Stats stats, Inventaire inventaire, double posX, double posY) {
    	this.id = id;
    	this.nom = nom;
        this.inventaire = inventaire;
        this.stats = stats;
        this.posX = posX;
        this.posY = posY;
    }

    //Attaque une autre entité
    public void attaque(Entity entity) {
        entity.actual_health -= (actual_atk);
    }

    //Déplacements
    abstract void tp(double x, double y);
    abstract void moveUp();
    abstract void moveDown();
    abstract void moveLeft();
    abstract void moveRight();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public ImageView getImageV() {
		return imageV;
	}

	public void setImageV(ImageView imageV) {
		this.imageV = imageV;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public int getDxHitbox() {
		return dxHitbox;
	}

	public void setDxHitbox(int dxHitbox) {
		this.dxHitbox = dxHitbox;
	}

	public int getDyHitbox() {
		return dyHitbox;
	}

	public void setDyHitbox(int dyHitbox) {
		this.dyHitbox = dyHitbox;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public Inventaire getInventaire() {
		return inventaire;
	}

	public void setInventaire(Inventaire inventaire) {
		this.inventaire = inventaire;
	}

	public int getHealth_base_max() {
		return health_base_max;
	}

	public void setHealth_base_max(int health_base_max) {
		this.health_base_max = health_base_max;
	}

	public int getHealth_bonus_max() {
		return health_bonus_max;
	}

	public void setHealth_bonus_max(int health_bonus_max) {
		this.health_bonus_max = health_bonus_max;
	}

	public int getActual_health_max() {
		return actual_health_max;
	}

	public void setActual_health_max(int actual_health_max) {
		this.actual_health_max = actual_health_max;
	}

	public int getActual_health() {
		return actual_health;
	}

	public void setActual_health(int actual_health) {
		this.actual_health = actual_health;
	}

	public int getAtk_base() {
		return atk_base;
	}

	public void setAtk_base(int atk_base) {
		this.atk_base = atk_base;
	}

	public int getAtk_bonus() {
		return atk_bonus;
	}

	public void setAtk_bonus(int atk_bonus) {
		this.atk_bonus = atk_bonus;
	}

	public int getActual_atk() {
		return actual_atk;
	}

	public void setActual_atk(int actual_atk) {
		this.actual_atk = actual_atk;
	}

	public int getDefense_base() {
		return defense_base;
	}

	public void setDefense_base(int defense_base) {
		this.defense_base = defense_base;
	}

	public int getDefense_bonus() {
		return defense_bonus;
	}

	public void setDefense_bonus(int defense_bonus) {
		this.defense_bonus = defense_bonus;
	}

	public int getActual_defense() {
		return actual_defense;
	}

	public void setActual_defense(int actual_defense) {
		this.actual_defense = actual_defense;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
    
    public int getTotalXp() {
		return totalXp;
	}

	public void setTotalXp(int totalXp) {
		this.totalXp = totalXp;
	}
    
	
}
