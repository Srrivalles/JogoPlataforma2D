package org.example.entities;

import org.example.inputs.CameraController;
import org.example.components.*;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Entidade do Player usando sistema de componentes
 * Substitui a classe Player original com arquitetura mais modular
 */
public class PlayerEntity extends Entity {
    
    // Componentes específicos do player
    private MovementComponent movement;
    private CollisionComponent collision;
    private HealthComponent health;
    private RenderComponent render;
    
    // Configurações específicas do player
    private float jumpStrength = (float) GameConfig.PLAYER_JUMP_STRENGTH;
    private float dashSpeed = (float) GameConfig.PLAYER_DASH_SPEED;
    private int dashEnergyCost = GameConfig.PLAYER_DASH_COST;
    private int maxEnergy = GameConfig.PLAYER_MAX_ENERGY;
    private int currentEnergy = maxEnergy;
    
    // Sistema de dash
    private boolean canDash = true;
    private boolean isDashing = false;
    private int dashCooldown = 0;
    private final int DASH_DURATION = 8;
    private final int DASH_COOLDOWN_TIME = 30;
    
    // Efeitos visuais
    private ArrayList<DashTrail> dashTrails = new ArrayList<>();
    private boolean showTeleportEffect = false;
    private int teleportEffectTimer = 0;
    
    // Sistema de sprites e animações
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;
    
    // Cores do personagem cyber
    private Color primaryColor = new Color(0, 200, 255);
    private Color accentColor = new Color(0, 255, 255);
    private Color bodyColor = new Color(70, 70, 80);
    
    public PlayerEntity(float startX, float startY) {
        super("sprites/player", "Cyber Runner");
        setPosition(startX, startY);
        setSize(32, 48);
        
        initializeComponents();
        setupCallbacks();
    }
    
    private void initializeComponents() {
        // Componente de movimento
        movement = new MovementComponent(this);
        movement.setMaxSpeed((float) GameConfig.PLAYER_SPEED);
        addComponent(movement);
        
        // Componente de colisão
        collision = new CollisionComponent(this);
        collision.setCollisionLayer("sprites/player");
        addComponent(collision);
        
        // Componente de saúde/vidas
        health = new HealthComponent(this, 100, 3); // 100 HP, 3 vidas
        addComponent(health);
        
        // Componente de renderização
        render = new RenderComponent(this, bodyColor, primaryColor);
        render.setBorderWidth(2);
        addComponent(render);
        
        // Inicializar sistema de sprites
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "player_idle";
    }

