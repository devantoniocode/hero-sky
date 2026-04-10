import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Mountain {
    public BufferedImage img;

    public int posX;
    public int posY;
    public int velX;

    public Mountain() {
        try {
            img = ImageIO.read(getClass().getResource("/imgs/desert_mountain.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        posX = 0;
        posY = 300;
        velX = 2;
    }
}
