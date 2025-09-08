package org.example.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma animação composta por uma sequência de frames
 */
public class Animation {
    
    private List<BufferedImage> frames;
    private int currentFrame;
    private int frameCount;
    private int frameDelay;
    private int frameDelayCounter;
    private boolean loop;
    private boolean finished;
    private String name;
    
    // Configurações de animação
    private float frameRate; // frames por segundo
    private boolean paused;
    
    public Animation(String name, float frameRate, boolean loop) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
        this.frameCount = 0;
        this.frameRate = frameRate;
        this.frameDelay = (int) (60.0f / frameRate); // Assumindo 60 FPS
        this.frameDelayCounter = 0;
        this.loop = loop;
        this.finished = false;
        this.paused = false;
    }
    
    /**
     * Adiciona um frame à animação
     */
    public void addFrame(BufferedImage frame) {
        frames.add(frame);
        frameCount = frames.size();
    }
    
    /**
     * Atualiza a animação
     */
    public void update() {
        if (paused || finished || frameCount <= 1) {
            return;
        }
        
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            frameDelayCounter = 0;
            currentFrame++;
            
            if (currentFrame >= frameCount) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frameCount - 1;
                    finished = true;
                }
            }
        }
    }
    
    /**
     * Obtém o frame atual da animação
     */
    public BufferedImage getCurrentFrame() {
        if (frames.isEmpty()) {
            return null;
        }
        return frames.get(currentFrame);
    }
    
    /**
     * Obtém um frame específico
     */
    public BufferedImage getFrame(int index) {
        if (index >= 0 && index < frames.size()) {
            return frames.get(index);
        }
        return null;
    }
    
    /**
     * Reinicia a animação
     */
    public void reset() {
        currentFrame = 0;
        frameDelayCounter = 0;
        finished = false;
    }
    
    /**
     * Pausa/despausa a animação
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    /**
     * Define se a animação deve fazer loop
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    /**
     * Define a taxa de frames da animação
     */
    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
        this.frameDelay = (int) (60.0f / frameRate);
    }
    
    // Getters
    public String getName() { return name; }
    public int getCurrentFrameIndex() { return currentFrame; }
    public int getFrameCount() { return frameCount; }
    public float getFrameRate() { return frameRate; }
    public boolean isFinished() { return finished; }
    public boolean isPaused() { return paused; }
    public boolean isLooping() { return loop; }
    
    /**
     * Obtém a duração total da animação em segundos
     */
    public float getDuration() {
        return frameCount / frameRate;
    }
    
    /**
     * Obtém o progresso da animação (0.0 a 1.0)
     */
    public float getProgress() {
        if (frameCount <= 1) return 1.0f;
        return (float) currentFrame / (frameCount - 1);
    }
}