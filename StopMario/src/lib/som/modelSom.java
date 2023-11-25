package lib.som;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class modelSom {

    private Clip clipComeco; // Variável para a música de começo
    private static final String Fim = "D:\\Projetos Completos\\Projeto stopMario\\StopMario\\src\\lib\\som\\fim.wav";
    private static final String Comeco = "D:\\Projetos Completos\\Projeto stopMario\\StopMario\\src\\lib\\som\\comeco.wav";

    public void somFim() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Fim).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }

    public void somComeco() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(Comeco).getAbsoluteFile());
            clipComeco = AudioSystem.getClip(); // Cria a instância do Clip para a música de começo
            clipComeco.open(audioInputStream);
            clipComeco.loop(Clip.LOOP_CONTINUOUSLY); // Define o loop como infinito
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }

    // Método para parar a música de começo
    public void pararComeco() {
        if (clipComeco != null && clipComeco.isRunning()) {
            clipComeco.stop();
            clipComeco.close();
        }
    }
}
