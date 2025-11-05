package org.example.world;
<<<<<<< HEAD

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.example.entities.EnergyOrbEntity;
import org.example.levels.InfiniteWorldSystem;
import org.example.ui.GameConfig;

/**
 * WorldBuilder com SISTEMA MISTO DE ORBS
 * - Orbs CIANOS (comuns): aparecem na maioria das plataformas
 * - Orbs AMARELOS (raros): aparecem a cada 5 plataformas e valem 3x mais
 */
public class WorldBuilder {

    private static InfiniteWorldSystem infiniteSystem = new InfiniteWorldSystem();
    private static final int RENDER_DISTANCE = 2000;
    private static final int CLEANUP_DISTANCE = 3000;

    // DOIS SISTEMAS DE ORBS
    public static ArrayList<EnergyOrbEntity> worldOrbs = new ArrayList<>(); // Orbs CIANOS
    public static ArrayList<org.example.objects.EnergyOrb> goldOrbs = new ArrayList<>(); // Orbs AMARELOS

    // Lista de inimigos voadores
    public static ArrayList<org.example.objects.FlyingEnemy> flyingEnemies = new ArrayList<>();
    private static Set<Integer> generatedChunks = new HashSet<>();

    private static final int MAX_JUMP_HEIGHT = 120;
    private static final int MIN_PLATFORM_GAP = 40;
    private static final int MAX_PLATFORM_GAP = 90;
    private static final int BASE_HEIGHT = 500;

    private static int totalPlatformsGenerated = 0;
    private static final int MAX_HEIGHT_VARIATION = 80;

    private static ArrayList<Platform> worldPlatforms = new ArrayList<>();

    /**
     * Cria plataformas com SISTEMA MISTO DE ORBS
     * ✅ CORRIGIDO: Inimigos voadores criados UMA ÚNICA VEZ
     */
    public static ArrayList<Platform> createInitialPlatforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // ✅ Limpar TODAS as listas
        worldOrbs = new ArrayList<>();
        goldOrbs = new ArrayList<>();
        flyingEnemies = new ArrayList<>();


        // Plataforma inicial
        platforms.add(new Platform(0, BASE_HEIGHT, 300, 20));

        // Sequência inicial
        platforms.add(new Platform(250, BASE_HEIGHT - 30, 120, 20));
        platforms.add(new Platform(420, BASE_HEIGHT - 60, 100, 20));
        platforms.add(new Platform(580, BASE_HEIGHT - 40, 150, 20));
        platforms.add(new Platform(780, BASE_HEIGHT - 70, 120, 20));
        platforms.add(new Platform(950, BASE_HEIGHT - 30, 140, 20));

        // Gera o restante até 345
        generateExactPlatforms(platforms, 345);

        int cianoOrbsCreated = 0;
        int goldOrbsCreated = 0;

        // ✅ Criar orbs
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = platforms.get(i);
            float orbX = p.getX() + p.getWidth() / 2f - GameConfig.ORB_SIZE / 2f;
            float orbY = p.getY() - 40f;

            // A cada 5 plataformas = ORB AMARELO (raro e valioso)
            if (i % 5 == 0 && i > 0) {
                org.example.objects.EnergyOrb goldOrb = new org.example.objects.EnergyOrb(
                        (int)orbX,
                        (int)orbY,
                        20
                );
                goldOrbs.add(goldOrb);
                goldOrbsCreated++;
            }
            // Outras plataformas = ORB CIANO (comum)
            else {
                EnergyOrbEntity cianoOrb = new EnergyOrbEntity(orbX, orbY, 10, Color.CYAN);
                worldOrbs.add(cianoOrb);
                cianoOrbsCreated++;
            }
        }

        flyingEnemies = createInitialFlyingEnemies();
        worldPlatforms = platforms;
        totalPlatformsGenerated = platforms.size();
=======
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import java.util.ArrayList;

public class WorldBuilder {

