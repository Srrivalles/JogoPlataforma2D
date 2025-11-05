package org.example.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

/**
 * Tela de Game Over com entrada de iniciais para high score
 */
public class GameOverScreen extends JPanel implements KeyListener {
    
    private HighScoreManager highScoreManager;
    private int finalScore;
    private boolean isHighScore;
    private String currentInitials = "";
    private int rankPosition = -1;
    private PropertyChangeSupport support;
    
    // Estados da tela
    private enum ScreenState {
        SHOWING_SCORE,      // Mostrando score final
        ENTERING_INITIALS,  // Digitando iniciais
        SHOWING_RANKING,    // Mostrando ranking final
        WAITING_RESTART     // Esperando restart
    }
    
    private ScreenState currentState = ScreenState.SHOWING_SCORE;
    private long stateStartTime;
    
    // Cores e fontes
    private final Color BG_COLOR = new Color(20, 20, 30);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color HIGHLIGHT_COLOR = new Color(0, 255, 255);
    private final Color HIGH_SCORE_COLOR = new Color(255, 215, 0); // Dourado
    
    private Font titleFont;
    private Font normalFont;
    private Font smallFont;
    
    public GameOverScreen() {
        this.highScoreManager = new HighScoreManager();
        this.support = new PropertyChangeSupport(this);
        initializeFonts();
        setBackground(BG_COLOR);
        setFocusable(true);
        addKeyListener(this);
        this.stateStartTime = System.currentTimeMillis();
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }
    
    private void initializeFonts() {
        try {
            titleFont = new Font("Arial", Font.BOLD, 48);
            normalFont = new Font("Arial", Font.BOLD, 24);
            smallFont = new Font("Arial", Font.PLAIN, 18);
        } catch (Exception e) {
            // Fallback para fontes padrão
            titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
            normalFont = new Font(Font.SANS_SERIF, Font.BOLD, 24);
            smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        }
    }
    
    public void showGameOver(int score) {
        this.finalScore = score;
        this.isHighScore = highScoreManager.isHighScore(score);
        this.currentInitials = "";
        this.rankPosition = -1;
        
        if (isHighScore) {
            this.currentState = ScreenState.SHOWING_SCORE;
        } else {
            this.currentState = ScreenState.SHOWING_RANKING;
        }
        
        this.stateStartTime = System.currentTimeMillis();
        
        
        
        requestFocusInWindow();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        
        // Background gradient
        GradientPaint gradient = new GradientPaint(0, 0, BG_COLOR, 0, height, new Color(40, 40, 60));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        switch (currentState) {
            case SHOWING_SCORE:
                drawScoreScreen(g2d, centerX, height);
                break;
            case ENTERING_INITIALS:
                drawInitialsScreen(g2d, centerX, height);
                break;
            case SHOWING_RANKING:
                drawRankingScreen(g2d, centerX, height);
                break;
            case WAITING_RESTART:
                drawRestartScreen(g2d, centerX, height);
                break;
        }
    }
    
    private void drawScoreScreen(Graphics2D g2d, int centerX, int height) {
        // Title
        g2d.setFont(titleFont);
        g2d.setColor(Color.RED);
        String title = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, centerX - fm.stringWidth(title) / 2, height / 4);
        
        // Score
        g2d.setFont(normalFont);
        g2d.setColor(TEXT_COLOR);
        String scoreText = "SCORE FINAL: " + String.format("%,d", finalScore);
        fm = g2d.getFontMetrics();
        g2d.drawString(scoreText, centerX - fm.stringWidth(scoreText) / 2, height / 3);
        
