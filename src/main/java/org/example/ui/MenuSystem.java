package org.example.ui;

<<<<<<< HEAD
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
=======
import javax.swing.*;

import org.example.main.CyberRunnerGame;
import java.awt.*;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
<<<<<<< HEAD
import org.example.world.MapTheme;
import javax.swing.JPanel;

import org.example.main.CyberRunnerGame;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
public class MenuSystem extends JPanel {
    // Estados do menu
    public enum GameState {
        MAIN_MENU,
        PLAYING,
        PAUSED,
        GAME_OVER,
        HIGH_SCORES,
<<<<<<< HEAD
        CONTROLS,
        ENTERING_HIGH_SCORE,

        THEME_SELECTION
=======
        CONTROLS
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    // Dimensões usando GameConfig
    final int SCREEN_WIDTH = GameConfig.SCREEN_WIDTH;
    final int SCREEN_HEIGHT = GameConfig.SCREEN_HEIGHT;

    // Estado atual
    GameState currentState = GameState.MAIN_MENU;
    GamePanel gamePanel;
    CyberRunnerGame gameFrame;

    // Sistema de pontuação
    int currentScore = 0;
    int highScore = 0;
    int energyOrbsCollected = 0;
    int enemiesDefeated = 0;
    long gameStartTime = 0;
    long gameEndTime = 0;

<<<<<<< HEAD
    // Gerenciador de High Scores
    private HighScoreManager highScoreManager;
    // --- Variáveis para a tela de High Score Interativa ---
    private char[] newInitials = {'A', 'A', 'A'};
    private int currentInitialIndex = 0;
    private int newPlayerScore = 0;
    private int newScoreRank = -1;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    // Animações do menu
    int animationTimer = 0;
    float logoGlow = 0;
    float menuFloat = 0;
    ArrayList<MenuParticle> menuParticles = new ArrayList<>();

    // Controles do menu
    int selectedOption = 0;
    boolean upPressed = false;
    boolean downPressed = false;
    boolean enterPressed = false;
    boolean escapePressed = false;

    // Cores cyber usando GameConfig
    Color primaryCyan = GameConfig.PRIMARY_COLOR;
    Color secondaryPink = GameConfig.SECONDARY_COLOR;
    Color accentYellow = GameConfig.ACCENT_COLOR;
    Color darkBg = new Color(15, 15, 25);
    Color lightBg = new Color(25, 15, 35);

    // Fontes melhoradas
    Font titleFont;
    Font menuFont;
    Font scoreFont;
    Font subtitleFont;

    // Opções dos menus
<<<<<<< HEAD
    // Mude as opções do menu principal
    String[] mainOptions = {"SELECT THEME", "CONTROLS", "HIGH SCORES", "EXIT"}; // Mude "START GAME"

    // Crie opções para a nova tela
    String[] themeOptions = {"CYBERPUNK", "HALLOWEEN", "BACK"};
=======
    String[] mainOptions = {"START GAME", "CONTROLS", "HIGH SCORES", "EXIT"};
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    String[] controlsOptions = {"BACK"};
    String[] gameOverOptions = {"RESTART (R)", "MAIN MENU (L)"};

    public MenuSystem() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(darkBg);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

<<<<<<< HEAD
        // Inicializar gerenciador de High Scores
        this.highScoreManager = new HighScoreManager();

=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        // Inicializar fontes
        try {
            titleFont = new Font("Arial", Font.BOLD, 56);
            subtitleFont = new Font("Arial", Font.ITALIC, 20);
            menuFont = new Font("Arial", Font.BOLD, 28);
            scoreFont = new Font("Courier New", Font.BOLD, 18);
        } catch (Exception e) {
            titleFont = new Font(Font.SANS_SERIF, Font.BOLD, 56);
            subtitleFont = new Font(Font.SANS_SERIF, Font.ITALIC, 20);
            menuFont = new Font(Font.SANS_SERIF, Font.BOLD, 28);
            scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 18);
        }

        setupControls();
        initMenuParticles();
        startAnimationLoop();
<<<<<<< HEAD
        
        // Tocar música de menu
        try {
            org.example.audio.AudioManager.playMenuMusic();
        } catch (Exception e) {
            // Ignorar erros de áudio
        }
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public void setGameFrame(CyberRunnerGame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private void setupControls() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
<<<<<<< HEAD
                if (currentState == GameState.ENTERING_HIGH_SCORE) {
                    handleEnterHighScoreInput(e);
                    return; // Impede que o input normal do menu seja processado
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                            if (!upPressed) {
                                navigateUp();
                                upPressed = true;
                            }
=======
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        if (!upPressed) {
                            navigateUp();
                            upPressed = true;
                        }
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        if (!downPressed) {
                            navigateDown();
                            downPressed = true;
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SPACE:
                        if (!enterPressed) {
                            handleSelection();
                            enterPressed = true;
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (!escapePressed) {
                            handleEscape();
                            escapePressed = true;
                        }
                        break;
                    case KeyEvent.VK_R:
                        if (currentState == GameState.GAME_OVER) {
                            restartGame();
                        }
                        break;
                    case KeyEvent.VK_L:
                        if (currentState == GameState.GAME_OVER) {
                            backToMainMenu();
                        }
                        break;
<<<<<<< HEAD
                    case KeyEvent.VK_F11:
                        // Alternar tela cheia via GameFrame
                        if (gameFrame != null) {
                            gameFrame.toggleFullScreen();
                        }
                        break;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        upPressed = false;
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        downPressed = false;
                        break;
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SPACE:
                        enterPressed = false;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        escapePressed = false;
                        break;
                }
            }
        });

        // Controles de mouse
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e);
            }
        });
    }

    private void navigateUp() {
        // int maxOptions = getMaxOptionsForCurrentState(); // Removido - não utilizado
        selectedOption = Math.max(0, selectedOption - 1);
        repaint();
    }

