import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JPanel {
    public HeroPlane heroi;
    public EnemyPlane[] inimigo;
    private Som gerenciadorSom = new Som();

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
    public boolean disparou = false;
    public boolean abateu_inimigo = false;
    public boolean alterou_imagem_explosao_inimgio = false;

    public boolean colidiu = false;
    public int disparoVeloX = 0;
    public int posYDisparo = 0;
    public int aux_inimigo_abatido = 0;
    public int count_abates = 0;

    ArrayList<EnemyPlane> inimigosAbatidos = new ArrayList<EnemyPlane>();
    ArrayList<EnemyPlane> inimigosColidiu = new ArrayList<EnemyPlane>();
    public ArrayList<Projetil> listaProjeteis = new ArrayList<>();

    public ArrayList<Explosao> listaExplosoes = new ArrayList<>();
    public BufferedImage spriteExplosao;

    public Game() {
        setFocusable(true);
        setLayout(null);

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
                }
            }
        });

        heroi = new HeroPlane();

        inimigo = new EnemyPlane[3];

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

        for (int i = 0; i < inimigo.length; i++) {
            inimigo[i] = new EnemyPlane();
            inimigo[i].posY = i * 250;

        }

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
    }

    public void disparar() {
        gerenciadorSom.tocar("disparo"); 
        heroi.disparando = true;
        heroi.frameDisparoAtual = 0;
        Projetil p = new Projetil();
        p.posX = heroi.posX + 80;
        p.posY = heroi.posY + 65;
        p.velX = 10;
        p.disparoProjetil();
        listaProjeteis.add(p);
    }

    public void update() {
        sky_A.posX += sky_A.velX;
        sky_B.posX += sky_B.velX;
        if (sky_A.posX >= 1280)
            sky_A.posX = sky_B.posX - 1458;
        if (sky_B.posX >= 1280)
            sky_B.posX = sky_A.posX - 1458;

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
                    gerenciadorSom.tocar("explosao");
                    listaExplosoes.add(new Explosao(e.posX, e.posY, spriteExplosao));

                    e.posX = 1300 + new Random().nextInt(400);
                    e.posY = new Random().nextInt(600);

                    listaProjeteis.remove(i);
                    i--;
                    count_abates++;
                    break;
                }
            }
        }

        for (EnemyPlane e : inimigo) {
            e.posX -= 3;

            if (e.posX < -100) {
                e.posX = 1300;
                e.posY = new Random().nextInt(600);
            }

            if (heroi.getBounds().intersects(e.getBounds())) {
                heroi.hp -= 20;
                listaExplosoes.add(new Explosao(e.posX, e.posY, spriteExplosao));

                e.posX = 1300 + new Random().nextInt(400);

                if (heroi.hp <= 0) {
                    System.out.println("GAME OVER!");
                }

                if (colidiu) {
                    listaExplosoes.add(new Explosao(e.posX, e.posY, spriteExplosao));
                }
            }
        }

        for (int k = 0; k < listaExplosoes.size(); k++) {
            Explosao expo = listaExplosoes.get(k);
            expo.update();
            if (expo.finalizada) {
                listaExplosoes.remove(k);
                k--;
            }
        }
    }

    public void render() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(sky_A.img, sky_A.posX, sky_A.posY, null);
        g.drawImage(sky_B.img, sky_B.posX, sky_B.posY, null);

        g.drawImage(brick_A.img, brick_A.posX, brick_B.posY, null);
        g.drawImage(brick_B.img, brick_B.posX, brick_B.posY, null);

        g.drawImage(mountain_A.img, mountain_A.posX, mountain_A.posY, null);
        g.drawImage(mountain_B.img, mountain_B.posX, mountain_B.posY, null);

        long tempoAgora = System.currentTimeMillis();

        BufferedImage imgHeroi = heroi.getImagemParaDesenhar(tempoAgora);
        if (heroi.hp > 0) {
            g.drawImage(imgHeroi, heroi.posX, heroi.posY, null);
        }

        for (Projetil p : listaProjeteis) {
            g.drawImage(p.img, p.posX, p.posY, null);
        }

        for (EnemyPlane e : inimigo) {
            g.drawImage(e.img, e.posX, e.posY, null);
        }

        for (int i = 0; i < listaExplosoes.size(); i++) {
            listaExplosoes.get(i).draw(g);
        }

        desenharInterface(g);
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
        g.drawString("PONTOS: " + count_abates, 250, 38);

        if (heroi.hp <= 0) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 1280, 720);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", 450, 350);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Pressione R para tentar novamente", 480, 400);
        }
    }

}