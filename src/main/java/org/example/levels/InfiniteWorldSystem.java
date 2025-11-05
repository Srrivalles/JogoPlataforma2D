package org.example.levels;

import java.util.ArrayList;
import java.util.Random;

/**
 * Sistema de mundo infinito que gera conteúdo dinamicamente
 * baseado na posição do jogador com plataformas acessíveis estilo Mario
 */
public class InfiniteWorldSystem {
    
    private static final int CHUNK_SIZE = 400; // Chunks menores para mais densidade
    // private static final int RENDER_DISTANCE = 5; // Não usado atualmente
    
    // Configurações para geração estilo Mario com muito mais plataformas
    private static final int MAX_JUMP_HEIGHT = 120; // Altura máxima que o player consegue pular
    private static final int MIN_PLATFORM_SPACING = 40; // Espaçamento mínimo muito menor
    private static final int MAX_PLATFORM_SPACING = 90; // Espaçamento máximo menor
    private static final int BASE_GROUND_LEVEL = 500; // Nível base do chão
    
    // Densidade de plataformas - máxima para fase de longa duração infinita
    private static final double PLATFORM_DENSITY = 0.95; // 95% de chance de plataforma por posição
    // private static final int PLATFORMS_PER_CHUNK = 25; // Não usado diretamente
    private static final double FLOATING_DENSITY = 0.8; // 80% de chance para plataformas flutuantes
    private static final double SPECIAL_FORMATION_DENSITY = 0.6; // 60% de chance para formações especiais
    
    // Configurações de spawning
    private static final int ENEMY_BASE_SPACING = 300;
    private static final int ORB_BASE_SPACING = 180;
    
    private Random random;
    private int lastGeneratedChunk = -1;
    private int difficultyLevel = 1;
    private int lastPlatformY = BASE_GROUND_LEVEL; // Tracking da altura da última plataforma
    
    public InfiniteWorldSystem() {
        this.random = new Random();
        this.lastGeneratedChunk = -1; // Inicializar para garantir que gere desde o início
        this.lastPlatformY = BASE_GROUND_LEVEL - 50; // Começar um pouco acima do chão
    }
    
    /**
     * Atualiza o sistema baseado na posição do jogador
     */
    public void update(int playerX) {
        int currentChunk = getChunkFromPosition(playerX);
        
        // Aumentar dificuldade gradualmente mas de forma suave
        difficultyLevel = Math.max(1, (currentChunk / 8) + 1);
        
        // Marcar chunk como processado
        if (currentChunk > lastGeneratedChunk) {
            lastGeneratedChunk = currentChunk;
        }
    }
    
    /**
     * Verifica se deve gerar novo conteúdo baseado na posição do jogador
     */
    public boolean shouldGenerateContent(int playerX) {
        int currentChunk = getChunkFromPosition(playerX);
        // Gerar se ainda não gerou este chunk ou os próximos
        return currentChunk > lastGeneratedChunk;
    }
    
    /**
     * Gera plataformas infinitas estilo Mario com alta densidade
     */
    public ArrayList<PlatformData> generatePlatforms(int startX, int endX) {
        ArrayList<PlatformData> platforms = new ArrayList<>();
        
        // Gerar múltiplas camadas de plataformas para alta densidade
        generateGroundLayer(platforms, startX, endX);
        generateElevatedPlatforms(platforms, startX, endX);
        generateFloatingPlatforms(platforms, startX, endX);
        generateSpecialPlatforms(platforms, startX, endX);
        
        // Atualizar tracking da última plataforma
        if (!platforms.isEmpty()) {
            lastPlatformY = platforms.get(platforms.size() - 1).y;
        }
        
        return platforms;
    }
    
    /**
     * Gera camada de chão base - sempre presente
     */
    private void generateGroundLayer(ArrayList<PlatformData> platforms, int startX, int endX) {
        int currentX = startX;
        
        while (currentX < endX) {
            int width = 100 + random.nextInt(150); // Plataformas de chão maiores
            platforms.add(new PlatformData(currentX, BASE_GROUND_LEVEL, width, 20));
            currentX += width + 20 + random.nextInt(40); // Pequenos gaps
        }
    }
    
