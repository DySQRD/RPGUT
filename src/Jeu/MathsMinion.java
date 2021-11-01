package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class MathsMinion extends Mob{
    private final Image IMAGE_MATHS_MINIONS = new Image("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/Images/minions.png");

    public MathsMinion(){
        super();
        this.name = "Méchante inconnue";
        ImageView imageView = new ImageView();
        imageView.setImage(IMAGE_MATHS_MINIONS);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        super.imageV = imageView;
    }
}
