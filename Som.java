import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Som {
    private Map<String, Clip[]> canaisSons = new HashMap<>();
    private Map<String, Integer> proximoCanal = new HashMap<>();
    private float volumeHelice = 0.5f;
    private float volumeDisparo = 0.7f;

    public Som() {
        carregarSom("helice", "/sons/helice.wav", 1);
        carregarSom("disparo", "/sons/disparo.wav", 10);

        setVolume("helice", volumeHelice);
        setVolume("disparo", volumeDisparo);
    }

    private void carregarSom(String nome, String caminho, int qtdCanais) {
        try {
            Clip[] clips = new Clip[qtdCanais];
            for (int i = 0; i < qtdCanais; i++) {
                URL url = getClass().getResource(caminho);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                clips[i] = AudioSystem.getClip();
                clips[i].open(audioIn);
            }
            canaisSons.put(nome, clips);
            proximoCanal.put(nome, 0);
        } catch (Exception e) {
            System.err.println("Erro: " + caminho);
        }
    }

    public void tocar(String nome) {
        Clip[] clips = canaisSons.get(nome);
        if (clips == null)
            return;

        int index = proximoCanal.get(nome);
        Clip clip = clips[index];

        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();

        proximoCanal.put(nome, (index + 1) % clips.length);
    }

    public void tocarEmLoop(String nome) {
        Clip[] clips = canaisSons.get(nome);
        if (clips != null) {
            clips[0].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(String tipo, float volume) {
        if (volume < 0f)
            volume = 0f;
        if (volume > 1f)
            volume = 1f;

        if (tipo.equals("helice")) {
            this.volumeHelice = volume;
        } else if (tipo.equals("disparo")) {
            this.volumeDisparo = volume;
        }
     
        float dB = (float) (Math.log(volume == 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);

        Clip[] clips = canaisSons.get(tipo);
        if (clips != null) {
            for (Clip clip : clips) {
                if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(dB);
                }
            }
        }

    }

    public float getVolume(String tipo) {
        if (tipo.equals("helice")) {
            return volumeHelice;
        } else {
            return volumeDisparo;
        }
    }

    public void tocarTeste(String nome) {
        Clip[] clips = canaisSons.get(nome);
        if (clips != null && clips.length > 0) {
            Clip clip = clips[0];
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

}
