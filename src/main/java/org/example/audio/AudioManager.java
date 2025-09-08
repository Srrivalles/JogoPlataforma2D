package org.example.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class AudioManager {

    private static Clip menuClip;
    private static Clip gameClip;

    // Caminhos esperados em resources: /audio/menu.wav e /audio/game.wav
    public static void playMenuMusic() {
        stopGameMusic();
        menuClip = loopClip(menuClip, "/audio/menu.wav", -6.0f);
    }

    public static void playGameMusic() {
        stopMenuMusic();
        gameClip = loopClip(gameClip, "/audio/game.wav", -6.0f);
    }

    public static void stopMenuMusic() {
        stopClip(menuClip);
        menuClip = null;
    }

    public static void stopGameMusic() {
        stopClip(gameClip);
        gameClip = null;
    }

    public static void stopAll() {
        stopMenuMusic();
        stopGameMusic();
    }

    private static Clip loopClip(Clip current, String resourcePath, float gainDb) {
        try {
            if (current != null && current.isActive()) {
                return current; 
            }
            URL url = AudioManager.class.getResource(resourcePath);
            if (url == null) {
                System.out.println("Audio não encontrado: " + resourcePath);
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, gainDb);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (Exception e) {
            System.out.println("Erro ao tocar áudio " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }

    private static void stopClip(Clip clip) {
        try {
            if (clip != null) {
                clip.stop();
                clip.flush();
                clip.close();
            }
        } catch (Exception ignored) {
        }
    }

    private static void setVolume(Clip clip, float gainDb) {
        try {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gainDb);
            }
        } catch (Exception ignored) {
        }
    }
}


