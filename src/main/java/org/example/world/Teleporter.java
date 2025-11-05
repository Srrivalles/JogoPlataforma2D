package org.example.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Teleporter que transporta o player entre duas localizações
 */
public class Teleporter {
    private int x, y, width, height;
    private int targetX, targetY;
    private boolean isActive;
    private Rectangle area;
    private Color teleporterColor;
    private String teleporterId;
    
    // Efeitos visuais
    private ArrayList<TeleporterParticle> particles;
    private int animationTimer = 0;
    private float pulseIntensity = 0;
    private boolean isActivated = false;
    private int activationTimer = 0;
    
    public Teleporter(int x, int y, int width, int height, int targetX, int targetY, String id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targetX = targetX;
        this.targetY = targetY;
        this.isActive = true;
        this.area = new Rectangle(x, y, width, height);
        this.teleporterColor = new Color(255, 0, 255, 100); // Magenta
        this.teleporterId = id;
        this.particles = new ArrayList<>();
        
        // Criar partículas iniciais
        createTeleporterParticles();
    }
    
    /**
     * Verifica se o player pode usar o teleporter
     */
    public boolean canTeleport(org.example.objects.Player player) {
        if (!isActive || isActivated) return false;
        
        // Verificar se o player está na área do teleporter
        return area.intersects(player.getHitbox());
    }
    
    /**
     * Teleporta o player para a localização alvo
     */
    public void teleportPlayer(org.example.objects.Player player) {
        if (!canTeleport(player)) return;
        
        // Ativar teleporter
        isActivated = true;
        activationTimer = 60; // 1 segundo de cooldown
        
        // Teleportar player
        player.x = targetX;
        player.y = targetY;
        player.hitbox.setLocation(targetX, targetY);
        
        // Criar efeito de teleporte
        createTeleportEffect();
        
        System.out.println("Player teleportado de [" + x + ", " + y + "] para [" + targetX + ", " + targetY + "]");
    }
    
    /**
     * Atualiza o teleporter
     */
    public void update() {
        if (!isActive) return;
        
        // Atualizar animação
        animationTimer++;
        pulseIntensity = (float)(Math.sin(animationTimer * 0.15) * 0.4 + 0.6);
        
        // Atualizar timer de ativação
        if (isActivated) {
            activationTimer--;
            if (activationTimer <= 0) {
                isActivated = false;
            }
        }
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            TeleporterParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas
        if (animationTimer % 5 == 0) {
            createTeleporterParticle();
        }
    }
    
    /**
     * Desenha o teleporter
     */
    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        
        // Desenhar área do teleporter
        Color currentColor = new Color(teleporterColor.getRed(), teleporterColor.getGreen(), 
                                     teleporterColor.getBlue(), 
                                     (int)(teleporterColor.getAlpha() * pulseIntensity));
        g2d.setColor(currentColor);
        g2d.fillRect(x, y, width, height);
        
        // Desenhar borda
        g2d.setColor(new Color(255, 0, 255, 200));
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRect(x, y, width, height);
        
        // Desenhar padrão interno
        drawTeleporterPattern(g2d);
        
        // Desenhar partículas
        for (TeleporterParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Desenhar símbolo de teleporte
        drawTeleporterSymbol(g2d);
        
        // Efeito de ativação
        if (isActivated) {
            drawActivationEffect(g2d);
        }
    }
    
    private void createTeleporterParticles() {
        for (int i = 0; i < 20; i++) {
            createTeleporterParticle();
        }
    }
    
    private void createTeleporterParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new TeleporterParticle(particleX, particleY));
    }
    
    private void createTeleportEffect() {
        // Criar explosão de partículas
        for (int i = 0; i < 30; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 2 + (float)(Math.random() * 4);
            particles.add(new TeleporterParticle(
                x + width/2 + (int)(Math.cos(angle) * 20),
                y + height/2 + (int)(Math.sin(angle) * 20),
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            ));
        }
    }
    
    private void drawTeleporterPattern(Graphics2D g2d) {
        g2d.setColor(new Color(255, 0, 255, 150));
        g2d.setStroke(new java.awt.BasicStroke(2));
        
        // Desenhar linhas diagonais
        for (int i = 0; i < width; i += 20) {
            g2d.drawLine(x + i, y, x + i + 10, y + height);
        }
        for (int i = 0; i < height; i += 20) {
            g2d.drawLine(x, y + i, x + width, y + i + 10);
        }
    }
    
    private void drawTeleporterSymbol(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new java.awt.BasicStroke(3));
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        // Desenhar símbolo de teleporte (círculo com setas)
        g2d.drawOval(centerX - 15, centerY - 15, 30, 30);
        
        // Setas apontando para dentro
        g2d.drawLine(centerX - 20, centerY, centerX - 10, centerY);
        g2d.drawLine(centerX + 10, centerY, centerX + 20, centerY);
        g2d.drawLine(centerX, centerY - 20, centerX, centerY - 10);
        g2d.drawLine(centerX, centerY + 10, centerX, centerY + 20);
        
        // Pontas das setas
        g2d.drawLine(centerX - 10, centerY, centerX - 5, centerY - 3);
        g2d.drawLine(centerX - 10, centerY, centerX - 5, centerY + 3);
        g2d.drawLine(centerX + 10, centerY, centerX + 5, centerY - 3);
        g2d.drawLine(centerX + 10, centerY, centerX + 5, centerY + 3);
        g2d.drawLine(centerX, centerY - 10, centerX - 3, centerY - 5);
        g2d.drawLine(centerX, centerY - 10, centerX + 3, centerY - 5);
        g2d.drawLine(centerX, centerY + 10, centerX - 3, centerY + 5);
        g2d.drawLine(centerX, centerY + 10, centerX + 3, centerY + 5);
    }
    
    private void drawActivationEffect(Graphics2D g2d) {
        // Efeito de ativação (flash branco)
        float flashIntensity = (float)(activationTimer / 60.0f);
        g2d.setColor(new Color(255, 255, 255, (int)(100 * flashIntensity)));
        g2d.fillRect(x - 5, y - 5, width + 10, height + 10);
    }
    
    // Getters
    public Rectangle getArea() { return area; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public String getTeleporterId() { return teleporterId; }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }
    
    /**
     * Classe interna para partículas do teleporter
     */
    private static class TeleporterParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        
        public TeleporterParticle(int x, int y) {
            this(x, y, (float)(Math.random() * 2 - 1), (float)(Math.random() * 2 - 1));
        }
        
        public TeleporterParticle(int x, int y, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = 30 + (int)(Math.random() * 20);
            this.color = new Color(255, 100, 255, 150);
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Fade out
            int alpha = (int)(150 * (life / 50.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(255, 100, 255, alpha);
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int)x, (int)y, 4, 4);
        }
        
        public boolean isExpired() {
            return life <= 0;
        }
    }
}

