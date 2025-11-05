package org.example.world;

import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import java.util.ArrayList;

/**
 * Sistema de progressão de dificuldade que ajusta o mapa baseado no progresso do jogador
 */
public class DifficultyProgressionSystem {
    
    private static int currentZone = 0;
    private static int playerProgress = 0;
    private static double difficultyMultiplier = 1.0;
    
    // Zonas do mapa
    private static final int BASIC_MOVEMENT_ZONE = 0;
    private static final int INTERMEDIATE_ZONE = 1;
    private static final int DASH_ZONE = 2;
    private static final int ADVANCED_ZONE = 3;
    private static final int VERTICAL_ZONE = 4;
    private static final int BOSS_ZONE = 5;
    
    /**
     * Atualiza o progresso do jogador e ajusta a dificuldade
     */
    public static void updateProgress(int playerX, int playerY) {
        // Determinar zona atual baseada na posição X
        currentZone = determineCurrentZone(playerX);
        
        // Calcular progresso dentro da zona
        playerProgress = calculateZoneProgress(playerX, currentZone);
        
        // Ajustar multiplicador de dificuldade
        difficultyMultiplier = calculateDifficultyMultiplier();
    }
    
    /**
     * Determina a zona atual baseada na posição X do jogador
     */
    private static int determineCurrentZone(int playerX) {
        if (playerX < 1500) return BASIC_MOVEMENT_ZONE;
        if (playerX < 2200) return INTERMEDIATE_ZONE;
        if (playerX < 2900) return DASH_ZONE;
        if (playerX < 3600) return ADVANCED_ZONE;
        if (playerX < 4300) return VERTICAL_ZONE;
        return BOSS_ZONE;
    }
    
    /**
     * Calcula o progresso dentro da zona atual
     */
    private static int calculateZoneProgress(int playerX, int zone) {
        int zoneStartX = getZoneStartX(zone);
        int zoneEndX = getZoneEndX(zone);
        
        if (playerX <= zoneStartX) return 0;
        if (playerX >= zoneEndX) return 100;
        
        return (int)(((double)(playerX - zoneStartX) / (zoneEndX - zoneStartX)) * 100);
    }
    
    /**
     * Calcula o multiplicador de dificuldade baseado no progresso
     */
    private static double calculateDifficultyMultiplier() {
        double baseMultiplier = 1.0 + (currentZone * 0.2);
        double progressMultiplier = 1.0 + (playerProgress * 0.01);
        
        return Math.min(baseMultiplier * progressMultiplier, 3.0); // Máximo 3x
    }
    
    /**
     * Obtém a posição X de início de uma zona
     */
    private static int getZoneStartX(int zone) {
        switch (zone) {
            case BASIC_MOVEMENT_ZONE: return 0;
            case INTERMEDIATE_ZONE: return 1500;
            case DASH_ZONE: return 2200;
            case ADVANCED_ZONE: return 2900;
            case VERTICAL_ZONE: return 3600;
            case BOSS_ZONE: return 4300;
            default: return 0;
        }
    }
    
    /**
     * Obtém a posição X de fim de uma zona
     */
    private static int getZoneEndX(int zone) {
        switch (zone) {
            case BASIC_MOVEMENT_ZONE: return 1500;
            case INTERMEDIATE_ZONE: return 2200;
            case DASH_ZONE: return 2900;
            case ADVANCED_ZONE: return 3600;
            case VERTICAL_ZONE: return 4300;
            case BOSS_ZONE: return 5000;
            default: return 1000;
        }
    }
    
    /**
     * Cria inimigos com dificuldade ajustada para a zona atual
     */
    public static ArrayList<Enemy> createZoneEnemies(int zone) {
        ArrayList<Enemy> enemies = new ArrayList<>();
        
        switch (zone) {
            case BASIC_MOVEMENT_ZONE:
                enemies.addAll(createBasicEnemies());
                break;
            case INTERMEDIATE_ZONE:
                enemies.addAll(createIntermediateEnemies());
                break;
            case DASH_ZONE:
                enemies.addAll(createDashEnemies());
                break;
            case ADVANCED_ZONE:
                enemies.addAll(createAdvancedEnemies());
                break;
            case VERTICAL_ZONE:
                enemies.addAll(createVerticalEnemies());
                break;
            case BOSS_ZONE:
                enemies.addAll(createBossEnemies());
                break;
        }
        
        return enemies;
    }
    
