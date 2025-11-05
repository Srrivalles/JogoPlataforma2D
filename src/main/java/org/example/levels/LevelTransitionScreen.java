package org.example.levels;

import java.awt.*;

import org.example.fhysics.ScoreSystem;
import org.example.ui.GameConfig;

public class LevelTransitionScreen {

    public static void drawLevelComplete(Graphics2D g2d, LevelSystem levelSystem, ScoreSystem scoreSystem) {
        int currentLevel = levelSystem.getCurrentLevel();
        int transitionTimer = levelSystem.getTransitionTimer();
        boolean isLastLevel = levelSystem.isLastLevel();

        // Fundo escuro com fade
        float alpha = 1.0f - (transitionTimer / 180.0f);
        alpha = Math.max(0.7f, alpha);

        g2d.setColor(new Color(0, 0, 0, (int)(255 * alpha)));
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Efeitos de partículas de celebração
        drawCelebrationEffects(g2d, transitionTimer);

        // Título principal
        drawMainTitle(g2d, currentLevel, isLastLevel);

        // Estatísticas da fase
        drawLevelStats(g2d, scoreSystem, currentLevel);

        // Próxima fase ou conclusão
        drawNextLevelInfo(g2d, currentLevel, isLastLevel, transitionTimer);

        // Barra de progresso
        drawProgressBar(g2d, transitionTimer);
    }

