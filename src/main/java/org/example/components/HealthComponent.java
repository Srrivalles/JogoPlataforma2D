package org.example.components;

/**
 * Componente responsável pelo sistema de saúde e vidas de uma entidade
 * Gerencia HP, vidas, regeneração e eventos de dano/morte
 */
public class HealthComponent implements Component {
    
    private Entity entity;
    private boolean active = true;
    
    // Saúde atual
    private int currentHealth;
    private int maxHealth;
    
    // Sistema de vidas
    private int lives;
    private int maxLives;
    
    // Regeneração
    private boolean canRegenerate = false;
    private float regenerationRate = 0; // HP por segundo
    private float regenerationTimer = 0;
    private float regenerationDelay = 0; // Delay antes de começar a regenerar
    
    // Invulnerabilidade
    private boolean isInvulnerable = false;
    // private float invulnerabilityDuration = 0; // Removido - não utilizado
    private float invulnerabilityTimer = 0;
    
    // Efeitos visuais
    private boolean isTakingDamage = false;
    private float damageEffectTimer = 0;
    private float damageEffectDuration = 1.0f; // 1 segundo
    
    // Callbacks
    private HealthCallback onHealthChanged;
    private HealthCallback onLivesChanged;
    private HealthCallback onDeath;
    private HealthCallback onDamage;
    
    public interface HealthCallback {
        void onEvent(Entity entity, HealthComponent health, int value);
    }
    
    public HealthComponent(Entity entity) {
        this.entity = entity;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.maxLives = 3;
        this.lives = maxLives;
    }
    
    public HealthComponent(Entity entity, int maxHealth, int maxLives) {
        this.entity = entity;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.maxLives = maxLives;
        this.lives = maxLives;
    }
    
    @Override
    public void update(float deltaTime) {
        if (!active) return;
        
        // Atualizar timer de invulnerabilidade
        if (isInvulnerable) {
            invulnerabilityTimer -= deltaTime;
            if (invulnerabilityTimer <= 0) {
                isInvulnerable = false;
            }
        }
        
        // Atualizar efeito visual de dano
        if (isTakingDamage) {
            damageEffectTimer -= deltaTime;
            if (damageEffectTimer <= 0) {
                isTakingDamage = false;
            }
        }
        
        // Regeneração de saúde
        if (canRegenerate && currentHealth < maxHealth && !isInvulnerable) {
            regenerationTimer += deltaTime;
            if (regenerationTimer >= regenerationDelay) {
                float regenAmount = regenerationRate * deltaTime;
                heal((int) regenAmount);
            }
        }
    }
    
    /**
     * Aplica dano à entidade
     * @param damage quantidade de dano
     * @return true se o dano foi aplicado
     */
    public boolean takeDamage(int damage) {
        if (isInvulnerable || damage <= 0) {
            return false;
        }
        
        currentHealth = Math.max(0, currentHealth - damage);
        
        // Ativar invencibilidade por 1 segundo após tomar dano
        setInvulnerable(1.0f);
        
        // Ativar efeito visual de dano
        isTakingDamage = true;
        damageEffectTimer = damageEffectDuration;
        
        // Callback de dano
        if (onDamage != null) {
            onDamage.onEvent(entity, this, damage);
        }
        
        // Callback de mudança de saúde
        if (onHealthChanged != null) {
            onHealthChanged.onEvent(entity, this, currentHealth);
        }
        
        // Verificar morte
        if (currentHealth <= 0) {
            die();
            return true;
        }
        
        return true;
    }
    
    /**
     * Cura a entidade
     * @param healAmount quantidade de cura
     * @return true se a cura foi aplicada
     */
    public boolean heal(int healAmount) {
        if (healAmount <= 0) return false;
        
        int oldHealth = currentHealth;
        currentHealth = Math.min(maxHealth, currentHealth + healAmount);
        
        if (currentHealth != oldHealth && onHealthChanged != null) {
            onHealthChanged.onEvent(entity, this, currentHealth);
        }
        
        return true;
    }
    