    /**
     * Gera plataformas elevadas - camada média
     */
    private void generateElevatedPlatforms(ArrayList<PlatformData> platforms, int startX, int endX) {
        int currentX = startX + 50;
        int baseHeight = BASE_GROUND_LEVEL - 80;
        
        while (currentX < endX) {
            if (random.nextDouble() < PLATFORM_DENSITY) {
                int width = 60 + random.nextInt(120);
                int height = baseHeight + random.nextInt(60) - 30; // Variação na altura
                platforms.add(new PlatformData(currentX, height, width, 20));
            }
            currentX += MIN_PLATFORM_SPACING + random.nextInt(MAX_PLATFORM_SPACING - MIN_PLATFORM_SPACING);
        }
    }
    
    /**
     * Gera plataformas flutuantes - camada alta com densidade máxima
     */
    private void generateFloatingPlatforms(ArrayList<PlatformData> platforms, int startX, int endX) {
        int currentX = startX + 50; // Começar mais cedo
        
        while (currentX < endX) {
            if (random.nextDouble() < FLOATING_DENSITY) { // Usar densidade configurada
                int width = 60 + random.nextInt(120); // Plataformas variadas
                int height = BASE_GROUND_LEVEL - 120 - random.nextInt(150); // Maior variação
                platforms.add(new PlatformData(currentX, height, width, 20));
            }
            currentX += 60 + random.nextInt(80); // Espaçamento menor
        }
    }
    
    /**
     * Gera plataformas especiais - obstáculos e desafios estilo Mario com alta densidade
     */
    private void generateSpecialPlatforms(ArrayList<PlatformData> platforms, int startX, int endX) {
        int currentX = startX + 150; // Começar mais cedo
        
        while (currentX < endX) {
            if (random.nextDouble() < SPECIAL_FORMATION_DENSITY) { // Usar densidade configurada
                int formationType = random.nextInt(6); // 6 tipos diferentes
                
                switch (formationType) {
                    case 0:
                        createStairFormation(platforms, currentX);
                        break;
                    case 1:
                        createBridgeFormation(platforms, currentX);
                        break;
                    case 2:
                        createPyramidFormation(platforms, currentX);
                        break;
                    case 3:
                        createZigZagFormation(platforms, currentX);
                        break;
                    case 4:
                        createFloatingIslandFormation(platforms, currentX);
                        break;
                    case 5:
                        createJumpChallengeFormation(platforms, currentX);
                        break;
                }
            }
            currentX += 180 + random.nextInt(120); // Formações mais frequentes e próximas
        }
    }
    
    /**
     * Cria formação de escada
     */
    private void createStairFormation(ArrayList<PlatformData> platforms, int startX) {
        for (int i = 0; i < 4; i++) {
            int x = startX + (i * 60);
            int y = BASE_GROUND_LEVEL - 60 - (i * 40);
            platforms.add(new PlatformData(x, y, 80, 20));
        }
    }
    
    /**
     * Cria formação de ponte
     */
    private void createBridgeFormation(ArrayList<PlatformData> platforms, int startX) {
        int bridgeY = BASE_GROUND_LEVEL - 100;
        for (int i = 0; i < 3; i++) {
            int x = startX + (i * 70);
            platforms.add(new PlatformData(x, bridgeY, 60, 20));
        }
    }
    
    /**
     * Cria formação de pirâmide - clássico do Mario
     */
    private void createPyramidFormation(ArrayList<PlatformData> platforms, int startX) {
        int baseY = BASE_GROUND_LEVEL - 40;
        
        // Base da pirâmide (3 plataformas)
        for (int i = 0; i < 3; i++) {
            platforms.add(new PlatformData(startX + (i * 60), baseY, 50, 20));
        }
        
        // Meio da pirâmide (2 plataformas)
        for (int i = 0; i < 2; i++) {
            platforms.add(new PlatformData(startX + 30 + (i * 60), baseY - 40, 50, 20));
        }
        
        // Topo da pirâmide (1 plataforma)
        platforms.add(new PlatformData(startX + 60, baseY - 80, 50, 20));
    }
    
