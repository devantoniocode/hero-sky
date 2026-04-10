
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Brick {
    public BufferedImage img;
    
    public int posX;
    public int posY;
    public int velX;

    public Brick() {
        try {
            img = ImageIO.read(getClass().getResource("/imgs/bicks.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        posX = 0;
        posY = 380;
        velX = 3;
    }
}
