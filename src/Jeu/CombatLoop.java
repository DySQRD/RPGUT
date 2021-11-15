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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class CombatLoop extends AnimationTimer {
    protected int positionCursorAction = 0;
    protected int actionSelected = -1;
    protected int positionCursorAttaque = 0;
    protected int attaqueSelected = -1;
    protected boolean tourMob = false;
    protected boolean dialogue = true;
    protected BorderPane combatPane = new BorderPane();
    protected GridPane actionSelection = new GridPane();
    protected GridPane attaqueSelection = new GridPane();
    protected GridPane dialoguePane = new GridPane();
    protected Label dialogueText = new Label();
    protected ArrayList<HBox> hBoxAction = new ArrayList<>();
    protected ArrayList<HBox> hBoxAttaque = new ArrayList<>();
    protected ArrayList<Label> labelAction = new ArrayList<>();
    protected ArrayList<Label> labelAttaque = new ArrayList<>();
    protected Polygon selectCursorAction = new Polygon();
    protected Polygon selectCursorAttaque = new Polygon();
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

    protected Label healthperso = new Label();
    protected Label healthMob = new Label();


    public CombatLoop(Double layoutX, Double layoutY,Double width, Double height, GameLoop gameLoop){

        //BORDERPANE SETUP (ECRAN DE COMBAT)
        this.gameLoop = gameLoop;
        this.combatPane.setPrefSize(width, height);
        this.combatPane.setLayoutX(layoutX);
        this.combatPane.setLayoutY(layoutY);
        this.combatPane.setStyle(borderPaneCSS);

        //GRIDPANE SETUP (MENUS)
        BorderPane.setMargin(actionSelection, new Insets(2));
        this.actionSelection.setStyle(gridPaneCSS);
        ColumnConstraints colonne1 = new ColumnConstraints();
        colonne1.setPercentWidth(50);
        ColumnConstraints colonne2 = new ColumnConstraints();
        colonne2.setPercentWidth(50);
        this.actionSelection.getColumnConstraints().addAll(colonne1, colonne2);

        BorderPane.setMargin(attaqueSelection, new Insets(2));
        this.attaqueSelection.setStyle(gridPaneCSS);
        this.attaqueSelection.getColumnConstraints().addAll(colonne1, colonne2);

        this.dialoguePane.setStyle(gridPaneCSS);

        //HBOX SET UP (CASES DES MENUS)
        for(int i=0; i<4; i++){
            HBox hBox = new HBox();
            GridPane.setMargin(hBox, new Insets(2));
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(5);
            hBox.setStyle(hBoxCSS);
            this.hBoxAction.add(hBox);
        }
        for(int i=0; i<2; i++){
            HBox hBox = new HBox();
            GridPane.setMargin(hBox, new Insets(2));
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(5);
            hBox.setStyle(hBoxCSS);
            this.hBoxAttaque.add(hBox);
        }


        //LABELS SETUP (TEXTES DES MENUS)
        this.labelAction.add(new Label("Attaque"));
        this.labelAction.add(new Label("Objets"));
        this.labelAction.add(new Label("Compétence"));
        this.labelAction.add(new Label("Fuite"));
        for(Label label : labelAction){
            label.setStyle("-fx-border-color: black;"+"-fx-padding: 10px;");
        }

        this.labelAttaque.add(new Label("Attaque 1"));
        this.labelAttaque.add(new Label("Attaque 2"));
        for(Label label : labelAttaque){
            label.setStyle("-fx-border-color: black;"+"-fx-padding: 10px;");
        }

        this.dialogueText.setStyle("-fx-padding: 10px;"+"-fx-alignment: center");

        //CURSOR
        this.selectCursorAction.getPoints().addAll(new Double[]{0.,0.,0.,10.,12.,5.});
        this.selectCursorAction.setFill(Color.BLACK);

        this.selectCursorAttaque.getPoints().addAll(new Double[]{0.,0.,0.,10.,12.,5.});
        this.selectCursorAttaque.setFill(Color.BLACK);

        //LABELS + CURSOR > HBOX
        for(int i=0; i<hBoxAction.size(); i++){
            if(i==0) {hBoxAction.get(i).getChildren().addAll(selectCursorAction, labelAction.get(i));}
            else hBoxAction.get(i).getChildren().addAll(labelAction.get(i));
        }

        for (int i=0; i<hBoxAttaque.size(); i++){
            if(i==0) {hBoxAttaque.get(i).getChildren().addAll(selectCursorAttaque, labelAttaque.get(i));}
            else hBoxAttaque.get(i).getChildren().addAll(labelAttaque.get(i));
        }
        dialoguePane.setAlignment(Pos.CENTER);
        dialoguePane.getChildren().add(dialogueText);

        actionSelection.add(hBoxAction.get(0),0,0 );
        actionSelection.add(hBoxAction.get(1),1,0 );
        actionSelection.add(hBoxAction.get(2),0,1 );
        actionSelection.add(hBoxAction.get(3),1,1 );

        attaqueSelection.add(hBoxAttaque.get(0), 0,0);
        attaqueSelection.add(hBoxAttaque.get(1), 1,0);

        this.healthperso.setFont(Font.font("",FontWeight.BOLD, 22));
        this.healthMob.setFont(Font.font("",FontWeight.BOLD, 22));
        this.healthperso.setTextFill(Color.BLUE);
        this.healthMob.setTextFill(Color.RED);

        this.combatPane.setBottom(actionSelection);
        this.combatPane.setLeft(healthperso);
        this.combatPane.setRight(healthMob);

    }

    public void quitCombat(){
        this.loopManager.game();
    }

    public void displayInit(){
        this.healthperso.setText("PV : " + gameLoop.perso.actual_health);
        this.healthMob.setText("PV : " + gameLoop.perso.mobVS.actual_health);
        moveCursorAttaque(0);
        moveCursorAction(0);
        this.actionSelected = -1;
        combatPane.setBottom(actionSelection);
        if(!(gameLoop.root.getChildren().contains(combatPane))){
            this.gameLoop.root.getChildren().add(combatPane);
        }
    }

    public void displayUpdate(){
        this.healthperso.setText("PV : " + gameLoop.perso.actual_health);
        this.healthMob.setText("PV : " + gameLoop.perso.mobVS.actual_health);
        this.gameLoop.root.getChildren().remove(combatPane);
        this.gameLoop.root.getChildren().add(combatPane);
    }

    public void displayRemove(){
        this.gameLoop.root.getChildren().remove(combatPane);
    }


    public void moveCursorUp(){
        if(actionSelected < 0){
            if(positionCursorAction == 2) {
                moveCursorAction(0);
            }
            else if(positionCursorAction == 3) {
                moveCursorAction(1);
            }
        }
        else if(actionSelected == 0){
        }


    }
    public void moveCursorDown(){
        if(actionSelected < 0){
            if(positionCursorAction == 0) {
                moveCursorAction(2);
            }
            else if(positionCursorAction == 1) {
                moveCursorAction(3);
            }
        }
        else if(actionSelected == 0){
        }

    }
    public void moveCursorLeft(){
        if(actionSelected < 0 ){
            if(positionCursorAction == 1) {
                moveCursorAction(0);
            }
            else if(positionCursorAction == 3) {
                moveCursorAction(2);
            }
        }
        else if(actionSelected == 0){
            if(positionCursorAttaque ==1){
                moveCursorAttaque(0);
            }
        }
    }

    public void moveCursorRight(){
        if(actionSelected < 0){
            if(positionCursorAction == 0) {
                moveCursorAction(1);
            }
            else if(positionCursorAction == 2) {
                moveCursorAction(3);
            }
        }
        else if(actionSelected == 0){
            if(positionCursorAttaque == 0) {
                moveCursorAttaque(1);
            }
        }
    }

    public void moveCursorAction(int n){
        hBoxAction.get(positionCursorAction).getChildren().remove(selectCursorAction);
        hBoxAction.get(n).getChildren().clear();
        hBoxAction.get(n).getChildren().addAll(selectCursorAction, labelAction.get(n));
        this.positionCursorAction = n;
    }
    public void moveCursorAttaque(int n){
        hBoxAttaque.get(positionCursorAttaque).getChildren().remove(selectCursorAttaque);
        hBoxAttaque.get(n).getChildren().clear();
        hBoxAttaque.get(n).getChildren().addAll(selectCursorAttaque, labelAttaque.get(n));
        this.positionCursorAttaque = n;
    }

    public void select(){
        if(actionSelected < 0){
            if(positionCursorAction == 0){
                this.actionSelected = 0;
                combatPane.setBottom(attaqueSelection);
            }
            else if(positionCursorAction == 3){
                System.out.println("Vous avez pris la fuite.");
                gameLoop.level.getMap(gameLoop.level.getCurrentMap()).getMobs().remove(gameLoop.perso.mobVS);
                loopManager.game();
            }
        }
        else if(actionSelected == 0){
            if(positionCursorAttaque == 0){
                gameLoop.perso.attaque(gameLoop.perso.mobVS);
                displayUpdate();
                this.tourMob = true;
            }
            else if(positionCursorAttaque == 1){
            }
        }

    }

    public void escape(){
        if(actionSelected < 0){
        }
        else if(actionSelected == 0){
            moveCursorAttaque(0);
            this.actionSelected = -1;
            this.combatPane.setBottom(actionSelection);
        }
    }

    public void gagne(){
        System.out.println("Vous avez battu " + gameLoop.perso.mobVS.name +" "+ gameLoop.perso.mobVS.id);
        gameLoop.perso.giveXp();
        gameLoop.level.getMap(gameLoop.level.getCurrentMap()).getMobs().remove(gameLoop.perso.mobVS);
        loopManager.game();
    }

    public void perd(){
        System.out.println("Vous avez été battu par " + gameLoop.perso.mobVS.name + " " + gameLoop.perso.mobVS.id);
        this.gameLoop.level.currentMap = 0;
        this.gameLoop.perso.tp(gameLoop.level.getMap(0).getSpawnX(), gameLoop.level.getMap(0).getSpawnY());
        this.gameLoop.perso.actual_health = 50;
        loopManager.game();
    }

    public void affiche(String text){
        this.dialogueText.setText(text);
        this.combatPane.setBottom(dialoguePane);

    }


    @Override
    public void handle(long now) {
        if(tourMob){
            gameLoop.perso.mobVS.attaque(gameLoop.perso);
            displayUpdate();
            //affiche(gameLoop.perso.mobVS.name + " vous inflige " + gameLoop.perso.mobVS.actual_atk + " dégats.");
            dialogue = true;
            tourMob = false;
        }
        if(gameLoop.perso.mobVS.actual_health <= 0){
            gagne();
        }
        if(gameLoop.perso.actual_health <= 0){
            perd();
        }

    }
}
