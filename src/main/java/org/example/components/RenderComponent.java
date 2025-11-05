package org.example.components;

import org.example.graphics.Animation;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente responsável pela renderização de uma entidade
 * Gerencia sprites, animações e efeitos visuais
 */
public class RenderComponent implements Component {
    
    private Entity entity;
    private boolean active = true;
    
    // Configurações de renderização
    private boolean visible = true;
    private float alpha = 1.0f;
    private Color tint = Color.WHITE;
    private int renderOrder = 0; // Ordem de renderização (menor = primeiro)
    
    // Forma básica para renderização
    // private Shape renderShape; // Removido - não utilizado
    private Color fillColor = Color.GRAY;
    private Color borderColor = Color.BLACK;
    private int borderWidth = 1;
    
    // Sistema de sprites e animações
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;
    private String spriteSheetName;
    private int currentSpriteIndex;
    private boolean useSprites = false;
    private boolean flipX = false;
    private boolean flipY = false;
    private float rotation = 0.0f;
    
    // Animação (sistema legado - mantido para compatibilidade)
    private boolean animated = false;
    private int animationFrame = 0;
    private int animationSpeed = 1; // frames por segundo
    private float animationTimer = 0;
    private int maxFrames = 1;
    
    // Efeitos visuais
    private List<VisualEffect> effects;
    
    public interface VisualEffect {
        void update(float deltaTime);
        void render(Graphics2D g2d, float x, float y, float width, float height);
        boolean isFinished();
    }
    
    public RenderComponent(Entity entity) {
        this.entity = entity;
        this.effects = new ArrayList<>();
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
    }
    
    public RenderComponent(Entity entity, Color fillColor, Color borderColor) {
        this.entity = entity;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.effects = new ArrayList<>();
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
    }
    
    public RenderComponent(Entity entity, String spriteSheetName, int spriteIndex) {
        this.entity = entity;
        this.effects = new ArrayList<>();
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.spriteSheetName = spriteSheetName;
        this.currentSpriteIndex = spriteIndex;
        this.useSprites = true;
    }
    
    public RenderComponent(Entity entity, String animationName) {
        this.entity = entity;
        this.effects = new ArrayList<>();
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = animationName;
        this.useSprites = true;
    }
    
    @Override
    public void update(float deltaTime) {
        if (!active || !visible) return;
        
        // Atualizar animação de sprite
        if (useSprites && currentAnimation != null) {
            Animation animation = animationManager.getAnimation(currentAnimation);
            if (animation != null) {
                animation.update();
            }
        }
        
        // Atualizar animação legada
        if (animated) {
            animationTimer += deltaTime;
            if (animationTimer >= 1.0f / animationSpeed) {
                animationFrame = (animationFrame + 1) % maxFrames;
                animationTimer = 0;
            }
        }
        
        // Atualizar efeitos visuais
        effects.removeIf(effect -> {
            effect.update(deltaTime);
            return effect.isFinished();
        });
    }
    
    /**
     * Renderiza a entidade
     * @param g2d contexto gráfico
     */
    public void render(Graphics2D g2d) {
        if (!active || !visible) return;
        
        // Salvar configurações originais
        Color originalColor = g2d.getColor();
        java.awt.Composite originalComposite = g2d.getComposite();
        
        // Aplicar transparência
        if (alpha < 1.0f) {
            g2d.setComposite(java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, alpha));
        }
        
        // Renderizar sprite ou forma básica
        if (useSprites && GameConfig.ANIMATIONS_ENABLED) {
            renderSprite(g2d);
        } else {
            // Renderização legada com formas geométricas
            renderBasicShape(g2d);
        }
        
        // Renderizar efeitos visuais
        for (VisualEffect effect : effects) {
            effect.render(g2d, entity.x, entity.y, entity.width, entity.height);
        }
        