<<<<<<< HEAD
    private void handleThemeSelection() {
        MapTheme chosenTheme = MapTheme.CYBERPUNK; // Padrão

        switch (selectedOption) {
            case 0: // CYBERPUNK
                chosenTheme = MapTheme.CYBERPUNK;
                startGame(chosenTheme); // Inicia o jogo com o tema
                break;
            case 1: // HALLOWEEN
                chosenTheme = MapTheme.HALLOWEEN;
                startGame(chosenTheme); // Inicia o jogo com o tema
                break;
            case 2: // BACK
                showMainMenu();
                break;
        }
    }

=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private void navigateDown() {
        int maxOptions = getMaxOptionsForCurrentState();
        selectedOption = Math.min(maxOptions - 1, selectedOption + 1);
        repaint();
    }

    private int getMaxOptionsForCurrentState() {
        switch (currentState) {
            case MAIN_MENU:
                return mainOptions.length;
<<<<<<< HEAD
            case THEME_SELECTION:
                return themeOptions.length;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            case CONTROLS:
                return controlsOptions.length;
            case GAME_OVER:
                return gameOverOptions.length;
            case HIGH_SCORES:
                return 1;
            default:
                return 1;
        }
    }

    private void handleSelection() {
        switch (currentState) {
            case MAIN_MENU:
                handleMainMenuSelection();
                break;
<<<<<<< HEAD
            case THEME_SELECTION: // <--- ADICIONE ESTE CASO
                handleThemeSelection();
                break;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            case CONTROLS:
                if (selectedOption == 0) {
                    showMainMenu();
                }
                break;
            case GAME_OVER:
                handleGameOverSelection();
                break;
            case HIGH_SCORES:
                showMainMenu();
                break;
            case PLAYING:
            case PAUSED:
                // Não há seleção durante o jogo
                break;
        }
    }

    private void handleMainMenuSelection() {
        switch (selectedOption) {
            case 0:
<<<<<<< HEAD
                showThemeSelectionScreen();
=======
                startGame();
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                break;
            case 1:
                showControlsScreen();
                break;
            case 2:
                showHighScoresScreen();
                break;
            case 3:
                System.exit(0);
                break;
        }
    }

    private void handleGameOverSelection() {
        switch (selectedOption) {
            case 0:
                restartGame();
                break;
            case 1:
                backToMainMenu();
                break;
        }
    }

    private void handleEscape() {
        switch (currentState) {
            case CONTROLS:
            case HIGH_SCORES:
            case GAME_OVER:
                showMainMenu();
                break;
            case MAIN_MENU:
            case PLAYING:
            case PAUSED:
<<<<<<< HEAD
=======
                // Comportamento específico para cada estado
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                break;
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (currentState == GameState.MAIN_MENU) {
            int mouseY = e.getY();
            int itemHeight = 60;
            int startY = SCREEN_HEIGHT / 2 - 20;

            for (int i = 0; i < mainOptions.length; i++) {
                int itemY = startY + i * itemHeight;
                if (mouseY >= itemY - 20 && mouseY <= itemY + 20) {
                    selectedOption = i;
                    handleSelection();
                    break;
                }
            }
        }
    }

    private void handleMouseMove(MouseEvent e) {
        if (currentState == GameState.MAIN_MENU) {
            int mouseY = e.getY();
            int itemHeight = 60;
            int startY = SCREEN_HEIGHT / 2 - 20;
            int oldSelection = selectedOption;

            for (int i = 0; i < mainOptions.length; i++) {
                int itemY = startY + i * itemHeight;
                if (mouseY >= itemY - 20 && mouseY <= itemY + 20) {
                    selectedOption = i;
                    break;
                }
            }

            if (oldSelection != selectedOption) {
                repaint();
            }
        }
    }

    // Métodos de navegação entre telas
    public void showMainMenu() {
        currentState = GameState.MAIN_MENU;
        selectedOption = 0;
        repaint();
    }

    public void showControlsScreen() {
        currentState = GameState.CONTROLS;
        selectedOption = 0;
        repaint();
    }

    public void showHighScoresScreen() {
        currentState = GameState.HIGH_SCORES;
        selectedOption = 0;
        repaint();
    }

    public void showGameOverScreen(int score, int orbs, int enemies) {
        currentState = GameState.GAME_OVER;
        selectedOption = 0;
        currentScore = score;
        energyOrbsCollected = orbs;
        enemiesDefeated = enemies;
        gameEndTime = System.currentTimeMillis();

<<<<<<< HEAD
        if (highScoreManager.isHighScore(score)) {
            newPlayerScore = score;
            currentState = GameState.ENTERING_HIGH_SCORE;
            currentInitialIndex = 0;
            newInitials = new char[]{'A', 'A', 'A'}; // Reseta as iniciais
        }
        // Atualizar high score local para exibição na tela de Game Over
        var scores = highScoreManager.getHighScores();
        if (!scores.isEmpty()) {
            highScore = scores.get(0).score;
=======
        if (currentScore > highScore) {
            highScore = currentScore;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }

        repaint();
    }

<<<<<<< HEAD
    private void startGame(MapTheme theme) { // Adicione o parâmetro
        currentState = GameState.PLAYING;
        // ... resto do seu código de startGame ...

        if (gameFrame != null) {
            // AQUI ESTÁ A CONEXÃO!
            gameFrame.startNewGame(theme); // Passamos o tema para o frame principal
        } else {
            // Lógica antiga (pode não funcionar mais dependendo da sua estrutura)
=======
    private void startGame() {
        currentState = GameState.PLAYING;
        gameStartTime = System.currentTimeMillis();
        currentScore = 0;
        energyOrbsCollected = 0;
        enemiesDefeated = 0;

        if (gameFrame != null) {
            gameFrame.startNewGame();
        } else {
            Container parent = getParent();
            if (parent != null) {
                CardLayout cl = (CardLayout) parent.getLayout();
                cl.show(parent, "GAME");

                if (gamePanel != null) {
                    gamePanel.requestFocusInWindow();
                    gamePanel.resetGame();
                    gamePanel.startGameLoop();
                }
            }
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }

    private void restartGame() {
<<<<<<< HEAD

        showThemeSelectionScreen();
    }
    public void showThemeSelectionScreen() {
        currentState = GameState.THEME_SELECTION;
        selectedOption = 0;
        repaint();
    }
=======
        if (gameFrame != null) {
            gameFrame.startNewGame();
        } else {
            startGame();
        }
    }

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private void backToMainMenu() {
        showMainMenu();
        if (gameFrame != null) {
            gameFrame.returnToMainMenu();
        }
    }

    public void triggerGameOver(int finalScore, int orbs, int enemies) {
        showGameOverScreen(finalScore, orbs, enemies);
    }

    public void updateScore(int score) {
        this.currentScore = score;
    }

    public void updateStats(int orbs, int enemies) {
        this.energyOrbsCollected = orbs;
        this.enemiesDefeated = enemies;
    }

    public GameState getCurrentState() {
        return currentState;
    }
<<<<<<< HEAD
    
    /**
     * Retorna o gerenciador de High Scores
     */
    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

    // Sistema de partículas e animação
    private void initMenuParticles() {
        for (int i = 0; i < 80; i++) {
            menuParticles.add(new MenuParticle());
        }
    }

    private void startAnimationLoop() {
        Thread animationThread = new Thread(() -> {
            while (true) {
                animationTimer++;
                logoGlow = (float)(Math.sin(animationTimer * 0.03) * 0.5 + 0.5);
                menuFloat = (float)(Math.sin(animationTimer * 0.02) * 3);

                // Atualizar partículas
                for (MenuParticle particle : menuParticles) {
                    particle.update();
                }

                // Spawn novas partículas
                if (animationTimer % 30 == 0) {
                    menuParticles.add(new MenuParticle());
                    menuParticles.removeIf(p -> p.life <= 0);
                    while (menuParticles.size() > 120) {
                        menuParticles.remove(0);
                    }
                }

                repaint();

                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        animationThread.setDaemon(true);
        animationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        switch (currentState) {
            case MAIN_MENU:
                drawMainMenu(g2d);
                break;
<<<<<<< HEAD
            case THEME_SELECTION: // <--- ADICIONE ESTE CASO
                drawThemeSelectionScreen(g2d);
                break;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            case CONTROLS:
                drawControlsScreen(g2d);
                break;
            case HIGH_SCORES:
                drawHighScoresScreen(g2d);
                break;
<<<<<<< HEAD
            case ENTERING_HIGH_SCORE:
                drawEnterHighScoreScreen(g2d);
                break;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            case GAME_OVER:
                drawGameOverScreen(g2d);
                break;
            case PLAYING:
                // Não desenha nada durante o jogo
                break;
            case PAUSED:
                // Não desenha nada durante pausa
                break;
        }

        g2d.dispose();
    }

<<<<<<< HEAD
    private void drawThemeSelectionScreen(Graphics2D g2d) {
        // Reutilize o fundo e o grid do menu principal
        drawMainMenu(g2d);

        // Caixa de fundo para legibilidade
        g2d.setColor(new Color(10, 10, 20, 220));
        g2d.fillRoundRect(SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 120, 400, 240, 30, 30);
        g2d.setColor(primaryCyan);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 120, 400, 240, 30, 30);

        // Título
        g2d.setFont(menuFont.deriveFont(32f));
        g2d.setColor(accentYellow);
        String title = "CHOOSE YOUR THEME";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, SCREEN_WIDTH / 2 - fm.stringWidth(title) / 2, SCREEN_HEIGHT / 2 - 70);

        // Desenhar as opções de tema
        int menuStartY = SCREEN_HEIGHT / 2 - 10;
        int itemHeight = 50;
        for (int i = 0; i < themeOptions.length; i++) {
            g2d.setFont(menuFont);
            if (i == selectedOption) {
                g2d.setColor(accentYellow);
                String selectedText = "> " + themeOptions[i] + " <";
                g2d.drawString(selectedText, SCREEN_WIDTH / 2 - fm.stringWidth(selectedText) / 2, menuStartY + i * itemHeight);
            } else {
                g2d.setColor(primaryCyan);
                g2d.drawString(themeOptions[i], SCREEN_WIDTH / 2 - fm.stringWidth(themeOptions[i]) / 2, menuStartY + i * itemHeight);
            }
        }
    }
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private void drawMainMenu(Graphics2D g2d) {
        // Fundo com gradiente radial
        Point2D center = new Point2D.Float(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);
        float radius = Math.max(SCREEN_WIDTH, SCREEN_HEIGHT);

        RadialGradientPaint bgGradient = new RadialGradientPaint(
                center, radius,
                new float[]{0f, 0.7f, 1f},
                new Color[]{
                        new Color(25, 25, 45),
                        new Color(15, 15, 30),
                        new Color(5, 5, 15)
                }
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Grid animado
        drawAnimatedGrid(g2d);

        // Partículas de fundo
        for (MenuParticle particle : menuParticles) {
            particle.draw(g2d);
        }

        // Logo centralizado
        drawCenteredLogo(g2d);

        // Menu principal centralizado
        drawCenteredMainMenu(g2d);

        // Instruções
        drawInstructions(g2d);
    }

<<<<<<< HEAD
    // NOVO MÉTODO PARA DESENHAR A TELA DE INSERIR INICIAIS
    private void drawEnterHighScoreScreen(Graphics2D g2d) {
        // Fundo escuro com um brilho central
        RadialGradientPaint bgGradient = new RadialGradientPaint(
                new Point2D.Float(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f),
                SCREEN_HEIGHT,
                new float[]{0f, 1f},
                new Color[]{new Color(60, 40, 90), darkBg}
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        drawAnimatedGrid(g2d); // Reutilizamos o grid animado

        // Título parabenizando o jogador
        g2d.setFont(titleFont.deriveFont(48f));
        g2d.setColor(accentYellow);
        String title = "NEW HIGH SCORE!";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);

        // Efeito de pulsação no título
        float pulse = (float) (1.0 + Math.sin(animationTimer * 0.1) * 0.05);
        g2d.translate(SCREEN_WIDTH / 2, 120);
        g2d.scale(pulse, pulse);
        g2d.drawString(title, -titleWidth / 2, 0);
        g2d.scale(1/pulse, 1/pulse); // Reseta a escala
        g2d.translate(-SCREEN_WIDTH/2, -120); // Reseta a translação

        // Mostra a pontuação alcançada
        g2d.setFont(menuFont);
        g2d.setColor(Color.WHITE);
        String scoreText = String.format("SCORE: %,d", newPlayerScore);
        int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, (SCREEN_WIDTH - scoreWidth) / 2, 220);

        // Instruções
        g2d.setFont(scoreFont);
        g2d.setColor(primaryCyan);
        g2d.drawString("ENTER YOUR INITIALS", (SCREEN_WIDTH - g2d.getFontMetrics().stringWidth("ENTER YOUR INITIALS")) / 2, 300);

        // Desenha as 3 caixas de iniciais
        for (int i = 0; i < 3; i++) {
            int boxX = (SCREEN_WIDTH / 2) - 150 + (i * 100);
            int boxY = 350;

            // A caixa da letra selecionada pisca
            if (i == currentInitialIndex && (animationTimer % 30 < 15)) {
                g2d.setColor(accentYellow);
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setColor(secondaryPink);
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.drawRoundRect(boxX, boxY, 80, 100, 15, 15);

            // Desenha a letra dentro da caixa
            g2d.setFont(titleFont.deriveFont(80f));
            String initial = String.valueOf(newInitials[i]);
            int initialWidth = g2d.getFontMetrics().stringWidth(initial);
            g2d.drawString(initial, boxX + (80 - initialWidth) / 2, boxY + 80);
        }

        // Instruções de controle
        g2d.setFont(scoreFont);
        g2d.setColor(primaryCyan);
        g2d.drawString("↑↓ CHANGE LETTER", (SCREEN_WIDTH - g2d.getFontMetrics().stringWidth("↑↓ CHANGE LETTER")) / 2, 500);
        g2d.drawString("←→ MOVE CURSOR", (SCREEN_WIDTH - g2d.getFontMetrics().stringWidth("←→ MOVE CURSOR")) / 2, 525);
        g2d.drawString("ENTER TO CONFIRM", (SCREEN_WIDTH - g2d.getFontMetrics().stringWidth("ENTER TO CONFIRM")) / 2, 550);
    }

    // NOVO MÉTODO PARA LIDAR COM O INPUT NA TELA DE INICIAIS
    private void handleEnterHighScoreInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                newInitials[currentInitialIndex]++;
                if (newInitials[currentInitialIndex] > 'Z') {
                    newInitials[currentInitialIndex] = 'A';
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                newInitials[currentInitialIndex]--;
                if (newInitials[currentInitialIndex] < 'A') {
                    newInitials[currentInitialIndex] = 'Z';
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                currentInitialIndex = Math.max(0, currentInitialIndex - 1);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                currentInitialIndex = Math.min(2, currentInitialIndex + 1);
                break;
            case KeyEvent.VK_ENTER:
                // Finaliza a inserção, salva o score e vai para a tela de ranking
                String initials = new String(newInitials);
                newScoreRank = highScoreManager.addScore(initials, newPlayerScore);
                showHighScoresScreen();
                // Reseta a pontuação do "último jogo" para não poluir a tela
                currentScore = 0;
                break;
        }
        repaint();
    }

    private void drawAnimatedGrid(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1));

        // Linhas verticais mais sutis
        for (int x = 0; x < SCREEN_WIDTH; x += 80) {
            float alpha = (float)(Math.sin((x + animationTimer) * 0.005) * 0.2 + 0.3);
            g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), (int)(15 * alpha)));
            g2d.drawLine(x, 0, x, SCREEN_HEIGHT);
        }

        // Linhas horizontais mais sutis
        for (int y = 0; y < SCREEN_HEIGHT; y += 80) {
            float alpha = (float)(Math.sin((y + animationTimer) * 0.005) * 0.2 + 0.3);
            g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), (int)(15 * alpha)));
            g2d.drawLine(0, y, SCREEN_WIDTH, y);
        }

        // Pontos de intersecção mais sutis
        g2d.setColor(new Color(accentYellow.getRed(), accentYellow.getGreen(), accentYellow.getBlue(), 30));
        for (int x = 0; x < SCREEN_WIDTH; x += 80) {
            for (int y = 0; y < SCREEN_HEIGHT; y += 80) {
                if ((x + y + animationTimer * 2) % 400 < 30) {
                    g2d.fillOval(x - 1, y - 1, 2, 2);
=======
    private void drawAnimatedGrid(Graphics2D g2d) {
        g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), 15));
        g2d.setStroke(new BasicStroke(1));

        // Linhas verticais com animação
        for (int x = 0; x < SCREEN_WIDTH; x += 60) {
            float alpha = (float)(Math.sin((x + animationTimer) * 0.01) * 0.3 + 0.7);
            g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), (int)(20 * alpha)));
            g2d.drawLine(x, 0, x, SCREEN_HEIGHT);
        }

        // Linhas horizontais com animação
        for (int y = 0; y < SCREEN_HEIGHT; y += 60) {
            float alpha = (float)(Math.sin((y + animationTimer) * 0.01) * 0.3 + 0.7);
            g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), (int)(20 * alpha)));
            g2d.drawLine(0, y, SCREEN_WIDTH, y);
        }

        // Pontos de intersecção brilhantes
        g2d.setColor(new Color(accentYellow.getRed(), accentYellow.getGreen(), accentYellow.getBlue(), 60));
        for (int x = 0; x < SCREEN_WIDTH; x += 60) {
            for (int y = 0; y < SCREEN_HEIGHT; y += 60) {
                if ((x + y + animationTimer * 2) % 300 < 50) {
                    g2d.fillOval(x - 2, y - 2, 4, 4);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                }
            }
        }
    }

    private void drawCenteredLogo(Graphics2D g2d) {
        int logoY = (int)(SCREEN_HEIGHT / 4 + menuFloat);

        // Efeito de brilho ao redor do logo
        if (logoGlow > 0.8) {
            g2d.setColor(new Color(255, 255, 255, (int)((logoGlow - 0.8) * 255 * 5)));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRoundRect(SCREEN_WIDTH/2 - 220, logoY - 40, 440, 120, 20, 20);
        }

        // Sombra do logo
        g2d.setFont(titleFont);
        g2d.setColor(new Color(0, 0, 0, 100));
        FontMetrics fm = g2d.getFontMetrics();

        String title1 = "CYBER";
        String title2 = "RUNNER";

        int title1Width = fm.stringWidth(title1);
        int title2Width = fm.stringWidth(title2);

        g2d.drawString(title1, SCREEN_WIDTH/2 - title1Width/2 + 3, logoY + 3);
        g2d.drawString(title2, SCREEN_WIDTH/2 - title2Width/2 + 3, logoY + 60 + 3);

        // Texto principal do logo
        Color logoColor = new Color(
                (int)(primaryCyan.getRed() + (accentYellow.getRed() - primaryCyan.getRed()) * logoGlow),
                (int)(primaryCyan.getGreen() + (accentYellow.getGreen() - primaryCyan.getGreen()) * logoGlow),
                (int)(primaryCyan.getBlue() + (accentYellow.getBlue() - primaryCyan.getBlue()) * logoGlow)
        );

        g2d.setColor(logoColor);
        g2d.drawString(title1, SCREEN_WIDTH/2 - title1Width/2, logoY);
        g2d.drawString(title2, SCREEN_WIDTH/2 - title2Width/2, logoY + 60);

        // Subtitle centralizado
        g2d.setFont(subtitleFont);
        g2d.setColor(secondaryPink);
        String subtitle = "THE ULTIMATE CYBER ADVENTURE";
        FontMetrics subFm = g2d.getFontMetrics();
        int subtitleWidth = subFm.stringWidth(subtitle);
        g2d.drawString(subtitle, SCREEN_WIDTH/2 - subtitleWidth/2, logoY + 100);
    }

    private void drawCenteredMainMenu(Graphics2D g2d) {
        int menuStartY = SCREEN_HEIGHT / 2 - 20;
        int itemHeight = 60;
        int itemWidth = 350;
        int itemX = SCREEN_WIDTH / 2 - itemWidth / 2;

        for (int i = 0; i < mainOptions.length; i++) {
            int itemY = (int)(menuStartY + i * itemHeight + (i == selectedOption ? Math.sin(animationTimer * 0.1) * 2 : 0));

            boolean isSelected = (i == selectedOption);
            Color itemColor = isSelected ? accentYellow : primaryCyan;
            Color bgColor = new Color(itemColor.getRed(), itemColor.getGreen(), itemColor.getBlue(),
                    isSelected ? 80 : 30);

            // Fundo do item
            g2d.setColor(bgColor);
            g2d.fillRoundRect(itemX, itemY - 25, itemWidth, 50, 25, 25);

            // Borda do item selecionado
            if (isSelected) {
                float borderAlpha = (float)(Math.sin(animationTimer * 0.15) * 0.3 + 0.7);
                g2d.setColor(new Color(itemColor.getRed(), itemColor.getGreen(), itemColor.getBlue(),
                        (int)(255 * borderAlpha)));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(itemX, itemY - 25, itemWidth, 50, 25, 25);

                // Efeito de brilho nas bordas
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(new Color(255, 255, 255, (int)(100 * borderAlpha)));
                g2d.drawRoundRect(itemX + 2, itemY - 23, itemWidth - 4, 46, 23, 23);
            } else {
                g2d.setColor(new Color(itemColor.getRed(), itemColor.getGreen(), itemColor.getBlue(), 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(itemX, itemY - 25, itemWidth, 50, 25, 25);
            }

            // Texto do item centralizado
            g2d.setFont(menuFont);
            g2d.setColor(itemColor);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(mainOptions[i]);
            g2d.drawString(mainOptions[i], SCREEN_WIDTH/2 - textWidth/2, itemY + 8);

            // Setas laterais para item selecionado
            if (isSelected) {
                int arrowOffset = (int)(Math.sin(animationTimer * 0.2) * 3);
                g2d.setColor(accentYellow);

                // Seta esquerda
                int[] leftArrowX = {itemX - 20 - arrowOffset, itemX - 10 - arrowOffset, itemX - 20 - arrowOffset};
                int[] leftArrowY = {itemY - 5, itemY, itemY + 5};
                g2d.fillPolygon(leftArrowX, leftArrowY, 3);

                // Seta direita
                int[] rightArrowX = {itemX + itemWidth + 20 + arrowOffset, itemX + itemWidth + 10 + arrowOffset,
                        itemX + itemWidth + 20 + arrowOffset};
                int[] rightArrowY = {itemY - 5, itemY, itemY + 5};
                g2d.fillPolygon(rightArrowX, rightArrowY, 3);
            }
        }
    }

    private void drawInstructions(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(new Color(primaryCyan.getRed(), primaryCyan.getGreen(), primaryCyan.getBlue(), 180));

        String[] instructions = {
                "Use ↑↓ or WASD to navigate",
                "ENTER or SPACE to select",
                "ESC to go back"
        };

        int instructionY = SCREEN_HEIGHT - 80;
        for (String instruction : instructions) {
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(instruction);
            g2d.drawString(instruction, SCREEN_WIDTH/2 - textWidth/2, instructionY);
            instructionY += 20;
        }
    }

    private void drawGameOverScreen(Graphics2D g2d) {
        // Fundo escuro
        GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(40, 0, 0),
                0, SCREEN_HEIGHT, new Color(80, 20, 20)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Título GAME OVER
        g2d.setFont(titleFont);
        String gameOverText = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);

        // Efeito de tremor
        int shake = (int)(Math.sin(animationTimer * 0.5) * 3);

        // Sombra
        g2d.setColor(new Color(255, 0, 0, 100));
        g2d.drawString(gameOverText, (SCREEN_WIDTH - textWidth) / 2 + shake + 2, 150 + shake + 2);

        // Texto principal
        g2d.setColor(Color.RED);
        g2d.drawString(gameOverText, (SCREEN_WIDTH - textWidth) / 2 + shake, 150 + shake);

        // Estatísticas
        g2d.setFont(scoreFont);
        g2d.setColor(primaryCyan);

        long gameTime = gameEndTime > gameStartTime ? (gameEndTime - gameStartTime) / 1000 : 0;
        String[] stats = {
                "FINAL SCORE: " + currentScore,
                "ENERGY ORBS: " + energyOrbsCollected,
                "ENEMIES DEFEATED: " + enemiesDefeated,
                "TIME: " + formatTime(gameTime),
                "",
                "HIGH SCORE: " + highScore
        };

        int startY = 250;
        for (int i = 0; i < stats.length; i++) {
            if (stats[i].isEmpty()) continue;

            Color statColor = stats[i].contains("HIGH SCORE") ? accentYellow : primaryCyan;
            g2d.setColor(statColor);

            FontMetrics statFm = g2d.getFontMetrics();
            int statWidth = statFm.stringWidth(stats[i]);
            g2d.drawString(stats[i], (SCREEN_WIDTH - statWidth) / 2, startY + i * 25);
        }

<<<<<<< HEAD
        // Menu de opções (retângulo de destaque centralizado com base no texto)
=======
        // Menu de opções
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        startY = 450;
        for (int i = 0; i < gameOverOptions.length; i++) {
            Color textColor = (i == selectedOption) ? accentYellow : primaryCyan;

<<<<<<< HEAD
            g2d.setFont(menuFont);
            FontMetrics optFm = g2d.getFontMetrics();
            int optWidth = optFm.stringWidth(gameOverOptions[i]);

            // Calcular largura e posição do retângulo com padding
            int paddingX = 40; // margem lateral
            int itemWidth = Math.max(400, optWidth + paddingX);
            int itemHeight = 30;
            int itemX = (SCREEN_WIDTH - itemWidth) / 2;
            int itemY = startY + i * 40 - 15; // centralizar verticalmente em torno da baseline

            if (i == selectedOption) {
                g2d.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 50));
                g2d.fillRoundRect(itemX, itemY, itemWidth, itemHeight, 15, 15);

                g2d.setColor(textColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(itemX, itemY, itemWidth, itemHeight, 15, 15);
            }

            g2d.setColor(textColor);
=======
            if (i == selectedOption) {
                g2d.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 50));
                g2d.fillRoundRect(200, startY + i * 40 - 15, 400, 30, 15, 15);

                g2d.setColor(textColor);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(200, startY + i * 40 - 15, 400, 30, 15, 15);
            }

            g2d.setFont(menuFont);
            g2d.setColor(textColor);
            FontMetrics optFm = g2d.getFontMetrics();
            int optWidth = optFm.stringWidth(gameOverOptions[i]);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            g2d.drawString(gameOverOptions[i], (SCREEN_WIDTH - optWidth) / 2, startY + i * 40);
        }

        // Instruções
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(secondaryPink);
        String instructions = "Press R to restart • L for main menu • Use arrows and ENTER";
        FontMetrics instrFm = g2d.getFontMetrics();
        int instrWidth = instrFm.stringWidth(instructions);
        g2d.drawString(instructions, (SCREEN_WIDTH - instrWidth) / 2, SCREEN_HEIGHT - 30);
    }

    private void drawControlsScreen(Graphics2D g2d) {
        // Fundo gradiente
        GradientPaint bgGradient = new GradientPaint(
                0, 0, darkBg,
                0, SCREEN_HEIGHT, lightBg
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Grid de fundo
        drawAnimatedGrid(g2d);

        // Título
        g2d.setFont(titleFont);
        g2d.setColor(primaryCyan);
        String title = "CONTROLS";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (SCREEN_WIDTH - titleWidth) / 2, 100);

        // Controles do jogo
        String[] controls = {
                "MOVEMENT",
<<<<<<< HEAD
                "A/D or Arrow Keys - Move Left/Right",
                "SPACE or W or Up Arrow - Jump",
=======
                "A/D or ← → - Move Left/Right",
                "SPACE or W or ↑ - Jump",
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                "",
                "GAME ACTIONS",
                "ESC - Pause Game",
                "R - Restart (Game Over)",
                "L - Main Menu (Game Over)",
                "",
                "MENU NAVIGATION",
<<<<<<< HEAD
                "Arrow Keys or W/S - Navigate",
=======
                "↑↓ or W/S - Navigate",
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                "ENTER or SPACE - Select",
                "ESC - Back"
        };

        int startY = 180;
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].isEmpty()) {
                startY += 20;
                continue;
            }

            if (controls[i].equals("MOVEMENT") || controls[i].equals("GAME ACTIONS") ||
                    controls[i].equals("MENU NAVIGATION")) {
                g2d.setColor(primaryCyan);
                g2d.setFont(new Font("Arial", Font.BOLD, 26));

                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(200, startY + 10, SCREEN_WIDTH - 200, startY + 10);
            } else {
                g2d.setColor(new Color(200, 200, 255));
                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            }

            FontMetrics controlFm = g2d.getFontMetrics();
            int controlWidth = controlFm.stringWidth(controls[i]);
            g2d.drawString(controls[i], (SCREEN_WIDTH - controlWidth) / 2, startY);

            startY += 35;
        }

