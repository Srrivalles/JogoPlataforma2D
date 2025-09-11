package org.example.entities;

import org.example.components.*;
import org.example.ui.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Entidade EnergyOrb usando sistema de componentes
 * Demonstra como criar novos tipos de entidades facilmente
 */
public class EnergyOrbEntity extends Entity {
    
    // Componentes específicos do orb
    private MovementComponent movement;
    private CollisionComponent collision;
    private RenderComponent render;
    
    // Configurações específicas do orb
    private int energyValue;
    private Color orbColor;
    private boolean collected = false;
    private boolean isAttractedToPlayer = false;
    // private float attractionRange = (float) GameConfig.ORB_ATTRACTION_RANGE; // Removido - não utilizado
    
    // Animação
    private float pulseOffset = 0;
    // private int animationTimer = 0; // Removido - não utilizado
    
    public EnergyOrbEntity(float x, float y, int energyValue, Color color) {
        super("energy_orb", "Energy Orb");
        setPosition(x, y);
        setSize(GameConfig.ORB_SIZE, GameConfig.ORB_SIZE);
        
        this.energyValue = energyValue;
        this.orbColor = color;
        
        initializeComponents();
        setupCallbacks();
    }
    
    private void initializeComponents() {
        // Componente de movimento (para atração magnética)
        movement = new MovementComponent(this);
        movement.setMaxSpeed(5.0f);
        addComponent(movement);
        
        // Componente de colisão
        collision = new CollisionComponent(this);
        collision.setCollisionLayer("orb");
        collision.setTrigger(true); // Orbs são triggers, não sólidos
        addComponent(collision);
        
        // Componente de renderização
        render = new RenderComponent(this, orbColor, Color.WHITE);
        render.setBorderWidth(1);
        addComponent(render);
    }
    
    private void setupCallbacks() {
        // Callback de colisão
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            if (otherCollision.getCollisionLayer().equals("sprites/player")) {
                collectOrb();
            }
        });
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Atualizar animação
        updateAnimation(deltaTime);
        
        // Atualizar atração magnética
        updateMagneticAttraction(deltaTime);
    }
    
    private void updateAnimation(float deltaTime) {
        // Animação de pulsação
        pulseOffset += deltaTime * 3.0f;
        // animationTimer++; // Comentado - variável removida
        
        // Efeito visual de pulsação
        float pulse = (float) (Math.sin(pulseOffset) * 0.2f + 0.8f);
        render.setAlpha(pulse);
    }
    
    private void updateMagneticAttraction(float deltaTime) {
        if (isAttractedToPlayer && !collected) {
            // Lógica de atração será implementada quando tivermos referência ao player
            // Por enquanto, apenas animação
        }
    }
    
    private void collectOrb() {
        if (collected) return;
        
        collected = true;
        setActive(false); // Desativar entidade
        
        // Efeito visual de coleta
        render.setAlpha(0.0f);
        
        System.out.println("Orb coletada! Energia: " + energyValue);
    }
    
    // Métodos para compatibilidade
    public boolean isCollected() { return collected; }
    public int getEnergyValue() { return energyValue; }
    public Color getOrbColor() { return orbColor; }
    
    // Método de renderização personalizada
    public void draw(Graphics2D g2d) {
        if (collected) return;
        
        // Renderização básica pelo RenderComponent
        render.render(g2d);
        
        // Adicionar efeito de brilho
        drawGlow(g2d);
    }
    
    private void drawGlow(Graphics2D g2d) {
        // Efeito de brilho ao redor do orb
        float glowIntensity = (float) (Math.sin(pulseOffset * 2) * 0.3f + 0.7f);
        Color glowColor = new Color(orbColor.getRed(), orbColor.getGreen(), 
                                  orbColor.getBlue(), (int)(glowIntensity * 100));
        
        g2d.setColor(glowColor);
        g2d.fillOval((int)x - 4, (int)y - 4, (int)width + 8, (int)height + 8);
    }
}
