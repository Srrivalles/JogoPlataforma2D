package org.example.levels;

import java.awt.*;
import java.util.ArrayList;

import org.example.objects.Player;

public class LevelGoal {
    private int x, y;
    private int width = 80;
    private int height = 120;
    private int levelNumber;
    private Rectangle hitbox;

    // Efeitos visuais
    private int animationTimer = 0;
    private ArrayList<GoalParticle> particles = new ArrayList<>();
    private boolean playerNearby = false;
    private float pulseIntensity = 1.0f;

    // Cores baseadas na fase
    private Color primaryColor;
    private Color secondaryColor;
    private Color beamColor;

    public LevelGoal(int x, int y, int levelNumber) {
        this.x = x;
        this.y = y;
        this.levelNumber = levelNumber;
        this.hitbox = new Rectangle(x - width/2, y - height, width, height);

        setupColors();
    }

    private void setupColors() {
        switch (levelNumber) {
            case 1:
                primaryColor = new Color(0, 255, 255);    // Ciano
                secondaryColor = new Color(0, 200, 255);
                beamColor = new Color(100, 255, 255);
                break;
            case 2:
                primaryColor = new Color(255, 100, 255);  // Magenta
                secondaryColor = new Color(255, 50, 200);
                beamColor = new Color(255, 150, 255);
                break;
            case 3:
                primaryColor = new Color(100, 255, 100);  // Verde
                secondaryColor = new Color(50, 255, 150);
                beamColor = new Color(150, 255, 200);
                break;
            default:
                primaryColor = new Color(255, 255, 100);  // Dourado
                secondaryColor = new Color(255, 200, 50);
                beamColor = new Color(255, 255, 150);
                break;
        }
    }

    public void update() {
        animationTimer++;

        // Atualizar partículas
        updateParticles();

        // Spawn de novas partículas
        if (animationTimer % 10 == 0) {
            spawnParticles();
        }

        // Atualizar intensidade do pulso baseado na proximidade do player
        if (playerNearby) {
            pulseIntensity = Math.min(2.0f, pulseIntensity + 0.05f);
        } else {
            pulseIntensity = Math.max(1.0f, pulseIntensity - 0.02f);
        }
    }

