package org.example.fhysics;

import org.example.ui.GameConfig;
import org.example.ui.MenuSystem;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
<<<<<<< HEAD
import org.example.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreSystem {

    // === Variáveis principais ===
=======

public class ScoreSystem {
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    private int currentScore = 0;
    private int energyOrbsCollected = 0;
    private int enemiesDefeated = 0;
    private int scoreMultiplier = 1;
<<<<<<< HEAD
    private int highScore = 0;

    // === Sistema de combo ===
    private int comboCounter = 0;
    private long lastActionTime = 0;
    private static final long COMBO_TIMEOUT_MS = 3000; // 3 segundos
    private static final int MAX_COMBO = 50;

    // === Sistema de multiplicadores ===
    private static final int MAX_SCORE_MULTIPLIER = 10;
    private double temporaryMultiplier = 1.0;
    private long multiplierEndTime = 0;

    // === Sistema de conquistas e estatísticas ===
    private int perfectLandings = 0;
    private int airTimeFrames = 0;
    private int consecutiveCollects = 0;
    private double maxHeightReached = 0;
    private double totalDistanceTraveled = 0;
    private int totalJumps = 0;
    private int doubleKills = 0;
    private int tripleKills = 0;

    // === Sistema de bônus temporal ===
    private List<TimedBonus> activeBonuses = new ArrayList<>();

    // === Sistema de ranking de performance ===
    private String currentRank = "D";
    private double performanceScore = 0;

    /**
     * Adiciona pontos base ao score
     */
    public void addScore(int points) {
        int finalPoints = (int)(points * getEffectiveMultiplier());
        this.currentScore += finalPoints;
        updatePerformanceScore(points);
    }

    public void addPoints(int points) {
        addScore(points);
    }

    /**
     * Sistema avançado de coleta de orbs com bônus complexos
     */
    public void collectOrb(EnergyOrb orb) {
        energyOrbsCollected++;
        consecutiveCollects++;
        updateCombo();

        int orbScore = calculateAdvancedOrbScore(orb);

        // Bônus por coletas consecutivas
        if (consecutiveCollects >= 5) {
            int streakBonus = consecutiveCollects * 50;
            orbScore += streakBonus;
        }

        currentScore += orbScore;
        updatePerformanceScore(orbScore);
        showScoreEffect(orbScore, orb.x, orb.y);

        // Verificar milestone de orbs
        checkOrbMilestone();
    }

    /**
     * Sistema avançado de derrota de inimigos com bônus de estilo
     */
    public void defeatEnemy(Enemy enemy, Player player, boolean wasStomp) {
        enemiesDefeated++;
        updateCombo();

        int baseScore = GameConfig.ENEMY_DEFEAT_POINTS;

        // Bônus por altura do stomp
        double stompHeight = wasStomp ? Math.abs(player.velocityY) : 0;
        int heightBonus = (int)(stompHeight * 10);

        // Bônus por velocidade do player
        double playerSpeed = Math.sqrt(player.velocityX * player.velocityX + player.velocityY * player.velocityY);
        int speedBonus = (int)(playerSpeed * 5);

        // Bônus por combo
        int comboBonus = comboCounter * 25;

        int totalScore = (baseScore + heightBonus + speedBonus + comboBonus) * scoreMultiplier;
        currentScore += totalScore;

        // Incrementar multiplicador
        scoreMultiplier = Math.min(MAX_SCORE_MULTIPLIER, scoreMultiplier + 1);

        updatePerformanceScore(totalScore);
        showScoreEffect(totalScore, enemy.x, enemy.y);

    }

    /**
     * Cálculo avançado de pontuação de orbs
     */
    private int calculateAdvancedOrbScore(EnergyOrb orb) {
        int baseScore = GameConfig.ORB_POINTS;

        // Bônus por distância percorrida
        int distanceBonus = (int)(orb.x / 150);

        // Bônus por altura (orbs mais altos = mais difíceis)
        int heightBonus = Math.max(0, (600 - (int)orb.y) / 40);

        // Bônus por combo ativo
        int comboBonus = comboCounter * 5;

        // Bônus por coleta no ar
        int airBonus = airTimeFrames > 30 ? 50 : 0;

        int totalScore = baseScore + distanceBonus + heightBonus + comboBonus + airBonus;

        // Aplicar limites
        totalScore = Math.max(25, Math.min(800, totalScore));

        return (int)(totalScore * getEffectiveMultiplier());
    }

    /**
     * Sistema de combo com timeout
     */
    private void updateCombo() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastActionTime < COMBO_TIMEOUT_MS) {
            comboCounter = Math.min(MAX_COMBO, comboCounter + 1);

            // Bônus de combo milestones
            if (comboCounter == 10) {
                addScore(500);
            } else if (comboCounter == 25) {
                addScore(1500);
                activateTemporaryMultiplier(2.0, 5000);
            } else if (comboCounter == 50) {
                addScore(5000);
                activateTemporaryMultiplier(3.0, 10000);
            }
        } else {
            comboCounter = 0;
            consecutiveCollects = 0;
        }

        lastActionTime = currentTime;
    }

    /**
     * Sistema de multiplicador temporário (power-ups)
     */
    public void activateTemporaryMultiplier(double multiplier, long durationMs) {
        this.temporaryMultiplier = Math.max(this.temporaryMultiplier, multiplier);
        this.multiplierEndTime = System.currentTimeMillis() + durationMs;
    }

    /**
     * Retorna o multiplicador efetivo total
     */
    public double getEffectiveMultiplier() {
        double effective = scoreMultiplier;

        // Adicionar multiplicador temporário se ativo
        if (System.currentTimeMillis() < multiplierEndTime) {
            effective *= temporaryMultiplier;
        } else {
            temporaryMultiplier = 1.0;
        }

        // Adicionar bônus de combo
        if (comboCounter >= 10) {
            effective *= (1.0 + (comboCounter / 100.0));
        }

        return effective;
    }

    /**
     * Sistema de bônus especiais
     */
    public void awardPerfectLanding() {
        perfectLandings++;
        int bonus = 200;
        addScore(bonus);

        if (perfectLandings % 5 == 0) {
            activateTemporaryMultiplier(1.5, 3000);
        }
    }

    public void awardAirTimeBonus(int frames) {
        airTimeFrames += frames;

        if (frames > 120) { // 2 segundos no ar
            int bonus = frames * 2;
            addScore(bonus);
        }
    }

    public void awardDoubleKill() {
        doubleKills++;
        addScore(1000);
    }

    public void awardTripleKill() {
        tripleKills++;
        addScore(3000);
        activateTemporaryMultiplier(2.5, 5000);
    }

    /**
     * Sistema de estatísticas avançadas
     */
    public void trackJump() {
        totalJumps++;
    }

    public void trackHeight(double height) {
        if (height > maxHeightReached) {
            maxHeightReached = height;

            // Bônus por novas alturas máximas
            if (height > 1000 && height % 500 < 10) {
                int heightBonus = (int)(height / 2);
                addScore(heightBonus);
            }
        }
    }

    public void trackDistance(double distance) {
        totalDistanceTraveled += Math.abs(distance);

        // Milestone a cada 10000 unidades
        if (totalDistanceTraveled > 0 && (int)totalDistanceTraveled % 10000 < 10) {
            addScore(500);
        }
    }

    /**
     * Sistema de ranking de performance
     */
    private void updatePerformanceScore(int pointsEarned) {
        performanceScore += pointsEarned;

        // Penalidades por morte/dano reduzem performance
        // Calcular ranking baseado em múltiplos fatores
        double rankScore = performanceScore / Math.max(1, totalJumps);
        rankScore += (comboCounter * 10);
        rankScore += (perfectLandings * 50);

        if (rankScore < 100) currentRank = "D";
        else if (rankScore < 300) currentRank = "C";
        else if (rankScore < 600) currentRank = "B";
        else if (rankScore < 1000) currentRank = "A";
        else if (rankScore < 2000) currentRank = "S";
        else currentRank = "SS";
    }

    /**
     * Verificar milestones de orbs coletados
     */
    private void checkOrbMilestone() {
        if (energyOrbsCollected == 10) {
            addScore(500);
        } else if (energyOrbsCollected == 25) {
            addScore(1500);
            activateTemporaryMultiplier(1.5, 5000);
        } else if (energyOrbsCollected == 50) {
            addScore(5000);
            activateTemporaryMultiplier(2.0, 10000);
        }
    }

    /**
     * Penalidade por dano/morte
     */
    public void applyDamagePenalty() {
        // Perder metade do multiplicador
        scoreMultiplier = Math.max(1, scoreMultiplier / 2);

        // Resetar combo
        comboCounter = 0;
        consecutiveCollects = 0;

        // Resetar multiplicador temporário
        temporaryMultiplier = 1.0;
        multiplierEndTime = 0;

    }

    public void showScoreEffect(int score, double x, double y) {
        String effect = "+" + score;

        if (comboCounter > 5) {
            effect += " (x" + comboCounter + " COMBO)";
        }

        if (getEffectiveMultiplier() > 2.0) {
            effect += " [MULT x" + String.format("%.1f", getEffectiveMultiplier()) + "]";
        }

    }

    /**
     * Reset de score (novo jogo)
     */
    public void resetScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
            System.out.println("🏆 NOVO HIGH SCORE! " + highScore);
        }