        // Restaurar configurações
        g2d.setColor(originalColor);
        g2d.setComposite(originalComposite);
    }
    
    /**
     * Renderiza sprite ou animação
     */
    private void renderSprite(Graphics2D g2d) {
        int x = (int) entity.x;
        int y = (int) entity.y;
        
        if (currentAnimation != null) {
            // Renderizar animação
            spriteRenderer.renderAnimation(g2d, currentAnimation, x, y, GameConfig.SPRITE_SCALE, flipX);
        } else if (spriteSheetName != null) {
            // Renderizar sprite estático
            spriteRenderer.renderSprite(g2d, spriteSheetName, currentSpriteIndex, x, y, GameConfig.SPRITE_SCALE, flipX);
        }
    }
    
    /**
     * Renderiza a forma básica da entidade
     * @param g2d contexto gráfico
     */
    private void renderBasicShape(Graphics2D g2d) {
        // Forma padrão: retângulo
        g2d.fillRect((int)entity.x, (int)entity.y, (int)entity.width, (int)entity.height);
        
        // Borda se especificada
        if (borderWidth > 0) {
            g2d.setColor(borderColor);
            g2d.setStroke(new java.awt.BasicStroke(borderWidth));
            g2d.drawRect((int)entity.x, (int)entity.y, (int)entity.width, (int)entity.height);
        }
    }
    
    /**
     * Mistura duas cores
     * @param base cor base
     * @param tint cor de tint
     * @return cor resultante
     */
    private Color blendColors(Color base, Color tint) {
        float r = (base.getRed() + tint.getRed()) / 2.0f / 255.0f;
        float g = (base.getGreen() + tint.getGreen()) / 2.0f / 255.0f;
        float b = (base.getBlue() + tint.getBlue()) / 2.0f / 255.0f;
        return new Color(r, g, b);
    }
    
    /**
     * Adiciona um efeito visual
     * @param effect efeito a ser adicionado
     */
    public void addEffect(VisualEffect effect) {
        effects.add(effect);
    }
    
    /**
     * Remove todos os efeitos visuais
     */
    public void clearEffects() {
        effects.clear();
    }
    
    /**
     * Configura animação
     * @param maxFrames número máximo de frames
     * @param speed velocidade da animação (frames por segundo)
     */
    public void setAnimation(int maxFrames, int speed) {
        this.animated = true;
        this.maxFrames = maxFrames;
        this.animationSpeed = speed;
        this.animationFrame = 0;
        this.animationTimer = 0;
    }
    
    /**
     * Para a animação
     */
    public void stopAnimation() {
        this.animated = false;
        this.animationFrame = 0;
    }
    
    /**
     * Define a forma de renderização
     * @param shape forma personalizada
     */
    public void setRenderShape(Shape shape) {
        // this.renderShape = shape; // Comentado - variável removida
    }
    
    // Getters e Setters
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public float getAlpha() { return alpha; }
    public void setAlpha(float alpha) { this.alpha = Math.max(0, Math.min(1, alpha)); }
    public Color getTint() { return tint; }
    public void setTint(Color tint) { this.tint = tint; }
    public int getRenderOrder() { return renderOrder; }
    public void setRenderOrder(int order) { this.renderOrder = order; }
    public Color getFillColor() { return fillColor; }
    public void setFillColor(Color color) { this.fillColor = color; }
    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color color) { this.borderColor = color; }
    public int getBorderWidth() { return borderWidth; }
    public void setBorderWidth(int width) { this.borderWidth = width; }
    public int getAnimationFrame() { return animationFrame; }
    public boolean isAnimated() { return animated; }
    public List<VisualEffect> getEffects() { return new ArrayList<>(effects); }
    
    // Métodos para controle de sprites e animações
    public void setCurrentAnimation(String animationName) {
        this.currentAnimation = animationName;
        this.useSprites = true;
        if (animationName != null) {
            Animation animation = animationManager.getAnimation(animationName);
            if (animation != null) {
                animation.reset();
            }
        }
    }
    
    public String getCurrentAnimation() { return currentAnimation; }
    
    public void setSpriteSheet(String spriteSheetName, int spriteIndex) {
        this.spriteSheetName = spriteSheetName;
        this.currentSpriteIndex = spriteIndex;
        this.useSprites = true;
        this.currentAnimation = null; // Limpar animação quando usar sprite estático
    }
    
    public String getSpriteSheetName() { return spriteSheetName; }
    public int getCurrentSpriteIndex() { return currentSpriteIndex; }
    
    public void setFlipX(boolean flipX) { this.flipX = flipX; }
    public boolean isFlipX() { return flipX; }
    
    public void setFlipY(boolean flipY) { this.flipY = flipY; }
    public boolean isFlipY() { return flipY; }
    
    public void setRotation(float rotation) { this.rotation = rotation; }
    public float getRotation() { return rotation; }
    
    public void setUseSprites(boolean useSprites) { this.useSprites = useSprites; }
    public boolean isUseSprites() { return useSprites; }
    
    public void resetAnimation() {
        if (currentAnimation != null) {
            Animation animation = animationManager.getAnimation(currentAnimation);
            if (animation != null) {
                animation.reset();
            }
        }
    }
    
    public boolean isAnimationFinished() {
        if (currentAnimation != null) {
            Animation animation = animationManager.getAnimation(currentAnimation);
            if (animation != null) {
                return animation.isFinished();
            }
        }
        return false;
    }
    
    @Override
    public void initialize() {
        // Inicialização se necessário
    }
    
    @Override
    public void dispose() {
        clearEffects();
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
