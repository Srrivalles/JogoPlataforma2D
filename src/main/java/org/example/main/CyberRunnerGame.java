package org.example.main;

import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.example.audio.AudioManager;
import org.example.ui.GamePanel;
import org.example.ui.MenuSystem;


import org.example.world.MapTheme;
public class CyberRunnerGame extends JFrame implements KeyListener {

    // Gerenciador de layouts
    CardLayout cardLayout;
    JPanel mainPanel;

    // Sistemas do jogo
    MenuSystem menuSystem;
    GamePanel gamePanel;

    // Estados
    boolean gameActive = false;

    // Sistema de tela cheia
    private boolean isFullScreen = false;
    private int windowedWidth = 1280;
    private int windowedHeight = 720;
    private int windowedX = 100;
    private int windowedY = 100;

    public CyberRunnerGame() {

        // Configurar janela principal
        this.setTitle("Cyber Runner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        setWindowIcon();

        // Configurar layout de cards
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar sistemas
        menuSystem = new MenuSystem();

        // Conectar o MenuSystem ao Frame
        menuSystem.setGameFrame(this);

        // Adicionar painéis ao CardLayout
        mainPanel.add(menuSystem, "MENU");

        // Configurar janela
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        // Configurar listeners de teclado
        this.addKeyListener(this);
        this.setFocusable(true);

        this.setVisible(true);

        switchToMenu();

    }
    private void setWindowIcon() {
        // O caminho DEVE começar com "/" e não ter "resources" no nome.
        String path = "/image/icone.jpeg";

        try {
            URL iconURL = getClass().getResource(path);
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                this.setIconImage(icon.getImage());
            } else {
                // Este erro aparecerá no seu console se o caminho estiver errado
                System.err.println("NÃO FOI POSSÍVEL ENCONTRAR O ÍCONE EM: " + path);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone: " + e.getMessage());
        }
    }

    public void startNewGame(MapTheme selectedTheme) {

        // 1. Crie uma NOVA instância do GamePanel com o tema selecionado
        gamePanel = new GamePanel(selectedTheme);

        // 2. Conecte o novo GamePanel ao Menu e ao Frame
        gamePanel.setGameFrame(this);
        gamePanel.setMenuSystem(menuSystem);
        menuSystem.setGamePanel(gamePanel);

        // 3. Adicione o novo painel ao CardLayout (ele substituirá o antigo, se houver)
        mainPanel.add(gamePanel, "GAME");

        // 4. Mude para a tela do jogo
        cardLayout.show(mainPanel, "GAME");
        gameActive = true;
        AudioManager.playGameMusic();
        gamePanel.requestFocusInWindow();

        // 5. Inicie o loop do jogo
        gamePanel.startGameLoop();
    }

    public void switchToMenu() {
        if (gamePanel != null && gamePanel.isGameLoopActive()) {
            gamePanel.stopGameLoop();
        }

        cardLayout.show(mainPanel, "MENU");
        gameActive = false;
        AudioManager.playMenuMusic();
        menuSystem.requestFocusInWindow();

        if (menuSystem.getCurrentState() == MenuSystem.GameState.GAME_OVER) {
            menuSystem.showMainMenu();
        }
    }

    public void onGameOver(int score, int orbs, int enemies) {
        if (gamePanel.isGameLoopActive()) {
            gamePanel.stopGameLoop();
        }

        menuSystem.triggerGameOver(score, orbs, enemies);
        cardLayout.show(mainPanel, "MENU");
        gameActive = false;
        menuSystem.requestFocusInWindow();
    }

    public void returnToMainMenu() {
        switchToMenu();
    }

    // <<< MUDANÇA: Removemos o antigo método startNewGame() sem parâmetros.
    // O método com o parâmetro MapTheme agora é o único que inicia o jogo.

    // O resto do seu código (KeyListener, toggleFullScreen, etc.) pode permanecer o mesmo.
    // ... (Cole o restante dos seus métodos aqui, como keyPressed, toggleFullScreen, main, etc.)
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F11:
                toggleFullScreen();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void toggleFullScreen() {
        if (isFullScreen) {
            dispose();
            setUndecorated(false);
            setSize(windowedWidth, windowedHeight);
            setLocation(windowedX, windowedY);
            setVisible(true);
            isFullScreen = false;
        } else {
            windowedX = getX();
            windowedY = getY();
            windowedWidth = getWidth();
            windowedHeight = getHeight();
            dispose();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
            isFullScreen = true;
        }
    }
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        javax.swing.SwingUtilities.invokeLater(() -> {
            new CyberRunnerGame();
        });
    }
}