import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.imageio.ImageIO;

public class Game extends JPanel {
    public HeroPlane heroi;
    public EnemyPlane[] inimigo;
    private Som gerenciadorSom = new Som();
    private MenuVolume menuVolume;

    public Sky sky_A;
    public Sky sky_B;
    public Brick brick_A;
    public Brick brick_B;
    public Mountain mountain_A;
    public Mountain mountain_B;

    public boolean k_cima = false;
    public boolean k_baixo = false;
    public boolean k_direita = false;
    public boolean k_esquerda = false;
    public boolean k_press = false;
    public boolean k_space = false;
    public boolean k_esc = false;
    public boolean k_r = false;
    public boolean pause = false;

    public int pontos = 0;
    private double anguloAtual = 0;

    public ArrayList<Projetil> listaProjeteis = new ArrayList<>();
    public ArrayList<Explosao> listaExplosoes = new ArrayList<>();
    public BufferedImage spriteExplosao;

    public Game() {
        setFocusable(true);
        setLayout(null);
        gerenciadorSom.tocarEmLoop("helice");

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        k_cima = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        k_baixo = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        k_esquerda = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        k_direita = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        k_space = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        k_esc = true;
                        break;
                    case KeyEvent.VK_R:
                        k_r = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        k_cima = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        k_baixo = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        k_esquerda = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        k_direita = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        k_space = false;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        k_esc = false;
                        break;
                    case KeyEvent.VK_R:
                        k_r = false;
                        break;
                }
            }
        });

        inimigo = new EnemyPlane[3];
        reiniciarJogo();

        sky_A = new Sky();
        sky_B = new Sky();
        mountain_A = new Mountain();
        mountain_B = new Mountain();
        brick_A = new Brick();
        brick_B = new Brick();
        sky_B.posX = sky_A.posX - 1458;
        mountain_B.posX = mountain_A.posX - 1696;
        brick_B.posX = brick_A.posX - 2050;

        setFocusable(true);
        setLayout(null);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                gameloop();
            }
        });
        t1.start();

        try {
            BufferedImage folhaSprites = ImageIO.read(getClass().getResource("/imgs/sprite_plane.png"));
            spriteExplosao = Recursos.getInstanse().cortarImagem(3, 100, 110, 203, folhaSprites);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gameloop() {
        while (true) {
            handlerEvents();
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handlerEvents() {
        if (heroi.hp <= 0) {
            if (k_r) {
                reiniciarJogo();
            }
            return;
        }

        if (k_esc) {
            pausarJogo();
            k_esc = false;
        }
        if (!pause) {
            if (k_cima)
                heroi.posY -= 8;
            if (k_baixo)
                heroi.posY += 7;
            if (k_esquerda)
                heroi.posX -= 8;
            if (k_direita)
                heroi.posX += 8;
            if (k_space) {
                disparar();
                k_space = false;
            }
            if (k_r) {
                reiniciarJogo();
                k_r = false;
            }
        }
    }

    public void update() {
        if (pause)
            return;

        sky_A.posX += sky_A.velX;
        sky_B.posX += sky_B.velX;
        if (sky_A.posX >= 1280)
            sky_A.posX = sky_B.posX - 1458;
        if (sky_B.posX >= 1280)
            sky_B.posX = sky_A.posX - 1458;

        Random rand = new Random();

        for (int i = 0; i < listaProjeteis.size(); i++) {
            Projetil p = listaProjeteis.get(i);
            p.posX += p.velX;

            if (p.posX > 1280) {
                listaProjeteis.remove(i);
                i--;
                continue;
            }

            for (int j = 0; j < inimigo.length; j++) {
                EnemyPlane e = inimigo[j];
                if (p.getBounds().intersects(e.getBounds())) {
                    // gerenciadorSom.tocar("explosao");
                    listaExplosoes.add(new Explosao(e.posX, e.posY, spriteExplosao));

                    e.posX = 1300 + rand.nextInt(400);
                    e.posY = rand.nextInt(600);

                    listaProjeteis.remove(i);
                    i--;
                    pontos++;
                    break;
                }
            }
        }

        // --- MOVIMENTAÇÃO DOS INIMIGOS E COLISÃO COM HERÓI ---// Colisão Herói x
        // Inimigo
        for (EnemyPlane e : inimigo) {
            e.posX -= 3;

            // Se sair da tela pela esquerda
            if (e.posX < -100) {
                e.posX = 1300;
                e.posY = rand.nextInt(600);
            }
            // Colisão Herói x Inimigo
            if (heroi.getBounds().intersects(e.getBounds())) {
                heroi.hp -= 20;
                listaExplosoes.add(new Explosao(e.posX, e.posY, spriteExplosao));

                e.posX = 1300 + rand.nextInt(400);

                if (heroi.hp <= 0) {
                    System.out.println("GAME OVER!");
                }
            }
        }
        // --- ATUALIZAÇÃO DE EXPLOSÕES ---
        for (int k = 0; k < listaExplosoes.size(); k++) {
            Explosao expo = listaExplosoes.get(k);
            expo.update();
            if (expo.finalizada) {
                listaExplosoes.remove(k);
                k--;
            }
        }
    }

    private void pausarJogo() {
        this.pause = true;
        abrirMenuVolume();
    }

    public void despausar() {
        this.pause = false;
    }

    private void abrirMenuVolume() {
        if (menuVolume == null || !menuVolume.isVisible()) {
            menuVolume = new MenuVolume(gerenciadorSom, this);
            menuVolume.setVisible(true);
        } else {
            menuVolume.toFront();
        }
    }

    private void reiniciarJogo() {
        heroi = new HeroPlane();
        Random rand = new Random();

        for (int i = 0; i < inimigo.length; i++) {
            inimigo[i] = new EnemyPlane();
            // Faz eles começarem FORA da tela à direita (1280 é o fim da tela)
            // O "i * 400" cria uma fila indiana, e o "rand.nextInt" quebra a regularidade
            inimigo[i].posX = 1300 + (i * 400) + rand.nextInt(300);
            // Posição vertical totalmente aleatória
            inimigo[i].posY = rand.nextInt(600);
        }

        listaProjeteis.clear();
        listaExplosoes.clear();
        pontos = 0;
        pause = false;
        k_r = false;
    }

    public void disparar() {
        gerenciadorSom.tocar("disparo");
        heroi.disparando = true;
        heroi.frameDisparoAtual = 0;
        Projetil p = new Projetil();
        p.posX = heroi.posX + 80;
        p.posY = heroi.posY + 65;
        p.velX = 17;
        p.disparoProjetil();
        listaProjeteis.add(p);
    }

    public void render() {
        repaint();
    }

    public double getAngulo() {
        if (k_cima && k_direita)
            return Math.toRadians(45); // Diagonal Nordeste
        if (k_baixo && k_direita)
            return Math.toRadians(-45); // Diagonal Sudeste (Ajuste o sinal conforme o eixo Y)
        if (k_cima && k_esquerda)
            return Math.toRadians(135);
        if (k_baixo && k_esquerda)
            return Math.toRadians(-135);

        // Movimentos retos
        if (k_cima)
            return Math.toRadians(0);
        if (k_baixo)
            return Math.toRadians(0); // Ou ajuste para inclinar levemente

        return 0; // Padrão reto
    }

    private double calcularAngulo() {
        if (k_cima)
            return Math.toRadians(-15);
        if (k_baixo)
            return Math.toRadians(15);
        return 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // --- 1. CONFIGURAÇÕES DE QUALIDADE ---
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.drawImage(sky_A.img, sky_A.posX, sky_A.posY, null);
        g2d.drawImage(sky_B.img, sky_B.posX, sky_B.posY, null);
        g2d.drawImage(brick_A.img, brick_A.posX, brick_B.posY, null);
        g2d.drawImage(brick_B.img, brick_B.posX, brick_B.posY, null);
        g2d.drawImage(mountain_A.img, mountain_A.posX, mountain_A.posY, null);
        g2d.drawImage(mountain_B.img, mountain_B.posX, mountain_B.posY, null);

        // --- 3. DESENHAR O HERÓI COM ROTAÇÃO ---
        if (heroi.hp > 0) {
            AffineTransform reset = g2d.getTransform();

            int centroX = heroi.posX + (heroi.largura / 2);
            int centroY = heroi.posY + (heroi.altura / 2);

            // Atualiza a inclinação suave
            double anguloAlvo = calcularAngulo();
            anguloAtual += (anguloAlvo - anguloAtual) * 0.1;

            g2d.translate(centroX, centroY);
            g2d.rotate(anguloAtual);

            // Pega a imagem da animação (se houver)
            long tempoAgora = System.currentTimeMillis();
            BufferedImage imgHeroi = heroi.getImagemParaDesenhar(tempoAgora);

            //   g.drawImage(imgHeroi, heroi.posX, heroi.posY, null);

            // DESENHA CENTRALIZADO NO EIXO ROTACIONADO
            g2d.drawImage(imgHeroi, - heroi.largura / 2, - heroi.altura / 2, null);

            g2d.setTransform(reset); // Restaura para os próximos desenhos serem retos
        }

        // --- 4. DESENHAR DEMAIS ELEMENTOS (RETOS) ---
        for (Projetil p : listaProjeteis) {
            g2d.drawImage(p.img, p.posX, p.posY, null);
        }

        for (EnemyPlane e : inimigo) {
            g2d.drawImage(e.img, e.posX, e.posY, null);
        }

        for (int i = 0; i < listaExplosoes.size(); i++) {
            listaExplosoes.get(i).draw(g2d);
        }

        desenharInterface(g2d);
    }

    private void desenharInterface(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(20, 20, 200, 20);

        g.setColor(Color.GREEN);
        int larguraVida = (int) (heroi.hp * 2);
        g.fillRect(20, 20, larguraVida, 20);

        g.setColor(Color.WHITE);
        g.drawRect(20, 20, 200, 20);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("PONTOS: " + pontos, 250, 38);

        if (heroi.hp <= 0) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 1280, 720);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", 450, 350);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Pressione R para tentar novamente", 500, 420);

            g.setColor(Color.RED);
            g.drawString(pontos + " Aviões abatios", 570, 390);
        }
    }

}