package org.example.world;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gerenciador de fundo temático de Halloween com decorações assustadoras
 */
public class HalloweenBackgroundManager implements IBackgroundManager {
    private int screenWidth, screenHeight;
    private List<BackgroundLayer> layers;
    private HalloweenSkyGradient skyGradient;
    private GhostSystem ghostSystem;
    private BatSystem batSystem;
    private FogSystem fogSystem;
    private int lastGeneratedX = 0;

    public HalloweenBackgroundManager(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.layers = new ArrayList<>();
        this.skyGradient = new HalloweenSkyGradient(width, height);
        this.ghostSystem = new GhostSystem(width, height);
        this.batSystem = new BatSystem(width, height);
        this.fogSystem = new FogSystem(width, height);

        createHalloweenLayers();
    }

    private void createHalloweenLayers() {
        // Camadas de prédios com tema Halloween
        layers.add(new HalloweenSkylineLayer(screenWidth, screenHeight, 0.1f, 0.3f, new Color(30, 10, 40, 120)));
        layers.add(new HalloweenSkylineLayer(screenWidth, screenHeight, 0.3f, 0.6f, new Color(40, 15, 50, 150)));
        layers.add(new HalloweenSkylineLayer(screenWidth, screenHeight, 0.6f, 0.9f, new Color(50, 20, 60, 180)));
    }

    public void update(int playerX, int playerY, long gameTime) {
        skyGradient.update(gameTime);
        ghostSystem.update(gameTime);
        batSystem.update(gameTime);
        fogSystem.update(gameTime);

        if (playerX > lastGeneratedX - screenWidth * 2) {
            generateMoreHalloweenDecor(lastGeneratedX, lastGeneratedX + screenWidth * 3);
            lastGeneratedX += screenWidth * 2;
        }

        for (BackgroundLayer layer : layers) {
            layer.update(playerX, playerY, gameTime);
        }
    }

    private void generateMoreHalloweenDecor(int startX, int endX) {
        ghostSystem.generateGhostsInRange(startX, endX);

        // Gera mais prédios para todas as camadas
        for (BackgroundLayer layer : layers) {
            if (layer instanceof HalloweenSkylineLayer) {
                ((HalloweenSkylineLayer) layer).generateBuildingsInRange(startX, endX);
            }
        }
    }

    public void renderDistantBackground(Graphics2D g2d, int cameraX, int cameraY) {
        skyGradient.render(g2d);
        batSystem.render(g2d, cameraX, cameraY);
        fogSystem.render(g2d, cameraX, cameraY);
    }

    public void renderMidBackground(Graphics2D g2d, int cameraX, int cameraY) {
        ghostSystem.render(g2d, cameraX, cameraY);

        for (BackgroundLayer layer : layers) {
            layer.render(g2d, cameraX, cameraY);
        }
    }

    // ==============================
    // CÉU DE HALLOWEEN
    // ==============================
    private static class HalloweenSkyGradient {
        private int width, height;
        private GradientPaint currentGradient;
        private float moonPhase = 0;

        public HalloweenSkyGradient(int width, int height) {
            this.width = width;
            this.height = height;
            updateGradient(0);
        }

        public void update(long gameTime) {
            updateGradient(gameTime);
            moonPhase = (float)(Math.sin(gameTime * 0.001) * 0.5 + 0.5);
        }

        private void updateGradient(long gameTime) {
            // Céu roxo/laranja assustador de Halloween
            Color topColor = new Color(25, 5, 35); // Roxo escuro
            Color midColor = new Color(60, 20, 40); // Roxo médio
            Color bottomColor = new Color(120, 40, 20); // Laranja escuro

            currentGradient = new GradientPaint(
                    0, 0, topColor,
                    0, height, bottomColor
            );
        }

