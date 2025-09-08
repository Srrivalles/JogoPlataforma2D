package org.example.world;
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

        return platforms;
    }

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
    private static ArrayList<Platform> createEndlessPlatforms() {
        ArrayList<Platform> platforms = new ArrayList<>();

        // Padrão procedural básico para modo infinito
        int currentX = 0;
        int currentY = 400;

        for (int i = 0; i < 50; i++) { // Criar muitas plataformas
            platforms.add(new Platform(currentX, currentY, 100 + (int)(Math.random() * 100), 20, Platform.PlatformType.BRICK));

            // Próxima plataforma
            currentX += 150 + (int)(Math.random() * 200);
            currentY = 200 + (int)(Math.random() * 250);
        }

        return platforms;
    }

    private static ArrayList<Enemy> createEndlessEnemies(float difficulty) {
        ArrayList<Enemy> enemies = new ArrayList<>();

        // Inimigos distribuídos randomicamente
        for (int i = 0; i < 20; i++) {
            int x = 200 + (i * 400) + (int)(Math.random() * 200);
            int y = 200 + (int)(Math.random() * 200);
            int patrolLeft = x - 100;
            int patrolRight = x + 100;

            enemies.add(new Enemy(x, y, patrolLeft, patrolRight));
        }

        return enemies;
    }

    private static ArrayList<EnergyOrb> createEndlessOrbs() {
        ArrayList<EnergyOrb> orbs = new ArrayList<>();

        // Mix aleatório de orbs
        for (int i = 0; i < 30; i++) {
            int x = 100 + (i * 300) + (int)(Math.random() * 100);
            int y = 100 + (int)(Math.random() * 300);

            // Chance de orbs raros aumenta no modo infinito
            int orbType = (int)(Math.random() * 100);
            int energyValue;

            if (orbType < 30) {
                energyValue = EnergyOrb.SMALL_ORB;
            } else if (orbType < 50) {
                energyValue = EnergyOrb.MEDIUM_ORB;
            } else if (orbType < 70) {
                energyValue = EnergyOrb.LARGE_ORB;
            } else if (orbType < 90) {
                energyValue = EnergyOrb.RARE_ORB;
            } else {
                energyValue = EnergyOrb.LEGENDARY_ORB;
            }

            orbs.add(new EnergyOrb(x, y, energyValue));
        }

        return orbs;
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
    }
}