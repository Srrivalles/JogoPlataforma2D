package org.example.objects;

import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Iterator;

public class EnergyOrb {
    // Posição e dimensões
    public int x, y;
    public int baseX, baseY; // Posição original do orbe
    public int width = 16, height = 16;

    // Animação e movimento
    int animationTimer = 0;
    float pulseOffset = 0;
    public boolean collected = false;
    boolean isAttractedToPlayer = false;
    float attractionSpeed = 2.0f;

    // Valores de energia
    int energyValue;
    Color orbColor;
    Color secondaryColor;

    // Hitbox
    public Rectangle hitbox;

    // Efeitos visuais
    ArrayList<EnergyParticle> particles = new ArrayList<>();
    int particleSpawnTimer = 0;
    boolean hasSpecialEffect = false;
    String orbType;
    
    // Sistema de sprites e animações
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    // Movimento orbital (para orbs especiais)
    boolean isOrbital = false;
    float orbitRadius = 30;
    float orbitSpeed = 0.03f;
    float orbitAngle = 0;

    // Sistema de respawn
    boolean canRespawn = false;
    int respawnTimer = 0;
    final int RESPAWN_TIME = 600; // 10 segundos a 60fps

    // Tipos de orbe
    public static final int SMALL_ORB = 15;   // +15 energia, cor ciano
    public static final int MEDIUM_ORB = 25;  // +25 energia, cor amarela
    public static final int LARGE_ORB = 50;   // +50 energia, cor rosa
    public static final int RARE_ORB = 75;    // +75 energia, cor roxa (raro)
    public static final int LEGENDARY_ORB = 100; // +100 energia, multicolorido
    
    public int getX() { return x; }

    public EnergyOrb(int x, int y, int energyValue) {
        this.baseX = x;
        this.baseY = y;
        this.x = x;
        this.y = y;
        this.energyValue = energyValue;
        this.hitbox = new Rectangle(x, y, width, height);
        this.pulseOffset = (float)(Math.random() * Math.PI * 2);

        setupOrbType(energyValue);
        
        // Inicializar sistema de sprites
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "orb_idle";
    }

    // Construtor para orbs orbitais
    public EnergyOrb(int centerX, int centerY, int energyValue, float orbitRadius, float startAngle) {
        this(centerX, centerY, energyValue);
        this.isOrbital = true;
        this.orbitRadius = orbitRadius;
        this.orbitAngle = startAngle;
        this.orbitSpeed = 0.02f + (float)(Math.random() * 0.02f); // Velocidade variável
    }

