package org.example.world;

import org.example.ui.GameConfig;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gerenciador de fundo dinâmico com múltiplas camadas
 */
public class CyberpunkBackgroundManager implements IBackgroundManager {
    private int screenWidth, screenHeight;
    private List<BackgroundLayer> layers;
    private SkyGradient skyGradient;
    private CloudSystem cloudSystem;
    private RainSystem rainSystem;
    private int lastGeneratedX = 0;

    public CyberpunkBackgroundManager(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.layers = new ArrayList<>();
        this.skyGradient = new SkyGradient(width, height);
        this.cloudSystem = new CloudSystem(width, height);
        this.rainSystem = new RainSystem(width, height);

        createBackgroundLayers();
    }

    private void createBackgroundLayers() {
        layers.add(new CyberpunkSkylineLayer(screenWidth, screenHeight, 0.1f, 0.3f, new Color(20, 20, 40, 120)));
        layers.add(new CyberpunkSkylineLayer(screenWidth, screenHeight, 0.3f, 0.6f, new Color(30, 30, 60, 150)));
        layers.add(new CyberpunkSkylineLayer(screenWidth, screenHeight, 0.6f, 0.9f, new Color(40, 40, 80, 180)));
        layers.add(new CyberpunkSkylineLayer(screenWidth, screenHeight, 0.9f, 1.0f, new Color(50, 50, 100, 200)));
    }

    public void update(int playerX, int playerY, long gameTime) {
        skyGradient.update(gameTime);
        cloudSystem.update();
        rainSystem.update(gameTime);

        if (playerX > lastGeneratedX - screenWidth * 2) {
            generateMoreBuildings(lastGeneratedX, lastGeneratedX + screenWidth * 3);
            lastGeneratedX += screenWidth * 2;
        }

        for (BackgroundLayer layer : layers) {
            layer.update(playerX, playerY, gameTime);
        }
    }

    private void generateMoreBuildings(int startX, int endX) {
        for (BackgroundLayer layer : layers) {
            if (layer instanceof CyberpunkSkylineLayer) {
                ((CyberpunkSkylineLayer) layer).generateBuildingsInRange(startX, endX);
            }
        }
    }

    public void renderDistantBackground(Graphics2D g2d, int cameraX, int cameraY) {
        skyGradient.render(g2d);
        cloudSystem.render(g2d, cameraX, cameraY);
        rainSystem.render(g2d, cameraX, cameraY);
    }

    public void renderMidBackground(Graphics2D g2d, int cameraX, int cameraY) {
        for (BackgroundLayer layer : layers) {
            layer.render(g2d, cameraX, cameraY);
        }
    }

    // ==============================
    // SISTEMA DE GRADIENTE DO CÉU
    // ==============================

    private static class SkyGradient {
        private int width, height;
        private GradientPaint currentGradient;

        public SkyGradient(int width, int height) {
            this.width = width;
            this.height = height;
            updateGradient(0);
        }

        public void update(long gameTime) {
            updateGradient(gameTime);
        }

        private void updateGradient(long gameTime) {
            float cycle = (float)(Math.sin(gameTime * 0.002) * 0.5 + 0.5);
            float neonCycle = (float)(Math.sin(gameTime * 0.005) * 0.3 + 0.7);

            Color topColor = interpolateColor(
                    new Color(5, 5, 25),
                    new Color(25, 0, 50),
                    cycle
            );

            Color bottomColor = interpolateColor(
                    new Color(0, 30, 60),
                    new Color(20, 0, 40),
                    cycle
            );

            if (neonCycle > 0.8f) {
                topColor = new Color(
                        Math.min(255, topColor.getRed() + 20),
                        Math.min(255, topColor.getGreen() + 10),
                        Math.min(255, topColor.getBlue() + 30)
                );
            }

            currentGradient = new GradientPaint(
                    0, 0, topColor,
                    0, height, bottomColor
            );
        }

        public void render(Graphics2D g2d) {
            g2d.setPaint(currentGradient);
            g2d.fillRect(0, 0, width, height);
        }

        private Color interpolateColor(Color c1, Color c2, float t) {
            return new Color(
                    (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * t),
                    (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t),
                    (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t),
                    (int)(c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * t)
            );
        }
    }

    // ==============================
// SISTEMA DE CHUVA DINÂMICA - CORRIGIDO
// ==============================
    private static class RainSystem {
        private final List<RainDrop> rainDrops = new ArrayList<>();
        private final Random random = new Random();
        private final int screenWidth, screenHeight;
        private boolean isRaining = true;
        private float rainIntensity = 0.75f;

