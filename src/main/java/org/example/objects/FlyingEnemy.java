package org.example.objects;

<<<<<<< HEAD
import org.example.components.*;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
<<<<<<< HEAD
 * Inimigo voador usando sistema de componentes
 * Com padrões de movimento e sistema de patrulha sobre plataformas
 */
public class FlyingEnemy extends Entity {

    // Componentes
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;

=======
 * Inimigo voador com padrões de movimento específicos
 */
public class FlyingEnemy {
    private int x, y, width, height;
    private float velocityX, velocityY;
    private Rectangle hitbox;
    private boolean isActive;
    private Color enemyColor;
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    // Padrões de movimento
    private MovementPattern pattern;
    private int patternTimer = 0;
    private float baseSpeed;
<<<<<<< HEAD
    private float patrolLeft, patrolRight, patrolTop, patrolBottom;

    // Sistema de patrulha sobre plataformas
    private int homePlatformIndex;
    private int patrolRange = 4;
    private boolean isTrackingPlayer = false;
    private float trackingDistance = 200f;

    // Direção do movimento
    private int direction = 1;

=======
    private int patrolLeft, patrolRight, patrolTop, patrolBottom;
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    // Efeitos visuais
    private ArrayList<FlyingParticle> particles;
    private int animationTimer = 0;
    private float wingFlap = 0;
<<<<<<< HEAD

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
=======
    
    public enum MovementPattern {
        HORIZONTAL_PATROL,    // Movimento horizontal simples
        VERTICAL_PATROL,      // Movimento vertical simples
        CIRCULAR,             // Movimento circular
        FIGURE_EIGHT,         // Movimento em forma de 8
        HOVER,                // Fica parado, mas se move quando player se aproxima
        DIVE_BOMB             // Mergulha em direção ao player
    }
    
    public FlyingEnemy(int x, int y, int width, int height, MovementPattern pattern, float speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.pattern = pattern;
        this.baseSpeed = speed;
        this.isActive = true;
        this.hitbox = new Rectangle(x, y, width, height);
        this.particles = new ArrayList<>();
        
        // Cor baseada no padrão de movimento
        switch (pattern) {
            case HORIZONTAL_PATROL:
                this.enemyColor = new Color(100, 50, 150, 200); // Roxo escuro
                break;
            case VERTICAL_PATROL:
                this.enemyColor = new Color(150, 50, 100, 200); // Rosa escuro
                break;
            case CIRCULAR:
                this.enemyColor = new Color(50, 100, 150, 200); // Azul escuro
                break;
            case FIGURE_EIGHT:
                this.enemyColor = new Color(150, 150, 50, 200); // Amarelo escuro
                break;
            case HOVER:
                this.enemyColor = new Color(100, 150, 50, 200); // Verde escuro
                break;
            case DIVE_BOMB:
                this.enemyColor = new Color(150, 50, 50, 200); // Vermelho escuro
                break;
        }
        
        // Configurar limites de patrulha baseado no padrão
        setupPatrolBounds();
        
        // Criar partículas iniciais
        createFlyingParticles();
    }
    
    /**
     * Atualiza o inimigo voador
     */
    public void update(org.example.objects.Player player) {
        if (!isActive) return;
        
        // Atualizar animação
        animationTimer++;
        wingFlap = (float)(Math.sin(animationTimer * 0.3) * 0.5 + 0.5);
        
        // Atualizar padrão de movimento
        updateMovementPattern(player);
        
        // Atualizar posição
        x += velocityX;
        y += velocityY;
        hitbox.setLocation(x, y);
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            FlyingParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas
        if (animationTimer % 8 == 0) {
            createFlyingParticle();
        }
        
        patternTimer++;
    }
    
    /**
     * Atualiza o padrão de movimento baseado no tipo
     */
    private void updateMovementPattern(org.example.objects.Player player) {
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
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
=======
                updateHoverMovement(player);
                break;
            case DIVE_BOMB:
                updateDiveBombMovement(player);
                break;
        }
    }
    
