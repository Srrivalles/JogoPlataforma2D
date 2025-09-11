package org.example.world;

import java.util.ArrayList;
import org.example.objects.Player;
import java.awt.Graphics2D;

/**
 * Classe de demonstração para mostrar como integrar os novos elementos de ambiente
 * no GamePanel existente
 */
public class EnvironmentDemo {
    
    // Listas para armazenar os elementos de ambiente
    private ArrayList<WindEffect> windEffects;
    private ArrayList<GravityWell> gravityWells;
    private ArrayList<Teleporter> teleporters;
    private ArrayList<Checkpoint> checkpoints;
    private ArrayList<Platform> specialPlatforms;
    
    public EnvironmentDemo() {
        // Inicializar listas
        windEffects = new ArrayList<>();
        gravityWells = new ArrayList<>();
        teleporters = new ArrayList<>();
        checkpoints = new ArrayList<>();
        specialPlatforms = new ArrayList<>();
        
        // Carregar elementos de demonstração
        loadDemoElements();
    }
    
    /**
     * Carrega elementos de demonstração
     */
    private void loadDemoElements() {
        // Carregar plataformas especiais
        specialPlatforms = WorldBuilder.createSpecialPlatforms();
        
        // Carregar efeitos de ambiente
        windEffects = WorldBuilder.createWindEffects();
        gravityWells = WorldBuilder.createGravityWells();
        teleporters = WorldBuilder.createTeleporters();
        checkpoints = WorldBuilder.createCheckpoints();
    }
    
    /**
     * Atualiza todos os elementos de ambiente
     */
    public void update() {
        // Atualizar efeitos de ambiente
        WorldBuilder.updateEnvironmentEffects(windEffects, gravityWells, teleporters, checkpoints);
        
        // Atualizar plataformas especiais
        for (Platform platform : specialPlatforms) {
            platform.update(0); // deltaTime = 0 para simplicidade
        }
    }
    
    /**
     * Aplica efeitos no player
     */
    public void applyEffects(Player player) {
        // Aplicar efeitos de vento
        WorldBuilder.applyWindEffects(player, windEffects);
        
        // Aplicar efeitos de gravidade
        WorldBuilder.applyGravityEffects(player, gravityWells);
        
        // Verificar teleporters
        WorldBuilder.checkTeleporters(player, teleporters);
        
        // Verificar checkpoints
        WorldBuilder.checkCheckpoints(player, checkpoints);
        
        // Aplicar efeitos das plataformas especiais
        applyPlatformEffects(player);
    }
    
    /**
     * Aplica efeitos das plataformas especiais
     */
    private void applyPlatformEffects(Player player) {
        for (Platform platform : specialPlatforms) {
            // Verificar colisão com plataforma
            if (platform.getHitbox().intersects(player.getHitbox())) {
                // Aplicar efeitos baseados no tipo
                switch (platform.getPlatformType()) {
                    case ICE:
                        platform.onPlayerContact(player);
                        break;
                    case BOUNCY:
                        if (player.isOnGround) {
                            platform.onPlayerLanded(player);
                        }
                        break;
                    case BREAKABLE:
                        if (player.isOnGround) {
                            platform.onPlayerLanded(player);
                        }
                        break;
                }
            }
        }
    }
    
    /**
     * Desenha todos os elementos de ambiente
     */
    public void draw(Graphics2D g2d) {
        // Desenhar plataformas especiais
        for (Platform platform : specialPlatforms) {
            platform.draw(g2d);
        }
        
        // Desenhar efeitos de ambiente
        WorldBuilder.drawEnvironmentEffects(g2d, windEffects, gravityWells, teleporters, checkpoints);
    }
    
    /**
     * Adiciona plataformas especiais às plataformas normais
     */
    public void addSpecialPlatformsTo(ArrayList<Platform> platforms) {
        platforms.addAll(specialPlatforms);
    }
    
    // Getters para acesso direto às listas (se necessário)
    public ArrayList<WindEffect> getWindEffects() { return windEffects; }
    public ArrayList<GravityWell> getGravityWells() { return gravityWells; }
    public ArrayList<Teleporter> getTeleporters() { return teleporters; }
    public ArrayList<Checkpoint> getCheckpoints() { return checkpoints; }
    public ArrayList<Platform> getSpecialPlatforms() { return specialPlatforms; }
    
    /**
     * Limpa todos os elementos
     */
    public void clear() {
        windEffects.clear();
        gravityWells.clear();
        teleporters.clear();
        checkpoints.clear();
        specialPlatforms.clear();
    }
    
    /**
     * Recarrega elementos de demonstração
     */
    public void reload() {
        clear();
        loadDemoElements();
    }
}

