package org.example.entities;

import org.example.components.*;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Entidade do Enemy usando sistema de componentes
 * Substitui a classe Enemy original com arquitetura mais modular
 */
public class EnemyEntity extends Entity {
    
    // Componentes específicos do enemy
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;
    
    // Configurações específicas do enemy
    private float patrolLeft;
    private float patrolRight;
    private float patrolSpeed = (float) GameConfig.ENEMY_SPEED;
    private int direction = 1; // 1 = direita, -1 = esquerda
    
    // Animação
    private int animationFrame = 0;
    private int eyeGlowIntensity = 0;
    private boolean glowIncreasing = true;
    
    // Sistema de sprites e animações
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;
    
    // Cores do enemy
    private Color enemyColor = new Color(150, 50, 50);
    private Color eyeColor = new Color(255, 100, 100);
    
    public EnemyEntity(float startX, float startY) {
        super("enemy", "Cyber Enemy");
        setPosition(startX, startY);
        setSize(30, 40);
        
        // Configurar limites de patrulha padrão
        this.patrolLeft = startX - 50;
        this.patrolRight = startX + 50;
        
        initializeComponents();
        setupCallbacks();
    }
    
    public EnemyEntity(float startX, float startY, float patrolLeft, float patrolRight) {
        super("enemy", "Cyber Enemy");
        setPosition(startX, startY);
        setSize(30, 40);
        
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        
        initializeComponents();
        setupCallbacks();
    }
    
    private void initializeComponents() {
        // Componente de movimento
        movement = new MovementComponent(this);
        movement.setMaxSpeed(patrolSpeed);
        movement.setCanMove(true);
        addComponent(movement);
        
        // Componente de colisão
        collision = new CollisionComponent(this);
        collision.setCollisionLayer("enemy");
        collision.setSolid(true);
        addComponent(collision);
        
        // Componente de saúde (enemies têm 1 HP, sem vidas)
        health = new HealthComponent(this, 1, 0);
        addComponent(health);
        
        // Componente de renderização
        render = new RenderComponent(this, enemyColor, Color.BLACK);
        render.setBorderWidth(1);
        addComponent(render);
        
        // Inicializar sistema de sprites
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "enemy_idle";
    }
    
    private void setupCallbacks() {
        // Callback de colisão
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            handleCollision(other, otherCollision);
        });
        
        // Callback de morte
        health.setOnDeath((entity, healthComp, value) -> {
            System.out.println("Enemy eliminado!");
            // Lógica de eliminação será gerenciada pelo GamePanel
        });
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Atualizar patrulha
        updatePatrol(deltaTime);
        
        // Atualizar animação
        updateAnimation(deltaTime);
        
        // Atualizar animação de sprite
        updateSpriteAnimation();
    }
    
    private void updatePatrol(float deltaTime) {
        // Mover na direção atual
        movement.applyForceX(direction * patrolSpeed * 10);
        
        // Verificar limites de patrulha
        if (x <= patrolLeft) {
            direction = 1; // Mover para direita
            x = patrolLeft;
            movement.stopHorizontal();
        } else if (x >= patrolRight) {
            direction = -1; // Mover para esquerda
            x = patrolRight;
            movement.stopHorizontal();
        }
    }
    
    private void updateAnimation(float deltaTime) {
        // Animação dos olhos brilhantes
        if (glowIncreasing) {
            eyeGlowIntensity += 2;
            if (eyeGlowIntensity >= 255) {
                glowIncreasing = false;
            }
        } else {
            eyeGlowIntensity -= 2;
            if (eyeGlowIntensity <= 100) {
                glowIncreasing = true;
            }
        }
        
        // Atualizar frame de animação
        animationFrame = (animationFrame + 1) % 60; // 1 segundo de ciclo
    }
    
    /**
     * Atualiza a animação de sprite baseada no estado do inimigo
     */
    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        
        String newAnimation = "enemy_idle";
        if (Math.abs(movement.getVelocityX()) > 0.1) {
            newAnimation = "enemy_walk";
        }
        
        // Mudar animação apenas se for diferente da atual
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
            render.setCurrentAnimation(currentAnimation);
        }
    }
    
    private void handleCollision(Entity other, CollisionComponent otherCollision) {
        // Lógica de colisão específica do enemy
        if (otherCollision.getCollisionLayer().equals("sprites/player")) {
            handlePlayerCollision(other);
        } else if (otherCollision.getCollisionLayer().equals("platform")) {
            // Enemy não deve cair das plataformas
            // Isso será gerenciado pelo PhysicsEngine
        }
    }
    
    private void handlePlayerCollision(Entity player) {
        // Verificar se player está vindo de cima
        MovementComponent playerMovement = player.getComponent(MovementComponent.class);
        if (playerMovement != null && playerMovement.getVelocityY() > 0 && 
            player.y + player.height - 10 < y) {
            // Player pulou em cima - enemy é eliminado
            health.takeDamage(1);
        }
        // Se player tocou lateralmente, o player perde vida (gerenciado pelo PlayerEntity)
    }
    
    // Métodos para compatibilidade com código existente
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Rectangle getHitbox() { return collision.getHitbox(); }
    public int getDirection() { return direction; }
    public float getVelocityX() { return movement.getVelocityX(); }
    public float getVelocityY() { return movement.getVelocityY(); }
    public boolean isOnGround() { return movement.isOnGround(); }
    
    public void setDirection(int direction) { 
        this.direction = direction; 
    }
    
    public void setPatrolBounds(float left, float right) {
        this.patrolLeft = left;
        this.patrolRight = right;
    }
    
    // Método de renderização personalizada (compatibilidade)
    public void draw(Graphics2D g2d) {
        if (GameConfig.ANIMATIONS_ENABLED) {
            // Renderizar com sprite usando RenderComponent
            render.render(g2d);
        } else {
            // Renderização legada com formas geométricas
            render.render(g2d);
            
            // Adicionar olhos brilhantes
            drawEyes(g2d);
        }
    }
    
    private void drawEyes(Graphics2D g2d) {
        // Olhos brilhantes
        Color glowColor = new Color(eyeColor.getRed(), eyeColor.getGreen(), 
                                  eyeColor.getBlue(), eyeGlowIntensity);
        g2d.setColor(glowColor);
        
        // Olho esquerdo
        g2d.fillOval((int)x + 5, (int)y + 8, 6, 6);
        // Olho direito
        g2d.fillOval((int)x + 19, (int)y + 8, 6, 6);
        
        // Brilho dos olhos
        g2d.setColor(new Color(255, 255, 255, eyeGlowIntensity / 2));
        g2d.fillOval((int)x + 6, (int)y + 9, 2, 2);
        g2d.fillOval((int)x + 20, (int)y + 9, 2, 2);
    }
}
