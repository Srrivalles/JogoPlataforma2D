package org.example.components;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe base para todas as entidades do jogo usando Component-Based Design
 * Cada entidade é uma coleção de componentes que definem seu comportamento
 */
public class Entity {
    
    private String id;
    private String name;
    private boolean active = true;
    
    // Mapa de componentes por tipo
    private Map<Class<? extends Component>, Component> components;
    
    // Posição e dimensões básicas (usadas por vários componentes)
    public float x, y;
    public float width, height;
    
    public Entity(String id, String name) {
        this.id = id;
        this.name = name;
        this.components = new HashMap<>();
        this.x = 0;
        this.y = 0;
        this.width = 32;
        this.height = 32;
    }
    
    /**
     * Adiciona um componente à entidade
     * @param component componente a ser adicionado
     * @param <T> tipo do componente
     * @return a própria entidade para method chaining
     */
    public <T extends Component> Entity addComponent(T component) {
        components.put(component.getClass(), component);
        component.initialize();
        return this;
    }
    
    /**
     * Remove um componente da entidade
     * @param componentClass classe do componente a ser removido
     * @param <T> tipo do componente
     * @return o componente removido ou null se não encontrado
     */
    public <T extends Component> T removeComponent(Class<T> componentClass) {
        T component = getComponent(componentClass);
        if (component != null) {
            component.dispose();
            components.remove(componentClass);
        }
        return component;
    }
    
    /**
     * Obtém um componente específico
     * @param componentClass classe do componente desejado
     * @param <T> tipo do componente
     * @return o componente ou null se não encontrado
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return (T) components.get(componentClass);
    }
    
    /**
     * Verifica se a entidade possui um componente específico
     * @param componentClass classe do componente
     * @return true se possui o componente
     */
    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }
    
    /**
     * Atualiza todos os componentes ativos da entidade
     * @param deltaTime tempo decorrido desde o último frame
     */
    public void update(float deltaTime) {
        if (!active) return;
        
        for (Component component : components.values()) {
            if (component.isActive()) {
                component.update(deltaTime);
            }
        }
    }
    
    /**
     * Limpa todos os componentes da entidade
     */
    public void dispose() {
        for (Component component : components.values()) {
            component.dispose();
        }
        components.clear();
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) { 
        this.x = x; 
        this.y = y; 
    }
    
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public void setSize(float width, float height) { 
        this.width = width; 
        this.height = height; 
    }
}
