import java.awt.image.BufferedImage;

public class Recursos {
    private static Recursos singleton = null;

    private Recursos() {

    }

    public static Recursos getInstanse() {
        if (singleton == null) {
            singleton = new Recursos();
        }
        return singleton;
    }

    public BufferedImage cortarImagem(int x1, int y1, int x2, int y2, BufferedImage img) {
        int largura = x2 - x1;
        int altura = y2 - y1;
        return img.getSubimage(x1, y1, largura, altura);
    }

}