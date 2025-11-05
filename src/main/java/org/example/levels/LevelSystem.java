package org.example.levels;

import org.example.objects.Player;

public class LevelSystem {
    private int currentLevel = 1;
    private int maxLevel = 3; // Total de fases
    private boolean levelCompleted = false;
    private boolean showLevelTransition = false;
    private int transitionTimer = 0;
    private final int TRANSITION_DURATION = 180; // 3 segundos a 60fps

    // Dados da fase atual
    private LevelData currentLevelData;

    // Zona de final da fase
    private LevelGoal levelGoal;

    public LevelSystem() {
        loadLevel(currentLevel);
    }

    public void loadLevel(int levelNumber) {
        this.currentLevel = levelNumber;
        this.levelCompleted = false;
        this.showLevelTransition = false;
        this.transitionTimer = 0;

        // Criar dados específicos da fase
        switch (levelNumber) {
            case 1:
                currentLevelData = createLevel1();
                break;
            case 2:
                currentLevelData = createLevel2();
                break;
            case 3:
                currentLevelData = createLevel3();
                break;
            default:
                // Se passou de todas as fases, criar fase final/infinita
                currentLevelData = createEndlessLevel();
                break;
        }

        // Criar zona de objetivo
        levelGoal = new LevelGoal(
                currentLevelData.goalX,
                currentLevelData.goalY,
                levelNumber
        );

        System.out.println("=== FASE " + levelNumber + " CARREGADA ===");
        System.out.println("Tema: " + currentLevelData.themeName);
        System.out.println("Objetivo em: [" + currentLevelData.goalX + ", " + currentLevelData.goalY + "]");
    }

    private LevelData createLevel1() {
        LevelData level = new LevelData();
        level.levelNumber = 1;
        level.themeName = "Cyber City - Tutorial";
        level.backgroundColor = new java.awt.Color(20, 30, 60);
        level.goalX = 1800; // Final da fase 1
        level.goalY = 200;
        level.playerStartX = 100;
        level.playerStartY = 100;
        level.difficultyMultiplier = 1.0f;
        level.timeLimit = 120; // 2 minutos
        return level;
    }

    private LevelData createLevel2() {
        LevelData level = new LevelData();
        level.levelNumber = 2;
        level.themeName = "Data Stream - Intermediate";
        level.backgroundColor = new java.awt.Color(60, 20, 30);
        level.goalX = 2500; // Fase mais longa
        level.goalY = 150;
        level.playerStartX = 100;
        level.playerStartY = 100;
        level.difficultyMultiplier = 1.5f;
        level.timeLimit = 150; // 2.5 minutos
        return level;
    }

    private LevelData createLevel3() {
        LevelData level = new LevelData();
        level.levelNumber = 3;
        level.themeName = "Neural Network - Advanced";
        level.backgroundColor = new java.awt.Color(30, 60, 20);
        level.goalX = 3000;
        level.goalY = 100;
        level.playerStartX = 100;
        level.playerStartY = 100;
        level.difficultyMultiplier = 2.0f;
        level.timeLimit = 180; // 3 minutos
        return level;
    }

    private LevelData createEndlessLevel() {
        LevelData level = new LevelData();
        level.levelNumber = 999;
        level.themeName = "Purple Infinity - Cyber Nexus";
        level.backgroundColor = new java.awt.Color(25, 0, 51); // Roxo muito escuro futurista
        level.goalX = 9999; // Sem fim real
        level.goalY = 200;
        level.playerStartX = 100;
        level.playerStartY = 100;
        level.difficultyMultiplier = 3.0f;
        level.timeLimit = -1; // Sem limite de tempo
        return level;
    }

    public void update(Player player) {
        if (levelCompleted) {
            updateTransition();
            return;
        }

        // Verificar se player chegou no objetivo
        if (levelGoal != null && levelGoal.checkPlayerReached(player)) {
            triggerLevelComplete();
        }

        // Atualizar objetivo
        if (levelGoal != null) {
            levelGoal.update();
        }
    }

    private void triggerLevelComplete() {
        levelCompleted = true;
        showLevelTransition = true;
        transitionTimer = TRANSITION_DURATION;

        System.out.println("=== FASE " + currentLevel + " COMPLETA! ===");
    }

    private void updateTransition() {
        if (transitionTimer > 0) {
            transitionTimer--;
        }
    }

    public boolean shouldAdvanceToNextLevel() {
        return levelCompleted && transitionTimer <= 0;
    }

    public void advanceToNextLevel() {
        if (currentLevel < maxLevel) {
            loadLevel(currentLevel + 1);
        } else {
            // Todas as fases completas - modo infinito
            loadLevel(999);
        }
    }

    public void resetCurrentLevel() {
        loadLevel(currentLevel);
    }

    // Getters
    public int getCurrentLevel() { return currentLevel; }
    public boolean isLevelCompleted() { return levelCompleted; }
    public boolean isShowingTransition() { return showLevelTransition; }
    public int getTransitionTimer() { return transitionTimer; }
    public LevelData getCurrentLevelData() { return currentLevelData; }
    public LevelGoal getLevelGoal() { return levelGoal; }
    public boolean isLastLevel() { return currentLevel >= maxLevel; }
    public boolean isEndlessMode() { return currentLevel >= 999; }

    // Classes internas para dados da fase
    public static class LevelData {
        public int levelNumber;
        public String themeName;
        public java.awt.Color backgroundColor;
        public int goalX, goalY;
        public int playerStartX, playerStartY;
        public float difficultyMultiplier;
        public int timeLimit; // -1 = sem limite
    }
}