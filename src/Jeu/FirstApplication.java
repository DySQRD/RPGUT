package Jeu;

import com.google.gson.Gson;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    // Variables représentant les touches du clavier
    private static boolean up, down, left,right;

    //Actions avant l'application
    @Override
    public void init() throws Exception {
        System.out.println("Before");
    }

    /*@Override
    public void start(Stage primaryStage) {
        Circle character = new Circle(100, 100, 20, Color.RED);
        Rectangle background = new Rectangle(2000, 200, new LinearGradient(0, 0, 100, 0, false, CycleMethod.REPEAT, new Stop(0, Color.WHITE), new Stop(100, Color.BLUE)));
        Group group = new Group(background, character);
        group.setManaged(false);

        Pane root = new Pane(group);
        root.setPrefSize(200, 200);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(evt -> {
            double direction = -1;
            switch (evt.getCode()) {
                case RIGHT:
                    direction = 1;
                case LEFT:
                    double delta = direction * 10;
                    character.setTranslateX(character.getTranslateX() + delta);
                    group.setTranslateX(group.getTranslateX() - delta);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

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

        //Désérialisation de la map Json dans map1
        Path path = FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/mapTest_28_18.json");
        Gson gson = new Gson();
        String json = new String(Files.readAllBytes(path));
        Map map1 = gson.fromJson(json, Map.class);

        //Désérialisation du tileset dans tileset1
        path = FileSystems.getDefault()
                .getPath("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/maps/default_set.json");
        json = new String(Files.readAllBytes(path));
        Tileset tileset1 = gson.fromJson(json, Tileset.class);

        //Conversion (img -> BufferedImage) du tileset
        tileset1.loadbImage();

        //Mise en forme de la map
        map1.addTileset(tileset1);
        map1.loadTiles();
        map1.renderMap();

        //Création des root (Layout manager)
        Group root = new Group();
        Group root2 = new Group();

        //Création du canvas sur lequel sera dessiné la map
        Canvas canvas = new Canvas(map1.tilewidth * map1.width, map1.tileheight * map1.width);
        GraphicsContext context = canvas.getGraphicsContext2D();

        //Dessin de la map
        context.drawImage(SwingFXUtils.toFXImage(map1.imageRendered, null),0,0);

        //canvas -> root
        root.getChildren().add(canvas);

        //Zones de texte
        Label label1 = new Label();     // Text
        label1.setTextFill(Color.BLUE);

        Label label2 = new Label();     // Text
        label2.setTextFill(Color.RED);

        //Obstacles
        ArrayList<Shape> obstacles = new ArrayList<>();
        for(int i=0; i<map1.layers.get(1).objects.size(); i++){
            obstacles.add(new Rectangle(map1.layers.get(1).objects.get(i).x, map1.layers.get(1).objects.get(i).y, map1.layers.get(1).objects.get(i).width, map1.layers.get(1).objects.get(i).height));
            obstacles.get(i).setFill(Color.RED);
            root.getChildren().add(obstacles.get(i));
        }

        //Personnages
        Personnage perso1 = new Personnage(20, 20, "C:/Users/marc_/IdeaProjects/Le_jeu_test/src/Images/teemo1.png");
        
        //Nodes -> root
        root.getChildren().addAll(perso1.imageV, label1);
        root2.getChildren().addAll(label2);

        //Root -> scene
        Scene scene1 = new Scene(root);

        //Scene -> stage et affichage stage
        window.setScene(scene1);
        window.show();

        //Pression d'une touche sur la scene1
        scene1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : up=true; break;
                    case DOWN : down=true; break;
                    case LEFT : left=true; break;
                    case RIGHT : right=true; break;
                    /*case SPACE :
                        if(scene1.getRoot().equals(root)){
                            rectangle1.setFill(Color.RED);
                            root.getChildren().remove(rectangle1);
                            root2.getChildren().add(rectangle1);
                            scene1.setRoot(root2);
                        } break;
                    case A :
                        if(scene1.getRoot().equals(root2)){
                            rectangle1.setFill(Color.BLUE);
                            root2.getChildren().remove(rectangle1);
                            root.getChildren().add(rectangle1);
                            scene1.setRoot(root);
                        } break;*/
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
/*
                //Déplacement joueur
                rectangleX = rectangle1.getLayoutX();
                rectangleY = rectangle1.getLayoutY();
                if(up) rectangleY -= 3;
                if(down) rectangleY += 3;
                if(left) rectangleX -= 3;
                if(right) rectangleX += 3;
                rectangle1.relocate(rectangleX, rectangleY);*/

                if(up) perso1.moveUp();
                if(down) perso1.moveDown();
                if(left) perso1.moveLeft();
                if(right) perso1.moveRight();

                //Réduction du rafraichissement du compteur fps
                if((now - lastFPSTime)>(1e8)){
                    label1.setText("FPS : "+fps);
                    label2.setText("FPS : "+fps);
                    lastFPSTime = now;
                }
/*
                //Restriction déplacement joueur (ne peut pas sortir de l'ecran)
                if(rectangleX>=scene1.getWidth()-rectangle1.getWidth()){
                    rectangle1.relocate(scene1.getWidth()-rectangle1.getWidth(),rectangleY);
                }
                if(rectangleX<=0){
                    rectangle1.relocate(0,rectangleY);
                }
                if(rectangleY>=scene1.getHeight()-rectangle1.getHeight()){
                    rectangle1.relocate(rectangleX,scene1.getHeight()-rectangle1.getHeight());
                }
                if(rectangleY<=0){
                    rectangle1.relocate(rectangleX,0);
                }*/
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
