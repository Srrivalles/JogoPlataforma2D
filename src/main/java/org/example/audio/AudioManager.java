package org.example.audio;

import javax.sound.sampled.*;
import java.net.URL;

public class AudioManager {

    private static Clip menuClip;
    private static Clip gameClip;
    private static Clip halloweenClip;
    private static Clip jump;
    private static Clip dashClip;
    private static Clip fallClip;
    private static Clip enemyDownClip;
    private static Clip robotStepClip;
    private static Clip enemyStepClip;
    private static Clip hurtClip;

    private static long musicStartTime = 0;
    private static int currentBPM = 120;
    private static boolean beatDetectionEnabled = false;

    // === MÚSICAS ===
    public static void playMenuMusic() {
        stopAllMusic();
        menuClip = loopClip(menuClip, "/audio/menu.wav", -6.0f);
    }

    public static void playGameMusic() {
        stopAllMusic();
        gameClip = loopClip(gameClip, "/audio/game.wav", -6.0f);
        initBeat(120);
    }

    public static void playHalloweenMusic() {
        stopAllMusic();
        halloweenClip = loopClip(halloweenClip, "/audio/halloween.wav", -6.0f);
        initBeat(100); // exemplo de BPM menor
    }

    public static void playEffectSound() {
        playEffect("/audio/orb_collect.wav", -3.0f);
    }

    public static void stopAllMusic() {
        stopClip(menuClip);
        stopClip(gameClip);
        stopClip(halloweenClip);
    }

    // === SONS DE EFEITO ===
    public static void playJumpSound() {
        playEffect("/audio/jump.wav", -3.0f);
    }

    public static void playDashSound() {
        playEffect("/audio/dash.wav", -3.0f);
    }

    public static void playFallSound() {
        playEffect("/audio/fall.wav", -4.0f);
    }

    public static void playEnemyDownSound() {
        playEffect("/audio/enemy_down.wav", -2.0f);
    }

    public static void playRobotStep() {
        playEffect("/audio/robot_step.wav", -5.0f);
    }

    public static void playEnemyStep() {
        playEffect("/audio/enemy_step.wav", -5.0f);
    }

    public static void playHurtSound() {
        playEffect("/audio/hurt.wav", -4.0f);
    }

    // === UTILITÁRIOS DE ÁUDIO ===
    public static void playEffect(String resourcePath, float gainDb) {
        try {
            URL url = AudioManager.class.getResource(resourcePath);
            if (url == null) {
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(clip, gainDb);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Clip loopClip(Clip current, String path, float gainDb) {
        try {
            if (current != null && current.isActive()) return current;
            URL url = AudioManager.class.getResource(path);
            if (url == null) {
                System.err.println("Áudio não encontrado: " + path);
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
            e.printStackTrace();
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
        } catch (Exception ignored) {}
    }

    private static void setVolume(Clip clip, float gainDb) {
        try {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gainDb);
            }
        } catch (Exception ignored) {}
    }

    // === BEAT DETECTION ===
    private static void initBeat(int bpm) {
        musicStartTime = System.currentTimeMillis();
        beatDetectionEnabled = true;
        currentBPM = bpm;
    }

    public static boolean isOnBeat() {
        if (!beatDetectionEnabled || musicStartTime == 0) return false;
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - musicStartTime;
        double beatInterval = 60000.0 / currentBPM;
        double currentBeat = elapsed / beatInterval;
        double beatFraction = currentBeat - Math.floor(currentBeat);
        return beatFraction < 0.1 || beatFraction > 0.9;
    }

    public static float getBeatIntensity() {
        if (!beatDetectionEnabled || musicStartTime == 0) return 0.0f;
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - musicStartTime;
        double beatInterval = 60000.0 / currentBPM;
        double currentBeat = elapsed / beatInterval;
        double beatFraction = currentBeat - Math.floor(currentBeat);
        if (beatFraction < 0.1) return (float)(1.0 - (beatFraction / 0.1));
        else if (beatFraction > 0.9) return (float)((beatFraction - 0.9) / 0.1);
        else return 0.0f;
    }
}