<<<<<<< HEAD
        // Botão voltar melhorado
=======
        // Botão voltar
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        int buttonY = SCREEN_HEIGHT - 100;
        boolean backSelected = (selectedOption == 0);

        Color buttonColor = backSelected ? accentYellow : primaryCyan;

<<<<<<< HEAD
        // Fundo do botão
        if (backSelected) {
            g2d.setColor(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), 30));
            g2d.fillRoundRect(SCREEN_WIDTH/2 - 120, buttonY - 25, 240, 50, 25, 25);
        }

        // Borda do botão
        g2d.setColor(buttonColor);
        g2d.setStroke(new BasicStroke(backSelected ? 3 : 2));
        g2d.drawRoundRect(SCREEN_WIDTH/2 - 120, buttonY - 25, 240, 50, 25, 25);

        // Texto do botão
=======
        if (backSelected) {
            g2d.setColor(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), 50));
            g2d.fillRoundRect(300, buttonY - 20, 200, 40, 20, 20);

            g2d.setColor(buttonColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(300, buttonY - 20, 200, 40, 20, 20);
        }

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        g2d.setFont(menuFont);
        g2d.setColor(buttonColor);
        String backText = "BACK TO MENU";
        FontMetrics backFm = g2d.getFontMetrics();
        int backWidth = backFm.stringWidth(backText);