        public void render(Graphics2D g2d) {
            g2d.setPaint(currentGradient);
            g2d.fillRect(0, 0, width, height);

            // Lua cheia assustadora
            int moonX = width / 4;
            int moonY = 80;
            int moonSize = 60;

            // Brilho da lua
            g2d.setColor(new Color(255, 200, 100, 30));
            g2d.fillOval(moonX - moonSize, moonY - moonSize, moonSize * 3, moonSize * 3);

            // Lua
            g2d.setColor(new Color(255, 220, 150, (int)(200 * moonPhase + 55)));
            g2d.fillOval(moonX, moonY, moonSize, moonSize);

            // Crateras
            g2d.setColor(new Color(200, 180, 120, 100));
            g2d.fillOval(moonX + 10, moonY + 15, 15, 15);
            g2d.fillOval(moonX + 35, moonY + 25, 10, 10);
            g2d.fillOval(moonX + 20, moonY + 35, 12, 12);

            // Nuvens sinistras
            drawSpookyCloud(g2d, width / 2, 120);
            drawSpookyCloud(g2d, width / 3, 160);
        }

        private void drawSpookyCloud(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(40, 20, 50, 80));
            g2d.fillOval(x, y, 80, 30);
            g2d.fillOval(x + 20, y - 10, 60, 35);
            g2d.fillOval(x + 50, y, 70, 28);
        }
    }

    // ==============================
    // SISTEMA DE FANTASMAS
    // ==============================
    private static class GhostSystem {
        private List<Ghost> ghosts;
        private Random random = new Random();
        private int screenWidth, screenHeight;

        public GhostSystem(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;
            this.ghosts = new ArrayList<>();
            generateInitialGhosts();
        }

        private void generateInitialGhosts() {
            for (int i = 0; i < 15; i++) {
                ghosts.add(new Ghost(
                        random.nextInt(screenWidth * 3) - screenWidth,
                        random.nextInt(screenHeight / 2) + 100,
                        random
                ));
            }
        }

        public void generateGhostsInRange(int startX, int endX) {
            int currentX = startX;
            while (currentX < endX) {
                ghosts.add(new Ghost(currentX, random.nextInt(screenHeight / 2) + 100, random));
                currentX += 300 + random.nextInt(400);
            }
        }

        public void update(long gameTime) {
            for (Ghost ghost : ghosts) {
                ghost.update(gameTime);
            }
        }

        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (Ghost ghost : ghosts) {
                ghost.render(g2d, cameraX);
            }
        }

        private static class Ghost {
            float x, y;
            float floatOffset = 0;
            long animationTimer = 0;
            float transparency = 0;
            float speed;

            public Ghost(int x, int y, Random rand) {
                this.x = x;
                this.y = y;
                this.speed = 0.3f + rand.nextFloat() * 0.5f;
                this.animationTimer = rand.nextInt(1000);
            }

            public void update(long gameTime) {
                animationTimer = gameTime;
                floatOffset = (float)(Math.sin(animationTimer * 0.002) * 15);
                transparency = (float)(Math.sin(animationTimer * 0.001) * 0.3 + 0.5);
                x += speed;
            }

            public void render(Graphics2D g2d, int cameraX) {
                float renderX = x - (cameraX * 0.4f);
                float renderY = y + floatOffset;

                // Corpo do fantasma
                g2d.setColor(new Color(255, 255, 255, (int)(120 * transparency)));
                g2d.fillOval((int)renderX, (int)renderY, 40, 50);

                // Cauda ondulada
                for (int i = 0; i < 3; i++) {
                    int tailX = (int)renderX + 8 + i * 8;
                    int tailY = (int)renderY + 50;
                    int waveOffset = (int)(Math.sin(animationTimer * 0.005 + i) * 8);
                    g2d.fillOval(tailX, tailY + waveOffset, 12, 15);
                }

                // Olhos
                g2d.setColor(new Color(0, 0, 0, (int)(200 * transparency)));
                g2d.fillOval((int)renderX + 10, (int)renderY + 15, 8, 12);
                g2d.fillOval((int)renderX + 24, (int)renderY + 15, 8, 12);

                // Boca
                g2d.fillOval((int)renderX + 15, (int)renderY + 32, 10, 8);
            }
        }
    }

    // ==============================
    // SISTEMA DE MORCEGOS
    // ==============================
    private static class BatSystem {
        private List<Bat> bats;
        private Random random = new Random();
        private int screenWidth, screenHeight;

        public BatSystem(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;
            this.bats = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                bats.add(new Bat(
                        random.nextInt(screenWidth * 2),
                        random.nextInt(screenHeight / 3) + 50,
                        random
                ));
            }
        }

        public void update(long gameTime) {
            for (Bat bat : bats) {
                bat.update(gameTime);
                if (bat.x > screenWidth * 2) {
                    bat.x = -50;
                }
            }
        }

        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (Bat bat : bats) {
                bat.render(g2d, cameraX);
            }
        }

        private static class Bat {
            float x, y;
            float speed;
            float wingFlap = 0;
            long animationTimer = 0;

            public Bat(int x, int y, Random rand) {
                this.x = x;
                this.y = y;
                this.speed = 1f + rand.nextFloat() * 2f;
            }

            public void update(long gameTime) {
                x += speed;
                animationTimer = gameTime;
                wingFlap = (float)(Math.sin(animationTimer * 0.02) * 8);
            }

            public void render(Graphics2D g2d, int cameraX) {
                float renderX = x - (cameraX * 0.2f);

                g2d.setColor(new Color(20, 20, 20, 200));

                // Corpo
                g2d.fillOval((int)renderX, (int)y, 8, 10);

                // Asas
                int[] leftWingX = {(int)renderX, (int)renderX - 12, (int)renderX};
                int[] leftWingY = {(int)y + 5, (int)y + (int)wingFlap, (int)y + 8};
                g2d.fillPolygon(leftWingX, leftWingY, 3);

                int[] rightWingX = {(int)renderX + 8, (int)renderX + 20, (int)renderX + 8};
                int[] rightWingY = {(int)y + 5, (int)y + (int)wingFlap, (int)y + 8};
                g2d.fillPolygon(rightWingX, rightWingY, 3);
            }
        }
    }

    // ==============================
    // SISTEMA DE NÉVOA
    // ==============================
    private static class FogSystem {
        private List<FogCloud> fogClouds;
        private Random random = new Random();
        private int screenWidth, screenHeight;

        public FogSystem(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;
            this.fogClouds = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                fogClouds.add(new FogCloud(
                        random.nextInt(screenWidth * 2),
                        screenHeight - 150 - random.nextInt(50),
                        random
                ));
            }
        }

        public void update(long gameTime) {
            for (FogCloud fog : fogClouds) {
                fog.update();
                if (fog.x > screenWidth * 2) {
                    fog.x = -200;
                }
            }
        }

        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (FogCloud fog : fogClouds) {
                fog.render(g2d, cameraX);
            }
        }

        private static class FogCloud {
            float x, y;
            float width, height;
            float speed;
            int alpha;

            public FogCloud(int x, int y, Random rand) {
                this.x = x;
                this.y = y;
                this.width = 200 + rand.nextInt(150);
                this.height = 40 + rand.nextInt(30);
                this.speed = 0.2f + rand.nextFloat() * 0.3f;
                this.alpha = 20 + rand.nextInt(30);
            }

            public void update() {
                x += speed;
            }

            public void render(Graphics2D g2d, int cameraX) {
                float renderX = x - (cameraX * 0.8f);

                g2d.setColor(new Color(80, 60, 90, alpha));
                g2d.fillOval((int)renderX, (int)y, (int)width, (int)height);
                g2d.fillOval((int)renderX + (int)width / 3, (int)y - 10, (int)width / 2, (int)height);
            }
        }
    }

    // ==============================
    // PRÉDIOS COM TEMA HALLOWEEN
    // ==============================
    private static class HalloweenSkylineLayer extends BackgroundLayer {
        private List<HalloweenBuilding> buildings;
        private Random random = new Random();
        private Color baseColor;
        private float minHeight, maxHeight;
        protected float parallaxFactor;

        public HalloweenSkylineLayer(int screenWidth, int screenHeight, float minHeight, float maxHeight, Color baseColor) {
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
            int currentX = -screenWidth;
            for (int i = 0; i < 15; i++) {
                int width = 50 + random.nextInt(100);
                int height = (int)((minHeight + random.nextFloat() * (maxHeight - minHeight)) * screenHeight);
                int y = screenHeight - height;

                buildings.add(new HalloweenBuilding(currentX, y, width, height, baseColor, random));
                currentX += width + 200 + random.nextInt(200);
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

                buildings.add(new HalloweenBuilding(currentX, y, width, height, baseColor, random));
                currentX += width + minSpacing + random.nextInt(maxSpacing - minSpacing);
            }
        }

        @Override
        public void render(Graphics2D g2d, int cameraX, int cameraY) {
            for (HalloweenBuilding building : buildings) {
                building.render(g2d, cameraX, parallaxFactor, screenWidth);
            }
        }

        private static class HalloweenBuilding {
            int x, y, width, height;
            Color baseColor;
            List<HalloweenWindow> windows;
            long animationOffset;
            Random rand;

            public HalloweenBuilding(int x, int y, int width, int height, Color baseColor, Random rand) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.baseColor = baseColor;
                this.rand = rand;
                this.windows = new ArrayList<>();
                this.animationOffset = (long)(Math.random() * 1000);
                generateWindows();
            }

            private void generateWindows() {
                int rows = height / 25;
                int cols = Math.max(2, width / 20);

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (rand.nextFloat() < 0.6f) {
                            int wx = x + 5 + col * (width / cols);
                            int wy = y + 5 + row * 25;
                            windows.add(new HalloweenWindow(wx, wy, rand));
                        }
                    }
                }
            }

            public void render(Graphics2D g2d, int cameraX, float parallaxFactor, int screenWidth) {
                float renderX = x - (cameraX * parallaxFactor);
                if (renderX + width < -200 || renderX > screenWidth + 200) return;

                long gameTime = System.currentTimeMillis() + animationOffset;

                // Prédio
                g2d.setColor(baseColor);
                g2d.fillRect((int)renderX, y, width, height);

                // Janelas
                for (HalloweenWindow window : windows) {
                    window.update(gameTime);
                    window.render(g2d, renderX - x, y);
                }

                // Decorações de Halloween no topo
                float pulse = (float)(Math.sin(gameTime * 0.003) * 0.5 + 0.5);
                g2d.setColor(new Color(255, 100, 0, (int)(150 * pulse)));
                g2d.fillRect((int)renderX, y, width, 3);
            }

            private static class HalloweenWindow {
                int x, y;
                float brightness;
                Color windowColor;

                HalloweenWindow(int x, int y, Random rand) {
                    this.x = x;
                    this.y = y;

                    // Janelas com cores de Halloween
                    int colorType = rand.nextInt(3);
                    switch (colorType) {
                        case 0:
                            windowColor = new Color(255, 150, 0); // Laranja
                            break;
                        case 1:
                            windowColor = new Color(150, 0, 255); // Roxo
                            break;
                        default:
                            windowColor = new Color(0, 255, 100); // Verde
                            break;
                    }
                    this.brightness = 0.5f + rand.nextFloat() * 0.5f;
                }

                void update(long gameTime) {
                    if (Math.random() < 0.02) {
                        brightness = 0.3f + (float)Math.random() * 0.7f;
                    }
                }

                void render(Graphics2D g2d, float offsetX, int buildingY) {
                    int alpha = (int)(brightness * 255);
                    Color color = new Color(windowColor.getRed(), windowColor.getGreen(), windowColor.getBlue(), alpha);

                    g2d.setColor(color);
                    g2d.fillRect((int)(offsetX + x), y, 8, 12);

                    // Brilho
                    g2d.setColor(new Color(windowColor.getRed(), windowColor.getGreen(), windowColor.getBlue(), alpha / 2));
                    g2d.fillRect((int)(offsetX + x - 1), y - 1, 10, 14);
                }
            }
        }
    }
}