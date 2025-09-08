package org.example.ui;

import org.example.entities.PlayerEntity;
import org.example.entities.EnemyEntity;
import org.example.systems.ComponentPhysicsEngine;
import org.example.inputs.CameraController;
import org.example.levels.LevelSystem;
import org.example.fhysics.ScoreSystem;
import org.example.world.Platform;
import org.example.world.WorldBuilder;
import org.example.inputs.InputHandler;
import org.example.objects.Player;
import org.example.components.HealthComponent;
import org.example.components.RenderComponent;
import org.example.components.Entity;
import org.example.graphics.AnimationManager;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Versão independente do GamePanel usando sistema de componentes
 * Funciona sem precisar modificar outras classes
 */
public class ComponentGamePanel extends JPanel {

    // Sistemas do jogo
    private InputHandler inputHandler;
    private ScoreSystem scoreSystem;
    
    // Sistema de animações
    private AnimationManager animationManager;
    private CameraController cameraController;
    private LevelSystem levelSystem;

    // Entidades usando sistema de componentes
    private PlayerEntity player;
    private List<EnemyEntity> enemies;
    private List<Entity> energyOrbs;

    // Objetos do mundo
    private List<Platform> platforms;

    // Estados do jogo
    private boolean gameLoopActive = false;
    private boolean gameOver = false;
    private boolean gamePaused = false;
    private Thread gameThread;

    public ComponentGamePanel() {
        setupPanel();
        initializeSystems();
        initializeGame();
        
        // Inicializar sistema de animações
        this.animationManager = AnimationManager.getInstance();
        setupInput();
    }