    /**
     * Cria formação zigue-zague para desafio
     */
    private void createZigZagFormation(ArrayList<PlatformData> platforms, int startX) {
        int currentX = startX;
        int currentY = BASE_GROUND_LEVEL - 60;
        boolean goingUp = true;
        
        for (int i = 0; i < 5; i++) {
            platforms.add(new PlatformData(currentX, currentY, 80, 20));
            currentX += 90;
            
            if (goingUp) {
                currentY -= 50;
                if (i == 2) goingUp = false; // Muda direção no meio
            } else {
                currentY += 50;
            }
        }
    }
    
    /**
     * Cria ilha flutuante com múltiplas plataformas
     */
    private void createFloatingIslandFormation(ArrayList<PlatformData> platforms, int startX) {
        int islandY = BASE_GROUND_LEVEL - 120;
        
        // Plataforma principal da ilha
        platforms.add(new PlatformData(startX, islandY, 120, 20));
        
        // Plataformas pequenas ao redor
        platforms.add(new PlatformData(startX - 60, islandY + 30, 50, 20));
        platforms.add(new PlatformData(startX + 130, islandY + 30, 50, 20));
        platforms.add(new PlatformData(startX + 60, islandY - 40, 60, 20));
    }
    
    /**
     * Cria desafio de pulo com gaps precisos
     */
    private void createJumpChallengeFormation(ArrayList<PlatformData> platforms, int startX) {
        int currentX = startX;
        int currentY = BASE_GROUND_LEVEL - 80;
        
        // Série de plataformas pequenas com gaps desafiadores
        for (int i = 0; i < 4; i++) {
            int width = 40 + random.nextInt(40); // Plataformas menores
            platforms.add(new PlatformData(currentX, currentY, width, 20));
            currentX += width + 60 + random.nextInt(40); // Gaps variáveis
            currentY += (random.nextInt(40) - 20); // Altura variável
        }
    }
    
    /**
     * Calcula a altura da próxima plataforma de forma inteligente
     */
    private int calculateNextPlatformHeight(int lastY, int currentX) {
        // Criar padrão de ondas suaves
        double waveEffect = Math.sin(currentX * 0.008) * 40;
        
        // Variação aleatória pequena
        int randomVariation = random.nextInt(40) - 20;
        
        // Tendência baseada na dificuldade (mais desafiador = mais variação)
        int difficultyVariation = (int)(random.nextGaussian() * difficultyLevel * 10);
        
        // Calcular nova altura
        int newY = (int)(BASE_GROUND_LEVEL + waveEffect + randomVariation + difficultyVariation);
        
        // Garantir que a diferença não seja muito grande
        int maxDifference = MAX_JUMP_HEIGHT - 30;
        if (newY > lastY + maxDifference) {
            newY = lastY + maxDifference;
        } else if (newY < lastY - maxDifference) {
            newY = lastY - maxDifference;
        }
        
        // Limitar altura absoluta
        newY = Math.max(150, Math.min(650, newY));
        
        return newY;
    }
    
    /**
     * Cria plataformas intermediárias quando necessário
     */
    private void createIntermediatePlatforms(ArrayList<PlatformData> platforms, 
                                           PlatformData lastPlatform, 
                                           int targetX, int targetY) {
        
        int heightDiff = targetY - lastPlatform.y;
        int horizontalDist = targetX - lastPlatform.x;
        
        // Calcular quantos steps são necessários
        int numSteps = Math.abs(heightDiff) / (MAX_JUMP_HEIGHT - 30) + 1;
        
        if (numSteps > 1) {
            for (int i = 1; i < numSteps; i++) {
                int stepX = lastPlatform.x + (horizontalDist * i) / numSteps;
                int stepY = lastPlatform.y + (heightDiff * i) / numSteps;
                
                // Plataformas menores para steps
                int stepWidth = 60 + random.nextInt(40);
                
                platforms.add(new PlatformData(stepX, stepY, stepWidth, 20));
            }
        }
    }
    
