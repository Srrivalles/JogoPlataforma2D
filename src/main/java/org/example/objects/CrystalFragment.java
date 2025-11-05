package org.example.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Fragmento de cristal - coletável especial da fase Abismo da Aurora
 */
public class CrystalFragment {
    private int x, y, width, height;
    private Rectangle hitbox;
    private boolean isActive;
    private boolean isCollected;
    private Color crystalColor;
    private String fragmentType;
    
    // Efeitos visuais
    private ArrayList<CrystalParticle> particles;
    private int animationTimer = 0;
    private float glowIntensity = 0;
    private float rotation = 0;
    private float floatOffset = 0;
    
    // Tipos de fragmentos
    public enum FragmentType {
        BLUE_CRYSTAL,      // Cristal azul - energia básica
        PURPLE_CRYSTAL,    // Cristal roxo - energia média
        GOLDEN_CRYSTAL,    // Cristal dourado - energia alta
        AURORA_CRYSTAL     // Cristal da aurora - energia máxima
    }
    
    public CrystalFragment(int x, int y, FragmentType type) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        this.isActive = true;
        this.isCollected = false;
        this.hitbox = new Rectangle(x, y, width, height);
        this.particles = new ArrayList<>();
        
        // Configurar tipo e cor
        setupFragmentType(type);
        
