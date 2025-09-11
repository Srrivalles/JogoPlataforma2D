package org.example.world;

import java.util.*;

import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.audio.AudioManager;

/**
 * Sistema de Mundo baseado em salas interconectadas
 * Inspirado em Celeste (telas de desafio) e Hollow Knight (exploração)
 */
public class InfiniteWorldSystem {
    // === SISTEMA DE SALAS INTERCONECTADAS ===
    private final Map<String, GameRoom> worldMap = new HashMap<>();
    private final List<Platform> activePlatforms = new ArrayList<>();
    private final List<Enemy> activeEnemies = new ArrayList<>();
    private final List<EnergyOrb> activeEnergyOrbs = new ArrayList<>();
    
    private String currentRoomId;
    private GameRoom currentRoom;
    private final Random random = new Random();
    
    // === CONFIGURAÇÕES DO MUNDO ===
    private static final int ROOM_WIDTH = 1200;  // Largura padrão de uma sala
    private static final int ROOM_HEIGHT = 800;  // Altura padrão de uma sala
    private static final int GROUND_HEIGHT = 450;
    
    // Sistema de música/batida
    private long lastBeatTime = 0;
    private int beatInterval = 500; // 120 BPM aproximadamente
    private boolean onBeat = false;
    
    // === TIPOS DE SALA ===
    public enum RoomType {
        PRECISION_CHALLENGE,  // Desafios de precisão (estilo Celeste)
        VERTICAL_SHAFT,       // Poços verticais (estilo Hollow Knight)
        BRANCHING_PATHS,      // Caminhos que se dividem
        BOSS_CHAMBER,         // Salas de chefe
        SECRET_AREA,          // Áreas secretas
        REST_AREA,            // Áreas de descanso
        TRANSITION_HUB        // Salas de conexão
    }
    
    // === DIREÇÕES DE CONEXÃO ===
    public enum Direction {
        NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST
    }

    /**
     * Construtor do sistema de mundo baseado em salas
     */
    public InfiniteWorldSystem(int groundHeight, int viewDistance) {
        // Começar na sala inicial
        this.currentRoomId = "start";
        
        generateWorldMap();
        loadRoom(currentRoomId);
    }
    
    /**
     * Classe interna que representa uma sala do jogo
     */
    public static class GameRoom {
        public String id;
        public RoomType type;
        public int x, y; // Posição no mapa mundial
        public int width, height;
        public Map<Direction, String> connections; // Salas conectadas
        public List<Platform> platforms;
        public List<Enemy> enemies;
        public List<EnergyOrb> energyOrbs;
        public boolean discovered;
        public boolean completed;
        public String theme; // Tema visual da sala
        public int difficulty;
        
        public GameRoom(String id, RoomType type, int x, int y) {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
            this.width = ROOM_WIDTH;
            this.height = ROOM_HEIGHT;
            this.connections = new HashMap<>();
            this.platforms = new ArrayList<>();
            this.enemies = new ArrayList<>();
            this.energyOrbs = new ArrayList<>();
            this.discovered = false;
            this.completed = false;
            this.theme = "cyber_purple";
            this.difficulty = 1;
        }
        
        public void addConnection(Direction direction, String roomId) {
            connections.put(direction, roomId);
        }
        
        public String getConnection(Direction direction) {
            return connections.get(direction);
        }
        
        public boolean hasConnection(Direction direction) {
            return connections.containsKey(direction);
        }
    }

    /**
     * Atualiza o sistema baseado na posição do player
     */
    public void update(int playerX, int playerY) {
        // Atualizar sistema de batida musical
        updateMusicBeat();
        
        // Verificar se o player saiu da sala atual
        checkRoomTransition(playerX, playerY);
        
        // Atualizar elementos da sala atual
        updateCurrentRoom();
    }
    