    public static ArrayList<Platform> createPlatformsForLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevel1Platforms();
            case 2:
                return createLevel2Platforms();
            case 3:
                return createLevel3Platforms();
            default:
                return createEndlessPlatforms();
        }
    }

    public static ArrayList<Enemy> createEnemiesForLevel(int levelNumber, float difficultyMultiplier) {
        switch (levelNumber) {
            case 1:
                return createLevel1Enemies(difficultyMultiplier);
            case 2:
                return createLevel2Enemies(difficultyMultiplier);
            case 3:
                return createLevel3Enemies(difficultyMultiplier);
            default:
                return createEndlessEnemies(difficultyMultiplier);
        }
    }

    public static ArrayList<EnergyOrb> createEnergyOrbsForLevel(int levelNumber, float difficultyMultiplier) {
        switch (levelNumber) {
            case 1:
                return createLevel1Orbs();
            case 2:
                return createLevel2Orbs();
            case 3:
                return createLevel3Orbs();
            default:
                return createEndlessOrbs();
        }
    }

    // === FASE 1 - TUTORIAL ===
    private static ArrayList<Platform> createLevel1Platforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // Plataformas básicas - design simples para tutorial
        platforms.add(new Platform(0, 400, 300, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(400, 350, 150, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(650, 300, 200, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(950, 250, 150, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1200, 200, 200, 20, Platform.PlatformType.BRICK));

        // Seção final
        platforms.add(new Platform(1500, 250, 300, 20, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1700, 200, 200, 100, Platform.PlatformType.GROUND)); // Plataforma do objetivo
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

        return platforms;
    }

<<<<<<< HEAD
    private static void generateExactPlatforms(ArrayList<Platform> platforms, int targetCount) {
        java.util.Random random = new java.util.Random();
        int currentCount = platforms.size();
        int targetX = 1200;

        while (currentCount < targetCount) {
            int width = 80 + random.nextInt(120);
            int height = 20;
            int y = BASE_HEIGHT - random.nextInt(MAX_HEIGHT_VARIATION);

            if (currentCount > 0) {
                Platform lastPlatform = platforms.get(currentCount - 1);
                int minX = lastPlatform.x + lastPlatform.width + MIN_PLATFORM_GAP;
                int maxX = lastPlatform.x + lastPlatform.width + MAX_PLATFORM_GAP;
                targetX = minX + random.nextInt(maxX - minX);
            }

            platforms.add(new Platform(targetX, y, width, height));
            currentCount++;

            targetX += width + MIN_PLATFORM_GAP + random.nextInt(MAX_PLATFORM_GAP - MIN_PLATFORM_GAP);
        }
    }

    /**
     * ✅ CORRIGIDO: Cria inimigos voadores de forma confiável
     * - A cada 8 plataformas
     * - 100% de chance de criação
     * - Sem recriação de inimigos terrestres
     */
    public static ArrayList<org.example.objects.FlyingEnemy> createInitialFlyingEnemies() {
        ArrayList<org.example.objects.FlyingEnemy> newFlyingEnemies = new ArrayList<>();
        Random random = new Random();

        for (int i = 8; i < worldPlatforms.size(); i += 8) {
            Platform platform = worldPlatforms.get(i);


            int flyingX = platform.x + platform.width / 2;
            int flyingY = platform.y - 150; // Bem acima da plataforma

            // Padrão aleatório
            org.example.objects.FlyingEnemy.MovementPattern pattern =
                    org.example.objects.FlyingEnemy.MovementPattern.values()[
                            random.nextInt(org.example.objects.FlyingEnemy.MovementPattern.values().length)
                            ];

            float speed = 2.0f + random.nextFloat() * 1.5f; // 2.0 a 3.5

            org.example.objects.FlyingEnemy enemy = new org.example.objects.FlyingEnemy(
                    flyingX, flyingY, pattern, speed, i
            );

            newFlyingEnemies.add(enemy);

        }
        return newFlyingEnemies;
    }

    public static void updateInfiniteWorld(ArrayList<Platform> platforms, int playerX) {
        infiniteSystem.update(playerX);

        if (infiniteSystem.shouldGenerateContent(playerX)) {
            generateNewPlatforms(platforms, playerX);
        }

        cleanupOldPlatforms(platforms, playerX);
    }

    private static void generateNewPlatforms(ArrayList<Platform> platforms, int playerX) {
        int startX = playerX + RENDER_DISTANCE;
        int endX = startX + 800;

        ArrayList<InfiniteWorldSystem.PlatformData> newPlatformData =
                infiniteSystem.generatePlatforms(startX, endX);

        for (InfiniteWorldSystem.PlatformData data : newPlatformData) {
            platforms.add(new Platform(data.x, data.y, data.width, data.height));
        }
    }

    private static void cleanupOldPlatforms(ArrayList<Platform> platforms, int playerX) {
        int cleanupX = playerX - CLEANUP_DISTANCE;
        final int[] removedCount = {0};

        platforms.removeIf(platform -> {
            if (platform.x < cleanupX) {
                removedCount[0]++;
                return true;
            }
            return false;
        });
    }

    public static void updateOrbs(int playerX) {
        worldOrbs.removeIf(orb -> orb.getX() < playerX - CLEANUP_DISTANCE);
        goldOrbs.removeIf(orb -> orb.x < playerX - CLEANUP_DISTANCE);
    }

    public static ArrayList<EnergyOrbEntity> getVisibleOrbs(int cameraX, int screenWidth) {
        ArrayList<EnergyOrbEntity> visibleOrbs = new ArrayList<>();
        int minX = cameraX - 200;
        int maxX = cameraX + screenWidth + 200;

        for (EnergyOrbEntity orb : worldOrbs) {
            float orbX = orb.getX();
            if (orbX >= minX && orbX <= maxX) {
                visibleOrbs.add(orb);
            }
        }

        return visibleOrbs;
    }

    public static void renderWorldOrbs(java.awt.Graphics2D g2d, int cameraX, int cameraY) {
        // Renderizar orbs CIANOS
        for (EnergyOrbEntity orb : worldOrbs) {
            if (!orb.isCollected()) {
                float orbX = orb.getX();
                float orbY = orb.getY();

                g2d.setColor(Color.CYAN);
                g2d.fillOval((int)orbX - 8, (int)orbY - 8, 16, 16);

                g2d.setColor(new java.awt.Color(0, 255, 255, 100));
                g2d.fillOval((int)orbX - 12, (int)orbY - 12, 24, 24);

                g2d.setColor(java.awt.Color.WHITE);
                g2d.setStroke(new java.awt.BasicStroke(1));
                g2d.drawOval((int)orbX - 8, (int)orbY - 8, 16, 16);
            }
        }

        // Renderizar orbs AMARELOS (raros)
        for (org.example.objects.EnergyOrb orb : goldOrbs) {
            if (!orb.isCollected()) {
                int orbX = orb.x;
                int orbY = orb.y;

                g2d.setColor(new java.awt.Color(255, 215, 0));
                g2d.fillOval(orbX - 10, orbY - 10, 20, 20);

                g2d.setColor(new java.awt.Color(255, 255, 0, 150));
                g2d.fillOval(orbX - 14, orbY - 14, 28, 28);

                g2d.setColor(new java.awt.Color(255, 215, 0, 80));
                g2d.fillOval(orbX - 18, orbY - 18, 36, 36);

                g2d.setColor(java.awt.Color.WHITE);
                g2d.setStroke(new java.awt.BasicStroke(2));
                g2d.drawOval(orbX - 10, orbY - 10, 20, 20);

                g2d.setColor(java.awt.Color.WHITE);
                g2d.fillOval(orbX - 2, orbY - 2, 4, 4);
=======
    private static ArrayList<Enemy> createLevel1Enemies(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Poucos inimigos para tutorial - posicionados SOBRE as plataformas
        // Plataforma em Y=350, inimigo em Y=330 (20 pixels acima da plataforma)
        enemies.add(new Enemy(500, 330, 400, 600)); // Na plataforma de Y=350
        // Plataforma em Y=250, inimigo em Y=230 (20 pixels acima da plataforma)
        enemies.add(new Enemy(1000, 230, 900, 1100)); // Na plataforma de Y=250

        return enemies;
    }

    private static ArrayList<EnergyOrb> createLevel1Orbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();

        // Orbs básicos espalhados
        orbs.add(new EnergyOrb(350, 320, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(700, 270, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(1000, 220, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(1350, 170, EnergyOrb.LARGE_ORB));

        return orbs;
    }

    // === FASE 2 - INTERMEDIÁRIA ===
    private static ArrayList<Platform> createLevel2Platforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // Design mais complexo
        platforms.add(new Platform(0, 450, 200, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(300, 400, 100, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(500, 350, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(680, 300, 120, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(900, 250, 100, 20, Platform.PlatformType.BRICK));

        // Seção do meio com desafios
        platforms.add(new Platform(1100, 200, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1250, 180, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1400, 160, 80, 20, Platform.PlatformType.BRICK));

        // Seção de plataformas móveis simuladas
        platforms.add(new Platform(1600, 220, 100, 20, Platform.PlatformType.CLOUD));
        platforms.add(new Platform(1800, 180, 100, 20, Platform.PlatformType.CLOUD));

        // Área final
        platforms.add(new Platform(2000, 250, 400, 20, Platform.PlatformType.GROUND));
        platforms.add(new Platform(2300, 200, 300, 150, Platform.PlatformType.GROUND)); // Objetivo

        return platforms;
    }

    private static ArrayList<Enemy> createLevel2Enemies(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Mais inimigos, posicionados corretamente sobre as plataformas
        // Plataforma em Y=400, inimigo em Y=380 (20 pixels acima)
        enemies.add(new Enemy(350, 380, 300, 400));
        // Plataforma em Y=300, inimigo em Y=280 (20 pixels acima)
        enemies.add(new Enemy(750, 280, 680, 800));
        // Plataforma em Y=200, inimigo em Y=180 (20 pixels acima)
        enemies.add(new Enemy(1150, 180, 1100, 1160));
        // Plataforma em Y=220, inimigo em Y=200 (20 pixels acima)
        enemies.add(new Enemy(1650, 200, 1600, 1700));
        // Plataforma em Y=250, inimigo em Y=230 (20 pixels acima)
        enemies.add(new Enemy(2100, 230, 2000, 2400));

        return enemies;
    }

    private static ArrayList<EnergyOrb> createLevel2Orbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();

        // Mix de orbs com alguns raros
        orbs.add(new EnergyOrb(250, 420, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(550, 320, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(780, 270, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(1000, 220, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(1300, 140, EnergyOrb.RARE_ORB)); // Orb raro
        orbs.add(new EnergyOrb(1700, 150, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(2000, 220, EnergyOrb.LARGE_ORB));

        return orbs;
    }

    // === FASE 3 - AVANÇADA ===
    private static ArrayList<Platform> createLevel3Platforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // Plataforma inicial sólida
        platforms.add(new Platform(0, 450, 200, 50, Platform.PlatformType.GROUND));

        // Seção 1: Sequência de plataformas em escada (distância máxima: 180px)
        platforms.add(new Platform(250, 420, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(400, 380, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(550, 340, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(700, 300, 80, 20, Platform.PlatformType.BRICK));

        // Seção 2: Plataformas em zigzag (desafio de timing)
        platforms.add(new Platform(850, 260, 100, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1000, 320, 100, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1150, 280, 100, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1300, 240, 100, 20, Platform.PlatformType.BRICK));

        // Seção 3: Torre escalável (plataformas empilhadas)
        platforms.add(new Platform(1450, 350, 120, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1450, 300, 120, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1450, 250, 120, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1450, 200, 120, 20, Platform.PlatformType.BRICK));

        // Seção 4: Saltos horizontais controlados
        platforms.add(new Platform(1650, 180, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1800, 200, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1950, 180, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2100, 200, 80, 20, Platform.PlatformType.BRICK));

        // Seção 5: Plataformas móveis (nuvens) - desafio final
        platforms.add(new Platform(2250, 160, 100, 20, Platform.PlatformType.CLOUD));
        platforms.add(new Platform(2400, 180, 100, 20, Platform.PlatformType.CLOUD));
        platforms.add(new Platform(2550, 160, 100, 20, Platform.PlatformType.CLOUD));

        // Área final e objetivo
        platforms.add(new Platform(2700, 200, 200, 20, Platform.PlatformType.GROUND));
        platforms.add(new Platform(2900, 150, 300, 100, Platform.PlatformType.GROUND)); // Objetivo

        return platforms;
    }

    private static ArrayList<Enemy> createLevel3Enemies(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Inimigos posicionados estrategicamente SOBRE as plataformas (20px acima)
        // Seção 1: Escada - inimigos em plataformas alternadas
        enemies.add(new Enemy(290, 400, 250, 330)); // Plataforma Y=420
        enemies.add(new Enemy(440, 360, 400, 480)); // Plataforma Y=380
        enemies.add(new Enemy(590, 320, 550, 630)); // Plataforma Y=340

        // Seção 2: Zigzag - inimigos em posições estratégicas
        enemies.add(new Enemy(890, 240, 850, 930)); // Plataforma Y=260
        enemies.add(new Enemy(1040, 300, 1000, 1080)); // Plataforma Y=320
        enemies.add(new Enemy(1190, 260, 1150, 1230)); // Plataforma Y=280

        // Seção 3: Torre - inimigos nas plataformas mais altas
        enemies.add(new Enemy(1490, 230, 1450, 1530)); // Torre Y=250
        enemies.add(new Enemy(1490, 180, 1450, 1530)); // Torre Y=200

        // Seção 4: Saltos horizontais - inimigos em posições desafiadoras
        enemies.add(new Enemy(1690, 160, 1650, 1730)); // Plataforma Y=180
        enemies.add(new Enemy(1840, 180, 1800, 1880)); // Plataforma Y=200
        enemies.add(new Enemy(1990, 160, 1950, 2030)); // Plataforma Y=180

        // Seção 5: Nuvens - inimigos nas plataformas móveis
        enemies.add(new Enemy(2290, 140, 2250, 2330)); // Nuvem Y=160
        enemies.add(new Enemy(2440, 160, 2400, 2480)); // Nuvem Y=180

        // Área final - guardião do objetivo
        enemies.add(new Enemy(2750, 180, 2700, 2800)); // Plataforma final Y=200

        return enemies;
    }

    private static ArrayList<EnergyOrb> createLevel3Orbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();

        // Orbs premium distribuídos estrategicamente no novo layout
        // Seção 1: Escada - orbs em plataformas seguras
        orbs.add(new EnergyOrb(280, 400, EnergyOrb.MEDIUM_ORB)); // Plataforma Y=420
        orbs.add(new EnergyOrb(430, 360, EnergyOrb.LARGE_ORB)); // Plataforma Y=380
        orbs.add(new EnergyOrb(580, 320, EnergyOrb.RARE_ORB)); // Plataforma Y=340

        // Seção 2: Zigzag - orbs em posições desafiadoras
        orbs.add(new EnergyOrb(880, 240, EnergyOrb.MEDIUM_ORB)); // Plataforma Y=260
        orbs.add(new EnergyOrb(1030, 300, EnergyOrb.LARGE_ORB)); // Plataforma Y=320
        orbs.add(new EnergyOrb(1180, 260, EnergyOrb.RARE_ORB)); // Plataforma Y=280

        // Seção 3: Torre - orbs nas plataformas altas (recompensas especiais)
        orbs.add(new EnergyOrb(1480, 230, EnergyOrb.LEGENDARY_ORB)); // Torre Y=250
        orbs.add(new EnergyOrb(1480, 180, EnergyOrb.LEGENDARY_ORB)); // Torre Y=200

        // Seção 4: Saltos horizontais - orbs em posições estratégicas
        orbs.add(new EnergyOrb(1680, 160, EnergyOrb.MEDIUM_ORB)); // Plataforma Y=180
        orbs.add(new EnergyOrb(1830, 180, EnergyOrb.LARGE_ORB)); // Plataforma Y=200
        orbs.add(new EnergyOrb(1980, 160, EnergyOrb.RARE_ORB)); // Plataforma Y=180

        // Seção 5: Nuvens - orbs raros nas plataformas móveis
        orbs.add(new EnergyOrb(2280, 140, EnergyOrb.LEGENDARY_ORB)); // Nuvem Y=160
        orbs.add(new EnergyOrb(2430, 160, EnergyOrb.RARE_ORB)); // Nuvem Y=180

        // Área final - recompensa máxima
        orbs.add(new EnergyOrb(2740, 180, EnergyOrb.LEGENDARY_ORB)); // Plataforma final Y=200
        orbs.add(new EnergyOrb(2950, 130, EnergyOrb.LEGENDARY_ORB)); // Objetivo Y=150

        return orbs;
    }

    // === FASE INFINITA ===
    // === FASE INFINITA - ABISMO DA AURORA ===
    private static ArrayList<Platform> createEndlessPlatforms() {
        return createAuroraAbyssPlatforms();
    }
    
    /**
     * Cria a fase "Abismo da Aurora" - ambiente de caverna com cristais
     */
    private static ArrayList<Platform> createAuroraAbyssPlatforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // === ZONA SEGURA (INÍCIO) ===
        // Clareira iluminada por cristal gigante
        platforms.add(new Platform(0, 450, 200, 50, Platform.PlatformType.GROUND)); // Base sólida
        platforms.add(new Platform(50, 400, 100, 20, Platform.PlatformType.BRICK)); // Plataforma de checkpoint
        
        // === PRIMEIRA ÁREA - DESAFIO DE SALTOS BÁSICOS ===
        // Plataformas de pedra em diferentes alturas
        platforms.add(new Platform(250, 420, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(380, 380, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(500, 340, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(650, 300, 70, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(800, 360, 90, 20, Platform.PlatformType.BRICK));
        
        // === SEGUNDA ÁREA - MOBILIDADE AVANÇADA ===
        // Paredes para wall-jump (plataformas estreitas verticais)
        platforms.add(new Platform(950, 200, 20, 200, Platform.PlatformType.PIPE)); // Parede esquerda
        platforms.add(new Platform(1100, 150, 20, 250, Platform.PlatformType.PIPE)); // Parede direita
        
        // Plataformas móveis que deslizam
        platforms.add(new Platform(1000, 350, 80, 20, Platform.PlatformType.MOVING, 1200, 350, 1.0f));
        platforms.add(new Platform(1050, 280, 60, 20, Platform.PlatformType.MOVING, 1250, 280, 0.8f));
        
        // Blocos que desaparecem (plataformas quebráveis)
        platforms.add(new Platform(1300, 320, 60, 20, Platform.PlatformType.BREAKABLE));
        platforms.add(new Platform(1400, 280, 60, 20, Platform.PlatformType.BREAKABLE));
        platforms.add(new Platform(1500, 240, 60, 20, Platform.PlatformType.BREAKABLE));
        
        // === TERCEIRA ÁREA - EXPLORAÇÃO OPCIONAL ===
        // Caminho alternativo mais difícil
        platforms.add(new Platform(1600, 200, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1700, 160, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1800, 120, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1900, 100, 40, 20, Platform.PlatformType.BRICK)); // Fragmento de cristal aqui
        
        // Caminho principal
        platforms.add(new Platform(1600, 350, 100, 20, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1750, 320, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1900, 300, 100, 20, Platform.PlatformType.BRICK));
        
        // === ÁREA DE DESCIDA - CONTROLE DE QUEDA ===
        // Corredor vertical com plataformas pequenas
        platforms.add(new Platform(2100, 100, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2200, 150, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2100, 200, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2200, 250, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2100, 300, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2200, 350, 30, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2100, 400, 30, 20, Platform.PlatformType.BRICK));
        
        // === ÁREA FINAL - GRANDE DESAFIO ===
        // Plataformas estreitas sobre espinhos
        platforms.add(new Platform(2400, 200, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2500, 180, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2600, 200, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2700, 180, 40, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2800, 200, 40, 20, Platform.PlatformType.BRICK));
        
        // Plataforma final com saída iluminada
        platforms.add(new Platform(2900, 150, 200, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(3000, 100, 100, 100, Platform.PlatformType.GROUND)); // Saída
        
        // === PLATAFORMAS ESPECIAIS TEMÁTICAS ===
        // Cristais que servem como plataformas
        platforms.add(new Platform(1200, 400, 60, 20, Platform.PlatformType.ICE)); // Cristal de gelo
        platforms.add(new Platform(1350, 380, 60, 20, Platform.PlatformType.BOUNCY)); // Cristal elástico
        
        // Raízes grossas
        platforms.add(new Platform(1800, 400, 120, 30, Platform.PlatformType.GROUND));
        platforms.add(new Platform(2000, 450, 100, 30, Platform.PlatformType.GROUND));
        
        return platforms;
    }

    // Variáveis para controle de posição
    private static int nextX, nextY;

    private static void calculateNextPlatformPosition(int currentX, int currentY, int width, int index) {
        // Distância horizontal reduzida para melhor jogabilidade
        int minDistance = 60; // Distância mínima
        int maxDistance = 120; // Distância máxima
        
        nextX = currentX + width + minDistance + (int)(Math.random() * (maxDistance - minDistance));
        
        // Variação vertical mais controlada
        int verticalVariation = 40 + (int)(Math.random() * 60); // 40-100 pixels
        if (Math.random() < 0.5) {
            nextY = Math.max(150, currentY - verticalVariation); // Para cima
        } else {
            nextY = Math.min(500, currentY + verticalVariation); // Para baixo
        }
        
        // Criar padrões interessantes a cada grupo de plataformas
        if (index % 8 == 0) {
            createPatternGroup(nextX, nextY);
        }
    }

    private static Platform.PlatformType choosePlatformType(int count, int index) {
        // Distribuição balanceada de tipos de plataforma
        if (count < 5) {
            return Platform.PlatformType.BRICK; // Começo sempre normal
        }
        
        double random = Math.random();
        
        // A cada 10 plataformas, garantir uma especial
        if (index % 10 == 0) {
            Platform.PlatformType[] specialTypes = {
                Platform.PlatformType.MOVING,
                Platform.PlatformType.BOUNCY,
                Platform.PlatformType.ICE
            };
            return specialTypes[(int)(Math.random() * specialTypes.length)];
        }
        
        // Distribuição normal
        if (random < 0.6) return Platform.PlatformType.BRICK;
        else if (random < 0.7) return Platform.PlatformType.GROUND;
        else if (random < 0.8) return Platform.PlatformType.PIPE;
        else if (random < 0.85) return Platform.PlatformType.MOVING;
        else if (random < 0.9) return Platform.PlatformType.ICE;
        else if (random < 0.95) return Platform.PlatformType.BOUNCY;
        else if (random < 0.98) return Platform.PlatformType.BREAKABLE;
        else return Platform.PlatformType.ONE_WAY;
    }

    private static Platform createStyledPlatform(int x, int y, int width, int height, 
                                               Platform.PlatformType type, int index) {
        Platform platform;
        
        switch (type) {
            case MOVING:
                // Plataforma móvel com movimento interessante
                int moveRange = 80 + (int)(Math.random() * 60);
                float moveSpeed = 0.8f + (float)(Math.random() * 1.2f);
                platform = new Platform(x, y, width, height, type, x + moveRange, y, moveSpeed);
                break;
                
            case BOUNCY:
                // Plataforma elástica com bounce customizado
                float bounceForce = 1.5f + (float)(Math.random() * 1.0f);
                platform = new Platform(x, y, width, height, type, bounceForce);
                break;
                
            case BREAKABLE:
                // Plataforma quebrável com tempo variado
                platform = new Platform(x, y, width, height, type);
                break;
                
            default:
                platform = new Platform(x, y, width, height, type);
                break;
        }
        
        return platform;
    }

    private static void createPatternGroup(int startX, int startY) {
        // Criar grupos de plataformas em padrões interessantes
        // Este método pode ser expandido com padrões específicos
    }

    private static void addDecorativePlatforms(ArrayList<Platform> platforms, int nearX, int nearY) {
        // Plataformas pequenas decorativas
        if (Math.random() < 0.5) {
            int decorX = nearX - 30 - (int)(Math.random() * 20);
            int decorY = nearY - 30 - (int)(Math.random() * 20);
            platforms.add(new Platform(decorX, decorY, 25, 8, Platform.PlatformType.GROUND));
        }
    }

    private static void addStrategicSpecialPlatforms(ArrayList<Platform> platforms) {
        // Adicionar plataformas especiais em posições estratégicas
        int baseX = 200;
        
        for (int i = 0; i < 5; i++) { // Reduzido para 5 para evitar muitos teleporters
            int x = baseX + (i * 500) + (int)(Math.random() * 100);
            int y = 200 + (int)(Math.random() * 200);
            
            if (i % 2 == 0) {
                // Plataformas especiais ocasionais
                platforms.add(new Platform(x, y, 80, 20, Platform.PlatformType.BOUNCY));
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            }
        }
    }

<<<<<<< HEAD
    public static ArrayList<org.example.objects.Enemy> createInitialEnemies() {
        ArrayList<org.example.objects.Enemy> enemies = new ArrayList<>();
        Random random = new Random();

        if (worldPlatforms.isEmpty()) {
            return enemies;
        }

        boolean shouldHaveEnemy = false;

        for (int i = 1; i < worldPlatforms.size(); i++) {
            Platform platform = worldPlatforms.get(i);

            if (platform.width >= 150) {
                if (shouldHaveEnemy) {
                    int enemyX = platform.x + platform.width / 3;
                    int enemyY = platform.y - 50;
                    enemies.add(new org.example.objects.Enemy(enemyX, enemyY));

                    if (random.nextDouble() < 0.3) {
                        int enemyX2 = platform.x + (2 * platform.width / 3);
                        enemies.add(new org.example.objects.Enemy(enemyX2, enemyY));
                    }
                }
            } else if (platform.width >= 100) {
                if (shouldHaveEnemy && random.nextDouble() < 0.7) {
                    int enemyX = platform.x + platform.width / 2;
                    int enemyY = platform.y - 50;
                    enemies.add(new org.example.objects.Enemy(enemyX, enemyY));
                }
            }

            shouldHaveEnemy = !shouldHaveEnemy;
=======
    // === INIMIGOS PARA ABISMO DA AURORA ===
    private static ArrayList<Enemy> createEndlessEnemies(float difficulty) {
        return createAuroraAbyssEnemies(difficulty);
    }
    
    /**
     * Cria inimigos para a fase Abismo da Aurora
     */
    private static ArrayList<Enemy> createAuroraAbyssEnemies(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Poucos inimigos terrestres - foco nos voadores
        // Inimigo na primeira área
        enemies.add(new Enemy(300, 400, 250, 350));
        
        // Inimigo na área de descida
        enemies.add(new Enemy(2150, 380, 2100, 2200));
        
        // Inimigo na área final
        enemies.add(new Enemy(2450, 180, 2400, 2500));

        return enemies;
    }
    
    /**
     * Cria inimigos voadores para a fase Abismo da Aurora
     */
    public static ArrayList<org.example.objects.FlyingEnemy> createAuroraAbyssFlyingEnemies() {
        ArrayList<org.example.objects.FlyingEnemy> flyingEnemies = new ArrayList<>();

        // Inimigos voadores na segunda área (mobilidade avançada)
        flyingEnemies.add(new org.example.objects.FlyingEnemy(1000, 250, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.HORIZONTAL_PATROL, 1.0f));
        flyingEnemies.add(new org.example.objects.FlyingEnemy(1150, 200, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.VERTICAL_PATROL, 0.8f));

        // Inimigos voadores na área de exploração opcional
        flyingEnemies.add(new org.example.objects.FlyingEnemy(1650, 150, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.CIRCULAR, 1.2f));
        flyingEnemies.add(new org.example.objects.FlyingEnemy(1850, 80, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.HOVER, 0.6f));

        // Inimigos voadores na área final (grande desafio)
        flyingEnemies.add(new org.example.objects.FlyingEnemy(2450, 150, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.DIVE_BOMB, 1.5f));
        flyingEnemies.add(new org.example.objects.FlyingEnemy(2650, 150, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.FIGURE_EIGHT, 1.0f));
        flyingEnemies.add(new org.example.objects.FlyingEnemy(2750, 150, 16, 16, 
            org.example.objects.FlyingEnemy.MovementPattern.HORIZONTAL_PATROL, 1.2f));

        return flyingEnemies;
    }
    
    /**
     * Cria espinhos para a fase Abismo da Aurora
     */
    public static ArrayList<org.example.world.Spike> createAuroraAbyssSpikes() {
        ArrayList<org.example.world.Spike> spikes = new ArrayList<>();

        // === PRIMEIRA ÁREA - ESPINHOS NO CHÃO ===
        // Buracos entre plataformas com espinhos
        spikes.add(new org.example.world.Spike(330, 470, 50, 30, org.example.world.Spike.SpikeType.FLOOR));
        spikes.add(new org.example.world.Spike(450, 470, 50, 30, org.example.world.Spike.SpikeType.FLOOR));
        spikes.add(new org.example.world.Spike(580, 470, 70, 30, org.example.world.Spike.SpikeType.FLOOR));
        spikes.add(new org.example.world.Spike(720, 470, 80, 30, org.example.world.Spike.SpikeType.FLOOR));

        // === SEGUNDA ÁREA - ESPINHOS NO TETO E PAREDES ===
        // Espinhos no teto da área de wall-jump
        spikes.add(new org.example.world.Spike(950, 180, 20, 20, org.example.world.Spike.SpikeType.CEILING));
        spikes.add(new org.example.world.Spike(1100, 130, 20, 20, org.example.world.Spike.SpikeType.CEILING));
        
        // Espinhos nas paredes laterais
        spikes.add(new org.example.world.Spike(930, 250, 20, 100, org.example.world.Spike.SpikeType.WALL_RIGHT));
        spikes.add(new org.example.world.Spike(1120, 200, 20, 100, org.example.world.Spike.SpikeType.WALL_LEFT));

        // === TERCEIRA ÁREA - ESPINHOS NO CAMINHO OPCIONAL ===
        // Espinhos no teto do caminho difícil
        spikes.add(new org.example.world.Spike(1600, 80, 40, 20, org.example.world.Spike.SpikeType.CEILING));
        spikes.add(new org.example.world.Spike(1700, 40, 40, 20, org.example.world.Spike.SpikeType.CEILING));
        spikes.add(new org.example.world.Spike(1800, 0, 40, 20, org.example.world.Spike.SpikeType.CEILING));
        spikes.add(new org.example.world.Spike(1900, 0, 40, 20, org.example.world.Spike.SpikeType.CEILING));

        // === ÁREA DE DESCIDA - ESPINHOS LATERAIS ===
        // Espinhos nas paredes do corredor vertical
        spikes.add(new org.example.world.Spike(2070, 100, 30, 350, org.example.world.Spike.SpikeType.WALL_RIGHT));
        spikes.add(new org.example.world.Spike(2230, 100, 30, 350, org.example.world.Spike.SpikeType.WALL_LEFT));

        // === ÁREA FINAL - ESPINHOS NO CHÃO ===
        // Lago de espinhos sob as plataformas estreitas
        spikes.add(new org.example.world.Spike(2400, 470, 500, 30, org.example.world.Spike.SpikeType.FLOOR));

        return spikes;
    }
    
    /**
     * Cria fragmentos de cristal para a fase Abismo da Aurora
     */
    public static ArrayList<org.example.objects.CrystalFragment> createAuroraAbyssCrystalFragments() {
        ArrayList<org.example.objects.CrystalFragment> fragments = new ArrayList<>();

        // === FRAGMENTOS BÁSICOS (AZUIS) ===
        // Primeira área - fácil acesso
        fragments.add(new org.example.objects.CrystalFragment(280, 400, 
            org.example.objects.CrystalFragment.FragmentType.BLUE_CRYSTAL));
        fragments.add(new org.example.objects.CrystalFragment(420, 360, 
            org.example.objects.CrystalFragment.FragmentType.BLUE_CRYSTAL));
        fragments.add(new org.example.objects.CrystalFragment(540, 320, 
            org.example.objects.CrystalFragment.FragmentType.BLUE_CRYSTAL));

        // === FRAGMENTOS MÉDIOS (ROXOS) ===
        // Segunda área - requer timing
        fragments.add(new org.example.objects.CrystalFragment(1020, 330, 
            org.example.objects.CrystalFragment.FragmentType.PURPLE_CRYSTAL));
        fragments.add(new org.example.objects.CrystalFragment(1320, 300, 
            org.example.objects.CrystalFragment.FragmentType.PURPLE_CRYSTAL));

        // === FRAGMENTOS ALTOS (DOURADOS) ===
        // Área de exploração opcional - caminho difícil
        fragments.add(new org.example.objects.CrystalFragment(1920, 80, 
            org.example.objects.CrystalFragment.FragmentType.GOLDEN_CRYSTAL));
        
        // Área de descida - posições estratégicas
        fragments.add(new org.example.objects.CrystalFragment(2120, 80, 
            org.example.objects.CrystalFragment.FragmentType.GOLDEN_CRYSTAL));
        fragments.add(new org.example.objects.CrystalFragment(2220, 130, 
            org.example.objects.CrystalFragment.FragmentType.GOLDEN_CRYSTAL));

        // === FRAGMENTOS MÁXIMOS (AURORA) ===
        // Área final - recompensa máxima
        fragments.add(new org.example.objects.CrystalFragment(2520, 160, 
            org.example.objects.CrystalFragment.FragmentType.AURORA_CRYSTAL));
        fragments.add(new org.example.objects.CrystalFragment(2720, 160, 
            org.example.objects.CrystalFragment.FragmentType.AURORA_CRYSTAL));
        
        // Fragmento final na saída
        fragments.add(new org.example.objects.CrystalFragment(3050, 80, 
            org.example.objects.CrystalFragment.FragmentType.AURORA_CRYSTAL));

        return fragments;
    }

    // === INIMIGOS COM MELHOR DISTRIBUIÇÃO (MÉTODO ORIGINAL) ===
    private static ArrayList<Enemy> createEndlessEnemiesOriginal(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Densidade maior de inimigos com patrulhamento inteligente
        for (int i = 0; i < 35; i++) { // Mais inimigos
            int x = 150 + (i * 250) + (int)(Math.random() * 100); // Distância menor
            int y = 200 + (int)(Math.random() * 200);
            
            // Patrulhamento baseado na dificuldade
            int patrolRange = (int)(80 * difficulty) + 50;
            int patrolLeft = x - patrolRange;
            int patrolRight = x + patrolRange;
            
            Enemy enemy = new Enemy(x, y, patrolLeft, patrolRight);
            
            // Adicionar variação baseada na dificuldade (se a classe Enemy suportar no futuro)
            // if (difficulty > 1.5f && Math.random() < 0.3) {
            //     enemy.setEnemyType("FAST");
            // } else if (difficulty > 2.0f && Math.random() < 0.2) {
            //     enemy.setEnemyType("STRONG");
            // }
            
            enemies.add(enemy);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }

        return enemies;
    }

<<<<<<< HEAD
    // GETTERS
    public static ArrayList<EnergyOrbEntity> getWorldOrbs() {
        return worldOrbs;
    }

    public static ArrayList<org.example.objects.EnergyOrb> getGoldOrbs() {
        return goldOrbs;
    }

    public static ArrayList<org.example.objects.FlyingEnemy> getFlyingEnemies() {
        return flyingEnemies;
=======
    // === ORBS COM DISTRIBUIÇÃO MELHORADA ===
    private static ArrayList<EnergyOrb> createEndlessOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();

        // Mais orbs com distribuição inteligente
        for (int i = 0; i < 50; i++) { // Mais orbs para coleta
            int x = 80 + (i * 200) + (int)(Math.random() * 80); // Distância menor
            int y = 80 + (int)(Math.random() * 350);

            // Sistema de raridade melhorado
            int energyValue = calculateOrbValue(i);
            
            EnergyOrb orb = new EnergyOrb(x, y, energyValue);
            
            // Adicionar efeito visual baseado no valor (se a classe suportar no futuro)
            // if (energyValue >= EnergyOrb.RARE_ORB) {
            //     orb.setVisualEffect("GLOW");
            // }
            // if (energyValue == EnergyOrb.LEGENDARY_ORB) {
            //     orb.setVisualEffect("SPARKLE");
            // }
            
            orbs.add(orb);
        }
        
        // Adicionar orbs especiais em locais secretos
        addSecretOrbs(orbs);

        return orbs;
    }

    private static int calculateOrbValue(int index) {
        // Sistema de progressão de raridade
        double rarity = Math.random() * 100;
        
        // Orbs mais raros aparecem mais tarde
        double rareBonus = Math.min(index * 0.5, 30); // Bonus até 30%
        
        if (rarity < (40 - rareBonus)) return EnergyOrb.SMALL_ORB;
        else if (rarity < (65 - rareBonus)) return EnergyOrb.MEDIUM_ORB;
        else if (rarity < (80 - rareBonus)) return EnergyOrb.LARGE_ORB;
        else if (rarity < (92 + rareBonus/2)) return EnergyOrb.RARE_ORB;
        else return EnergyOrb.LEGENDARY_ORB;
    }

    private static void addSecretOrbs(ArrayList<EnergyOrb> orbs) {
        // Orbs escondidos em locais difíceis de alcançar
        for (int i = 0; i < 5; i++) {
            int x = 500 + (i * 600);
            int y = 50 + (int)(Math.random() * 100); // Posições altas
            orbs.add(new EnergyOrb(x, y, EnergyOrb.LEGENDARY_ORB));
        }
    }

    // Métodos de compatibilidade com o código existente
    public static ArrayList<Platform> createPlatforms() {
        return createPlatformsForLevel(1);
    }

    public static ArrayList<Enemy> createEnemies() {
        return createEnemiesForLevel(1, 1.0f);
    }

    public static ArrayList<EnergyOrb> createEnergyOrbs() {
        return createEnergyOrbsForLevel(1, 1.0f);
    }

    // === MÉTODOS PARA EFEITOS VISUAIS MELHORADOS ===

    /**
     * Atualiza efeitos visuais das plataformas
     */
    public static void updatePlatformVisuals(ArrayList<Platform> platforms, long deltaTime) {
        for (Platform platform : platforms) {
            // Atualizar animações específicas de cada tipo
            updatePlatformAnimation(platform, deltaTime);
        }
    }

    private static void updatePlatformAnimation(Platform platform, long deltaTime) {
        Platform.PlatformType type = platform.getType();
        
        switch (type) {
            case ICE:
                // Efeito de brilho no gelo
                // platform.setAnimationFrame((int)((System.currentTimeMillis() / 200) % 4));
                break;
                
            case BOUNCY:
                // Animação de elasticidade
                // if (platform.isBeingUsed()) {
                //     platform.setBounceAnimation(true);
                // }
                break;
                
            case BREAKABLE:
                // Animação de rachadura se danificada
                // if (platform.getDamage() > 0) {
                //     platform.setCrackLevel(platform.getDamage());
                // }
                break;
                
            default:
                // Outros tipos não precisam de animação especial
                break;
        }
    }

    /**
     * Desenha efeitos visuais melhorados para plataformas
     */
    public static void drawEnhancedPlatforms(java.awt.Graphics2D g2d, ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            drawPlatformWithEffects(g2d, platform);
        }
    }

    private static void drawPlatformWithEffects(java.awt.Graphics2D g2d, Platform platform) {
        Platform.PlatformType type = platform.getType();
        
        // Desenhar a plataforma base
        platform.draw(g2d);
        
        // Adicionar efeitos específicos
        switch (type) {
            case MOVING:
                drawMovingPlatformTrail(g2d, platform);
                break;
                
            case ICE:
                drawIceGlowEffect(g2d, platform);
                break;
                
            case BOUNCY:
                drawBouncyIndicator(g2d, platform);
                break;
                
            case BREAKABLE:
                drawCrackPattern(g2d, platform);
                break;
                
            default:
                // Outros tipos usam apenas a renderização base
                break;
        }
    }

    // Métodos auxiliares para efeitos visuais específicos
    private static void drawMovingPlatformTrail(java.awt.Graphics2D g2d, Platform platform) {
        // Desenhar rastro da plataforma móvel
        java.awt.Color trailColor = new java.awt.Color(255, 215, 0, 80); // Dourado transparente
        g2d.setColor(trailColor);
        g2d.fillRect(platform.x - 5, platform.y - 2, platform.width + 10, platform.height + 4);
    }

    private static void drawIceGlowEffect(java.awt.Graphics2D g2d, Platform platform) {
        // Desenhar brilho azulado no gelo
        java.awt.Color iceGlow = new java.awt.Color(173, 216, 230, 60); // Azul gelo transparente
        g2d.setColor(iceGlow);
        g2d.fillRect(platform.x - 3, platform.y - 3, platform.width + 6, platform.height + 6);
    }

    private static void drawBouncyIndicator(java.awt.Graphics2D g2d, Platform platform) {
        // Desenhar indicador visual para plataforma elástica
        java.awt.Color bounceColor = new java.awt.Color(255, 20, 147, 100); // Rosa vibrante
        g2d.setColor(bounceColor);
        // Desenhar pequenos arcos indicando bounce
        for (int i = 0; i < 3; i++) {
            int x = platform.x + (platform.width / 4) * (i + 1);
            int y = platform.y - 8 - (i * 2);
            g2d.fillOval(x - 2, y, 4, 4);
        }
    }

    private static void drawCrackPattern(java.awt.Graphics2D g2d, Platform platform) {
        // Desenhar padrão de rachaduras
        java.awt.Color crackColor = new java.awt.Color(80, 40, 20); // Marrom escuro
        g2d.setColor(crackColor);
        g2d.setStroke(new java.awt.BasicStroke(1));
        
        // Rachaduras simples
        int centerX = platform.x + platform.width / 2;
        int centerY = platform.y + platform.height / 2;
        g2d.drawLine(centerX - 10, centerY, centerX + 10, centerY);
        g2d.drawLine(centerX, centerY - 5, centerX, centerY + 5);
    }

    // === NOVOS MÉTODOS PARA PLATAFORMAS ESPECIAIS ===

    /**
     * Cria plataformas especiais para demonstração
     */
    public static ArrayList<Platform> createSpecialPlatforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // Moving Platform - se move horizontalmente
        platforms.add(new Platform(500, 300, 100, 20, Platform.PlatformType.MOVING, 700, 300, 1.5f));

        // Breakable Platform - quebra ao pular
        platforms.add(new Platform(800, 350, 80, 20, Platform.PlatformType.BREAKABLE));

        // Ice Platform - escorregadia
        platforms.add(new Platform(1000, 300, 120, 20, Platform.PlatformType.ICE));

        // Bouncy Platform - faz pular mais alto
        platforms.add(new Platform(1200, 350, 80, 20, Platform.PlatformType.BOUNCY, 2.0f));

        // One-way Platform - só passa por baixo
        platforms.add(new Platform(1400, 300, 100, 20, Platform.PlatformType.ONE_WAY));

        return platforms;
    }

    /**
     * Cria efeitos de vento para demonstração
     */
    public static ArrayList<WindEffect> createWindEffects() {
        ArrayList<WindEffect> windEffects = new ArrayList<>();

        // Vento para a direita
        windEffects.add(new WindEffect(600, 200, 200, 100, 2.0f));

        // Vento para a esquerda
        windEffects.add(new WindEffect(1000, 250, 150, 80, -1.5f));

        return windEffects;
    }

    /**
     * Cria poços de gravidade para demonstração
     */
    public static ArrayList<GravityWell> createGravityWells() {
        ArrayList<GravityWell> gravityWells = new ArrayList<>();

        // Gravidade aumentada (vermelho)
        gravityWells.add(new GravityWell(750, 200, 50, 1.5f));

        // Gravidade reduzida (azul)
        gravityWells.add(new GravityWell(1100, 180, 40, 0.5f));

        return gravityWells;
    }

    /**
     * Cria teleporters para demonstração
     */
    public static ArrayList<Teleporter> createTeleporters() {
        ArrayList<Teleporter> teleporters = new ArrayList<>();

        // Teleporter 1 -> Teleporter 2
        teleporters.add(new Teleporter(900, 250, 60, 20, 1300, 200, "teleporter_1"));
        teleporters.add(new Teleporter(1300, 200, 60, 20, 900, 250, "teleporter_2"));

        return teleporters;
    }

    /**
     * Cria checkpoints para demonstração
     */
    public static ArrayList<Checkpoint> createCheckpoints() {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();

        // Checkpoint no meio da fase
        checkpoints.add(new Checkpoint(800, 200, 40, 40, "checkpoint_1"));

        // Checkpoint próximo ao final
        checkpoints.add(new Checkpoint(1500, 150, 40, 40, "checkpoint_2"));

        return checkpoints;
    }

    // === MÉTODOS PARA INTEGRAR NO GAMEPANEL ===

    /**
     * Aplica efeitos de vento no player
     */
    public static void applyWindEffects(org.example.objects.Player player, ArrayList<WindEffect> windEffects) {
        for (WindEffect wind : windEffects) {
            wind.applyWindEffect(player);
        }
    }

    /**
     * Aplica efeitos de gravidade no player
     */
    public static void applyGravityEffects(org.example.objects.Player player, ArrayList<GravityWell> gravityWells) {
        for (GravityWell well : gravityWells) {
            well.applyGravityEffect(player);
        }
    }

    /**
     * Verifica teleporters
     */
    public static void checkTeleporters(org.example.objects.Player player, ArrayList<Teleporter> teleporters) {
        for (Teleporter teleporter : teleporters) {
            if (teleporter.canTeleport(player)) {
                teleporter.teleportPlayer(player);
            }
        }
    }

    /**
     * Verifica checkpoints
     */
    public static void checkCheckpoints(org.example.objects.Player player, ArrayList<Checkpoint> checkpoints) {
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.canActivate(player)) {
                checkpoint.activate(player);
            }
        }
    }

    /**
     * Atualiza todos os efeitos de ambiente
     */
    public static void updateEnvironmentEffects(ArrayList<WindEffect> windEffects, 
                                              ArrayList<GravityWell> gravityWells,
                                              ArrayList<Teleporter> teleporters,
                                              ArrayList<Checkpoint> checkpoints) {
        // Atualizar efeitos de vento
        for (WindEffect wind : windEffects) {
            wind.update();
        }

        // Atualizar poços de gravidade
        for (GravityWell well : gravityWells) {
            well.update();
        }

        // Atualizar teleporters
        for (Teleporter teleporter : teleporters) {
            teleporter.update();
        }

        // Atualizar checkpoints
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.update();
        }
    }

    /**
     * Desenha todos os efeitos de ambiente
     */
    public static void drawEnvironmentEffects(java.awt.Graphics2D g2d,
                                            ArrayList<WindEffect> windEffects,
                                            ArrayList<GravityWell> gravityWells,
                                            ArrayList<Teleporter> teleporters,
                                            ArrayList<Checkpoint> checkpoints) {
        // Desenhar efeitos de vento
        for (WindEffect wind : windEffects) {
            wind.draw(g2d);
        }

        // Desenhar poços de gravidade
        for (GravityWell well : gravityWells) {
            well.draw(g2d);
        }

        // Desenhar teleporters
        for (Teleporter teleporter : teleporters) {
            teleporter.draw(g2d);
        }

        // Desenhar checkpoints
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.draw(g2d);
        }
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }
}