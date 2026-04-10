import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Explosao {
    public int posX, posY;
    public boolean finalizada = false;
    private BufferedImage img;
    private int tempoDeVida = 20;

    public Explosao(int x, int y, BufferedImage imagemBoom) {
        this.posX = x;
        this.posY = y;
        this.img = imagemBoom;
    }

    public void update() {
        tempoDeVida--;
        if (tempoDeVida <= 0) {
            finalizada = true;
        }
    }

    public void draw(Graphics g) {
        if (!finalizada && img != null) {
            g.drawImage(img, posX, posY, null);
        }
    }
}