    private void updateHorizontalPatrol() {
        // Movimento horizontal simples
        if (x <= patrolLeft) {
            velocityX = baseSpeed;
        } else if (x >= patrolRight) {
            velocityX = -baseSpeed;
        }
        velocityY = 0;
    }
    
    private void updateVerticalPatrol() {
        // Movimento vertical simples
        if (y <= patrolTop) {
            velocityY = baseSpeed;
        } else if (y >= patrolBottom) {
            velocityY = -baseSpeed;
        }
        velocityX = 0;
    }
    
    private void updateCircularMovement() {
        // Movimento circular
        float angle = (float)(patternTimer * 0.05);
        velocityX = (float)(Math.cos(angle) * baseSpeed);
        velocityY = (float)(Math.sin(angle) * baseSpeed);
    }
    
    private void updateFigureEightMovement() {
        // Movimento em forma de 8
        float angle = (float)(patternTimer * 0.03);
        velocityX = (float)(Math.cos(angle) * baseSpeed);
        velocityY = (float)(Math.sin(angle * 2) * baseSpeed * 0.5f);
    }
    
    private void updateHoverMovement(org.example.objects.Player player) {
        // Fica parado, mas se move quando player se aproxima
        float distanceToPlayer = (float)Math.sqrt(
            Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2)
        );
        
        if (distanceToPlayer < 150) { // Player próximo
            // Mover para longe do player
            float angle = (float)Math.atan2(y - player.y, x - player.x);
            velocityX = (float)(Math.cos(angle) * baseSpeed * 0.5f);
            velocityY = (float)(Math.sin(angle) * baseSpeed * 0.5f);
        } else {
            // Movimento suave de hover
            velocityX = (float)(Math.sin(patternTimer * 0.02) * baseSpeed * 0.3f);
            velocityY = (float)(Math.cos(patternTimer * 0.02) * baseSpeed * 0.3f);
        }
    }
    
    private void updateDiveBombMovement(org.example.objects.Player player) {
        // Mergulha em direção ao player
        float distanceToPlayer = (float)Math.sqrt(
            Math.pow(x - player.x, 2) + Math.pow(y - player.y, 2)
        );
        
        if (distanceToPlayer < 200) { // Player no alcance
            // Mover em direção ao player
            float angle = (float)Math.atan2(player.y - y, player.x - x);
            velocityX = (float)(Math.cos(angle) * baseSpeed * 1.5f);
            velocityY = (float)(Math.sin(angle) * baseSpeed * 1.5f);
        } else {
            // Patrulha normal
            updateHorizontalPatrol();
        }
    }
    
    /**
     * Verifica colisão com o player
     */
    public boolean checkCollision(org.example.objects.Player player) {
        if (!isActive) return false;
        
        if (hitbox.intersects(player.getHitbox())) {
            // Aplicar dano ao player
            // player.takeDamage(1, null);
            // Criar efeito de impacto
            createImpactEffect();
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Desenha o inimigo voador
     */
    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        
        // Desenhar corpo principal
        g2d.setColor(enemyColor);
        g2d.fillOval(x, y, width, height);
        
        // Desenhar asas com animação
        drawWings(g2d);
        
        // Desenhar olhos
        drawEyes(g2d);
        
        // Desenhar partículas
        for (FlyingParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Efeito de brilho
        drawGlowEffect(g2d);
    }
    
    private void drawWings(Graphics2D g2d) {
        // Asas com animação de batida
        g2d.setColor(new Color(enemyColor.getRed(), enemyColor.getGreen(), 
                              enemyColor.getBlue(), 150));
        
        // Asa esquerda
        int wingOffset = (int)(wingFlap * 3);
        g2d.fillOval(x - 8, y + 2 + wingOffset, 12, 8);
        
        // Asa direita
        g2d.fillOval(x + width - 4, y + 2 + wingOffset, 12, 8);
    }
    
    private void drawEyes(Graphics2D g2d) {
        // Olhos brilhantes
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(x + 3, y + 3, 4, 4);
        g2d.fillOval(x + width - 7, y + 3, 4, 4);
        
        // Pupilas
        g2d.setColor(new Color(0, 0, 0, 255));
        g2d.fillOval(x + 4, y + 4, 2, 2);
        g2d.fillOval(x + width - 6, y + 4, 2, 2);
    }
    
    private void drawGlowEffect(Graphics2D g2d) {
        // Efeito de brilho ao redor do inimigo
        Color glowColor = new Color(enemyColor.getRed(), enemyColor.getGreen(), 
                                  enemyColor.getBlue(), 50);
        g2d.setColor(glowColor);
        g2d.fillOval(x - 3, y - 3, width + 6, height + 6);
    }
    
    private void setupPatrolBounds() {
        // Configurar limites baseado no padrão
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        switch (pattern) {
            case HORIZONTAL_PATROL:
                patrolLeft = x - 100;
                patrolRight = x + 100;
                break;
            case VERTICAL_PATROL:
                patrolTop = y - 80;
                patrolBottom = y + 80;
                break;
<<<<<<< HEAD
=======
            case CIRCULAR:
            case FIGURE_EIGHT:
                // Usar posição inicial como centro
                break;
            case HOVER:
                // Não precisa de limites específicos
                break;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            case DIVE_BOMB:
                patrolLeft = x - 150;
                patrolRight = x + 150;
                break;
<<<<<<< HEAD
            default:
                break;
        }
    }

=======
        }
    }
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private void createFlyingParticles() {
        for (int i = 0; i < 5; i++) {
            createFlyingParticle();
        }
    }
