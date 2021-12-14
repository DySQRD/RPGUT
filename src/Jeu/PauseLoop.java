package Jeu;

import BD.BD;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 *  La boucle de pause pour l'interface de pause est généré ici. <br>
 *  Elle permettra de se déconnecter, d'accéder aux statistiques et à l'inventaire du joueur.
 * @author Marc SANCHEZ
 * 
 */
public class PauseLoop extends AnimationTimer {
	/**
	 * Boucle permettant de gérer toutes les boucles de jouabilité.
	 */
    protected LoopManager loopManager;
    
    /**
     * Boucle de combat.
     */
    protected GameLoop gameLoop;
    
    /**
     * Booléen pour accéder au menu si true.
     */
    protected boolean menu = true;
    
    /**
     * Représente la case sélectionnée (Statistiques, Sauvegarder, Inventaire, Déconnexion, Retour).
     */
    protected int menuSelected = 0;

    protected StackPane pane = new StackPane();
    protected VBox vBox = new VBox();
    protected String vBoxCSS = "-fx-font-size: 15pt; -fx-font-weight: bold; -fx-spacing: 5px; -fx-alignment: center;";
    protected String labelCSS = "-fx-padding: 5px; -fx-border-color: rgba(0,0,0,0); -fx-border-width: 3px; -fx-border-style: solid;";
    protected String selectedLabelCSS = "-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 3px; -fx-min-width: 350px; -fx-alignment: center; -fx-background-color: rgba(100,100,100, 0.3); -fx-border-radius: 30px;";
    protected Label statsLabel = new Label("Stats");
    protected Label inventaireLabel = new Label("Inventaire");
    protected Label saveLabel = new Label("Sauvegarder");
    protected Label menuLabel = new Label("Menu de connexion");
    protected Label backLabel = new Label("Retour");
    
    /**
     * Regroupe les cases/labels (Sauvegarder, Déconnexion, etc).
     */
    protected ArrayList<Label> labelList = new ArrayList<Label>();
    
    /**
     * Crée l'interface de pause (Permettra de sauvegarder, vérifier les stats etc)
     * @param width
     * Largeur de l'écran de pause.
     * @param height
     * Hauteur de l'écran de pause.
     * @param gameloop
     * Boucle de jouabilité à mettre en pause.
     */
    public PauseLoop(Double width, Double height, GameLoop gameloop){
        this.gameLoop = gameloop;
        pane.setPrefSize(width*0.75, height*0.75);
        pane.setLayoutX(width*0.125);
        pane.setLayoutY(height*0.125);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(74, 85, 189, 1);");
        labelList.add(statsLabel);
        labelList.add(inventaireLabel);
        labelList.add(saveLabel);
        labelList.add(menuLabel);
        labelList.add(backLabel);
        for (Label l : labelList) {
            l.setStyle(labelCSS);
            vBox.getChildren().add(l);
        }
        labelList.get(0).setStyle(selectedLabelCSS);
        vBox.setStyle(vBoxCSS);
        pane.getChildren().add(vBox);
    }
    
    /**
     * Ajoute un group de nodes (ce qui permettra d'ajouter des éléments graphiques).
     */
    public void displayInit() {
        gameLoop.root.getChildren().add(pane);
    }
    
    /**
     * Retire le groupe de nodes (ce qui pourra être appelé une "interface").
     */
    public void displayRemove(){
        gameLoop.root.getChildren().remove(pane);
    }
    
    /**
     * Sélectionne la case au dessus, lui attribue une couleur à l'élément choisi (pour la visibilité) et l'entoure.
     */
    public void moveCursorUp(){
        if(menu){
            if(menuSelected == 0){}
            else{
                labelList.get(menuSelected).setStyle(labelCSS);
                menuSelected--;
                labelList.get(menuSelected).setStyle(selectedLabelCSS);
            }
        }
    }
    /**
     * Sélectionne la case au dessous, lui attribue une couleur à l'élément choisi (pour la visibilité) et l'entoure.
     */
    public void moveCursorDown(){
        if(menu){
            if(menuSelected == labelList.size()-1){}
            else{
                labelList.get(menuSelected).setStyle(labelCSS);
                menuSelected++;
                labelList.get(menuSelected).setStyle(selectedLabelCSS);
            }
        }
    }
    
    /**
     * Crée les cases de l'interface de pause (Sauvegarde, Stats, etc)
     * @throws SQLException
     * Erreur SQL
     */
    public void select() throws SQLException {
        if(menu){
            switch(menuSelected){
                case 0 :break;
                case 1 :break;
                case 2 : BD.sauvegarder();break;
                case 3 : loopManager.connexion();break;
                case 4 : loopManager.game();break;
            }
        }
    }
    
    /**
     * Permet de fermer l'interface.
     */
    public void escape(){
        loopManager.game();
    }

    @Override
    public void handle(long l) {

    }

}
