package org.example.world;

import java.util.*;

import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;

public class InfiniteWorldSystem {
    private final List<Platform> activePlatforms;
    private final List<Enemy> activeEnemies;
    private final List<EnergyOrb> activeEnergyOrbs;

    private final Random random;
    private int lastGeneratedX;
    private final int viewDistance;
    private final int cleanupDistance;
    private final int groundHeight;
    private int difficulty;
    private int sectionLength;

    // Configurações de geração
    private static final int MIN_GAP = 80;
    private static final int MAX_GAP = 250;
    private static final int MIN_PLATFORM_WIDTH = 60;
    private static final int MAX_PLATFORM_WIDTH = 200;
    private static final int SECTION_SIZE = 800;
    private static final int DIFFICULTY_INTERVAL = 2000;
    private static final int MAX_DIFFICULTY = 10;
    private static final int MAX_ENEMIES_PER_SECTION = 4;

    public InfiniteWorldSystem(int groundHeight, int viewDistance) {
        this.activePlatforms = new ArrayList<>();
        this.activeEnemies = new ArrayList<>();
        this.activeEnergyOrbs = new ArrayList<>();
        this.random = new Random();
        this.lastGeneratedX = 0;
        this.viewDistance = viewDistance;
        this.cleanupDistance = viewDistance * 2;
        this.groundHeight = groundHeight;
        this.difficulty = 1;
        this.sectionLength = SECTION_SIZE;

        generateInitialWorld();
    }

    public void update(int playerX) {
        // Gerar novas seções se necessário
        while (lastGeneratedX < playerX + viewDistance) {
            generateNextSection();
        }

        // Limpar objetos muito distantes para otimização
        cleanupDistantObjects(playerX);

        // Atualizar dificuldade
        updateDifficulty(playerX);
    }

    private void generateInitialWorld() {
        try {
            // Usar o WorldBuilder para criar o mundo inicial
            List<Platform> initialPlatforms = WorldBuilder.createPlatforms();
            List<Enemy> initialEnemies = WorldBuilder.createEnemies();
            List<EnergyOrb> initialOrbs = WorldBuilder.createEnergyOrbs();

            activePlatforms.addAll(initialPlatforms);
            activeEnemies.addAll(initialEnemies);
            activeEnergyOrbs.addAll(initialOrbs);

            // Definir onde começar a gerar conteúdo novo (após o conteúdo do WorldBuilder)
            lastGeneratedX = initialPlatforms.stream()
                    .mapToInt(p -> p.getX() + p.getWidth())
                    .max()
                    .orElse(0);
        } catch (Exception e) {
            // Fallback caso WorldBuilder falhe
            System.err.println("Erro ao gerar mundo inicial: " + e.getMessage());
            generateFallbackWorld();
            lastGeneratedX = 1000;
        }
    }

    private void generateFallbackWorld() {
        // Mundo básico de emergência
        activePlatforms.add(new Platform(0, groundHeight, 800, 50, Platform.PlatformType.GROUND));
        activePlatforms.add(new Platform(600, groundHeight - 60, 200, 20, Platform.PlatformType.CLOUD));
        activeEnemies.add(new Enemy(400.0, 100.0, 300.0, 700.0));
        activeEnergyOrbs.add(new EnergyOrb(500, 300, 100));
    }

    private void generateNextSection() {
        int sectionStartX = lastGeneratedX;
        int sectionEndX = lastGeneratedX + sectionLength;

        // Gerar plataformas para esta seção
        generateSectionPlatforms(sectionStartX, sectionEndX);

        // Gerar inimigos para esta seção
        generateSectionEnemies(sectionStartX, sectionEndX);

        // Gerar orbs de energia para esta seção
        generateSectionEnergyOrbs(sectionStartX, sectionEndX);

        lastGeneratedX = sectionEndX;
    }

