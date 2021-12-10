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
    //Position du curseur (en haut à gauche = 0, en haut à droite = 1, ...)
    protected int positionCursorAction = 0;
    protected int positionCursorAttaque = 0;

    //Cas
    protected boolean tourMob = false;
    protected boolean dialogue = false;
    protected boolean dialogueStart = false;
    protected boolean action = false;
    protected boolean attaque = false;

    //Fenêtre de combat
    protected BorderPane combatPane = new BorderPane();

    //Portion en bas du borderPane
    protected GridPane actionSelection = new GridPane();
    protected GridPane attaqueSelection = new GridPane();
    protected GridPane dialoguePane = new GridPane();

    protected Label dialogueText = new Label();

    //Cases de sélection (exemple: Objets)
    protected ArrayList<HBox> hBoxAction = new ArrayList<>();
    protected ArrayList<HBox> hBoxAttaque = new ArrayList<>();

    //Texte des cases
    protected ArrayList<Label> labelAction = new ArrayList<>();
    protected ArrayList<Label> labelAttaque = new ArrayList<>();

    //Curseurs (des triangles)
    protected Polygon selectCursorAction = new Polygon();
    protected Polygon selectCursorAttaque = new Polygon();

    //CSS des différents composants
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

    //Vie des protagonistes
    protected Label healthperso = new Label();
    protected Label healthMob = new Label();


    public CombatLoop(Double layoutX, Double layoutY,Double width, Double height, GameLoop gameLoop){

        //BORDERPANE SETUP (FENETRE DE COMBAT)
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

        dialoguePane.setStyle(gridPaneCSS);
        dialoguePane.setAlignment(Pos.CENTER);
        dialoguePane.getChildren().add(dialogueText);

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


        //LABELS SETUP
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

        //HBOX > GRIDPANE
        actionSelection.add(hBoxAction.get(0),0,0 );
        actionSelection.add(hBoxAction.get(1),1,0 );
        actionSelection.add(hBoxAction.get(2),0,1 );
        actionSelection.add(hBoxAction.get(3),1,1 );

        attaqueSelection.add(hBoxAttaque.get(0), 0,0);
        attaqueSelection.add(hBoxAttaque.get(1), 1,0);

        //POINTS DE VIE
        this.healthperso.setFont(Font.font("",FontWeight.BOLD, 22));
        this.healthMob.setFont(Font.font("",FontWeight.BOLD, 22));
        this.healthperso.setTextFill(Color.BLUE);
        this.healthMob.setTextFill(Color.RED);

        //GRIDPANE + POINTS DE VIE > BORDERPANE
        this.combatPane.setBottom(actionSelection);
        this.combatPane.setLeft(healthperso);
        this.combatPane.setRight(healthMob);

    }

    public void quitCombat(){
        this.loopManager.game();
    }

    //Entrée en combat
    public void displayInit(){
        this.healthperso.setText("PV : " + gameLoop.perso.actual_health);
        this.healthMob.setText("PV : " + gameLoop.perso.mobVS.actual_health);
        moveCursorAttaque(0);
        moveCursorAction(0);
        //this.actionSelected = -1;
        this.dialogueStart = true;
        this.action = false;
        this.attaque = false;
        this.dialogue = false;
        this.dialogueText.setText(gameLoop.perso.mobVS.nom+" sauvage apparaît !");
        combatPane.setBottom(dialoguePane);
        if(!(gameLoop.root.getChildren().contains(combatPane))){
            this.gameLoop.root.getChildren().add(combatPane);
        }
    }

    //Update des points de vie
    public void displayUpdate(){
        this.healthperso.setText("PV : " + gameLoop.perso.actual_health);
        this.healthMob.setText("PV : " + gameLoop.perso.mobVS.actual_health);
        this.gameLoop.root.getChildren().remove(combatPane);
        this.gameLoop.root.getChildren().add(combatPane);
    }

    //Retire la fenêtre de combat
    public void displayRemove(){
        this.gameLoop.root.getChildren().remove(combatPane);
    }

    //Déplacement des curseurs
    public void moveCursorUp(){
        if(dialogue){

        }
        else if(action){
            if(positionCursorAction == 2) {
                moveCursorAction(0);
            }
            else if(positionCursorAction == 3) {
                moveCursorAction(1);
            }
        }
        else if(attaque){
        }
    }
    public void moveCursorDown(){
        if(dialogue){

        }
        else if(action){
            if(positionCursorAction == 0) {
                moveCursorAction(2);
            }
            else if(positionCursorAction == 1) {
                moveCursorAction(3);
            }
        }
        else if(attaque){
        }
    }
    public void moveCursorLeft(){
        if(dialogue){

        }
        else if(action){
            if(positionCursorAction == 1) {
                moveCursorAction(0);
            }
            else if(positionCursorAction == 3) {
                moveCursorAction(2);
            }
        }
        else if(attaque){
            if(positionCursorAttaque == 1){
                moveCursorAttaque(0);
            }
        }
    }

    public void moveCursorRight(){
        if(dialogue){

        }
        else if(action){
            if(positionCursorAction == 0) {
                moveCursorAction(1);
            }
            else if(positionCursorAction == 2) {
                moveCursorAction(3);
            }
        }
        else if(attaque){
            if(positionCursorAttaque == 0) {
                moveCursorAttaque(1);
            }
        }
    }

    //affichage du curseur sur la nouvelle position (0 = en haut à gauche, 1 = en haut à droite, ...)
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

    //Gestion de la touche "ESPACE" selon le cas
    public void select(){
        if(dialogueStart){
            if(tourMob){
                this.dialogueStart=false;
            }
            else{
                this.combatPane.setBottom(actionSelection);
                this.action = true;
                this.dialogueStart = false;
            }
        }
        else if(dialogue){
            if(tourMob){
                this.dialogue = false;
            }
            else{
                this.combatPane.setBottom(actionSelection);
                this.dialogue = false;
                this.action = true;
            }
        }
        else if(action){
            if(positionCursorAction == 0){
                this.action = false;
                this.attaque = true;
                combatPane.setBottom(attaqueSelection);
            }
            else if(positionCursorAction == 3){
                System.out.println("Vous avez pris la fuite.");
                gameLoop.level.getMap(gameLoop.level.getCurrentMap()).getMobs().remove(gameLoop.perso.mobVS);
                loopManager.game();
            }
        }
        else if(attaque){
            if(positionCursorAttaque == 0){
                gameLoop.perso.attaque(gameLoop.perso.mobVS);
                affiche("Vous infligez "+gameLoop.perso.actual_atk+" dégats !");
                displayUpdate();
                this.dialogue = true;
                this.tourMob = true;
                this.attaque = false;
            }
            else if(positionCursorAttaque == 1){
            }
        }

    }

    //Gestion de la touche "ECHAP" selon le cas
    public void escape(){
        if(action){
        }
        else if(attaque){
            moveCursorAttaque(0);
            this.action = true;
            this.combatPane.setBottom(actionSelection);
        }
    }

    //Le joueur gagne le combat : gain xp + retire le mob + continue sur la fenêtre de jeu
    public void gagne(){
        System.out.println("Vous avez battu " + gameLoop.perso.mobVS.nom +" "+ gameLoop.perso.mobVS.id);
        gameLoop.perso.giveXp();
        gameLoop.level.getMap(gameLoop.level.getCurrentMap()).getMobs().remove(gameLoop.perso.mobVS);
        loopManager.game();
    }

    //Le joueur perd le combat : respawn sur la map du début (= 0) + points de vie / 2
    public void perd(){
        System.out.println("Vous avez été battu par " + gameLoop.perso.mobVS.nom + " " + gameLoop.perso.mobVS.id);
        this.gameLoop.level.currentMap = 0;
        this.gameLoop.perso.tp(gameLoop.level.getMap(0).getSpawnX(), gameLoop.level.getMap(0).getSpawnY());
        this.gameLoop.perso.actual_health = this.gameLoop.perso.actual_health_max/2;
        loopManager.game();
    }

    //Affiche le texte dans le cadre dialogue
    public void affiche(String text){
        this.dialogueText.setText(text);
        this.combatPane.setBottom(dialoguePane);
    }


    //Loop (animations + tour du mob)
    @Override
    public void handle(long now) {
        if(tourMob){
            if(!(dialogue || dialogueStart)){
                gameLoop.perso.mobVS.attaque(gameLoop.perso);
                displayUpdate();
                affiche(gameLoop.perso.mobVS.nom + " vous inflige " + gameLoop.perso.mobVS.actual_atk + " dégats.");
                dialogue = true;
                tourMob = false;
            }
        }
        if(gameLoop.perso.mobVS.actual_health <= 0){
            gagne();
        }
        if(gameLoop.perso.actual_health <= 0){
            perd();
        }

    }
}
