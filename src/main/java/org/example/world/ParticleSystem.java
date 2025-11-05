package org.example.world;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Sistema simples de partículas para efeitos visuais
 */
public class ParticleSystem {
    
    private ArrayList<Particle> particles;
    
    public ParticleSystem() {
        this.particles = new ArrayList<>();
    }
    
    /**
     * Atualiza todas as partículas
     */
    public void update() {
        // Atualizar e remover partículas expiradas
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.update();
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
    }
    
    /**
     * Renderiza todas as partículas
     */
    public void render(Graphics2D g2d) {
        for (Particle particle : particles) {
            particle.render(g2d);
        }
    }
    
    /**
     * Adiciona uma nova partícula
     */
    public void addParticle(Particle particle) {
        particles.add(particle);
    }
    
    /**
     * Adiciona partículas ambientais cyberpunk nas plataformas
     */
    public void addAmbientParticles(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float particleX = x + (float)(Math.random() * 100 - 50);
            float particleY = y + (float)(Math.random() * 20 - 10);
            float velocityX = (float)(Math.random() * 2 - 1);
            float velocityY = (float)(Math.random() * 2 - 1);
            int life = 60 + (int)(Math.random() * 120);
            
            particles.add(new CyberpunkParticle(particleX, particleY, velocityX, velocityY, life));
        }
    }
    
    /**
     * Adiciona partículas de energia cyberpunk
     */
    public void addEnergyParticles(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float particleX = x + (float)(Math.random() * 60 - 30);
            float particleY = y + (float)(Math.random() * 40 - 20);
            float velocityX = (float)(Math.random() * 3 - 1.5);
            float velocityY = (float)(Math.random() * 3 - 1.5);
            int life = 80 + (int)(Math.random() * 100);
            
            particles.add(new EnergyParticle(particleX, particleY, velocityX, velocityY, life));
        }
    }
    
    /**
     * Adiciona partículas de holograma
     */
    public void addHologramParticles(float x, float y, int count) {
        for (int i = 0; i < count; i++) {
            float particleX = x + (float)(Math.random() * 80 - 40);
            float particleY = y + (float)(Math.random() * 30 - 15);
            float velocityX = (float)(Math.random() * 1.5 - 0.75);
            float velocityY = (float)(Math.random() * 1.5 - 0.75);
            int life = 100 + (int)(Math.random() * 150);
            
            particles.add(new HologramParticle(particleX, particleY, velocityX, velocityY, life));
        }
    }
    
    /**
     * Limpa todas as partículas
     */
    public void clear() {
        particles.clear();
    }
    
    /**
     * Renderiza partículas de fundo
     */
    public void renderBackground(Graphics2D g2d, int cameraX, int cameraY) {
        // Renderizar partículas de fundo se houver
        render(g2d);
    }
    
    /**
     * Renderiza partículas de primeiro plano
     */
    public void renderForeground(Graphics2D g2d, int cameraX, int cameraY) {
        // Renderizar partículas de primeiro plano se houver
        render(g2d);
    }
    
    /**
     * Classe interna para representar uma partícula simples
     */
    public static class Particle {
        protected float x, y;
        protected float velocityX, velocityY;
        protected int life;
        protected int maxLife;
        
        public Particle(float x, float y, float velocityX, float velocityY, int life) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = life;
            this.maxLife = life;
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
        }
        
        public void render(Graphics2D g2d) {
            // Renderização básica - pode ser sobrescrita
            g2d.fillOval((int)x, (int)y, 2, 2);
        }
        
        public boolean isExpired() {
            return life <= 0;
        }
    }
    
    /**
     * Partícula cyberpunk com efeitos neon
     */
    public static class CyberpunkParticle extends Particle {
        private java.awt.Color color;
        private float glowIntensity = 0;
        
        public CyberpunkParticle(float x, float y, float velocityX, float velocityY, int life) {
            super(x, y, velocityX, velocityY, life);
            
            // Cores cyberpunk aleatórias
            int colorType = (int)(Math.random() * 3);
            switch (colorType) {
                case 0: // Azul cyberpunk
                    this.color = new java.awt.Color(0, 150, 255);
                    break;
                case 1: // Roxo neon
                    this.color = new java.awt.Color(150, 0, 255);
                    break;
                default: // Verde matrix
                    this.color = new java.awt.Color(0, 255, 100);
                    break;
            }
        }
        
        @Override
        public void update() {
            super.update();
            glowIntensity = (float)(Math.sin(life * 0.1) * 0.3 + 0.7);
        }
        
        @Override
        public void render(Graphics2D g2d) {
            // Efeito de brilho
            if (glowIntensity > 0.8f) {
                g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 
                    (int)(50 * (glowIntensity - 0.8f) * 5)));
                g2d.fillOval((int)x - 2, (int)y - 2, 6, 6);
            }
            
            // Partícula principal
            g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 
                (int)(255 * (life / (float)maxLife))));
            g2d.fillOval((int)x, (int)y, 3, 3);
        }
    }
    
    /**
     * Partícula de energia com efeitos elétricos
     */
    public static class EnergyParticle extends Particle {
        private java.awt.Color color;
        private float sparkIntensity = 0;
        
        public EnergyParticle(float x, float y, float velocityX, float velocityY, int life) {
            super(x, y, velocityX, velocityY, life);
            this.color = new java.awt.Color(255, 255, 0); // Amarelo energia
        }
        
        @Override
        public void update() {
            super.update();
            sparkIntensity = (float)(Math.random() * 0.5 + 0.5);
        }
        
        @Override
        public void render(Graphics2D g2d) {
            // Efeito de faísca
            g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 
                (int)(200 * sparkIntensity * (life / (float)maxLife))));
            
            // Desenhar como uma pequena estrela
            int[] starX = {(int)x, (int)x + 2, (int)x + 4, (int)x + 2, (int)x, (int)x - 2, (int)x - 4, (int)x - 2};
            int[] starY = {(int)y - 4, (int)y - 2, (int)y, (int)y + 2, (int)y + 4, (int)y + 2, (int)y, (int)y - 2};
            g2d.fillPolygon(starX, starY, 8);
        }
    }
    
    /**
     * Partícula de holograma com efeitos translúcidos
     */
    public static class HologramParticle extends Particle {
        private java.awt.Color color;
        private float transparency = 0;
        
        public HologramParticle(float x, float y, float velocityX, float velocityY, int life) {
            super(x, y, velocityX, velocityY, life);
            this.color = new java.awt.Color(0, 255, 255); // Ciano holográfico
        }
        
        @Override
        public void update() {
            super.update();
            transparency = (float)(Math.sin(life * 0.05) * 0.4 + 0.6);
        }
        
        @Override
        public void render(Graphics2D g2d) {
            // Efeito holográfico translúcido
            g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 
                (int)(100 * transparency * (life / (float)maxLife))));
            
            // Desenhar como um quadrado translúcido
            g2d.fillRect((int)x, (int)y, 4, 4);
            
            // Borda brilhante
            g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), 
                (int)(150 * transparency * (life / (float)maxLife))));
            g2d.drawRect((int)x, (int)y, 4, 4);
        }
    }
}
