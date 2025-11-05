package org.example.world;

import org.example.ui.GameConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Sistema de validação de caminhos para garantir que todas as plataformas sejam acessíveis
 */
public class PathValidationSystem {
    
    // Capacidades do jogador
    private static final double PLAYER_SPEED = GameConfig.PLAYER_SPEED;
    private static final double JUMP_STRENGTH = GameConfig.PLAYER_JUMP_STRENGTH;
    private static final double DASH_SPEED = GameConfig.PLAYER_DASH_SPEED;
    private static final double GRAVITY = GameConfig.GRAVITY;
    
    // Constantes de cálculo
    private static final double MAX_JUMP_HEIGHT = (JUMP_STRENGTH * JUMP_STRENGTH) / (2 * GRAVITY);
    private static final double MAX_HORIZONTAL_JUMP = (JUMP_STRENGTH * PLAYER_SPEED) / GRAVITY;
    private static final double MAX_DASH_DISTANCE = DASH_SPEED * 0.5;
    private static final double MAX_REACHABLE_HEIGHT = MAX_JUMP_HEIGHT + MAX_DASH_DISTANCE;
    private static final double MAX_HORIZONTAL_REACH = MAX_HORIZONTAL_JUMP + MAX_DASH_DISTANCE;
    
    /**
     * Valida se um mapa é completamente acessível
     */
    public static boolean validateMap(ArrayList<Platform> platforms) {
        if (platforms.isEmpty()) return false;
        
        // Ordenar plataformas por posição X
        platforms.sort((a, b) -> Integer.compare(a.x, b.x));
        
        // Verificar se todas as plataformas são acessíveis
        boolean[] accessible = new boolean[platforms.size()];
        accessible[0] = true; // Primeira plataforma sempre acessível
        
        // Usar BFS para encontrar todas as plataformas acessíveis
        List<Integer> queue = new ArrayList<>();
        queue.add(0);
        
        while (!queue.isEmpty()) {
            int currentIndex = queue.remove(0);
            Platform current = platforms.get(currentIndex);
            
            // Verificar todas as outras plataformas
            for (int i = 0; i < platforms.size(); i++) {
                if (!accessible[i]) {
                    Platform target = platforms.get(i);
                    if (isReachable(current, target)) {
                        accessible[i] = true;
                        queue.add(i);
                    }
                }
            }
        }
        
        // Verificar se todas as plataformas são acessíveis
        for (boolean acc : accessible) {
            if (!acc) return false;
        }
        
        return true;
    }
    
    /**
     * Verifica se uma plataforma é alcançável a partir de outra
     */
    public static boolean isReachable(Platform from, Platform to) {
        double deltaX = to.x - (from.x + from.width);
        double deltaY = from.y - to.y;
        
        // Verificar se está dentro do alcance horizontal
        if (deltaX > MAX_HORIZONTAL_REACH) {
            return false;
        }
        
        // Verificar se está dentro do alcance vertical
        if (deltaY > MAX_REACHABLE_HEIGHT) {
            return false;
        }
        
        // Verificar se não está muito baixo (requer dash para baixo)
        if (deltaY < -MAX_JUMP_HEIGHT) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Corrige um mapa para torná-lo acessível
     */
    public static ArrayList<Platform> fixMap(ArrayList<Platform> platforms) {
        if (platforms.isEmpty()) return platforms;
        
        ArrayList<Platform> fixedPlatforms = new ArrayList<>();
        
        // Ordenar plataformas por posição X
        platforms.sort((a, b) -> Integer.compare(a.x, b.x));
        
        // Adicionar primeira plataforma
        fixedPlatforms.add(platforms.get(0));
        
        // Corrigir distâncias entre plataformas
        for (int i = 1; i < platforms.size(); i++) {
            Platform current = platforms.get(i);
            Platform previous = fixedPlatforms.get(fixedPlatforms.size() - 1);
            
            // Verificar se a plataforma atual é acessível
            if (isReachable(previous, current)) {
                fixedPlatforms.add(current);
            } else {
                // Criar plataforma intermediária
                Platform intermediate = createIntermediatePlatform(previous, current);
                if (intermediate != null) {
                    fixedPlatforms.add(intermediate);
                }
                fixedPlatforms.add(current);
            }
        }
        
        return fixedPlatforms;
    }
    
    /**
     * Cria uma plataforma intermediária para conectar duas plataformas
     */
    private static Platform createIntermediatePlatform(Platform from, Platform to) {
        double deltaX = to.x - (from.x + from.width);
        double deltaY = from.y - to.y;
        
        // Se a distância horizontal é muito grande, criar plataforma intermediária
        if (deltaX > MAX_HORIZONTAL_REACH) {
            int intermediateX = from.x + from.width + (int)(MAX_HORIZONTAL_REACH / 2);
            int intermediateY = from.y - (int)(deltaY / 2);
            
            return new Platform(intermediateX, intermediateY, 80, 20, Platform.PlatformType.BRICK);
        }
        
        // Se a distância vertical é muito grande, criar plataforma intermediária
        if (deltaY > MAX_REACHABLE_HEIGHT) {
            int intermediateX = from.x + from.width + (int)(deltaX / 2);
            int intermediateY = from.y - (int)(MAX_REACHABLE_HEIGHT / 2);
            
            return new Platform(intermediateX, intermediateY, 80, 20, Platform.PlatformType.BRICK);
        }
        
        return null;
    }
    
    /**
     * Gera um mapa de teste para validar o sistema
     */
    public static ArrayList<Platform> generateTestMap() {
        ArrayList<Platform> platforms = new ArrayList<>();
        
        // Mapa de teste com gaps controlados
        platforms.add(new Platform(0, 500, 100, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(150, 480, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(280, 460, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(410, 500, 100, 50, Platform.PlatformType.GROUND));
        
        return platforms;
    }
    
    /**
     * Valida e corrige um mapa automaticamente
     */
    public static ArrayList<Platform> validateAndFixMap(ArrayList<Platform> platforms) {
        if (validateMap(platforms)) {
            return platforms;
        } else {
            return fixMap(platforms);
        }
    }
    
    /**
     * Cria um mapa progressivo com dificuldade crescente
     */
    public static ArrayList<Platform> createProgressiveMap() {
        ArrayList<Platform> platforms = new ArrayList<>();
        
        // Zona 1: Movimento Básico (gaps pequenos)
        platforms.add(new Platform(0, 500, 100, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(120, 480, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(240, 460, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(360, 500, 100, 50, Platform.PlatformType.GROUND));
        
        // Zona 2: Pulos médios
        platforms.add(new Platform(500, 500, 100, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(650, 470, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(800, 440, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(950, 500, 100, 50, Platform.PlatformType.GROUND));
        
        // Zona 3: Pulos altos
        platforms.add(new Platform(1100, 500, 100, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1300, 450, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1500, 400, 80, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(1700, 500, 100, 50, Platform.PlatformType.GROUND));
        
        // Zona 4: Desafios avançados
        platforms.add(new Platform(1900, 500, 100, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(2100, 450, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2300, 400, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2500, 350, 60, 20, Platform.PlatformType.BRICK));
        platforms.add(new Platform(2700, 500, 100, 50, Platform.PlatformType.GROUND));
        
        return platforms;
    }
}
