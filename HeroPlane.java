import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Rectangle;

public class HeroPlane {

    public BufferedImage[] frames;
    private long timeLastChange = 0;
    int actualFrame = 0;
    public int posX;
    public int posY;
    public int raio;
    public int velX;
    public int velY;
    public BufferedImage cima;
    public BufferedImage baixo;
    public BufferedImage esquerda;
    public BufferedImage direita;
    public BufferedImage img_colisao;
    public BufferedImage sprite;
    public BufferedImage img;
    public int hp = 100;

    public HeroPlane() {

        raio = 80;
        posX = 100;
        posY = 100;
        velX = 0;
        velY = 0;

        try {
            sprite = ImageIO.read(getClass().getResource("imgs/sprite_plane.png"));
            img = Recursos.getInstanse().cortarImagem(152, 0, 350, 100, sprite);
            cima = img;
            baixo = img;
            esquerda = img;
            img_colisao = Recursos.getInstanse().cortarImagem(3, 100, 110, 203, sprite);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem da bola!");
        }

    }

    public void heroiDisparo() {

        frames = new BufferedImage[4];
        try {
            frames[0] = Recursos.getInstanse().cortarImagem(503, 0, 700, 100, sprite);
            frames[1] = Recursos.getInstanse().cortarImagem(701, 0, 855, 100, sprite);
            frames[2] = Recursos.getInstanse().cortarImagem(855, 0, 1050, 100, sprite);
            frames[3] = Recursos.getInstanse().cortarImagem(1050, 0, 1203, 100, sprite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getActualImage(long actualTime) {
        if ((actualTime - timeLastChange) >= 150) {
            changeFrame();
            timeLastChange = actualTime;
        }
        return frames[actualFrame];
    }

    private void changeFrame() {
        actualFrame++;
        if (actualFrame > 3) {
            actualFrame = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, 100, 80);
    }

}