<<<<<<< HEAD

    private void createFlyingParticle() {
        int particleX = (int)(x + Math.random() * width);
        int particleY = (int)(y + Math.random() * height);
        particles.add(new FlyingParticle(particleX, particleY, enemyColor));
    }

    private void createImpactEffect() {
=======
    
    private void createFlyingParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new FlyingParticle(particleX, particleY, enemyColor));
    }
    
    private void createImpactEffect() {
        // Criar explosão de partículas quando player toca
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        for (int i = 0; i < 10; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 2);
            particles.add(new FlyingParticle(
<<<<<<< HEAD
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
    

=======
                x + width/2 + (int)(Math.cos(angle) * 8),
                y + height/2 + (int)(Math.sin(angle) * 8),
                enemyColor,
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            ));
        }
    }
    
    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public MovementPattern getPattern() { return pattern; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    /**
     * Classe interna para partículas do inimigo voador
     */
    private static class FlyingParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
<<<<<<< HEAD

        public FlyingParticle(int x, int y, Color baseColor) {
            this(x, y, baseColor, (float)(Math.random() * 1 - 0.5), (float)(Math.random() * 1 - 0.5));
        }

=======
        
        public FlyingParticle(int x, int y, Color baseColor) {
            this(x, y, baseColor, (float)(Math.random() * 1 - 0.5), (float)(Math.random() * 1 - 0.5));
        }
        
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        public FlyingParticle(int x, int y, Color baseColor, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = 25 + (int)(Math.random() * 15);
<<<<<<< HEAD
            this.color = new Color(baseColor.getRed(), baseColor.getGreen(),
                    baseColor.getBlue(), 120);
        }

=======
            this.color = new Color(baseColor.getRed(), baseColor.getGreen(), 
                                 baseColor.getBlue(), 120);
        }
        
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
<<<<<<< HEAD

=======
            
            // Fade out
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            int alpha = (int)(120 * (life / 40.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }
<<<<<<< HEAD

=======
        
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int)x, (int)y, 3, 3);
        }
<<<<<<< HEAD

=======
        
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        public boolean isExpired() {
            return life <= 0;
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
