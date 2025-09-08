package org.example.fhysics;

import org.example.ui.GameConfig;
import org.example.ui.MenuSystem;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;

public class ScoreSystem {
    private int currentScore = 0;
    private int energyOrbsCollected = 0;
    private int enemiesDefeated = 0;
    private int scoreMultiplier = 1;

    // Constante para o multiplicador máximo (você pode mover para GameConfig se preferir)
    private static final int MAX_SCORE_MULTIPLIER = 5;

    // ⭐ MÉTODOS ADICIONADOS PARA COMPATIBILIDADE
    public void addScore(int points) {
        this.currentScore += points;
        System.out.println("Pontos adicionados: " + points + " | Total: " + this.currentScore);
    }

    public void addPoints(int points) {
        addScore(points); // Alias para compatibilidade
    }

    public void collectOrb(EnergyOrb orb) {
        energyOrbsCollected++;
        int orbScore = calculateOrbScore(orb);
        currentScore += orbScore;
        showScoreEffect(orbScore, orb.x, orb.y);
    }

    public void defeatEnemy(Enemy enemy) {
        enemiesDefeated++;
        int enemyScore = GameConfig.ENEMY_DEFEAT_POINTS * scoreMultiplier;
        currentScore += enemyScore;
        scoreMultiplier = Math.min  (MAX_SCORE_MULTIPLIER, scoreMultiplier + 1);
        showScoreEffect(enemyScore, enemy.x, enemy.y);
    }

    private int calculateOrbScore(EnergyOrb orb) {
        int baseScore = GameConfig.ORB_POINTS;

        // Bonus por distância (quanto mais longe do início, mais pontos)
        int distanceBonus = orb.x / 200;

        // Bonus por altura (orbs mais altos são mais difíceis de pegar)
        int heightBonus = Math.max(0, (500 - orb.y) / 50);

        int totalScore = baseScore + distanceBonus + heightBonus;

        // Limitar entre 25 e 500 pontos base
        totalScore = Math.max(25, Math.min(500, totalScore));

        return totalScore * scoreMultiplier;
    }

    public void showScoreEffect(int score, double x, double y) {
        System.out.println("+" + score + " pontos em [" + x + ", " + y + "]!");
    }

    public void resetScore() {
        currentScore = 0;
        energyOrbsCollected = 0;
        enemiesDefeated = 0;
        scoreMultiplier = 1;
    }

    public void resetMultiplier() {
        scoreMultiplier = 1;
    }

    // Getters
    public int getCurrentScore() { return currentScore; }
    public int getEnergyOrbsCollected() { return energyOrbsCollected; }
    public int getEnemiesDefeated() { return enemiesDefeated; }
    public int getScoreMultiplier() { return scoreMultiplier; }

    // Método para atualizar o menu system
    public void updateMenuSystem(MenuSystem menuSystem) {
        if (menuSystem != null) {
            try {
                menuSystem.updateScore(currentScore);
                menuSystem.updateStats(energyOrbsCollected, enemiesDefeated);
            } catch (Exception e) {
                System.out.println("Aviso: MenuSystem não possui métodos esperados");
            }
        }
    }

    public void triggerGameOver(MenuSystem menuSystem) {
        System.out.println("Game Over! Score final: " + currentScore);

        if (menuSystem != null) {
            try {
                menuSystem.triggerGameOver(currentScore, energyOrbsCollected, enemiesDefeated);
            } catch (Exception e) {
                System.out.println("Erro ao chamar triggerGameOver no MenuSystem");
            }
        }

        resetMultiplier();
    }
}