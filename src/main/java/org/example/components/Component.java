package org.example.components;
/**
 * Interface base para todos os componentes do sistema Component-Based Design
 * Cada componente é responsável por uma funcionalidade específica
 */
public interface Component {
    
    /**
     * Atualiza o componente a cada frame
     * @param deltaTime tempo decorrido desde o último frame
     */
    void update(float deltaTime);
    
    /**
     * Inicializa o componente
     */
    void initialize();
    
    /**
     * Limpa recursos do componente
     */
    void dispose();
    
    /**
     * Verifica se o componente está ativo
     * @return true se ativo, false caso contrário
     */
    boolean isActive();
    
    /**
     * Ativa/desativa o componente
     * @param active estado desejado
     */
    void setActive(boolean active);
}