    private void setupPanel() {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(new Color(135, 206, 235));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    private void initializeSystems() {
        inputHandler = new InputHandler(this);
        scoreSystem = new ScoreSystem();
        cameraController = new CameraController();
        levelSystem = new LevelSystem();
    }

    private void initializeGame() {
        // Criar player usando sistema de componentes
        LevelSystem.LevelData levelData = levelSystem.getCurrentLevelData();
        player = new PlayerEntity(levelData.playerStartX, levelData.playerStartY);

        // Criar mundo usando WorldBuilder
        platforms = WorldBuilder.createPlatformsForLevel(levelSystem.getCurrentLevel());

        // Criar enemies usando sistema de componentes
        enemies = new ArrayList<>();

        // Criar orbs usando sistema de componentes
        energyOrbs = new ArrayList<>();

        System.out.println("Jogo inicializado com sistema de componentes!");
    }

    private void setupInput() {
        this.addKeyListener(inputHandler);
        this.setFocusable(true);
    }

    public void startGameLoop() {
        if (gameLoopActive) return;

        gameLoopActive = true;
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    public void stopGameLoop() {
        gameLoopActive = false;
        if (gameThread != null) {
            try {
                gameThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        double deltaTime = 0;

        while (gameLoopActive) {
            long currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / GameConfig.NANOSECONDS_PER_FRAME;
            lastTime = currentTime;

            if (!gamePaused && !gameOver) {
                update((float) deltaTime);
            }

            repaint();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void update(float deltaTime) {
        // Atualizar input
        inputHandler.update();
        
        // Atualizar animações
        if (animationManager != null) {
            animationManager.update();
        }

        // Aplicar física
        applyPhysics(deltaTime);

        // Atualizar entidades
        updateEntities(deltaTime);

        // Verificar colisões
        checkCollisions();

        // CORREÇÃO: Atualizar câmera de forma robusta
        updateCameraSafely();

        // Verificar condições de game over
        checkGameOverConditions();
    }

    /**
     * Método seguro para atualizar câmera que tenta diferentes abordagens
     */
    private void updateCameraSafely() {
        try {
            // Tentativa 1: Método com int
            java.lang.reflect.Method method = cameraController.getClass().getMethod("updateCamera", int.class, int.class);
            method.invoke(cameraController, (int)player.getX(), (int)player.getY());
            return;
        } catch (Exception e1) {
            try {
                // Tentativa 2: Método com float
                java.lang.reflect.Method method = cameraController.getClass().getMethod("updateCamera", float.class, float.class);
                method.invoke(cameraController, player.getX(), player.getY());
                return;
            } catch (Exception e2) {
                try {
                    // Tentativa 3: Método com Player (assumindo que existe um adaptador)
                    java.lang.reflect.Method method = cameraController.getClass().getMethod("updateCamera", Player.class);
                    method.invoke(cameraController, new Player((int)player.getX(), (int)player.getY()));
                } catch (Exception e3) {
                    // Fallback: não atualiza câmera mas não quebra o jogo
                    System.out.println("Info: Câmera não foi atualizada - método não encontrado");
                }
            }
        }
    }

    private void applyPhysics(float deltaTime) {
        // Aplicar gravidade ao player
        ComponentPhysicsEngine.applyGravity(player);

        // Aplicar gravidade aos enemies
        for (EnemyEntity enemy : enemies) {
            ComponentPhysicsEngine.applyGravity(enemy);
        }
    }

    private void updateEntities(float deltaTime) {
        // Atualizar player
        player.update(deltaTime);

        // Atualizar enemies
        for (EnemyEntity enemy : enemies) {
            enemy.update(deltaTime);
        }

        // Atualizar orbs
        for (Entity orb : energyOrbs) {
            orb.update(deltaTime);
        }
    }

    private void checkCollisions() {
        // Colisões player-plataforma
        ComponentPhysicsEngine.checkPlayerPlatformCollisions(player, platforms);

        // Colisões enemy-plataforma
        for (EnemyEntity enemy : enemies) {
            ComponentPhysicsEngine.checkEnemyPlatformCollisions(enemy, platforms);
        }

        // Colisões player-enemy
        ComponentPhysicsEngine.checkPlayerEnemyCollisions(player, enemies);

        // Colisões player-orb
        List<Entity> collectedOrbs = ComponentPhysicsEngine.checkPlayerOrbCollisions(player, energyOrbs);
        for (Entity orb : collectedOrbs) {
            energyOrbs.remove(orb);
        }
    }

    private void checkGameOverConditions() {
        if (ComponentPhysicsEngine.isOutOfWorldBounds(player)) {
            triggerGameOver();
        }

        // Verificar se player ficou sem vidas
        HealthComponent health = player.getComponent(HealthComponent.class);
        if (health != null && !health.isAlive()) {
            triggerGameOver();
        }
    }

    private void triggerGameOver() {
        if (gameOver) return;

        gameOver = true;
        System.out.println("GAME OVER! Pontuação final: " + scoreSystem.getCurrentScore());

        // Parar o game loop
        stopGameLoop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Configurar renderização
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        drawBackgroundSafely(g2d);

        // Aplicar transformação da câmera
        int cameraX = getCameraX();
        int cameraY = getCameraY();
        g2d.translate(-cameraX, -cameraY);

        // Renderizar mundo
        renderWorld(g2d);

        // Resetar transformação
        g2d.translate(cameraX, cameraY);

        // Renderizar HUD
        renderHUD(g2d);

        // Renderizar tela de game over se necessário
        if (gameOver) {
            renderGameOver(g2d);
        }

        g2d.dispose();
    }

    /**
     * Método seguro para obter coordenada X da câmera
     */
    private int getCameraX() {
        try {
            return cameraController.getCameraX();
        } catch (Exception e) {
            return 0; // Fallback
        }
    }

    /**
     * Método seguro para obter coordenada Y da câmera
     */
    private int getCameraY() {
        try {
            return cameraController.getCameraY();
        } catch (Exception e) {
            return 0; // Fallback
        }
    }

    /**
     * Desenha o background de forma segura
     */
    private void drawBackgroundSafely(Graphics2D g2d) {
        try {
            HUDRenderer.drawBackground(g2d);
        } catch (Exception e) {
            // Fallback: background simples
            GradientPaint bgGradient = new GradientPaint(
                    0, 0, new Color(10, 10, 30),
                    0, GameConfig.SCREEN_HEIGHT, new Color(0, 50, 100)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        }
    }

    private void renderWorld(Graphics2D g2d) {
        // Renderizar plataformas
        for (Platform platform : platforms) {
            renderPlatformSafely(g2d, platform);
        }

        // Renderizar player
        player.draw(g2d);

        // Renderizar enemies
        for (EnemyEntity enemy : enemies) {
            enemy.draw(g2d);
        }

        // Renderizar orbs
        for (Entity orb : energyOrbs) {
            RenderComponent render = orb.getComponent(RenderComponent.class);
            if (render != null) {
                render.render(g2d);
            }
        }
    }

    /**
     * Renderiza plataforma de forma segura
     */
    private void renderPlatformSafely(Graphics2D g2d, Platform platform) {
        try {
            // Tenta usar o método draw() se existir
            platform.draw(g2d);
        } catch (Exception e) {
            // Fallback: renderização manual
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(platform.getX(), platform.getY(),
                    platform.getWidth(), platform.getHeight());

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(platform.getX(), platform.getY(),
                    platform.getWidth(), platform.getHeight());
        }
    }

    private void renderHUD(Graphics2D g2d) {
        // HUD simplificado estilo Super Mario
        renderSimpleHUD(g2d);
    }
    
    private void renderSimpleHUD(Graphics2D g2d) {
        // Vidas no canto superior esquerdo
        renderLivesHUDSafely(g2d);
        
        // Score no canto superior direito
        renderGameHUDSafely(g2d);
        
        // Barra de energia
        renderEnergyBar(g2d);
    }

    // Método removido - não utilizado

    /**
     * Renderiza HUD de pontuação de forma segura
     */
    private void renderGameHUDSafely(Graphics2D g2d) {
        try {
            HUDRenderer.drawGameHUD(g2d, scoreSystem);
        } catch (Exception e) {
            // Fallback simples - Score no canto superior direito
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.WHITE);
            
            // Fundo semi-transparente para o score
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(GameConfig.SCREEN_WIDTH - 150, 5, 145, 35, 5, 5);
            
            // Score
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.format("%06d", scoreSystem.getCurrentScore()), 
                          GameConfig.SCREEN_WIDTH - 140, 28);
        }
    }

    /**
     * Renderiza HUD de vidas de forma segura
     */
    private void renderLivesHUDSafely(Graphics2D g2d) {
        try {
            // Tenta usar método com PlayerEntity se existir
            java.lang.reflect.Method method = HUDRenderer.class.getMethod("drawLivesHUD",
                    Graphics2D.class, PlayerEntity.class);
            method.invoke(null, g2d, player);
        } catch (Exception e) {
            // Fallback: HUD customizado
            renderCustomLivesHUD(g2d);
        }
    }

    /**
     * HUD futurista customizado (fallback)
     */
    private void renderCustomFuturisticHUD(Graphics2D g2d) {
        // Painel de informações
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(5, 5, 300, 180, 10, 10);

        g2d.setColor(new Color(0, 255, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(5, 5, 300, 180, 10, 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(0, 255, 200));
        g2d.drawString(">> CYBER RUNNER SYSTEM <<", 15, 25);

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(new Color(255, 255, 100));
        g2d.drawString("CONTROLS: WASD/ARROWS + SPACE + SHIFT/X", 15, 45);

        g2d.setColor(new Color(255, 100, 200));
        g2d.drawString("Position: [" + (int)player.getX() + ", " + (int)player.getY() + "]", 15, 80);
        g2d.drawString("Camera: [" + getCameraX() + ", " + getCameraY() + "]", 15, 95);
        g2d.drawString("Ground Status: " + (player.isOnGround() ? "STABLE" : "AIRBORNE"), 15, 110);
        g2d.drawString("Mode: " + player.getCurrentMode().toUpperCase(), 15, 125);
        g2d.drawString("Direction: " + (player.isFacingRight() ? "RIGHT" : "LEFT"), 15, 140);
        g2d.drawString("Energy: " + player.getCurrentEnergy() + "%", 15, 155);
        g2d.drawString("Dash: " + (player.canDash() ? "READY" : "COOLDOWN"), 15, 170);
    }

    /**
     * HUD de vidas customizado (fallback)
     */
    private void renderCustomLivesHUD(Graphics2D g2d) {
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
    
    private void renderEnergyBar(Graphics2D g2d) {
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

    private void renderGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String gameOverText = "GAME OVER";
        int textWidth = fm.stringWidth(gameOverText);
        g2d.drawString(gameOverText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 - 50);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        String scoreText = "Score: " + scoreSystem.getCurrentScore();
        textWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 20);
    }

    // Getters para compatibilidade
    public boolean isGameLoopActive() { return gameLoopActive; }
    public boolean isGamePaused() { return gamePaused; }
    public boolean isGameOver() { return gameOver; }
    public PlayerEntity getPlayer() { return player; }
}