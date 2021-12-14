package Jeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Une collection de map est un niveau. <br>
 * Le joueur pourra se balader dessus en combattant des monstres.<br>
 * Chaque niveau/level contient 9 maps et un nom.
 * 
 * @author Marc SANCHEZ
 *
 */

public class Level {
	/**
	 * Nom du niveau
	 */
    private String name;
    
    /**
     * Collection contenant les maps du niveau.
     */
    private HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
    protected int currentMap = 0;

    /**
     * Constructeur pour la création du niveau et son nom.
     * @param name
     * Nom du niveau.
     */
    public Level(String name){
        this.name = name;
    }
    
    /**
     * Ajout des maps au niveau.
     * @param id
     * Identifiant de la map.
     * @param map
     * L'objet map.
     */
    public void addMap(int id, Map map){
        this.maps.put(id, map);
    }
    
    /**
     * Charge les textures, obstacles des maps du niveau.
     * @throws IOException
     * Erreur In/Out.
     */
    public void loadLevel() throws IOException {
        this.maps.get(1).tileset.loadbImage();
        for(Integer mapId : this.maps.keySet()){
            this.maps.get(mapId).loadTiles();
            this.maps.get(mapId).renderMap();
            this.maps.get(mapId).loadCanvas();
            this.maps.get(mapId).setObstacles();
        }
    }

    public HashMap<Integer, Map> getMapsList(){
        return this.maps;
    }

    public Map getMap(int n){
        return this.maps.get(n);
    }

    public int getCurrentMap(){
        return currentMap;
    }
    
    /**
     * Permet le changement de map.
     * @param sortie
     * la sortie choisie.
     */
    public void switchMap(String sortie){
        if(sortie.equals("sortie1")) this.currentMap -=3;
    else if (sortie.equals("sortie2")) this.currentMap++;
    else if (sortie.equals("sortie3")) this.currentMap +=3;
    else if (sortie.equals("sortie4")) this.currentMap--;
    }
}
