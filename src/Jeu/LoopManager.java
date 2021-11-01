package Jeu;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class LoopManager {
    protected GameLoop gameLoop;
    protected CombatLoop combatLoop;
    protected AnimationTimer currentLoop;
    public LoopManager(GameLoop gameLoop, CombatLoop combatLoop){
        this.gameLoop = gameLoop;
        this.combatLoop = combatLoop;
        gameLoop.loopManager = this;
        combatLoop.loopManager = this;
        this.currentLoop = gameLoop;
    }

    public void combat(){
        gameLoop.stop();
        combatLoop.displayUpdate();
        combatLoop.start();
        this.currentLoop = combatLoop;

    }
    public void game(){
        combatLoop.stop();
        gameLoop.displayUpdate();
        gameLoop.start();
        this.currentLoop = gameLoop;
    }
}
