package org.example.entities;

import org.example.components.*;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Exemplo de novo tipo de inimigo usando sistema de componentes
 * Demonstra como criar entidades complexas facilmente
 */
public class FlyingEnemyEntity extends Entity {
    
    // Componentes específicos do flying enemy
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;
    
    // Configurações específicas
    private float patrolLeft;
    private float patrolRight;
    private float patrolTop;
    private float patrolBottom;
    private float flySpeed = 2.0f;
    private int direction = 1; // 1 = direita, -1 = esquerda
    
    // Animação
    private int wingFlapTimer = 0;
    private boolean wingsUp = false;
    
    // Cores
    private Color bodyColor = new Color(100, 50, 150);
    private Color wingColor = new Color(150, 100, 200);
    
    public FlyingEnemyEntity(float x, float y, float patrolLeft, float patrolRight, 
                           float patrolTop, float patrolBottom) {
        super("flying_enemy", "Flying Cyber Enemy");
        setPosition(x, y);
        setSize(40, 30);
        
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.patrolTop = patrolTop;
        this.patrolBottom = patrolBottom;
        
        initializeComponents();
        setupCallbacks();
    }
    
    private void initializeComponents() {
        // Componente de movimento
        movement = new MovementComponent(this);
        movement.setMaxSpeed(flySpeed);
        addComponent(movement);
        
        // Componente de colisão
        collision = new CollisionComponent(this);
        collision.setCollisionLayer("flying_enemy");
        collision.setSolid(true);
        addComponent(collision);
        
        // Componente de saúde
        health = new HealthComponent(this, 1, 0);
        addComponent(health);
        
        // Componente de renderização
        render = new RenderComponent(this, bodyColor, Color.BLACK);
        render.setBorderWidth(1);
        addComponent(render);
    }
    
    private void setupCallbacks() {
        // Callback de colisão
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            if (otherCollision.getCollisionLayer().equals("player")) {
                handlePlayerCollision(other);
            }
        });
        
        // Callback de morte
        health.setOnDeath((entity, healthComp, value) -> {
            System.out.println("Flying Enemy eliminado!");
        });
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Atualizar patrulha aérea
        updateAerialPatrol(deltaTime);
        
        // Atualizar animação das asas
        updateWingAnimation(deltaTime);
    }
    
    private void updateAerialPatrol(float deltaTime) {
        // Movimento horizontal
        movement.applyForceX(direction * flySpeed * 10);
        
        // Movimento vertical (voo em padrão senoidal)
        float verticalMovement = (float) Math.sin(System.currentTimeMillis() * 0.003) * 2.0f;
        movement.applyForceY(verticalMovement);
        
        // Verificar limites horizontais
        if (x <= patrolLeft) {
            direction = 1;
            x = patrolLeft;
        } else if (x >= patrolRight) {
            direction = -1;
            x = patrolRight;
        }
        
        // Verificar limites verticais
        if (y <= patrolTop) {
            y = patrolTop;
            movement.stopVertical();
        } else if (y >= patrolBottom) {
            y = patrolBottom;
            movement.stopVertical();
        }
    }
    
    private void updateWingAnimation(float deltaTime) {
        wingFlapTimer++;
        if (wingFlapTimer >= 10) { // A cada 10 frames
            wingsUp = !wingsUp;
            wingFlapTimer = 0;
        }
    }
    
    private void handlePlayerCollision(Entity player) {
        // Verificar se player está vindo de baixo (pular para cima)
        MovementComponent playerMovement = player.getComponent(MovementComponent.class);
        if (playerMovement != null && playerMovement.getVelocityY() < 0 && 
            player.y > y + height) {
            // Player pulou para cima - enemy é eliminado
            health.takeDamage(1);
        }
        // Se player tocou de qualquer outra forma, player perde vida
    }
    
    // Método de renderização personalizada
    public void draw(Graphics2D g2d) {
        // Renderização básica pelo RenderComponent
        render.render(g2d);
        
        // Adicionar asas
        drawWings(g2d);
        
        // Adicionar olhos
        drawEyes(g2d);
    }
    
    private void drawWings(Graphics2D g2d) {
        g2d.setColor(wingColor);
        
        if (wingsUp) {
            // Asas para cima
                    g2d.fillOval((int)x - 8, (int)y + 5, 12, 8);
        g2d.fillOval((int)x + (int)width - 4, (int)y + 5, 12, 8);
        } else {
            // Asas para baixo
            g2d.fillOval((int)x - 6, (int)y + 15, 10, 6);
            g2d.fillOval((int)x + (int)width - 4, (int)y + 15, 10, 6);
        }
    }
    
    private void drawEyes(Graphics2D g2d) {
        // Olhos brilhantes
        g2d.setColor(new Color(255, 100, 100));
        g2d.fillOval((int)x + 8, (int)y + 8, 4, 4);
        g2d.fillOval((int)x + 20, (int)y + 8, 4, 4);
        
        // Brilho dos olhos
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)x + 9, (int)y + 9, 1, 1);
        g2d.fillOval((int)x + 21, (int)y + 9, 1, 1);
    }
    
    // Getters para compatibilidade
    public int getDirection() { return direction; }
    public float getVelocityX() { return movement.getVelocityX(); }
    public float getVelocityY() { return movement.getVelocityY(); }
}
