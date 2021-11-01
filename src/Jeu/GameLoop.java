package Jeu;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    protected long delta = 0;
    protected long lastFrameTime=0;
    protected long fps;
    protected long lastFPSTime;

    protected boolean up, down, left, right;

    public GameLoop(boolean up, boolean down, boolean left, boolean right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    @Override
    public void handle(long now) {
        //compteur de fps
        this.delta = now - lastFrameTime;
        this.lastFrameTime = now;
        this.fps = (long) 1e9 / delta;



    }
}
