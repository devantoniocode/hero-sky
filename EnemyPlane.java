import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class EnemyPlane {
    public BufferedImage img;
    public BufferedImage sprite;
    public BufferedImage img_kill;

    public int posX;
    public int posY;
    public int raio;
    public int velX;
    public int velY;
    public double alt_inimigo;
    public boolean abatido;

    public EnemyPlane() {
        raio = 0;
        posX = 800;
        posY = 0;
        velX = 0;
        velY = 0;
        alt_inimigo = 0;
        abatido = false;

        try {
            sprite = ImageIO.read(getClass().getResource("imgs/sprite_plane.png"));
            img = Recursos.getInstanse().cortarImagem(422, 100, 601, 212, sprite);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem da bola!");
        }
    }

    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(posX, posY, 100, 100);
    }

}