    public static void drawFuturisticHUD(Graphics2D g2d, PlayerEntity player, CameraController camera) {
        // Painel de informações do sistema
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(5, 5, 300, 180, 10, 10);

        g2d.setColor(new Color(0, 255, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(5, 5, 300, 180, 10, 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(0, 255, 200));
        g2d.drawString(">> CYBER RUNNER SYSTEM <<", 15, 25);

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(new Color(255, 255, 100));
        g2d.drawString("CONTROLS: WASD/ARROWS + SPACE + SHIFT/X", 15, 45);
        g2d.drawString("ESC: Return to Menu", 15, 60);

        g2d.setColor(new Color(255, 100, 200));
        g2d.drawString("Position: [" + (int)player.getX() + ", " + (int)player.getY() + "]", 15, 80);
        g2d.drawString("Camera: [" + camera.getCameraX() + ", " + camera.getCameraY() + "]", 15, 95);
        g2d.drawString("Ground Status: " + (player.isOnGround() ? "STABLE" : "AIRBORNE"), 15, 110);

        // Informações do player
        try {
            g2d.drawString("Mode: " + player.getCurrentMode().toUpperCase(), 15, 125);
            g2d.drawString("Direction: " + (player.isFacingRight() ? "RIGHT" : "LEFT"), 15, 140);
            g2d.drawString("Energy: " + player.getCurrentEnergy() + "%", 15, 155);
            g2d.drawString("Dash: " + (player.canDash() ? "READY" : "COOLDOWN"), 15, 170);
        } catch (Exception e) {
            g2d.drawString("Player Status: ACTIVE", 15, 125);
            g2d.drawString("Velocity: [" + (int)player.getVelocityX() + ", " + (int)player.getVelocityY() + "]", 15, 140);
        }
    }

    public static void drawLivesHUD(Graphics2D g2d, PlayerEntity player) {
        // Painel de vidas no canto superior esquerdo
        int panelX = 10;
        int panelY = 10;
        int panelWidth = 120;
        int panelHeight = 50;

        // Fundo do painel
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 8, 8);

        // Borda do painel
        g2d.setColor(new Color(0, 255, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 8, 8);

        // Título "LIVES"
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(0, 255, 200));
        g2d.drawString("LIVES", panelX + 8, panelY + 18);

        // Desenhar ícones de vida
        int iconSize = 20;
        int iconSpacing = 25;
        int startX = panelX + 8;
        int iconY = panelY + 25;

        for (int i = 0; i < 3; i++) {
            int iconX = startX + (i * iconSpacing);

            if (i < player.getLives()) {
                // Vida ativa - desenhar ícone do player
                drawPlayerIcon(g2d, iconX, iconY, iconSize, true);
            } else {
                // Vida perdida - desenhar ícone cinza
                drawPlayerIcon(g2d, iconX, iconY, iconSize, false);
            }
        }
    }
    
    private static void drawPlayerIcon(Graphics2D g2d, int x, int y, int size, boolean active) {
        // Desenhar ícone simples do player
        if (active) {
            g2d.setColor(new Color(0, 200, 255));
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillRect(x, y, size, size);
        
        // Borda
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, size, size);
    }
    private void setupCallbacks() {
        // Callback de colisão
        collision.setOnCollisionEnter((thisEntity, other, otherCollision) -> {
            handleCollision(other, otherCollision);
        });
        
        // Callback de dano
        health.setOnDamage((entity, healthComp, damage) -> {
            // Efeito visual de dano já é gerenciado pelo HealthComponent
<<<<<<< HEAD
=======
            System.out.println("Player tomou " + damage + " de dano!");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        });
        
        // Callback de morte
        health.setOnDeath((entity, healthComp, value) -> {
<<<<<<< HEAD
=======
            System.out.println("Player morreu!");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            // Lógica de game over será gerenciada pelo GamePanel
        });
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Atualizar animação
        updateAnimation();
        
        // Atualizar sistema de dash
        updateDash(deltaTime);
        
        // Atualizar energia
        updateEnergy(deltaTime);
        
        // Atualizar efeitos visuais
        updateVisualEffects(deltaTime);
    }
    
    /**
     * Atualiza a animação baseada no estado do player
     */
    private void updateAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        
        String newAnimation = "player_idle";
        
        if (isDashing) {
            newAnimation = "player_dash";
        } else if (!movement.isOnGround() && movement.getVelocityY() < -5) {
            newAnimation = "player_jump";
        } else if (Math.abs(movement.getVelocityX()) > 1) {
            newAnimation = "player_walk";
        }
        
        // Mudar animação apenas se for diferente da atual
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
            render.setCurrentAnimation(currentAnimation);
        }
    }

    public void updateCamera(float x, float y) {
        // Método para atualizar câmera com coordenadas float
        // Implementação será feita pelo CameraController
    }
    
    private void updateDash(float deltaTime) {
        if (isDashing) {
            // Lógica do dash
        }
        
        if (dashCooldown > 0) {
            dashCooldown--;
            if (dashCooldown <= 0) {
                canDash = true;
            }
        }
    }
    
    private void updateEnergy(float deltaTime) {
        // Regenerar energia quando no chão
        if (movement.isOnGround() && currentEnergy < maxEnergy) {
            currentEnergy += GameConfig.PLAYER_ENERGY_RECHARGE_RATE;
            if (currentEnergy > maxEnergy) {
                currentEnergy = maxEnergy;
            }
        }
    }
    
    private void updateVisualEffects(float deltaTime) {
        // Atualizar trails de dash
        dashTrails.removeIf(trail -> {
            trail.update();
            return trail.isFinished();
        });
        
        // Atualizar efeito de teleporte
        if (showTeleportEffect) {
            teleportEffectTimer--;
            if (teleportEffectTimer <= 0) {
                showTeleportEffect = false;
            }
        }
    }
    
    private void handleCollision(Entity other, CollisionComponent otherCollision) {
        // Lógica de colisão específica do player
        if (otherCollision.getCollisionLayer().equals("enemy")) {
            handleEnemyCollision(other);
        } else if (otherCollision.getCollisionLayer().equals("orb")) {
            handleOrbCollision(other);
        }
    }
    
    private void handleEnemyCollision(Entity enemy) {
        // Verificar se player está vindo de cima (pular em cima do inimigo)
        if (movement.getVelocityY() > 0 && y + height - 15 < enemy.y) {
            // Eliminar inimigo
            movement.applyImpulse(0, -jumpStrength);
<<<<<<< HEAD
=======
            // Adicionar pontos (será gerenciado pelo ScoreSystem)
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        } else if (movement.getVelocityY() <= 0) {
            // Player tocou inimigo lateralmente ou por baixo - perder vida
            // O HealthComponent já verifica invencibilidade internamente
            health.loseLife();
            
            // Efeito de knockback
            if (movement.isFacingRight()) {
                movement.applyImpulse(-8, -6);
            } else {
                movement.applyImpulse(8, -6);
            }
        }
        // Se velocityY > 0 mas não está acima do inimigo, não faz nada (evita dano desnecessário)
    }
    
    private void handleOrbCollision(Entity orb) {
        // Coletar orb (lógica será gerenciada pelo ScoreSystem)
<<<<<<< HEAD
=======
        System.out.println("Orb coletada!");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }
    
    // Métodos de controle do player
    public void moveLeft() {
        if (movement.isCanMove()) {
            movement.applyForceX(-movement.getMaxSpeed() * 10);
        }
    }
    
    public void moveRight() {
        if (movement.isCanMove()) {
            movement.applyForceX(movement.getMaxSpeed() * 10);
        }
    }
    
    public void jump() {
        if (movement.isOnGround()) {
            movement.applyImpulse(0, -jumpStrength);
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void dash() {
        if (canDash && currentEnergy >= dashEnergyCost && !isDashing) {
            isDashing = true;
            canDash = false;
            currentEnergy -= dashEnergyCost;
            dashCooldown = DASH_COOLDOWN_TIME;
<<<<<<< HEAD

            // ✅ SOM DO DASH
            org.example.audio.AudioManager.playDashSound();

            // Aplicar impulso de dash
            float dashDirection = movement.isFacingRight() ? 1 : -1;
            movement.applyImpulse(dashSpeed * dashDirection, 0);

            // Efeito visual
            showTeleportEffect = true;
            teleportEffectTimer = DASH_DURATION;

=======
            
            // Aplicar impulso de dash
            float dashDirection = movement.isFacingRight() ? 1 : -1;
            movement.applyImpulse(dashSpeed * dashDirection, 0);
            
            // Efeito visual
            showTeleportEffect = true;
            teleportEffectTimer = DASH_DURATION;
            
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            // Adicionar trail de dash
            dashTrails.add(new DashTrail(x, y, movement.isFacingRight()));
        }
    }
<<<<<<< HEAD


=======
    
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void stopMoving() {
        movement.stopHorizontal();
    }
    
    // Getters para compatibilidade com código existente
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Rectangle getHitbox() { return collision.getHitbox(); }
    public boolean isFacingRight() { return movement.isFacingRight(); }
    public boolean isOnGround() { return movement.isOnGround(); }
    public float getVelocityX() { return movement.getVelocityX(); }
    public float getVelocityY() { return movement.getVelocityY(); }
    public int getLives() { return health.getLives(); }
    public int getCurrentHealth() { return health.getCurrentHealth(); }
    public int getCurrentEnergy() { return currentEnergy; }
    public boolean canDash() { return canDash; }
    public boolean isDashing() { return isDashing; }
    public String getCurrentMode() {
        if (isDashing) return "dash";
        if (!movement.isOnGround() && movement.getVelocityY() < -10) return "boost";
        if (!movement.isOnGround() && movement.getVelocityY() > 0) return "gliding";
        if (Math.abs(movement.getVelocityX()) > 3) return "charging";
        return "normal";
    }
    
    // Métodos para renderização personalizada (compatibilidade)
    public void draw(Graphics2D g2d) {
        if (GameConfig.ANIMATIONS_ENABLED) {
            // Renderizar com sprite usando RenderComponent
            render.render(g2d);
        } else {
            // Renderização legada com formas geométricas
            render.render(g2d);
        }
        
        // Renderizar trails de dash
        for (DashTrail trail : dashTrails) {
            trail.render(g2d);
        }
    }
    
    // Classe interna para trails de dash
    private class DashTrail {
        private float x, y;
        private int life;
        private boolean facingRight;
        
        public DashTrail(float x, float y, boolean facingRight) {
            this.x = x;
            this.y = y;
            this.life = 20;
            this.facingRight = facingRight;
        }
        
        public void update() {
            life--;
            x += facingRight ? -2 : 2;
        }
        
        public void render(Graphics2D g2d) {
            if (life > 0) {
                float alpha = (float) life / 20.0f;
                g2d.setComposite(java.awt.AlphaComposite.getInstance(
                    java.awt.AlphaComposite.SRC_OVER, alpha));
                g2d.setColor(accentColor);
                g2d.fillRect((int)x, (int)y, 8, 8);
            }
        }
        
        public boolean isFinished() {
            return life <= 0;
        }
    }
}
