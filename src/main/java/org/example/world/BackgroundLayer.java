package org.example.world;

import java.awt.*;

/**
 * Classe abstrata para camadas de fundo com efeito parallax
 */
public abstract class BackgroundLayer {
    protected int screenWidth, screenHeight;
    protected float parallaxSpeed;
    protected Color baseColor;
    
    public BackgroundLayer(int width, int height, float parallaxSpeed) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.parallaxSpeed = parallaxSpeed;
    }
    
    public abstract void update(int playerX, int playerY, long gameTime);
    public abstract void render(Graphics2D g2d, int cameraX, int cameraY);
}
