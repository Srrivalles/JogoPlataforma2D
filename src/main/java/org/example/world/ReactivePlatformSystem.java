package org.example.world;

import org.example.objects.Player;
import java.util.ArrayList;
import java.util.Random;

/**
 * Sistema de plataformas reativas que respondem ao jogador
 * Cria efeitos visuais e mecânicos baseados na proximidade e ações do jogador
 */
public class ReactivePlatformSystem {
    
    private static Random random = new Random();
    private static ArrayList<ReactivePlatform> reactivePlatforms = new ArrayList<>();
    
    /**
     * Plataforma reativa que responde ao jogador
     */
    public static class ReactivePlatform {
        public Platform platform;
        public PlatformType type;
        public float reactionRadius;
        public float animationTimer;
        public boolean isActivated;
        public float glowIntensity;
        public float pulseSpeed;
        public int colorShift;
        
        public enum PlatformType {
            ENERGY_PULSE,      // Pulsa com energia quando o jogador se aproxima
            QUANTUM_SHIFT,     // Muda de posição aleatoriamente
            GRAVITY_FIELD,     // Afeta a gravidade do jogador
            TELEPORT_PAD,      // Teleporta o jogador para outra plataforma
            SHIELD_GENERATOR,  // Gera escudo temporário
            SPEED_BOOST,       // Aumenta velocidade do jogador
            INVISIBLE,         // Aparece apenas quando o jogador se aproxima
            MOVING_BRIDGE      // Cria ponte móvel
        }
        
        public ReactivePlatform(Platform platform, PlatformType type, float reactionRadius) {
            this.platform = platform;
            this.type = type;
            this.reactionRadius = reactionRadius;
            this.animationTimer = 0;
            this.isActivated = false;
            this.glowIntensity = 0;
            this.pulseSpeed = 1.0f + random.nextFloat() * 2.0f;
            this.colorShift = random.nextInt(360);
        }
        
        public void update(Player player) {
            animationTimer += 0.016f; // ~60 FPS
            
            float distance = calculateDistance(player);
            boolean wasActivated = isActivated;
            isActivated = distance <= reactionRadius;
            
            if (isActivated && !wasActivated) {
                onPlayerEnter();
            } else if (!isActivated && wasActivated) {
                onPlayerExit();
            }
            
            if (isActivated) {
                updateActivated();
            } else {
                updateInactive();
            }
        }
        
        private float calculateDistance(Player player) {
            float dx = (player.x + player.width/2) - (platform.x + platform.width/2);
            float dy = (player.y + player.height/2) - (platform.y + platform.height/2);
            return (float) Math.sqrt(dx * dx + dy * dy);
        }
        
        private void onPlayerEnter() {
            switch (type) {
                case ENERGY_PULSE:
                    glowIntensity = 1.0f;
                    break;
                case QUANTUM_SHIFT:
                    // Muda posição aleatoriamente
                    platform.x += (random.nextFloat() - 0.5f) * 100;
                    platform.y += (random.nextFloat() - 0.5f) * 50;
                    break;
                case GRAVITY_FIELD:
                    // Aplica efeito de gravidade reduzida
                    break;
                case TELEPORT_PAD:
                    // Prepara teleporte
                    break;
                case SHIELD_GENERATOR:
                    // Ativa escudo
                    break;
                case SPEED_BOOST:
                    // Aplica boost de velocidade
                    break;
                case INVISIBLE:
                    // Torna visível
                    break;
                case MOVING_BRIDGE:
                    // Ativa ponte móvel
                    break;
            }
        }
        
        private void onPlayerExit() {
            switch (type) {
                case ENERGY_PULSE:
                    glowIntensity = 0.3f;
                    break;
                case QUANTUM_SHIFT:
                    // Retorna à posição original
                    break;
                case GRAVITY_FIELD:
                    // Remove efeito de gravidade
                    break;
                case TELEPORT_PAD:
                    // Desativa teleporte
                    break;
                case SHIELD_GENERATOR:
                    // Desativa escudo
                    break;
                case SPEED_BOOST:
                    // Remove boost de velocidade
                    break;
                case INVISIBLE:
                    // Torna invisível
                    break;
                case MOVING_BRIDGE:
                    // Desativa ponte móvel
                    break;
            }
        }
        
        private void updateActivated() {
            switch (type) {
                case ENERGY_PULSE:
                    glowIntensity = 0.7f + 0.3f * (float) Math.sin(animationTimer * pulseSpeed);
                    break;
                case QUANTUM_SHIFT:
                    // Efeito de deslocamento quântico
                    break;
                case GRAVITY_FIELD:
                    // Efeito de campo gravitacional
                    break;
                case TELEPORT_PAD:
                    // Efeito de teleporte
                    break;
                case SHIELD_GENERATOR:
                    // Efeito de gerador de escudo
                    break;
                case SPEED_BOOST:
                    // Efeito de boost de velocidade
                    break;
                case INVISIBLE:
                    // Efeito de invisibilidade
                    break;
                case MOVING_BRIDGE:
                    // Efeito de ponte móvel
                    break;
            }
        }
        
        private void updateInactive() {
            switch (type) {
                case ENERGY_PULSE:
                    glowIntensity = 0.2f + 0.1f * (float) Math.sin(animationTimer * pulseSpeed * 0.5f);
                    break;
                case QUANTUM_SHIFT:
                    // Efeito sutil de deslocamento
                    break;
                case GRAVITY_FIELD:
                    // Efeito sutil de campo gravitacional
                    break;
                case TELEPORT_PAD:
                    // Efeito sutil de teleporte
                    break;
                case SHIELD_GENERATOR:
                    // Efeito sutil de gerador
                    break;
                case SPEED_BOOST:
                    // Efeito sutil de boost
                    break;
                case INVISIBLE:
                    // Efeito sutil de invisibilidade
                    break;
                case MOVING_BRIDGE:
                    // Efeito sutil de ponte
                    break;
            }
        }
    }
    
    /**
     * Cria plataformas reativas para o mapa
     */
    public static void createReactivePlatforms(ArrayList<Platform> platforms) {
        reactivePlatforms.clear();
        
        // Adicionar plataformas reativas em posições estratégicas
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            
            // 20% de chance de ser uma plataforma reativa
            if (random.nextFloat() < 0.2f) {
                ReactivePlatform.PlatformType type = getRandomPlatformType();
                float radius = 80 + random.nextFloat() * 120; // Raio de reação
                
                ReactivePlatform reactivePlatform = new ReactivePlatform(platform, type, radius);
                reactivePlatforms.add(reactivePlatform);
            }
        }
    }
    
    /**
     * Atualiza todas as plataformas reativas
     */
    public static void updateReactivePlatforms(Player player) {
        for (ReactivePlatform reactivePlatform : reactivePlatforms) {
            reactivePlatform.update(player);
        }
    }
    
    /**
     * Obtém uma plataforma reativa aleatória
     */
    private static ReactivePlatform.PlatformType getRandomPlatformType() {
        ReactivePlatform.PlatformType[] types = ReactivePlatform.PlatformType.values();
        return types[random.nextInt(types.length)];
    }
    
    /**
     * Obtém todas as plataformas reativas
     */
    public static ArrayList<ReactivePlatform> getReactivePlatforms() {
        return reactivePlatforms;
    }
    
    /**
     * Limpa todas as plataformas reativas
     */
    public static void clearReactivePlatforms() {
        reactivePlatforms.clear();
    }
}