    /**
     * Cria orbs com dificuldade ajustada para a zona atual
     */
    public static ArrayList<EnergyOrb> createZoneOrbs(int zone) {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        
        switch (zone) {
            case BASIC_MOVEMENT_ZONE:
                orbs.addAll(createBasicOrbs());
                break;
            case INTERMEDIATE_ZONE:
                orbs.addAll(createIntermediateOrbs());
                break;
            case DASH_ZONE:
                orbs.addAll(createDashOrbs());
                break;
            case ADVANCED_ZONE:
                orbs.addAll(createAdvancedOrbs());
                break;
            case VERTICAL_ZONE:
                orbs.addAll(createVerticalOrbs());
                break;
            case BOSS_ZONE:
                orbs.addAll(createBossOrbs());
                break;
        }
        
        return orbs;
    }
    
    // Métodos para criar inimigos por zona
    
    private static ArrayList<Enemy> createBasicEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(950, 460, 900, 1000));
        enemies.add(new Enemy(1100, 440, 1050, 1150));
        enemies.add(new Enemy(1250, 420, 1200, 1300));
        return enemies;
    }
    
    private static ArrayList<Enemy> createIntermediateEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(1650, 460, 1600, 1700));
        enemies.add(new Enemy(1800, 440, 1750, 1850));
        enemies.add(new Enemy(1950, 420, 1900, 2000));
        enemies.add(new Enemy(2100, 400, 2050, 2150));
        return enemies;
    }
    
    private static ArrayList<Enemy> createDashEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(2350, 460, 2300, 2400));
        enemies.add(new Enemy(2500, 460, 2450, 2550));
        enemies.add(new Enemy(2650, 460, 2600, 2700));
        enemies.add(new Enemy(2800, 460, 2750, 2850));
        return enemies;
    }
    
    private static ArrayList<Enemy> createAdvancedEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(3050, 440, 3000, 3100));
        enemies.add(new Enemy(3200, 420, 3150, 3250));
        enemies.add(new Enemy(3350, 400, 3300, 3400));
        enemies.add(new Enemy(3500, 380, 3450, 3550));
        return enemies;
    }
    
    private static ArrayList<Enemy> createVerticalEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(3750, 460, 3700, 3800));
        enemies.add(new Enemy(3750, 420, 3700, 3800));
        enemies.add(new Enemy(3750, 380, 3700, 3800));
        enemies.add(new Enemy(3750, 340, 3700, 3800));
        return enemies;
    }
    
    private static ArrayList<Enemy> createBossEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(4500, 460, 4450, 4550));
        enemies.add(new Enemy(4600, 460, 4550, 4650));
        enemies.add(new Enemy(4700, 460, 4650, 4750));
        enemies.add(new Enemy(4800, 460, 4750, 4850));
        return enemies;
    }
    
    // Métodos para criar orbs por zona
    
    private static ArrayList<EnergyOrb> createBasicOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(850, 450, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(1050, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(1250, 410, EnergyOrb.SMALL_ORB));
        orbs.add(new EnergyOrb(1450, 430, EnergyOrb.MEDIUM_ORB));
        return orbs;
    }
    
    private static ArrayList<EnergyOrb> createIntermediateOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(1650, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(1850, 410, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(2050, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(2250, 410, EnergyOrb.LARGE_ORB));
        return orbs;
    }
    
    private static ArrayList<EnergyOrb> createDashOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(2350, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(2550, 430, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(2750, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(2950, 430, EnergyOrb.RARE_ORB));
        return orbs;
    }
    
    private static ArrayList<EnergyOrb> createAdvancedOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(3050, 410, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(3250, 390, EnergyOrb.RARE_ORB));
        orbs.add(new EnergyOrb(3450, 410, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(3650, 390, EnergyOrb.RARE_ORB));
        return orbs;
    }
    
    private static ArrayList<EnergyOrb> createVerticalOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(3750, 430, EnergyOrb.MEDIUM_ORB));
        orbs.add(new EnergyOrb(3750, 390, EnergyOrb.LARGE_ORB));
        orbs.add(new EnergyOrb(3750, 350, EnergyOrb.RARE_ORB));
        orbs.add(new EnergyOrb(3750, 310, EnergyOrb.LEGENDARY_ORB));
        return orbs;
    }
    
    private static ArrayList<EnergyOrb> createBossOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();
        orbs.add(new EnergyOrb(4500, 430, EnergyOrb.RARE_ORB));
        orbs.add(new EnergyOrb(4600, 430, EnergyOrb.LEGENDARY_ORB));
        orbs.add(new EnergyOrb(4700, 430, EnergyOrb.RARE_ORB));
        orbs.add(new EnergyOrb(4800, 430, EnergyOrb.LEGENDARY_ORB));
        return orbs;
    }
    
    // Getters para informações de progresso
    public static int getCurrentZone() {
        return currentZone;
    }
    
    public static int getPlayerProgress() {
        return playerProgress;
    }
    
    public static double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }
    
    /**
     * Reseta o progresso do jogador
     */
    public static void resetProgress() {
        currentZone = 0;
        playerProgress = 0;
        difficultyMultiplier = 1.0;
    }
}
