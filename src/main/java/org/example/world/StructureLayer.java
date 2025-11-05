package org.example.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Camada de estruturas antigas
 */
public class StructureLayer extends BackgroundLayer {
    private List<AncientStructure> structures;
    private Random random = new Random();
    
    public StructureLayer(int width, int height, float parallaxSpeed) {
        super(width, height, parallaxSpeed);
        this.structures = new ArrayList<>();
        generateStructures();
    }
    
    private void generateStructures() {
        for (int i = 0; i < 6; i++) {
            structures.add(new AncientStructure(
                i * 400 + random.nextInt(200),
                screenHeight - 150 - random.nextInt(100),
                40 + random.nextInt(60),
                80 + random.nextInt(120)
            ));
        }
    }
    
    @Override
    public void update(int playerX, int playerY, long gameTime) {
        for (AncientStructure structure : structures) {
            structure.update(gameTime);
        }
    }
    
    @Override
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        for (AncientStructure structure : structures) {
            structure.render(g2d, cameraX, cameraY, parallaxSpeed);
        }
    }
    
    private static class AncientStructure {
        int x, y, width, height;
        float glowIntensity = 0;
        long animationTimer = 0;
        
        public AncientStructure(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public void update(long gameTime) {
            animationTimer = gameTime;
            glowIntensity = (float)(Math.sin(animationTimer * 0.02) * 0.3 + 0.7);
        }
        
        public void render(Graphics2D g2d, int cameraX, int cameraY, float parallaxSpeed) {
            int renderX = (int)(x - (cameraX * parallaxSpeed));
            int renderY = y;
            
            // Estrutura principal
            Color structureColor = new Color(72, 61, 139, (int)(150 * glowIntensity));
            g2d.setColor(structureColor);
            g2d.fillRect(renderX, renderY, width, height);
            
            // Detalhes arquitetônicos
            g2d.setColor(new Color(106, 90, 205, 100));
            g2d.fillRect(renderX + 5, renderY + 10, width - 10, 5);
            g2d.fillRect(renderX + 5, renderY + height - 15, width - 10, 5);
            
            // Cristais brilhantes
            g2d.setColor(new Color(255, 0, 255, (int)(80 * glowIntensity)));
            g2d.fillOval(renderX + width / 2 - 3, renderY + height / 2 - 3, 6, 6);
            
            // Borda brilhante
            g2d.setColor(new Color(186, 85, 211, (int)(100 * glowIntensity)));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(renderX, renderY, width, height);
        }
    }
}
