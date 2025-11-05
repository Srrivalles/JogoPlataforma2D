// IBackgroundManager.java
package org.example.world;

import java.awt.Graphics2D;

/**
 * Interface que define o contrato para qualquer gerenciador de fundo.
 * Todos os temas (Cyberpunk, Halloween) devem implementar estes métodos.
 */
public interface IBackgroundManager {
    void update(int playerX, int playerY, long gameTime);
    void renderDistantBackground(Graphics2D g2d, int cameraX, int cameraY);
    void renderMidBackground(Graphics2D g2d, int cameraX, int cameraY);
}