import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Projetil {
    public int posX;
    public int posY;
    public int velX;

    public double heigth;
    public double raio_colisao;
    public BufferedImage img;
    public BufferedImage sprite;

    public Projetil() {

        posX = 0;
        posY = 0;
        velX = 0;
        heigth = 0;
        raio_colisao = 0;
        try {
            sprite = ImageIO.read(getClass().getResource("imgs/sprite_plane.png"));

        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem da bola!");
        }
    }

    public void disparoProjetil() {

        try {
            img = Recursos.getInstanse().cortarImagem(650, 160, 760, 192, sprite);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(posX, posY, 70, 32);
    }

}
