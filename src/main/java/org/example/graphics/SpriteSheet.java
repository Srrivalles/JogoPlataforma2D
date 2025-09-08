package org.example.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe para carregar e gerenciar sprite sheets
 */
public class SpriteSheet {
    
    private BufferedImage sheet;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesPerRow;
    private int spritesPerColumn;
    private String name;
    
    // Cache de sprites individuais
    private Map<String, BufferedImage> spriteCache;
    
    public SpriteSheet(String name, String resourcePath, int spriteWidth, int spriteHeight) {
        this.name = name;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.spriteCache = new HashMap<>();
        
        loadSpriteSheet(resourcePath);
        
        // Calcular quantos sprites cabem na sheet
        this.spritesPerRow = sheet.getWidth() / spriteWidth;
        this.spritesPerColumn = sheet.getHeight() / spriteHeight;
    }
    
    /**
     * Carrega o sprite sheet do arquivo de recursos
     */
    private void loadSpriteSheet(String resourcePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                System.err.println("Erro: Não foi possível carregar o sprite sheet: " + resourcePath);
                // Criar uma imagem placeholder
                createPlaceholderSheet();
                return;
            }
            
            sheet = ImageIO.read(inputStream);
            inputStream.close();
            
            System.out.println("Sprite sheet carregado: " + name + " (" + sheet.getWidth() + "x" + sheet.getHeight() + ")");
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar sprite sheet " + resourcePath + ": " + e.getMessage());
            createPlaceholderSheet();
        }
    }
    
    /**
     * Cria um sprite sheet placeholder quando não consegue carregar a imagem
     */
    private void createPlaceholderSheet() {
        // Criar uma imagem simples com padrão colorido
        sheet = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = sheet.createGraphics();
        
        // Preencher com padrão de cores
        for (int y = 0; y < 128; y += spriteHeight) {
            for (int x = 0; x < 128; x += spriteWidth) {
                g2d.setColor(new java.awt.Color(
                    (x + y) % 255,
                    (x * 2) % 255,
                    (y * 2) % 255
                ));
                g2d.fillRect(x, y, spriteWidth, spriteHeight);
                
                // Adicionar borda
                g2d.setColor(java.awt.Color.BLACK);
                g2d.drawRect(x, y, spriteWidth, spriteHeight);
            }
        }
        
        g2d.dispose();
        System.out.println("Sprite sheet placeholder criado para: " + name);
    }
    
    /**
     * Obtém um sprite específico por coordenadas
     */
    public BufferedImage getSprite(int x, int y) {
        if (sheet == null) return null;
        
        int pixelX = x * spriteWidth;
        int pixelY = y * spriteHeight;
        
        // Verificar limites
        if (pixelX + spriteWidth > sheet.getWidth() || pixelY + spriteHeight > sheet.getHeight()) {
            return null;
        }
        
        return sheet.getSubimage(pixelX, pixelY, spriteWidth, spriteHeight);
    }
    
    /**
     * Obtém um sprite por índice (da esquerda para direita, de cima para baixo)
     */
    public BufferedImage getSprite(int index) {
        if (sheet == null) return null;
        
        int x = index % spritesPerRow;
        int y = index / spritesPerRow;
        
        return getSprite(x, y);
    }
    
    /**
     * Obtém um sprite com cache por nome
     */
    public BufferedImage getSprite(String name) {
        if (spriteCache.containsKey(name)) {
            return spriteCache.get(name);
        }
        
        // Se não estiver no cache, tentar carregar
        BufferedImage sprite = loadSpriteByName(name);
        if (sprite != null) {
            spriteCache.put(name, sprite);
        }
        
        return sprite;
    }
    
    /**
     * Carrega um sprite por nome (implementação específica para cada sprite sheet)
     */
    private BufferedImage loadSpriteByName(String name) {
        // Implementação padrão - pode ser sobrescrita por subclasses
        // Por enquanto, retorna o primeiro sprite
        return getSprite(0);
    }
    
    /**
     * Cria uma animação a partir de uma sequência de sprites
     */
    public Animation createAnimation(String animationName, int startIndex, int endIndex, float frameRate, boolean loop) {
        Animation animation = new Animation(animationName, frameRate, loop);
        
        for (int i = startIndex; i <= endIndex; i++) {
            BufferedImage frame = getSprite(i);
            if (frame != null) {
                animation.addFrame(frame);
            }
        }
        
        return animation;
    }
    
    /**
     * Cria uma animação a partir de coordenadas específicas
     */
    public Animation createAnimation(String animationName, int[] xCoords, int[] yCoords, float frameRate, boolean loop) {
        Animation animation = new Animation(animationName, frameRate, loop);
        
        for (int i = 0; i < xCoords.length; i++) {
            BufferedImage frame = getSprite(xCoords[i], yCoords[i]);
            if (frame != null) {
                animation.addFrame(frame);
            }
        }
        
        return animation;
    }
    
    // Getters
    public BufferedImage getSheet() { return sheet; }
    public int getSpriteWidth() { return spriteWidth; }
    public int getSpriteHeight() { return spriteHeight; }
    public int getSpritesPerRow() { return spritesPerRow; }
    public int getSpritesPerColumn() { return spritesPerColumn; }
    public String getName() { return name; }
    public int getTotalSprites() { return spritesPerRow * spritesPerColumn; }
    
    /**
     * Verifica se o sprite sheet foi carregado corretamente
     */
    public boolean isLoaded() {
        return sheet != null;
    }
}