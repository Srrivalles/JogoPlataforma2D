package org.example.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador central de animações e sprite sheets
 */
public class AnimationManager {
    
    private static AnimationManager instance;
    
    // Mapas para armazenar sprite sheets e animações
    private Map<String, SpriteSheet> spriteSheets;
    private Map<String, Animation> animations;
    
    // Configurações
    private boolean enabled;
    private float globalSpeedMultiplier;
    
    private AnimationManager() {
        this.spriteSheets = new HashMap<>();
        this.animations = new HashMap<>();
        this.enabled = true;
        this.globalSpeedMultiplier = 1.0f;
        
        initializeDefaultSprites();
    }
    
    /**
     * Obtém a instância singleton do AnimationManager
     */
    public static AnimationManager getInstance() {
        if (instance == null) {
            instance = new AnimationManager();
        }
        return instance;
    }
    
    /**
     * Inicializa sprites padrão (placeholders)
     */
    private void initializeDefaultSprites() {
        // Player sprites
        registerSpriteSheet("sprites/player", "sprites/player/player_sheet.png", 32, 48);
        registerAnimation("player_idle", "sprites/player", 0, 3, 8.0f, true);
        registerAnimation("player_walk", "sprites/player", 4, 7, 12.0f, true);
        registerAnimation("player_jump", "sprites/player", 8, 10, 10.0f, false);
        registerAnimation("player_dash", "sprites/player", 11, 13, 15.0f, false);
        
        // Enemy sprites
        registerSpriteSheet("enemy", "sprites/enemies/enemy_sheet.png", 30, 40);
        registerAnimation("enemy_idle", "enemy", 0, 2, 6.0f, true);
        registerAnimation("enemy_walk", "enemy", 3, 5, 8.0f, true);
        registerAnimation("enemy_attack", "enemy", 6, 8, 10.0f, false);
        
        // Flying enemy sprites
        registerSpriteSheet("flying_enemy", "sprites/enemies/flying_enemy_sheet.png", 35, 25);
        registerAnimation("flying_idle", "flying_enemy", 0, 3, 10.0f, true);
        registerAnimation("flying_fly", "flying_enemy", 4, 7, 12.0f, true);
        
        // Energy orb sprites
        registerSpriteSheet("energy_orb", "sprites/objects/energy_orb_sheet.png", 24, 24);
        registerAnimation("orb_idle", "energy_orb", 0, 7, 8.0f, true);
        registerAnimation("orb_collect", "energy_orb", 8, 11, 15.0f, false);
        
        // Platform sprites
        registerSpriteSheet("platform", "sprites/world/platform_sheet.png", 64, 32);
        registerAnimation("platform_normal", "platform", 0, 0, 1.0f, true);
        registerAnimation("platform_moving", "platform", 1, 3, 6.0f, true);
        registerAnimation("platform_breakable", "platform", 4, 4, 1.0f, true);
        
        // Effect sprites
        registerSpriteSheet("effects", "sprites/effects/effects_sheet.png", 32, 32);
        registerAnimation("explosion", "effects", 0, 7, 20.0f, false);
        registerAnimation("particles", "effects", 8, 15, 12.0f, true);
    }
    
    /**
     * Registra um sprite sheet
     */
    public void registerSpriteSheet(String name, String resourcePath, int spriteWidth, int spriteHeight) {
        SpriteSheet spriteSheet = new SpriteSheet(name, resourcePath, spriteWidth, spriteHeight);
        spriteSheets.put(name, spriteSheet);
        System.out.println("Sprite sheet registrado: " + name);
    }
    
    /**
     * Registra uma animação
     */
    public void registerAnimation(String animationName, String spriteSheetName, int startIndex, int endIndex, float frameRate, boolean loop) {
        SpriteSheet spriteSheet = spriteSheets.get(spriteSheetName);
        if (spriteSheet != null) {
            Animation animation = spriteSheet.createAnimation(animationName, startIndex, endIndex, frameRate, loop);
            animations.put(animationName, animation);
            System.out.println("Animação registrada: " + animationName);
        } else {
            System.err.println("Erro: Sprite sheet não encontrado: " + spriteSheetName);
        }
    }
    
    /**
     * Obtém uma animação por nome
     */
    public Animation getAnimation(String name) {
        return animations.get(name);
    }
    
    /**
     * Obtém um sprite sheet por nome
     */
    public SpriteSheet getSpriteSheet(String name) {
        return spriteSheets.get(name);
    }
    
    /**
     * Obtém um sprite específico
     */
    public java.awt.image.BufferedImage getSprite(String spriteSheetName, int index) {
        SpriteSheet spriteSheet = spriteSheets.get(spriteSheetName);
        if (spriteSheet != null) {
            return spriteSheet.getSprite(index);
        }
        return null;
    }
    
    /**
     * Obtém um sprite por coordenadas
     */
    public java.awt.image.BufferedImage getSprite(String spriteSheetName, int x, int y) {
        SpriteSheet spriteSheet = spriteSheets.get(spriteSheetName);
        if (spriteSheet != null) {
            return spriteSheet.getSprite(x, y);
        }
        return null;
    }
    
    /**
     * Atualiza todas as animações
     */
    public void update() {
        if (!enabled) return;
        
        for (Animation animation : animations.values()) {
            animation.update();
        }
    }
    
    /**
     * Reinicia uma animação específica
     */
    public void resetAnimation(String animationName) {
        Animation animation = animations.get(animationName);
        if (animation != null) {
            animation.reset();
        }
    }
    
    /**
     * Pausa/despausa uma animação específica
     */
    public void setAnimationPaused(String animationName, boolean paused) {
        Animation animation = animations.get(animationName);
        if (animation != null) {
            animation.setPaused(paused);
        }
    }
    
    /**
     * Pausa/despausa todas as animações
     */
    public void setAllAnimationsPaused(boolean paused) {
        for (Animation animation : animations.values()) {
            animation.setPaused(paused);
        }
    }
    
    /**
     * Define o multiplicador de velocidade global
     */
    public void setGlobalSpeedMultiplier(float multiplier) {
        this.globalSpeedMultiplier = multiplier;
        
        // Aplicar o multiplicador a todas as animações
        for (Animation animation : animations.values()) {
            animation.setFrameRate(animation.getFrameRate() * multiplier);
        }
    }
    
    /**
     * Habilita/desabilita o sistema de animações
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    // Getters
    public boolean isEnabled() { return enabled; }
    public float getGlobalSpeedMultiplier() { return globalSpeedMultiplier; }
    public int getTotalSpriteSheets() { return spriteSheets.size(); }
    public int getTotalAnimations() { return animations.size(); }
    
    /**
     * Lista todas as animações disponíveis
     */
    public void listAnimations() {
        System.out.println("=== Animações Disponíveis ===");
        for (String name : animations.keySet()) {
            Animation anim = animations.get(name);
            System.out.println("- " + name + " (" + anim.getFrameCount() + " frames, " + anim.getFrameRate() + " FPS)");
        }
    }
    
    /**
     * Lista todos os sprite sheets disponíveis
     */
    public void listSpriteSheets() {
        System.out.println("=== Sprite Sheets Disponíveis ===");
        for (String name : spriteSheets.keySet()) {
            SpriteSheet sheet = spriteSheets.get(name);
            System.out.println("- " + name + " (" + sheet.getSpritesPerRow() + "x" + sheet.getSpritesPerColumn() + " sprites)");
        }
    }
}
