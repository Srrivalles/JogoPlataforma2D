package org.example.world;

import java.util.ArrayList;
import org.example.objects.Player;
import org.example.objects.FlyingEnemy;
import org.example.objects.CrystalFragment;
import java.awt.Graphics2D;

/**
 * Classe de demonstração para a fase "Abismo da Aurora"
 * Integra todos os elementos especiais da fase
 */
public class AuroraAbyssDemo {
    
    // Listas para armazenar os elementos da fase
    private ArrayList<Platform> platforms;
    private ArrayList<org.example.objects.Enemy> enemies;
    private ArrayList<FlyingEnemy> flyingEnemies;
    private ArrayList<Spike> spikes;
    private ArrayList<CrystalFragment> crystalFragments;
    private ArrayList<org.example.objects.EnergyOrb> energyOrbs;
    
    public AuroraAbyssDemo() {
        // Inicializar listas
        platforms = new ArrayList<>();
        enemies = new ArrayList<>();
        flyingEnemies = new ArrayList<>();
        spikes = new ArrayList<>();
        crystalFragments = new ArrayList<>();
        energyOrbs = new ArrayList<>();
        
        // Carregar elementos da fase
        loadAuroraAbyssElements();
    }
    
    /**
     * Carrega todos os elementos da fase Abismo da Aurora
     */
    private void loadAuroraAbyssElements() {
        // Carregar plataformas (usar método público)
        platforms = WorldBuilder.createPlatformsForLevel(4); // Fase infinita
        
        // Carregar inimigos (usar método público)
        enemies = WorldBuilder.createEnemiesForLevel(4, 1.0f); // Fase infinita
        
        // Carregar inimigos voadores
        flyingEnemies = WorldBuilder.createAuroraAbyssFlyingEnemies();
        
        // Carregar espinhos
        spikes = WorldBuilder.createAuroraAbyssSpikes();
        
        // Carregar fragmentos de cristal
        crystalFragments = WorldBuilder.createAuroraAbyssCrystalFragments();
        
        // Carregar orbs de energia (usar método padrão por enquanto)
        energyOrbs = WorldBuilder.createEnergyOrbsForLevel(1, 1.0f);
    }
    
    /**
     * Atualiza todos os elementos da fase
     */
    public void update(Player player) {
        // Atualizar plataformas
        for (Platform platform : platforms) {
            platform.update(0); // deltaTime = 0 para simplicidade
        }
        
        // Atualizar inimigos terrestres
        for (org.example.objects.Enemy enemy : enemies) {
            enemy.update();
        }
        
        // Atualizar inimigos voadores
        for (FlyingEnemy flyingEnemy : flyingEnemies) {
            flyingEnemy.update(player);
        }
        
        // Atualizar espinhos
        for (Spike spike : spikes) {
            spike.update();
        }
        
        // Atualizar fragmentos de cristal
        for (CrystalFragment fragment : crystalFragments) {
            fragment.update();
        }
        
        // Atualizar orbs de energia
        for (org.example.objects.EnergyOrb orb : energyOrbs) {
            orb.update(player); // Passar o player como parâmetro
        }
    }
    
    /**
     * Aplica efeitos e colisões no player
     */
    public void applyEffects(Player player) {
        // Verificar colisões com espinhos
        for (Spike spike : spikes) {
            spike.checkCollision(player);
        }
        
        // Verificar colisões com inimigos terrestres
        for (org.example.objects.Enemy enemy : enemies) {
            if (enemy.getHitbox().intersects(player.getHitbox())) {
                // player.takeDamage(1, null);
            }
        }
        
        // Verificar colisões com inimigos voadores
        for (FlyingEnemy flyingEnemy : flyingEnemies) {
            flyingEnemy.checkCollision(player);
        }
        
        // Verificar coleta de fragmentos de cristal
        for (CrystalFragment fragment : crystalFragments) {
            fragment.checkCollection(player);
        }
        
        // Verificar coleta de orbs de energia
        for (org.example.objects.EnergyOrb orb : energyOrbs) {
            if (orb.getHitbox().intersects(player.getHitbox())) {
                orb.collect(); // Método sem parâmetros
            }
        }
        
        // Aplicar efeitos das plataformas especiais
        applyPlatformEffects(player);
    }
    
    /**
     * Aplica efeitos das plataformas especiais
     */
    private void applyPlatformEffects(Player player) {
        for (Platform platform : platforms) {
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
                    case GROUND:
                    case BRICK:
                    case CLOUD:
                    case MOVING:
                    case PIPE:
                    case ONE_WAY:
                    default:
                        // Não têm efeitos especiais
                        break;
                }
            }
        }
    }
    
    /**
     * Desenha todos os elementos da fase
     */
    public void draw(Graphics2D g2d) {
        // Desenhar espinhos primeiro (fundo)
        for (Spike spike : spikes) {
            spike.draw(g2d);
        }
        
        // Desenhar plataformas
        for (Platform platform : platforms) {
            platform.draw(g2d);
        }
        
        // Desenhar inimigos terrestres
        for (org.example.objects.Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
        
        // Desenhar inimigos voadores
        for (FlyingEnemy flyingEnemy : flyingEnemies) {
            flyingEnemy.draw(g2d);
        }
        
        // Desenhar fragmentos de cristal
        for (CrystalFragment fragment : crystalFragments) {
            fragment.draw(g2d);
        }
        
        // Desenhar orbs de energia
        for (org.example.objects.EnergyOrb orb : energyOrbs) {
            orb.draw(g2d);
        }
    }
    
    /**
     * Adiciona plataformas às plataformas normais
     */
    public void addPlatformsTo(ArrayList<Platform> platformList) {
        platformList.addAll(platforms);
    }
    
    /**
     * Adiciona inimigos aos inimigos normais
     */
    public void addEnemiesTo(ArrayList<org.example.objects.Enemy> enemyList) {
        enemyList.addAll(enemies);
    }
    
    /**
     * Adiciona orbs aos orbs normais
     */
    public void addOrbsTo(ArrayList<org.example.objects.EnergyOrb> orbList) {
        orbList.addAll(energyOrbs);
    }
    
    // Getters para acesso direto às listas
    public ArrayList<Platform> getPlatforms() { return platforms; }
    public ArrayList<org.example.objects.Enemy> getEnemies() { return enemies; }
    public ArrayList<FlyingEnemy> getFlyingEnemies() { return flyingEnemies; }
    public ArrayList<Spike> getSpikes() { return spikes; }
    public ArrayList<CrystalFragment> getCrystalFragments() { return crystalFragments; }
    public ArrayList<org.example.objects.EnergyOrb> getEnergyOrbs() { return energyOrbs; }
    
    /**
     * Limpa todos os elementos
     */
    public void clear() {
        platforms.clear();
        enemies.clear();
        flyingEnemies.clear();
        spikes.clear();
        crystalFragments.clear();
        energyOrbs.clear();
    }
    
    /**
     * Recarrega elementos da fase
     */
    public void reload() {
        clear();
        loadAuroraAbyssElements();
    }
    
    /**
     * Obtém informações sobre a fase
     */
    public String getPhaseInfo() {
        return "Abismo da Aurora - Fase Infinita\n" +
               "Plataformas: " + platforms.size() + "\n" +
               "Inimigos: " + enemies.size() + "\n" +
               "Inimigos Voadores: " + flyingEnemies.size() + "\n" +
               "Espinhos: " + spikes.size() + "\n" +
               "Fragmentos de Cristal: " + crystalFragments.size() + "\n" +
               "Orbs de Energia: " + energyOrbs.size();
    }
}
