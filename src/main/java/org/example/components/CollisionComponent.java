package org.example.components;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente responsável pela detecção e resposta a colisões
 * Gerencia hitbox e eventos de colisão
 */
public class CollisionComponent implements Component {
    
    private Entity entity;
    private boolean active = true;
    
    // Hitbox
    private Rectangle hitbox;
    private float offsetX = 0;
    private float offsetY = 0;
    
    // Configurações de colisão
    public boolean isSolid = true;
    public boolean isTrigger = false; // Para colisões que não bloqueiam movimento
    public String collisionLayer = "default";
    
    // Lista de entidades em colisão
    private List<Entity> collidingEntities;
    
    // Callbacks de colisão
    private CollisionCallback onCollisionEnter;
    private CollisionCallback onCollisionExit;
    private CollisionCallback onCollisionStay;
    
    public interface CollisionCallback {
        void onCollision(Entity thisEntity, Entity otherEntity, CollisionComponent otherCollision);
    }
    
    public CollisionComponent(Entity entity) {
        this.entity = entity;
        this.collidingEntities = new ArrayList<>();
        updateHitbox();
    }
    
    public CollisionComponent(Entity entity, float offsetX, float offsetY) {
        this.entity = entity;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.collidingEntities = new ArrayList<>();
        updateHitbox();
    }
    
    @Override
    public void update(float deltaTime) {
        if (!active) return;
        
        // Atualizar hitbox com a posição atual da entidade
        updateHitbox();
    }
    
    /**
     * Atualiza a posição da hitbox baseada na posição da entidade
     */
    private void updateHitbox() {
        if (hitbox == null) {
            hitbox = new Rectangle();
        }
        
        hitbox.setBounds(
            (int)(entity.x + offsetX),
            (int)(entity.y + offsetY),
            (int)entity.width,
            (int)entity.height
        );
    }
    
    /**
     * Verifica colisão com outra entidade
     * @param other outra entidade
     * @return true se há colisão
     */
    public boolean checkCollision(Entity other) {
        CollisionComponent otherCollision = other.getComponent(CollisionComponent.class);
        if (otherCollision == null || !otherCollision.isActive()) {
            return false;
        }
        
        return hitbox.intersects(otherCollision.getHitbox());
    }
    
    /**
     * Verifica colisão com um retângulo
     * @param rect retângulo para verificar
     * @return true se há colisão
     */
    public boolean checkCollision(Rectangle rect) {
        return hitbox.intersects(rect);
    }
    
    /**
     * Processa colisão com outra entidade
     * @param other outra entidade
     */
    public void processCollision(Entity other) {
        CollisionComponent otherCollision = other.getComponent(CollisionComponent.class);
        if (otherCollision == null) return;
        
        boolean wasColliding = collidingEntities.contains(other);
        
        if (!wasColliding) {
            // Nova colisão
            collidingEntities.add(other);
            if (onCollisionEnter != null) {
                onCollisionEnter.onCollision(entity, other, otherCollision);
            }
        } else {
            // Colisão contínua
            if (onCollisionStay != null) {
                onCollisionStay.onCollision(entity, other, otherCollision);
            }
        }
    }
    
    /**
     * Remove entidade da lista de colisões
     * @param other entidade que não está mais colidindo
     */
    public void removeCollision(Entity other) {
        if (collidingEntities.remove(other) && onCollisionExit != null) {
            CollisionComponent otherCollision = other.getComponent(CollisionComponent.class);
            onCollisionExit.onCollision(entity, other, otherCollision);
        }
    }
    
    /**
     * Limpa todas as colisões ativas
     */
    public void clearCollisions() {
        for (Entity other : collidingEntities) {
            if (onCollisionExit != null) {
                CollisionComponent otherCollision = other.getComponent(CollisionComponent.class);
                onCollisionExit.onCollision(entity, other, otherCollision);
            }
        }
        collidingEntities.clear();
    }
    
    /**
     * Obtém a distância até outra entidade
     * @param other outra entidade
     * @return distância em pixels
     */
    public float getDistanceTo(Entity other) {
        float dx = entity.x - other.x;
        float dy = entity.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Obtém a direção normalizada para outra entidade
     * @param other outra entidade
     * @return array [x, y] com direção normalizada
     */
    public float[] getDirectionTo(Entity other) {
        float dx = other.x - entity.x;
        float dy = other.y - entity.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance == 0) {
            return new float[]{0, 0};
        }
        
        return new float[]{dx / distance, dy / distance};
    }
    
    // Getters e Setters
    public Rectangle getHitbox() { return hitbox; }
    public List<Entity> getCollidingEntities() { return new ArrayList<>(collidingEntities); }
    public boolean isSolid() { return isSolid; }
    public void setSolid(boolean solid) { this.isSolid = solid; }
    public boolean isTrigger() { return isTrigger; }
    public void setTrigger(boolean trigger) { this.isTrigger = trigger; }
    public String getCollisionLayer() { return collisionLayer; }
    public void setCollisionLayer(String layer) { this.collisionLayer = layer; }
    
    // Callbacks
    public void setOnCollisionEnter(CollisionCallback callback) { this.onCollisionEnter = callback; }
    public void setOnCollisionExit(CollisionCallback callback) { this.onCollisionExit = callback; }
    public void setOnCollisionStay(CollisionCallback callback) { this.onCollisionStay = callback; }
    
    @Override
    public void initialize() {
        updateHitbox();
    }
    
    @Override
    public void dispose() {
        clearCollisions();
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