    private void updateMusicBeat() {
        // Usar o sistema de áudio melhorado para detecção de batida
        if (AudioManager.isBeatDetectionEnabled()) {
            onBeat = AudioManager.isOnBeat();
            
            // Atualizar intervalo baseado no BPM da música
            int currentBPM = AudioManager.getCurrentBPM();
            beatInterval = 60000 / currentBPM; // Converter BPM para millisegundos
        } else {
            // Fallback para sistema básico
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBeatTime >= beatInterval) {
                onBeat = true;
                lastBeatTime = currentTime;
            } else {
                onBeat = false;
            }
        }
    }
    
    /**
     * Verifica se o player deve transitar para uma nova sala
     */
    private void checkRoomTransition(int playerX, int playerY) {
        if (currentRoom == null) return;
        
        // Verificar bordas da sala para transição
        Direction transitionDirection = null;
        
        if (playerX < 0) {
            transitionDirection = Direction.WEST;
        } else if (playerX > currentRoom.width) {
            transitionDirection = Direction.EAST;
        } else if (playerY < 0) {
            transitionDirection = Direction.NORTH;
        } else if (playerY > currentRoom.height) {
            transitionDirection = Direction.SOUTH;
        }
        
        // Fazer transição se houver conexão
        if (transitionDirection != null && currentRoom.hasConnection(transitionDirection)) {
            String nextRoomId = currentRoom.getConnection(transitionDirection);
            transitionToRoom(nextRoomId);
        }
    }
    
    /**
     * Faz a transição para uma nova sala
     */
    public void transitionToRoom(String roomId) {
        if (worldMap.containsKey(roomId)) {
            currentRoomId = roomId;
            loadRoom(roomId);
            System.out.println("Transitioning to room: " + roomId);
        }
    }
    
    /**
     * Carrega uma sala específica
     */
    private void loadRoom(String roomId) {
        currentRoom = worldMap.get(roomId);
        if (currentRoom == null) return;
        
        // Marcar sala como descoberta
        currentRoom.discovered = true;
        
        // Carregar elementos da sala
        activePlatforms.clear();
        activeEnemies.clear();
        activeEnergyOrbs.clear();
        
        activePlatforms.addAll(currentRoom.platforms);
        activeEnemies.addAll(currentRoom.enemies);
        activeEnergyOrbs.addAll(currentRoom.energyOrbs);
    }
    
    /**
     * Atualiza elementos da sala atual
     */
    private void updateCurrentRoom() {
        // Atualizar plataformas móveis
        for (Platform platform : activePlatforms) {
            if (platform.getPlatformType() == Platform.PlatformType.MOVING) {
                platform.update(0); // Delta time
            }
        }
    }
    
    /**
     * Gera o mapa mundial com salas interconectadas
     */
    private void generateWorldMap() {
        // Criar sala inicial (tipo hub)
        GameRoom startRoom = new GameRoom("start", RoomType.TRANSITION_HUB, 0, 0);
        generateTransitionHub(startRoom);
        worldMap.put("start", startRoom);
        
        // Criar rede de salas interconectadas
        generateMainPath();
        generateSecretAreas();
        generateVerticalShafts();
        generatePrecisionChallenges();
        generateBossRooms();
        
        // Conectar todas as salas
        connectRooms();
    }
    
    /**
     * Gera o caminho principal do jogo
     */
    private void generateMainPath() {
        String[] mainPath = {
            "forest_entry", "precision_valley", "vertical_ascent", 
            "branching_caverns", "crystal_chambers", "cyber_core"
        };
        
        for (int i = 0; i < mainPath.length; i++) {
            RoomType type = determineRoomType(i);
            GameRoom room = new GameRoom(mainPath[i], type, (i + 1) * 2, 0);
            room.difficulty = (i / 2) + 1; // Dificuldade cresce gradualmente
            
            generateRoomContent(room);
            worldMap.put(mainPath[i], room);
            
            // Conectar com sala anterior
            if (i > 0) {
                GameRoom prevRoom = worldMap.get(mainPath[i - 1]);
                prevRoom.addConnection(Direction.EAST, mainPath[i]);
                room.addConnection(Direction.WEST, mainPath[i - 1]);
            }
        }
        
        // Conectar primeira sala com o hub
        GameRoom firstRoom = worldMap.get(mainPath[0]);
        GameRoom startRoom = worldMap.get("start");
        startRoom.addConnection(Direction.EAST, mainPath[0]);
        firstRoom.addConnection(Direction.WEST, "start");
    }
    
    private RoomType determineRoomType(int index) {
        switch (index % 4) {
            case 0: return RoomType.PRECISION_CHALLENGE;
            case 1: return RoomType.VERTICAL_SHAFT;
            case 2: return RoomType.BRANCHING_PATHS;
            case 3: return RoomType.TRANSITION_HUB;
            default: return RoomType.PRECISION_CHALLENGE;
        }
    }
    
    /**
     * Gera conteúdo específico para cada tipo de sala
     */
    private void generateRoomContent(GameRoom room) {
        switch (room.type) {
            case PRECISION_CHALLENGE:
                generatePrecisionChallengeRoom(room);
                break;
            case VERTICAL_SHAFT:
                generateVerticalShaftRoom(room);
                break;
            case BRANCHING_PATHS:
                generateBranchingPathsRoom(room);
                break;
            case TRANSITION_HUB:
                generateTransitionHub(room);
                break;
            case SECRET_AREA:
                generateSecretAreaRoom(room);
                break;
            case BOSS_CHAMBER:
                generateBossChamberRoom(room);
                break;
            case REST_AREA:
                generateRestAreaRoom(room);
                break;
        }
    }
    
    /**
     * DESAFIOS DE PRECISÃO (estilo Celeste)
     * Sequências que exigem timing e precisão perfeitos
     */
    private void generatePrecisionChallengeRoom(GameRoom room) {
        // Chão base
        room.platforms.add(new Platform(0, GROUND_HEIGHT, 200, 50, Platform.PlatformType.GROUND));
        
        // Sequência de plataformas pequenas com gaps precisos
        int currentX = 250;
        int currentY = GROUND_HEIGHT - 80;
        
        for (int i = 0; i < 8; i++) {
            // Plataformas pequenas que exigem precisão
            int platformWidth = 40 + random.nextInt(20);
            int gap = 65 + random.nextInt(10); // Gap que requer pulo perfeito
            
            // Variação de altura para criar ritmo
            int heightVariation = (i % 2 == 0) ? -20 : 20;
            currentY += heightVariation;
            currentY = Math.max(currentY, 150);
            currentY = Math.min(currentY, GROUND_HEIGHT - 50);
            
            Platform platform = new Platform(currentX, currentY, platformWidth, 15, Platform.PlatformType.CLOUD);
            room.platforms.add(platform);
            
            currentX += gap + platformWidth;
        }
        
        // Plataforma final de chegada
        room.platforms.add(new Platform(currentX, GROUND_HEIGHT - 100, 150, 30, Platform.PlatformType.GROUND));
        
        // Orbs de energia como recompensa pela precisão
        for (int i = 1; i < 8; i += 2) {
            Platform p = room.platforms.get(i + 1); // +1 por causa da plataforma base
            room.energyOrbs.add(new EnergyOrb(p.getX() + 20, p.getY() - 30, 100));
        }
    }
    
    /**
     * POÇOS VERTICAIS (estilo Hollow Knight)
     * Foco em exploração vertical e wall jumping
     */
    private void generateVerticalShaftRoom(GameRoom room) {
        // Paredes do poço
        room.platforms.add(new Platform(0, 0, 50, ROOM_HEIGHT, Platform.PlatformType.GROUND));
        room.platforms.add(new Platform(ROOM_WIDTH - 50, 0, 50, ROOM_HEIGHT, Platform.PlatformType.GROUND));
        
        // Plataformas para wall jumping (alternando lados)
        for (int level = 0; level < 8; level++) {
            int y = GROUND_HEIGHT - 50 - (level * 80);
            y = Math.max(y, 100);
            
            boolean leftSide = (level % 2 == 0);
            int x = leftSide ? 60 : ROOM_WIDTH - 150;
            
            // Plataforma pequena para wall jumping
            room.platforms.add(new Platform(x, y, 90, 15, Platform.PlatformType.CLOUD));
            
            // Inimigo ocasional para aumentar desafio
            if (level % 3 == 1) {
                room.enemies.add(new Enemy(x + 45, y - 20, x + 10, x + 80));
            }
        }
        
        // Entrada/saída no topo
        room.platforms.add(new Platform(ROOM_WIDTH / 2 - 75, 50, 150, 30, Platform.PlatformType.GROUND));
        
        // Cristais de energia espalhados verticalmente
        for (int level = 1; level < 8; level += 2) {
            int y = GROUND_HEIGHT - 30 - (level * 80);
            room.energyOrbs.add(new EnergyOrb(ROOM_WIDTH / 2, y, 75));
        }
    }
    
    /**
     * CAMINHOS RAMIFICADOS
     * Múltiplas rotas com diferentes dificuldades
     */
    private void generateBranchingPathsRoom(GameRoom room) {
        // Plataforma inicial
        room.platforms.add(new Platform(50, GROUND_HEIGHT - 50, 150, 30, Platform.PlatformType.GROUND));
        
        // Caminho inferior (mais fácil)
        generateLowerPath(room);
        
        // Caminho superior (mais difícil, mais recompensas)
        generateUpperPath(room);
        
        // Plataforma final onde os caminhos se encontram
        room.platforms.add(new Platform(ROOM_WIDTH - 200, GROUND_HEIGHT - 100, 150, 30, Platform.PlatformType.GROUND));
    }
    
    private void generateLowerPath(GameRoom room) {
        int currentX = 250;
        int pathY = GROUND_HEIGHT - 100;
        
        while (currentX < ROOM_WIDTH - 300) {
            int width = 80 + random.nextInt(40);
            int gap = 50 + random.nextInt(30);
            
            room.platforms.add(new Platform(currentX, pathY, width, 20, Platform.PlatformType.BRICK));
            currentX += width + gap;
        }
    }
    
    private void generateUpperPath(GameRoom room) {
        int currentX = 250;
        int pathY = GROUND_HEIGHT - 250;
        
        while (currentX < ROOM_WIDTH - 300) {
            int width = 50 + random.nextInt(30); // Plataformas menores
            int gap = 60 + random.nextInt(20);   // Gaps maiores
            
            room.platforms.add(new Platform(currentX, pathY, width, 15, Platform.PlatformType.CLOUD));
            
            // Orbs de energia como recompensa pelo caminho difícil
            if (random.nextBoolean()) {
                room.energyOrbs.add(new EnergyOrb(currentX + width/2, pathY - 30, 150));
            }
            
            currentX += width + gap;
        }
    }
    
    /**
     * HUB DE TRANSIÇÃO
     * Salas de descanso com múltiplas conexões
     */
    private void generateTransitionHub(GameRoom room) {
        // Área central ampla
        room.platforms.add(new Platform(ROOM_WIDTH/2 - 200, GROUND_HEIGHT, 400, 50, Platform.PlatformType.GROUND));
        
        // Plataformas de acesso para diferentes direções
        room.platforms.add(new Platform(50, GROUND_HEIGHT - 80, 100, 20, Platform.PlatformType.BRICK));  // Oeste
        room.platforms.add(new Platform(ROOM_WIDTH - 150, GROUND_HEIGHT - 80, 100, 20, Platform.PlatformType.BRICK)); // Leste
        room.platforms.add(new Platform(ROOM_WIDTH/2 - 50, GROUND_HEIGHT - 150, 100, 20, Platform.PlatformType.BRICK)); // Norte
        
        // Orb de energia para recuperação
        room.energyOrbs.add(new EnergyOrb(ROOM_WIDTH/2, GROUND_HEIGHT - 50, 200));
    }

    /**
     * Gera áreas secretas conectadas ao mapa principal
     */
    private void generateSecretAreas() {
        // Área secreta 1: Caverna de cristais
        GameRoom crystalCave = new GameRoom("crystal_cave", RoomType.SECRET_AREA, 1, -2);
        generateSecretAreaRoom(crystalCave);
        worldMap.put("crystal_cave", crystalCave);
        
        // Área secreta 2: Laboratório abandonado
        GameRoom abandonedLab = new GameRoom("abandoned_lab", RoomType.SECRET_AREA, 3, 2);
        generateSecretAreaRoom(abandonedLab);
        worldMap.put("abandoned_lab", abandonedLab);
    }
    
    private void generateSecretAreaRoom(GameRoom room) {
        // Layout especial para áreas secretas - mais recompensas
        room.platforms.add(new Platform(100, GROUND_HEIGHT, ROOM_WIDTH - 200, 50, Platform.PlatformType.GROUND));
        
        // Plataformas com cristais de energia
        for (int i = 0; i < 5; i++) {
            int x = 200 + (i * 150);
            int y = GROUND_HEIGHT - 100 - (i % 2 * 50);
            
            room.platforms.add(new Platform(x, y, 80, 20, Platform.PlatformType.BOUNCY));
            room.energyOrbs.add(new EnergyOrb(x + 40, y - 30, 250)); // Orbs valiosos
        }
    }
    
    /**
     * Gera poços verticais adicionais
     */
    private void generateVerticalShafts() {
        GameRoom deepShaft = new GameRoom("deep_shaft", RoomType.VERTICAL_SHAFT, 2, -3);
        generateVerticalShaftRoom(deepShaft);
        worldMap.put("deep_shaft", deepShaft);
    }
    
    /**
     * Gera salas de desafio de precisão adicionais
     */
    private void generatePrecisionChallenges() {
        GameRoom precisionTrial = new GameRoom("precision_trial", RoomType.PRECISION_CHALLENGE, 4, -1);
        generatePrecisionChallengeRoom(precisionTrial);
        worldMap.put("precision_trial", precisionTrial);
    }
    
    /**
     * Gera salas de chefe
     */
    private void generateBossRooms() {
        GameRoom bossRoom = new GameRoom("cyber_boss", RoomType.BOSS_CHAMBER, 6, 0);
        generateBossChamberRoom(bossRoom);
        worldMap.put("cyber_boss", bossRoom);
    }
    
    private void generateBossChamberRoom(GameRoom room) {
        // Arena circular para luta de chefe
        room.platforms.add(new Platform(ROOM_WIDTH/2 - 300, GROUND_HEIGHT, 600, 50, Platform.PlatformType.GROUND));
        
        // Plataformas elevadas para estratégia
        room.platforms.add(new Platform(200, GROUND_HEIGHT - 100, 100, 20, Platform.PlatformType.BRICK));
        room.platforms.add(new Platform(ROOM_WIDTH - 300, GROUND_HEIGHT - 100, 100, 20, Platform.PlatformType.BRICK));
        room.platforms.add(new Platform(ROOM_WIDTH/2 - 50, GROUND_HEIGHT - 150, 100, 20, Platform.PlatformType.BRICK));
        
        // Inimigo chefe (mais forte)
        room.enemies.add(new Enemy(ROOM_WIDTH/2, GROUND_HEIGHT - 50, ROOM_WIDTH/2 - 200, ROOM_WIDTH/2 + 200));
    }
    
    private void generateRestAreaRoom(GameRoom room) {
        // Área simples de descanso
        room.platforms.add(new Platform(0, GROUND_HEIGHT, ROOM_WIDTH, 50, Platform.PlatformType.GROUND));
        room.energyOrbs.add(new EnergyOrb(ROOM_WIDTH/2, GROUND_HEIGHT - 50, 300)); // Recuperação total
    }
    
    /**
     * Conecta todas as salas criando uma rede navegável
     */
    private void connectRooms() {
        // Conectar áreas secretas ao caminho principal
        GameRoom forestEntry = worldMap.get("forest_entry");
        GameRoom crystalCave = worldMap.get("crystal_cave");
        if (forestEntry != null && crystalCave != null) {
            forestEntry.addConnection(Direction.NORTH, "crystal_cave");
            crystalCave.addConnection(Direction.SOUTH, "forest_entry");
        }
        
        // Conectar laboratório abandonado
        GameRoom branchingCaverns = worldMap.get("branching_caverns");
        GameRoom abandonedLab = worldMap.get("abandoned_lab");
        if (branchingCaverns != null && abandonedLab != null) {
            branchingCaverns.addConnection(Direction.SOUTHEAST, "abandoned_lab");
            abandonedLab.addConnection(Direction.NORTHWEST, "branching_caverns");
        }
        
        // Conectar poço profundo
        GameRoom precisionValley = worldMap.get("precision_valley");
        GameRoom deepShaft = worldMap.get("deep_shaft");
        if (precisionValley != null && deepShaft != null) {
            precisionValley.addConnection(Direction.SOUTH, "deep_shaft");
            deepShaft.addConnection(Direction.NORTH, "precision_valley");
        }
        
        // Conectar teste de precisão
        GameRoom verticalAscent = worldMap.get("vertical_ascent");
        GameRoom precisionTrial = worldMap.get("precision_trial");
        if (verticalAscent != null && precisionTrial != null) {
            verticalAscent.addConnection(Direction.NORTHEAST, "precision_trial");
            precisionTrial.addConnection(Direction.SOUTHWEST, "vertical_ascent");
        }
        
        // Conectar sala do chefe
        GameRoom cyberCore = worldMap.get("cyber_core");
        GameRoom cyberBoss = worldMap.get("cyber_boss");
        if (cyberCore != null && cyberBoss != null) {
            cyberCore.addConnection(Direction.NORTH, "cyber_boss");
            cyberBoss.addConnection(Direction.SOUTH, "cyber_core");
        }
    }
    
    // === MÉTODOS PÚBLICOS PARA INTEGRAÇÃO ===
    
    /**
     * Obtém informações sobre a sala atual
     */
    public GameRoom getCurrentRoom() {
        return currentRoom;
    }
    
    /**
     * Obtém ID da sala atual
     */
    public String getCurrentRoomId() {
        return currentRoomId;
    }
    
    /**
     * Obtém o mapa completo do mundo
     */
    public Map<String, GameRoom> getWorldMap() {
        return new HashMap<>(worldMap);
    }
    
    /**
     * Verifica se uma sala foi descoberta
     */
    public boolean isRoomDiscovered(String roomId) {
        GameRoom room = worldMap.get(roomId);
        return room != null && room.discovered;
    }
    
    /**
     * Marca uma sala como completada
     */
    public void markRoomCompleted(String roomId) {
        GameRoom room = worldMap.get(roomId);
        if (room != null) {
            room.completed = true;
        }
    }
    
    /**
     * Calcula progresso do jogo baseado em salas descobertas
     */
    public float getExplorationProgress() {
        long discoveredRooms = worldMap.values().stream().mapToLong(room -> room.discovered ? 1 : 0).sum();
        return (float) discoveredRooms / worldMap.size();
    }
    
    /**
     * Retorna uma string com informações de debug
     */
    public String getDebugInfo() {
        return String.format(
            "Current Room: %s | Type: %s | Discovered: %d/%d | Completed: %d/%d",
            currentRoomId,
            currentRoom != null ? currentRoom.type : "null",
            (int) worldMap.values().stream().mapToLong(room -> room.discovered ? 1 : 0).sum(),
            worldMap.size(),
            (int) worldMap.values().stream().mapToLong(room -> room.completed ? 1 : 0).sum(),
            worldMap.size()
        );
    }

    // === MÉTODOS LEGADOS PARA COMPATIBILIDADE ===
    
    /**
     * Método de compatibilidade - mantido para não quebrar código existente
     */
    public void update(int playerX) {
        update(playerX, 300); // Y padrão
    }
    
    // Getters legados
    public List<Platform> getActivePlatforms() {
        return new ArrayList<>(activePlatforms);
    }

    public List<Enemy> getActiveEnemies() {
        return new ArrayList<>(activeEnemies);
    }

    public List<EnergyOrb> getActiveEnergyOrbs() {
        return new ArrayList<>(activeEnergyOrbs);
    }
    
    public boolean isOnBeat() {
        return onBeat;
    }
    
    public float getBeatIntensity() {
        return AudioManager.getBeatIntensity();
    }
    
    public void setBeatInterval(int intervalMs) {
        this.beatInterval = intervalMs;
    }
    
    public int getDifficulty() {
        return currentRoom != null ? currentRoom.difficulty : 1;
    }
    
    public void reset() {
        currentRoomId = "start";
        loadRoom(currentRoomId);
    }
}
