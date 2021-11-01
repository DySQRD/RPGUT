package Jeu;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class CombatLoop extends AnimationTimer {
    protected long delta = 0;
    protected long lastFrameTime=0;
    protected long fps;
    protected long lastFPSTime;
    protected int positionCursor = 0;
    protected Label fpsLabel = new Label();
    protected BorderPane combatPane = new BorderPane();
    protected GridPane actionSelection = new GridPane();
    protected ArrayList<HBox> hBoxAction = new ArrayList<>();
    protected ArrayList<Label> labelAction = new ArrayList<>();
    protected Polygon selectCursor = new Polygon();
    protected String hBoxCSS = ("-fx-padding: 10;" + "-fx-border-style: solid inside;"
            + "-fx-border-width: 2;" + "-fx-border-color: black;");
    protected String borderPaneCSS = ("-fx-background-color: white;");
    protected String gridPaneCSS = ("-fx-padding: 5;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: black;");
    protected LoopManager loopManager;
    protected GameLoop gameLoop;


    public CombatLoop(Double layoutX, Double layoutY,Double width, Double height, GameLoop gameLoop){
        this.gameLoop = gameLoop;
        this.combatPane.setPrefSize(width, height);
        this.combatPane.setLayoutX(layoutX);
        this.combatPane.setLayoutY(layoutY);
        this.combatPane.setStyle(borderPaneCSS);


        this.actionSelection = new GridPane();
        BorderPane.setMargin(actionSelection, new Insets(2));
        this.actionSelection.setStyle(gridPaneCSS);
        ColumnConstraints colonne1 = new ColumnConstraints();
        colonne1.setPercentWidth(50);
        ColumnConstraints colonne2 = new ColumnConstraints();
        colonne2.setPercentWidth(50);
        this.actionSelection.getColumnConstraints().addAll(colonne1, colonne2);

        for(int i=0; i<4; i++){
            HBox hBox = new HBox();
            GridPane.setMargin(hBox, new Insets(2));
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(5);
            hBox.setStyle(hBoxCSS);
            this.hBoxAction.add(hBox);
        }
        for(int i=0; i<4; i++){
            Label labelAction_ = new Label("Action "+ (i+1));
            labelAction_.setStyle("-fx-border-color: black;"+"-fx-padding: 10px;");
            this.labelAction.add(labelAction_);
        }

        this.selectCursor.getPoints().addAll(new Double[]{0.,0.,0.,10.,12.,5.});
        this.selectCursor.setFill(Color.BLACK);

        for(int i=0; i<hBoxAction.size(); i++){
            if(i==0) {hBoxAction.get(i).getChildren().addAll(selectCursor, labelAction.get(i));}
            else hBoxAction.get(i).getChildren().addAll(labelAction.get(i));
        }
        GridPane.setConstraints(this.hBoxAction.get(0),0,0);
        GridPane.setConstraints(this.hBoxAction.get(1),1,0);
        GridPane.setConstraints(this.hBoxAction.get(2),0,1);
        GridPane.setConstraints(this.hBoxAction.get(3),1,1);

        for(int i=0; i<hBoxAction.size(); i++){
            actionSelection.getChildren().add(hBoxAction.get(i));
        }

        combatPane.setBottom(actionSelection);

    }

    public void quitCombat(){
        this.loopManager.game();
    }

    public void displayUpdate(){
        this.gameLoop.root.getChildren().add(combatPane);
    }


    public void moveCursorUp(){
        if(positionCursor == 2) {
            hBoxAction.get(2).getChildren().remove(selectCursor);
            hBoxAction.get(0).getChildren().clear();
            hBoxAction.get(0).getChildren().addAll(selectCursor, labelAction.get(0));
            this.positionCursor = 0;
        }
        else if(positionCursor == 3) {
            hBoxAction.get(3).getChildren().remove(selectCursor);
            hBoxAction.get(1).getChildren().clear();
            hBoxAction.get(1).getChildren().addAll(selectCursor, labelAction.get(1));
            this.positionCursor = 1;
        }

    }
    public void moveCursorDown(){
        if(positionCursor == 0) {
            hBoxAction.get(0).getChildren().remove(selectCursor);
            hBoxAction.get(2).getChildren().clear();
            hBoxAction.get(2).getChildren().addAll(selectCursor, labelAction.get(2));
            this.positionCursor = 2;
        }
        else if(positionCursor == 1) {
            hBoxAction.get(1).getChildren().remove(selectCursor);
            hBoxAction.get(3).getChildren().clear();
            hBoxAction.get(3).getChildren().addAll(selectCursor, labelAction.get(3));
            this.positionCursor = 3;
        }
    }
    public void moveCursorLeft(){
        if(positionCursor == 1) {
            hBoxAction.get(1).getChildren().remove(selectCursor);
            hBoxAction.get(0).getChildren().clear();
            hBoxAction.get(0).getChildren().addAll(selectCursor, labelAction.get(0));
            this.positionCursor = 0;
        }
        else if(positionCursor == 3) {
            hBoxAction.get(3).getChildren().remove(selectCursor);
            hBoxAction.get(2).getChildren().clear();
            hBoxAction.get(2).getChildren().addAll(selectCursor, labelAction.get(2));
            this.positionCursor = 2;
        }
    }

    public void moveCursorRight(){
        if(positionCursor == 0) {
            hBoxAction.get(0).getChildren().remove(selectCursor);
            hBoxAction.get(1).getChildren().clear();
            hBoxAction.get(1).getChildren().addAll(selectCursor, labelAction.get(1));
            this.positionCursor = 1;
        }
        else if(positionCursor == 2) {
            hBoxAction.get(2).getChildren().remove(selectCursor);
            hBoxAction.get(3).getChildren().clear();
            hBoxAction.get(3).getChildren().addAll(selectCursor, labelAction.get(3));
            this.positionCursor = 3;
        }
    }


    @Override
    public void handle(long now) {
        delta = now - lastFrameTime;
        lastFrameTime = now;
        fps = (long) 1e9 / delta;
        if((now - lastFPSTime)>(1e8)){
            fpsLabel.setText(""+fps);
            lastFPSTime = now;
            //System.out.println(now);
        }

    }
}
