package Jeu;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoopManager {
    protected Connexion connexion;
    protected GameLoop gameLoop;
    protected CombatLoop combatLoop;
    protected PauseLoop pauseLoop;
    protected AnimationTimer currentLoop;
    protected Group root;
    public LoopManager(Group root){
        this.root = root;
        this.connexion = new Connexion(root);
        connexion.loopManager = this;
        this.currentLoop = connexion;
    }

    public void newGame() throws IOException, SQLException {
        this.gameLoop = new GameLoop(root);
        this.combatLoop = gameLoop.combatLoop;
        this.pauseLoop = gameLoop.pauseLoop;
        gameLoop.loopManager = this;
        combatLoop.loopManager = this;
        pauseLoop.loopManager = this;
    }

    public void connexion(){
        if(currentLoop instanceof PauseLoop){
            pauseLoop.stop();
            pauseLoop.displayRemove();
            //Se déconnecter de la BDD
            connexion.displayUpdate();
            this.currentLoop = connexion;
        }
    }

    public void combat(){
        gameLoop.stop();
        combatLoop.displayInit();
        combatLoop.start();
        this.currentLoop = combatLoop;

    }
    public void game(){
        if (currentLoop instanceof CombatLoop) {
            combatLoop.stop();
            combatLoop.displayRemove();
            gameLoop.displayUpdate();
            gameLoop.start();
            this.currentLoop = gameLoop;
        }
        else if(currentLoop instanceof PauseLoop){
            pauseLoop.stop();
            pauseLoop.displayRemove();
            gameLoop.displayUpdate();
            gameLoop.start();
            this.currentLoop = gameLoop;
        }
        else if(currentLoop instanceof Connexion){
            connexion.stop();
            connexion.displayRemove();
            //Se connecter à la BDD
            gameLoop.displayUpdate();
            gameLoop.start();
            this.currentLoop = gameLoop;
        }

    }
    public void pause(){
        gameLoop.stop();
        pauseLoop.displayInit();
        pauseLoop.start();
        this.currentLoop = pauseLoop;
    }
    
    public GameLoop getGameLoop() {
    	return gameLoop;
    }
}
