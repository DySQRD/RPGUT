package Jeu;

import Exceptions.ImprevuDBError;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
/**
 * 
 * @author Marc SANCHEZ
 *
 */
public class FirstApplication extends Application {
	public static LoopManager loopManager;

    //Actions avant l'application
    @Override
    public void init() throws Exception {
        System.out.println("Before");
    }

    /**
     * La méthode où la taille de la fênetre, le stage, les scenes, et la gestion d'évenement en temps réel sont pris en compte.
     * @throws Exception
     * Erreur.
     * @param stage
     * Objet stage où seront stockés les éléments visuels.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Création fenêtre
        Stage window = new Stage();
        window = stage;

        //Configuration fenêtre
        window.setTitle("Project Game");
        window.setWidth(900);
        window.setHeight(600);      // Taille des maps : 576 x 896
        window.setResizable(false);

        Group root = new Group();
        Scene scene1 = new Scene(root);

        //Scene -> stage et affichage stage
        window.setScene(scene1);
        window.show();

        loopManager = new LoopManager(root);
        loopManager.connexion.displayUpdate();



        loopManager.connexion.loginButton.setOnAction(action -> {
            try {
                loopManager.connexion.connect(loopManager.connexion.unField.getText(), loopManager.connexion.pField.getText());
            } catch (ImprevuDBError imprevuDBError) {
                imprevuDBError.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loopManager.connexion.registerButton.setOnAction(action -> {
            try {
                loopManager.connexion.register(loopManager.connexion.unField.getText(), loopManager.connexion.pField.getText());
            } catch (ImprevuDBError imprevuDBError) {
                imprevuDBError.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        scene1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(loopManager.currentLoop instanceof GameLoop){
                    loopManager.gameLoop.mouseLocationLabel.setText("x = " + mouseEvent.getSceneX() + ", y = "+mouseEvent.getSceneY());
                }
            }
        });

        //Lancement de la Loop jeu
        //gameLoop.start();



        //Pression d'une touche sur la scene1
        scene1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : if(loopManager.currentLoop instanceof GameLoop){loopManager.gameLoop.up=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorUp();break;
                    }
                    else if(loopManager.currentLoop instanceof PauseLoop){
                        loopManager.pauseLoop.moveCursorUp();break;
                    }
                    case DOWN : if(loopManager.currentLoop instanceof GameLoop){loopManager.gameLoop.down=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorDown();break;
                    }
                    else if(loopManager.currentLoop instanceof PauseLoop){
                        loopManager.pauseLoop.moveCursorDown();break;
                    }
                    case LEFT : if(loopManager.currentLoop instanceof GameLoop){loopManager.gameLoop.left=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorLeft();break;
                    }
                    else if(loopManager.currentLoop instanceof PauseLoop){
                        break;
                    }
                    case RIGHT : if(loopManager.currentLoop instanceof GameLoop){loopManager.gameLoop.right=true;break;}
                    else if(loopManager.currentLoop instanceof CombatLoop){
                        loopManager.combatLoop.moveCursorRight();break;
                    }
                    else if(loopManager.currentLoop instanceof PauseLoop){
                        break;
                    }
                    case ESCAPE: if (loopManager.currentLoop instanceof GameLoop){
                        loopManager.gameLoop.enterPause();break;
                    }
                    else if(loopManager.currentLoop instanceof PauseLoop){
                        loopManager.pauseLoop.escape();break;
                    }
                        else if(loopManager.currentLoop instanceof CombatLoop){
                            loopManager.combatLoop.escape();break;
                    }
                    case SPACE: if(loopManager.currentLoop instanceof GameLoop){}
                        else if(loopManager.currentLoop instanceof CombatLoop){
                            loopManager.combatLoop.select();break;
                        }
                        else if(loopManager.currentLoop instanceof PauseLoop){
                        try {
                            loopManager.pauseLoop.select();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        });

        //Relâchement d'une touche sur la scene1
        scene1.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP : loopManager.gameLoop.up=false; break;
                    case DOWN : loopManager.gameLoop.down=false; break;
                    case LEFT : loopManager.gameLoop.left=false; break;
                    case RIGHT : loopManager.gameLoop.right=false; break;
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

