package org.example.ui;

import org.example.inputs.CameraController;
import org.example.main.CyberRunnerGame;
import org.example.inputs.InputHandler;
import org.example.levels.LevelSystem;
import org.example.levels.LevelGoal;
import org.example.levels.LevelTransitionScreen;
import org.example.fhysics.PhysicsEngine;
import org.example.fhysics.ScoreSystem;
import org.example.world.Platform;
import org.example.world.WorldBuilder;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.objects.Player;
import org.example.graphics.AnimationManager;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.BasicStroke;

// Classe para gerenciar dados de respawn de inimigos
class EnemyRespawnData {
    public Enemy enemy;
    public long deathTime;
    public double originalX;
    public double originalY;
    public double patrolLeft;
    public double patrolRight;
    
    public EnemyRespawnData(Enemy enemy, double originalX, double originalY, double patrolLeft, double patrolRight) {
        this.enemy = enemy;
        this.deathTime = System.currentTimeMillis();
        this.originalX = originalX;
        this.originalY = originalY;
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
    }
    
    public boolean shouldRespawn() {
        return System.currentTimeMillis() - deathTime >= GamePanel.ENEMY_RESPAWN_DELAY;
    }
}

public class GamePanel extends JPanel {

    // Sistemas do jogo
    private InputHandler inputHandler;
    private ScoreSystem scoreSystem;
    
    // Sistema de animações
    private AnimationManager animationManager;
    private CameraController cameraController;
    private LevelSystem levelSystem; // ⭐ ADICIONADO: Declaração do campo levelSystem

    // Integração com menu e frame principal
    private MenuSystem menuSystem;
    private CyberRunnerGame gameFrame;
    private boolean gameLoopActive = false;
    private Thread gameThread;

    // Objetos do jogo
    private Player player;
    private ArrayList<Platform> platforms;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnergyOrb> energyOrbs;

    // Sistema de respawn de inimigos
    private ArrayList<EnemyRespawnData> enemyRespawnQueue;
    public static final int ENEMY_RESPAWN_DELAY = 10000; // 10 segundos em millisegundos

    // Sistema de ativação/desativação de inimigos
    private boolean enemiesEnabled = true;
    private ArrayList<Enemy> disabledEnemies; // Inimigos temporariamente desativados

    private boolean gameOver = false;
    private boolean gamePaused = false;

    public LevelSystem getLevelSystem() {
        return levelSystem;
    }

    public GamePanel() {
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
        enemyRespawnQueue = new ArrayList<>();
        disabledEnemies = new ArrayList<>();
    }

    private void initializeGame() {
        // Inicializar sistema de fases
        levelSystem = new LevelSystem();

        // Inicializar player na posição da fase
        LevelSystem.LevelData levelData = levelSystem.getCurrentLevelData();
        player = new Player(levelData.playerStartX, levelData.playerStartY);
        setupPlayerColors();

        // Usar WorldBuilder para criar o mundo baseado na fase atual
        platforms = WorldBuilder.createPlatformsForLevel(
                levelSystem.getCurrentLevel()
        );
        enemies = WorldBuilder.createEnemiesForLevel(
                levelSystem.getCurrentLevel(),
                levelData.difficultyMultiplier
        );
        energyOrbs = WorldBuilder.createEnergyOrbsForLevel(
                levelSystem.getCurrentLevel(),
                levelData.difficultyMultiplier
        );

        System.out.println("Fase " + levelSystem.getCurrentLevel() + " criada: " +
                platforms.size() + " plataformas, " +
                enemies.size() + " inimigos, " +
                energyOrbs.size() + " orbs");
    }

