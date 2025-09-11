package org.example.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class AudioManager {

    private static Clip menuClip;
    private static Clip gameClip;
    
    // Sistema de sincronização musical
    private static long musicStartTime = 0;
    private static int currentBPM = 120; // BPM padrão
    private static boolean beatDetectionEnabled = false;

    // Caminhos esperados em resources: /audio/menu.wav e /audio/game.wav
    public static void playMenuMusic() {
        stopGameMusic();
        menuClip = loopClip(menuClip, "/audio/menu.wav", -6.0f);
    }

    public static void playGameMusic() {
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


