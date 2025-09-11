package org.example.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Espinhos que causam dano ao player
 */
public class Spike {
    private int x, y, width, height;
    private SpikeType type;
    private Rectangle hitbox;
    private boolean isActive;
    private Color spikeColor;
    
    // Efeitos visuais
    private ArrayList<SpikeParticle> particles;
    private int animationTimer = 0;
    private float glowIntensity = 0;
    
    public enum SpikeType {
        FLOOR,      // Espinhos no chão
        CEILING,    // Espinhos no teto
        WALL_LEFT,  // Espinhos na parede esquerda
        WALL_RIGHT  // Espinhos na parede direita
    }
    
    public Spike(int x, int y, int width, int height, SpikeType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.isActive = true;
        this.hitbox = new Rectangle(x, y, width, height);
        this.particles = new ArrayList<>();
        
        // Cor baseada no tipo
        switch (type) {
            case FLOOR:
                this.spikeColor = new Color(139, 0, 0, 200); // Vermelho escuro
                break;
            case CEILING:
                this.spikeColor = new Color(75, 0, 130, 200); // Índigo
                break;
            case WALL_LEFT:
            case WALL_RIGHT:
                this.spikeColor = new Color(128, 0, 128, 200); // Roxo
                break;
        }
        
        // Criar partículas iniciais
        createSpikeParticles();
    }
    
    /**
     * Verifica se o player colidiu com os espinhos
     */
    public boolean checkCollision(org.example.objects.Player player) {
        if (!isActive) return false;
        
        // Verificar colisão com hitbox do player
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
     * Atualiza os espinhos
     */
    public void update() {
        if (!isActive) return;
        
        // Atualizar animação
        animationTimer++;
        glowIntensity = (float)(Math.sin(animationTimer * 0.1) * 0.3 + 0.7);
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            SpikeParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas ocasionalmente
        if (animationTimer % 15 == 0) {
            createSpikeParticle();
        }
    }
    
    /**
     * Desenha os espinhos
     */
    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        
        // Desenhar base dos espinhos
        Color baseColor = new Color(spikeColor.getRed(), spikeColor.getGreen(), 
                                  spikeColor.getBlue(), (int)(spikeColor.getAlpha() * glowIntensity));
        g2d.setColor(baseColor);
        g2d.fillRect(x, y, width, height);
        
        // Desenhar espinhos baseado no tipo
        drawSpikes(g2d);
        
        // Desenhar borda
        g2d.setColor(new Color(spikeColor.getRed(), spikeColor.getGreen(), 
                              spikeColor.getBlue(), 255));
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRect(x, y, width, height);
        
        // Desenhar partículas
        for (SpikeParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Efeito de brilho
        drawGlowEffect(g2d);
    }
    
    private void drawSpikes(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new java.awt.BasicStroke(1));
        
        switch (type) {
            case FLOOR:
                drawFloorSpikes(g2d);
                break;
            case CEILING:
                drawCeilingSpikes(g2d);
                break;
            case WALL_LEFT:
                drawLeftWallSpikes(g2d);
                break;
            case WALL_RIGHT:
                drawRightWallSpikes(g2d);
                break;
        }
    }
    
    private void drawFloorSpikes(Graphics2D g2d) {
        // Espinhos apontando para cima
        int spikeCount = width / 8;
        for (int i = 0; i < spikeCount; i++) {
            int spikeX = x + (i * 8) + 4;
            int spikeY = y;
            int spikeHeight = 8 + (int)(Math.random() * 4);
            
            // Desenhar triângulo
            int[] xPoints = {spikeX, spikeX - 3, spikeX + 3};
            int[] yPoints = {spikeY + spikeHeight, spikeY, spikeY};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawCeilingSpikes(Graphics2D g2d) {
        // Espinhos apontando para baixo
        int spikeCount = width / 8;
        for (int i = 0; i < spikeCount; i++) {
            int spikeX = x + (i * 8) + 4;
            int spikeY = y + height;
            int spikeHeight = 8 + (int)(Math.random() * 4);
            
            // Desenhar triângulo
            int[] xPoints = {spikeX, spikeX - 3, spikeX + 3};
            int[] yPoints = {spikeY - spikeHeight, spikeY, spikeY};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawLeftWallSpikes(Graphics2D g2d) {
        // Espinhos apontando para a direita
        int spikeCount = height / 8;
        for (int i = 0; i < spikeCount; i++) {
            int spikeX = x + width;
            int spikeY = y + (i * 8) + 4;
            int spikeWidth = 8 + (int)(Math.random() * 4);
            
            // Desenhar triângulo
            int[] xPoints = {spikeX - spikeWidth, spikeX, spikeX};
            int[] yPoints = {spikeY, spikeY - 3, spikeY + 3};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawRightWallSpikes(Graphics2D g2d) {
        // Espinhos apontando para a esquerda
        int spikeCount = height / 8;
        for (int i = 0; i < spikeCount; i++) {
            int spikeX = x;
            int spikeY = y + (i * 8) + 4;
            int spikeWidth = 8 + (int)(Math.random() * 4);
            
            // Desenhar triângulo
            int[] xPoints = {spikeX + spikeWidth, spikeX, spikeX};
            int[] yPoints = {spikeY, spikeY - 3, spikeY + 3};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    private void drawGlowEffect(Graphics2D g2d) {
        // Efeito de brilho ao redor dos espinhos
        Color glowColor = new Color(spikeColor.getRed(), spikeColor.getGreen(), 
                                  spikeColor.getBlue(), (int)(50 * glowIntensity));
        g2d.setColor(glowColor);
        g2d.fillRect(x - 2, y - 2, width + 4, height + 4);
    }
    
    private void createSpikeParticles() {
        for (int i = 0; i < 8; i++) {
            createSpikeParticle();
        }
    }
    
    private void createSpikeParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new SpikeParticle(particleX, particleY, type));
    }
    
    private void createImpactEffect() {
        // Criar explosão de partículas quando player toca
        for (int i = 0; i < 15; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 1 + (float)(Math.random() * 3);
            particles.add(new SpikeParticle(
                x + width/2 + (int)(Math.cos(angle) * 10),
                y + height/2 + (int)(Math.sin(angle) * 10),
                type,
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            ));
        }
    }
    
    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public SpikeType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    /**
     * Classe interna para partículas dos espinhos
     */
    private static class SpikeParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        // private SpikeType spikeType; // Removido - não utilizado
        
        public SpikeParticle(int x, int y, SpikeType type) {
            this(x, y, type, (float)(Math.random() * 2 - 1), (float)(Math.random() * 2 - 1));
        }
        
        public SpikeParticle(int x, int y, SpikeType type, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            // this.spikeType = type; // Removido - não utilizado
            this.life = 20 + (int)(Math.random() * 15);
            
            // Cor baseada no tipo de espinho
            switch (type) {
                case FLOOR:
                    this.color = new Color(255, 100, 100, 150);
                    break;
                case CEILING:
                    this.color = new Color(150, 100, 255, 150);
                    break;
                case WALL_LEFT:
                case WALL_RIGHT:
                    this.color = new Color(255, 100, 255, 150);
                    break;
            }
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Fade out
            int alpha = (int)(150 * (life / 35.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int)x, (int)y, 2, 2);
        }
        
        public boolean isExpired() {
            return life <= 0;
        }
    }
}
