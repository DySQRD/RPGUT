package Jeu;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author Marc SANCHEZ
 *
 */
public class LoopManager {
	protected Connexion connexion;
    
    /**
     * Boucle gérant le jeu vidéo.
     */
    protected GameLoop gameLoop;
    
    /**
     * Boucle pour les combats.
     */
    protected CombatLoop combatLoop;
    
    /**
     * Boucle pour l'interface de pause.
     */
    protected PauseLoop pauseLoop;
    
    /**
     * Boucle actuelle à gérer.
     */
    protected AnimationTimer currentLoop;
    protected Group root;
    
    /**
     * Constructeur pour la boucle permettant de gérer la totalité des boucles.
     * @param root
     * Groupe de nodes.
     */
    public LoopManager(Group root){
        this.root = root;
        this.connexion = new Connexion(root);
        connexion.loopManager = this;
        this.currentLoop = connexion;
    }
    
    /**
     * Crée une nouvelle partie.
     * @throws IOException
     * Erreur In/Out.
     * @throws SQLException
     * Erreur BDD/SQL.
     */
    public void newGame() throws IOException, SQLException {
        this.gameLoop = new GameLoop(root);
        this.combatLoop = gameLoop.combatLoop;
        this.pauseLoop = gameLoop.pauseLoop;
        gameLoop.loopManager = this;
        combatLoop.loopManager = this;
        pauseLoop.loopManager = this;
    }

    /**.
     * En cas de déconnexion (via l'interface de pause), retire la connexion actuelle pour se connecter de nouveau (via un nouveau compte ou le même).
     */
    public void connexion(){
        if(currentLoop instanceof PauseLoop){
            pauseLoop.stop();
            pauseLoop.displayRemove();
            //Se déconnecter de la BDD
            connexion.displayUpdate();
            this.currentLoop = connexion;
        }
    }

    /**
     * Remplace la boucle actuelle par la loop de combat en cas de combat.
     */
    public void combat(){
        gameLoop.stop();
        combatLoop.displayInit();
        combatLoop.start();
        this.currentLoop = combatLoop;

    }
    
    /**
     * Gère les transitions d'une loop a vers la GameLoop.
     */
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
    
    /**
     * Met l'interface de pause (PauseLoop) en cas de pause.
     */
    public void pause(){
        gameLoop.stop();
        pauseLoop.displayInit();
        pauseLoop.start();
        this.currentLoop = pauseLoop;
    }
    
    /**
     * Retourne la gameloop.
     * @return gameLoop
     * la boucle du jeu
     */
    public GameLoop getGameLoop() {
    	return gameLoop;
    }
}
