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
<<<<<<< HEAD
 * Mantém toda a lógica original, apenas o visual legado foi alterado
 * para um esqueleto verde pixelado.
 */
public class EnemyEntity extends Entity {

    // Componentes
=======
 * Substitui a classe Enemy original com arquitetura mais modular
 */
public class EnemyEntity extends Entity {
    
    // Componentes específicos do enemy
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;
<<<<<<< HEAD

    // Configurações
    private float patrolLeft;
    private float patrolRight;
    private float patrolSpeed = (float) GameConfig.ENEMY_SPEED;
    private int direction = 1;

=======
    
    // Configurações específicas do enemy
    private float patrolLeft;
    private float patrolRight;
    private float patrolSpeed = (float) GameConfig.ENEMY_SPEED;
    private int direction = 1; // 1 = direita, -1 = esquerda
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    // Animação
    private int animationFrame = 0;
    private int eyeGlowIntensity = 0;
    private boolean glowIncreasing = true;
<<<<<<< HEAD

    // Sistema de sprites
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    // Cores do inimigo (esqueleto)
    private Color boneLight = new Color(160, 255, 180);
    private Color boneDark = new Color(100, 200, 120);
    private Color shadow = new Color(40, 70, 40);
    private Color eyeColor = new Color(150, 255, 150);

    public EnemyEntity(float startX, float startY) {
        super("enemy", "Skeleton Enemy");
        setPosition(startX, startY);
        setSize(30, 40);
        this.patrolLeft = startX - 50;
        this.patrolRight = startX + 50;
        initializeComponents();
        setupCallbacks();
    }

    public EnemyEntity(float startX, float startY, float patrolLeft, float patrolRight) {
        super("enemy", "Skeleton Enemy");
        setPosition(startX, startY);
        setSize(30, 40);
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        initializeComponents();
        setupCallbacks();
    }

    private void initializeComponents() {
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        movement = new MovementComponent(this);
        movement.setMaxSpeed(patrolSpeed);
        movement.setCanMove(true);
        addComponent(movement);
<<<<<<< HEAD

=======
        
        // Componente de colisão
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        collision = new CollisionComponent(this);
        collision.setCollisionLayer("enemy");
        collision.setSolid(true);
        addComponent(collision);
<<<<<<< HEAD

        health = new HealthComponent(this, 1, 0);
        addComponent(health);

        render = new RenderComponent(this, boneLight, Color.BLACK);
        render.setBorderWidth(1);
        addComponent(render);

=======
        
        // Componente de saúde (enemies têm 1 HP, sem vidas)
        health = new HealthComponent(this, 1, 0);
        addComponent(health);
        
        // Componente de renderização
        render = new RenderComponent(this, enemyColor, Color.BLACK);
        render.setBorderWidth(1);
        addComponent(render);
        
        // Inicializar sistema de sprites
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "enemy_idle";
    }
<<<<<<< HEAD

    private void setupCallbacks() {
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            handleCollision(other, otherCollision);
        });

        health.setOnDeath((entity, healthComp, value) -> {
            org.example.audio.AudioManager.playEnemyDownSound();
        });
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updatePatrol(deltaTime);
        updateAnimation(deltaTime);
        updateSpriteAnimation();
    }

    private void updatePatrol(float deltaTime) {
        movement.applyForceX(direction * patrolSpeed * 10);
        if (x <= patrolLeft) {
            direction = 1;
            x = patrolLeft;
            movement.stopHorizontal();
        } else if (x >= patrolRight) {
            direction = -1;
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            x = patrolRight;
            movement.stopHorizontal();
        }
    }
<<<<<<< HEAD

    private void updateAnimation(float deltaTime) {
        if (glowIncreasing) {
            eyeGlowIntensity += 2;
            if (eyeGlowIntensity >= 255) glowIncreasing = false;
        } else {
            eyeGlowIntensity -= 2;
            if (eyeGlowIntensity <= 100) glowIncreasing = true;
        }
        animationFrame = (animationFrame + 1) % 60;
    }

    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        String newAnimation = Math.abs(movement.getVelocityX()) > 0.1 ? "enemy_walk" : "enemy_idle";
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
            render.setCurrentAnimation(currentAnimation);
        }
    }
