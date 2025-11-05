package org.example.ui;

<<<<<<< HEAD
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;
import org.example.world.MapTheme;

import org.example.world.IBackgroundManager;
import org.example.fhysics.PhysicsEngine;
import org.example.fhysics.ScoreSystem;
import org.example.inputs.CameraController;
import org.example.inputs.InputHandler;
import org.example.levels.InfiniteWorldSystem;
import org.example.main.CyberRunnerGame;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.objects.Player;
import org.example.world.CyberpunkBackgroundManager;
import org.example.world.ParticleSystem;
import org.example.world.Platform;
import org.example.world.ReactivePlatformSystem;
import org.example.world.WindEffect;
import org.example.world.WorldBuilder;


=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

public class GamePanel extends JPanel {

    // Sistemas do jogo
<<<<<<< HEAD
    private long lastRenderTime = 0;
    private InputHandler inputHandler;
    private ScoreSystem scoreSystem;
    private CameraController cameraController;
    private InfiniteWorldSystem infiniteWorldSystem;
    private IBackgroundManager backgroundManager;
    private ParticleSystem particleSystem;
    private ArrayList<WindEffect> windEffects;
=======
    private InputHandler inputHandler;
    private ScoreSystem scoreSystem;
    
    // Sistema de animações
    private AnimationManager animationManager;
    private CameraController cameraController;
    private LevelSystem levelSystem; // ⭐ ADICIONADO: Declaração do campo levelSystem
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

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
<<<<<<< HEAD
    private ArrayList<org.example.objects.FlyingEnemy> flyingEnemies;

    // Sistema de respawn de inimigos
    private ArrayList<EnemyRespawnData> enemyRespawnQueue;
    public static final int ENEMY_RESPAWN_DELAY = 5000;
    public static final int MAX_ACTIVE_ENEMIES = 8;

    // Constantes de morte
    private static final int DEATH_Y_THRESHOLD = 800;
    private static final int WORLD_BOUNDS_LEFT = -500;

    // Sistema de ativação/desativação de inimigos
    private boolean enemiesEnabled = true;

    // Sistema de geração de partículas
    private long lastParticleSpawnTime = 0;
    private static final long PARTICLE_SPAWN_INTERVAL = 2000;
=======

    // Sistema de respawn de inimigos
    private ArrayList<EnemyRespawnData> enemyRespawnQueue;
    public static final int ENEMY_RESPAWN_DELAY = 10000; // 10 segundos em millisegundos

    // Sistema de ativação/desativação de inimigos
    private boolean enemiesEnabled = true;
    private ArrayList<Enemy> disabledEnemies; // Inimigos temporariamente desativados
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

