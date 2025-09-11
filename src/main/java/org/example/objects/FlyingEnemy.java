package org.example.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Inimigo voador com padrões de movimento específicos
 */
public class FlyingEnemy {
    private int x, y, width, height;
    private float velocityX, velocityY;
    private Rectangle hitbox;
    private boolean isActive;
    private Color enemyColor;
    
    // Padrões de movimento
    private MovementPattern pattern;
    private int patternTimer = 0;
    private float baseSpeed;
    private int patrolLeft, patrolRight, patrolTop, patrolBottom;
    
    // Efeitos visuais
    private ArrayList<FlyingParticle> particles;
    private int animationTimer = 0;
    private float wingFlap = 0;
    
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
        switch (pattern) {
            case HORIZONTAL_PATROL:
                patrolLeft = x - 100;
                patrolRight = x + 100;
                break;
            case VERTICAL_PATROL:
                patrolTop = y - 80;
                patrolBottom = y + 80;
                break;
            case CIRCULAR:
            case FIGURE_EIGHT:
                // Usar posição inicial como centro
                break;
            case HOVER:
                // Não precisa de limites específicos
                break;
            case DIVE_BOMB:
                patrolLeft = x - 150;
                patrolRight = x + 150;
                break;
        }
    }
    
    private void createFlyingParticles() {
        for (int i = 0; i < 5; i++) {
            createFlyingParticle();
        }
    }
    
    private void createFlyingParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new FlyingParticle(particleX, particleY, enemyColor));
    }
    
    private void createImpactEffect() {
        // Criar explosão de partículas quando player toca
        for (int i = 0; i < 10; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 2);
            particles.add(new FlyingParticle(
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
            
            // Fade out
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
