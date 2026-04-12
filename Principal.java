import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class Principal{

     public static final int LARGURA_TELA = 1280;
     public static final int ALTURA_TELA = 720;
     
    public static void main(String[] args){
        JFrame janela = new JFrame("Hero Sky");
        
        Game game = new Game();
        game.setPreferredSize(new Dimension(LARGURA_TELA,ALTURA_TELA) );
        janela.add(game);
        
        janela.setResizable(false); 
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocation(300, 100);
        janela.setVisible(true);

        janela.pack();

    }
}