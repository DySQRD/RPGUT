package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class MathsMinion extends Mob{
    private final Image IMAGE_MATHS_MINIONS = new Image("C:/Users/marc_/IdeaProjects/Le_jeu_test/src/Images/Maths_minions.png");

    public MathsMinion(double x, double y){
        super.setName("Méchante inconnue");
        ImageView imageView = new ImageView();
        imageView.setImage(IMAGE_MATHS_MINIONS);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX(x);
        imageView.setY(y);
        super.setImageMobV(imageView);
        super.setPosX(x);
        super.setPosY(y);
        super.setHitbox(new Rectangle(x, y, 25, 25));
    }
}