    /**
     * Remove uma vida da entidade
     * @return true se uma vida foi removida
     */
    public boolean loseLife() {
        if (lives <= 0 || isInvulnerable) return false;
        
        lives--;
        
        // Ativar invencibilidade por 1 segundo após perder vida
        setInvulnerable(1.0f);
        
        // Resetar saúde para máxima ao perder vida
        currentHealth = maxHealth;
        
        // Ativar efeito visual de dano
        isTakingDamage = true;
        damageEffectTimer = damageEffectDuration;
        
        // Callback de mudança de vidas
        if (onLivesChanged != null) {
            onLivesChanged.onEvent(entity, this, lives);
        }
        
        // Callback de mudança de saúde
        if (onHealthChanged != null) {
            onHealthChanged.onEvent(entity, this, currentHealth);
        }
        
        // Verificar game over
        if (lives <= 0) {
            die();
        }
        
        return true;
    }
    
    /**
     * Adiciona uma vida à entidade
     * @return true se uma vida foi adicionada
     */
    public boolean gainLife() {
        if (lives >= maxLives) return false;
        
        lives++;
        
        if (onLivesChanged != null) {
            onLivesChanged.onEvent(entity, this, lives);
        }
        
        return true;
    }
    
    /**
     * Mata a entidade
     */
    public void die() {
        currentHealth = 0;
        
        if (onDeath != null) {
            onDeath.onEvent(entity, this, 0);
        }
    }
    
    /**
     * Revive a entidade
     * @param health quantidade de saúde ao reviver
     */
    public void revive(int health) {
        currentHealth = Math.min(maxHealth, health);
        isTakingDamage = false;
        damageEffectTimer = 0;
        
        if (onHealthChanged != null) {
            onHealthChanged.onEvent(entity, this, currentHealth);
        }
    }
    
    /**
     * Torna a entidade invulnerável por um tempo
     * @param duration duração em segundos
     */
    public void setInvulnerable(float duration) {
        isInvulnerable = true;
        // invulnerabilityDuration = duration; // Comentado - variável removida
        invulnerabilityTimer = duration;
    }
    
    /**
     * Define se a entidade pode regenerar saúde
     * @param canRegen true se pode regenerar
     * @param rate taxa de regeneração por segundo
     * @param delay delay antes de começar a regenerar
     */
    public void setRegeneration(boolean canRegen, float rate, float delay) {
        this.canRegenerate = canRegen;
        this.regenerationRate = rate;
        this.regenerationDelay = delay;
        this.regenerationTimer = 0;
    }
    
    // Getters e Setters
    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public int getLives() { return lives; }
    public int getMaxLives() { return maxLives; }
    public boolean isAlive() { return lives > 0; }
    public boolean isDead() { return lives <= 0; }
    public boolean isInvulnerable() { return isInvulnerable; }
    public boolean isTakingDamage() { return isTakingDamage; }
    public float getHealthPercentage() { return (float) currentHealth / maxHealth; }
    public float getLivesPercentage() { return (float) lives / maxLives; }
    
    public void setMaxHealth(int maxHealth) { 
        this.maxHealth = maxHealth;
        this.currentHealth = Math.min(currentHealth, maxHealth);
    }
    
    public void setMaxLives(int maxLives) { 
        this.maxLives = maxLives;
        this.lives = Math.min(lives, maxLives);
    }
    
    public void setDamageEffectDuration(float duration) { 
        this.damageEffectDuration = duration; 
    }
    
    // Callbacks
    public void setOnHealthChanged(HealthCallback callback) { this.onHealthChanged = callback; }
    public void setOnLivesChanged(HealthCallback callback) { this.onLivesChanged = callback; }
    public void setOnDeath(HealthCallback callback) { this.onDeath = callback; }
    public void setOnDamage(HealthCallback callback) { this.onDamage = callback; }
    
    @Override
    public void initialize() {
        // Inicialização se necessário
    }
    
    @Override
    public void dispose() {
        // Limpeza se necessário
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
