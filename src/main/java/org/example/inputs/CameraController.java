package org.example.inputs;

import org.example.ui.GameConfig;
import org.example.world.Platform;
import org.example.objects.Enemy;
import org.example.objects.EnergyOrb;
import org.example.objects.Player;

public class CameraController {
    private int cameraX = 0;
    private int cameraY = 0;

    // Configurações da câmera
    private static final int CAMERA_SMOOTHNESS = 8; // Quanto maior, mais suave
    private static final int CAMERA_DEADZONE = 50;  // Zona morta antes de mover câmera

    public void updateCamera(Player player) {
        // Câmera segue o player horizontalmente
        int targetCameraX = player.x - GameConfig.SCREEN_WIDTH / 2;

        // Limitar câmera aos bounds do mundo
        if (targetCameraX < 0) {
            targetCameraX = 0;
        } else if (targetCameraX > GameConfig.WORLD_WIDTH - GameConfig.SCREEN_WIDTH) {
            targetCameraX = GameConfig.WORLD_WIDTH - GameConfig.SCREEN_WIDTH;
        }

        // Aplicar suavização da câmera (opcional)
        cameraX += (targetCameraX - cameraX); // Movimento direto
        // Para movimento suave, use:
        // cameraX += (targetCameraX - cameraX) / CAMERA_SMOOTHNESS;

        // Câmera fixa na vertical por enquanto
        cameraY = 0;
    }

    public void updateCameraSmooth(Player player) {
        // Versão com movimento suave da câmera
        int targetCameraX = player.x - GameConfig.SCREEN_WIDTH / 2;

        // Zona morta - só move câmera se player sair da zona central
        int playerScreenX = player.x - cameraX;
        int screenCenter = GameConfig.SCREEN_WIDTH / 2;

        if (playerScreenX < screenCenter - CAMERA_DEADZONE) {
            targetCameraX = player.x - screenCenter + CAMERA_DEADZONE;
        } else if (playerScreenX > screenCenter + CAMERA_DEADZONE) {
            targetCameraX = player.x - screenCenter - CAMERA_DEADZONE;
        } else {
            targetCameraX = cameraX; // Não mover câmera
        }

        // Limitar aos bounds do mundo
        if (targetCameraX < 0) {
            targetCameraX = 0;
        } else if (targetCameraX > GameConfig.WORLD_WIDTH - GameConfig.SCREEN_WIDTH) {
            targetCameraX = GameConfig.WORLD_WIDTH - GameConfig.SCREEN_WIDTH;
        }

        // Aplicar suavização
        cameraX += (targetCameraX - cameraX) / CAMERA_SMOOTHNESS;

        cameraY = 0;
    }

    public void resetCamera() {
        cameraX = 0;
        cameraY = 0;
    }

    public boolean isObjectVisible(double x, double y, int width, int height) {
        return x + width >= cameraX &&
                x <= cameraX + GameConfig.SCREEN_WIDTH &&
                y + height >= cameraY &&
                y <= cameraY + GameConfig.SCREEN_HEIGHT;
    }


    public boolean isPlatformVisible(Platform platform) {
        return isObjectVisible(platform.x, platform.y, platform.width, platform.height);
    }

    public boolean isEnemyVisible(Enemy enemy) {
        return isObjectVisible(enemy.x, enemy.y, enemy.width, enemy.height);
    }

    public boolean isOrbVisible(EnergyOrb orb) {
        return isObjectVisible(orb.x - 10, orb.y - 10, 20, 20); // Assumindo orb de 20x20
    }

    // Getters
    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }

    // Setters (para casos especiais)
    public void setCameraX(int x) { this.cameraX = x; }
    public void setCameraY(int y) { this.cameraY = y; }
}