    private static void drawCelebrationEffects(Graphics2D g2d, int timer) {
        // Fogos de artifício simples
        for (int i = 0; i < 20; i++) {
            double angle = (i * Math.PI * 2) / 20;
            int distance = (180 - timer) * 2;

            if (distance > 0 && distance < 300) {
                int x = GameConfig.SCREEN_WIDTH / 2 + (int)(Math.cos(angle) * distance);
                int y = GameConfig.SCREEN_HEIGHT / 2 + (int)(Math.sin(angle) * distance);

                Color[] colors = {Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
                g2d.setColor(colors[i % colors.length]);
                g2d.fillOval(x - 3, y - 3, 6, 6);
            }
        }

        // Estrelas cadentes
        for (int i = 0; i < 10; i++) {
            int x = (i * GameConfig.SCREEN_WIDTH / 10) + ((180 - timer) * 2);
            int y = 50 + (i * 30);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(x % GameConfig.SCREEN_WIDTH, y, 2, 2);
            g2d.drawLine(x % GameConfig.SCREEN_WIDTH, y,
                    (x - 20) % GameConfig.SCREEN_WIDTH, y - 5);
        }
    }

    private static void drawMainTitle(Graphics2D g2d, int currentLevel, boolean isLastLevel) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();

        String title = isLastLevel && currentLevel < 999 ? "JOGO COMPLETO!" : "FASE " + currentLevel + " COMPLETA!";
        int titleWidth = fm.stringWidth(title);

        // Sombra
        g2d.setColor(Color.BLACK);
        g2d.drawString(title, (GameConfig.SCREEN_WIDTH - titleWidth) / 2 + 3, 100 + 3);

        // Texto principal
        g2d.setColor(Color.YELLOW);
        g2d.drawString(title, (GameConfig.SCREEN_WIDTH - titleWidth) / 2, 100);

        // Subtítulo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g2d.getFontMetrics();

        String subtitle = isLastLevel && currentLevel < 999 ?
                "Parabéns! Você completou todas as fases!" :
                "Excelente trabalho, Cyber Runner!";

        int subtitleWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, (GameConfig.SCREEN_WIDTH - subtitleWidth) / 2, 130);
    }

    private static void drawLevelStats(Graphics2D g2d, ScoreSystem scoreSystem, int currentLevel) {
        int startY = 180;
        int lineHeight = 30;

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.CYAN);

        // Estatísticas
        String[] stats = {
                "PONTUAÇÃO: " + scoreSystem.getCurrentScore(),
                "ORBS COLETADOS: " + scoreSystem.getEnergyOrbsCollected(),
                "INIMIGOS DERROTADOS: " + scoreSystem.getEnemiesDefeated(),
                "FASE: " + currentLevel
        };

        for (int i = 0; i < stats.length; i++) {
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(stats[i]);

            // Background da estatística
            g2d.setColor(new Color(0, 100, 150, 100));
            g2d.fillRoundRect(
                    (GameConfig.SCREEN_WIDTH - textWidth) / 2 - 10,
                    startY + (i * lineHeight) - 20,
                    textWidth + 20,
                    25,
                    10, 10
            );

            // Texto
            g2d.setColor(Color.CYAN);
            g2d.drawString(stats[i], (GameConfig.SCREEN_WIDTH - textWidth) / 2, startY + (i * lineHeight));
        }

        // Bônus por fase
        drawLevelBonus(g2d, currentLevel, startY + (stats.length * lineHeight) + 20);
    }

    private static void drawLevelBonus(Graphics2D g2d, int level, int y) {
        int bonus = level * 1000; // Bônus base por fase

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();

        String bonusText = "BÔNUS DE FASE: +" + bonus + " pontos";
        int textWidth = fm.stringWidth(bonusText);

        // Efeito brilhante
        g2d.setColor(new Color(255, 255, 0, 150));
        g2d.fillRoundRect(
                (GameConfig.SCREEN_WIDTH - textWidth) / 2 - 15,
                y - 18,
                textWidth + 30,
                25,
                12, 12
        );

        g2d.setColor(Color.YELLOW);
        g2d.drawString(bonusText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, y);
    }

    private static void drawNextLevelInfo(Graphics2D g2d, int currentLevel, boolean isLastLevel, int timer) {
        int y = 400;

        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);

        String message;
        String instruction;

        if (isLastLevel && currentLevel < 999) {
            message = "MODO INFINITO DESBLOQUEADO!";
            instruction = "Prepare-se para o desafio final...";
        } else if (currentLevel >= 999) {
            message = "MODO INFINITO";
            instruction = "Continue explorando o cyberspace!";
        } else {
            message = "PRÓXIMA FASE: " + (currentLevel + 1);
            instruction = "Preparando nova área...";
        }

        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);

        g2d.drawString(message, (GameConfig.SCREEN_WIDTH - messageWidth) / 2, y);

        // Instrução
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.LIGHT_GRAY);
        fm = g2d.getFontMetrics();
        int instructionWidth = fm.stringWidth(instruction);

        g2d.drawString(instruction, (GameConfig.SCREEN_WIDTH - instructionWidth) / 2, y + 30);

        // Countdown
        if (timer > 0) {
            int seconds = (timer / 60) + 1;
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.YELLOW);
            fm = g2d.getFontMetrics();

            String countdownText = "Iniciando em " + seconds + "s";
            int countdownWidth = fm.stringWidth(countdownText);
            g2d.drawString(countdownText, (GameConfig.SCREEN_WIDTH - countdownWidth) / 2, y + 60);
        }
    }

    private static void drawProgressBar(Graphics2D g2d, int timer) {
        int barWidth = 400;
        int barHeight = 8;
        int barX = (GameConfig.SCREEN_WIDTH - barWidth) / 2;
        int barY = GameConfig.SCREEN_HEIGHT - 80;

        // Background da barra
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 4, 4);

        // Progresso
        float progress = 1.0f - (timer / 180.0f);
        int progressWidth = (int)(barWidth * progress);

        // Gradiente de progresso
        GradientPaint gradient = new GradientPaint(
                barX, barY,
                Color.GREEN,
                barX + progressWidth, barY,
                Color.YELLOW
        );

        g2d.setPaint(gradient);
        g2d.fillRoundRect(barX, barY, progressWidth, barHeight, 4, 4);

        // Texto de progresso
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();

        String progressText = (int)(progress * 100) + "%";
        int textWidth = fm.stringWidth(progressText);
        g2d.drawString(progressText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, barY + 25);
    }
}