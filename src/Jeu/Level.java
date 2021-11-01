package Jeu;

import java.io.IOException;
import java.util.ArrayList;

public class Level {
    private String name;
    private ArrayList<Map>maps = new ArrayList<>();
    private int currentMap = 0;

    public Level(String name){
        this.name = name;
    }
    public void addMap(Map map){
        this.maps.add(map);
    }
    public void loadLevel() throws IOException {
        this.maps.get(0).tileset.loadbImage();
        for(int i=0; i<this.maps.size(); i++){
            this.maps.get(i).loadTiles();
            this.maps.get(i).renderMap();
            this.maps.get(i).loadCanvas();
            this.maps.get(i).setObstacles();
        }
    }

    public ArrayList<Map> getMapsList(){
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
