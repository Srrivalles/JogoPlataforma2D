package org.example.audio;

<<<<<<< HEAD
import javax.sound.sampled.*;
=======
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
import java.net.URL;

public class AudioManager {

    private static Clip menuClip;
    private static Clip gameClip;
<<<<<<< HEAD
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
=======
    
    // Sistema de sincronização musical
    private static long musicStartTime = 0;
    private static int currentBPM = 120; // BPM padrão
    private static boolean beatDetectionEnabled = false;

    // Caminhos esperados em resources: /audio/menu.wav e /audio/game.wav
    public static void playMenuMusic() {
        stopGameMusic();
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        menuClip = loopClip(menuClip, "/audio/menu.wav", -6.0f);
    }

    public static void playGameMusic() {
<<<<<<< HEAD
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
=======
        stopMenuMusic();
        gameClip = loopClip(gameClip, "/audio/game.wav", -6.0f);
        
        // Inicializar sistema de batida musical
        musicStartTime = System.currentTimeMillis();
        beatDetectionEnabled = true;
        currentBPM = 120; // BPM da música do jogo
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
            e.printStackTrace();
=======
            System.out.println("Erro ao tocar áudio " + resourcePath + ": " + e.getMessage());
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
        } catch (Exception ignored) {}
=======
        } catch (Exception ignored) {
        }
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    private static void setVolume(Clip clip, float gainDb) {
        try {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gainDb);
            }
<<<<<<< HEAD
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
=======
        } catch (Exception ignored) {
        }
    }
    
    // === MÉTODOS DE SINCRONIZAÇÃO MUSICAL ===
    
    /**
     * Verifica se estamos atualmente no "beat" da música
     */
    public static boolean isOnBeat() {
        if (!beatDetectionEnabled || musicStartTime == 0) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - musicStartTime;
        
        // Calcular intervalos de batida baseado no BPM
        double beatInterval = 60000.0 / currentBPM; // Millisegundos por batida
        double currentBeat = elapsed / beatInterval;
        
        // Verificar se estamos próximos de uma batida (dentro de 10% do intervalo)
        double beatFraction = currentBeat - Math.floor(currentBeat);
        return beatFraction < 0.1 || beatFraction > 0.9;
    }
    
    /**
     * Obtém a intensidade da batida atual (0.0 a 1.0)
     */
    public static float getBeatIntensity() {
        if (!beatDetectionEnabled || musicStartTime == 0) {
            return 0.0f;
        }
        
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - musicStartTime;
        
        double beatInterval = 60000.0 / currentBPM;
        double currentBeat = elapsed / beatInterval;
        double beatFraction = currentBeat - Math.floor(currentBeat);
        
        // Criar uma curva de intensidade que pulsa no ritmo
        if (beatFraction < 0.1) {
            return (float)(1.0 - (beatFraction / 0.1)); // Decai de 1.0 para 0.0
        } else if (beatFraction > 0.9) {
            return (float)((beatFraction - 0.9) / 0.1); // Cresce de 0.0 para 1.0
        } else {
            return 0.0f;
        }
    }
    
    /**
     * Define o BPM da música atual
     */
    public static void setBPM(int bpm) {
        currentBPM = bpm;
    }
    
    /**
     * Obtém o BPM atual
     */
    public static int getCurrentBPM() {
        return currentBPM;
    }
    
    /**
     * Habilita/desabilita detecção de batida
     */
    public static void setBeatDetectionEnabled(boolean enabled) {
        beatDetectionEnabled = enabled;
    }
    
    /**
     * Verifica se detecção de batida está habilitada
     */
    public static boolean isBeatDetectionEnabled() {
        return beatDetectionEnabled;
    }
    
    /**
     * Obtém o tempo desde que a música começou (em ms)
     */
    public static long getMusicElapsedTime() {
        if (musicStartTime == 0) {
            return 0;
        }
        return System.currentTimeMillis() - musicStartTime;
    }
    
    /**
     * Reseta o timer musical (útil para sincronizar com loops)
     */
    public static void resetMusicTimer() {
        musicStartTime = System.currentTimeMillis();
    }
}


>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
