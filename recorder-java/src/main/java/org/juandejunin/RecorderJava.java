package org.juandejunin;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class RecorderJava {

    public void recordAudio() {
        try {
            // Configuración del formato de audio
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

//            // Obtención del micrófono
           DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            System.out.println(info);

            // Obtención del micrófono
            TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(format);
            targetDataLine.open(format);
            targetDataLine.start();
            
            
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("La línea no es compatible");
                return; // Salir del programa si la línea no es compatible
            }

            System.out.println("Iniciando grabación");

            targetDataLine.start();
            
                        // Creación del hilo para la grabación
            Thread grabacionThread = new Thread(() -> {
                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                try {
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new java.io.File("grabacion.wav"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            grabacionThread.start();

            Thread.sleep(15000);
            targetDataLine.stop();
            targetDataLine.close();

            System.out.println("Prueba de sonido finalizada");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
