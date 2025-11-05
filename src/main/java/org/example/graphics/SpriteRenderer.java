package org.example.graphics;

import org.example.ui.GameConfig;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe responsável por renderizar sprites e animações
 */
public class SpriteRenderer {
    
    private static SpriteRenderer instance;
    private AnimationManager animationManager;
    
    // Configurações de renderização
    private boolean useSmoothing;
    private boolean useInterpolation;
    
    private SpriteRenderer() {
        this.animationManager = AnimationManager.getInstance();
        this.useSmoothing = true;
        this.useInterpolation = true;
    }
    
    /**
     * Obtém a instância singleton do SpriteRenderer
     */
    public static SpriteRenderer getInstance() {
        if (instance == null) {
            instance = new SpriteRenderer();
        }
        return instance;
    }
    
    /**
     * Renderiza um sprite estático
     */
    public void renderSprite(Graphics2D g2d, String spriteSheetName, int spriteIndex, int x, int y) {
        renderSprite(g2d, spriteSheetName, spriteIndex, x, y, GameConfig.SPRITE_SCALE, false);
    }
    
    /**
     * Renderiza um sprite estático com escala e flip
     */
    public void renderSprite(Graphics2D g2d, String spriteSheetName, int spriteIndex, int x, int y, int scale, boolean flipX) {
        BufferedImage sprite = animationManager.getSprite(spriteSheetName, spriteIndex);
        if (sprite != null) {
            renderSprite(g2d, sprite, x, y, scale, flipX);
        }
    }
    
    /**
     * Renderiza um sprite por coordenadas
     */
    public void renderSprite(Graphics2D g2d, String spriteSheetName, int spriteX, int spriteY, int x, int y) {
        renderSprite(g2d, spriteSheetName, spriteX, spriteY, x, y, GameConfig.SPRITE_SCALE, false);
    }
    
    /**
     * Renderiza um sprite por coordenadas com escala e flip
     */
    public void renderSprite(Graphics2D g2d, String spriteSheetName, int spriteX, int spriteY, int x, int y, int scale, boolean flipX) {
        BufferedImage sprite = animationManager.getSprite(spriteSheetName, spriteX, spriteY);
        if (sprite != null) {
            renderSprite(g2d, sprite, x, y, scale, flipX);
        }
    }
    
    /**
     * Renderiza uma animação
     */
    public void renderAnimation(Graphics2D g2d, String animationName, int x, int y) {
        renderAnimation(g2d, animationName, x, y, GameConfig.SPRITE_SCALE, false);
    }
    
    /**
     * Renderiza uma animação com escala e flip
     */
    public void renderAnimation(Graphics2D g2d, String animationName, int x, int y, int scale, boolean flipX) {
        Animation animation = animationManager.getAnimation(animationName);
        if (animation != null) {
            BufferedImage currentFrame = animation.getCurrentFrame();
            if (currentFrame != null) {
                renderSprite(g2d, currentFrame, x, y, scale, flipX);
            }
        }
    }
    
