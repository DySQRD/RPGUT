package Jeu;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PauseLoop extends AnimationTimer {

    protected LoopManager loopManager;
    protected GameLoop gameLoop;
    protected boolean menu = true;
    protected int menuSelected = 0;

    protected StackPane pane = new StackPane();
    protected VBox vBox = new VBox();
    protected String vBoxCSS = "-fx-font-size: 15pt; -fx-font-weight: bold; -fx-spacing: 5px; -fx-alignment: center;";
    protected String labelCSS = "-fx-padding: 5px; -fx-border-color: rgba(0,0,0,0); -fx-border-width: 3px; -fx-border-style: solid;";
    protected String selectedLabelCSS = "-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 3px; -fx-min-width: 350px; -fx-alignment: center; -fx-background-color: rgba(100,100,100, 0.3); -fx-border-radius: 30px;";
    protected Label statsLabel = new Label("Stats");
    protected Label inventaireLabel = new Label("Iventaire");
    protected Label saveLabel = new Label("Sauvegarder");
    protected Label menuLabel = new Label("Menu de connexion");
    protected Label backLabel = new Label("Retour");
    protected ArrayList<Label> labelList = new ArrayList<Label>();

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

    public void displayInit() {
        gameLoop.root.getChildren().add(pane);
    }

    public void displayRemove(){
        gameLoop.root.getChildren().remove(pane);
    }

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

    public void select(){
        if(menu){
            switch(menuSelected){
                case 0 :break;
                case 1 :break;
                case 2 :break;
                case 3 : loopManager.connexion();break;
                case 4 : loopManager.game();break;
            }
        }
    }

    public void escape(){
        loopManager.game();
    }

    @Override
    public void handle(long l) {

    }

}
