package Jeu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
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
    // Variables représentant les touches du clavier
    private static boolean up, down, left,right;

    //Actions avant l'application
    @Override
    public void init() throws Exception {
        System.out.println("Before");
    }

    //Lancement de l'application
    @Override
    public void start(Stage stage) throws Exception {

        // Création fenêtre
        Stage window = new Stage();
        window = stage;

        //Configuration fenêtre
        window.setTitle("Teemo");
        window.setWidth(900);
        window.setHeight(600);
        window.setResizable(false);


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



        map1.addTileset(tileset1);
        map2.addTileset(tileset1);
        map3.addTileset(tileset1);
        map4.addTileset(tileset1);
        map5.addTileset(tileset1);
        map6.addTileset(tileset1);


        //Création du niveau
        Level level1 = new Level("Level 1");
        level1.addMap(map1);
        level1.addMap(map2);
        level1.addMap(map3);
        level1.addMap(map4);
        level1.addMap(map5);
        level1.addMap(map6);
        level1.loadLevel();


        //Création des root (Layout manager)
        Group root = new Group();
        Group root_bis = new Group();

        //canvas -> root
        root.getChildren().add(level1.getMap(level1.getCurrentMap()).getCanvas());

        //Zones de texte
        Label label1 = new Label();     // Text
        label1.setTextFill(Color.BLUE);

        Label mouseLocation = new Label();
        mouseLocation.setLayoutX(700);
        mouseLocation.setLayoutY(30);
        mouseLocation.setTextFill(Color.MIDNIGHTBLUE);
        mouseLocation.setFont(Font.font("",FontWeight.BOLD, 18));



        //Personnages
        Personnage perso1 = new Personnage(map1.getSpawnX(), map1.getSpawnY(), "C:/Users/marc_/IdeaProjects/Le_jeu_test/src/Images/teemo1.png");

        //Nodes -> root
        root.getChildren().addAll(perso1.imageV, perso1.hitbox, root_bis, label1, mouseLocation);

        //Root -> scene
        Scene scene1 = new Scene(root);

        //Scene -> stage et affichage stage
        window.setScene(scene1);
        window.show();

        scene1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseLocation.setText("x = " + mouseEvent.getSceneX() + ", y = "+mouseEvent.getSceneY());
            }
        });

        //Pression d'une touche sur la scene1
        scene1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : up=true; break;
                    case DOWN : down=true; break;
                    case LEFT : left=true; break;
                    case RIGHT : right=true; break;
                    case SPACE :
                        System.out.println(""+level1.getCurrentMap());break;
                }
            }
        });

        //Relâchement d'une touche sur la scene1
        scene1.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : up=false; break;
                    case DOWN : down=false; break;
                    case LEFT : left=false; break;
                    case RIGHT : right=false; break;
                }
            }
        });




        //Initialisation GameLoop
        AnimationTimer gameLoop = new AnimationTimer() {
            private long delta = 0;
            private long lastFrameTime=0;
            private long fps;
            private long lastFPSTime;
            double rectangleX, rectangleY;

            //GameLoop (60 fois par seconde)
            @Override
            public void handle(long now) {
                //Compteur de fps
                delta = now - lastFrameTime;
                lastFrameTime = now;
                fps = (long) 1e9 / delta;

                if(up){

                    perso1.hitbox.setY(perso1.hitbox.getY()-perso1.velocity);
                    for(int i=0; i<level1.getMap(level1.getCurrentMap()).getObstacles().size(); i++){
                        if(perso1.hitbox.getBoundsInParent().intersects(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                            //obstacles.get(i).setFill(Color.BLUE);
                            if(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                                root.getChildren().clear();

                                perso1.tp(perso1.posX, 530);

                                level1.switchMap(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).name);

                                root.getChildren().addAll(level1.getMap(level1.getCurrentMap()).getCanvas(), label1, mouseLocation, perso1.imageV);
                            }
                            perso1.collision = true; break;

                        }
                        else{
                            //obstacles.get(i).setFill(Color.RED);
                        }
                    }
                    if(!(perso1.collision)) perso1.moveUp();
                    perso1.collision = false;
                    perso1.hitbox.setY(perso1.hitbox.getY()+perso1.velocity);
                }
                if(down){
                    perso1.hitbox.setY(perso1.hitbox.getY()+perso1.velocity);
                    for(int i=0; i<level1.getMap(level1.getCurrentMap()).getObstacles().size(); i++){
                        if(perso1.hitbox.getBoundsInParent().intersects(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                            //obstacles.get(i).setFill(Color.BLUE);
                            if(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                                root.getChildren().clear();

                                perso1.tp(perso1.posX, 30);

                                level1.switchMap(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).name);

                                root.getChildren().addAll(level1.getMap(level1.getCurrentMap()).getCanvas(), label1, mouseLocation, perso1.imageV);
                            }
                            perso1.collision = true; break;

                        }
                        else{
                            //obstacles.get(i).setFill(Color.RED);
                        }
                    }
                    if(!(perso1.collision)) perso1.moveDown();
                    perso1.collision = false;
                    perso1.hitbox.setY(perso1.hitbox.getY()-perso1.velocity);
                }
                if(left){
                    perso1.hitbox.setX(perso1.hitbox.getX()-perso1.velocity);
                    for(int i=0; i<level1.getMap(level1.getCurrentMap()).getObstacles().size(); i++){
                        if(perso1.hitbox.getBoundsInParent().intersects(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                            //obstacles.get(i).setFill(Color.BLUE);
                            if(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                                root.getChildren().clear();
                                perso1.tp(830, perso1.posY);

                                level1.switchMap(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).name);

                                root.getChildren().addAll(level1.getMap(level1.getCurrentMap()).getCanvas(), label1, mouseLocation, perso1.imageV);
                            }
                            perso1.collision = true; break;

                        }
                        else{
                            //obstacles.get(i).setFill(Color.RED);
                        }
                    }
                    if(!(perso1.collision)) perso1.moveLeft();
                    perso1.collision = false;
                    perso1.hitbox.setX(perso1.hitbox.getX()+perso1.velocity);
                }
                if(right){
                    perso1.hitbox.setX(perso1.hitbox.getX()+perso1.velocity);
                    for(int i=0; i<level1.getMap(level1.getCurrentMap()).getObstacles().size(); i++){
                        if(perso1.hitbox.getBoundsInParent().intersects(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                            //obstacles.get(i).setFill(Color.BLUE);
                            if(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                                root.getChildren().clear();

                                perso1.tp(30, perso1.posY);

                                level1.switchMap(level1.getMap(level1.getCurrentMap()).getObstacles().get(i).name);

                                root.getChildren().addAll(level1.getMap(level1.getCurrentMap()).getCanvas(), label1, mouseLocation, perso1.imageV);
                            }
                            perso1.collision = true; break;

                        }
                        else{
                            //obstacles.get(i).setFill(Color.RED);
                        }
                    }
                    if(!(perso1.collision)) perso1.moveRight();
                    perso1.collision = false;
                    perso1.hitbox.setX(perso1.hitbox.getX()-perso1.velocity);
                }

                //Gestion collision avec les obstacles

                //Réduction du rafraichissement du compteur fps
                if((now - lastFPSTime)>(1e8)){
                    label1.setText(""+level1.getCurrentMap());
                    lastFPSTime = now;
                }
            }
        };

        //Lancement de la Loop
        gameLoop.start();

    }

    //Actions après l'application
    @Override
    public void stop() throws Exception {
        System.out.println("After");
    }
}