        // Criar partículas iniciais
        createCrystalParticles();
    }
    
    /**
     * Configura o tipo e cor do fragmento
     */
    private void setupFragmentType(FragmentType type) {
        switch (type) {
            case BLUE_CRYSTAL:
                this.crystalColor = new Color(100, 150, 255, 200);
                this.fragmentType = "Blue Crystal";
                break;
            case PURPLE_CRYSTAL:
                this.crystalColor = new Color(150, 100, 255, 200);
                this.fragmentType = "Purple Crystal";
                break;
            case GOLDEN_CRYSTAL:
                this.crystalColor = new Color(255, 215, 0, 200);
                this.fragmentType = "Golden Crystal";
                break;
            case AURORA_CRYSTAL:
                this.crystalColor = new Color(255, 100, 255, 200);
                this.fragmentType = "Aurora Crystal";
                break;
        }
    }
    
    /**
     * Verifica se o player coletou o fragmento
     */
    public boolean checkCollection(org.example.objects.Player player) {
        if (!isActive || isCollected) return false;
        
        if (hitbox.intersects(player.getHitbox())) {
            // Coletar fragmento
            isCollected = true;
            
            // Aplicar efeito ao player
            applyCrystalEffect(player);
            
            // Criar efeito de coleta
            createCollectionEffect();

            return true;
        }
        
        return false;
    }
    
    /**
     * Aplica o efeito do cristal ao player
     */
    private void applyCrystalEffect(org.example.objects.Player player) {
        switch (fragmentType) {
            case "Blue Crystal":
                // Energia básica
                // player.addEnergy(20);
                break;
            case "Purple Crystal":
                // Energia média
                // player.addEnergy(40);
                break;
            case "Golden Crystal":
                // Energia alta + vida
                // player.addEnergy(60);
                // player.addHealth(1);
                break;
            case "Aurora Crystal":
                // Energia máxima + efeitos especiais
                // player.addEnergy(100)
                // player.addHealth(2);
                break;
        }
    }
    
    /**
     * Atualiza o fragmento de cristal
     */
    public void update() {
        if (!isActive || isCollected) return;
        
        // Atualizar animação
        animationTimer++;
        glowIntensity = (float)(Math.sin(animationTimer * 0.1) * 0.4 + 0.6);
        rotation += 0.05f;
        floatOffset = (float)(Math.sin(animationTimer * 0.08) * 3);
        
        // Atualizar partículas
        for (int i = particles.size() - 1; i >= 0; i--) {
            CrystalParticle particle = particles.get(i);
            particle.update();
            
            if (particle.isExpired()) {
                particles.remove(i);
            }
        }
        
        // Criar novas partículas
        if (animationTimer % 12 == 0) {
            createCrystalParticle();
        }
    }
    
    /**
     * Desenha o fragmento de cristal
     */
    public void draw(Graphics2D g2d) {
        if (!isActive || isCollected) return;
        
        // Salvar transformação atual
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        
        // Aplicar rotação e flutuação
        g2dCopy.translate(x + width/2, y + height/2 + floatOffset);
        g2dCopy.rotate(rotation);
        g2dCopy.translate(-width/2, -height/2);
        
        // Desenhar brilho de fundo
        drawGlowBackground(g2dCopy);
        
        // Desenhar cristal principal
        drawCrystalShape(g2dCopy);
        
        // Desenhar reflexos
        drawCrystalReflections(g2dCopy);
        
        // Restaurar transformação
        g2dCopy.dispose();
        
        // Desenhar partículas (sem rotação)
        for (CrystalParticle particle : particles) {
            particle.draw(g2d);
        }
        
        // Desenhar aura
        drawCrystalAura(g2d);
    }
    
    private void drawGlowBackground(Graphics2D g2d) {
        // Brilho de fundo pulsante
        Color glowColor = new Color(crystalColor.getRed(), crystalColor.getGreen(), 
                                  crystalColor.getBlue(), (int)(80 * glowIntensity));
        g2d.setColor(glowColor);
        g2d.fillOval(-4, -4, width + 8, height + 8);
    }
    
    private void drawCrystalShape(Graphics2D g2d) {
        // Forma do cristal (diamante)
        g2d.setColor(crystalColor);
        
        int[] xPoints = {width/2, width, width/2, 0};
        int[] yPoints = {0, height/2, height, height/2};
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Borda
        g2d.setColor(new Color(crystalColor.getRed(), crystalColor.getGreen(), 
                              crystalColor.getBlue(), 255));
        g2d.setStroke(new java.awt.BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 4);
    }
    
    private void drawCrystalReflections(Graphics2D g2d) {
        // Reflexos internos
        g2d.setColor(new Color(255, 255, 255, 100));
        
        // Reflexo principal
        g2d.fillOval(width/4, height/4, width/4, height/4);
        
        // Reflexos menores
        g2d.fillOval(width/3, height/6, 3, 3);
        g2d.fillOval(width/2, height/3, 2, 2);
    }
    
    private void drawCrystalAura(Graphics2D g2d) {
        // Aura ao redor do cristal
        Color auraColor = new Color(crystalColor.getRed(), crystalColor.getGreen(), 
                                  crystalColor.getBlue(), (int)(30 * glowIntensity));
        g2d.setColor(auraColor);
        
        // Múltiplos círculos concêntricos
        for (int i = 1; i <= 3; i++) {
            int radius = 8 + (i * 4);
            g2d.drawOval(x + width/2 - radius, y + height/2 - radius + (int)floatOffset, 
                        radius * 2, radius * 2);
        }
    }
    
    private void createCrystalParticles() {
        for (int i = 0; i < 8; i++) {
            createCrystalParticle();
        }
    }
    
    private void createCrystalParticle() {
        int particleX = x + (int)(Math.random() * width);
        int particleY = y + (int)(Math.random() * height);
        particles.add(new CrystalParticle(particleX, particleY, crystalColor));
    }
    
    private void createCollectionEffect() {
        // Criar explosão de partículas quando coletado
        for (int i = 0; i < 20; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 2 + (float)(Math.random() * 3);
            particles.add(new CrystalParticle(
                x + width/2 + (int)(Math.cos(angle) * 8),
                y + height/2 + (int)(Math.sin(angle) * 8),
                crystalColor,
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            ));
        }
    }
    
    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public boolean isCollected() { return isCollected; }
    public String getFragmentType() { return fragmentType; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    /**
     * Classe interna para partículas do cristal
     */
    private static class CrystalParticle {
        private float x, y;
        private float velocityX, velocityY;
        private int life;
        private Color color;
        
        public CrystalParticle(int x, int y, Color baseColor) {
            this(x, y, baseColor, (float)(Math.random() * 1 - 0.5), (float)(Math.random() * 1 - 0.5));
        }
        
        public CrystalParticle(int x, int y, Color baseColor, float velocityX, float velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.life = 30 + (int)(Math.random() * 20);
            this.color = new Color(baseColor.getRed(), baseColor.getGreen(), 
                                 baseColor.getBlue(), 150);
        }
        
        public void update() {
            x += velocityX;
            y += velocityY;
            life--;
            
            // Fade out
            int alpha = (int)(150 * (life / 50.0f));
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
