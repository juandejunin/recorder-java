package org.juandejunin;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecorderJava {
    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        AudioFormat format = new AudioFormat(1600,8,2,true,true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)){
            System.out.println("Line is not supporeted");
        }

        TargetDataLine targetDataLine =  (TargetDataLine) AudioSystem.getLine(info);

        targetDataLine.open();

        System.out.println("Starting recorder");

        targetDataLine.start();

        Thread stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                AudioInputStream audioStream = new AudioInputStream(targetDataLine);

                File wavfile = new File("D://recording.wav");

              try {
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavfile);
              }catch (IOException e){
                  e.printStackTrace();
              }
            }
        });
        stopper.start();

        Thread.sleep(5000);
        targetDataLine.stop();
        targetDataLine.close();

        System.out.println("Ended sound test");


    }
}