        public RainSystem(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;

            // Cria gotas iniciais
            int maxDrops = 300;
            for (int i = 0; i < maxDrops; i++) {
                rainDrops.add(new RainDrop(
                        random.nextInt(screenWidth * 3) - screenWidth,
                        random.nextInt(screenHeight),
                        2 + random.nextInt(4),
                        8 + random.nextFloat() * 12
                ));
            }
        }

        // Atualiza posições das gotas (sem desenhar nada)
        public void update(long gameTime) {
            if (!isRaining) return;

            for (RainDrop drop : rainDrops) {
                drop.update();
                if (drop.y > screenHeight) {
                    drop.y = -drop.length;
                    drop.x = random.nextInt(screenWidth * 3) - screenWidth;
                }
            }
        }

        // Apenas desenha (sem atualizar lógicas)
        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            if (!isRaining || rainIntensity <= 0) return;

            Color oldColor = g2d.getColor();
            Stroke oldStroke = g2d.getStroke();

            g2d.setStroke(new BasicStroke(1.2f));
            g2d.setColor(new Color(150, 180, 255, 120));

            for (RainDrop drop : rainDrops) {
                float renderX = drop.x - (cameraX * 0.05f);
                g2d.drawLine(
                        (int) renderX,
                        (int) drop.y,
                        (int) (renderX - drop.length * 0.5f),
                        (int) (drop.y + drop.length)
                );
            }

            g2d.setColor(oldColor);
            g2d.setStroke(oldStroke);
        }

        private static class RainDrop {
            float x, y, length, speed;

            public RainDrop(float x, float y, float length, float speed) {
                this.x = x;
                this.y = y;
                this.length = length;
                this.speed = speed;
            }

