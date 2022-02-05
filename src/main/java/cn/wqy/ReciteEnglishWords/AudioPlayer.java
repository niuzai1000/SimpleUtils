package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    public static void play(File audioFile){
        InformationDialog.INFO_DIALOG.showInfo("加载并准备播放音频中..." , () -> {
            try {
                /*
                    播放音频
                 */
                if (audioFile != null){
                    AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
                    SourceDataLine sd = AudioSystem.getSourceDataLine(ais.getFormat());
                    sd.open();
                    sd.start();

                    InformationDialog.INFO_DIALOG.setInfo("播放中...");
                    int readCount = 0;
                    byte[] data = new byte[1024 * 512 * 16];
                    while(readCount != -1){
                        readCount = ais.read(data);
                        if (readCount >= 0) sd.write(data , 0 , readCount);
                    }

                    sd.drain();
                    sd.close();
                }else {
                    InformationDialog.INFO_DIALOG.setInfo("加载失败...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    InformationDialog.INFO_DIALOG.hideInfo();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
        });
    }
}
