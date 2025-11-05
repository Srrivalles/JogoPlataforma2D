package org.example.inputs;

<<<<<<< HEAD
=======
import org.example.ui.ComponentGamePanel;
import org.example.ui.GamePanel;
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

<<<<<<< HEAD
import org.example.ui.ComponentGamePanel;
import org.example.ui.GamePanel;

=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
public class InputHandler implements KeyListener {
    private ComponentGamePanel componentGamePanel;
    private GamePanel gamePanel;
    private Set<Integer> keysPressed = new HashSet<>();
    
    // Estados dos botões
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean jumpPressed = false;
    private boolean dashPressed = false;
    private boolean escPressed = false;
    private boolean ePressed = false;
    
    public InputHandler(ComponentGamePanel gamePanel) {
        this.componentGamePanel = gamePanel;
    }
    
    public InputHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void update() {
        // Aplicar inputs ao player
        if (componentGamePanel != null && componentGamePanel.getPlayer() != null) {
            var player = componentGamePanel.getPlayer();
            
            // Movimento horizontal
            if (leftPressed) {
                player.moveLeft();
            } else if (rightPressed) {
                player.moveRight();
            } else {
                player.stopMoving();
            }
            
            // Pulo
            if (jumpPressed) {
                player.jump();
            }
            
            // Dash
            if (dashPressed) {
                player.dash();
            }
            
            // Toggle inimigos (tecla E)
            if (ePressed) {
                gamePanel.toggleEnemies();
                ePressed = false; // Reset para evitar múltiplas ativações
            }
        } else if (gamePanel != null && gamePanel.getPlayer() != null) {
            var player = gamePanel.getPlayer();
            
            // Movimento horizontal
            if (leftPressed) {
                player.moveLeft();
            } else if (rightPressed) {
                player.moveRight();
            } else {
                player.stopMoving();
            }
            
            // Pulo
            if (jumpPressed) {
                player.jump();
            }
            
            // Dash
            if (dashPressed) {
                player.dash();
            }
            
            // Toggle inimigos (tecla E)
            if (ePressed) {
                gamePanel.toggleEnemies();
                ePressed = false; // Reset para evitar múltiplas ativações
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.add(keyCode);
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jumpPressed = true;
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_X:
                dashPressed = true;
                break;
            case KeyEvent.VK_ESCAPE:
                escPressed = true;
                break;
            case KeyEvent.VK_E:
                ePressed = true;
                break;
<<<<<<< HEAD
            case KeyEvent.VK_F11:
                // Alternar tela cheia via GamePanel
                if (gamePanel != null && gamePanel.getGameFrame() != null) {
                    gamePanel.getGameFrame().toggleFullScreen();
                }
                break;
=======
>>>>>>> 5909f9628214d32c37618f5fb01e5d573c4da176
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keysPressed.remove(keyCode);
        
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jumpPressed = false;
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_X:
                dashPressed = false;
                break;
            case KeyEvent.VK_ESCAPE:
                escPressed = false;
                break;
            case KeyEvent.VK_E:
                ePressed = false;
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Não usado
    }
    
    // Getters para estados
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isJumpPressed() { return jumpPressed; }
    public boolean isDashPressed() { return dashPressed; }
    public boolean isEscPressed() { return escPressed; }
    public boolean isEPressed() { return ePressed; }
}