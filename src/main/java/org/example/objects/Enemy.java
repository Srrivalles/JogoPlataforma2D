package org.example.objects;

import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
<<<<<<< HEAD

=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
import java.awt.*;

public class Enemy {
    public double x, y;
    public int width = 30;
    public int height = 40;
    public int direction = 1; // 1 = direita, -1 = esquerda
    public Rectangle hitbox;

<<<<<<< HEAD
    // Física
=======
    // Campos de física
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public double velocityX = 1.5;
    public double velocityY = 0;
    public boolean isOnGround = false;

<<<<<<< HEAD
    // Patrulha
    public double patrolLeft;
    public double patrolRight;
    public double speed = 1.0;

    // Animação
    private int animationFrame = 0;
    private int eyeGlowIntensity = 0;
    private boolean glowIncreasing = true;

    // Sprites
=======
    // Limites de patrulha - mudados para double
    public double patrolLeft;
    public double patrolRight;
    public double speed = 1.0; // Mudado para double

    // Variáveis para animações assustadoras
    private int animationFrame = 0;
    private int eyeGlowIntensity = 0;
    private boolean glowIncreasing = true;
    
    // Sistema de sprites e animações
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    public Enemy(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.patrolLeft = startX - 50;
        this.patrolRight = startX + 50;
        this.hitbox = new Rectangle((int)x, (int)y, width, height);
<<<<<<< HEAD
=======
        
        // Inicializar sistema de sprites
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "enemy_idle";
    }

<<<<<<< HEAD
=======
    // Construtor com limites de patrulha personalizados
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public Enemy(double startX, double startY, double patrolLeft, double patrolRight) {
        this.x = startX;
        this.y = startY;
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.hitbox = new Rectangle((int)x, (int)y, width, height);
    }

