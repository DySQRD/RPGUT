package Jeu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        Gson gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/default_set.json")));
        Tileset tileset1 = gsonBuilder.fromJson(json, Tileset.class);

        //Désérialisation des map
        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map1.json")));
        Map map1 = gsonBuilder.fromJson(json, Map.class);
        map1.setSpawnX(242);
        map1.setSpawnY(242);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map2.json")));
        Map map2 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map3.json")));
        Map map3 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map4.json")));
        Map map4 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map5.json")));
        Map map5 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map6.json")));
        Map map6 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map7.json")));
        Map map7 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map8.json")));
        Map map8 = gsonBuilder.fromJson(json, Map.class);

        json = new String(Files.readAllBytes(FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/map9.json")));
        Map map9 = gsonBuilder.fromJson(json, Map.class);

        map1.addTileset(tileset1);
        map2.addTileset(tileset1);
        map3.addTileset(tileset1);
        map4.addTileset(tileset1);
        map5.addTileset(tileset1);
        map6.addTileset(tileset1);
        map7.addTileset(tileset1);
        map8.addTileset(tileset1);
        map9.addTileset(tileset1);

        //Création du niveau
        Level level1 = new Level("Level 1");
        level1.addMap(map1);
        level1.addMap(map2);
        level1.addMap(map3);
        level1.addMap(map4);
        level1.addMap(map5);
        level1.addMap(map6);
        level1.addMap(map7);
        level1.addMap(map8);
        level1.addMap(map9);
        level1.loadLevel();

        map2.spawnMobs(10);
        map5.spawnMobs(10);


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
        Personnage perso1 = new Personnage(map1.getSpawnX(), map1.getSpawnY(), 5, "C:/Users/marc_/IdeaProjects/Le_jeu_test/src/Images/teemo1.png", level1);


        // Création fenêtre
        Stage window = new Stage();
        window = stage;

        //Configuration fenêtre
        window.setTitle("Project Game");
        window.setWidth(900);
        window.setHeight(600);      // Taille des maps : 576 x 896  384x597 espaces 96x150
        window.setResizable(false);

        //Création des root (Layout manager) 576 x 896 // 144 x 224
        Group root = new Group();
        root.getChildren().add(level1.getMap(level1.getCurrentMap()).getCanvas());
        root.getChildren().addAll(perso1.imageV, fps1, mouseLocation);
        //Root -> scene
        Scene scene1 = new Scene(root);

        //Scene -> stage et affichage stage
        window.setScene(scene1);
        window.show();

        //Loop
        GameLoop gameLoop = new GameLoop(perso1,level1,fps1, mouseLocation, root);
        LoopManager loopManager = new LoopManager(gameLoop, gameLoop.combatLoop);

        scene1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseLocation.setText("x = " + mouseEvent.getSceneX() + ", y = "+mouseEvent.getSceneY());
            }
        });

        //Lancement de la Loop
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
                    case SPACE: if (loopManager.currentLoop instanceof GameLoop){}
                        else if(loopManager.currentLoop instanceof CombatLoop){
                            gameLoop.level.getMap(gameLoop.level.getCurrentMap()).getMobs().remove(gameLoop.perso.mobVS);
                            loopManager.game();
                    }break;
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

