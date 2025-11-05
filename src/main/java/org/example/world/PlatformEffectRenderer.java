package org.example.world;

import org.example.world.ReactivePlatformSystem.ReactivePlatform;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Renderizador de efeitos visuais para plataformas reativas
 */
public class PlatformEffectRenderer {
    
    /**
     * Renderiza todos os efeitos das plataformas reativas
     */
    public static void renderPlatformEffects(Graphics2D g2d, ArrayList<ReactivePlatform> reactivePlatforms) {
        for (ReactivePlatform reactivePlatform : reactivePlatforms) {
            renderPlatformEffect(g2d, reactivePlatform);
        }
    }
    
    /**
     * Renderiza o efeito de uma plataforma específica
     */
    private static void renderPlatformEffect(Graphics2D g2d, ReactivePlatform reactivePlatform) {
        Platform platform = reactivePlatform.platform;
        int x = (int) platform.x;
        int y = (int) platform.y;
        int width = (int) platform.width;
        int height = (int) platform.height;
        
        // Salvar configurações originais
        Composite originalComposite = g2d.getComposite();
        Stroke originalStroke = g2d.getStroke();
        
        switch (reactivePlatform.type) {
            case ENERGY_PULSE:
                renderEnergyPulseEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case QUANTUM_SHIFT:
                renderQuantumShiftEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case GRAVITY_FIELD:
                renderGravityFieldEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case TELEPORT_PAD:
                renderTeleportPadEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case SHIELD_GENERATOR:
                renderShieldGeneratorEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case SPEED_BOOST:
                renderSpeedBoostEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case INVISIBLE:
                renderInvisibleEffect(g2d, x, y, width, height, reactivePlatform);
                break;
            case MOVING_BRIDGE:
                renderMovingBridgeEffect(g2d, x, y, width, height, reactivePlatform);
                break;
        }
        
        // Restaurar configurações originais
        g2d.setComposite(originalComposite);
        g2d.setStroke(originalStroke);
    }
    
    /**
     * Efeito de pulso de energia
     */
    private static void renderEnergyPulseEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Brilho externo
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.3f));
        g2d.setColor(new Color(0, 255, 255, 100));
        g2d.fillRect(x - 10, y - 10, width + 20, height + 20);
        
        // Brilho interno
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.6f));
        g2d.setColor(new Color(0, 200, 255, 150));
        g2d.fillRect(x - 5, y - 5, width + 10, height + 10);
        
        // Borda pulsante
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(0, 255, 255, 200));
        g2d.setStroke(new BasicStroke(2 + alpha * 2));
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Efeito de deslocamento quântico
     */
    private static void renderQuantumShiftEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Efeito de deslocamento
        for (int i = 0; i < 3; i++) {
            float offset = (float) Math.sin(reactivePlatform.animationTimer * 2 + i) * 5;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.3f / (i + 1)));
            g2d.setColor(new Color(255, 0, 255, 100));
            g2d.fillRect(x + (int) offset, y + (int) offset, width, height);
        }
        
        // Borda quântica
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(255, 0, 255, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Efeito de campo gravitacional
     */
    private static void renderGravityFieldEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Campo gravitacional circular
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int radius = Math.max(width, height) / 2 + 20;
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.4f));
        g2d.setColor(new Color(100, 100, 255, 80));
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Linhas de campo
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.8f));
        g2d.setColor(new Color(150, 150, 255, 150));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 8; i++) {
            double angle = (reactivePlatform.animationTimer + i * Math.PI / 4) * 2;
            int endX = centerX + (int) (Math.cos(angle) * radius);
            int endY = centerY + (int) (Math.sin(angle) * radius);
            g2d.drawLine(centerX, centerY, endX, endY);
        }
    }
    
    /**
     * Efeito de plataforma de teleporte
     */
    private static void renderTeleportPadEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Símbolo de teleporte
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.6f));
        g2d.setColor(new Color(255, 255, 0, 120));
        g2d.fillOval(centerX - 15, centerY - 15, 30, 30);
        
        // Anel de teleporte
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(255, 255, 0, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - 20, centerY - 20, 40, 40);
        
        // Efeito de rotação
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < 4; i++) {
            double angle = reactivePlatform.animationTimer * 3 + i * Math.PI / 2;
            int endX = centerX + (int) (Math.cos(angle) * 25);
            int endY = centerY + (int) (Math.sin(angle) * 25);
            g2d.drawLine(centerX, centerY, endX, endY);
        }
    }
    
    /**
     * Efeito de gerador de escudo
     */
    private static void renderShieldGeneratorEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Escudo hexagonal
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int radius = Math.min(width, height) / 2;
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.5f));
        g2d.setColor(new Color(0, 255, 0, 100));
        
        // Desenhar hexágono
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            xPoints[i] = centerX + (int) (Math.cos(angle) * radius);
            yPoints[i] = centerY + (int) (Math.sin(angle) * radius);
        }
        g2d.fillPolygon(xPoints, yPoints, 6);
        
        // Borda do escudo
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(0, 255, 0, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 6);
    }
    
    /**
     * Efeito de boost de velocidade
     */
    private static void renderSpeedBoostEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Linhas de velocidade
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.8f));
        g2d.setColor(new Color(255, 100, 0, 150));
        g2d.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < 5; i++) {
            int startX = x + i * width / 5;
            int endX = startX + (int) (Math.sin(reactivePlatform.animationTimer * 4 + i) * 20);
            g2d.drawLine(startX, y, endX, y + height);
        }
        
        // Símbolo de velocidade
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(255, 150, 0, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Efeito de invisibilidade
     */
    private static void renderInvisibleEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        if (alpha > 0.3f) {
            // Contorno fantasmagórico
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.4f));
            g2d.setColor(new Color(200, 200, 200, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, width, height);
            
            // Efeito de distorção
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.2f));
            g2d.setColor(new Color(150, 150, 255, 80));
            g2d.fillRect(x, y, width, height);
        }
    }
    
    /**
     * Efeito de ponte móvel
     */
    private static void renderMovingBridgeEffect(Graphics2D g2d, int x, int y, int width, int height, ReactivePlatform reactivePlatform) {
        float alpha = reactivePlatform.glowIntensity;
        
        // Efeito de movimento
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.6f));
        g2d.setColor(new Color(100, 100, 100, 120));
        g2d.fillRect(x, y, width, height);
        
        // Linhas de movimento
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(150, 150, 150, 200));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 3; i++) {
            int lineY = y + height / 4 + i * height / 4;
            g2d.drawLine(x, lineY, x + width, lineY);
        }
        
        // Borda da ponte
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);
    }
}