    public void update() {
<<<<<<< HEAD
        x += speed * direction;

        if (x <= patrolLeft) {
            direction = 1;
            x = patrolLeft;
        } else if (x >= patrolRight) {
            direction = -1;
=======
        // Lógica de movimento de patrulha
        x += speed * direction;

        // Mudar direção quando atingir os limites da patrulha
        if (x <= patrolLeft) {
            direction = 1; // Mover para direita
            x = patrolLeft;
        } else if (x >= patrolRight) {
            direction = -1; // Mover para esquerda
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            x = patrolRight;
        }

        hitbox.setLocation((int)x, (int)y);

<<<<<<< HEAD
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
=======
        // Animações assustadoras
        animationFrame++;

        // Efeito de brilho pulsante nos olhos
        if (glowIncreasing) {
            eyeGlowIntensity += 2;
            if (eyeGlowIntensity >= 100) glowIncreasing = false;
        } else {
            eyeGlowIntensity -= 2;
            if (eyeGlowIntensity <= 20) glowIncreasing = true;
        }
        
        // Atualizar animação de sprite
        updateSpriteAnimation();
    }
    
    /**
     * Atualiza a animação de sprite baseada no estado do inimigo
     */
    private void updateSpriteAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        
        String newAnimation = "enemy_idle";
        if (Math.abs(speed) > 0.1) {
            newAnimation = "enemy_walk";
        }
        
        // Mudar animação apenas se for diferente da atual
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
        }
    }

    // Sobrecarga para compatibilidade com GamePanel
    public void update(Player player) {
        update(); // Chama a versão básica
    }

    // Getters essenciais
    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(Graphics2D g2d) {
        if (GameConfig.ANIMATIONS_ENABLED) {
            // Renderizar com sprite
            drawSprite(g2d);
        } else {
            // Renderização legada
            drawLegacy(g2d);
        }
    }
    
    /**
     * Renderiza o inimigo usando sprites
     */
    private void drawSprite(Graphics2D g2d) {
        // Renderizar sprite do inimigo
        boolean flipX = direction < 0; // Virar sprite se movendo para esquerda
        spriteRenderer.renderAnimation(g2d, currentAnimation, (int)x, (int)y, GameConfig.SPRITE_SCALE, flipX);
    }
    
    /**
     * Renderização legada com formas geométricas
     */
    private void drawLegacy(Graphics2D g2d) {
        // Converter coordenadas double para int para desenho
        int drawX = (int)x;
        int drawY = (int)y;

        // Habilitar antialiasing para visual mais suave
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sombra mais dramática e distorcida
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillOval(drawX + 2, drawY + height - 8, width - 4, 12);

        // Aura sinistra ao redor do inimigo
        int auraOffset = (int)(Math.sin(animationFrame * 0.1) * 3);
        g2d.setColor(new Color(80, 0, 0, 40));
        g2d.fillOval(drawX - 5 + auraOffset, drawY - 5 + auraOffset, width + 10, height + 10);

        // Corpo principal mais angular e ameaçador
        g2d.setColor(new Color(120, 20, 20)); // Vermelho mais escuro
        int[] bodyX = {drawX + 4, drawX + width - 4, drawX + width - 2, drawX + width - 6, drawX + 6, drawX + 2};
        int[] bodyY = {drawY + 15, drawY + 15, drawY + 25, drawY + height - 5, drawY + height - 5, drawY + 25};
        g2d.fillPolygon(bodyX, bodyY, 6);

        // Contorno com gradiente sombrio
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(bodyX, bodyY, 6);

        // Cabeça mais assustadora - formato irregular
        g2d.setColor(new Color(150, 30, 30));
        int headWidth = width - 8;
        int headHeight = 28;

        // Cabeça com chifres
        g2d.fillOval(drawX + 4, drawY + 2, headWidth, headHeight);

        // Chifres malignos
        g2d.setColor(new Color(60, 10, 10));
        int[] hornLeftX = {drawX + 8, drawX + 12, drawX + 6};
        int[] hornLeftY = {drawY + 5, drawY - 3, drawY + 2};
        g2d.fillPolygon(hornLeftX, hornLeftY, 3);

        int[] hornRightX = {drawX + width - 8, drawX + width - 6, drawX + width - 12};
        int[] hornRightY = {drawY + 5, drawY + 2, drawY - 3};
        g2d.fillPolygon(hornRightX, hornRightY, 3);

        // Contorno da cabeça
        g2d.setColor(Color.BLACK);
        g2d.drawOval(drawX + 4, drawY + 2, headWidth, headHeight);
        g2d.drawPolygon(hornLeftX, hornLeftY, 3);
        g2d.drawPolygon(hornRightX, hornRightY, 3);

        // Cicatrizes na cabeça
        g2d.setColor(new Color(80, 15, 15));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(drawX + 10, drawY + 8, drawX + 16, drawY + 12);
        g2d.drawLine(drawX + 24, drawY + 6, drawX + 28, drawY + 14);

        // Olhos brilhantes e malignos com efeito de brilho
        int glowSize = eyeGlowIntensity / 10;

        // Brilho ao redor dos olhos
        g2d.setColor(new Color(255, 0, 0, eyeGlowIntensity));
        if (direction == 1) {
            g2d.fillOval(drawX + 16 - glowSize, drawY + 10 - glowSize, 12 + glowSize * 2, 10 + glowSize * 2);
            g2d.fillOval(drawX + 24 - glowSize, drawY + 10 - glowSize, 12 + glowSize * 2, 10 + glowSize * 2);
        } else {
            g2d.fillOval(drawX + 8 - glowSize, drawY + 10 - glowSize, 12 + glowSize * 2, 10 + glowSize * 2);
            g2d.fillOval(drawX + 16 - glowSize, drawY + 10 - glowSize, 12 + glowSize * 2, 10 + glowSize * 2);
        }

        // Olhos vermelhos brilhantes
        g2d.setColor(new Color(255, 50, 50));
        if (direction == 1) {
            g2d.fillOval(drawX + 18, drawY + 12, 8, 6);
            g2d.fillOval(drawX + 26, drawY + 12, 8, 6);
        } else {
            g2d.fillOval(drawX + 8, drawY + 12, 8, 6);
            g2d.fillOval(drawX + 16, drawY + 12, 8, 6);
        }

        // Pupilas verticais como de réptil
        g2d.setColor(Color.BLACK);
        if (direction == 1) {
            g2d.fillRect(drawX + 21, drawY + 13, 2, 4);
            g2d.fillRect(drawX + 29, drawY + 13, 2, 4);
        } else {
            g2d.fillRect(drawX + 11, drawY + 13, 2, 4);
            g2d.fillRect(drawX + 19, drawY + 13, 2, 4);
        }

        // Pontos de luz nos olhos para efeito mais sinistro
        g2d.setColor(new Color(255, 200, 200));
        if (direction == 1) {
            g2d.fillOval(drawX + 22, drawY + 13, 1, 1);
            g2d.fillOval(drawX + 30, drawY + 13, 1, 1);
        } else {
            g2d.fillOval(drawX + 12, drawY + 13, 1, 1);
            g2d.fillOval(drawX + 20, drawY + 13, 1, 1);
        }

        // Boca com dentes afiados
        g2d.setColor(new Color(40, 0, 0));
        g2d.fillArc(drawX + 14, drawY + 18, width - 28, 8, 0, -180);

        // Dentes pontiagudos
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            int toothX = drawX + 16 + (i * 4);
            int[] toothXPoints = {toothX, toothX + 2, toothX + 1};
            int[] toothYPoints = {drawY + 22, drawY + 22, drawY + 25};
            g2d.fillPolygon(toothXPoints, toothYPoints, 3);
        }

        // Contorno da boca
        g2d.setColor(Color.BLACK);
        g2d.drawArc(drawX + 14, drawY + 18, width - 28, 8, 0, -180);

        // Pernas mais musculosas e ameaçadoras
        g2d.setColor(new Color(90, 20, 20));
        // Perna esquerda
        g2d.fillPolygon(
                new int[]{drawX + 8, drawX + 16, drawX + 14, drawX + 6},
                new int[]{drawY + height - 18, drawY + height - 18, drawY + height, drawY + height},
                4
        );
        // Perna direita
        g2d.fillPolygon(
                new int[]{drawX + width - 16, drawX + width - 8, drawX + width - 6, drawX + width - 14},
                new int[]{drawY + height - 18, drawY + height - 18, drawY + height, drawY + height},
                4
        );

        // Contorno das pernas
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(
                new int[]{drawX + 8, drawX + 16, drawX + 14, drawX + 6},
                new int[]{drawY + height - 18, drawY + height - 18, drawY + height, drawY + height},
                4
        );
        g2d.drawPolygon(
                new int[]{drawX + width - 16, drawX + width - 8, drawX + width - 6, drawX + width - 14},
                new int[]{drawY + height - 18, drawY + height - 18, drawY + height, drawY + height},
                4
        );

        // Garras maiores e mais afiadas
        g2d.setColor(new Color(40, 40, 40));
        // Garra esquerda - 3 pontas
        g2d.fillPolygon(
                new int[]{drawX + 6, drawX + 10, drawX + 4},
                new int[]{drawY + height, drawY + height - 8, drawY + height - 6},
                3
        );
        g2d.fillPolygon(
                new int[]{drawX + 10, drawX + 14, drawX + 8},
                new int[]{drawY + height, drawY + height - 8, drawY + height - 6},
                3
        );
        g2d.fillPolygon(
                new int[]{drawX + 14, drawX + 18, drawX + 12},
                new int[]{drawY + height, drawY + height - 8, drawY + height - 6},
                3
        );

        // Garra direita - 3 pontas
        g2d.fillPolygon(
                new int[]{drawX + width - 6, drawX + width - 4, drawX + width - 10},
                new int[]{drawY + height, drawY + height - 6, drawY + height - 8},
                3
        );
        g2d.fillPolygon(
                new int[]{drawX + width - 10, drawX + width - 8, drawX + width - 14},
                new int[]{drawY + height, drawY + height - 6, drawY + height - 8},
                3
        );
        g2d.fillPolygon(
                new int[]{drawX + width - 14, drawX + width - 12, drawX + width - 18},
                new int[]{drawY + height, drawY + height - 6, drawY + height - 8},
                3
        );

        // Efeito de partículas sombrias flutuando
        g2d.setColor(new Color(100, 0, 0, 60));
        for (int i = 0; i < 5; i++) {
            int particleX = drawX + (int)(Math.sin(animationFrame * 0.05 + i) * 15) + width/2;
            int particleY = drawY + (int)(Math.cos(animationFrame * 0.07 + i) * 10) + height/2;
            g2d.fillOval(particleX, particleY, 2, 2);
        }

        // Reset stroke
        g2d.setStroke(new BasicStroke(1));

        // Hitbox (debug) - descomentado para testes
        // g2d.setColor(Color.GREEN);
        // g2d.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
}
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
