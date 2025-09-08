package org.example.components;

import org.example.ui.GameConfig;

/**
 * Componente responsável pelo movimento de uma entidade
 * Gerencia velocidade, direção e aplicação de forças
 */
public class MovementComponent implements Component {
    
    private Entity entity;
    private boolean active = true;
    
    // Velocidades
    public float velocityX = 0;
    public float velocityY = 0;
    
    // Configurações de movimento
    public float maxSpeed = (float) GameConfig.PLAYER_SPEED;
    public float acceleration = 0.5f;
    public float friction = (float) GameConfig.FRICTION;
    
    // Estado de movimento
    public boolean facingRight = true;
    public boolean isOnGround = false;
    public boolean canMove = true;
    
    // Forças aplicadas
    private float forceX = 0;
    private float forceY = 0;
    
    public MovementComponent(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public void update(float deltaTime) {
        if (!active || !canMove) return;
        
        // Aplicar forças
        velocityX += forceX * deltaTime;
        velocityY += forceY * deltaTime;
        
        // Limitar velocidade máxima
        if (Math.abs(velocityX) > maxSpeed) {
            velocityX = Math.signum(velocityX) * maxSpeed;
        }
        
        // Aplicar atrito quando no chão
        if (isOnGround && Math.abs(velocityX) > 0.1f) {
            velocityX *= friction;
        }
        
        // Atualizar direção
        if (velocityX > 0.1f) {
            facingRight = true;
        } else if (velocityX < -0.1f) {
            facingRight = false;
        }
        
        // Aplicar movimento à entidade
        entity.x += velocityX * deltaTime * 60; // 60 FPS base
        entity.y += velocityY * deltaTime * 60;
        
        // Resetar forças
        forceX = 0;
        forceY = 0;
    }
    
    /**
     * Aplica uma força horizontal
     * @param force magnitude da força
     */
    public void applyForceX(float force) {
        this.forceX += force;
    }
    
    /**
     * Aplica uma força vertical
     * @param force magnitude da força
     */
    public void applyForceY(float force) {
        this.forceY += force;
    }
    
    /**
     * Define a velocidade diretamente
     * @param velocityX velocidade horizontal
     * @param velocityY velocidade vertical
     */
    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    /**
     * Aplica um impulso (força instantânea)
     * @param impulseX impulso horizontal
     * @param impulseY impulso vertical
     */
    public void applyImpulse(float impulseX, float impulseY) {
        this.velocityX += impulseX;
        this.velocityY += impulseY;
    }
    
    /**
     * Para o movimento horizontal
     */
    public void stopHorizontal() {
        velocityX = 0;
    }
    
    /**
     * Para o movimento vertical
     */
    public void stopVertical() {
        velocityY = 0;
    }
    
    /**
     * Para todo o movimento
     */
    public void stop() {
        velocityX = 0;
        velocityY = 0;
    }
    
    // Getters e Setters
    public float getVelocityX() { return velocityX; }
    public float getVelocityY() { return velocityY; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return isOnGround; }
    public void setOnGround(boolean onGround) { this.isOnGround = onGround; }
    public void setCanMove(boolean canMove) { this.canMove = canMove; }
    public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
    public boolean isCanMove() { return canMove; }
    public float getMaxSpeed() { return maxSpeed; }
    
    @Override
    public void initialize() {
        // Inicialização se necessário
    }
    
    @Override
    public void dispose() {
        // Limpeza se necessário
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