<<<<<<< HEAD
        g2d.drawString(backText, (SCREEN_WIDTH - backWidth) / 2, buttonY + 5);
=======
        g2d.drawString(backText, (SCREEN_WIDTH - backWidth) / 2, buttonY);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    private void drawHighScoresScreen(Graphics2D g2d) {
        // Fundo gradiente
        GradientPaint bgGradient = new GradientPaint(
                0, 0, darkBg,
                0, SCREEN_HEIGHT, new Color(25, 25, 45)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Grid animado
        drawAnimatedGrid(g2d);

        // Título
        g2d.setFont(titleFont);
        g2d.setColor(accentYellow);
<<<<<<< HEAD
        String title = "🏆 HIGH SCORES 🏆";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (SCREEN_WIDTH - titleWidth) / 2, 80);

        // Efeito de brilho no título
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawString(title, (SCREEN_WIDTH - titleWidth) / 2 - 1, 79);

        // Lista de High Scores
        var scores = highScoreManager.getHighScores();
        g2d.setFont(new Font("Consolas", Font.BOLD, 28)); // Fonte mais "arcade"

        int startY = 160;
        int lineHeight = 40;

        if (scores.isEmpty()) {
            // Mensagem se não há scores (mantém a lógica original)
        } else {
            // Desenhar ranking com ANIMAÇÃO
            for (int i = 0; i < scores.size(); i++) {
                var entry = scores.get(i);

                if (i == newScoreRank - 1) {
                    float alpha = (float)(Math.sin(animationTimer * 0.2) * 0.5 + 0.5); // Pisca suavemente
                    g2d.setColor(new Color(accentYellow.getRed(), accentYellow.getGreen(), accentYellow.getBlue(), (int)(alpha * 100)));
                    g2d.fillRoundRect(150, startY + (i * lineHeight) - 25, SCREEN_WIDTH - 300, 35, 20, 20);
                }

                Color rankColor;
                if (i == 0) rankColor = new Color(255, 215, 0); // Dourado
                else if (i == 1) rankColor = new Color(192, 192, 192); // Prata
                else if (i == 2) rankColor = new Color(205, 127, 50); // Bronze
                else rankColor = primaryCyan;

                g2d.setColor(rankColor);
                String rankText = String.format("%2d.", i + 1);
                g2d.drawString(rankText, 200, startY + (i * lineHeight));

                g2d.setColor(accentYellow);
                g2d.drawString(entry.initials, 300, startY + (i * lineHeight));

                g2d.setColor(rankColor);
                String scoreText = String.format("%,d", entry.score);
                int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
                g2d.drawString(scoreText, SCREEN_WIDTH - scoreWidth - 200, startY + (i * lineHeight));

                // Adiciona um "NEW!" piscando ao lado do novo score
                if (i == newScoreRank - 1) {
                    if (animationTimer % 20 < 10) { // Pisca rápido
                        g2d.setColor(Color.ORANGE);
                        g2d.setFont(new Font("Consolas", Font.BOLD, 20));
                        g2d.drawString("NEW!", SCREEN_WIDTH - 180, startY + (i * lineHeight));
                        g2d.setFont(new Font("Consolas", Font.BOLD, 28)); // Reseta a fonte
                    }
                }
            }
        }

        // Botão voltar (a lógica original está ótima)
        int buttonY = SCREEN_HEIGHT - 100;
        boolean backSelected = (selectedOption == 0);
        Color buttonColor = backSelected ? accentYellow : primaryCyan;
        if (backSelected) {
            g2d.setColor(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), 30));
            g2d.fillRoundRect(SCREEN_WIDTH/2 - 120, buttonY - 25, 240, 50, 25, 25);
        }
        g2d.setColor(buttonColor);
        g2d.setStroke(new BasicStroke(backSelected ? 3 : 2));
        g2d.drawRoundRect(SCREEN_WIDTH/2 - 120, buttonY - 25, 240, 50, 25, 25);
        g2d.setFont(menuFont);
        g2d.setColor(buttonColor);
        String backText = "BACK TO MENU";
        int backWidth = g2d.getFontMetrics().stringWidth(backText);
        g2d.drawString(backText, (SCREEN_WIDTH - backWidth) / 2, buttonY + 5);
    }

