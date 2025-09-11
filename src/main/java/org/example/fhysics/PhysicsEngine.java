package org.example.fhysics;

import java.util.ArrayList;

import org.example.ui.GameConfig;
import org.example.world.Platform;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.objects.Player;

public class PhysicsEngine {

    public static void applyGravityToPlayer(Player player) {
        player.velocityY += GameConfig.GRAVITY;
        if (player.velocityY > GameConfig.TERMINAL_VELOCITY) {
            player.velocityY = GameConfig.TERMINAL_VELOCITY;
        }
    }

    public static void applyGravityToEnemy(Enemy enemy) {
        enemy.velocityY += GameConfig.GRAVITY;
        if (enemy.velocityY > GameConfig.TERMINAL_VELOCITY) {
            enemy.velocityY = GameConfig.TERMINAL_VELOCITY;
        }
        enemy.y += enemy.velocityY;
    }

    public static void checkPlayerPlatformCollisions(Player player, ArrayList<Platform> platforms) {
        player.isOnGround = false;

        // Salvar posição atual antes de aplicar movimento
        int oldX = player.x;
        int oldY = player.y;

        // Aplicar movimento horizontal primeiro
        player.x += (int)player.velocityX;
        player.hitbox.setLocation(player.x, player.y);

        // Verificar colisões horizontais
        for (Platform platform : platforms) {
            if (player.getHitbox().intersects(platform.getHitbox())) {
                // Colisão horizontal - reverter movimento X
                player.x = oldX;
                player.velocityX = 0;
                player.hitbox.setLocation(player.x, player.y);
                break;
            }
        }

        // Aplicar movimento vertical
        player.y += (int)player.velocityY;
        player.hitbox.setLocation(player.x, player.y);

        // Verificar colisões verticais
        for (Platform platform : platforms) {
            if (player.getHitbox().intersects(platform.getHitbox())) {
                resolveVerticalCollision(player, platform, oldY);
            }
        }
    }

    private static void resolveVerticalCollision(Player player, Platform platform, int previousY) {
        // Colisão vindo de cima (aterrissando na plataforma)
        if (player.velocityY > 0 && previousY + player.height <= platform.y + 5) {
            player.y = platform.y - player.height;
            player.velocityY = 0;
            player.isOnGround = true;
            System.out.println("Player aterrissou na plataforma em Y=" + player.y);
        }
        // Colisão vindo de baixo (batendo a cabeça)
        else if (player.velocityY < 0 && previousY >= platform.y + platform.height - 5) {
            player.y = platform.y + platform.height;
            player.velocityY = 0;
            System.out.println("Player bateu a cabeça na plataforma");
        }
        // Se não conseguiu resolver, colocar em posição segura
        else {
            if (player.velocityY > 0) {
                player.y = platform.y - player.height;
                player.velocityY = 0;
                player.isOnGround = true;
            } else {
                player.y = platform.y + platform.height;
                player.velocityY = 0;
            }
        }

        // Atualizar hitbox após correção
        player.hitbox.setLocation(player.x, player.y);
    }

    public static void checkEnemyPlatformCollisions(Enemy enemy, ArrayList<Platform> platforms) {
        enemy.isOnGround = false;

        for (Platform platform : platforms) {
            if (enemy.getHitbox().intersects(platform.getHitbox())) {
                resolveEnemyPlatformCollision(enemy, platform);
            }
        }
    }

    // Impedir que inimigos caiam das plataformas: detectar borda e inverter direção
    public static void preventEnemyFallFromPlatforms(Enemy enemy, ArrayList<Platform> platforms) {
        if (!enemy.isOnGround) return;

        // Sensor à frente do inimigo (no pé líder)
        int sensorX = (int)(enemy.x + (enemy.direction == 1 ? enemy.width + 1 : -1));
        int sensorY = (int)(enemy.y + enemy.height + 1);

        boolean groundAhead = false;
        for (Platform platform : platforms) {
            // Verificar se o ponto do sensor está acima da plataforma
            if (sensorX >= platform.x && sensorX <= platform.x + platform.width &&
                    sensorY >= platform.y && sensorY <= platform.y + platform.height) {
                groundAhead = true;
                break;
            }
        }

        if (!groundAhead) {
            // Inverter direção e recuar um pouco
            enemy.direction *= -1;
            enemy.x += enemy.direction * 2;
            updateEnemyHitbox(enemy);
        }
    }

    private static void resolveEnemyPlatformCollision(Enemy enemy, Platform platform) {
        // Colisão por cima
        if (enemy.velocityY > 0 && enemy.y <= platform.y) {
            enemy.y = platform.y - enemy.height;
            enemy.velocityY = 0;
            enemy.isOnGround = true;
        }
        // Colisão por baixo
        else if (enemy.velocityY < 0) {
            enemy.y = platform.y + platform.height;
            enemy.velocityY = 0;
        }
        // Colisões laterais - inverter direção
        else if (enemy.velocityX > 0) {
            enemy.x = platform.x - enemy.width;
            enemy.direction = -1;
        } else if (enemy.velocityX < 0) {
            enemy.x = platform.x + platform.width;
            enemy.direction = 1;
        }

        // Atualizar hitbox
        updateEnemyHitbox(enemy);
    }

    public static boolean checkPlayerEnemyCollision(Player player, Enemy enemy) {
        if (player.getHitbox().intersects(enemy.getHitbox())) {
            // Player pula em cima do inimigo (elimina o inimigo)
            // Verificar se player está caindo (velocityY > 0) e se está acima do inimigo
            if (player.velocityY > 0 && player.y + player.height - 15 < enemy.y) {
                player.velocityY = -12; // Bounce do player
                return true; // Inimigo derrotado
            }
            // Se não é um pulo em cima, não faz nada aqui - deixa o GamePanel lidar com dano lateral
        }
        return false; // Sem colisão ou não é pulo em cima
    }

    public static boolean checkPlayerOrbCollision(Player player, EnergyOrb orb) {
        return !orb.isCollected() && player.getHitbox().intersects(orb.getHitbox());
    }

    public static boolean isOutOfWorldBounds(Player player) {
        return player.y > GameConfig.WORLD_HEIGHT + 100;
    }

    private static void updateEnemyHitbox(Enemy enemy) {
        try {
            if (enemy.getHitbox() != null) {
                enemy.getHitbox().setLocation((int)enemy.x, (int)enemy.y);
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar hitbox do enemy: " + e.getMessage());
        }
    }
}