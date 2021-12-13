package Jeu;

import BD.BD;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author Marc SANCHEZ
 *
 */
public class GameLoop extends AnimationTimer {
    protected long delta = 0;
    protected long lastFrameTime=0;
    protected long fps;
    protected long lastFPSTime;
    protected Personnage perso;
    protected Group root;
    protected Label fpsLabel = new Label();
    protected Label hpLabel;
    protected Label mouseLocationLabel = new Label();
    protected CombatLoop combatLoop;
    protected PauseLoop pauseLoop;
    protected LoopManager loopManager;
    protected HashMap<Integer, Level> levels = new HashMap<Integer, Level>();
    protected int currentLevel;

    protected boolean up, down, left, right;

    protected double width, height;

    /**
     * Une boucle de jouabilité pour gérer les niveaux, leurs map etc
     * @param root
     * L'objet Group permettant de stocker tout node.
     * @throws IOException
     * Erreur d'input/output
     * @throws SQLException
     * Erreur de BDD/SQL.
     */
    public GameLoop(Group root) throws IOException, SQLException {

        //Désérialisation du tileset dans tileset1
        Gson gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("res/maps/default_set.json")));
        Tileset tileset1 = gsonBuilder.fromJson(json, Tileset.class);

        //Désérialisation des map

        HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
        //Création du niveau
        Level level1 = new Level("Level 1");

        for(int i = 1; i <= 9; i++) {
            json = new String(Files.readAllBytes(FileSystems.getDefault()
                    .getPath("res/maps/map" + i + ".json")));
            maps.put(i, gsonBuilder.fromJson(json, Map.class));
            maps.get(i).addTileset(tileset1);
            level1.addMap(i, maps.get(i));
            System.out.println(i + " " + level1.getMapsList().size());
        }

        maps.get(1).setSpawnX(242);
        maps.get(1).setSpawnY(242);
        level1.loadLevel();
        levels.put(1, level1);
        this.currentLevel = 1;

        //maps.get(2).spawnMobs(10, "Maths", "Minion");
        //maps.get(5).spawnMobs(10, "Maths", "Minion");

        /*for(Integer lvl : BD.getEntites().keySet() ){
            for(Integer map : BD.getEntites().get(lvl).keySet()){
                for(Integer mob : BD.getEntites().get(lvl).get(map).keySet()){
                    levels.get(lvl).getMap(map).spawnMobs(BD.getEntites().get(lvl).get(map).get(mob));

                }
            }
        }*/


        //Zones de texte
        fpsLabel.setTextFill(Color.BLUE);
        fpsLabel.setFont(Font.font("", FontWeight.BOLD, 22));

        mouseLocationLabel.setLayoutX(700);
        mouseLocationLabel.setLayoutY(30);
        mouseLocationLabel.setTextFill(Color.MIDNIGHTBLUE);
        mouseLocationLabel.setFont(Font.font("",FontWeight.BOLD, 18));

        //Personnage
        this.perso = BD.getPersonnage();

        currentLevel = BD.getJoueurTable().getInt("niveau_id");
        levels.get(currentLevel).currentMap = BD.getJoueurTable().getInt("map_id");
        System.out.println("mapjsp: " + levels.get(currentLevel).currentMap);

        //Création des root (Layout manager) 576 x 896
        /*root.getChildren().add(level1.getMap(level1.getCurrentMap()).getCanvas());
        root.getChildren().addAll(perso1.imageV, fps1, mouseLocation);*/
        //Root -> scene

        this.root = root;
        this.width = levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getCanvas().getWidth();
        this.height = levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getCanvas().getHeight();
        this.mouseLocationLabel = mouseLocationLabel;
        this.combatLoop = new CombatLoop(width/8, height/8, width*3/4, height*3/4, this);
        this.pauseLoop = new PauseLoop(width, height, this);
    }

    /**
     * Met à jour l'affichage de l'écran (Niveau, map, et monstres).
     */
    public void displayUpdate(){
        this.root.getChildren().clear();
        this.root.getChildren().addAll(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getCanvas(), fpsLabel, mouseLocationLabel, perso.imageV);
        
        System.out.println("level: " + currentLevel);
        System.out.println("map: " + levels.get(currentLevel).currentMap);
        System.out.println("entites: " + BD.getEntites());
        System.out.println(BD.getEntites().get(currentLevel).keySet());
        
        if(BD.getEntites().get(currentLevel).containsKey(levels.get(currentLevel).currentMap)) {
            for(Integer mobID : getCurrentLevelMapMobs().keySet()){
            	root.getChildren().add(getCurrentLevelMapMobs().get(mobID).imageV);
            }
        }
    }
    
    /**
     * à utiliser en combat. Gère le tour par tour pour les capacités, la fuite, et les objets.
     */
    public void enterCombat(){
        this.loopManager.combat();
    }
    
    /**
     *	permet de mettre en pause la boucle de jouabilité. 
     */
    public void enterPause(){
    	this.loopManager.pause();
    	}
    
    /**
     * 
     */
    @Override
    public void handle(long now) {
        //compteur de fps
        this.delta = now - lastFrameTime;
        this.lastFrameTime = now;
        this.fps = (long) 1e9 / delta;
        if (up) {
            perso.hitbox.setY(perso.hitbox.getY() - perso.velocity);
            for (int i = 0; i < levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().size(); i++) {
                if (perso.hitbox.getBoundsInParent().intersects(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())) {
                    if (levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).type.equals("sortie")) {
                        perso.tp(perso.posX, 500);
                        levels.get(currentLevel).switchMap(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).name);
                        this.displayUpdate();
                    }
                    perso.collision = true;
                    break;
                }
            }
            if (!(perso.collision)) perso.moveUp();
            perso.collision = false;
            perso.hitbox.setY(perso.hitbox.getY() + perso.velocity);
            checkCombatPerso();
        }
        if (down) {
            perso.hitbox.setY(perso.hitbox.getY() + perso.velocity);
            for (int i = 0; i < levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().size(); i++) {
                if (perso.hitbox.getBoundsInParent().intersects(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())) {
                    //obstacles.get(i).setFill(Color.BLUE);
                    if (levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).type.equals("sortie")) {
                        perso.tp(perso.posX, 30);
                        levels.get(currentLevel).switchMap(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true;
                    break;

                }
            }
            if (!(perso.collision)) perso.moveDown();
            perso.collision = false;
            perso.hitbox.setY(perso.hitbox.getY() - perso.velocity);
            checkCombatPerso();
        }

        if (left) {
            perso.hitbox.setX(perso.hitbox.getX() - perso.velocity);
            for (int i = 0; i < levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().size(); i++) {
                if (perso.hitbox.getBoundsInParent().intersects(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())) {
                    if (levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).type.equals("sortie")) {
                        perso.tp(830, perso.posY);
                        levels.get(currentLevel).switchMap(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true;
                    break;

                }
            }
            if (!(perso.collision)) perso.moveLeft();
            perso.collision = false;
            perso.hitbox.setX(perso.hitbox.getX() + perso.velocity);
            checkCombatPerso();
        }

        if (right) {
            perso.hitbox.setX(perso.hitbox.getX() + perso.velocity);
            for (int i = 0; i < levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().size(); i++) {
                if (perso.hitbox.getBoundsInParent().intersects(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())) {
                    //obstacles.get(i).setFill(Color.BLUE);
                    if (levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).type.equals("sortie")) {
                        perso.tp(30, perso.posY);
                        levels.get(currentLevel).switchMap(levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true;
                    break;

                }
            }
            if (!(perso.collision)) perso.moveRight();
            perso.collision = false;
            perso.hitbox.setX(perso.hitbox.getX() - perso.velocity);
            checkCombatPerso();
        }

        if ((now - lastFPSTime) > (1e8)) {
            fpsLabel.setText("" + fps);
            lastFPSTime = now;
        }

        if(!(BD.getEntites().size() == 0)) {
            if (BD.getEntites().get(currentLevel).containsKey(levels.get(currentLevel).currentMap)) {
                for (Integer mobID : getCurrentLevelMapMobs().keySet()) {
                    int random = (int) (Math.random() * 4);
                    Mob mob = getCurrentLevelMapMobs().get(mobID);
                    int xMove = 0, yMove = 0;
                    switch (random) {
                        case 0:
                            yMove = -mob.velocity;
                            break;
                        case 1:
                            yMove = mob.velocity;
                            break;
                        case 2:
                            xMove = -mob.velocity;
                            break;
                        case 3:
                            xMove = mob.velocity;
                            break;
                    }
                    mob.hitbox.setY(mob.hitbox.getY() + yMove);
                    mob.hitbox.setX(mob.hitbox.getX() + xMove);
                    for (int k = 0; k < getCurrentMap().getObstacles().size(); k++) {
                        if ((mob.hitbox.getBoundsInParent().intersects(getCurrentMap().getObstacles().get(k).hitbox.getBoundsInParent()))) {
                            mob.collision = true;
                        }
                    }
                    mob.hitbox.setY(mob.hitbox.getY() - yMove);
                    mob.hitbox.setX(mob.hitbox.getX() - xMove);
                    if (!(mob.collision)) {
                        switch (random) {
                            case 0:
                                mob.moveUp();
                                break;
                            case 1:
                                mob.moveDown();
                                break;
                            case 2:
                                mob.moveLeft();
                                break;
                            case 3:
                                mob.moveRight();
                                break;
                        }
                    }
                    mob.collision = false;
                }
            }
        }
        checkCombatMob();
    }
/**
     * Vérifie si le personnage est entré en collision avec un monstre, et donc si le combat est initié par le joueur.
     */
    public void checkCombatPerso() {
        if (!(BD.getEntites().size() == 0)) {
            if (BD.getEntites().get(currentLevel).containsKey(levels.get(currentLevel).currentMap)) {
                for (Integer mobID : getCurrentLevelMapMobs().keySet()) {
                    if (perso.hitbox.getBoundsInParent().intersects(getCurrentLevelMapMobs().get(mobID).hitbox.getBoundsInParent())) {
                        perso.mobVS = getCurrentLevelMapMobs().get(mobID);
                        loopManager.combatLoop.tourMob = false;
                        enterCombat();
                    }
                }
            }
        }
    }
  /**
     * Vérifie si le mob est entré en collision avec un personnage, et donc si le combat est initié par un monstre.
     */
    public void checkCombatMob() {
        if (!(BD.getEntites().size() == 0)) {
            if (BD.getEntites().get(currentLevel).containsKey(levels.get(currentLevel).currentMap)) {
                for (Integer mobID : getCurrentLevelMapMobs().keySet()) {
                    if (perso.hitbox.getBoundsInParent().intersects(getCurrentLevelMapMobs().get(mobID).hitbox.getBoundsInParent())) {
                        perso.mobVS = getCurrentLevelMapMobs().get(mobID);
                        loopManager.combatLoop.tourMob = true;
                        enterCombat();
                    }
                }
            }
        }
    }
    
    /**
     * Retourne le niveau.
     * @return currentLevel
     * L'id du niveau actuel
     */
    public int getCurrentLevelId() {
    	return currentLevel;
    }
    
    /**
     * Permet de définir l'id du niveau actuel
     * @param levelId
     * l'id du niveau à sélectionner.
     */
    public void setCurrentLevelId(int levelId) {
    	currentLevel = levelId;
    }
    
    /**
     * Retourne un objet Level.
     * @return levels.get(currentLevel)
     * l'objet level
     */
    public Level getCurrentLevel() {
    	return levels.get(currentLevel);
    }
    /**
     * Retourne la map actuelle.
     * @return levels.get(currentLevel).currentMap
     * la map actuel du niveau actuel
     */
    public int getCurrentMapId() {
    	return levels.get(currentLevel).currentMap;
    }
    
    /**
     * Permet de définir la map.
     * @param mapId
     * l'id de la map à définir.
     */
    public void setCurrentMapId(int mapId) {
    	levels.get(currentLevel).currentMap = mapId;
    }
    
    /**
     * Retourne un objet Map
     * @return levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap())
     * l'objet Map (généralement la map sur laquelle on se situe).
     */
    private Map getCurrentMap() {
    	return levels.get(currentLevel).getMap(levels.get(currentLevel).getCurrentMap());
    }
    
    /**
     * Retourne la map sur laquelle chaque mob se situe.
     * @return BD.getEntites().get(currentLevel)
     * toutes entités sur chaque map de chaque niveau.
     */
    private HashMap<Integer, HashMap<Integer, Mob>> getCurrentLevelMaps() {
    	return BD.getEntites().get(currentLevel);
    }
    
    /**
     * Retourne les mobs de la map actuelle.
     * @return BD.getEntites().get(currentLevel).get(levels.get(currentLevel).currentMap)
     * les entités(dont le joueur) à la map actuelle du level actuel.
     */
    private HashMap<Integer, Mob> getCurrentLevelMapMobs() {
    	return BD.getEntites().get(currentLevel).get(levels.get(currentLevel).currentMap);
    }
}