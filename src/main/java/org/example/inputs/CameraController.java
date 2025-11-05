package org.example.inputs;

import org.example.objects.Player;

public class CameraController {
    private int cameraX = 0;
    private int cameraY = 0;

    // Camera settings
    private static final int CAMERA_SMOOTHNESS = 8;
    private static final int CAMERA_DEADZONE = 50;

    // Screen dimensions
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    // CORREÇÃO: Aumentar margem de renderização para orbs distantes
    private static final int RENDER_MARGIN = 1000; // Era 200, agora 1000

    public void updateCamera(int playerX, int playerY, int screenWidth) {
        int targetCameraX = playerX - screenWidth / 2;

        // Permitir câmera negativa temporariamente se necessário
        if (playerX < screenWidth / 2) {
            targetCameraX = 0;
        }

        // Direct movement (no smoothing)
        cameraX = targetCameraX;

        // Fixed vertical camera
        cameraY = 0;
    }

    public void updateCameraSmooth(int playerX, int playerY, int screenWidth) {
        int targetCameraX = playerX - screenWidth / 2;

        // Dead zone
        int playerScreenX = playerX - cameraX;
        int screenCenter = screenWidth / 2;

        if (playerScreenX < screenCenter - CAMERA_DEADZONE) {
            targetCameraX = playerX - screenCenter + CAMERA_DEADZONE;
        } else if (playerScreenX > screenCenter + CAMERA_DEADZONE) {
            targetCameraX = playerX - screenCenter - CAMERA_DEADZONE;
        } else {
            targetCameraX = cameraX;
        }

        if (playerX < screenWidth / 2) {
            targetCameraX = 0;
        }

        // Apply smoothing
        cameraX += (targetCameraX - cameraX) / CAMERA_SMOOTHNESS;

        cameraY = 0;
    }

    public void updateCamera(Player player) {
        updateCamera((int)player.x, (int)player.y, SCREEN_WIDTH);
    }

    public void updateCameraSmooth(Player player) {
        updateCameraSmooth((int)player.x, (int)player.y, SCREEN_WIDTH);
    }

    public void centerOnPlayer(Player player) {
        int targetCameraX = (int)player.x - SCREEN_WIDTH / 2;

        if (player.x < SCREEN_WIDTH / 2) {
            targetCameraX = 0;
        }

        cameraX = targetCameraX;
        cameraY = 0;
    }

    public void resetCamera() {
        cameraX = 0;
        cameraY = 0;
    }

    /**
     * CORRIGIDO: Aumentar margem de segurança para garantir renderização de objetos distantes
     */
    public boolean isObjectVisible(double x, double y, double width, double height) {
        return x + width >= cameraX - RENDER_MARGIN &&
                x <= cameraX + SCREEN_WIDTH + RENDER_MARGIN &&
                y + height >= cameraY - RENDER_MARGIN &&
                y <= cameraY + SCREEN_HEIGHT + RENDER_MARGIN;
    }

    /**
     * NOVO: Verificação específica para orbs (maior alcance)
     */
    public boolean isOrbVisibleExtended(Object orb) {
        if (orb == null) return false;

        try {
            java.lang.reflect.Field xField = orb.getClass().getDeclaredField("x");
            java.lang.reflect.Field yField = orb.getClass().getDeclaredField("y");

            xField.setAccessible(true);
            yField.setAccessible(true);

            double x = ((Number) xField.get(orb)).doubleValue();
            double y = ((Number) yField.get(orb)).doubleValue();

            // Margem maior para orbs (2x o normal)
            int extendedMargin = RENDER_MARGIN * 2;

            return x >= cameraX - extendedMargin &&
                    x <= cameraX + SCREEN_WIDTH + extendedMargin &&
                    y >= cameraY - extendedMargin &&
                    y <= cameraY + SCREEN_HEIGHT + extendedMargin;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isEnemyVisible(Object enemy) {
        if (enemy == null) return false;

        try {
            java.lang.reflect.Field xField = enemy.getClass().getDeclaredField("x");
            java.lang.reflect.Field yField = enemy.getClass().getDeclaredField("y");
            java.lang.reflect.Field widthField = enemy.getClass().getDeclaredField("width");
            java.lang.reflect.Field heightField = enemy.getClass().getDeclaredField("height");

            xField.setAccessible(true);
            yField.setAccessible(true);
            widthField.setAccessible(true);
            heightField.setAccessible(true);

            double x = ((Number) xField.get(enemy)).doubleValue();
            double y = ((Number) yField.get(enemy)).doubleValue();
            double width = ((Number) widthField.get(enemy)).doubleValue();
            double height = ((Number) heightField.get(enemy)).doubleValue();

            return isObjectVisible(x, y, width, height);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isPlatformVisible(Object platform) {
        if (platform == null) return false;

        try {
            java.lang.reflect.Field xField = platform.getClass().getDeclaredField("x");
            java.lang.reflect.Field yField = platform.getClass().getDeclaredField("y");
            java.lang.reflect.Field widthField = platform.getClass().getDeclaredField("width");
            java.lang.reflect.Field heightField = platform.getClass().getDeclaredField("height");

            xField.setAccessible(true);
            yField.setAccessible(true);
            widthField.setAccessible(true);
            heightField.setAccessible(true);

            double x = ((Number) xField.get(platform)).doubleValue();
            double y = ((Number) yField.get(platform)).doubleValue();
            double width = ((Number) widthField.get(platform)).doubleValue();
            double height = ((Number) heightField.get(platform)).doubleValue();

            return isObjectVisible(x, y, width, height);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isOrbVisible(Object orb) {
        if (orb == null) return false;

        try {
            java.lang.reflect.Field xField = orb.getClass().getDeclaredField("x");
            java.lang.reflect.Field yField = orb.getClass().getDeclaredField("y");
            java.lang.reflect.Field widthField = orb.getClass().getDeclaredField("width");
            java.lang.reflect.Field heightField = orb.getClass().getDeclaredField("height");

            xField.setAccessible(true);
            yField.setAccessible(true);
            widthField.setAccessible(true);
            heightField.setAccessible(true);

            double x = ((Number) xField.get(orb)).doubleValue();
            double y = ((Number) yField.get(orb)).doubleValue();
            double width = ((Number) widthField.get(orb)).doubleValue();
            double height = ((Number) heightField.get(orb)).doubleValue();

            return isObjectVisible(x, y, width, height);
        } catch (Exception e) {
            return true;
        }
    }

    // Getters
    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    // Setters
    public void setCameraX(int x) {
        this.cameraX = x;
    }

    public void setCameraY(int y) {
        this.cameraY = y;
    }
}