        // High score message
        if (isHighScore) {
            g2d.setColor(HIGH_SCORE_COLOR);
            String hsText = "🏆 NOVO HIGH SCORE! 🏆";
            fm = g2d.getFontMetrics();
            g2d.drawString(hsText, centerX - fm.stringWidth(hsText) / 2, height / 2 - 20);
            
            // Flash effect
            long elapsed = System.currentTimeMillis() - stateStartTime;
            if ((elapsed / 500) % 2 == 0) {
                g2d.setColor(HIGHLIGHT_COLOR);
                String continueText = "Pressione ENTER para continuar";
                g2d.setFont(smallFont);
                fm = g2d.getFontMetrics();
                g2d.drawString(continueText, centerX - fm.stringWidth(continueText) / 2, height / 2 + 20);
            }
        } else {
            g2d.setColor(TEXT_COLOR);
            String noHsText = "Não foi desta vez...";
            fm = g2d.getFontMetrics();
            g2d.drawString(noHsText, centerX - fm.stringWidth(noHsText) / 2, height / 2 - 20);
            
            g2d.setColor(HIGHLIGHT_COLOR);
            String continueText = "Pressione ENTER para ver ranking";
            g2d.setFont(smallFont);
            fm = g2d.getFontMetrics();
            g2d.drawString(continueText, centerX - fm.stringWidth(continueText) / 2, height / 2 + 20);
        }
    }
    
    private void drawInitialsScreen(Graphics2D g2d, int centerX, int height) {
        // Title
        g2d.setFont(titleFont);
        g2d.setColor(HIGH_SCORE_COLOR);
        String title = "HIGH SCORE!";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, centerX - fm.stringWidth(title) / 2, height / 4);
        
        // Instructions
        g2d.setFont(normalFont);
        g2d.setColor(TEXT_COLOR);
        String instructions = "Digite suas iniciais (3 letras):";
        fm = g2d.getFontMetrics();
        g2d.drawString(instructions, centerX - fm.stringWidth(instructions) / 2, height / 2 - 40);
        
        // Input box
        g2d.setColor(Color.WHITE);
        int boxWidth = 200;
        int boxHeight = 60;
        int boxX = centerX - boxWidth / 2;
        int boxY = height / 2 - 10;
        g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(boxX, boxY, boxWidth, boxHeight);
        
        // Current initials
        g2d.setFont(titleFont);
        String displayInitials = currentInitials + "_".repeat(3 - currentInitials.length());
        fm = g2d.getFontMetrics();
        int textX = boxX + (boxWidth - fm.stringWidth(displayInitials)) / 2;
        int textY = boxY + (boxHeight + fm.getAscent()) / 2;
        g2d.drawString(displayInitials, textX, textY);
        
        // Instructions
        g2d.setFont(smallFont);
        g2d.setColor(TEXT_COLOR);
        String hint = "Digite A-Z | BACKSPACE para apagar | ENTER para confirmar";
        fm = g2d.getFontMetrics();
        g2d.drawString(hint, centerX - fm.stringWidth(hint) / 2, height / 2 + 80);
    }
    
    private void drawRankingScreen(Graphics2D g2d, int centerX, int height) {
        // Title
        g2d.setFont(normalFont);
        g2d.setColor(HIGH_SCORE_COLOR);
        String title = "🏆 HIGH SCORES 🏆";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, centerX - fm.stringWidth(title) / 2, 80);
        
        // High scores list
        var scores = highScoreManager.getHighScores();
        g2d.setFont(smallFont);
        
        int startY = 130;
        int lineHeight = 30;
        
        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            var entry = scores.get(i);
            
            // Destacar se for o score recém-adicionado
            if (rankPosition == i + 1) {
                g2d.setColor(HIGHLIGHT_COLOR);
                g2d.fillRect(50, startY + (i * lineHeight) - 20, getWidth() - 100, 25);
                g2d.setColor(Color.BLACK);
            } else {
                g2d.setColor(TEXT_COLOR);
            }
            
            String rankText = String.format("%2d. %s ........ %,d", 
                i + 1, entry.initials, entry.score);
            g2d.drawString(rankText, centerX - 150, startY + (i * lineHeight));
        }
        
        // Instructions
        g2d.setColor(HIGHLIGHT_COLOR);
        g2d.setFont(smallFont);
        String restartText = "Pressione R para jogar novamente | ESC para sair";
        fm = g2d.getFontMetrics();
        g2d.drawString(restartText, centerX - fm.stringWidth(restartText) / 2, height - 50);
    }
    
    private void drawRestartScreen(Graphics2D g2d, int centerX, int height) {
        drawRankingScreen(g2d, centerX, height);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (currentState) {
            case SHOWING_SCORE:
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (isHighScore) {
                        currentState = ScreenState.ENTERING_INITIALS;
                    } else {
                        currentState = ScreenState.SHOWING_RANKING;
                    }
                    stateStartTime = System.currentTimeMillis();
                    repaint();
                }
                break;
                
            case ENTERING_INITIALS:
                handleInitialsInput(e);
                break;
                
            case SHOWING_RANKING:
            case WAITING_RESTART:
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // Voltar ao menu/lobby
                    firePropertyChange("backToMenu", false, true);
                }
                break;
        }
    }
    
    private void handleInitialsInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && currentInitials.length() == 3) {
            // Submeter score
            rankPosition = highScoreManager.addScore(currentInitials, finalScore);
            currentState = ScreenState.SHOWING_RANKING;
            stateStartTime = System.currentTimeMillis();
            
            // Mostrar ranking no console também
            highScoreManager.printHighScores();
            
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && currentInitials.length() > 0) {
            // Apagar última letra
            currentInitials = currentInitials.substring(0, currentInitials.length() - 1);
            
        } else if (currentInitials.length() < 3) {
            // Adicionar letra
            char keyChar = e.getKeyChar();
            if (Character.isLetter(keyChar)) {
                currentInitials += Character.toUpperCase(keyChar);
            }
        }
        
        repaint();
    }
    
    private void restartGame() {
        // Disparar evento para reiniciar o jogo
        firePropertyChange("restartGame", false, true);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    /**
     * Retorna o gerenciador de high scores
     */
    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }
}

