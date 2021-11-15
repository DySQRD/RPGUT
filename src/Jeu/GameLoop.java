package Jeu;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Label;

public class GameLoop extends AnimationTimer {
    protected long delta = 0;
    protected long lastFrameTime=0;
    protected long fps;
    protected long lastFPSTime;
    protected Personnage perso;
    protected Level level;
    protected Group root;
    protected Label fpsLabel;
    protected Label hpLabel;
    protected Label mouseLocationLabel;
    protected CombatLoop combatLoop;
    protected LoopManager loopManager;

    protected boolean up, down, left, right;
    protected boolean gamePause = false;

    protected double width, height;

    public GameLoop(Personnage perso, Level level, Label fpsLabel, Label mouseLocationLabel, Group root){
        this.perso = perso;
        this.level = level;
        this.root = root;
        this.fpsLabel = fpsLabel;
        this.width = level.getMap(level.getCurrentMap()).getCanvas().getWidth();
        this.height = level.getMap(level.getCurrentMap()).getCanvas().getHeight();
        this.mouseLocationLabel = mouseLocationLabel;
        this.combatLoop = new CombatLoop(width/8, height/8, width*3/4, height*3/4, this);
    }

    public void displayUpdate(){
        this.root.getChildren().clear();
        this.root.getChildren().addAll(level.getMap(level.getCurrentMap()).getCanvas(), fpsLabel, mouseLocationLabel, perso.imageV);
        for(int k=0; k<level.getMap(level.getCurrentMap()).getMobs().size(); k++){
            root.getChildren().add(level.getMap(level.getCurrentMap()).getMobs().get(k).imageV);
        }
    }

    public void enterCombat(){
        this.loopManager.combat();
    }

