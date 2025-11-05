package org.example.ui;

// Sistema de sprites removido
import java.awt.*;
import java.util.ArrayList;

import org.example.inputs.CameraController;
import org.example.world.Platform;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.objects.Player;

public class GameRenderer {

    public static void renderPlatforms(Graphics2D g2d, ArrayList<Platform> platforms, CameraController camera) {
        for (Platform platform : platforms) {
            // Frustum culling - só desenhar se visível
            if (platform != null && camera.isPlatformVisible(platform)) {
                
                if (GameConfig.ANIMATIONS_ENABLED) {
                    // Renderizar com sprite
                    renderPlatformSprite(g2d, platform);
                } else {
                    // Renderização legada com formas geométricas
                    renderPlatformLegacy(g2d, platform);
                }
            }
        }
    }
    
    private static void renderPlatformSprite(Graphics2D g2d, Platform platform) {
        // Determinar tipo de plataforma e animação
        // Renderização geométrica (sprites removidos)
        renderPlatformLegacy(g2d, platform);
    }
    
    private static void renderPlatformLegacy(Graphics2D g2d, Platform platform) {
        g2d.setColor(new Color(70, 70, 90));

        // Corpo da plataforma
        g2d.fillRect(platform.x, platform.y, platform.width, platform.height);

        // Borda neon
        g2d.setColor(new Color(0, 200, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(platform.x, platform.y, platform.width, platform.height);

        // Detalhes luminosos
        g2d.setColor(new Color(100, 255, 200, 100));
        for (int i = 0; i < platform.width; i += 20) {
            g2d.fillRect(platform.x + i + 2, platform.y + 2, 2, platform.height - 4);
        }

        g2d.setColor(new Color(70, 70, 90)); // Reset cor para próxima plataforma
    }

    public static void renderEnemies(Graphics2D g2d, ArrayList<Enemy> enemies, CameraController camera) {
        for (Enemy enemy : enemies) {
            if (enemy != null && camera.isEnemyVisible(enemy)) {
                
                if (GameConfig.ANIMATIONS_ENABLED) {
                    // Renderizar com sprite
                    renderEnemySprite(g2d, enemy);
                } else {
                    // Renderização legada
                    try {
                        enemy.draw(g2d);
                    } catch (Exception e) {
                        // Fallback: desenhar retângulo simples para inimigo
                        renderEnemyFallback(g2d, enemy);
                    }
                }
            }
        }
    }
    
    private static void renderEnemySprite(Graphics2D g2d, Enemy enemy) {
        // Renderização geométrica (sprites removidos)
        renderEnemyFallback(g2d, enemy);
    }

    private static void renderEnemyFallback(Graphics2D g2d, Enemy enemy) {
        // Desenho básico do inimigo (cast para int apenas na renderização)
        int drawX = (int) enemy.x;
        int drawY = (int) enemy.y;

        g2d.setColor(new Color(255, 50, 50));
        g2d.fillRect(drawX, drawY, enemy.width, enemy.height);

        // Borda
        g2d.setColor(new Color(255, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(drawX, drawY, enemy.width, enemy.height);

        // Olhos simples
        g2d.setColor(Color.WHITE);
        g2d.fillOval(drawX + 5, drawY + 5, 8, 8);
        g2d.fillOval(drawX + enemy.width - 13, drawY + 5, 8, 8);

        g2d.setColor(Color.RED);
        g2d.fillOval(drawX + 7, drawY + 7, 4, 4);
        g2d.fillOval(drawX + enemy.width - 11, drawY + 7, 4, 4);
    }

    public static void renderEnergyOrbs(Graphics2D g2d, ArrayList<EnergyOrb> orbs, CameraController camera) {
        for (EnergyOrb orb : orbs) {
            if (orb != null && camera.isOrbVisible(orb) && !orb.isCollected()) {
                
                if (GameConfig.ANIMATIONS_ENABLED) {
                    // Renderizar com sprite
                    renderOrbSprite(g2d, orb);
                } else {
                    // Renderização legada
                    try {
                        orb.draw(g2d);
                    } catch (Exception e) {
                        // Fallback: desenhar círculo simples para orb
                        renderOrbFallback(g2d, orb);
                    }
                }
            }
        }
    }
    
    private static void renderOrbSprite(Graphics2D g2d, EnergyOrb orb) {
        // Renderização geométrica (sprites removidos)
        renderOrbFallback(g2d, orb);
    }

    private static void renderOrbFallback(Graphics2D g2d, EnergyOrb orb) {
        // Determinar cor baseada na posição (orbs mais longe = mais valiosos)
        Color orbColor;
        if (orb.x > 4000) {
            orbColor = new Color(255, 215, 0); // Dourado - lendário
        } else if (orb.x > 3000) {
            orbColor = new Color(255, 0, 255); // Magenta - raro
        } else if (orb.x > 2000) {
            orbColor = new Color(0, 255, 0);   // Verde - uncommon
        } else {
            orbColor = new Color(0, 255, 255); // Ciano - comum
        }

        // Núcleo do orb
        g2d.setColor(orbColor);
        g2d.fillOval(orb.x - 8, orb.y - 8, 16, 16);

        // Brilho exterior
        g2d.setColor(new Color(orbColor.getRed(), orbColor.getGreen(), orbColor.getBlue(), 100));
        g2d.fillOval(orb.x - 12, orb.y - 12, 24, 24);

        // Borda brilhante
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(orb.x - 8, orb.y - 8, 16, 16);
    }

    public static void renderPlayer(Graphics2D g2d, Player player) {
        if (player != null) {
            if (GameConfig.ANIMATIONS_ENABLED) {
                // Renderizar com sprite
                renderPlayerSprite(g2d, player);
            } else {
                // Renderização legada
                try {
                    player.draw(g2d);
                } catch (Exception e) {
                    // Fallback: desenhar retângulo simples para player
                    renderPlayerFallback(g2d, player);
                }
            }
        }
    }
    
    private static void renderPlayerSprite(Graphics2D g2d, Player player) {
        // Renderização geométrica (sprites removidos)
        renderPlayerFallback(g2d, player);
    }

    private static void renderPlayerFallback(Graphics2D g2d, Player player) {
        // Corpo principal
        g2d.setColor(GameConfig.PRIMARY_COLOR);
        g2d.fillRect(player.x, player.y, player.width, player.height);

        // Detalhes
        g2d.setColor(GameConfig.SECONDARY_COLOR);
        g2d.fillRect(player.x + 5, player.y + 5, player.width - 10, 10);

        // Borda
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(player.x, player.y, player.width, player.height);

        // Indicador de direção
        g2d.setColor(GameConfig.ACCENT_COLOR);
        if (player.facingRight) {
            g2d.fillPolygon(
                    new int[]{player.x + player.width - 5, player.x + player.width, player.x + player.width - 5},
                    new int[]{player.y + 10, player.y + player.height/2, player.y + player.height - 10},
                    3
            );
        } else {
            g2d.fillPolygon(
                    new int[]{player.x + 5, player.x, player.x + 5},
                    new int[]{player.y + 10, player.y + player.height/2, player.y + player.height - 10},
                    3
            );
        }
    }

    // Método para renderizar tudo de uma vez (para facilitar)
    public static void renderAllGameObjects(Graphics2D g2d,
                                            ArrayList<Platform> platforms,
                                            ArrayList<Enemy> enemies,
                                            ArrayList<EnergyOrb> orbs,
                                            Player player,
                                            CameraController camera) {

        // Aplicar transformação da câmera
        g2d.translate(-camera.getCameraX(), -camera.getCameraY());

        // Renderizar em ordem de profundidade (fundo para frente)
        // Forçar renderização legada
        for (Platform platform : platforms) {
            if (platform != null && camera.isPlatformVisible(platform)) {
                renderPlatformLegacy(g2d, platform);
            }
        }

        for (EnergyOrb orb : orbs) {
            if (orb != null && camera.isOrbVisible(orb) && !orb.isCollected()) {
                renderOrbFallback(g2d, orb);
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy != null && camera.isEnemyVisible(enemy)) {
                try {
                    enemy.draw(g2d);
                } catch (Exception e) {
                    renderEnemyFallback(g2d, enemy);
                }
            }
        }

        // Renderizar o player usando o desenho próprio (detalhado) sem sprites
        try {
            player.draw(g2d);
        } catch (Exception e) {
            renderPlayerFallback(g2d, player);
        }

        // Reverter transformação da câmera
        g2d.translate(camera.getCameraX(), camera.getCameraY());
    }
}