    private void setupPlayerColors() {
        try {
            // CORREÇÃO: Verificar se os métodos existem antes de chamá-los
            if (hasMethod(player, "changePrimaryColor")) {
                player.changePrimaryColor(GameConfig.PRIMARY_COLOR);
            }
            if (hasMethod(player, "changeSecondaryColor")) {
                player.changeSecondaryColor(GameConfig.SECONDARY_COLOR);
            }
            if (hasMethod(player, "changeAccentColor")) {
                player.changeAccentColor(GameConfig.ACCENT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("Aviso: Métodos de cor do player não encontrados: " + e.getMessage());
        }
    }

    private boolean hasMethod(Object obj, String methodName) {
        try {
            obj.getClass().getMethod(methodName, Color.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private void setupInput() {
        this.addKeyListener(inputHandler);
    }

    // === SETTERS PARA INTEGRAÇÃO ===

    public void setMenuSystem(MenuSystem menuSystem) {
        this.menuSystem = menuSystem;
    }

    public void setGameFrame(CyberRunnerGame gameFrame) {
        this.gameFrame = gameFrame;
    }

    // === CONTROLE DO GAME LOOP ===

    public void startGameLoop() {
        if (gameLoopActive) return;

        gameLoopActive = true;
        gamePaused = false;
        System.out.println("Iniciando game loop...");

        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    public void stopGameLoop() {
        System.out.println("Parando game loop...");
        gameLoopActive = false;
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    public void pauseGame() {
        gamePaused = true;
        System.out.println("Jogo pausado");
    }

    public void resumeGame() {
        gamePaused = false;
        System.out.println("Jogo resumido");
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1000.0 / 60.0; // 60 FPS

        while (gameLoopActive && !Thread.currentThread().isInterrupted()) {
            long now = System.nanoTime();

            if (now - lastTime >= nsPerFrame) {
                if (!gamePaused) {
                    update();
                }
                repaint();
                lastTime = now;
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // === LÓGICA PRINCIPAL DO JOGO ===

    public void update() {
        if (gameOver) return;

        try {
            // 0. Atualizar input
            if (inputHandler != null) {
                inputHandler.update();
            }
            
            // 0.5. Atualizar animações
            if (animationManager != null) {
                animationManager.update();
            }

            // 1. Aplicar física
            PhysicsEngine.applyGravityToPlayer(player);
            player.update();

            // 2. Recarregar energia do player
            rechargePlayerEnergy();

            // 3. Verificar colisões do player
            PhysicsEngine.checkPlayerPlatformCollisions(player, platforms);

            // 4. Atualizar e verificar orbs
            updateEnergyOrbs();

            // 5. Atualizar inimigos
            updateEnemies();

            // 6. Atualizar sistema de fases ⭐ NOVO
            levelSystem.update(player);

            // 7. Verificar se deve avançar para próxima fase ⭐ NOVO
            if (levelSystem.shouldAdvanceToNextLevel()) {
                advanceToNextLevel();
                return;
            }

            // 9. Atualizar câmera
            cameraController.updateCamera(player);

            // 10. Atualizar menu system com pontuação
            if (menuSystem != null) {
                menuSystem.updateScore(scoreSystem.getCurrentScore());
                menuSystem.updateStats(scoreSystem.getEnergyOrbsCollected(), scoreSystem.getEnemiesDefeated());
            }

            // 11. Verificar condições de game over
            checkGameOverConditions();

        } catch (Exception e) {
            System.out.println("Erro durante update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void advanceToNextLevel() {
        System.out.println("=== AVANÇANDO PARA PRÓXIMA FASE ===");

        // Adicionar bônus de fase à pontuação
        int levelBonus = levelSystem.getCurrentLevel() * 1000;
        scoreSystem.addScore(levelBonus); // ✅ Agora funciona com o método adicionado
        
        // Limpar fila de respawn de inimigos ao avançar de fase
        enemyRespawnQueue.clear();

        // Avançar para próxima fase
        levelSystem.advanceToNextLevel();

        // Recriar o mundo para a nova fase
        LevelSystem.LevelData newLevelData = levelSystem.getCurrentLevelData();

        // Reposicionar player
        player.x = newLevelData.playerStartX;
        player.y = newLevelData.playerStartY;
        player.velocityX = 0;
        player.velocityY = 0;
        resetPlayerProperties();

        // Recriar mundo
        platforms = WorldBuilder.createPlatformsForLevel(levelSystem.getCurrentLevel());
        enemies = WorldBuilder.createEnemiesForLevel(
                levelSystem.getCurrentLevel(),
                newLevelData.difficultyMultiplier
        );
        energyOrbs = WorldBuilder.createEnergyOrbsForLevel(
                levelSystem.getCurrentLevel(),
                newLevelData.difficultyMultiplier
        );

        // Resetar inimigos
        for (Enemy enemy : enemies) {
            resetEnemy(enemy);
        }

        // Resetar câmera
        cameraController.resetCamera();

        System.out.println("Nova fase carregada: Fase " + levelSystem.getCurrentLevel());
    }

    private void rechargePlayerEnergy() {
        try {
            // CORREÇÃO: Verificar se o método existe
            if (hasMethod(player, "rechargeEnergy")) {
                player.rechargeEnergy();
            } else {
                // Implementação alternativa se o método não existir
                if (player.energyLevel < GameConfig.PLAYER_MAX_ENERGY) {
                    player.energyLevel = Math.min(GameConfig.PLAYER_MAX_ENERGY,
                            player.energyLevel + GameConfig.PLAYER_ENERGY_RECHARGE_RATE);
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso: Erro ao recarregar energia do player: " + e.getMessage());
        }
    }

    private void updateEnergyOrbs() {
        for (EnergyOrb orb : energyOrbs) {
            if (orb != null && !orb.isCollected()) {
                orb.update(player);

                if (PhysicsEngine.checkPlayerOrbCollision(player, orb)) {
                    orb.onCollect(player);
                    scoreSystem.collectOrb(orb);
                }
            }
        }
    }

    private void updateEnemies() {
        // Primeiro, verificar se algum inimigo deve ressuscitar
        updateEnemyRespawn();
        
        // Só atualizar inimigos se estiverem habilitados
        if (!enemiesEnabled) {
            return;
        }
        
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                PhysicsEngine.applyGravityToEnemy(enemy);
                enemy.update(player);
                PhysicsEngine.checkEnemyPlatformCollisions(enemy, platforms);
                PhysicsEngine.preventEnemyFallFromPlatforms(enemy, platforms);

                // Verificar colisão com player
                if (PhysicsEngine.checkPlayerEnemyCollision(player, enemy)) {
                    // Player pula no inimigo e o derrota
                    player.velocityY = -12;
                    scoreSystem.defeatEnemy(enemy);
                    killEnemy(enemy);
                } else if (player.getHitbox().intersects(enemy.getHitbox()) && player.velocityY >= 0) {
                    // Player foi atingido lateralmente
                    triggerGameOver();
                    return;
                }
            }
        }
    }

    private void updateEnemyRespawn() {
        // Verificar inimigos que devem ressuscitar
        for (int i = enemyRespawnQueue.size() - 1; i >= 0; i--) {
            EnemyRespawnData respawnData = enemyRespawnQueue.get(i);
            
            if (respawnData.shouldRespawn()) {
                // Ressuscitar o inimigo
                respawnEnemy(respawnData);
                enemyRespawnQueue.remove(i);
            }
        }
    }
    
    private void killEnemy(Enemy enemy) {
        // Salvar dados originais do inimigo
        EnemyRespawnData respawnData = new EnemyRespawnData(
            enemy, 
            enemy.x, 
            enemy.y, 
            enemy.patrolLeft, 
            enemy.patrolRight
        );
        
        // Adicionar à fila de respawn
        enemyRespawnQueue.add(respawnData);
        
        // Mover inimigo para fora da tela (torná-lo invisível)
        enemy.x = -1000;
        enemy.y = -1000;
        enemy.velocityX = 0;
        enemy.velocityY = 0;
        
        System.out.println("Inimigo eliminado! Respawn em 10 segundos...");
    }
    
    private void respawnEnemy(EnemyRespawnData respawnData) {
        Enemy enemy = respawnData.enemy;
        
        // Restaurar posição e propriedades originais
        enemy.x = respawnData.originalX;
        enemy.y = respawnData.originalY;
        enemy.patrolLeft = respawnData.patrolLeft;
        enemy.patrolRight = respawnData.patrolRight;
        enemy.velocityX = 0;
        enemy.velocityY = 0;
        enemy.direction = 1; // Resetar direção
        
        System.out.println("Inimigo ressuscitado na posição (" + enemy.x + ", " + enemy.y + ")");
    }

    private void checkGameOverConditions() {
        if (PhysicsEngine.isOutOfWorldBounds(player)) {
            triggerGameOver();
        }
        
        // Verificar se o player ficou sem vidas
        if (!player.isAlive()) {
            triggerGameOver();
        }
    }

    public void onPlayerDeath() {
        triggerGameOver();
    }

    public void triggerGameOver() {
        if (gameOver) return; // Evitar múltiplas chamadas

        System.out.println("GAME OVER! Pontuação final: " + scoreSystem.getCurrentScore());

        gameOver = true;
        stopGameLoop();

        // Notificar o frame principal sobre o game over
        if (gameFrame != null) {
            gameFrame.onGameOver(
                    scoreSystem.getCurrentScore(),
                    scoreSystem.getEnergyOrbsCollected(),
                    scoreSystem.getEnemiesDefeated()
            );
        }

        // Fallback: se não há gameFrame, notificar diretamente o menuSystem
        else if (menuSystem != null) {
            menuSystem.triggerGameOver(
                    scoreSystem.getCurrentScore(),
                    scoreSystem.getEnergyOrbsCollected(),
                    scoreSystem.getEnemiesDefeated()
            );
            switchToMenu();
        }
    }

    // === RESET E INICIALIZAÇÃO ===

    public void resetGame() {
        System.out.println("Resetando jogo completamente...");

        // Parar o game loop se estiver ativo
        if (gameLoopActive) {
            stopGameLoop();

            try {
                if (gameThread != null) {
                    gameThread.join(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset do sistema de pontuação
        scoreSystem.resetScore();
        
        // Limpar fila de respawn de inimigos
        enemyRespawnQueue.clear();
        
        // Reativar inimigos se estiverem desativados
        enemiesEnabled = true;

        // ⭐ RESETAR SISTEMA DE FASES PARA A FASE 1
        if (levelSystem != null) {
            levelSystem.loadLevel(1);
        } else {
            levelSystem = new LevelSystem();
        }

        // Reset player para posição inicial da fase 1
        LevelSystem.LevelData levelData = levelSystem.getCurrentLevelData();
        player.x = levelData.playerStartX;
        player.y = levelData.playerStartY;
        player.velocityX = 0;
        player.velocityY = 0;
        player.resetLives(); // Resetar vidas para 3
        resetPlayerProperties();

        // Reset câmera
        cameraController.resetCamera();

        // ⭐ RECRIAR MUNDO PARA FASE 1
        platforms = WorldBuilder.createPlatformsForLevel(1);
        enemies = WorldBuilder.createEnemiesForLevel(1, 1.0f);
        energyOrbs = WorldBuilder.createEnergyOrbsForLevel(1, 1.0f);

        // Reset inimigos
        for (Enemy enemy : enemies) {
            resetEnemy(enemy);
        }

        // Reset orbs
        resetEnergyOrbs();

        // Reset estado do jogo
        gameOver = false;
        gamePaused = false;

        this.requestFocusInWindow();
        System.out.println("Jogo resetado para Fase 1!");
    }

    private void resetPlayerProperties() {
        try {
            // CORREÇÃO: Usar reflexão para verificar campos antes de acessar
            setFieldSafely(player, "energyLevel", 100);
            setFieldSafely(player, "canDash", true);
            setFieldSafely(player, "dashCooldown", 0);
        } catch (Exception e) {
            System.out.println("Aviso: Algumas propriedades do player não puderam ser resetadas: " + e.getMessage());
        }
    }

    private void setFieldSafely(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getField(fieldName);
            field.set(obj, value);
        } catch (Exception e) {
            System.out.println("Campo " + fieldName + " não encontrado ou não acessível");
        }
    }

    private void resetEnemy(Enemy enemy) {
        if (enemy != null) {
            // ⭐ CORREÇÃO: Usar campos públicos do Enemy diretamente
            enemy.x = (enemy.patrolLeft + enemy.patrolRight) / 2;
            enemy.y = 100;
            enemy.velocityY = 0;
        }
    }

    private void resetEnergyOrbs() {
        for (EnergyOrb orb : energyOrbs) {
            if (orb != null) {
                try {
                    // CORREÇÃO: Usar métodos públicos em vez de acessar campos diretamente
                    if (orb.isCollected()) {
                        // Se não houver método público para resetar, criar um
                        resetOrbSafely(orb);
                    }
                } catch (Exception e) {
                    System.out.println("Aviso: Erro ao resetar orb: " + e.getMessage());
                }
            }
        }
    }

    private void resetOrbSafely(EnergyOrb orb) {
        try {
            // Usar reflexão para acessar campos privados
            setFieldSafely(orb, "collected", false);
            setFieldSafely(orb, "respawnTimer", 0);
            setFieldSafely(orb, "isAttractedToPlayer", false);
            setFieldSafely(orb, "attractionSpeed", 2.0f);
        } catch (Exception e) {
            System.out.println("Não foi possível resetar o orb completamente: " + e.getMessage());
        }
    }

    // === RENDERIZAÇÃO ===

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        try {
            // Configurar antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gameOver) {
                // Durante game over, mostrar tela simples
                drawGameOverMessage(g2d);
            } else {
                // Renderizar o jogo normal
                renderGame(g2d);
            }

        } catch (Exception e) {
            System.out.println("ERRO CRÍTICO no paintComponent: " + e.getMessage());
            e.printStackTrace();
            HUDRenderer.drawErrorScreen(g2d, e.getMessage());
        } finally {
            g2d.dispose();
        }
    }

    private void renderGame(Graphics2D g2d) {
        // Fundo baseado na fase atual ⭐ NOVO
        if (levelSystem != null && levelSystem.getCurrentLevelData() != null) {
            g2d.setColor(levelSystem.getCurrentLevelData().backgroundColor);
            g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        } else {
            HUDRenderer.drawBackground(g2d);
        }

        // Renderizar todos os objetos do jogo
        GameRenderer.renderAllGameObjects(g2d, platforms, enemies, energyOrbs, player, cameraController);

        // ⭐ RENDERIZAR OBJETIVO DA FASE
        if (levelSystem != null && levelSystem.getLevelGoal() != null) {
            // Verificar se está visível na câmera
            LevelGoal goal = levelSystem.getLevelGoal();
            if (cameraController.isObjectVisible(
                    goal.getX() - 50, goal.getY() - 150, 100, 150)) {

                g2d.translate(-cameraController.getCameraX(), -cameraController.getCameraY());
                goal.draw(g2d);
                g2d.translate(cameraController.getCameraX(), cameraController.getCameraY());
            }
        }

        // HUD simplificado
        drawSimpleHUD(g2d);

        // ⭐ HUD DE FASE
        drawLevelHUD(g2d);

        // ⭐ TELA DE TRANSIÇÃO DE FASE
        if (levelSystem != null && levelSystem.isShowingTransition()) {
            LevelTransitionScreen.drawLevelComplete(g2d, levelSystem, scoreSystem);
        }

        // Indicador de pausa
        if (gamePaused) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            String pauseText = "PAUSED";
            int textWidth = fm.stringWidth(pauseText);
            g2d.drawString(pauseText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2);
        }
    }

    private void drawSimpleHUD(Graphics2D g2d) {
        // Vidas no canto superior esquerdo
        drawLivesHUD(g2d);
        
        // Score no canto superior direito
        drawScoreHUD(g2d);
        
        // Barra de energia
        drawEnergyBar(g2d);
    }
    
    private void drawLivesHUD(Graphics2D g2d) {
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
    
    private void drawScoreHUD(Graphics2D g2d) {
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
    
    private void drawEnergyBar(Graphics2D g2d) {
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

    private void drawLevelHUD(Graphics2D g2d) {
        if (levelSystem == null) return;

        // Informações da fase no canto superior direito
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(GameConfig.SCREEN_WIDTH - 200, 10, 190, 80, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("FASE " + levelSystem.getCurrentLevel(), GameConfig.SCREEN_WIDTH - 190, 30);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.CYAN);

        LevelSystem.LevelData levelData = levelSystem.getCurrentLevelData();
        if (levelData != null) {
            g2d.drawString(levelData.themeName, GameConfig.SCREEN_WIDTH - 190, 45);

            // Indicador de proximidade do objetivo
            if (levelSystem.getLevelGoal() != null) {
                LevelGoal goal = levelSystem.getLevelGoal();
                float distance = (float)Math.sqrt(
                        Math.pow(player.x - goal.getX(), 2) +
                                Math.pow(player.y - goal.getY(), 2)
                );

                if (distance < 300) {
                    g2d.setColor(Color.YELLOW);
                    g2d.drawString("OBJETIVO PRÓXIMO!", GameConfig.SCREEN_WIDTH - 190, 60);
                } else {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.drawString("Distância: " + (int)distance, GameConfig.SCREEN_WIDTH - 190, 60);
                }
            }
        }
    }

    private void drawGameOverMessage(Graphics2D g2d) {
        // Fundo escuro
        g2d.setColor(new Color(20, 20, 20, 200));
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Mensagem de game over
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String gameOverText = "GAME OVER";
        int textWidth = fm.stringWidth(gameOverText);
        g2d.drawString(gameOverText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 - 50);

        // Pontuação final
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        fm = g2d.getFontMetrics();
        String scoreText = "Final Score: " + scoreSystem.getCurrentScore();
        textWidth = fm.stringWidth(scoreText);
        g2d.drawString(scoreText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 20);

        // Instrução
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2d.getFontMetrics();
        String instructionText = "Returning to menu...";
        textWidth = fm.stringWidth(instructionText);
        g2d.drawString(instructionText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 80);
    }

    // === GETTERS E SETTERS ===

    public boolean isGameLoopActive() {
        return gameLoopActive;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (!gameOver) {
            System.out.println("Game Over resetado - jogo pode continuar");
        }
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public MenuSystem getMenuSystem() {
        return menuSystem;
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public Player getPlayer() {
        return player;
    }

    // === MÉTODOS PARA ATIVAR/DESATIVAR INIMIGOS ===

    /**
     * Desativa todos os inimigos temporariamente
     * Os inimigos param de se mover e não causam dano ao player
     */
    public void disableEnemies() {
        if (enemiesEnabled) {
            enemiesEnabled = false;
            System.out.println("Inimigos desativados temporariamente");
        }
    }

    /**
     * Reativa todos os inimigos
     * Os inimigos voltam a se mover e causar dano normalmente
     */
    public void enableEnemies() {
        if (!enemiesEnabled) {
            enemiesEnabled = true;
            System.out.println("Inimigos reativados");
        }
    }

    /**
     * Verifica se os inimigos estão ativos
     * @return true se os inimigos estão ativos, false caso contrário
     */
    public boolean areEnemiesEnabled() {
        return enemiesEnabled;
    }

    /**
     * Alterna o estado dos inimigos (ativa/desativa)
     */
    public void toggleEnemies() {
        if (enemiesEnabled) {
            disableEnemies();
        } else {
            enableEnemies();
        }
    }

    // Método auxiliar para trocar para o menu (fallback)
    private void switchToMenu() {
        Container parent = getParent();
        if (parent != null) {
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show(parent, "MENU");
            if (menuSystem != null) {
                menuSystem.requestFocusInWindow();
            }
        }
    }

    // Método público para ser chamado pelo InputHandler quando ESC for pressionado
    public void handleEscapePressed() {
        if (gameFrame != null) {
            gameFrame.switchToMenu();
        } else {
            // Fallback
            switchToMenu();
        }
    }
}