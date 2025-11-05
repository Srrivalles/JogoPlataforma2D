package org.example.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Efeito de vento que empurra o player
 */
public class WindEffect {
    private int x, y, width, height;
    private float windStrength;
    private boolean isActive;
    private Rectangle area;
    private Color windColor;
    
    // Efeitos visuais
    private ArrayList<WindParticle> particles;
    private int particleTimer = 0;
    
    public WindEffect(int x, int y, int width, int height, float windStrength) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.windStrength = windStrength;
        this.isActive = true;
        this.area = new Rectangle(x, y, width, height);
        this.windColor = new Color(100, 200, 255, 100);
        this.particles = new ArrayList<>();
        
        // Criar partículas iniciais
        createWindParticles();
    }
    
    /**
     * Aplica o efeito de vento no player
     */
    public void applyWindEffect(org.example.objects.Player player) {
        if (!isActive) return;
        
        // Verificar se o player está na área do vento
        if (area.intersects(player.getHitbox())) {
            // Aplicar força do vento
            player.velocityX += windStrength * 0.1f;
            
            // Limitar velocidade máxima
            if (player.velocityX > 8) player.velocityX = 8;
            if (player.velocityX < -8) player.velocityX = -8;
        }
    }
    
    /**
     * Atualiza o efeito de vento
     */
    public void update() {
        if (!isActive) return;
        
        // Atualizar partículas
        particleTimer++;
        if (particleTimer >= 3) { // Criar nova partícula a cada 3 frames
            createWindParticle();
            particleTimer = 0;
        }
        
        // Atualizar partículas existentes
        for (int i = particles.size() - 1; i >= 0; i--) {
            WindParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
    }
    
    /**
     * Desenha o efeito de vento
     */
    
    private void createWindParticles() {
        for (int i = 0; i < 10; i++) {
            createWindParticle();
        }
    }
    
    private void createWindParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new WindParticle(particleX, particleY, windStrength));
    }

    public float getWindStrength() { return windStrength; }
    
    /**
     * Classe interna para partículas de vento
     */
    private static class WindParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        
        public WindParticle(int x, int y, float windStrength) {
            this.x = x;
            this.y = y;
            this.velocityX = windStrength * 0.5f + (float)(Math.random() * 2 - 1);
            this.velocityY = (float)(Math.random() * 2 - 1);
            this.life = 30 + (int)(Math.random() * 20);
            this.color = new Color(200, 220, 255, 100);
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Fade out
            int alpha = (int)(100 * (life / 50.0f));
            if (alpha < 0) alpha = 0;
            color = new Color(200, 220, 255, alpha);
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