    private void generateSectionPlatforms(int startX, int endX) {
        // Sempre ter uma plataforma base
        activePlatforms.add(new Platform(startX, groundHeight, sectionLength, 50, Platform.PlatformType.GROUND));

        // Gerar plataformas baseado na dificuldade
        switch (difficulty) {
            case 1:
            case 2:
                generateEasyPlatforms(startX, endX);
                break;
            case 3:
            case 4:
                generateMediumPlatforms(startX, endX);
                break;
            case 5:
            case 6:
                generateHardPlatforms(startX, endX);
                break;
            default:
                generateExtremePlatforms(startX, endX);
                break;
        }
    }

    private void generateEasyPlatforms(int startX, int endX) {
        int numPlatforms = 2 + random.nextInt(3);

        for (int i = 0; i < numPlatforms; i++) {
            int x = startX + 100 + (i * 200) + random.nextInt(100);
            int y = groundHeight - 50 - random.nextInt(100);
            int width = MIN_PLATFORM_WIDTH + random.nextInt(MAX_PLATFORM_WIDTH - MIN_PLATFORM_WIDTH);

            if (x + width < endX) { // Verificar se a plataforma cabe na seção
                activePlatforms.add(new Platform(x, y, width, 20, Platform.PlatformType.CLOUD));
            }
        }
    }

    private void generateMediumPlatforms(int startX, int endX) {
        // Alternar entre diferentes padrões
        int pattern = random.nextInt(4);

        switch (pattern) {
            case 0:
                generateStaircase(startX, endX);
                break;
            case 1:
                generateFloatingPlatforms(startX, endX);
                break;
            case 2:
                generateTowers(startX, endX);
                break;
            case 3:
                generateSimpleMaze(startX, endX);
                break;
        }
    }

    private void generateHardPlatforms(int startX, int endX) {
        int pattern = random.nextInt(3);

        switch (pattern) {
            case 0:
                generatePrecisionJumps(startX, endX);
                break;
            case 1:
                generateVerticalMaze(startX, endX);
                break;
            case 2:
                generateCombinedObstacles(startX, endX);
                break;
        }
    }

    private void generateExtremePlatforms(int startX, int endX) {
        // Combinação de múltiplos padrões difíceis
        int sectionThird = (endX - startX) / 3;
        generatePrecisionJumps(startX, startX + sectionThird);
        generateVerticalMaze(startX + sectionThird, startX + 2 * sectionThird);
        generateFloatingPlatforms(startX + 2 * sectionThird, endX);
    }

    // Implementações dos padrões específicos
    private void generateStaircase(int startX, int endX) {
        boolean goingUp = random.nextBoolean();
        int maxSteps = Math.min(6, (endX - startX) / 80);
        int steps = Math.max(3, maxSteps);
        int stepWidth = Math.min(80, (endX - startX) / steps);
        int stepHeight = 60;

        for (int i = 0; i < steps && startX + i * stepWidth + stepWidth <= endX; i++) {
            int x = startX + i * stepWidth;
            int y = groundHeight - (goingUp ? (i + 1) * stepHeight : (steps - i) * stepHeight);
            y = Math.max(y, 100); // Evitar plataformas muito altas

            activePlatforms.add(new Platform(x, y, stepWidth, 20, Platform.PlatformType.BRICK));
        }
    }

    private void generateFloatingPlatforms(int startX, int endX) {
        int currentX = startX + 100;

        while (currentX + 160 < endX) { // Garantir espaço para plataforma + gap
            int gap = Math.max(MIN_GAP, Math.min(MAX_GAP, 80 + random.nextInt(80)));
            int width = Math.max(MIN_PLATFORM_WIDTH, Math.min(MAX_PLATFORM_WIDTH, 60 + random.nextInt(60)));
            int height = Math.max(150, groundHeight - 80 - random.nextInt(150));

            activePlatforms.add(new Platform(currentX, height, width, 15, Platform.PlatformType.CLOUD));
            currentX += gap + width;
        }
    }