    /**
     * Gera inimigos para uma área específica
     */
    public ArrayList<EnemyData> generateEnemies(int startX, int endX) {
        ArrayList<EnemyData> enemies = new ArrayList<>();
        
        // Espaçamento baseado na dificuldade
        int spacing = Math.max(200, ENEMY_BASE_SPACING - (difficultyLevel * 20));
        
        for (int x = startX; x < endX; x += spacing) {
            // Probabilidade de spawn baseada na dificuldade
            float spawnChance = Math.min(0.8f, 0.3f + (difficultyLevel * 0.08f));
            
            if (random.nextFloat() < spawnChance) {
                // Calcular altura baseada no terreno
                int y = calculateTerrainHeight(x) - 50;
                enemies.add(new EnemyData(x, y));
            }
        }
        
        return enemies;
    }
    
    /**
     * Gera orbs de energia para uma área específica
     */
    public ArrayList<OrbData> generateEnergyOrbs(int startX, int endX) {
        ArrayList<OrbData> orbs = new ArrayList<>();
        
        // Espaçamento baseado na dificuldade (menos orbs = mais difícil)
        int spacing = ORB_BASE_SPACING + (difficultyLevel * 20);
        
        for (int x = startX; x < endX; x += spacing) {
            // Probabilidade de spawn (diminui com dificuldade)
            float spawnChance = Math.max(0.5f, 0.9f - (difficultyLevel * 0.05f));
            
            if (random.nextFloat() < spawnChance) {
                // Posicionar orbs no ar, mas alcançáveis
                int terrainHeight = calculateTerrainHeight(x);
                int orbY = terrainHeight - 60 - random.nextInt(80);
                
                // Energia baseada na dificuldade
                int energy = 20 + (difficultyLevel * 3) + random.nextInt(15);
                
                orbs.add(new OrbData(x, orbY, energy));
            }
        }
        
        return orbs;
    }
    
    /**
     * Calcula a altura estimada do terreno em uma posição X
     */
    private int calculateTerrainHeight(int x) {
        double waveEffect = Math.sin(x * 0.008) * 40;
        return (int)(BASE_GROUND_LEVEL + waveEffect);
    }
    
    /**
     * Calcula o chunk baseado na posição X
     */
    private int getChunkFromPosition(int x) {
        return x / CHUNK_SIZE;
    }
    
    /**
     * Obtém o nível de dificuldade atual
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }
    
    /**
     * Obtém o multiplicador de dificuldade
     */
    public float getDifficultyMultiplier() {
        return 1.0f + (difficultyLevel * 0.15f);
    }
    
    /**
     * Obtém a posição inicial do jogador
     */
    public int getPlayerStartX() {
        return 100;
    }
    
    /**
     * Obtém a posição inicial do jogador
     */
    public int getPlayerStartY() {
        return BASE_GROUND_LEVEL - 100; // Um pouco acima do chão
    }
    
    /**
     * Reset do sistema para novo jogo
     */
    public void reset() {
        lastGeneratedChunk = -1;
        difficultyLevel = 1;
        lastPlatformY = BASE_GROUND_LEVEL - 50;
        random = new Random();
    }
    
    /**
     * Define seed para geração determinística (útil para testes)
     */
    public void setSeed(long seed) {
        random = new Random(seed);
    }
    
    // Classes de dados para os elementos do jogo
    public static class PlatformData {
        public int x, y, width, height;
        
        public PlatformData(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public String toString() {
            return String.format("Platform[x=%d, y=%d, w=%d, h=%d]", x, y, width, height);
        }
    }
    
    public static class EnemyData {
        public int x, y;
        
        public EnemyData(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return String.format("Enemy[x=%d, y=%d]", x, y);
        }
    }
    
    public static class OrbData {
        public int x, y, energy;
        
        public OrbData(int x, int y, int energy) {
            this.x = x;
            this.y = y;
            this.energy = energy;
        }
        
        @Override
        public String toString() {
            return String.format("Orb[x=%d, y=%d, energy=%d]", x, y, energy);
        }
    }
}