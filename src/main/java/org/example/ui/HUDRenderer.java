package org.example.ui;

import java.awt.*;

import org.example.fhysics.ScoreSystem;
import org.example.inputs.CameraController;
import org.example.objects.Player;
import org.example.entities.PlayerEntity;

public class HUDRenderer {

    public static void drawFuturisticHUD(Graphics2D g2d, Player player, CameraController camera) {
        // HUD simplificado estilo Super Mario - apenas informações essenciais
        drawSimpleHUD(g2d, player);
    }
    
    public static void drawSimpleHUD(Graphics2D g2d, Player player) {
        // HUD limpo e organizado como Super Mario
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Score no canto superior direito
        g2d.setColor(Color.WHITE);
        g2d.drawString("SCORE", GameConfig.SCREEN_WIDTH - 120, 25);
        
        // Energy bar no canto superior direito
        drawEnergyBar(g2d, player);
        
        // Controles básicos no canto inferior esquerdo (apenas quando necessário)
        if (player.getCurrentEnergy() < 50) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.drawString("SHIFT/X = DASH", 10, GameConfig.SCREEN_HEIGHT - 20);
        }
    }

    public static void drawGameHUD(Graphics2D g2d, ScoreSystem scoreSystem) {
        // Score simples no canto superior direito
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.WHITE);
        
        // Fundo semi-transparente para o score
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(GameConfig.SCREEN_WIDTH - 150, 5, 145, 35, 5, 5);
        
        // Score
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("%06d", scoreSystem.getCurrentScore()), 
                      GameConfig.SCREEN_WIDTH - 140, 28);
        
        // Multiplicador se > 1
        if (scoreSystem.getScoreMultiplier() > 1) {
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("x" + scoreSystem.getScoreMultiplier(), 
                          GameConfig.SCREEN_WIDTH - 30, 28);
        }
    }

    public static void drawDebugInfo(Graphics2D g2d, Player player, CameraController camera,
                                     int platformCount, int enemyCount, int orbCount) {
        // Debug: desenhar um retângulo vermelho para testar se está renderizando
        g2d.setColor(Color.RED);
        g2d.fillRect(50, 50, 100, 100);
        g2d.setColor(Color.WHITE);
        g2d.drawString("DEBUG: Se você vê isso, o render funciona!", 60, 110);
<<<<<<< HEAD
=======

        // Debug info no console
        System.out.println("Renderizando - Player: [" + player.x + ", " + player.y + "] Camera: [" +
                camera.getCameraX() + ", " + camera.getCameraY() + "]");
        System.out.println("Plataformas: " + platformCount + ", Inimigos: " + enemyCount + ", Orbs: " + orbCount);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public static void drawBackground(Graphics2D g2d) {
        // Background gradient - usando cores futurísticas
        GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(10, 10, 30),           // Azul escuro no topo
                0, GameConfig.SCREEN_HEIGHT, new Color(0, 50, 100)  // Azul mais claro na base
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    }

    public static void drawErrorScreen(Graphics2D g2d, String errorMessage) {
<<<<<<< HEAD
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            errorMessage = "Erro desconhecido na renderização";
        }
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("ERRO DE RENDERIZAÇÃO", 50, 50);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Verifique o console para detalhes", 50, 80);
        g2d.drawString(errorMessage, 50, 110);
    }

    public static void drawLivesHUD(Graphics2D g2d, PlayerEntity player) {
        // Vidas simples no canto superior esquerdo
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        
        // Fundo semi-transparente
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(10, 5, 120, 35, 5, 5);
        
        // Texto "LIVES"
        g2d.setColor(Color.WHITE);
        g2d.drawString("LIVES", 15, 25);
        
        // Ícones de vida simples
        int iconX = 80;
        int iconY = 10;
        int iconSize = 20;
        
        for (int i = 0; i < 3; i++) {
            if (i < player.getLives()) {
                // Vida ativa - cor azul
                g2d.setColor(new Color(0, 150, 255));
            } else {
                // Vida perdida - cor cinza
                g2d.setColor(new Color(100, 100, 100));
            }
            
            // Desenhar ícone simples (círculo)
            g2d.fillOval(iconX + (i * 25), iconY, iconSize, iconSize);
            
            // Borda
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(iconX + (i * 25), iconY, iconSize, iconSize);
        }
    }
    
    // Método removido - não utilizado
    
    private static void drawEnergyBar(Graphics2D g2d, Player player) {
        // Barra de energia no canto superior direito
        int barX = GameConfig.SCREEN_WIDTH - 200;
        int barY = 50;
        int barWidth = 150;
        int barHeight = 20;
        
        // Fundo da barra
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Borda da barra
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Energia atual
        int energy = player.getCurrentEnergy();
        int energyWidth = (int)((energy / 100.0) * (barWidth - 4));
        
        // Cor da energia baseada no nível
        Color energyColor;
        if (energy > 60) {
            energyColor = new Color(0, 255, 0); // Verde
        } else if (energy > 30) {
            energyColor = new Color(255, 255, 0); // Amarelo
        } else {
            energyColor = new Color(255, 0, 0); // Vermelho
        }
        
        g2d.setColor(energyColor);
        g2d.fillRoundRect(barX + 2, barY + 2, energyWidth, barHeight - 4, 3, 3);
        
        // Texto "ENERGY"
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("ENERGY", barX, barY - 5);
    }
}