    private void updateParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            GoalParticle particle = particles.get(i);
            particle.update();
            if (particle.isDead()) {
                particles.remove(i);
            }
        }
    }

    private void spawnParticles() {
        // Partículas subindo
        for (int i = 0; i < 3; i++) {
            particles.add(new GoalParticle(
                    x + (int)(Math.random() * width - width/2),
                    y,
                    primaryColor,
                    60
            ));
        }

        // Partículas orbitais se player estiver próximo
        if (playerNearby) {
            for (int i = 0; i < 2; i++) {
                double angle = Math.random() * Math.PI * 2;
                int orbitalX = (int)(x + Math.cos(angle) * 40);
                int orbitalY = (int)(y - height/2 + Math.sin(angle) * 30);

                particles.add(new GoalParticle(
                        orbitalX, orbitalY,
                        beamColor,
                        40
                ));
            }
        }
    }

    public void draw(Graphics2D g2d) {
        // Desenhar partículas primeiro
        for (GoalParticle particle : particles) {
            particle.draw(g2d);
        }

        // Feixe de luz subindo
        drawLightBeam(g2d);

        // Base/pedestal
        drawBase(g2d);

        // Portal/núcleo principal
        drawCore(g2d);

        // Efeitos de borda
        drawBorderEffects(g2d);

        // Indicador de nível
        drawLevelIndicator(g2d);

        // Texto de instrução se player estiver próximo
        if (playerNearby) {
            drawInstructionText(g2d);
        }
    }

    private void drawLightBeam(Graphics2D g2d) {
        // Feixe subindo até o topo da tela
        int beamWidth = (int)(20 * pulseIntensity);
        int alpha = (int)(100 + Math.sin(animationTimer * 0.2) * 50);

        Color beamGradient = new Color(
                beamColor.getRed(),
                beamColor.getGreen(),
                beamColor.getBlue(),
                Math.max(0, Math.min(255, alpha))
        );

        GradientPaint gradient = new GradientPaint(
                x, y - height,
                beamGradient,
                x, 0,
                new Color(beamGradient.getRed(), beamGradient.getGreen(), beamGradient.getBlue(), 0)
        );

        g2d.setPaint(gradient);
        g2d.fillRect(x - beamWidth/2, 0, beamWidth, y - height);
    }

    private void drawBase(Graphics2D g2d) {
        // Plataforma base
        g2d.setColor(new Color(100, 100, 120));
        g2d.fillRoundRect(x - width/2, y - 20, width, 20, 10, 10);

        // Detalhes tecnológicos na base
        g2d.setColor(primaryColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x - width/2, y - 20, width, 20, 10, 10);

        // Luzes na base
        for (int i = 0; i < 3; i++) {
            int lightX = x - 30 + (i * 30);
            g2d.setColor(primaryColor);
            g2d.fillOval(lightX - 3, y - 15, 6, 6);
        }
    }

    private void drawCore(Graphics2D g2d) {
        // Núcleo principal pulsante
        int coreSize = (int)(40 * pulseIntensity);
        float phase = animationTimer * 0.1f;

        // Múltiplas camadas do núcleo
        for (int layer = 3; layer >= 0; layer--) {
            int layerSize = coreSize + (layer * 8);
            int alpha = 255 - (layer * 60);

            Color layerColor = new Color(
                    primaryColor.getRed(),
                    primaryColor.getGreen(),
                    primaryColor.getBlue(),
                    Math.max(20, alpha)
            );

            g2d.setColor(layerColor);
            g2d.fillOval(
                    x - layerSize/2,
                    y - height/2 - layerSize/2,
                    layerSize,
                    layerSize
            );
        }

        // Núcleo central brilhante
        g2d.setColor(Color.WHITE);
        int innerCore = (int)(15 + Math.sin(phase * 2) * 5);
        g2d.fillOval(
                x - innerCore/2,
                y - height/2 - innerCore/2,
                innerCore,
                innerCore
        );
    }

    private void drawBorderEffects(Graphics2D g2d) {
        // Anéis de energia orbitais
        g2d.setStroke(new BasicStroke(3));

        for (int ring = 0; ring < 2; ring++) {
            double ringAngle = (animationTimer * 0.02) + (ring * Math.PI);
            int ringRadius = 50 + (ring * 15);

            Color ringColor = new Color(
                    secondaryColor.getRed(),
                    secondaryColor.getGreen(),
                    secondaryColor.getBlue(),
                    150 - (ring * 50)
            );
            g2d.setColor(ringColor);

            // Desenhar segmentos do anel
            for (int segment = 0; segment < 8; segment++) {
                double segmentAngle = ringAngle + (segment * Math.PI / 4);
                int startAngle = (int)(Math.toDegrees(segmentAngle));

                g2d.drawArc(
                        x - ringRadius,
                        y - height/2 - ringRadius,
                        ringRadius * 2,
                        ringRadius * 2,
                        startAngle,
                        30
                );
            }
        }
    }

    private void drawLevelIndicator(Graphics2D g2d) {
        // Número da fase
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();

        String levelText = "FASE " + levelNumber;
        int textWidth = fm.stringWidth(levelText);

        g2d.drawString(levelText, x - textWidth/2, y - height - 10);

        // Subtítulo
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        fm = g2d.getFontMetrics();

        String subtitle = levelNumber <= 3 ? "OBJETIVO" : "INFINITO";
        textWidth = fm.stringWidth(subtitle);

        g2d.setColor(primaryColor);
        g2d.drawString(subtitle, x - textWidth/2, y - height + 5);
    }

    private void drawInstructionText(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();

        String instruction = "Entre na zona para completar!";
        int textWidth = fm.stringWidth(instruction);

        // Background do texto
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(x - textWidth/2 - 10, y + 30, textWidth + 20, 25, 10, 10);

        g2d.setColor(Color.YELLOW);
        g2d.drawString(instruction, x - textWidth/2, y + 47);
    }

    public boolean checkPlayerReached(Player player) {
        boolean playerInZone = hitbox.intersects(player.getHitbox());

        // Atualizar se player está próximo (para efeitos visuais)
        float distance = getDistanceToPlayer(player);
        playerNearby = distance < 120;

        return playerInZone;
    }

    private float getDistanceToPlayer(Player player) {
        float dx = (player.x + player.width/2) - x;
        float dy = (player.y + player.height/2) - (y - height/2);
        return (float)Math.sqrt(dx*dx + dy*dy);
    }

    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public int getX() { return x; }
    public int getY() { return y; }

    // Classe interna para partículas
    private class GoalParticle {
        float x, y;
        float vx, vy;
        Color color;
        int life, maxLife;

        public GoalParticle(int x, int y, Color color, int maxLife) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.life = this.maxLife = maxLife;

            // Movimento para cima
            this.vx = (float)(Math.random() * 2 - 1) * 0.5f;
            this.vy = -1 - (float)(Math.random() * 2);
        }

        public void update() {
            x += vx;
            y += vy;
            life--;

            // Desaceleração
            vy *= 0.98f;
            vx *= 0.99f;
        }

        public void draw(Graphics2D g2d) {
            float alpha = (float)life / maxLife;
            Color fadeColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int)(255 * alpha)
            );

            g2d.setColor(fadeColor);
            int size = (int)(3 * alpha) + 1;
            g2d.fillOval((int)x - size/2, (int)y - size/2, size, size);
        }

        public boolean isDead() {
            return life <= 0;
        }
    }
}