    private boolean gameOver = false;
    private boolean gamePaused = false;

<<<<<<< HEAD
    public InfiniteWorldSystem getInfiniteWorldSystem() {
        return infiniteWorldSystem;
=======
    public LevelSystem getLevelSystem() {
        return levelSystem;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public GamePanel() {
        setupPanel();
<<<<<<< HEAD

        initializeSystems(MapTheme.CYBERPUNK);
        initializeGame();
=======
        initializeSystems();
        initializeGame();
        
        // Inicializar sistema de animações
        this.animationManager = AnimationManager.getInstance();
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        setupInput();
    }

    private void setupPanel() {
        this.setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        this.setBackground(new Color(135, 206, 235));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

<<<<<<< HEAD
    public GamePanel(MapTheme selectedTheme) {
        setupPanel();
        initializeSystems(selectedTheme);
        initializeGame();
        setupInput();
    }

    // Mude o método initializeSystems para usar o tema
    private void initializeSystems(MapTheme selectedTheme) {
        inputHandler = new InputHandler(this);
        scoreSystem = new ScoreSystem();
        cameraController = new CameraController();

        // LÓGICA DE ESCOLHA DO TEMA!
        switch (selectedTheme) {
            case HALLOWEEN:
                // Se o usuário escolheu Halloween, crie o gerenciador de Halloween
                backgroundManager = new org.example.world.HalloweenBackgroundManager(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
                break;
            case CYBERPUNK:
            default: // Cyberpunk será o padrão
                // Se o usuário escolheu Cyberpunk, crie o gerenciador Cyberpunk
                backgroundManager = new org.example.world.CyberpunkBackgroundManager(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
                break;
        }

        particleSystem = new ParticleSystem();
        windEffects = new ArrayList<>();
        enemyRespawnQueue = new ArrayList<>();
    }

    private void initializeGame() {
        infiniteWorldSystem = new InfiniteWorldSystem();
        player = new Player(infiniteWorldSystem.getPlayerStartX(), infiniteWorldSystem.getPlayerStartY());
        setupPlayerColors();

        platforms = WorldBuilder.createInitialPlatforms();
        cameraController.centerOnPlayer(player);

        enemies = WorldBuilder.createInitialEnemies();
        energyOrbs = new ArrayList<>();

        flyingEnemies = WorldBuilder.getFlyingEnemies();
        if (flyingEnemies != null && !flyingEnemies.isEmpty()) {
            // Definir o player como alvo dos inimigos voadores
            for (org.example.objects.FlyingEnemy flyingEnemy : flyingEnemies) {
                if (flyingEnemy != null) {
                    flyingEnemy.setTargetPlayer(player);
                }
            }
        }

        ReactivePlatformSystem.createReactivePlatforms(platforms);
        createWindEffects();
    }
    private void setupPlayerColors() {
        try {
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
=======
            System.out.println("Aviso: Métodos de cor do player não encontrados: " + e.getMessage());
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
        for (java.awt.event.KeyListener listener : this.getKeyListeners()) {
            this.removeKeyListener(listener);
        }

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (gameOver) {
                    if (keyCode == java.awt.event.KeyEvent.VK_R) {
                        restartGame();
                        return;
                    }
                    if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                        handleEscapePressed();
                        return;
                    }
                    return;
                }

                if (gamePaused) {
                    if (keyCode == java.awt.event.KeyEvent.VK_P ||
                            keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                        gamePaused = false;
                    }
                    return;
                }

                if (keyCode == java.awt.event.KeyEvent.VK_P ||
                        keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                    gamePaused = true;
                    return;
                }

                if (inputHandler != null) {
                    inputHandler.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (inputHandler != null && !gameOver && !gamePaused) {
                    inputHandler.keyReleased(e);
                }
            }
        });

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

=======
        this.addKeyListener(inputHandler);
    }

    // === SETTERS PARA INTEGRAÇÃO ===

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void setMenuSystem(MenuSystem menuSystem) {
        this.menuSystem = menuSystem;
    }

    public void setGameFrame(CyberRunnerGame gameFrame) {
        this.gameFrame = gameFrame;
    }

<<<<<<< HEAD
    public CyberRunnerGame getGameFrame() {
        return gameFrame;
    }
=======
    // === CONTROLE DO GAME LOOP ===
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

    public void startGameLoop() {
        if (gameLoopActive) return;

        gameLoopActive = true;
        gamePaused = false;
<<<<<<< HEAD
=======
        System.out.println("Iniciando game loop...");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    public void stopGameLoop() {
<<<<<<< HEAD
=======
        System.out.println("Parando game loop...");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        gameLoopActive = false;
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    public void pauseGame() {
        gamePaused = true;
<<<<<<< HEAD
=======
        System.out.println("Jogo pausado");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public void resumeGame() {
        gamePaused = false;
<<<<<<< HEAD
=======
        System.out.println("Jogo resumido");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
<<<<<<< HEAD
        final double OPTIMAL_TIME = 1000000000.0 / 60.0;
        double delta = 0;

        while (gameLoopActive && !Thread.currentThread().isInterrupted()) {
            long now = System.nanoTime();
            delta += (now - lastTime);
            lastTime = now;

            if (delta >= OPTIMAL_TIME) {
                if (!gamePaused && !gameOver) {
                    update();
                }
                delta -= OPTIMAL_TIME;
            }

            repaint();

            try {
                Thread.sleep(8);
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            } catch (InterruptedException e) {
                break;
            }
        }
    }

<<<<<<< HEAD
=======
    // === LÓGICA PRINCIPAL DO JOGO ===

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void update() {
        if (gameOver) return;

        try {
<<<<<<< HEAD
            if (inputHandler != null) {
                inputHandler.update();
            }

            updateInfiniteWorld();

            PhysicsEngine.applyGravityToPlayer(player);
            player.update();

            rechargePlayerEnergy();

            PhysicsEngine.checkPlayerPlatformCollisions(player, platforms);

            // SISTEMA MISTO DE ORBS
            updateCianoOrbs();  // Orbs cianos (comuns)
            updateGoldOrbs();   // Orbs amarelos (raros - 3x pontos)

            updateEnemies();

            updateFlyingEnemies();

            spawnRandomEnemies();

            spawnPlatformParticles();

            infiniteWorldSystem.update((int)player.x);

            if (backgroundManager != null) {
                try {
                    backgroundManager.update(player.x, player.y, System.currentTimeMillis());
                } catch (Exception e) {
                }
            }

            particleSystem.update();

            updateWindEffects();

            ReactivePlatformSystem.updateReactivePlatforms(player);

            cameraController.updateCamera(player);

=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            if (menuSystem != null) {
                menuSystem.updateScore(scoreSystem.getCurrentScore());
                menuSystem.updateStats(scoreSystem.getEnergyOrbsCollected(), scoreSystem.getEnemiesDefeated());
            }

<<<<<<< HEAD
            checkGameOverConditions();

        } catch (Exception e) {
        }
    }

    private void updateFlyingEnemies() {
        if (flyingEnemies == null) {
            flyingEnemies = WorldBuilder.getFlyingEnemies();
            if (flyingEnemies == null) {
                flyingEnemies = new ArrayList<>();
            }
            return;
        }

        if (flyingEnemies.isEmpty()) {
            return;
        }

        if (!enemiesEnabled) {
            return;
        }

        for (int i = flyingEnemies.size() - 1; i >= 0; i--) {
            org.example.objects.FlyingEnemy flyingEnemy = flyingEnemies.get(i);

            if (flyingEnemy.x < -5000) {
                // Inimigo foi derrotado, considerar respawn
                continue;
            }

            if (player != null) {
                flyingEnemy.setTargetPlayer(player);
            }

            if (flyingEnemy.x > -1000 && flyingEnemy.y > -1000) {
                flyingEnemy.update(0.016f);

                java.awt.Rectangle playerHitbox = player.getHitbox();
                java.awt.Rectangle enemyHitbox = flyingEnemy.getHitbox();

                if (playerHitbox.intersects(enemyHitbox)) {
                    // Player pulou em cima do inimigo voador
                    if (player.velocityY > 0 && player.y + player.height - 10 < flyingEnemy.y) {
                        player.velocityY = -12;

                        // ✅ SOM DE INIMIGO DERROTADO
                        org.example.audio.AudioManager.playEnemyDownSound();

                        scoreSystem.addScore(50);
                        flyingEnemy.x = -10000;
                        flyingEnemy.y = -10000;
                    }
                    // Inimigo voador machuca o player
                    else if (!player.isInvulnerable()) {
                        // ✅ SOM DE DANO AO PLAYER
                        org.example.audio.AudioManager.playHurtSound();

                        player.loseLife();
                        if (player.lives <= 0) {
                            triggerGameOver();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void updateInfiniteWorld() {
        try {
            int playerX = (int)player.x;

            if (playerX % 500 == 0 && playerX > 0) {
            }

        } catch (Exception e) {
        }
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    private void rechargePlayerEnergy() {
        try {
<<<<<<< HEAD
            if (hasMethod(player, "rechargeEnergy")) {
                player.rechargeEnergy();
            } else {
=======
            // CORREÇÃO: Verificar se o método existe
            if (hasMethod(player, "rechargeEnergy")) {
                player.rechargeEnergy();
            } else {
                // Implementação alternativa se o método não existir
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                if (player.energyLevel < GameConfig.PLAYER_MAX_ENERGY) {
                    player.energyLevel = Math.min(GameConfig.PLAYER_MAX_ENERGY,
                            player.energyLevel + GameConfig.PLAYER_ENERGY_RECHARGE_RATE);
                }
            }
        } catch (Exception e) {
<<<<<<< HEAD
        }
    }

    // COLETA DE ORBS CIANOS (comuns - 10 pontos)
    private void updateCianoOrbs() {
        ArrayList<org.example.entities.EnergyOrbEntity> worldOrbs = WorldBuilder.getWorldOrbs();

        if (worldOrbs == null || worldOrbs.isEmpty()) return;

        java.awt.Rectangle playerHitbox = player.getHitbox();

        for (org.example.entities.EnergyOrbEntity orb : worldOrbs) {
            if (orb.isCollected()) continue;

            float orbX = orb.getX();
            float orbY = orb.getY();
            int orbSize = 16;

            java.awt.Rectangle orbHitbox = new java.awt.Rectangle(
                    (int)orbX - orbSize/2,
                    (int)orbY - orbSize/2,
                    orbSize,
                    orbSize
            );

            if (playerHitbox.intersects(orbHitbox)) {
                // ✅ SOM DE COLETA DE ORB
                org.example.audio.AudioManager.playEffectSound();

                try {
                    java.lang.reflect.Field collectedField = orb.getClass().getDeclaredField("collected");
                    collectedField.setAccessible(true);
                    collectedField.set(orb, true);
                } catch (Exception e) {
                }

                scoreSystem.addScore(GameConfig.ORB_POINTS);

                player.energyLevel = Math.min(GameConfig.PLAYER_MAX_ENERGY,
                        player.energyLevel + GameConfig.ORB_ENERGY_RESTORE);
            }
        }
    }

    // COLETA DE ORBS DOURADOS (raros - 30 pontos)
    private void updateGoldOrbs() {
        ArrayList<org.example.objects.EnergyOrb> goldOrbs = WorldBuilder.getGoldOrbs();

        if (goldOrbs == null || goldOrbs.isEmpty()) return;

        java.awt.Rectangle playerHitbox = player.getHitbox();

        for (org.example.objects.EnergyOrb orb : goldOrbs) {
            if (orb.isCollected()) continue;

            int orbSize = 20;

            java.awt.Rectangle orbHitbox = new java.awt.Rectangle(
                    orb.x - orbSize/2,
                    orb.y - orbSize/2,
                    orbSize,
                    orbSize
            );

            if (playerHitbox.intersects(orbHitbox)) {
                // ✅ O orb.onCollect() já toca o som internamente
                orb.onCollect(player);

                int goldPoints = GameConfig.ORB_POINTS * 3;
                scoreSystem.addScore(goldPoints);

                player.energyLevel = Math.min(GameConfig.PLAYER_MAX_ENERGY,
                        player.energyLevel + GameConfig.ORB_ENERGY_RESTORE * 2);
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            }
        }
    }

    private void updateEnemies() {
<<<<<<< HEAD
        updateEnemyRespawn();

        if (!enemiesEnabled) {
            return;
        }

=======
        // Primeiro, verificar se algum inimigo deve ressuscitar
        updateEnemyRespawn();
        
        // Só atualizar inimigos se estiverem habilitados
        if (!enemiesEnabled) {
            return;
        }
        
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                PhysicsEngine.applyGravityToEnemy(enemy);
                enemy.update(player);
                PhysicsEngine.checkEnemyPlatformCollisions(enemy, platforms);
                PhysicsEngine.preventEnemyFallFromPlatforms(enemy, platforms);

<<<<<<< HEAD
                // Player mata o inimigo
                if (PhysicsEngine.checkPlayerEnemyCollision(player, enemy)) {
                    player.velocityY = -12;

                    // ✅ SOM DE INIMIGO DERROTADO
                    org.example.audio.AudioManager.playEnemyDownSound();

                    scoreSystem.defeatEnemy(enemy, player, true);
                    killEnemy(enemy);
                }
                // Inimigo machuca o player
                else if (player.getHitbox().intersects(enemy.getHitbox()) && player.velocityY >= 0) {
                    if (!player.isInvulnerable()) {
                        // ✅ SOM DE DANO AO PLAYER
                        org.example.audio.AudioManager.playHurtSound();

=======
                // Verificar colisão com player
                if (PhysicsEngine.checkPlayerEnemyCollision(player, enemy)) {
                    // Player pula no inimigo e o derrota
                    player.velocityY = -12;
                    scoreSystem.defeatEnemy(enemy);
                    killEnemy(enemy);
                } else if (player.getHitbox().intersects(enemy.getHitbox()) && player.velocityY >= 0) {
                    // Player foi atingido lateralmente - só aplica dano se não estiver invulnerável
                    if (!player.isInvulnerable()) {
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                        player.loseLife();
                        if (player.lives <= 0) {
                            triggerGameOver();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void updateEnemyRespawn() {
<<<<<<< HEAD
        int activeEnemies = 0;
        for (Enemy enemy : enemies) {
            if (enemy.x > -500 && enemy.y > -500) {
                activeEnemies++;
            }
        }

        for (int i = enemyRespawnQueue.size() - 1; i >= 0; i--) {
            EnemyRespawnData respawnData = enemyRespawnQueue.get(i);

            if (respawnData.shouldRespawn() && activeEnemies < MAX_ACTIVE_ENEMIES) {
                respawnEnemy(respawnData);
                enemyRespawnQueue.remove(i);
                activeEnemies++;
            }
        }
    }

    private void spawnRandomEnemies() {
        return;
    }

    private void spawnPlatformParticles() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastParticleSpawnTime >= PARTICLE_SPAWN_INTERVAL) {
            lastParticleSpawnTime = currentTime;

            int playerX = (int)player.x;

            for (Platform platform : platforms) {
                int distance = Math.abs(platform.x - playerX);

                if (distance <= 600 && Math.random() < 0.6) {
                    float platformCenterX = platform.x + platform.width / 2;
                    float platformY = platform.y;

                    switch (platform.getType()) {
                        case MOVING:
                            particleSystem.addEnergyParticles(platformCenterX, platformY, 3);
                            break;
                        case BOUNCY:
                            particleSystem.addHologramParticles(platformCenterX, platformY, 2);
                            break;
                        case ICE:
                            particleSystem.addAmbientParticles(platformCenterX, platformY, 4);
                            break;
                        default:
                            particleSystem.addAmbientParticles(platformCenterX, platformY, 2);
                            break;
                    }
                }
            }
        }
    }

    private void killEnemy(Enemy enemy) {
        EnemyRespawnData respawnData = new EnemyRespawnData(
                enemy,
                enemy.x,
                enemy.y,
                enemy.patrolLeft,
                enemy.patrolRight
        );

        enemyRespawnQueue.add(respawnData);

=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        enemy.x = -1000;
        enemy.y = -1000;
        enemy.velocityX = 0;
        enemy.velocityY = 0;
<<<<<<< HEAD
    }

    private void respawnEnemy(EnemyRespawnData respawnData) {
        Enemy enemy = respawnData.enemy;

        Platform nearestPlatform = findNearestPlatform(respawnData.originalX);

        if (nearestPlatform != null) {
            enemy.x = nearestPlatform.x + (nearestPlatform.width / 2);
            enemy.y = nearestPlatform.y - 40;
            enemy.patrolLeft = nearestPlatform.x;
            enemy.patrolRight = nearestPlatform.x + nearestPlatform.width - 30;
        } else {
            enemy.x = respawnData.originalX;
            enemy.y = respawnData.originalY;
            enemy.patrolLeft = respawnData.patrolLeft;
            enemy.patrolRight = respawnData.patrolRight;
        }

        enemy.velocityX = 0;
        enemy.velocityY = 0;
        enemy.direction = 1;
    }

    private Platform findNearestPlatform(double x) {
        Platform nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Platform platform : platforms) {
            double distance = Math.abs(platform.x - x);
            if (distance < minDistance && distance < 500) {
                minDistance = distance;
                nearest = platform;
            }
        }

        return nearest;
    }

    private void checkGameOverConditions() {
        if (player == null) return;

        // Player caiu do mapa
        if (player.y > DEATH_Y_THRESHOLD) {
            // ✅ SOM DE QUEDA/DANO
            org.example.audio.AudioManager.playFallSound();

            player.loseLife();

            if (player.lives <= 0) {
                triggerGameOver();
            } else {
                respawnPlayer();
            }
            return;
        }

        // Player saiu dos limites do mundo
        if (player.x < WORLD_BOUNDS_LEFT) {
            // ✅ SOM DE DANO
            org.example.audio.AudioManager.playHurtSound();

            player.loseLife();

            if (player.lives <= 0) {
                triggerGameOver();
            } else {
                respawnPlayer();
            }
            return;
        }

        if (player.lives <= 0) {
            triggerGameOver();
        }
    }

    private void respawnPlayer() {
        Platform respawnPlatform = findSafeRespawnPlatform();

        if (respawnPlatform != null) {
            player.x = respawnPlatform.x + (respawnPlatform.width / 2) - (player.width / 2);
            player.y = respawnPlatform.y - player.height - 10;
        } else {
            player.x = infiniteWorldSystem.getPlayerStartX();
            player.y = infiniteWorldSystem.getPlayerStartY();
        }

        player.velocityX = 0;
        player.velocityY = 0;

        try {
            java.lang.reflect.Method setInvulnerableMethod = player.getClass().getMethod("setInvulnerable", boolean.class);
            setInvulnerableMethod.invoke(player, true);
        } catch (Exception e) {
        }

        cameraController.centerOnPlayer(player);

    }

    private Platform findSafeRespawnPlatform() {
        if (platforms.isEmpty()) return null;

        Platform bestPlatform = null;
        double minDistance = Double.MAX_VALUE;

        for (Platform platform : platforms) {
            if (platform.x < player.x && platform.x > player.x - 800) {
                double distance = player.x - platform.x;
                if (distance < minDistance) {
                    minDistance = distance;
                    bestPlatform = platform;
                }
            }
        }

        if (bestPlatform == null && !platforms.isEmpty()) {
            for (Platform platform : platforms) {
                if (platform.x >= 0) {
                    bestPlatform = platform;
                    break;
                }
            }
        }

        return bestPlatform;
=======
        
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public void onPlayerDeath() {
        triggerGameOver();
    }

    public void triggerGameOver() {
<<<<<<< HEAD
        if (gameOver) return;

        gameOver = true;
        gamePaused = false;
        int finalScore = scoreSystem.getCurrentScore();

        try {
            java.lang.reflect.Method saveMethod = scoreSystem.getClass().getMethod("saveHighScore");
            saveMethod.invoke(scoreSystem);
        } catch (Exception e) {
        }

        this.requestFocusInWindow();

        javax.swing.SwingUtilities.invokeLater(() -> {
            this.requestFocusInWindow();
        });

        if (gameFrame != null) {
            gameFrame.onGameOver(
                    finalScore,
                    scoreSystem.getEnergyOrbsCollected(),
                    scoreSystem.getEnemiesDefeated()
            );
        } else if (menuSystem != null) {
            menuSystem.triggerGameOver(
                    finalScore,
                    scoreSystem.getEnergyOrbsCollected(),
                    scoreSystem.getEnemiesDefeated()
            );
        }
    }

    private void restartGame() {

        gameOver = false;
        gamePaused = false;

        if (gameThread != null) {
            stopGameLoop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        platforms.clear();
        enemies.clear();
        energyOrbs.clear();
        enemyRespawnQueue.clear();

        if (particleSystem != null) {
            particleSystem.clear();
        }

        if (scoreSystem != null) {
            try {
                java.lang.reflect.Method resetMethod = scoreSystem.getClass().getMethod("reset");
                resetMethod.invoke(scoreSystem);
            } catch (Exception e) {
                scoreSystem.resetScore();
            }
        }

        infiniteWorldSystem = new InfiniteWorldSystem();
        player = new Player(infiniteWorldSystem.getPlayerStartX(), infiniteWorldSystem.getPlayerStartY());
        setupPlayerColors();

        platforms = WorldBuilder.createInitialPlatforms();
        enemies = WorldBuilder.createInitialEnemies();
        energyOrbs = new ArrayList<>();

        cameraController.centerOnPlayer(player);

        try {
            java.lang.reflect.Method setTargetMethod = cameraController.getClass().getMethod("setTarget", Player.class);
            setTargetMethod.invoke(cameraController, player);
        } catch (Exception e) {
        }

        ReactivePlatformSystem.createReactivePlatforms(platforms);
        createWindEffects();

        this.requestFocusInWindow();

        startGameLoop();

    }

    public void resetGame() {
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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

<<<<<<< HEAD
        scoreSystem.resetScore();

        enemyRespawnQueue.clear();

        enemiesEnabled = true;

        infiniteWorldSystem = new InfiniteWorldSystem();

        player.x = infiniteWorldSystem.getPlayerStartX();
        player.y = infiniteWorldSystem.getPlayerStartY();
        player.velocityX = 0;
        player.velocityY = 0;
        player.resetLives();
        resetPlayerProperties();

        cameraController.resetCamera();

        platforms = WorldBuilder.createInitialPlatforms();
        enemies = WorldBuilder.createInitialEnemies();
        energyOrbs = new ArrayList<>();

        flyingEnemies = WorldBuilder.getFlyingEnemies();
        for (org.example.objects.FlyingEnemy flyingEnemy : flyingEnemies) {
            flyingEnemy.setTargetPlayer(player);
        }

=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        for (Enemy enemy : enemies) {
            resetEnemy(enemy);
        }

<<<<<<< HEAD
=======
        // Reset orbs
        resetEnergyOrbs();

        // Reset estado do jogo
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        gameOver = false;
        gamePaused = false;

        this.requestFocusInWindow();
<<<<<<< HEAD
=======
        System.out.println("Jogo resetado para Fase 1!");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    private void resetPlayerProperties() {
        try {
<<<<<<< HEAD
=======
            // CORREÇÃO: Usar reflexão para verificar campos antes de acessar
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            setFieldSafely(player, "energyLevel", 100);
            setFieldSafely(player, "canDash", true);
            setFieldSafely(player, "dashCooldown", 0);
        } catch (Exception e) {
<<<<<<< HEAD
=======
            System.out.println("Aviso: Algumas propriedades do player não puderam ser resetadas: " + e.getMessage());
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }

    private void setFieldSafely(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getField(fieldName);
            field.set(obj, value);
        } catch (Exception e) {
<<<<<<< HEAD
=======
            System.out.println("Campo " + fieldName + " não encontrado ou não acessível");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }

    private void resetEnemy(Enemy enemy) {
        if (enemy != null) {
<<<<<<< HEAD
=======
            // ⭐ CORREÇÃO: Usar campos públicos do Enemy diretamente
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            enemy.x = (enemy.patrolLeft + enemy.patrolRight) / 2;
            enemy.y = 100;
            enemy.velocityY = 0;
        }
    }

<<<<<<< HEAD
=======
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

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        try {
<<<<<<< HEAD
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gameOver) {
                drawGameOverMessage(g2d);
            } else {
=======
            // Configurar antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gameOver) {
                // Durante game over, mostrar tela simples
                drawGameOverMessage(g2d);
            } else {
                // Renderizar o jogo normal
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
                renderGame(g2d);
            }

        } catch (Exception e) {
<<<<<<< HEAD
            // ✅ CORREÇÃO: Sempre fornecer uma mensagem válida
            String errorMsg = "Erro de renderização";
            if (e != null && e.getMessage() != null && !e.getMessage().trim().isEmpty()) {
                errorMsg = e.getMessage();
            }

            // Desenhar tela de erro simples diretamente (sem depender de HUDRenderer)
            try {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.drawString("ERRO DE RENDERIZAÇÃO", 50, 100);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 14));
                g2d.drawString(errorMsg, 50, 150);
                g2d.drawString("Pressione ESC para voltar ao menu", 50, 200);

                // Log do erro no console
                if (e != null) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                // Se até isso falhar, apenas pinta a tela de preto
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            }
=======
            System.out.println("ERRO CRÍTICO no paintComponent: " + e.getMessage());
            e.printStackTrace();
            HUDRenderer.drawErrorScreen(g2d, e.getMessage());
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        } finally {
            g2d.dispose();
        }
    }

    private void renderGame(Graphics2D g2d) {
<<<<<<< HEAD
        // ✅ VERIFICAÇÃO CRÍTICA: Garantir que objetos essenciais existem
        if (player == null || platforms == null || cameraController == null) {
            // Desenhar tela de carregamento
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Inicializando...", GameConfig.SCREEN_WIDTH / 2 - 70, GameConfig.SCREEN_HEIGHT / 2);
            return;
        }

        boolean backgroundRendered = false;

        // Renderizar background
        if (backgroundManager != null) {
            try {
                backgroundManager.renderDistantBackground(g2d, cameraController.getCameraX(), cameraController.getCameraY());
                backgroundManager.renderMidBackground(g2d, cameraController.getCameraX(), cameraController.getCameraY());
                backgroundRendered = true;
            } catch (Exception e) {
                // Silencioso - usa fallback
            }
        }

        // Fallback background
        if (!backgroundRendered) {
            java.awt.GradientPaint bgGradient = new java.awt.GradientPaint(
                    0, 0, new Color(10, 10, 30),
                    0, GameConfig.SCREEN_HEIGHT, new Color(0, 50, 100)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        }

        // Aplicar transformação da câmera
        int cameraX = 0;
        int cameraY = 0;

        try {
            cameraX = cameraController.getCameraX();
            cameraY = cameraController.getCameraY();
        } catch (Exception e) {
            // Usar posição padrão
        }

        g2d.translate(-cameraX, -cameraY);

        // Renderizar plataformas
        for (Platform platform : platforms) {
            if (platform != null) {
                try {
                    platform.draw(g2d);
                } catch (Exception e) {
                    // Pular plataforma com erro
                }
            }
        }

        // Renderizar orbs (SISTEMA MISTO)
        try {
            WorldBuilder.renderWorldOrbs(g2d, cameraX, cameraY);
        } catch (Exception e) {
            // Silencioso
        }

        // Renderizar inimigos terrestres
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                if (enemy != null && enemy.x > -1000) {
                    try {
                        enemy.draw(g2d);
                    } catch (Exception e) {
                        // Pular inimigo com erro
                    }
                }
            }
        }

        // Renderizar inimigos voadores
        if (flyingEnemies != null) {
            for (org.example.objects.FlyingEnemy flyingEnemy : flyingEnemies) {
                if (flyingEnemy != null && flyingEnemy.x > -1000) {
                    try {
                        flyingEnemy.draw(g2d);
                    } catch (Exception e) {
                        // Pular inimigo voador com erro
                    }
                }
            }
        }

        // Renderizar player
        if (player != null) {
            try {
                player.draw(g2d);
            } catch (Exception e) {
                // Fallback: retângulo simples
                g2d.setColor(Color.CYAN);
                g2d.fillRect((int)player.x, (int)player.y, (int)player.width, (int)player.height);
            }
        }

        // Resetar transformação da câmera
        g2d.translate(cameraX, cameraY);

        // Renderizar partículas
        if (particleSystem != null) {
            try {
                particleSystem.render(g2d);
            } catch (Exception e) {
                // Silencioso
            }
        }

        // Renderizar HUD
        try {
            drawSimpleHUD(g2d);
        } catch (Exception e) {
            // HUD mínimo de emergência
            try {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString("Vidas: " + player.getLives(), 10, 20);
                if (scoreSystem != null) {
                    g2d.drawString("Score: " + scoreSystem.getCurrentScore(), 10, 40);
                }
            } catch (Exception ex) {
                // Desistir do HUD
            }
        }

        // Renderizar pausa
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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
<<<<<<< HEAD
        drawLivesHUD(g2d);
        drawScoreHUD(g2d);
        drawEnergyBar(g2d);
    }

    private void drawLivesHUD(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(10, 5, 120, 35, 5, 5);

        g2d.setColor(Color.WHITE);
        g2d.drawString("LIVES", 15, 25);

        int iconX = 80;
        int iconY = 10;
        int iconSize = 20;

        for (int i = 0; i < 3; i++) {
            if (i < player.getLives()) {
                g2d.setColor(new Color(0, 150, 255));
            } else {
                g2d.setColor(new Color(100, 100, 100));
            }

            g2d.fillOval(iconX + (i * 25), iconY, iconSize, iconSize);

=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(iconX + (i * 25), iconY, iconSize, iconSize);
        }
    }
<<<<<<< HEAD

    private void drawScoreHUD(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.WHITE);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(GameConfig.SCREEN_WIDTH - 150, 5, 145, 35, 5, 5);

        g2d.setColor(Color.WHITE);
        g2d.drawString(String.format("%06d", scoreSystem.getCurrentScore()),
                GameConfig.SCREEN_WIDTH - 140, 28);

        if (scoreSystem.getScoreMultiplier() > 1) {
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("x" + scoreSystem.getScoreMultiplier(),
                    GameConfig.SCREEN_WIDTH - 30, 28);
        }
    }

    private void drawEnergyBar(Graphics2D g2d) {
=======
    
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        int barX = GameConfig.SCREEN_WIDTH - 200;
        int barY = 50;
        int barWidth = 150;
        int barHeight = 20;
<<<<<<< HEAD

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 5, 5);

        int energy = (int) player.getCurrentEnergy();
        int energyWidth = (int)((energy / 100.0) * (barWidth - 4));

        Color energyColor;
        if (energy > 60) {
            energyColor = new Color(0, 255, 0);
        } else if (energy > 30) {
            energyColor = new Color(255, 255, 0);
        } else {
            energyColor = new Color(255, 0, 0);
        }

        g2d.setColor(energyColor);
        g2d.fillRoundRect(barX + 2, barY + 2, energyWidth, barHeight - 4, 3, 3);

=======
        
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("ENERGY", barX, barY - 5);
    }

<<<<<<< HEAD
    private void createWindEffects() {
        windEffects.add(new WindEffect(500, 200, 200, 100, 2.0f));
        windEffects.add(new WindEffect(1200, 300, 150, 80, -1.5f));
        windEffects.add(new WindEffect(2000, 150, 300, 120, 3.0f));
        windEffects.add(new WindEffect(3000, 250, 180, 90, -2.0f));
    }

    private void updateWindEffects() {
        for (WindEffect wind : windEffects) {
            wind.update();
            wind.applyWindEffect(player);
=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }

    private void drawGameOverMessage(Graphics2D g2d) {
<<<<<<< HEAD
        g2d.setColor(new Color(20, 20, 20, 200));
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

=======
        // Fundo escuro
        g2d.setColor(new Color(20, 20, 20, 200));
        g2d.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Mensagem de game over
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String gameOverText = "GAME OVER";
        int textWidth = fm.stringWidth(gameOverText);
        g2d.drawString(gameOverText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 - 50);

<<<<<<< HEAD
=======
        // Pontuação final
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        fm = g2d.getFontMetrics();
        String scoreText = "Final Score: " + scoreSystem.getCurrentScore();
        textWidth = fm.stringWidth(scoreText);
        g2d.drawString(scoreText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 20);

<<<<<<< HEAD
        g2d.setColor(new Color(0, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        fm = g2d.getFontMetrics();

        String restartText = "Pressione R para jogar novamente";
        textWidth = fm.stringWidth(restartText);
        g2d.drawString(restartText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 80);

        String menuText = "Pressione ESC para voltar ao menu";
        textWidth = fm.stringWidth(menuText);
        g2d.drawString(menuText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 110);
    }

=======
        // Instrução
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2d.getFontMetrics();
        String instructionText = "Returning to menu...";
        textWidth = fm.stringWidth(instructionText);
        g2d.drawString(instructionText, (GameConfig.SCREEN_WIDTH - textWidth) / 2, GameConfig.SCREEN_HEIGHT / 2 + 80);
    }

    // === GETTERS E SETTERS ===

>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public boolean isGameLoopActive() {
        return gameLoopActive;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
<<<<<<< HEAD
=======
        if (!gameOver) {
            System.out.println("Game Over resetado - jogo pode continuar");
        }
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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

<<<<<<< HEAD
    public void disableEnemies() {
        if (enemiesEnabled) {
            enemiesEnabled = false;
        }
    }

    public void enableEnemies() {
        if (!enemiesEnabled) {
            enemiesEnabled = true;
        }
    }

=======
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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public boolean areEnemiesEnabled() {
        return enemiesEnabled;
    }

<<<<<<< HEAD
=======
    /**
     * Alterna o estado dos inimigos (ativa/desativa)
     */
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void toggleEnemies() {
        if (enemiesEnabled) {
            disableEnemies();
        } else {
            enableEnemies();
        }
    }

<<<<<<< HEAD
=======
    // Método auxiliar para trocar para o menu (fallback)
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
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

<<<<<<< HEAD
    private void handleEscapePressed() {

        stopGameLoop();

        gameOver = false;
        gamePaused = false;

        platforms.clear();
        enemies.clear();
        energyOrbs.clear();
        enemyRespawnQueue.clear();

        if (particleSystem != null) {
            particleSystem.clear();
        }

        if (gameFrame != null) {
            gameFrame.returnToMainMenu();
        } else {
            java.awt.Container parent = this.getParent();
            if (parent != null && parent.getLayout() instanceof java.awt.CardLayout) {
                java.awt.CardLayout layout = (java.awt.CardLayout) parent.getLayout();
                layout.show(parent, "MENU");
            }
=======
    // Método público para ser chamado pelo InputHandler quando ESC for pressionado
    public void handleEscapePressed() {
        if (gameFrame != null) {
            gameFrame.switchToMenu();
        } else {
            // Fallback
            switchToMenu();
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }
}