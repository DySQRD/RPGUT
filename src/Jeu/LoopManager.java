package Jeu;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class LoopManager {
    protected GameLoop gameLoop;
    protected CombatLoop combatLoop;
    protected PauseLoop pauseLoop;
    protected AnimationTimer currentLoop;
    public LoopManager(GameLoop gameLoop, CombatLoop combatLoop, PauseLoop pauseLoop){
        this.gameLoop = gameLoop;
        this.combatLoop = combatLoop;
        this.pauseLoop = pauseLoop;
        gameLoop.loopManager = this;
        combatLoop.loopManager = this;
        pauseLoop.loopManager = this;
        this.currentLoop = gameLoop;
    }

    public void combat(){
        gameLoop.stop();
        combatLoop.displayInit();
        combatLoop.start();
        this.currentLoop = combatLoop;

    }
    public void game(){
        combatLoop.stop();
        combatLoop.displayRemove();
        gameLoop.displayUpdate();
        gameLoop.start();
        this.currentLoop = gameLoop;
    }
    public void pause(){
        gameLoop.stop();
        pauseLoop.displayInit();
        pauseLoop.start();
        this.currentLoop = pauseLoop;
    }
}
