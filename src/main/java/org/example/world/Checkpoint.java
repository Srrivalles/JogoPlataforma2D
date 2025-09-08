package org.example.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Checkpoint que salva a posição do player
 */
public class Checkpoint {
    private int x, y, width, height;
    private boolean isActive;
    private boolean isActivated;
    private Rectangle area;
    private Color checkpointColor;
    private String checkpointId;
    
    // Efeitos visuais
    private ArrayList<CheckpointParticle> particles;
    private int animationTimer = 0;
    private float pulseIntensity = 0;
    private int activationTimer = 0;
    
    public Checkpoint(int x, int y, int width, int height, String id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isActive = true;
        this.isActivated = false;
        this.area = new Rectangle(x, y, width, height);
        this.checkpointColor = new Color(0, 255, 0, 100); // Verde
        this.checkpointId = id;
        this.particles = new ArrayList<>();
        
        // Criar partículas iniciais
        createCheckpointParticles();
    }
    
    /**
     * Verifica se o player pode ativar o checkpoint
     */
    public boolean canActivate(org.example.objects.Player player) {
        if (!isActive || isActivated) return false;
        
        // Verificar se o player está na área do checkpoint
        return area.intersects(player.getHitbox());
    }
    
    /**
     * Ativa o checkpoint
     */
    public void activate(org.example.objects.Player player) {
        if (!canActivate(player)) return;
        
        // Ativar checkpoint
        isActivated = true;
        activationTimer = 120; // 2 segundos de efeito
        
        // Criar efeito de ativação
        createActivationEffect();
        
        System.out.println("Checkpoint ativado: " + checkpointId + " em [" + x + ", " + y + "]");
    }
    
    /**
     * Atualiza o checkpoint
     */
    public void update() {
        if (!isActive) return;
        
        // Atualizar animação
        animationTimer++;
        pulseIntensity = (float)(Math.sin(animationTimer * 0.1) * 0.3 + 0.7);
        
        // Atualizar timer de ativação
        if (isActivated && activationTimer > 0) {
            activationTimer--;
        }
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            CheckpointParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas
        if (animationTimer % 8 == 0) {
            createCheckpointParticle();
        }
    }
    
    /**
     * Desenha o checkpoint
     */
    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        
        // Desenhar área do checkpoint
        Color currentColor = new Color(checkpointColor.getRed(), checkpointColor.getGreen(), 
                                     checkpointColor.getBlue(), 
                                     (int)(checkpointColor.getAlpha() * pulseIntensity));
        g2d.setColor(currentColor);
        g2d.fillRect(x, y, width, height);
        
        // Desenhar borda
        Color borderColor = isActivated ? new Color(0, 255, 0, 255) : new Color(0, 200, 0, 200);
        g2d.setColor(borderColor);
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRect(x, y, width, height);
        
        // Desenhar padrão interno
        drawCheckpointPattern(g2d);
        
        // Desenhar partículas
        for (CheckpointParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Desenhar símbolo de checkpoint
        drawCheckpointSymbol(g2d);
        
        // Efeito de ativação
        if (isActivated && activationTimer > 0) {
            drawActivationEffect(g2d);
        }
    }
    
    private void createCheckpointParticles() {
        for (int i = 0; i < 15; i++) {
            createCheckpointParticle();
        }
    }
    
    private void createCheckpointParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new CheckpointParticle(particleX, particleY));
    }
    
    private void createActivationEffect() {
        // Criar explosão de partículas verdes
        for (int i = 0; i < 25; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 3);
            particles.add(new CheckpointParticle(
                x + width/2 + (int)(Math.cos(angle) * 15),
                y + height/2 + (int)(Math.sin(angle) * 15),
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            ));
        }
    }
    
    private void drawCheckpointPattern(Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 0, 150));
        g2d.setStroke(new java.awt.BasicStroke(2));
        
        // Desenhar linhas de energia
        for (int i = 0; i < width; i += 15) {
            g2d.drawLine(x + i, y, x + i, y + height);
        }
        for (int i = 0; i < height; i += 15) {
            g2d.drawLine(x, y + i, x + width, y + i);
        }
    }
    
    private void drawCheckpointSymbol(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new java.awt.BasicStroke(3));
        
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        if (isActivated) {
            // Símbolo de checkpoint ativado (círculo com check)
            g2d.drawOval(centerX - 12, centerY - 12, 24, 24);
            g2d.drawLine(centerX - 6, centerY, centerX - 2, centerY + 4);
            g2d.drawLine(centerX - 2, centerY + 4, centerX + 6, centerY - 4);
        } else {
            // Símbolo de checkpoint inativo (círculo vazio)
            g2d.drawOval(centerX - 12, centerY - 12, 24, 24);
        }
    }
    
    private void drawActivationEffect(Graphics2D g2d) {
        // Efeito de ativação (flash verde)
        float flashIntensity = (float)(activationTimer / 120.0f);
        g2d.setColor(new Color(0, 255, 0, (int)(100 * flashIntensity)));
        g2d.fillRect(x - 10, y - 10, width + 20, height + 20);
        
        // Anel de energia
        g2d.setColor(new Color(0, 255, 0, (int)(150 * flashIntensity)));
        g2d.setStroke(new java.awt.BasicStroke(5));
        g2d.drawOval(x - 15, y - 15, width + 30, height + 30);
    }
    
    // Getters
    public Rectangle getArea() { return area; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public boolean isActivated() { return isActivated; }
    public String getCheckpointId() { return checkpointId; }
    public int getX() { return x; }
    public int getY() { return y; }
    
    /**
     * Classe interna para partículas do checkpoint
     */
    private static class CheckpointParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        
        public CheckpointParticle(int x, int y) {
            this(x, y, (float)(Math.random() * 1 - 0.5), (float)(Math.random() * 1 - 0.5));
        }
        
        public CheckpointParticle(int x, int y, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = 40 + (int)(Math.random() * 20);
            this.color = new Color(100, 255, 100, 120);
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Fade out
            int alpha = (int)(120 * (life / 60.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(100, 255, 100, alpha);
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
