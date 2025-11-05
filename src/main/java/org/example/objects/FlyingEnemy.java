package org.example.objects;

import org.example.components.*;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Inimigo voador usando sistema de componentes
 * Com padrões de movimento e sistema de patrulha sobre plataformas
 */
public class FlyingEnemy extends Entity {

    // Componentes
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;

    // Padrões de movimento
    private MovementPattern pattern;
    private int patternTimer = 0;
    private float baseSpeed;
    private float patrolLeft, patrolRight, patrolTop, patrolBottom;

    // Sistema de patrulha sobre plataformas
    private int homePlatformIndex;
    private int patrolRange = 4;
    private boolean isTrackingPlayer = false;
    private float trackingDistance = 200f;

    // Direção do movimento
    private int direction = 1;

    // Efeitos visuais
    private ArrayList<FlyingParticle> particles;
    private int animationTimer = 0;
    private float wingFlap = 0;

    // Sistema de sprites
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    // Cor do inimigo
    private Color enemyColor;

    // Referência ao player para perseguição
    private Entity targetPlayer;

    public enum MovementPattern {
        HORIZONTAL_PATROL,
        VERTICAL_PATROL,
        CIRCULAR,
        FIGURE_EIGHT,
        HOVER,
        DIVE_BOMB
    }

    public FlyingEnemy(float startX, float startY, MovementPattern pattern, float speed, int homePlatformIndex) {
        super("flying_enemy", "Flying Enemy");
        setPosition(startX, startY);
        setSize(20, 20);
        this.pattern = pattern;
        this.baseSpeed = speed;
        this.homePlatformIndex = homePlatformIndex;
        this.particles = new ArrayList<>();

        // Cor baseada no padrão de movimento
        this.enemyColor = getColorForPattern(pattern);

        initializeComponents();
        setupCallbacks();
        setupPatrolBounds();
        createFlyingParticles();
    }

    private Color getColorForPattern(MovementPattern pattern) {
        switch (pattern) {
            case HORIZONTAL_PATROL: return new Color(100, 50, 150, 200);
            case VERTICAL_PATROL: return new Color(150, 50, 100, 200);
            case CIRCULAR: return new Color(50, 100, 150, 200);
            case FIGURE_EIGHT: return new Color(150, 150, 50, 200);
            case HOVER: return new Color(100, 150, 50, 200);
            case DIVE_BOMB: return new Color(150, 50, 50, 200);
            default: return new Color(100, 50, 150, 200);
        }
    }

    private void initializeComponents() {
        movement = new MovementComponent(this);
        movement.setMaxSpeed(baseSpeed * 3);
        movement.setCanMove(true);
        addComponent(movement);

        collision = new CollisionComponent(this);
        collision.setCollisionLayer("flying_enemy");
        collision.setSolid(false);
        addComponent(collision);

        health = new HealthComponent(this, 1, 0);
        addComponent(health);

        render = new RenderComponent(this, enemyColor, Color.BLACK);
        render.setBorderWidth(1);
        addComponent(render);

        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "flying_enemy_idle";
    }

