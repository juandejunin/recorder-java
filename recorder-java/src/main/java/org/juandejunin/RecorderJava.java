package org.juandejunin;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecorderJava {
    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        AudioFormat format = new AudioFormat(1600, 8, 2, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("La línea no es compatible");
            return; // Salir del programa si la línea no es compatible
        }

        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

        targetDataLine.open();

        System.out.println("Iniciando grabación");

        targetDataLine.start();

        // Función para verificar si se está accediendo al micrófono
        Thread microphoneStatusChecker = new Thread(() -> {
            while (true) {
                // Obtener el nivel de amplitud actual
                byte[] buffer = new byte[targetDataLine.getBufferSize() / 5];
                int bytesRead = targetDataLine.read(buffer, 0, buffer.length);
                int maxAmplitude = 0;
                for (int i = 0; i < bytesRead; i++) {
                    maxAmplitude = Math.max(maxAmplitude, Math.abs(buffer[i]));
                }

                // Puedes ajustar este umbral según tus necesidades
                if (maxAmplitude > 50) {
                    System.out.println("Sonido detectado en el micrófono");
                } else {
                    System.out.println("Sin sonido en el micrófono");
                }

                try {
                    Thread.sleep(1000); // Puedes ajustar el intervalo de verificación
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "MicrophoneStatusChecker");

        microphoneStatusChecker.setDaemon(true);
        microphoneStatusChecker.start();

        // Hilo para detener la grabación después de cierto tiempo
        Thread stopper = new Thread(() -> {
            AudioInputStream audioStream = new AudioInputStream(targetDataLine);

            File wavFile = new File("D:" + File.separator + "recording.wav");

            try {
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    audioStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "HiloDetenedor");

        stopper.start();

        Thread.sleep(5000);
        targetDataLine.stop();
        targetDataLine.close();

        System.out.println("Prueba de sonido finalizada");
    }
}