            public void update() {
                y += speed;
                x -= speed * 0.3f;
            }
        }
    }

    // ==============================
    // SISTEMA DE NUVENS DINÂMICAS
    // ==============================

    private static class CloudSystem {
        private List<Cloud> clouds;
        private Random random = new Random();
        private int screenWidth, screenHeight;

        public CloudSystem(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;
            this.clouds = new ArrayList<>();
            generateClouds();
        }

        private void generateClouds() {
            for (int i = 0; i < 20; i++) {
                clouds.add(new Cloud(
                        random.nextInt(screenWidth * 3) - screenWidth,
                        random.nextInt(screenHeight / 3) + 50,
                        40 + random.nextInt(80),
                        25 + random.nextInt(40),
                        0.1f + random.nextFloat() * 0.4f
                ));
            }
        }

        public void update() {
            for (Cloud cloud : clouds) {
                cloud.update();

                if (cloud.x > screenWidth * 2) {
                    cloud.x = -cloud.width * 2;
                    cloud.y = random.nextInt(screenHeight / 3) + 50;
                }
            }
        }

        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (Cloud cloud : clouds) {
                cloud.render(g2d, cameraX, cameraY);
            }
        }

        private static class Cloud {
            float x, y, width, height, speed;
            Color cloudColor;
            Color neonColor;
            float glowIntensity = 0;
            long animationTimer = 0;

            public Cloud(float x, float y, float width, float height, float speed) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.speed = speed;

                Random rand = new Random();
                int colorType = rand.nextInt(3);
                switch (colorType) {
                    case 0:
                        this.cloudColor = new Color(0, 100, 200, 40 + rand.nextInt(30));
                        this.neonColor = new Color(0, 150, 255, 80);
                        break;
                    case 1:
                        this.cloudColor = new Color(100, 0, 150, 40 + rand.nextInt(30));
                        this.neonColor = new Color(150, 0, 255, 80);
                        break;
                    default:
                        this.cloudColor = new Color(0, 150, 50, 40 + rand.nextInt(30));
                        this.neonColor = new Color(0, 255, 100, 80);
                        break;
                }
            }

            public void update() {
                x += speed;
                animationTimer += 16;
                glowIntensity = (float)(Math.sin(animationTimer * 0.003) * 0.3 + 0.7);
            }

            public void render(Graphics2D g2d, int cameraX, int cameraY) {
                float renderX = x - (cameraX * 0.1f);
                float renderY = y;

                if (glowIntensity > 0.8f) {
                    g2d.setColor(new Color(neonColor.getRed(), neonColor.getGreen(), neonColor.getBlue(),
                            (int)(neonColor.getAlpha() * (glowIntensity - 0.8f) * 5)));
                    g2d.fill(new Ellipse2D.Float(renderX - 5, renderY - 5, width + 10, height + 10));
                }

                g2d.setColor(cloudColor);
                g2d.fill(new Ellipse2D.Float(renderX, renderY, width, height));
                g2d.fill(new Ellipse2D.Float(renderX + width * 0.3f, renderY - height * 0.2f, width * 0.8f, height * 0.8f));
                g2d.fill(new Ellipse2D.Float(renderX + width * 0.6f, renderY, width * 0.7f, height * 0.9f));

                g2d.setColor(new Color(neonColor.getRed(), neonColor.getGreen(), neonColor.getBlue(), 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new Ellipse2D.Float(renderX, renderY, width, height));
            }
        }
    }

    // ==============================
    // SISTEMA DE SKYLINE CYBERPUNK
    // ==============================

    private static class CyberpunkSkylineLayer extends BackgroundLayer {
        private List<CyberpunkBuilding> buildings;
        private Random random = new Random();
        private Color baseColor;
        private float minHeight, maxHeight;
        protected float parallaxFactor;

        public CyberpunkSkylineLayer(int screenWidth, int screenHeight, float minHeight, float maxHeight, Color baseColor) {
            super(screenWidth, screenHeight, 0.5f);
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.baseColor = baseColor;
            this.parallaxFactor = 0.5f;
            this.buildings = new ArrayList<>();
            generateBuildings();
        }

        @Override
        public void update(int playerX, int playerY, long gameTime) {
        }

        private void generateBuildings() {
            int buildingCount = 15;
            int minSpacing = 200;
            int maxSpacing = 400;

            int currentX = -screenWidth;

            for (int i = 0; i < buildingCount; i++) {
                int width = 50 + random.nextInt(100);
                int height = (int)((minHeight + random.nextFloat() * (maxHeight - minHeight)) * screenHeight);
                int y = screenHeight - height;

                CyberpunkBuilding.BuildingType type = getRandomBuildingType();
                buildings.add(new CyberpunkBuilding(currentX, y, width, height, type, baseColor));

                currentX += width + minSpacing + random.nextInt(maxSpacing - minSpacing);
            }
        }

        public void generateBuildingsInRange(int startX, int endX) {
            int minSpacing = 200;
            int maxSpacing = 400;
            int currentX = startX;

            while (currentX < endX) {
                int width = 50 + random.nextInt(100);
                int height = (int)((minHeight + random.nextFloat() * (maxHeight - minHeight)) * screenHeight);
                int y = screenHeight - height;

                CyberpunkBuilding.BuildingType type = getRandomBuildingType();
                buildings.add(new CyberpunkBuilding(currentX, y, width, height, type, baseColor));

                currentX += width + minSpacing + random.nextInt(maxSpacing - minSpacing);
            }
        }

        private CyberpunkBuilding.BuildingType getRandomBuildingType() {
            CyberpunkBuilding.BuildingType[] types = CyberpunkBuilding.BuildingType.values();
            return types[random.nextInt(types.length)];
        }

        @Override
        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (CyberpunkBuilding building : buildings) {
                building.render(g2d, cameraX, cameraY, parallaxFactor, screenWidth);
            }
        }

        private static class CyberpunkBuilding {
            private int x, y, width, height;
            private BuildingType type;
            private Color baseColor;
            private List<Window> windows;
            private List<NeonSign> neonSigns;
            private long animationOffset;
            private Random rand = new Random();

            public enum BuildingType {
                TOWER, BLOCK, PYRAMID, SPHERE, BRIDGE, MEGA_TOWER, HANGING, GLASS
            }

            public CyberpunkBuilding(int x, int y, int width, int height, BuildingType type, Color baseColor) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.type = type;
                this.baseColor = baseColor;
                this.windows = new ArrayList<>();
                this.neonSigns = new ArrayList<>();
                this.animationOffset = (long)(Math.random() * 1000);
                generateWindows();
                generateNeonSigns();
            }

            private void generateWindows() {
                int rows = height / 25;
                int cols = Math.max(2, width / 20);

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (rand.nextFloat() < 0.7f) {
                            int wx = x + 5 + col * (width / cols);
                            int wy = y + 5 + row * 25;
                            windows.add(new Window(wx, wy, 8, 12));
                        }
                    }
                }
            }

            private void generateNeonSigns() {
                int numSigns = rand.nextInt(3) + 1;
                for (int i = 0; i < numSigns; i++) {
                    int sx = x + rand.nextInt(Math.max(1, width - 30));
                    int sy = y + rand.nextInt(Math.max(1, height / 2));
                    Color[] neonColors = {
                            new Color(0, 255, 255),
                            new Color(255, 0, 255),
                            new Color(255, 0, 100),
                            new Color(0, 255, 150)
                    };
                    neonSigns.add(new NeonSign(sx, sy, 25, 8, neonColors[rand.nextInt(neonColors.length)]));
                }
            }

            public void render(Graphics2D g2d, int cameraX, int cameraY, float parallaxFactor, int screenWidth) {
                float renderX = x - (cameraX * parallaxFactor);
                float renderY = y;

                if (renderX + width < -200 || renderX > screenWidth + 200) return;

                long gameTime = System.currentTimeMillis() + animationOffset;

                Color originalColor = g2d.getColor();
                Composite originalComposite = g2d.getComposite();
                Stroke originalStroke = g2d.getStroke();

                renderBuildingShape(g2d, renderX, renderY, gameTime);

                for (Window window : windows) {
                    window.update(gameTime);
                    window.render(g2d, renderX - x, renderY - y, gameTime);
                }

                for (NeonSign sign : neonSigns) {
                    sign.render(g2d, renderX - x, renderY - y, gameTime);
                }

                float topGlow = (float)(Math.sin(gameTime * 0.002) * 0.3 + 0.7);
                g2d.setColor(new Color(0, 255, 255, (int)(topGlow * 80)));
                g2d.fillRect((int)renderX, (int)renderY, width, 3);

                g2d.setColor(new Color(255, 0, 255, (int)(topGlow * 100)));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine((int)renderX, (int)renderY, (int)renderX, (int)renderY + height);
                g2d.drawLine((int)renderX + width, (int)renderY, (int)renderX + width, (int)renderY + height);

                g2d.setColor(originalColor);
                g2d.setComposite(originalComposite);
                g2d.setStroke(originalStroke);
            }

            private void renderBuildingShape(Graphics2D g2d, float renderX, float renderY, long gameTime) {
                float pulse = (float)(Math.sin(gameTime * 0.001 + animationOffset) * 0.1 + 0.9);
                Color buildingColor = new Color(
                        (int)(baseColor.getRed() * pulse),
                        (int)(baseColor.getGreen() * pulse),
                        (int)(baseColor.getBlue() * pulse),
                        baseColor.getAlpha()
                );

                g2d.setColor(buildingColor);
                g2d.fillRect((int)renderX, (int)renderY, width, height);

                if (type == BuildingType.TOWER) {
                    float antennaPulse = (float)(Math.sin(gameTime * 0.005) * 0.5 + 0.5);
                    g2d.setColor(new Color(0, 255, 255, (int)(200 * antennaPulse)));
                    g2d.fillRect((int)renderX + width/2 - 3, (int)renderY - 25, 6, 25);

                    g2d.setColor(new Color(255, 0, 255, (int)(255 * antennaPulse)));
                    g2d.fillOval((int)renderX + width/2 - 5, (int)renderY - 30, 10, 10);
                }
            }

            private static class Window {
                int x, y, width, height;
                float brightness;
                long flickerTimer;

                Window(int x, int y, int width, int height) {
                    this.x = x;
                    this.y = y;
                    this.width = width;
                    this.height = height;
                    this.brightness = 0.5f + (float)Math.random() * 0.5f;
                    this.flickerTimer = (long)(Math.random() * 1000);
                }

                void update(long gameTime) {
                    flickerTimer = gameTime;
                    if (Math.random() < 0.01) {
                        brightness = 0.3f + (float)Math.random() * 0.7f;
                    }
                }

                void render(Graphics2D g2d, float offsetX, float offsetY, long gameTime) {
                    float flicker = (float)(Math.sin((gameTime + flickerTimer) * 0.01) * 0.1 + 0.9);
                    int alpha = (int)(brightness * flicker * 255);

                    Color windowColor = new Color(100, 200, 255, Math.min(255, alpha));
                    g2d.setColor(windowColor);
                    g2d.fillRect((int)(offsetX + x), (int)(offsetY + y), width, height);

                    Color glowColor = new Color(0, 150, 255, Math.min(100, alpha / 2));
                    g2d.setColor(glowColor);
                    g2d.fillRect((int)(offsetX + x - 1), (int)(offsetY + y - 1), width + 2, height + 2);
                }
            }

            private static class NeonSign {
                int x, y, width, height;
                Color color;
                float pulsePhase;

                NeonSign(int x, int y, int width, int height, Color color) {
                    this.x = x;
                    this.y = y;
                    this.width = width;
                    this.height = height;
                    this.color = color;
                    this.pulsePhase = (float)(Math.random() * Math.PI * 2);
                }

                void render(Graphics2D g2d, float offsetX, float offsetY, long gameTime) {
                    float pulse = (float)(Math.sin(gameTime * 0.003 + pulsePhase) * 0.3 + 0.7);
                    int alpha = (int)(pulse * 200);

                    Color signColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    g2d.setColor(signColor);
                    g2d.fillRect((int)(offsetX + x), (int)(offsetY + y), width, height);

                    if (pulse > 0.8f) {
                        Color glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((pulse - 0.8f) * 500));
                        g2d.fillRect((int)(offsetX + x - 2), (int)(offsetY + y - 2), width + 4, height + 4);
                    }
                }
            }
        }
    }
}