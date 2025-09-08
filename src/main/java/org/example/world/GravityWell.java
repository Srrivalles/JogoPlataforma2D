package org.example.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Poço de gravidade que altera a gravidade do player
 */
public class GravityWell {
    private int x, y, radius;
    private float gravityMultiplier;
    private boolean isActive;
    private Rectangle area;
    private Color wellColor;
    
    // Efeitos visuais
    private ArrayList<GravityParticle> particles;
    private int animationTimer = 0;
    private float pulseIntensity = 0;
    
    public GravityWell(int x, int y, int radius, float gravityMultiplier) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.gravityMultiplier = gravityMultiplier;
        this.isActive = true;
        this.area = new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
        this.particles = new ArrayList<>();
        
        // Definir cor baseada no tipo de gravidade
        if (gravityMultiplier > 1.0f) {
            // Gravidade aumentada (vermelho)
            this.wellColor = new Color(255, 100, 100, 80);
        } else if (gravityMultiplier < 1.0f) {
            // Gravidade reduzida (azul)
            this.wellColor = new Color(100, 100, 255, 80);
        } else {
            // Gravidade normal (verde)
            this.wellColor = new Color(100, 255, 100, 80);
        }
        
        // Criar partículas iniciais
        createGravityParticles();
    }
    
    /**
     * Aplica o efeito de gravidade no player
     */
    public void applyGravityEffect(org.example.objects.Player player) {
        if (!isActive) return;
        
        // Verificar se o player está na área do poço
        if (area.intersects(player.getHitbox())) {
            // Calcular distância do centro
            float centerX = x + radius;
            float centerY = y + radius;
            float playerCenterX = player.x + player.width / 2;
            float playerCenterY = player.y + player.height / 2;
            
            float distance = (float)Math.sqrt(
                Math.pow(playerCenterX - centerX, 2) + 
                Math.pow(playerCenterY - centerY, 2)
            );
            
            // Aplicar efeito baseado na distância (mais forte no centro)
            float intensity = 1.0f - (distance / radius);
            if (intensity < 0) intensity = 0;
            
            // Aplicar multiplicador de gravidade
            float gravityEffect = gravityMultiplier * intensity;
            player.velocityY *= gravityEffect;
        }
    }
    
    /**
     * Atualiza o poço de gravidade
     */
    public void update() {
        if (!isActive) return;
        
        // Atualizar animação
        animationTimer++;
        pulseIntensity = (float)(Math.sin(animationTimer * 0.1) * 0.3 + 0.7);
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            GravityParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas ocasionalmente
        if (animationTimer % 10 == 0) {
            createGravityParticle();
        }
    }
    
    /**
     * Desenha o poço de gravidade
     */
    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        
        // Desenhar círculo principal
        g2d.setColor(new Color(wellColor.getRed(), wellColor.getGreen(), wellColor.getBlue(), 
                               (int)(wellColor.getAlpha() * pulseIntensity)));
        g2d.fillOval(x, y, radius * 2, radius * 2);
        
        // Desenhar borda
        g2d.setColor(new Color(wellColor.getRed(), wellColor.getGreen(), wellColor.getBlue(), 200));
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawOval(x, y, radius * 2, radius * 2);
        
        // Desenhar círculos concêntricos
        for (int i = 1; i <= 3; i++) {
            int innerRadius = radius - (i * 10);
            if (innerRadius > 0) {
                g2d.setColor(new Color(wellColor.getRed(), wellColor.getGreen(), wellColor.getBlue(), 
                                      50 - (i * 10)));
                g2d.drawOval(x + (radius - innerRadius), y + (radius - innerRadius), 
                            innerRadius * 2, innerRadius * 2);
            }
        }
        
        // Desenhar partículas
        for (GravityParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Desenhar símbolo de gravidade
        drawGravitySymbol(g2d);
    }
    
    private void createGravityParticles() {
        for (int i = 0; i < 15; i++) {
            createGravityParticle();
        }
    }
    
    private void createGravityParticle() {
        float angle = (float)(Math.random() * Math.PI * 2);
        float distance = (float)(Math.random() * radius);
        int particleX = (int)(x + radius + Math.cos(angle) * distance);
        int particleY = (int)(y + radius + Math.sin(angle) * distance);
        particles.add(new GravityParticle(particleX, particleY, gravityMultiplier));
    }
    
    private void drawGravitySymbol(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new java.awt.BasicStroke(2));
        
        int centerX = x + radius;
        int centerY = y + radius;
        
        if (gravityMultiplier > 1.0f) {
            // Símbolo de gravidade alta (seta para baixo)
            g2d.drawLine(centerX, centerY - 10, centerX, centerY + 10);
            g2d.drawLine(centerX - 5, centerY + 5, centerX, centerY + 10);
            g2d.drawLine(centerX + 5, centerY + 5, centerX, centerY + 10);
        } else if (gravityMultiplier < 1.0f) {
            // Símbolo de gravidade baixa (seta para cima)
            g2d.drawLine(centerX, centerY - 10, centerX, centerY + 10);
            g2d.drawLine(centerX - 5, centerY - 5, centerX, centerY - 10);
            g2d.drawLine(centerX + 5, centerY - 5, centerX, centerY - 10);
        } else {
            // Símbolo de gravidade normal (círculo)
            g2d.drawOval(centerX - 8, centerY - 8, 16, 16);
        }
    }
    
    // Getters
    public Rectangle getArea() { return area; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public float getGravityMultiplier() { return gravityMultiplier; }
    
    /**
     * Classe interna para partículas de gravidade
     */
    private static class GravityParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        private float gravityMultiplier;
        
        public GravityParticle(int x, int y, float gravityMultiplier) {
            this.x = x;
            this.y = y;
            this.gravityMultiplier = gravityMultiplier;
            this.velocityX = (float)(Math.random() * 2 - 1);
            this.velocityY = (float)(Math.random() * 2 - 1);
            this.life = 40 + (int)(Math.random() * 20);
            
            // Cor baseada no tipo de gravidade
            if (gravityMultiplier > 1.0f) {
                this.color = new Color(255, 150, 150, 120);
            } else if (gravityMultiplier < 1.0f) {
                this.color = new Color(150, 150, 255, 120);
            } else {
                this.color = new Color(150, 255, 150, 120);
            }
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Aplicar gravidade às partículas
            if (gravityMultiplier > 1.0f) {
                velocityY += 0.1f; // Puxar para baixo
            } else if (gravityMultiplier < 1.0f) {
                velocityY -= 0.1f; // Puxar para cima
            }
            
            // Fade out
            int alpha = (int)(120 * (life / 60.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
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
