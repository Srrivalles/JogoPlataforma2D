package org.example.objects;

import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;

import java.awt.*;

public class Enemy {
    public double x, y;
    public int width = 30;
    public int height = 40;
    public int direction = 1; // 1 = direita, -1 = esquerda
    public Rectangle hitbox;

    // Física
    public double velocityX = 1.5;
    public double velocityY = 0;
    public boolean isOnGround = false;

    // Patrulha
    public double patrolLeft;
    public double patrolRight;
    public double speed = 1.0;

    // Animação
    private int animationFrame = 0;
    private int eyeGlowIntensity = 0;
    private boolean glowIncreasing = true;

    // Sprites
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    public Enemy(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.patrolLeft = startX - 50;
        this.patrolRight = startX + 50;
        this.hitbox = new Rectangle((int)x, (int)y, width, height);
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "enemy_idle";
    }

    public Enemy(double startX, double startY, double patrolLeft, double patrolRight) {
        this.x = startX;
        this.y = startY;
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.hitbox = new Rectangle((int)x, (int)y, width, height);
    }

    public void update() {
        x += speed * direction;

        if (x <= patrolLeft) {
            direction = 1;
            x = patrolLeft;
        } else if (x >= patrolRight) {
            direction = -1;
            x = patrolRight;
        }

        hitbox.setLocation((int)x, (int)y);

        animationFrame++;

        if (glowIncreasing) {
            eyeGlowIntensity += 3;
            if (eyeGlowIntensity >= 200) glowIncreasing = false;
        } else {
            eyeGlowIntensity -= 3;
            if (eyeGlowIntensity <= 60) glowIncreasing = true;
        }

        updateSpriteAnimation();
    }

    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        String newAnimation = "enemy_idle";
        if (Math.abs(speed) > 0.1) newAnimation = "enemy_walk";
        if (!newAnimation.equals(currentAnimation)) currentAnimation = newAnimation;
    }

    public void update(Player player) {
        update();
    }

    public Rectangle getHitbox() { return hitbox; }
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void draw(Graphics2D g2d) {
        if (GameConfig.ANIMATIONS_ENABLED) {
            drawSprite(g2d);
        } else {
            drawLegacy(g2d);
        }
    }

    private void drawSprite(Graphics2D g2d) {
        boolean flipX = direction < 0;
        spriteRenderer.renderAnimation(g2d, currentAnimation, (int)x, (int)y, GameConfig.SPRITE_SCALE, flipX);
    }

    /**
     * 🎨 NOVO VISUAL “ESQUELETO VERDE” – modo legado
     */
    private void drawLegacy(Graphics2D g2d) {
        int drawX = (int)x;
        int drawY = (int)y;

        // Ativar antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sombra
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(drawX + 4, drawY + height - 8, width - 8, 10);

        // Corpo (verde pálido esquelético)
        g2d.setColor(new Color(90, 180, 90));
        g2d.fillRoundRect(drawX + 6, drawY + 12, width - 12, height - 14, 6, 6);

        // “Costelas” (linhas horizontais brancas)
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(220, 255, 220));
        for (int i = 0; i < 4; i++) {
            int ribY = drawY + 20 + i * 6;
            g2d.drawLine(drawX + 10, ribY, drawX + width - 10, ribY);
        }

        // Cabeça (crânio)
        g2d.setColor(new Color(180, 255, 180));
        g2d.fillOval(drawX + 6, drawY - 2, width - 12, 20);

        // Mandíbula (separada)
        g2d.setColor(new Color(160, 240, 160));
        g2d.fillRect(drawX + 10, drawY + 14, width - 20, 4);

        // Olhos (brilho esverdeado)
        g2d.setColor(new Color(120, 255, 120, eyeGlowIntensity));
        int eyeOffsetX = direction == 1 ? 4 : -4;
        g2d.fillOval(drawX + 10 + eyeOffsetX, drawY + 5, 6, 6);
        g2d.fillOval(drawX + width - 16 + eyeOffsetX, drawY + 5, 6, 6);

        // Pupilas pretas
        g2d.setColor(Color.BLACK);
        g2d.fillOval(drawX + 12 + eyeOffsetX, drawY + 6, 2, 3);
        g2d.fillOval(drawX + width - 14 + eyeOffsetX, drawY + 6, 2, 3);

        // Fenda nasal
        g2d.setColor(new Color(30, 60, 30));
        g2d.drawLine(drawX + width / 2 - 2, drawY + 9, drawX + width / 2 + 2, drawY + 9);

        // Pernas (ossos)
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(200, 255, 200));
        g2d.drawLine(drawX + 10, drawY + height - 10, drawX + 10, drawY + height - 2);
        g2d.drawLine(drawX + width - 10, drawY + height - 10, drawX + width - 10, drawY + height - 2);

        // Braços (ossos laterais)
        g2d.drawLine(drawX + 4, drawY + 18, drawX + 4, drawY + 32);
        g2d.drawLine(drawX + width - 4, drawY + 18, drawX + width - 4, drawY + 32);

        // Efeito de aura espectral verde
        g2d.setColor(new Color(80, 255, 120, 40));
        int auraOffset = (int)(Math.sin(animationFrame * 0.1) * 3);
        g2d.fillOval(drawX - 6 + auraOffset, drawY - 6 + auraOffset, width + 12, height + 12);

        // Efeito de partículas verdes
        g2d.setColor(new Color(120, 255, 150, 90));
        for (int i = 0; i < 4; i++) {
            int px = drawX + width / 2 + (int)(Math.sin(animationFrame * 0.2 + i) * 8);
            int py = drawY + (int)(Math.cos(animationFrame * 0.3 + i) * 6);
            g2d.fillOval(px, py, 3, 3);
        }
    }
}