<<<<<<< HEAD

    private void handleCollision(Entity other, CollisionComponent otherCollision) {
        if (otherCollision.getCollisionLayer().equals("sprites/player")) {
            handlePlayerCollision(other);
        }
    }

    private void handlePlayerCollision(Entity player) {
        MovementComponent playerMovement = player.getComponent(MovementComponent.class);

        if (playerMovement != null && playerMovement.getVelocityY() > 0 &&
                player.y + player.height - 15 < y) {
            // Player matou o inimigo
            // ✅ SOM DE INIMIGO DERROTADO
            org.example.audio.AudioManager.playEnemyDownSound();

            health.takeDamage(1);
        } else if (playerMovement != null && playerMovement.getVelocityY() <= 0) {
            // Inimigo machucou o player
            HealthComponent playerHealth = player.getComponent(HealthComponent.class);
            if (playerHealth != null && !playerHealth.isInvulnerable()) {
                // ✅ SOM DE DANO AO PLAYER
                org.example.audio.AudioManager.playHurtSound();

                playerHealth.loseLife();

                // Knockback
                if (playerMovement.isFacingRight()) {
                    playerMovement.applyImpulse(-8, -6);
                } else {
                    playerMovement.applyImpulse(8, -6);
                }
            }
        }
    }

    // ==============================
    // VISUAL DO ESQUELETO (modo legado)
    // ==============================

    public void draw(Graphics2D g2d) {
    if (GameConfig.ANIMATIONS_ENABLED) {
        // Renderização com sprite (se houver animação ativada)
        render.render(g2d);
        return;
    }

    // Renderização detalhada estilo "cyber skeleton"
    int bodyX = (int) x;
    int bodyY = (int) y;
    int w = (int) width;
    int h = (int) height;

    // ===== CORPO =====
    // Gradiente metálico do corpo
    Color metalDark = new Color(100, 30, 30);
    Color metalLight = new Color(180, 80, 80);
    g2d.setPaint(new java.awt.GradientPaint(bodyX, bodyY, metalLight, bodyX, bodyY + h, metalDark));
    g2d.fillRoundRect(bodyX, bodyY, w, h, 8, 8);

    // ===== CABEÇA =====
    int headHeight = (int) (h * 0.35);
    g2d.setColor(new Color(120, 40, 40));
    g2d.fillRoundRect(bodyX + 3, bodyY - headHeight / 2, w - 6, headHeight, 6, 6);

    // ===== OLHOS BRILHANTES =====
    Color glowColor = new Color(eyeColor.getRed(), eyeColor.getGreen(), eyeColor.getBlue(), eyeGlowIntensity);
    g2d.setColor(glowColor);
    int eyeY = bodyY - headHeight / 4;
    g2d.fillOval(bodyX + 6, eyeY, 6, 6);
    g2d.fillOval(bodyX + w - 12, eyeY, 6, 6);

    // Brilho interno dos olhos
    g2d.setColor(new Color(255, 255, 255, Math.min(eyeGlowIntensity, 180)));
    g2d.fillOval(bodyX + 7, eyeY + 1, 3, 3);
    g2d.fillOval(bodyX + w - 11, eyeY + 1, 3, 3);

    // ===== COSTELAS =====
    g2d.setColor(new Color(90, 20, 20));
    for (int i = 0; i < 4; i++) {
        int ribY = bodyY + (i * 8) + 8;
        g2d.fillRect(bodyX + 5, ribY, w - 10, 3);
    }

    // ===== BRAÇOS =====
    g2d.setColor(new Color(100, 30, 30));
    g2d.fillRect(bodyX - 6, bodyY + 10, 5, 15); // braço esquerdo
    g2d.fillRect(bodyX + w + 1, bodyY + 10, 5, 15); // braço direito

    // ===== PERNAS =====
    g2d.setColor(new Color(80, 25, 25));
    g2d.fillRect(bodyX + 6, bodyY + h - 5, 5, 8); // perna esquerda
    g2d.fillRect(bodyX + w - 11, bodyY + h - 5, 5, 8); // perna direita

    // ===== DETALHES DE LUZ =====
    g2d.setColor(new Color(255, 60, 60, 90));
    g2d.drawLine(bodyX + 5, bodyY + h / 2, bodyX + w - 5, bodyY + h / 2); // faixa luminosa no meio

    // ===== BORDA =====
    g2d.setColor(new Color(40, 0, 0));
    g2d.drawRoundRect(bodyX, bodyY, w, h, 8, 8);
}


    private void drawEyes(Graphics2D g2d) {
        Color glowColor = new Color(eyeColor.getRed(), eyeColor.getGreen(),
                eyeColor.getBlue(), eyeGlowIntensity);
        g2d.setColor(glowColor);

        int drawX = (int) x;
        int drawY = (int) y;

        // Olhos (com leve oscilação)
        int offset = (int) (Math.sin(animationFrame * 0.3) * 1);
        g2d.fillRect(drawX + 12, drawY - 4 + offset, 2, 2);
        g2d.fillRect(drawX + 18, drawY - 4 + offset, 2, 2);
    }

    // Métodos compatíveis
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Rectangle getHitbox() { return collision.getHitbox(); }
    public int getDirection() { return direction; }
    public float getVelocityX() { return movement.getVelocityX(); }
    public float getVelocityY() { return movement.getVelocityY(); }
    public boolean isOnGround() { return movement.isOnGround(); }
<<<<<<< HEAD
    public void setDirection(int direction) { this.direction = direction; }
=======
    
    public void setDirection(int direction) { 
        this.direction = direction; 
    }
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void setPatrolBounds(float left, float right) {
        this.patrolLeft = left;
        this.patrolRight = right;
    }
<<<<<<< HEAD
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
}
