package org.example.main;

import org.example.audio.AudioManager;
import org.example.ui.GamePanel;
import org.example.ui.MenuSystem;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CyberRunnerGame extends JFrame implements KeyListener {

    // Gerenciador de layouts
    CardLayout cardLayout;
    JPanel mainPanel;

    // Sistemas do jogo
    MenuSystem menuSystem;
    GamePanel gamePanel;

    // Estados
    boolean gameActive = false;

    public CyberRunnerGame() {
        // Configurar janela principal
        this.setTitle("Cyber Runner - The Ultimate Cyber Adventure");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Configurar layout de cards
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar sistemas
        menuSystem = new MenuSystem();
        gamePanel = new GamePanel();

        // CORREÇÃO: Conectar sistemas bidirecionalmente ANTES de adicionar ao layout
        menuSystem.setGamePanel(gamePanel);
        menuSystem.setGameFrame(this); // Adicionar referência ao frame no menu
        gamePanel.setMenuSystem(menuSystem);
        gamePanel.setGameFrame(this);

        // Adicionar painéis ao CardLayout
        mainPanel.add(menuSystem, "MENU");
        mainPanel.add(gamePanel, "GAME");

        // Configurar janela
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        // Configurar listeners de teclado
        this.addKeyListener(this);
        this.setFocusable(true);

        this.setVisible(true);

        // Focar no menu inicialmente
        switchToMenu();
    }

    // CORREÇÃO: Método melhorado para alternar para o jogo
    public void switchToGame() {
        System.out.println("=== INICIANDO JOGO ===");

        // Parar qualquer game loop ativo primeiro
        if (gamePanel.isGameLoopActive()) {
            gamePanel.stopGameLoop();
            try {
                Thread.sleep(100); // Dar tempo para parar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Resetar o jogo ANTES de trocar de tela
        gamePanel.resetGame();

        // Trocar para a tela do jogo
        cardLayout.show(mainPanel, "GAME");
        gameActive = true;

        // Música do jogo
        AudioManager.playGameMusic();

        // Garantir foco no painel do jogo
        gamePanel.requestFocusInWindow();

        // Aguardar um pouco antes de iniciar o game loop
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(50);
                if (!gamePanel.isGameLoopActive()) {
                    gamePanel.startGameLoop();
                    System.out.println("Game loop iniciado!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    // CORREÇÃO: Método melhorado para alternar para o menu
    public void switchToMenu() {
        System.out.println("=== VOLTANDO AO MENU ===");

        // Parar o game loop primeiro
        if (gamePanel != null && gamePanel.isGameLoopActive()) {
            gamePanel.stopGameLoop();
        }

        // Trocar para a tela do menu
        cardLayout.show(mainPanel, "MENU");
        gameActive = false;

        // Música do menu
        AudioManager.playMenuMusic();

        // Focar no menu
        menuSystem.requestFocusInWindow();

        // CORREÇÃO: Resetar o estado do menu
        if (menuSystem.getCurrentState() == MenuSystem.GameState.GAME_OVER) {
            // Se veio do game over, voltar ao menu principal
            menuSystem.showMainMenu();
        }
    }

    // CORREÇÃO: Método de game over melhorado
    public void onGameOver(int score, int orbs, int enemies) {
        System.out.println("=== GAME OVER ===");
        System.out.println("Score: " + score + ", Orbs: " + orbs + ", Enemies: " + enemies);

        // Parar o game loop
        if (gamePanel.isGameLoopActive()) {
            gamePanel.stopGameLoop();
        }

        // Notificar o menu system sobre o game over
        menuSystem.triggerGameOver(score, orbs, enemies);

        // Mudar para o menu (que agora mostrará a tela de game over)
        cardLayout.show(mainPanel, "MENU");
        gameActive = false;

        // Garantir foco no menu
        menuSystem.requestFocusInWindow();
    }

    // Pausar/resumir jogo
    public void pauseGame() {
        if (gameActive && gamePanel.isGameLoopActive()) {
            gamePanel.pauseGame();
        }
    }

    public void resumeGame() {
        if (gameActive && gamePanel.isGamePaused()) {
            gamePanel.resumeGame();
        }
    }

    public boolean isGameActive() {
        return gameActive;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Teclas globais que funcionam em qualquer tela
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                if (gameActive) {
                    // ESC durante o jogo volta para o menu
                    switchToMenu();
                }
                break;

            case KeyEvent.VK_F11:
                // Toggle fullscreen
                toggleFullscreen();
                break;

            // CORREÇÃO: Melhorar as teclas do game over
            case KeyEvent.VK_R:
                // R para reiniciar (funciona globalmente se estiver no game over)
                if (!gameActive && menuSystem.getCurrentState() == MenuSystem.GameState.GAME_OVER) {
                    System.out.println("Reiniciando jogo via tecla R...");
                    switchToGame();
                }
                break;

            case KeyEvent.VK_L:
                // L para voltar ao lobby (se no game over)
                if (!gameActive && menuSystem.getCurrentState() == MenuSystem.GameState.GAME_OVER) {
                    System.out.println("Voltando ao menu principal via tecla L...");
                    menuSystem.showMainMenu();
                }
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Implementação vazia - pode ser usada se necessário
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Implementação vazia - pode ser usada se necessário
    }

    private void toggleFullscreen() {
        try {
            if (this.isUndecorated()) {
                this.dispose();
                this.setUndecorated(false);
                this.setExtendedState(JFrame.NORMAL);
            } else {
                this.dispose();
                this.setUndecorated(true);
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            this.setVisible(true);

            // Restaurar foco apropriado
            if (gameActive) {
                gamePanel.requestFocusInWindow();
            } else {
                menuSystem.requestFocusInWindow();
            }
        } catch (Exception ex) {
            System.out.println("Erro ao alternar fullscreen: " + ex.getMessage());
        }
    }

    // CORREÇÃO: Método público para ser chamado pelo menu
    public void startNewGame() {
        switchToGame();
    }

    // CORREÇÃO: Método público para ser chamado pelo menu
    public void returnToMainMenu() {
        switchToMenu();
    }

    public static void main(String[] args) {
        // Configurar look and feel do sistema
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível definir o look and feel do sistema: " + e.getMessage());
        }

        // Configurações para melhor performance
        System.setProperty("sun.java2d.opengl", "true");

        // Iniciar o jogo na thread de eventos do Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new CyberRunnerGame();
        });
    }
}