    private void setupOrbType(int energyValue) {
        if (energyValue <= 15) {
            orbColor = new Color(0, 255, 255); // Ciano
            secondaryColor = new Color(0, 200, 255);
            orbType = "basic";
        } else if (energyValue <= 25) {
            orbColor = new Color(255, 255, 0); // Amarelo
            secondaryColor = new Color(255, 200, 0);
            orbType = "medium";
        } else if (energyValue <= 50) {
            orbColor = new Color(255, 0, 150); // Rosa
            secondaryColor = new Color(255, 100, 200);
            orbType = "large";
            width = 20;
            height = 20;
        } else if (energyValue <= 75) {
            orbColor = new Color(150, 0, 255); // Roxo
            secondaryColor = new Color(200, 100, 255);
            orbType = "rare";
            width = 24;
            height = 24;
            hasSpecialEffect = true;
        } else {
            orbColor = new Color(255, 255, 255); // Branco base para multicolor
            secondaryColor = new Color(255, 200, 100);
            orbType = "legendary";
            width = 28;
            height = 28;
            hasSpecialEffect = true;
            canRespawn = true;
        }

        // Recriar hitbox com novo tamanho
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public void update(Player player) {
        if (collected && !canRespawn) return;

        // Sistema de respawn para orbs lendários
        if (collected && canRespawn) {
            respawnTimer++;
            if (respawnTimer >= RESPAWN_TIME) {
                respawn();
            }
            return;
        }

        animationTimer++;

        // Movimento orbital
        if (isOrbital) {
            orbitAngle += orbitSpeed;
            x = (int)(baseX + Math.cos(orbitAngle) * orbitRadius);
            y = (int)(baseY + Math.sin(orbitAngle) * orbitRadius);
        } else {
            // Movimento flutuante normal
            float floatOffset = (float)Math.sin((animationTimer * 0.05) + pulseOffset) * 2;
            y = (int)(baseY + floatOffset);
        }

        // Atração pelo player quando próximo
        if (player != null && !isAttractedToPlayer) {
            float distanceToPlayer = getDistanceToPlayer(player);
            if (distanceToPlayer < 60) { // Raio de atração
                isAttractedToPlayer = true;
            }
        }

        // Movimento de atração
        if (isAttractedToPlayer && player != null) {
            float dx = (player.x + player.width/2) - (x + width/2);
            float dy = (player.y + player.height/2) - (y + height/2);
            float distance = (float)Math.sqrt(dx*dx + dy*dy);

            if (distance > 10) {
                x += (dx / distance) * attractionSpeed;
                y += (dy / distance) * attractionSpeed;
                attractionSpeed += 0.1f; // Acelera conforme se aproxima
            }
        }

        // Atualizar hitbox
        hitbox.setLocation(x, y);

        // Gerar partículas
        updateParticles();
        spawnParticles();
        
        // Atualizar animação de sprite
        updateSpriteAnimation();
    }
    
    /**
     * Atualiza a animação de sprite baseada no estado do orb
     */
    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        
        String newAnimation = "orb_idle";
        if (collected) {  // Changed from 'isCollected' to 'collected'
            newAnimation = "orb_collect";
        }
        
        // Mudar animação apenas se for diferente da atual
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
        }
    }

    private void updateParticles() {
        Iterator<EnergyParticle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            EnergyParticle particle = iterator.next();
            particle.update();
            if (particle.isDead()) {
                iterator.remove();
            }
        }
    }

    private void spawnParticles() {
        particleSpawnTimer++;

        int spawnRate = hasSpecialEffect ? 3 : 8; // Orbs especiais geram mais partículas

        if (particleSpawnTimer >= spawnRate) {
            particleSpawnTimer = 0;

            // Spawn normal particles
            for (int i = 0; i < (hasSpecialEffect ? 2 : 1); i++) {
                particles.add(new EnergyParticle(
                        x + width/2 + (int)(Math.random() * 10 - 5),
                        y + height/2 + (int)(Math.random() * 10 - 5),
                        orbColor,
                        20 + (int)(Math.random() * 20)
                ));
            }

            // Partículas especiais para orbs raros
            if (orbType.equals("legendary")) {
                Color randomColor = getRandomRainbowColor();
                particles.add(new EnergyParticle(
                        x + width/2,
                        y + height/2,
                        randomColor,
                        30
                ));
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if (collected && !canRespawn) return;

        // Desenhar respawn effect
        if (collected && canRespawn) {
            drawRespawnEffect(g2d);
            return;
        }

        if (GameConfig.ANIMATIONS_ENABLED) {
            // Renderizar com sprite
            drawSprite(g2d);
        } else {
            // Renderização legada
            drawLegacy(g2d);
        }
    }
    
    /**
     * Renderiza o orb usando sprites
     */
    private void drawSprite(Graphics2D g2d) {
        // Renderizar sprite do orb
        spriteRenderer.renderAnimation(g2d, currentAnimation, x, y);
        
        // Desenhar partículas por cima
        for (EnergyParticle particle : particles) {
            particle.draw(g2d);
        }
    }
    
    /**
     * Renderização legada com formas geométricas
     */
    private void drawLegacy(Graphics2D g2d) {
        // Desenhar partículas primeiro
        for (EnergyParticle particle : particles) {
            particle.draw(g2d);
        }

        // Aura externa pulsante
        float pulseSize = 1.0f + (float)Math.sin((animationTimer * 0.1) + pulseOffset) * 0.4f;
        int auraSize = (int)(width * pulseSize * 1.5f);

        // Aura com gradiente
        Color auraColor = new Color(orbColor.getRed(), orbColor.getGreen(), orbColor.getBlue(), 30);
        g2d.setColor(auraColor);
        g2d.fillOval(x + width/2 - auraSize/2, y + height/2 - auraSize/2, auraSize, auraSize);

        // Efeito especial para orbs lendários
        if (orbType.equals("legendary")) {
            drawLegendaryEffect(g2d, pulseSize);
        }

        // Núcleo principal com gradiente
        if (orbType.equals("legendary")) {
            // Cor que muda com o tempo para orbs lendários
            Color dynamicColor = getTimeDynamicColor();
            GradientPaint gradient = new GradientPaint(
                    x, y, dynamicColor,
                    x + width, y + height, dynamicColor.darker()
            );
            g2d.setPaint(gradient);
        } else {
            GradientPaint gradient = new GradientPaint(
                    x, y, orbColor,
                    x + width, y + height, secondaryColor
            );
            g2d.setPaint(gradient);
        }

        g2d.fillOval(x + 2, y + 2, width - 4, height - 4);

        // Brilho interno
        g2d.setColor(Color.WHITE);
        int glowSize = width - 8;
        if (hasSpecialEffect) {
            glowSize = (int)(glowSize * (1 + Math.sin(animationTimer * 0.2) * 0.3));
        }
        g2d.fillOval(x + (width - glowSize)/2, y + (height - glowSize)/2, glowSize, glowSize);

        // Partículas orbitais
        drawOrbitalParticles(g2d);

        // Contorno com brilho
        g2d.setColor(orbColor.brighter());
        g2d.setStroke(new BasicStroke(hasSpecialEffect ? 2 : 1));
        g2d.drawOval(x + 2, y + 2, width - 4, height - 4);

        // Indicador de valor (para debug ou UI)
        if (energyValue > 50) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("+" + energyValue, x - 5, y - 5);
        }
    }

    private void drawOrbitalParticles(Graphics2D g2d) {
        int particleCount = hasSpecialEffect ? 8 : 6;

        for (int i = 0; i < particleCount; i++) {
            double angle = (i * Math.PI * 2 / particleCount) + (animationTimer * 0.03);
            int radius = width/2 + 12;

            if (orbType.equals("legendary")) {
                radius += Math.sin(animationTimer * 0.1 + i) * 5;
            }

            int particleX = (int)(x + width/2 + Math.cos(angle) * radius);
            int particleY = (int)(y + height/2 + Math.sin(angle) * radius);

            Color particleColor = orbType.equals("legendary") ?
                    getRandomRainbowColor() :
                    new Color(orbColor.getRed(), orbColor.getGreen(), orbColor.getBlue(), 180);

            g2d.setColor(particleColor);
            int particleSize = hasSpecialEffect ? 3 : 2;
            g2d.fillOval(particleX - particleSize/2, particleY - particleSize/2, particleSize, particleSize);
        }
    }

    private void drawLegendaryEffect(Graphics2D g2d, float pulseSize) {
        // Anéis de energia multicoloridos
        for (int i = 0; i < 3; i++) {
            int ringRadius = (int)((width + i * 15) * pulseSize);
            Color ringColor = getTimeDynamicColor();
            ringColor = new Color(ringColor.getRed(), ringColor.getGreen(), ringColor.getBlue(), 100 - i * 30);

            g2d.setColor(ringColor);
            g2d.setStroke(new BasicStroke(2 - i * 0.5f));
            g2d.drawOval(x + width/2 - ringRadius/2, y + height/2 - ringRadius/2, ringRadius, ringRadius);
        }
    }

    private void drawRespawnEffect(Graphics2D g2d) {
        float progress = (float)respawnTimer / RESPAWN_TIME;
        int effectRadius = (int)(40 * progress);

        // Círculo de respawn
        g2d.setColor(new Color(orbColor.getRed(), orbColor.getGreen(), orbColor.getBlue(), (int)(100 * progress)));
        g2d.fillOval(baseX - effectRadius/2, baseY - effectRadius/2, effectRadius, effectRadius);

        // Partículas convergindo
        for (int i = 0; i < 12; i++) {
            double angle = i * Math.PI * 2 / 12;
            int particleDistance = (int)(50 * (1 - progress));
            int particleX = (int)(baseX + Math.cos(angle) * particleDistance);
            int particleY = (int)(baseY + Math.sin(angle) * particleDistance);

            g2d.setColor(orbColor);
            g2d.fillOval(particleX - 2, particleY - 2, 4, 4);
        }
    }

    private Color getTimeDynamicColor() {
        float hue = (animationTimer * 0.01f) % 1.0f;
        return Color.getHSBColor(hue, 1.0f, 1.0f);
    }

    private Color getRandomRainbowColor() {
        float hue = (float)Math.random();
        return Color.getHSBColor(hue, 1.0f, 1.0f);
    }

    private float getDistanceToPlayer(Player player) {
        float dx = (player.x + player.width/2) - (x + width/2);
        float dy = (player.y + player.height/2) - (y + height/2);
        return (float)Math.sqrt(dx*dx + dy*dy);
    }

    private void respawn() {
        collected = false;
        respawnTimer = 0;
        x = baseX;
        y = baseY;
        isAttractedToPlayer = false;
        attractionSpeed = 2.0f;
        particles.clear();

        // Efeito de spawn
        for (int i = 0; i < 20; i++) {
            particles.add(new EnergyParticle(
                    x + width/2 + (int)(Math.random() * 20 - 10),
                    y + height/2 + (int)(Math.random() * 20 - 10),
                    orbColor,
                    40
            ));
        }
    }

    // Métodos especiais para diferentes efeitos ao coletar
    public void onCollect(Player player) {
        collected = true;

<<<<<<< HEAD
        // Tocar som de efeito ao coletar (com base no tipo)
        try {
            switch (orbType) {
                case "rare":
                    org.example.audio.AudioManager.playEffect("/audio/orb_rare.wav", -3.0f);
                    break;
                case "legendary":
                    org.example.audio.AudioManager.playEffect("/audio/orb_legendary.wav", -3.0f);
                    break;
                default:
                    org.example.audio.AudioManager.playEffect("/audio/orb_collect.wav", -3.0f);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        // Restaurar energia do player
        player.energyLevel = Math.min(100, player.energyLevel + energyValue);

        // Efeitos especiais baseados no tipo
        switch (orbType) {
            case "rare":
                // Dar boost temporário de velocidade
                // Implementar sistema de buffs no Player se necessário
                break;
            case "legendary":
                // Recarregar completamente o dash
                player.canDash = true;
                player.dashCooldown = 0;
                // Adicionar efeito visual especial no player
                break;
        }

        // Criar partículas de coleta
        createCollectionEffect();
    }

    private void createCollectionEffect() {
        // Explosão de partículas ao coletar
        for (int i = 0; i < 15; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 2 + (float)(Math.random() * 4);
            particles.add(new EnergyParticle(
                    x + width/2,
                    y + height/2,
                    orbColor,
                    30,
                    (float)Math.cos(angle) * speed,
                    (float)Math.sin(angle) * speed
            ));
        }
    }

    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }
    public int getEnergyValue() { return energyValue; }
    public String getOrbType() { return orbType; }
    public boolean canRespawn() { return canRespawn; }

    // Classe interna para partículas de energia
    private class EnergyParticle {
        float x, y;
        float vx, vy;
        Color color;
        int life;
        int maxLife;
        float alpha;

        public EnergyParticle(int x, int y, Color color, int life) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.life = life;
            this.maxLife = life;
            this.alpha = 1.0f;

            // Movimento aleatório
            this.vx = (float)(Math.random() * 2 - 1) * 0.5f;
            this.vy = (float)(Math.random() * 2 - 1) * 0.5f;
        }

        public EnergyParticle(int x, int y, Color color, int life, float vx, float vy) {
            this(x, y, color, life);
            this.vx = vx;
            this.vy = vy;
        }

        public void update() {
            x += vx;
            y += vy;
            life--;
            alpha = (float)life / maxLife;

            // Gravidade leve para cima (efeito de energia)
            vy -= 0.02f;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * alpha)));
            g2d.fillOval((int)x - 1, (int)y - 1, 2, 2);
        }

        public boolean isDead() {
            return life <= 0;
        }
    }
}