    /**
     * Renderiza um sprite BufferedImage diretamente
     */
    public void renderSprite(Graphics2D g2d, BufferedImage sprite, int x, int y, int scale, boolean flipX) {
        if (sprite == null) return;

        // Trabalhar numa cópia e preservar o g2d original
        Graphics2D drawG = (Graphics2D) g2d.create();

        // Configurar qualidade de renderização
        if (useSmoothing) {
            drawG.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                useInterpolation ? RenderingHints.VALUE_INTERPOLATION_BILINEAR : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            drawG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Aplicar transformações
        if (flipX) {
            drawG.scale(-1, 1);
            x = -x - (sprite.getWidth() * scale);
        }

        if (scale != 1) {
            drawG.scale(scale, scale);
            x /= scale;
            y /= scale;
        }

        // Renderizar o sprite
        drawG.drawImage(sprite, x, y, null);

        // Descartar apenas a cópia
        drawG.dispose();
    }
    
    /**
     * Renderiza um sprite com rotação
     */
    public void renderSpriteRotated(Graphics2D g2d, BufferedImage sprite, int x, int y, double angle, int scale) {
        if (sprite == null) return;
        
        // Salvar configurações originais
        Graphics2D drawG = (Graphics2D) g2d.create();
        
        // Configurar qualidade de renderização
        if (useSmoothing) {
            drawG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            drawG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
        // Aplicar rotação
        drawG.rotate(Math.toRadians(angle), x + (sprite.getWidth() * scale) / 2, y + (sprite.getHeight() * scale) / 2);
        
        // Aplicar escala
        if (scale != 1) {
            drawG.scale(scale, scale);
            x /= scale;
            y /= scale;
        }
        
        // Renderizar o sprite
        drawG.drawImage(sprite, x, y, null);
        
        // Restaurar configurações
        drawG.dispose();
    }
    
    /**
     * Renderiza um sprite com efeito de pulso
     */
    public void renderSpritePulsing(Graphics2D g2d, BufferedImage sprite, int x, int y, float pulseIntensity, int scale) {
        if (sprite == null) return;
        
        // Calcular escala do pulso
        float pulseScale = 1.0f + (pulseIntensity * 0.2f);
        int scaledWidth = (int) (sprite.getWidth() * scale * pulseScale);
        int scaledHeight = (int) (sprite.getHeight() * scale * pulseScale);
        
        // Centralizar o sprite com o pulso
        int offsetX = (scaledWidth - sprite.getWidth() * scale) / 2;
        int offsetY = (scaledHeight - sprite.getHeight() * scale) / 2;
        
        renderSprite(g2d, sprite, x - offsetX, y - offsetY, scale, false);
    }
    
    /**
     * Renderiza um sprite com efeito de brilho
     */
    public void renderSpriteGlowing(Graphics2D g2d, BufferedImage sprite, int x, int y, Color glowColor, float glowIntensity, int scale) {
        if (sprite == null) return;
        
        // Salvar configurações originais
        Graphics2D drawG = (Graphics2D) g2d.create();
        
        // Aplicar efeito de brilho
        drawG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowIntensity));
        drawG.setColor(glowColor);
        
        // Desenhar brilho (sprite ligeiramente maior)
        int glowSize = 4;
        drawG.fillRect(x - glowSize, y - glowSize, 
                    sprite.getWidth() * scale + glowSize * 2, 
                    sprite.getHeight() * scale + glowSize * 2);
        
        // Restaurar transparência
        drawG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Renderizar o sprite normal
        renderSprite(drawG, sprite, x, y, scale, false);
        
        // Restaurar configurações
        drawG.dispose();
    }
    
    /**
     * Renderiza um sprite com efeito de sombra
     */
    public void renderSpriteWithShadow(Graphics2D g2d, BufferedImage sprite, int x, int y, int shadowOffsetX, int shadowOffsetY, Color shadowColor, int scale) {
        if (sprite == null) return;
        
        // Salvar configurações originais
        Graphics2D drawG = (Graphics2D) g2d.create();
        
        // Renderizar sombra
        drawG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        drawG.setColor(shadowColor);
        drawG.fillRect(x + shadowOffsetX, y + shadowOffsetY, sprite.getWidth() * scale, sprite.getHeight() * scale);
        
        // Restaurar transparência
        drawG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Renderizar o sprite normal
        renderSprite(drawG, sprite, x, y, scale, false);
        
        // Restaurar configurações
        drawG.dispose();
    }
    
    /**
     * Obtém o tamanho de um sprite
     */
    public Dimension getSpriteSize(String spriteSheetName, int spriteIndex) {
        BufferedImage sprite = animationManager.getSprite(spriteSheetName, spriteIndex);
        if (sprite != null) {
            return new Dimension(sprite.getWidth() * GameConfig.SPRITE_SCALE, 
                               sprite.getHeight() * GameConfig.SPRITE_SCALE);
        }
        return new Dimension(0, 0);
    }
    
    /**
     * Obtém o tamanho de uma animação
     */
    public Dimension getAnimationSize(String animationName) {
        Animation animation = animationManager.getAnimation(animationName);
        if (animation != null) {
            BufferedImage frame = animation.getCurrentFrame();
            if (frame != null) {
                return new Dimension(frame.getWidth() * GameConfig.SPRITE_SCALE, 
                                   frame.getHeight() * GameConfig.SPRITE_SCALE);
            }
        }
        return new Dimension(0, 0);
    }
    
    // Getters e Setters
    public boolean isUseSmoothing() { return useSmoothing; }
    public void setUseSmoothing(boolean useSmoothing) { this.useSmoothing = useSmoothing; }
    
    public boolean isUseInterpolation() { return useInterpolation; }
    public void setUseInterpolation(boolean useInterpolation) { this.useInterpolation = useInterpolation; }
}