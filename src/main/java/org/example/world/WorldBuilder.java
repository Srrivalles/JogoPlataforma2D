package org.example.world;

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

        return platforms;
    }

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
            }
        }
    }

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
        }

        return enemies;
    }

    // GETTERS
    public static ArrayList<EnergyOrbEntity> getWorldOrbs() {
        return worldOrbs;
    }

    public static ArrayList<org.example.objects.EnergyOrb> getGoldOrbs() {
        return goldOrbs;
    }

    public static ArrayList<org.example.objects.FlyingEnemy> getFlyingEnemies() {
        return flyingEnemies;
    }
}