    private void generateTowers(int startX, int endX) {
        int availableWidth = endX - startX - 200; // Margem de segurança
        int maxTowers = Math.max(1, availableWidth / 250);
        int numTowers = 1 + random.nextInt(Math.min(3, maxTowers));
        int towerSpacing = availableWidth / numTowers;

        for (int i = 0; i < numTowers; i++) {
            int towerX = startX + 100 + i * towerSpacing;
            int towerHeight = 150 + random.nextInt(150);

            if (towerX + 100 < endX) {
                // Base da torre
                activePlatforms.add(new Platform(towerX, groundHeight - towerHeight, 60, towerHeight, Platform.PlatformType.PIPE));
                // Plataforma no topo
                activePlatforms.add(new Platform(towerX - 20, groundHeight - towerHeight - 15, 100, 15, Platform.PlatformType.CLOUD));
            }
        }
    }

    private void generateSimpleMaze(int startX, int endX) {
        int levels = 3;
        int levelHeight = 80;

        for (int level = 0; level < levels; level++) {
            int y = Math.max(150, groundHeight - (level + 1) * levelHeight);
            int maxPlatforms = Math.max(2, (endX - startX) / 150);
            int numPlatforms = Math.min(2 + random.nextInt(2), maxPlatforms);

            for (int i = 0; i < numPlatforms; i++) {
                int x = startX + (i * (endX - startX) / numPlatforms) + random.nextInt(50);
                int width = 80 + random.nextInt(40);

                if (x + width < endX) {
                    activePlatforms.add(new Platform(x, y, width, 20, Platform.PlatformType.BRICK));
                }
            }
        }
    }

    private void generatePrecisionJumps(int startX, int endX) {
        int currentX = startX + 100;

        while (currentX + 110 < endX) {
            int gap = 70 + random.nextInt(40);
            int width = 40 + random.nextInt(20);
            int height = Math.max(200, groundHeight - 60 - random.nextInt(120));

            activePlatforms.add(new Platform(currentX, height, width, 15, Platform.PlatformType.CLOUD));
            currentX += gap + width;
        }
    }

    private void generateVerticalMaze(int startX, int endX) {
        int width = endX - startX;
        int levels = 4;

        for (int level = 0; level < levels; level++) {
            int y = Math.max(150, groundHeight - (level + 1) * 80);
            int maxPlatforms = Math.max(2, width / 120);
            int numPlatforms = Math.min(2 + random.nextInt(2), maxPlatforms);

            for (int i = 0; i < numPlatforms; i++) {
                int x = startX + (i * width / numPlatforms) + random.nextInt(50);
                int platWidth = 60 + random.nextInt(40);

                if (x + platWidth < endX) {
                    activePlatforms.add(new Platform(x, y, platWidth, 20, Platform.PlatformType.BRICK));
                }
            }
        }
    }

    private void generateCombinedObstacles(int startX, int endX) {
        int sectionSize = (endX - startX) / 3;

        generateStaircase(startX, startX + sectionSize);
        generateFloatingPlatforms(startX + sectionSize, startX + 2 * sectionSize);
        generateTowers(startX + 2 * sectionSize, endX);
    }

    private void generateSectionEnemies(int startX, int endX) {
        int numEnemies = Math.min(difficulty, MAX_ENEMIES_PER_SECTION);

        for (int i = 0; i < numEnemies; i++) {
            int spacing = (endX - startX - 200) / Math.max(1, numEnemies);
            double x = startX + 100 + (i * spacing) + random.nextInt(Math.max(1, spacing / 2));
            int patrolRange = 50 + difficulty * 10;
            double patrolStart = Math.max(startX + 50, x - patrolRange);
            double patrolEnd = Math.min(endX - 50, x + patrolRange);

            if (patrolStart < patrolEnd && x >= startX && x <= endX) {
                activeEnemies.add(new Enemy(x, 100.0, patrolStart, patrolEnd));
            }
        }
    }