=======

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
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        currentScore = 0;
        energyOrbsCollected = 0;
        enemiesDefeated = 0;
        scoreMultiplier = 1;
<<<<<<< HEAD
        comboCounter = 0;
        consecutiveCollects = 0;
        perfectLandings = 0;
        airTimeFrames = 0;
        maxHeightReached = 0;
        totalDistanceTraveled = 0;
        totalJumps = 0;
        doubleKills = 0;
        tripleKills = 0;
        temporaryMultiplier = 1.0;
        multiplierEndTime = 0;
        activeBonuses.clear();
        performanceScore = 0;
        currentRank = "D";
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    }

    public void resetMultiplier() {
        scoreMultiplier = 1;
    }

<<<<<<< HEAD
    // === Getters ===
    public int getCurrentScore() { return currentScore; }
    public int getHighScore() { return highScore; }
    public int getEnergyOrbsCollected() { return energyOrbsCollected; }
    public int getEnemiesDefeated() { return enemiesDefeated; }
    public int getScoreMultiplier() { return scoreMultiplier; }
    public int getComboCounter() { return comboCounter; }
    public double getEffectiveMultiplierValue() { return getEffectiveMultiplier(); }
    public String getCurrentRank() { return currentRank; }
    public int getPerfectLandings() { return perfectLandings; }
    public int getTotalJumps() { return totalJumps; }
    public double getMaxHeightReached() { return maxHeightReached; }
    public double getTotalDistanceTraveled() { return totalDistanceTraveled; }
    public int getDoubleKills() { return doubleKills; }
    public int getTripleKills() { return tripleKills; }
    public boolean hasActiveMultiplier() {
        return System.currentTimeMillis() < multiplierEndTime;
    }
    public long getRemainingMultiplierTime() {
        return Math.max(0, multiplierEndTime - System.currentTimeMillis());
    }

    // === Métodos de atualização do UI ===