=======
        String title = "HIGH SCORES";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (SCREEN_WIDTH - titleWidth) / 2, 100);

        // Efeito de brilho no título
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawString(title, (SCREEN_WIDTH - titleWidth) / 2 - 1, 99);

        // Pontuação atual
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.setColor(primaryCyan);

        String currentScoreText = "CURRENT HIGH SCORE";
        String scoreValue = String.valueOf(highScore);

        FontMetrics scoreFm = g2d.getFontMetrics();
        int currentWidth = scoreFm.stringWidth(currentScoreText);

        g2d.drawString(currentScoreText, (SCREEN_WIDTH - currentWidth) / 2, 200);

        // Valor da pontuação com efeito especial
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.setColor(accentYellow);

        // Efeito de pulsação
        float pulse = (float)(Math.sin(animationTimer * 0.1) * 0.2 + 1);
        Font pulsedFont = g2d.getFont().deriveFont(g2d.getFont().getSize() * pulse);
        g2d.setFont(pulsedFont);

        FontMetrics pulsedFm = g2d.getFontMetrics();
        int pulsedWidth = pulsedFm.stringWidth(scoreValue);

        // Sombra da pontuação
        g2d.setColor(new Color(255, 255, 0, 100));
        g2d.drawString(scoreValue, (SCREEN_WIDTH - pulsedWidth) / 2 + 2, 272);

        // Pontuação principal
        g2d.setColor(accentYellow);
        g2d.drawString(scoreValue, (SCREEN_WIDTH - pulsedWidth) / 2, 270);

        // Estatísticas detalhadas se houver jogo jogado
        if (currentScore > 0 || energyOrbsCollected > 0 || enemiesDefeated > 0) {
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(secondaryPink);

            String lastGameTitle = "LAST GAME STATS";
            FontMetrics lastFm = g2d.getFontMetrics();
            int lastWidth = lastFm.stringWidth(lastGameTitle);
            g2d.drawString(lastGameTitle, (SCREEN_WIDTH - lastWidth) / 2, 350);

            g2d.setFont(scoreFont);
            g2d.setColor(primaryCyan);

            String[] lastStats = {
                    "Score: " + currentScore,
                    "Energy Orbs: " + energyOrbsCollected,
                    "Enemies Defeated: " + enemiesDefeated
            };

            int statsY = 380;
            for (String stat : lastStats) {
                FontMetrics statFm = g2d.getFontMetrics();
                int statWidth = statFm.stringWidth(stat);
                g2d.drawString(stat, (SCREEN_WIDTH - statWidth) / 2, statsY);
                statsY += 25;
            }
        } else {
            // Mensagem motivacional se não há pontuação
            g2d.setFont(new Font("Arial", Font.ITALIC, 24));
            g2d.setColor(secondaryPink);

            String motivational = "Play your first game to set a high score!";
            FontMetrics motivFm = g2d.getFontMetrics();
            int motivWidth = motivFm.stringWidth(motivational);
            g2d.drawString(motivational, (SCREEN_WIDTH - motivWidth) / 2, 350);
        }

        // Botão voltar
        int buttonY = SCREEN_HEIGHT - 100;
        boolean backSelected = (selectedOption == 0);

        Color buttonColor = backSelected ? accentYellow : primaryCyan;

        if (backSelected) {
            g2d.setColor(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), 50));
            g2d.fillRoundRect(300, buttonY - 20, 200, 40, 20, 20);

            g2d.setColor(buttonColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(300, buttonY - 20, 200, 40, 20, 20);
        }

        g2d.setFont(menuFont);
        g2d.setColor(buttonColor);
        String backText = "BACK TO MENU";
        FontMetrics backFm = g2d.getFontMetrics();
        int backWidth = backFm.stringWidth(backText);
        g2d.drawString(backText, (SCREEN_WIDTH - backWidth) / 2, buttonY);
    }

    // Método auxiliar para formatar tempo
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    // Classe para partículas do menu
    private class MenuParticle {
        float x, y;
        float vx, vy;
        float life;
        float maxLife;
        Color color;
        float size;

        public MenuParticle() {
            // Spawn aleatório nas bordas
            if (Math.random() < 0.5) {
                x = (float)(Math.random() * SCREEN_WIDTH);
                y = Math.random() < 0.5 ? -10 : SCREEN_HEIGHT + 10;
            } else {
                x = Math.random() < 0.5 ? -10 : SCREEN_WIDTH + 10;
                y = (float)(Math.random() * SCREEN_HEIGHT);
            }

            // Velocidade direcionada ao centro com variação
            float centerX = SCREEN_WIDTH / 2f;
            float centerY = SCREEN_HEIGHT / 2f;
            float angle = (float)Math.atan2(centerY - y, centerX - x);
            float speed = (float)(Math.random() * 2 + 0.5);

            vx = (float)(Math.cos(angle) * speed);
            vy = (float)(Math.sin(angle) * speed);

            // Adicionar movimento perpendicular para tornar mais interessante
            vx += (Math.random() - 0.5) * 2;
            vy += (Math.random() - 0.5) * 2;

            maxLife = (float)(Math.random() * 300 + 200);
            life = maxLife;

            // Cores cyber aleatórias
            int colorChoice = (int)(Math.random() * 3);
            switch (colorChoice) {
                case 0:
                    color = primaryCyan;
                    break;
                case 1:
                    color = secondaryPink;
                    break;
                default:
                    color = accentYellow;
                    break;
            }

            size = (float)(Math.random() * 3 + 1);
        }

        public void update() {
            x += vx;
            y += vy;
            life--;

            // Aplicar uma força sutil em direção ao centro
            float centerX = SCREEN_WIDTH / 2f;
            float centerY = SCREEN_HEIGHT / 2f;
            float dx = centerX - x;
            float dy = centerY - y;
            float distance = (float)Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                vx += (dx / distance) * 0.02f;
                vy += (dy / distance) * 0.02f;
            }

            // Aplicar arrasto
            vx *= 0.995f;
            vy *= 0.995f;

            // Remover se saiu muito das bordas
            if (x < -50 || x > SCREEN_WIDTH + 50 || y < -50 || y > SCREEN_HEIGHT + 50) {
                life = 0;
            }
        }

        public void draw(Graphics2D g2d) {
            if (life <= 0) return;

            float alpha = Math.min(1.0f, life / maxLife);
            alpha = alpha * alpha; // Fade mais suave

            g2d.setColor(new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int)(255 * alpha * 0.7f)
            ));

            // Desenhar partícula com efeito de brilho
            int drawSize = (int)(size * alpha);
            g2d.fillOval((int)x - drawSize/2, (int)y - drawSize/2, drawSize, drawSize);

            // Efeito de brilho central
            if (alpha > 0.5f) {
                g2d.setColor(new Color(255, 255, 255, (int)(100 * alpha)));
                g2d.fillOval((int)x - 1, (int)y - 1, 2, 2);
            }

            // Linha de rastro sutil
            if (alpha > 0.3f) {
                g2d.setColor(new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        (int)(50 * alpha)
                ));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine((int)x, (int)y, (int)(x - vx * 3), (int)(y - vy * 3));
            }
        }
    }
}