    private void generateSectionEnergyOrbs(int startX, int endX) {
        int numOrbs = 2 + random.nextInt(3);

        for (int i = 0; i < numOrbs; i++) {
            int spacing = (endX - startX - 300) / Math.max(1, numOrbs);
            int x = startX + 150 + (i * spacing) + random.nextInt(Math.max(1, spacing / 2));
            int y = Math.max(150, Math.min(groundHeight - 50, groundHeight - 50 - random.nextInt(200)));
            int baseValue = 50 + (difficulty * 25);
            int value = baseValue + random.nextInt(Math.max(1, baseValue));

            if (x >= startX && x <= endX - 50) {
                activeEnergyOrbs.add(new EnergyOrb(x, y, value));
            }
        }
    }

    private void updateDifficulty(int playerX) {
        int newDifficulty = (playerX / DIFFICULTY_INTERVAL) + 1;
        if (newDifficulty > difficulty) {
            difficulty = Math.min(newDifficulty, MAX_DIFFICULTY);
            sectionLength = SECTION_SIZE + (difficulty * 100);
        }
    }

    private void cleanupDistantObjects(int playerX) {
        int cleanupThreshold = playerX - cleanupDistance;

        // Remover plataformas distantes (exceto ground platforms essenciais)
        activePlatforms.removeIf(platform ->
                platform.getX() + platform.getWidth() < cleanupThreshold &&
                        platform.getPlatformType() != Platform.PlatformType.GROUND
        );

        // Remover inimigos distantes
        activeEnemies.removeIf(enemy -> enemy.getX() < cleanupThreshold);

        // Remover orbs distantes
        activeEnergyOrbs.removeIf(orb -> orb.getX() < cleanupThreshold);
    }

    // Getters públicos
    public List<Platform> getActivePlatforms() {
        return new ArrayList<>(activePlatforms);
    }

    public List<Enemy> getActiveEnemies() {
        return new ArrayList<>(activeEnemies);
    }

    public List<EnergyOrb> getActiveEnergyOrbs() {
        return new ArrayList<>(activeEnergyOrbs);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getLastGeneratedX() {
        return lastGeneratedX;
    }

    public int getSectionLength() {
        return sectionLength;
    }

    // Método para resetar o sistema
    public void reset() {
        activePlatforms.clear();
        activeEnemies.clear();
        activeEnergyOrbs.clear();
        lastGeneratedX = 0;
        difficulty = 1;
        sectionLength = SECTION_SIZE;
        generateInitialWorld();
    }

    // Método para adicionar seção customizada
    public void addCustomSection(int startX, int endX, String sectionType) {
        if (startX >= endX) {
            throw new IllegalArgumentException("startX deve ser menor que endX");
        }

        switch (sectionType.toLowerCase()) {
            case "easy":
                generateEasyPlatforms(startX, endX);
                break;
            case "medium":
                generateMediumPlatforms(startX, endX);
                break;
            case "hard":
                generateHardPlatforms(startX, endX);
                break;
            case "extreme":
                generateExtremePlatforms(startX, endX);
                break;
            default:
                generateMediumPlatforms(startX, endX);
                break;
        }

        generateSectionEnemies(startX, endX);
        generateSectionEnergyOrbs(startX, endX);

        // Atualizar lastGeneratedX se necessário
        if (endX > lastGeneratedX) {
            lastGeneratedX = endX;
        }
    }

    // Método para obter estatísticas do mundo
    public String getWorldStats() {
        return String.format(
                "Mundo Stats - Dificuldade: %d, Último X: %d, Plataformas: %d, Inimigos: %d, Orbs: %d",
                difficulty, lastGeneratedX, activePlatforms.size(), activeEnemies.size(), activeEnergyOrbs.size()
        );
    }
}