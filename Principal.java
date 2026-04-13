import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class Principal {

    public static final int LARGURA_TELA = 1280;
    public static final int ALTURA_TELA = 720;

    public static void main(String[] args) {
        JFrame janela = new JFrame("Hero Sky");

         try {
            ImageIcon icone = new ImageIcon(Principal.class.getResource("/imgs/hero_sky.png"));
            janela.setIconImage(icone.getImage());
        } catch (Exception e) {
            System.out.println("Não foi possível carregar o ícone: " + e.getMessage());
        }

        Game game = new Game();
        game.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        janela.add(game);

        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocation(300, 100);
        janela.pack();
        janela.setVisible(true);
    }
}