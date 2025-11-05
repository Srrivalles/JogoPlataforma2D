package org.example.systems;

import org.example.components.*;
import org.example.entities.PlayerEntity;
import org.example.entities.EnemyEntity;
import org.example.ui.GameConfig;
import org.example.world.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de física que trabalha com o sistema de componentes
 * Gerencia gravidade, colisões e interações entre entidades
 */
public class ComponentPhysicsEngine {
    
    /**
     * Aplica gravidade a uma entidade
     * @param entity entidade a receber gravidade
     */
    public static void applyGravity(Entity entity) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null && movement.isActive()) {
            movement.applyForceY((float) GameConfig.GRAVITY);
            
            // Limitar velocidade terminal
            if (movement.getVelocityY() > GameConfig.TERMINAL_VELOCITY) {
                movement.setVelocity(movement.getVelocityX(), (float) GameConfig.TERMINAL_VELOCITY);
            }
        }
    }
    
    /**
     * Verifica colisões entre player e plataformas
     * @param player entidade do player
     * @param platforms lista de plataformas
     */
    public static void checkPlayerPlatformCollisions(PlayerEntity player, List<Platform> platforms) {
        MovementComponent movement = player.getComponent(MovementComponent.class);
        CollisionComponent collision = player.getComponent(CollisionComponent.class);
        
        if (movement == null || collision == null) return;
        
        movement.setOnGround(false);
        
        // Salvar posição atual
        float oldX = player.x;
        
        // Aplicar movimento horizontal primeiro
        player.x += movement.getVelocityX();
        collision.update(0); // Atualizar hitbox
        
        // Verificar colisões horizontais
        for (Platform platform : platforms) {
            if (collision.checkCollision(platform.getHitbox())) {
                // Colisão horizontal - reverter movimento X
                player.x = oldX;
                movement.stopHorizontal();
                collision.update(0);
                break;
            }
        }
        
        // Aplicar movimento vertical
        player.y += movement.getVelocityY();
        collision.update(0);
        
        // Verificar colisões verticais
        for (Platform platform : platforms) {
            if (collision.checkCollision(platform.getHitbox())) {
                if (movement.getVelocityY() > 0) {
                    // Colisão por baixo - player está no chão
                    player.y = platform.y - player.height;
                    movement.stopVertical();
                    movement.setOnGround(true);
                } else {
                    // Colisão por cima - player bateu no teto
                    player.y = platform.y + platform.height;
                    movement.stopVertical();
                }
                collision.update(0);
                break;
            }
        }
    }
    
    /**
     * Verifica colisões entre enemies e plataformas
     * @param enemy entidade do enemy
     * @param platforms lista de plataformas
     */
    public static void checkEnemyPlatformCollisions(EnemyEntity enemy, List<Platform> platforms) {
        MovementComponent movement = enemy.getComponent(MovementComponent.class);
        CollisionComponent collision = enemy.getComponent(CollisionComponent.class);
        
        if (movement == null || collision == null) return;
        
        movement.setOnGround(false);
        
        // Aplicar gravidade ao enemy
        applyGravity(enemy);
        
        // Verificar colisões com plataformas
        for (Platform platform : platforms) {
            if (collision.checkCollision(platform.getHitbox())) {
                if (movement.getVelocityY() > 0) {
                    // Enemy está no chão
                    enemy.y = platform.y - enemy.height;
                    movement.stopVertical();
                    movement.setOnGround(true);
                }
                break;
            }
        }
        
        // Prevenir enemy de cair das plataformas
        preventEnemyFallFromPlatforms(enemy, platforms);
    }
    
    /**
     * Previne enemies de caírem das plataformas
     * @param enemy entidade do enemy
     * @param platforms lista de plataformas
     */
    public static void preventEnemyFallFromPlatforms(EnemyEntity enemy, List<Platform> platforms) {
        MovementComponent movement = enemy.getComponent(MovementComponent.class);
        if (movement == null) return;
        
        // Verificar se há plataforma à frente do enemy
        float checkDistance = 20; // Distância para verificar à frente
        float checkX = enemy.x + (enemy.getDirection() > 0 ? enemy.width + checkDistance : -checkDistance);
        float checkY = enemy.y + enemy.height + 5; // Ligeiramente abaixo dos pés
        
        boolean hasPlatformAhead = false;
        for (Platform platform : platforms) {
            if (checkX >= platform.x && checkX <= platform.x + platform.width &&
                checkY >= platform.y && checkY <= platform.y + platform.height) {
                hasPlatformAhead = true;
                break;
            }
        }
        
        // Se não há plataforma à frente, inverter direção
        if (!hasPlatformAhead && movement.isOnGround()) {
            enemy.setDirection(-enemy.getDirection());
            // Empurrar enemy de volta para a plataforma
            enemy.x += enemy.getDirection() * 5;
        }
    }
    
    /**
     * Verifica colisões entre player e enemies
     * @param player entidade do player
     * @param enemies lista de enemies
     * @return lista de enemies que foram derrotados
     */
    public static List<EnemyEntity> checkPlayerEnemyCollisions(PlayerEntity player, List<EnemyEntity> enemies) {
        List<EnemyEntity> defeatedEnemies = new ArrayList<>();
        CollisionComponent playerCollision = player.getComponent(CollisionComponent.class);
        
        if (playerCollision == null) return defeatedEnemies;
        
        for (EnemyEntity enemy : enemies) {
            CollisionComponent enemyCollision = enemy.getComponent(CollisionComponent.class);
            if (enemyCollision == null) continue;
            
            if (playerCollision.checkCollision(enemy)) {
                // Processar colisão (lógica será gerenciada pelos próprios componentes)
                playerCollision.processCollision(enemy);
                enemyCollision.processCollision(player);
            }
        }
        
        return defeatedEnemies;
    }
    
    /**
     * Verifica colisões entre player e orbs
     * @param player entidade do player
     * @param orbs lista de orbs
     * @return lista de orbs coletadas
     */
    public static List<Entity> checkPlayerOrbCollisions(PlayerEntity player, List<Entity> orbs) {
        List<Entity> collectedOrbs = new ArrayList<>();
        CollisionComponent playerCollision = player.getComponent(CollisionComponent.class);
        
        if (playerCollision == null) return collectedOrbs;
        
        for (Entity orb : orbs) {
            CollisionComponent orbCollision = orb.getComponent(CollisionComponent.class);
            if (orbCollision == null) continue;
            
            if (playerCollision.checkCollision(orb)) {
                // Processar colisão
                playerCollision.processCollision(orb);
                orbCollision.processCollision(player);
                collectedOrbs.add(orb);
            }
        }
        
        return collectedOrbs;
    }
    
    /**
     * Verifica se uma entidade está fora dos limites do mundo
     * @param entity entidade a verificar
     * @return true se está fora dos limites
     */
    public static boolean isOutOfWorldBounds(Entity entity) {
        return entity.y > GameConfig.WORLD_HEIGHT + 100;
    }
    
    /**
     * Aplica atrito a uma entidade quando no chão
     * @param entity entidade a receber atrito
     */
    public static void applyFriction(Entity entity) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null && movement.isOnGround()) {
            // O atrito já é aplicado pelo MovementComponent
        }
    }
    
    /**
     * Aplica impulso a uma entidade
     * @param entity entidade a receber impulso
     * @param impulseX impulso horizontal
     * @param impulseY impulso vertical
     */
    public static void applyImpulse(Entity entity, float impulseX, float impulseY) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null) {
            movement.applyImpulse(impulseX, impulseY);
        }
    }
    
    /**
     * Para o movimento de uma entidade
     * @param entity entidade a parar
     */
    public static void stopMovement(Entity entity) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null) {
            movement.stop();
        }
    }
    
    /**
     * Define se uma entidade pode se mover
     * @param entity entidade
     * @param canMove se pode se mover
     */
    public static void setCanMove(Entity entity, boolean canMove) {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null) {
            movement.setCanMove(canMove);
        }
    }
}
