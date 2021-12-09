package Jeu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BD.BD;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstApplication extends Application {

    //Actions avant l'application
    @Override
    public void init() throws Exception {
        System.out.println("Before");
    }

    //Lancement de l'application
    @Override
    public void start(Stage stage) throws Exception {
        //Désérialisation du tileset dans tileset1
        BD.identifier("Dylan", "Toledano");
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
            level1.addMap(maps.get(i));
        }

        maps.get(1).setSpawnX(242);
        maps.get(1).setSpawnY(242);
        level1.loadLevel();

        maps.get(2).spawnMobs(10, "Maths", "Minion");
        maps.get(5).spawnMobs(10, "Maths", "Minion");


        //Zones de texte
        Label fps1 = new Label();     // Text
        fps1.setTextFill(Color.BLUE);
        fps1.setFont(Font.font("",FontWeight.BOLD, 22));

        Label fps3 = new Label();     // Text
        fps3.setTextFill(Color.RED);
        fps3.setFont(Font.font("",FontWeight.BOLD, 22));

        Label mouseLocation = new Label();
        mouseLocation.setLayoutX(700);
        mouseLocation.setLayoutY(30);
        mouseLocation.setTextFill(Color.MIDNIGHTBLUE);
        mouseLocation.setFont(Font.font("",FontWeight.BOLD, 18));

        //Personnages
        //TODO A remplacer par le personnage téléchargé avec :
        //Personnage perso1 = BD.getPersonnage();
        Personnage perso1 = BD.getPersonnage();


        // Création fenêtre
        Stage window = new Stage();
        window = stage;

        //Configuration fenêtre
        window.setTitle("Project Game");
        window.setWidth(900);
        window.setHeight(600);      // Taille des maps : 576 x 896
        window.setResizable(false);

        //Création des root (Layout manager) 576 x 896
        Group root = new Group();
        root.getChildren().add(level1.getMap(level1.getCurrentMap()).getCanvas());
        root.getChildren().addAll(perso1.imageV, fps1, mouseLocation);
        //Root -> scene
        Scene scene1 = new Scene(root);

        //Scene -> stage et affichage stage
        window.setScene(scene1);
        window.show();

        //Loops
        GameLoop gameLoop = new GameLoop(perso1,level1,fps1, mouseLocation, root);
        LoopManager loopManager = new LoopManager(gameLoop, gameLoop.combatLoop);

        scene1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseLocation.setText("x = " + mouseEvent.getSceneX() + ", y = "+mouseEvent.getSceneY());
            }
        });

        //Lancement de la Loop jeu
        gameLoop.start();



        //Pression d'une touche sur la scene1
        scene1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : if(loopManager.currentLoop instanceof GameLoop){gameLoop.up=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorUp();break;
                    }
                    case DOWN : if(loopManager.currentLoop instanceof GameLoop){gameLoop.down=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorDown();break;
                    }
                    case LEFT : if(loopManager.currentLoop instanceof GameLoop){gameLoop.left=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorLeft();break;
                    }
                    case RIGHT : if(loopManager.currentLoop instanceof GameLoop){gameLoop.right=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorRight();break;
                    }
                    case ESCAPE: if (loopManager.currentLoop instanceof GameLoop){}
                        else if(loopManager.currentLoop instanceof CombatLoop){
                            loopManager.combatLoop.escape();break;
                    }
                    case SPACE: if(loopManager.currentLoop instanceof GameLoop){}
                        else if(loopManager.currentLoop instanceof CombatLoop){
                        ((CombatLoop) loopManager.currentLoop).select();
                    }
                }
            }
        });

        //Relâchement d'une touche sur la scene1
        scene1.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : gameLoop.up=false; break;
                    case DOWN : gameLoop.down=false; break;
                    case LEFT : gameLoop.left=false; break;
                    case RIGHT : gameLoop.right=false; break;
                }
            }
        });
    }



    //Actions après l'application
    @Override
    public void stop() throws Exception {
        System.out.println("After");
    }
}