    private void setupCallbacks() {
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            handleCollision(other, otherCollision);
        });

        health.setOnDeath((entity, healthComp, value) -> {
            createEliminationEffect();
        });
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Atualizar animação
        animationTimer++;
        wingFlap = (float)(Math.sin(animationTimer * 0.3) * 0.5 + 0.5);

        // Verificar tracking do player
        if (targetPlayer != null) {
            checkPlayerTracking(targetPlayer);
        }

        updateMovement(deltaTime);
        updateParticles();
        updateSpriteAnimation();

        patternTimer++;
    }

    /**
     * Define o player alvo para perseguição
     */
    public void setTargetPlayer(Entity player) {
        this.targetPlayer = player;
    }

    /**
     * Verifica se deve começar a seguir o player
     */
    private void checkPlayerTracking(Entity player) {
        float distanceToPlayer = (float)Math.sqrt(
                Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2)
        );

        if (distanceToPlayer < trackingDistance && isPlayerInPatrolRange(player)) {
            isTrackingPlayer = true;
        } else if (distanceToPlayer > trackingDistance * 1.5f) {
            isTrackingPlayer = false;
        }
    }

    /**
     * Verifica se o player está dentro do alcance de patrulha
     */
    private boolean isPlayerInPatrolRange(Entity player) {
        int playerPlatformIndex = findNearestPlatformIndex(player.x);
        int distance = Math.abs(playerPlatformIndex - homePlatformIndex);
        return distance <= patrolRange;
    }

    /**
     * Encontra o índice da plataforma mais próxima
     */
    private int findNearestPlatformIndex(float x) {
        return homePlatformIndex + (int)((x - this.x) / 200);
    }

    private void updateMovement(float deltaTime) {
        if (isTrackingPlayer && targetPlayer != null) {
            updatePlayerTrackingMovement(targetPlayer);
        } else {
            updatePatrolMovement();
        }
    }

    /**
     * Movimento de perseguição ao player
     */
    private void updatePlayerTrackingMovement(Entity player) {
        if (!isPlayerInPatrolRange(player)) {
            isTrackingPlayer = false;
            return;
        }

        float dx = player.x - x;
        float dy = player.y - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance > 10) {
            float forceX = (dx / distance) * baseSpeed * 15f;
            float forceY = (dy / distance) * baseSpeed * 6f;

            movement.applyForceX(forceX);
            movement.applyForceY(forceY);

            // Manter altura mínima
            if (y > player.y - 30) {
                movement.applyForceY(-baseSpeed * 8f);
            }
        } else {
            // Movimento circular ao redor do player
            float angle = (float)(patternTimer * 0.05);
            movement.applyForceX((float)(Math.cos(angle) * baseSpeed * 10));
            movement.applyForceY((float)(Math.sin(angle) * baseSpeed * 3));
        }
    }

    /**
     * Atualiza movimento baseado no padrão
     */
    private void updatePatrolMovement() {
        switch (pattern) {
            case HORIZONTAL_PATROL:
                updateHorizontalPatrol();
                break;
            case VERTICAL_PATROL:
                updateVerticalPatrol();
                break;
            case CIRCULAR:
                updateCircularMovement();
                break;
            case FIGURE_EIGHT:
                updateFigureEightMovement();
                break;
            case HOVER:
                updateHoverMovement();
                break;
            case DIVE_BOMB:
                updateDiveBombMovement();
                break;
        }
    }

    private void updateHorizontalPatrol() {
        movement.applyForceX(direction * baseSpeed * 10);

        if (x <= patrolLeft) {
            direction = 1;
            x = patrolLeft;
            movement.stopHorizontal();
        } else if (x >= patrolRight) {
            direction = -1;
            x = patrolRight;
            movement.stopHorizontal();
        }

        // Movimento vertical suave
        movement.applyForceY((float)(Math.sin(patternTimer * 0.02) * baseSpeed * 2));
    }

    private void updateVerticalPatrol() {
        movement.applyForceY(direction * baseSpeed * 10);

        if (y <= patrolTop) {
            direction = 1;
            y = patrolTop;
            movement.stopVertical();
        } else if (y >= patrolBottom) {
            direction = -1;
            y = patrolBottom;
            movement.stopVertical();
        }

        // Movimento horizontal suave
        movement.applyForceX((float)(Math.cos(patternTimer * 0.02) * baseSpeed * 2));
    }

    private void updateCircularMovement() {
        float angle = (float)(patternTimer * 0.05);
        movement.applyForceX((float)(Math.cos(angle) * baseSpeed * 10));
        movement.applyForceY((float)(Math.sin(angle) * baseSpeed * 10));
    }

    private void updateFigureEightMovement() {
        float angle = (float)(patternTimer * 0.03);
        movement.applyForceX((float)(Math.cos(angle) * baseSpeed * 10));
        movement.applyForceY((float)(Math.sin(angle * 2) * baseSpeed * 5));
    }

    private void updateHoverMovement() {
        if (targetPlayer != null) {
            float distanceToPlayer = (float)Math.sqrt(
                    Math.pow(x - targetPlayer.x, 2) + Math.pow(y - targetPlayer.y, 2)
            );

            if (distanceToPlayer < 150) {
                // Fugir do player
                float angle = (float)Math.atan2(y - targetPlayer.y, x - targetPlayer.x);
                movement.applyForceX((float)(Math.cos(angle) * baseSpeed * 5));
                movement.applyForceY((float)(Math.sin(angle) * baseSpeed * 5));
            } else {
                // Hover suave
                movement.applyForceX((float)(Math.sin(patternTimer * 0.02) * baseSpeed * 3));
                movement.applyForceY((float)(Math.cos(patternTimer * 0.02) * baseSpeed * 3));
            }
        } else {
            movement.applyForceX((float)(Math.sin(patternTimer * 0.02) * baseSpeed * 3));
            movement.applyForceY((float)(Math.cos(patternTimer * 0.02) * baseSpeed * 3));
        }
    }

    private void updateDiveBombMovement() {
        if (targetPlayer != null) {
            float distanceToPlayer = (float)Math.sqrt(
                    Math.pow(x - targetPlayer.x, 2) + Math.pow(y - targetPlayer.y, 2)
            );

            if (distanceToPlayer < 200) {
                // Mergulhar em direção ao player
                float angle = (float)Math.atan2(targetPlayer.y - y, targetPlayer.x - x);
                movement.applyForceX((float)(Math.cos(angle) * baseSpeed * 15));
                movement.applyForceY((float)(Math.sin(angle) * baseSpeed * 15));
            } else {
                updateHorizontalPatrol();
            }
        } else {
            updateHorizontalPatrol();
        }
    }

    private void updateParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            FlyingParticle particle = particles.get(i);
            particle.update();

            if (particle.isExpired()) {
                particles.remove(i);
            }
        }

        if (animationTimer % 8 == 0) {
            createFlyingParticle();
        }
    }

    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;

        String newAnimation = "flying_enemy_fly";
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
            render.setCurrentAnimation(currentAnimation);
        }
    }

    private void handleCollision(Entity other, CollisionComponent otherCollision) {
        if (otherCollision.getCollisionLayer().equals("player")) {
            handlePlayerCollision(other);
        }
    }

    private void handlePlayerCollision(Entity player) {
        MovementComponent playerMovement = player.getComponent(MovementComponent.class);
        if (playerMovement != null && playerMovement.getVelocityY() > 0 &&
                player.y + player.height - 10 < y) {
            health.takeDamage(1);
            if (!health.isAlive()) {
                org.example.audio.AudioManager.playEnemyDownSound();
            }
            // Dar impulso ao player (aplicar força negativa em Y)
            playerMovement.applyForceY(-400);
            createImpactEffect();
        }
    }

    private void setupPatrolBounds() {
        switch (pattern) {
            case HORIZONTAL_PATROL:
                patrolLeft = x - 100;
                patrolRight = x + 100;
                break;
            case VERTICAL_PATROL:
                patrolTop = y - 80;
                patrolBottom = y + 80;
                break;
            case DIVE_BOMB:
                patrolLeft = x - 150;
                patrolRight = x + 150;
                break;
            default:
                break;
        }
    }

    private void createFlyingParticles() {
        for (int i = 0; i < 5; i++) {
            createFlyingParticle();
        }
    }

    private void createFlyingParticle() {
        int particleX = (int)(x + Math.random() * width);
        int particleY = (int)(y + Math.random() * height);
        particles.add(new FlyingParticle(particleX, particleY, enemyColor));
    }

    private void createImpactEffect() {
        for (int i = 0; i < 10; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 2);
            particles.add(new FlyingParticle(
                    (int)(x + width/2 + Math.cos(angle) * 8),
                    (int)(y + height/2 + Math.sin(angle) * 8),
                    enemyColor,
                    (float)Math.cos(angle) * speed,
                    (float)Math.sin(angle) * speed
            ));
        }
    }

    private void createEliminationEffect() {
        for (int i = 0; i < 15; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 2 + (float)(Math.random() * 3);
            particles.add(new FlyingParticle(
                    (int)(x + width/2 + Math.cos(angle) * 10),
                    (int)(y + height/2 + Math.sin(angle) * 10),
                    enemyColor,
                    (float)Math.cos(angle) * speed,
                    (float)Math.sin(angle) * speed
            ));
        }
    }

    // ==============================
    // VISUAL DO INIMIGO VOADOR
    // ==============================

    public void draw(Graphics2D g2d) {
        if (!health.isAlive()) {
            // Desenhar apenas partículas de explosão
            for (FlyingParticle particle : particles) {
                particle.draw(g2d);
            }
            return;
        }

        if (GameConfig.ANIMATIONS_ENABLED) {
            render.render(g2d);
            drawParticles(g2d);
            return;
        }

        // Renderização legada
        int drawX = (int) x;
        int drawY = (int) y;
        int w = (int) width;
        int h = (int) height;

        // Efeito de brilho (desenhar primeiro, atrás)
        drawGlowEffect(g2d, drawX, drawY, w, h);

        // Desenhar corpo principal
        g2d.setColor(enemyColor);
        g2d.fillOval(drawX, drawY, w, h);

        // Desenhar asas
        drawWings(g2d, drawX, drawY, w, h);

        // Desenhar olhos
        drawEyes(g2d, drawX, drawY, w, h);

        // Desenhar partículas
        drawParticles(g2d);
    }

    private void drawWings(Graphics2D g2d, int drawX, int drawY, int w, int h) {
        g2d.setColor(new Color(enemyColor.getRed(), enemyColor.getGreen(),
                enemyColor.getBlue(), 150));

        int wingOffset = (int)(wingFlap * 3);
        g2d.fillOval(drawX - 8, drawY + 2 + wingOffset, 12, 8);
        g2d.fillOval(drawX + w - 4, drawY + 2 + wingOffset, 12, 8);
    }

    private void drawEyes(Graphics2D g2d, int drawX, int drawY, int w, int h) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(drawX + 3, drawY + 3, 4, 4);
        g2d.fillOval(drawX + w - 7, drawY + 3, 4, 4);

        g2d.setColor(new Color(0, 0, 0, 255));
        g2d.fillOval(drawX + 4, drawY + 4, 2, 2);
        g2d.fillOval(drawX + w - 6, drawY + 4, 2, 2);
    }

    private void drawGlowEffect(Graphics2D g2d, int drawX, int drawY, int w, int h) {
        Color glowColor = new Color(enemyColor.getRed(), enemyColor.getGreen(),
                enemyColor.getBlue(), 50);
        g2d.setColor(glowColor);
        g2d.fillOval(drawX - 3, drawY - 3, w + 6, h + 6);
    }

    private void drawParticles(Graphics2D g2d) {
        for (FlyingParticle particle : particles) {
            particle.draw(g2d);
        }
    }

    // Getters
    public Rectangle getHitbox() { return collision.getHitbox(); }
    public MovementPattern getPattern() { return pattern; }
    public int getHomePlatformIndex() { return homePlatformIndex; }
    public boolean isTrackingPlayer() { return isTrackingPlayer; }
    public float getVelocityX() { return movement.getVelocityX(); }
    public float getVelocityY() { return movement.getVelocityY(); }
    public int getDirection() { return direction; }
    

    /**
     * Classe interna para partículas do inimigo voador
     */
    private static class FlyingParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;

        public FlyingParticle(int x, int y, Color baseColor) {
            this(x, y, baseColor, (float)(Math.random() * 1 - 0.5), (float)(Math.random() * 1 - 0.5));
        }

        public FlyingParticle(int x, int y, Color baseColor, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = 25 + (int)(Math.random() * 15);
            this.color = new Color(baseColor.getRed(), baseColor.getGreen(),
                    baseColor.getBlue(), 120);
        }

        public void update() {
            x += velocityX;
            y += velocityY;
            life--;

            int alpha = (int)(120 * (life / 40.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int)x, (int)y, 3, 3);
        }

        public boolean isExpired() {
            return life <= 0;
        }
    }
}