=======
    // Getters
    public int getCurrentScore() { return currentScore; }
    public int getEnergyOrbsCollected() { return energyOrbsCollected; }
    public int getEnemiesDefeated() { return enemiesDefeated; }
    public int getScoreMultiplier() { return scoreMultiplier; }

    // Método para atualizar o menu system
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
    public void updateMenuSystem(MenuSystem menuSystem) {
        if (menuSystem != null) {
            try {
                menuSystem.updateScore(currentScore);
                menuSystem.updateStats(energyOrbsCollected, enemiesDefeated);
<<<<<<< HEAD

                // Tentar atualizar informações adicionais se disponível
                if (hasMethod(menuSystem, "updateCombo")) {
                    menuSystem.getClass()
                            .getMethod("updateCombo", int.class)
                            .invoke(menuSystem, comboCounter);
                }

                if (hasMethod(menuSystem, "updateMultiplier")) {
                    menuSystem.getClass()
                            .getMethod("updateMultiplier", double.class)
                            .invoke(menuSystem, getEffectiveMultiplier());
                }

                if (hasMethod(menuSystem, "updateRank")) {
                    menuSystem.getClass()
                            .getMethod("updateRank", String.class)
                            .invoke(menuSystem, currentRank);
                }
            } catch (Exception e) {
                // Falha silenciosa para compatibilidade
=======
            } catch (Exception e) {
                System.out.println("Aviso: MenuSystem não possui métodos esperados");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            }
        }
    }

    public void triggerGameOver(MenuSystem menuSystem) {
<<<<<<< HEAD
        System.out.println("════════════════════════════════════════");
        System.out.println("           GAME OVER");
        System.out.println("════════════════════════════════════════");
        System.out.println("Score Final: " + currentScore);
        System.out.println("High Score: " + highScore);
        System.out.println("Ranking: " + currentRank);
        System.out.println("────────────────────────────────────────");
        System.out.println("Estatísticas:");
        System.out.println("  • Orbs Coletados: " + energyOrbsCollected);
        System.out.println("  • Inimigos Derrotados: " + enemiesDefeated);
        System.out.println("  • Combo Máximo: " + comboCounter);
        System.out.println("  • Perfect Landings: " + perfectLandings);
        System.out.println("  • Altura Máxima: " + (int)maxHeightReached);
        System.out.println("  • Distância Total: " + (int)totalDistanceTraveled);
        System.out.println("  • Double Kills: " + doubleKills);
        System.out.println("  • Triple Kills: " + tripleKills);
        System.out.println("════════════════════════════════════════");
=======
        System.out.println("Game Over! Score final: " + currentScore);
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176

        if (menuSystem != null) {
            try {
                menuSystem.triggerGameOver(currentScore, energyOrbsCollected, enemiesDefeated);
            } catch (Exception e) {
<<<<<<< HEAD
                System.err.println("Erro ao chamar triggerGameOver no MenuSystem");
=======
                System.out.println("Erro ao chamar triggerGameOver no MenuSystem");
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
            }
        }

        resetMultiplier();
    }
