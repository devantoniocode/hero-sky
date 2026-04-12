import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Som {
    // Armazena os sons pré-carregados para ganhar performance
    private Map<String, Clip> sons = new HashMap<>();

    public Som() {
        // Carregue todos os seus sons aqui ao iniciar o jogo
        carregarSom("disparo", "/sons/disparo.wav");
        // carregarSom("explosao", "/sons/explosao.wav");
        // carregarSom("gameover", "/sons/gameover.wav");
    }

    private void carregarSom(String nome, String caminho) {
        try {
            URL url = getClass().getResource(caminho);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            sons.put(nome, clip);
        } catch (Exception e) {
            System.err.println("Erro ao carregar som: " + caminho);
        }
    }

    public void tocar(String nome) {
        Clip clip = sons.get(nome);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Interrompe se já estiver tocando (ex: tiros rápidos)
            }
            clip.setFramePosition(0); // Volta para o início
            clip.start();
        }
    }
}
