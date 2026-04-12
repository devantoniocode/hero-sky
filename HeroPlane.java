import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Rectangle;

public class HeroPlane {

    public BufferedImage[] framesVoo;
    public BufferedImage[] framesDisparos;
    private int frameVooAtual = 0;
    public int frameDisparoAtual = 0;
    private long tempoUltimaTrocaVoo = 0;
    private long tempoUltimaTrocaDisparo = 0;

    public int largura;
    public int altura;
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
    public boolean disparando = false;

    public HeroPlane() {

        raio = 80;
        posX = 100;
        posY = 100;
        velX = 0;
        velY = 0;
        largura = 135;
        altura = 50;

        try {
            sprite = ImageIO.read(getClass().getResource("imgs/sprite_plane.png"));
            img = Recursos.getInstanse().cortarImagem(170, 0, 330, 105, sprite);
            cima = img;
            baixo = img;
            esquerda = img;

            framesVoo = new BufferedImage[2];
            framesVoo[0] = Recursos.getInstanse().cortarImagem(170, 0, 330, 105, sprite);
            framesVoo[1] = Recursos.getInstanse().cortarImagem(352, 2, 500, 100, sprite);

            framesDisparos = new BufferedImage[4];
            framesDisparos[0] = Recursos.getInstanse().cortarImagem(525, 1, 680, 100, sprite);
            framesDisparos[1] = Recursos.getInstanse().cortarImagem(702, 1, 860, 100, sprite);
            framesDisparos[2] = Recursos.getInstanse().cortarImagem(870, 0, 1025, 100, sprite);
            framesDisparos[3] = Recursos.getInstanse().cortarImagem(1048, 0, 1205, 98, sprite);

            img_colisao = Recursos.getInstanse().cortarImagem(3, 100, 110, 203, sprite);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem da bola!");
        }

    }

    public BufferedImage getImagemParaDesenhar(long tempoAtual) {
        if (disparando) {
            return animacaoTiro(tempoAtual);
        }
        return getImage(tempoAtual);
    }

    public BufferedImage getImage(long tempoAtual) {
        if ((tempoAtual - tempoUltimaTrocaVoo) >= 16.6) {//=> ms ~60fps
            frameVooAtual++;
            if (frameVooAtual > 1) {
                frameVooAtual = 0;
            }
            tempoUltimaTrocaVoo = tempoAtual;
        }
        return framesVoo[frameVooAtual];
    }

    public BufferedImage animacaoTiro(long tempoAtual) {
        if ((tempoAtual - tempoUltimaTrocaDisparo) >= 50) {
            frameDisparoAtual++;
            if (frameDisparoAtual > 3) {
                frameDisparoAtual = 0;
                this.disparando = false;
            }
            tempoUltimaTrocaDisparo = tempoAtual;
        }
        return framesDisparos[frameDisparoAtual];
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, 100, 80);
    }

}
