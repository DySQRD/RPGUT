package Jeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    private String name;
    private HashMap<Integer, Map> maps = new HashMap<Integer, Map>();
    protected int currentMap = 0;

    public Level(String name){
        this.name = name;
    }
    public void addMap(int id, Map map){
        this.maps.put(id, map);
    }
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

    public void switchMap(String sortie){
        if(sortie.equals("sortie1")) this.currentMap -=3;
    else if (sortie.equals("sortie2")) this.currentMap++;
    else if (sortie.equals("sortie3")) this.currentMap +=3;
    else if (sortie.equals("sortie4")) this.currentMap--;
    }
}