    @Override
    public void handle(long now) {
        //compteur de fps
        this.delta = now - lastFrameTime;
        this.lastFrameTime = now;
        this.fps = (long) 1e9 / delta;
        if(up){
            perso.hitbox.setY(perso.hitbox.getY()-perso.velocity);
            for(int i=0; i<level.getMap(level.getCurrentMap()).getObstacles().size(); i++){
                if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                    if(level.getMap(level.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                        perso.tp(perso.posX, 525);
                        level.switchMap(level.getMap(level.getCurrentMap()).getObstacles().get(i).name);
                        this.displayUpdate();
                    }
                    perso.collision = true; break;
                }
            }
            if(!(perso.collision)) perso.moveUp();
            perso.collision = false;
            perso.hitbox.setY(perso.hitbox.getY()+perso.velocity);
            checkCombatPerso();
        }
        if(down){
            perso.hitbox.setY(perso.hitbox.getY()+perso.velocity);
            for(int i=0; i<level.getMap(level.getCurrentMap()).getObstacles().size(); i++){
                if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                    //obstacles.get(i).setFill(Color.BLUE);
                    if(level.getMap(level.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                        perso.tp(perso.posX, 30);
                        level.switchMap(level.getMap(level.getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true; break;

                }
            }
            if(!(perso.collision)) perso.moveDown();
            perso.collision = false;
            perso.hitbox.setY(perso.hitbox.getY()-perso.velocity);
            checkCombatPerso();
        }

        if(left){
            perso.hitbox.setX(perso.hitbox.getX()-perso.velocity);
            for(int i=0; i<level.getMap(level.getCurrentMap()).getObstacles().size(); i++){
                if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                    if(level.getMap(level.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                        perso.tp(830, perso.posY);
                        level.switchMap(level.getMap(level.getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true; break;

                }
            }
            if(!(perso.collision)) perso.moveLeft();
            perso.collision = false;
            perso.hitbox.setX(perso.hitbox.getX()+perso.velocity);
            checkCombatPerso();
        }

        if(right){
            perso.hitbox.setX(perso.hitbox.getX()+perso.velocity);
            for(int i=0; i<level.getMap(level.getCurrentMap()).getObstacles().size(); i++){
                if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(i).hitbox.getBoundsInParent())){
                    //obstacles.get(i).setFill(Color.BLUE);
                    if(level.getMap(level.getCurrentMap()).getObstacles().get(i).type.equals("sortie")){
                        perso.tp(30, perso.posY);
                        level.switchMap(level.getMap(level.getCurrentMap()).getObstacles().get(i).name);
                        displayUpdate();
                    }
                    perso.collision = true; break;

                }
            }
            if(!(perso.collision)) perso.moveRight();
            perso.collision = false;
            perso.hitbox.setX(perso.hitbox.getX()-perso.velocity);
            checkCombatPerso();
        }

        if((now - lastFPSTime)>(1e8)){
            fpsLabel.setText(""+fps);
            lastFPSTime = now;
        }

        for(int i=0; i<level.getMap(level.getCurrentMap()).getMobs().size(); i++){
            int random = (int) (Math.random()*4);
            Mob mob = level.getMap(level.getCurrentMap()).getMobs().get(i);
            switch (random){
                case 0 : mob.hitbox.setY(mob.hitbox.getY()-mob.velocity);
                for(int k=0; k<level.getMap(level.getCurrentMap()).getObstacles().size(); k++){
                    if((mob.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(k).hitbox.getBoundsInParent()))){
                        mob.collision = true;
                    }
                }
                mob.hitbox.setY(mob.hitbox.getY()+mob.velocity);
                if(!(mob.collision)){
                    mob.moveUp();
                }
                mob.collision = false;break;
                case 1 : mob.hitbox.setY(mob.hitbox.getY()+mob.velocity);
                    for(int k=0; k<level.getMap(level.getCurrentMap()).getObstacles().size(); k++){
                        if((mob.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(k).hitbox.getBoundsInParent()))){
                            mob.collision = true;
                        }
                    }
                    mob.hitbox.setY(mob.hitbox.getY()-mob.velocity);
                    if(!(mob.collision)){
                        mob.moveDown();
                    }
                    mob.collision = false;break;
                case 2 : mob.hitbox.setX(mob.hitbox.getX()-mob.velocity);
                    for(int k=0; k<level.getMap(level.getCurrentMap()).getObstacles().size(); k++){
                        if((mob.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(k).hitbox.getBoundsInParent()))){
                            mob.collision = true;
                        }
                    }
                    mob.hitbox.setX(mob.hitbox.getX()+mob.velocity);
                    if(!(mob.collision)){
                        mob.moveLeft();
                    }
                    mob.collision = false;break;
                case 3 : mob.hitbox.setX(mob.hitbox.getX()+mob.velocity);
                    for(int k=0; k<level.getMap(level.getCurrentMap()).getObstacles().size(); k++){
                        if((mob.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getObstacles().get(k).hitbox.getBoundsInParent()))){
                            mob.collision = true;
                        }
                    }
                    mob.hitbox.setX(mob.hitbox.getX()-mob.velocity);
                    if(!(mob.collision)){
                        mob.moveRight();
                    }
                    mob.collision = false;break;
            }
        }
        checkCombatMob();
        //METTRE LES COLLISIONS GROUPEES A LA FIN
    }

    public void checkCombatPerso(){
        for(int i=0; i<level.getMap(level.getCurrentMap()).getMobs().size(); i++){
            if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getMobs().get(i).hitbox.getBoundsInParent())){
                perso.mobVS = level.getMap(level.getCurrentMap()).getMobs().get(i);
                loopManager.combatLoop.tourMob = false;
                enterCombat();
            }
        }
    }

    public void checkCombatMob(){
        for(int i=0; i<level.getMap(level.getCurrentMap()).getMobs().size(); i++){
            if(perso.hitbox.getBoundsInParent().intersects(level.getMap(level.getCurrentMap()).getMobs().get(i).hitbox.getBoundsInParent())){
                perso.mobVS = level.getMap(level.getCurrentMap()).getMobs().get(i);
                loopManager.combatLoop.tourMob = true;
                enterCombat();
            }
        }
    }
}