<<<<<<< HEAD

    // === Métodos auxiliares ===
    private boolean hasMethod(Object obj, String methodName) {
        try {
            for (java.lang.reflect.Method method : obj.getClass().getMethods()) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignorar
        }
        return false;
    }

    /**
     * Gera relatório de performance detalhado
     */
    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n═══════════════════════════════════════\n");
        report.append("      RELATÓRIO DE PERFORMANCE\n");
        report.append("═══════════════════════════════════════\n\n");

        // Scoring
        report.append("📊 PONTUAÇÃO:\n");
        report.append(String.format("  Score: %,d\n", currentScore));
        report.append(String.format("  High Score: %,d\n", highScore));
        report.append(String.format("  Ranking: %s\n\n", currentRank));

        // Combate
        report.append("⚔️ COMBATE:\n");
        report.append(String.format("  Inimigos Derrotados: %d\n", enemiesDefeated));
        report.append(String.format("  Double Kills: %d\n", doubleKills));
        report.append(String.format("  Triple Kills: %d\n", tripleKills));
        report.append(String.format("  Combo Máximo: x%d\n\n", comboCounter));

        // Coleta
        report.append("💎 COLETA:\n");
        report.append(String.format("  Orbs Coletados: %d\n", energyOrbsCollected));
        report.append(String.format("  Streak Máximo: %d\n\n", consecutiveCollects));

        // Habilidade
        report.append("🎯 HABILIDADE:\n");
        report.append(String.format("  Perfect Landings: %d\n", perfectLandings));
        report.append(String.format("  Saltos Totais: %d\n", totalJumps));
        report.append(String.format("  Altura Máxima: %.0f\n\n", maxHeightReached));

        // Exploração
        report.append("🗺️ EXPLORAÇÃO:\n");
        report.append(String.format("  Distância Percorrida: %.0f\n", totalDistanceTraveled));

        // Eficiência
        double efficiency = totalJumps > 0 ? (double)currentScore / totalJumps : 0;
        report.append(String.format("\n📈 Eficiência: %.1f pontos/salto\n", efficiency));

        report.append("═══════════════════════════════════════\n");

        return report.toString();
    }

    /**
     * Calcula grade final baseado em múltiplos fatores
     */
    public String calculateFinalGrade() {
        int gradePoints = 0;

        // Pontuação (40%)
        if (currentScore >= 50000) gradePoints += 40;
        else if (currentScore >= 30000) gradePoints += 30;
        else if (currentScore >= 15000) gradePoints += 20;
        else if (currentScore >= 5000) gradePoints += 10;

        // Combate (30%)
        if (enemiesDefeated >= 50) gradePoints += 30;
        else if (enemiesDefeated >= 30) gradePoints += 20;
        else if (enemiesDefeated >= 15) gradePoints += 10;

        // Coleta (20%)
        if (energyOrbsCollected >= 50) gradePoints += 20;
        else if (energyOrbsCollected >= 30) gradePoints += 15;
        else if (energyOrbsCollected >= 15) gradePoints += 10;

        // Habilidade (10%)
        if (perfectLandings >= 20) gradePoints += 10;
        else if (perfectLandings >= 10) gradePoints += 5;

        // Determinar grade
        if (gradePoints >= 90) return "SS";
        else if (gradePoints >= 80) return "S";
        else if (gradePoints >= 70) return "A";
        else if (gradePoints >= 60) return "B";
        else if (gradePoints >= 50) return "C";
        else if (gradePoints >= 40) return "D";
        else return "F";
    }

    /**
     * Sistema de conquistas
     */
    public List<String> getUnlockedAchievements() {
        List<String> achievements = new ArrayList<>();

        // Conquistas de pontuação
        if (currentScore >= 10000) achievements.add("🏆 Score Master - 10,000 pontos");
        if (currentScore >= 50000) achievements.add("💎 Score Legend - 50,000 pontos");
        if (currentScore >= 100000) achievements.add("⭐ Score God - 100,000 pontos");

        // Conquistas de combo
        if (comboCounter >= 10) achievements.add("🔥 Combo Starter - Combo x10");
        if (comboCounter >= 25) achievements.add("💥 Combo Master - Combo x25");
        if (comboCounter >= 50) achievements.add("⚡ Combo Legend - Combo x50");

        // Conquistas de combate
        if (enemiesDefeated >= 25) achievements.add("⚔️ Warrior - 25 inimigos derrotados");
        if (enemiesDefeated >= 50) achievements.add("🗡️ Slayer - 50 inimigos derrotados");
        if (doubleKills >= 5) achievements.add("💀 Double Trouble - 5 Double Kills");
        if (tripleKills >= 1) achievements.add("☠️ Triple Threat - Triple Kill");

        // Conquistas de coleta
        if (energyOrbsCollected >= 25) achievements.add("💎 Collector - 25 orbs");
        if (energyOrbsCollected >= 50) achievements.add("🌟 Hoarder - 50 orbs");

        // Conquistas de habilidade
        if (perfectLandings >= 10) achievements.add("✨ Graceful - 10 Perfect Landings");
        if (perfectLandings >= 25) achievements.add("🎯 Precise - 25 Perfect Landings");

        // Conquistas de exploração
        if (maxHeightReached >= 2000) achievements.add("🏔️ Sky Walker - Altura 2000");
        if (totalDistanceTraveled >= 50000) achievements.add("🏃 Marathon Runner - 50k distância");

        return achievements;
    }

    /**
     * Classe interna para bônus temporários
     */
    private static class TimedBonus {
        String name;
        double multiplier;
        long endTime;

        TimedBonus(String name, double multiplier, long duration) {
            this.name = name;
            this.multiplier = multiplier;
            this.endTime = System.currentTimeMillis() + duration;
        }

        boolean isActive() {
            return System.currentTimeMillis() < endTime;
        }
    }
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
}