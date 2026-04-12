import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuVolume extends JFrame {

    public MenuVolume(Som gerenciadorSom, Game gamePrincipal) {
        setTitle("Menu");
        setResizable(false);
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelSons = new JPanel(new GridLayout(4, 1, 5, 5));

        int volHeliceAtual = (int) (gerenciadorSom.getVolume("helice") * 100);
        int volDisparoAtual = (int) (gerenciadorSom.getVolume("disparo") * 100);

        painelSons.add(new JLabel("  Volume Hélice:"));
        JSlider sliderHelice = new JSlider(0, 100, volHeliceAtual);
        sliderHelice.addChangeListener(e -> {
            float vol = sliderHelice.getValue() / 100f;
            gerenciadorSom.setVolume("helice", vol);
        });
        painelSons.add(sliderHelice);

        painelSons.add(new JLabel("  Volume Disparo:"));
        JSlider sliderDisparo = new JSlider(0, 100, volDisparoAtual);
        sliderDisparo.addChangeListener(e -> {
            float vol = sliderDisparo.getValue() / 100f;
            gerenciadorSom.setVolume("disparo", vol);
        });

        sliderDisparo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                gerenciadorSom.tocarTeste("disparo");
            }
        });
        painelSons.add(sliderDisparo);

        JPanel painelBotao = new JPanel(null);
        painelBotao.setPreferredSize(new Dimension(300, 110)); 
        painelBotao.setOpaque(false);

        JButton btnPlay = new JButton("PLAY");
        btnPlay.setBounds(50, 5, 200, 40);

        btnPlay.setHorizontalAlignment(SwingConstants.CENTER);
        btnPlay.setVerticalAlignment(SwingConstants.CENTER);

        btnPlay.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlay.setBackground(new Color(70, 130, 180));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);

        btnPlay.setHorizontalTextPosition(SwingConstants.LEFT);
        btnPlay.setHorizontalAlignment(SwingConstants.CENTER);

        btnPlay.addActionListener(e -> {
            gamePrincipal.despausar();
            dispose();
        });

        JButton btnSair = new JButton("SAIR DO JOGO");
        btnSair.setBounds(50, 55, 200, 40);
        btnSair.setBackground(new Color(180, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.addActionListener(e -> System.exit(0));

        painelBotao.add(btnPlay);
        painelBotao.add(btnSair);

        painelPrincipal.add(painelSons, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);

        add(painelPrincipal);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gamePrincipal.despausar();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

}
