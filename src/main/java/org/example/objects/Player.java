package org.example.objects;

import org.example.entities.PlayerEntity;
import org.example.graphics.AnimationManager;
import org.example.graphics.SpriteRenderer;
import org.example.ui.GameConfig;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.util.ArrayList;

import org.example.ui.GamePanel;

public class Player extends PlayerEntity {
        public int x, y;
    public int previousY;
    public int width = 32, height = 48;
    public int health = 100;
    public int lives = 3; // Sistema de vidas (3 vidas iniciais)
    public double velocityX = 0, velocityY = 0;
    public Rectangle hitbox;
    public boolean facingRight = true, isOnGround = false;
    public int animationTimer = 0, hoverOffset = 0;

    // Sistema de Dash/Teleporte - VARIAVEIS CORRIGIDAS
    boolean dashIgnoresGravity = false; // Para dash no ar não cair
    int dashOriginalY = 0; // Para manter altura durante dash
    boolean isTeleportDash = true; // true = teleporte, false = dash normal
    public boolean canDash = true, isDashing = false;
    
    int dashTimer = 0;
    public int dashCooldown = 0;
    final int DASH_DURATION = 8;
    final int DASH_COOLDOWN_TIME = 30;
    final int DASH_DISTANCE = 80;
    final int DASH_ENERGY_COST = 25;

    // Cores do personagem cyber (ajuste: corpo metálico cinza + neon azul)
    Color primaryColor = new Color(0, 200, 255);      // Azul neon
    Color secondaryColor = new Color(0, 160, 220);    // Azul secundário
    Color accentColor = new Color(0, 255, 255);       // Ciano para brilhos
    Color bodyColor = new Color(70, 70, 80);          // Cinza metálico
    public String currentMode = "normal";

    // Efeitos visuais
    public int energyLevel = 100;
    boolean isCharging = false;
    int particleTimer = 0;

    // Efeitos visuais do dash
    ArrayList<DashTrail> dashTrails = new ArrayList<>();
    boolean showTeleportEffect = false;
    int teleportEffectTimer = 0;
    
    // Efeito visual de dano
    boolean isTakingDamage = false;
    int damageEffectTimer = 0;
    
    // Sistema de invencibilidade
    boolean isInvulnerable = false;
    int invulnerabilityTimer = 0;
    final int INVULNERABILITY_DURATION = 60; // 1 segundo a 60 FPS
    
    // Sistema de sprites e animações
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private String currentAnimation;

    // Construtor
    public Player(int startX, int startY) {
        super(startX, startY); // Chama o construtor da classe pai
        this.x = startX;
        this.y = startY;
        this.previousY = startY;
        this.hitbox = new Rectangle(x, y, width, height);
        
        // Inicializar sistema de sprites
        this.animationManager = AnimationManager.getInstance();
        this.spriteRenderer = SpriteRenderer.getInstance();
        this.currentAnimation = "player_idle";
    }

    public void takeDamage(int dmg, GamePanel panel) {
        health -= dmg;
        if (health <= 0) {
            panel.onPlayerDeath();
        }
    }

    public void update() {
        // Salva a posição anterior (importante para colisões)
        previousY = y;

        if (velocityX > 0) {
            facingRight = true;
        } else if (velocityX < 0) {
            facingRight = false;
        }
        
        // Atualizar animação baseada no estado
        updateAnimation();
        
        // Atualizar sistema de dash
        updateDash();
        
        // Atualizar efeito de dano
        if (isTakingDamage && damageEffectTimer > 0) {
            damageEffectTimer--;
            if (damageEffectTimer <= 0) {
                isTakingDamage = false;
            }
        }
        
        // Atualizar invencibilidade
        if (isInvulnerable && invulnerabilityTimer > 0) {
            invulnerabilityTimer--;
            if (invulnerabilityTimer <= 0) {
                isInvulnerable = false;
            }
        }

        if (isDashing) {
            currentMode = "dash";
        } else if (!isOnGround && velocityY < -10) {
            currentMode = "boost";
        } else if (!isOnGround && velocityY > 0) {
            currentMode = "gliding";
        } else if (Math.abs(velocityX) > 3) {
            currentMode = "charging";
        } else {
            currentMode = "normal";
        }
// MOVIMENTO ESPECIAL PARA DASH DE TELEPORTE
        if (isDashing && isTeleportDash) {
            // No dash de teleporte, manter Y fixo
            y = dashOriginalY;
        }
        // MOVIMENTO ESPECIAL PARA DASH NORMAL
        else if (isDashing && !isTeleportDash) {
            // No dash normal, apenas aplicar velocityY se não estiver ignorando gravidade
            if (!dashIgnoresGravity) {
                // O movimento X e Y será aplicado pelo PhysicsEngine
            }
        }
        // MOVIMENTO NORMAL
        else {
            // O movimento será aplicado pelo PhysicsEngine durante verificação de colisões
            // NÃO aplicar movimento aqui para evitar atravessar plataformas
        }

        // Aplicar gravidade apenas se não estiver em dash que ignora gravidade
        if (dashIgnoresGravity && isDashing) {
            // Durante dash, manter posição Y estável
            if (isTeleportDash) {
                y = dashOriginalY; // Força a altura original no teleporte
            }
            // velocityY já está em 0, então não aplicar gravidade
        }
        // A gravidade normal será aplicada pelo PhysicsEngine no GamePanel

        // Movimento será aplicado pelo PhysicsEngine durante verificação de colisões
        // Não aplicar x += velocityX ou y += velocityY aqui para evitar atravessar plataformas

        // Atualiza os timers de animação
        animationTimer++;
        particleTimer++;

        // Atualizar trails do dash
        updateDashTrails();

        // Efeito de flutuação quando parado
        if (velocityX == 0 && isOnGround && !isDashing) {
            hoverOffset = (int)(Math.sin(animationTimer * 0.1) * 2);
        } else {
            hoverOffset = 0;
        }

        // Atualiza a hitbox (posição já ajustada pela física)
        hitbox.setLocation(x, y);
    }

    // Métodos para movimento
    public void moveLeft() {
        if (!isDashing) {
            velocityX = -4;
        }
    }

    public void moveRight() {
        if (!isDashing) {
            velocityX = 4;
        }
    }

    public void stopMoving() {
        if (!isDashing) {
            velocityX = 0;
        }
    }

    public void jump() {
        if (isOnGround && !isDashing) {
            velocityY = -10; // Força do pulo
            isOnGround = false;
            currentMode = "boost";

            // ✅ SOM DO PULO (já deve estar aqui)
            try {
                org.example.audio.AudioManager.playJumpSound();
            } catch (Exception e) {
                // Ignorar erros de áudio
            }
        }
    }


    public void dash() {
        // CONDIÇÕES SIMPLIFICADAS - funciona no chão E no ar
        if (canDash && energyLevel >= DASH_ENERGY_COST && !isDashing) {
            energyLevel -= DASH_ENERGY_COST;
            isDashing = true;
            canDash = false;
            dashTimer = DASH_DURATION;
            dashCooldown = DASH_COOLDOWN_TIME;

            // ✅ SOM DO DASH
            org.example.audio.AudioManager.playDashSound();

            int dashDirection = facingRight ? 1 : -1;

            if (isTeleportDash) {
                // MODO TELEPORTE - movimento instantâneo
                dashOriginalY = y; // Salva Y atual
                x += dashDirection * DASH_DISTANCE; // Teleporte instantâneo

                // SEMPRE ignora gravidade durante teleporte (ar ou chão)
                dashIgnoresGravity = true;
                velocityY = 0; // Para qualquer movimento vertical

            } else {
                // MODO DASH NORMAL - velocidade alta
                velocityX = dashDirection * 15;

                // SEMPRE ignora gravidade durante dash normal (ar ou chão)
                dashIgnoresGravity = true;
                velocityY = 0; // Estabiliza movimento vertical
            }

            // Efeitos visuais
            dashTrails.add(new DashTrail(x, y, 15));
            showTeleportEffect = true;
            teleportEffectTimer = 10;

            String location = isOnGround ? "CHÃO" : "AR";
        } else {
            // Debug - por que o dash não funcionou?
            if (!canDash) {}
            if (energyLevel < DASH_ENERGY_COST) {}
            if (isDashing) {}
        }
    }


    // MÉTODO UPDATE DASH CORRIGIDO
    private void updateDash() {
        // Atualizar timer do dash
        if (isDashing) {
            dashTimer--;

            if (dashTimer <= 0) {
                isDashing = false;
                dashIgnoresGravity = false; // Volta a aplicar gravidade

                if (!isTeleportDash) {
                    velocityX = 0; // Para o movimento no dash normal
                }

                
            }
        }

        // Atualizar cooldown
        if (dashCooldown > 0) {
            dashCooldown--;
            if (dashCooldown <= 0) {
                canDash = true;
            }
        }

        // Atualizar efeito de teleporte
        if (showTeleportEffect) {
            teleportEffectTimer--;
            if (teleportEffectTimer <= 0) {
                showTeleportEffect = false;
            }
        }
    }

    // MÉTODO PARA ALTERNAR TIPO DE DASH
    public void toggleDashType() {
        isTeleportDash = !isTeleportDash;
    }

    private void updateDashTrails() {
        // Atualizar trails existentes
        for (int i = dashTrails.size() - 1; i >= 0; i--) {
            DashTrail trail = dashTrails.get(i);
            trail.update();
            if (trail.isExpired()) {
                dashTrails.remove(i);
            }
        }
    }
    
    /**
     * Atualiza a animação baseada no estado do player
     */
    private void updateAnimation() {
        if (!GameConfig.ANIMATIONS_ENABLED) return;
        
        String newAnimation = "player_idle";
        
        if (isDashing) {
            newAnimation = "player_dash";
        } else if (!isOnGround && velocityY < -5) {
            newAnimation = "player_jump";
        } else if (Math.abs(velocityX) > 1) {
            newAnimation = "player_walk";
        }
        
        // Mudar animação apenas se for diferente da atual
        if (!newAnimation.equals(currentAnimation)) {
            currentAnimation = newAnimation;
        }
    }

    public void draw(Graphics2D g2d) {
        if (GameConfig.ANIMATIONS_ENABLED) {
            // Renderizar com sprite
            drawSprite(g2d);
        } else {
            // Renderização legada
            drawLegacy(g2d);
        }
    }
    
    /**
     * Renderiza o player usando sprites
     */
    private void drawSprite(Graphics2D g2d) {
        // Renderizar sprite do player
        boolean flipX = !facingRight;
        spriteRenderer.renderAnimation(g2d, currentAnimation, x, y, GameConfig.SPRITE_SCALE, flipX);
        
        // Renderizar efeitos visuais adicionais se necessário
        if (isTakingDamage) {
            // Efeito de dano (piscar)
            int alpha = (damageEffectTimer % 10 < 5) ? 100 : 255;
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha / 255.0f));
            spriteRenderer.renderAnimation(g2d, currentAnimation, x, y, GameConfig.SPRITE_SCALE, flipX);
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    /**
     * Renderização legada com formas geométricas
     */
    private void drawLegacy(Graphics2D g2d) {
        // Salvar configurações originais
        Color originalColor = g2d.getColor();
        BasicStroke originalStroke = (BasicStroke) g2d.getStroke();

        // Efeito visual de dano (piscar vermelho)
        if (isTakingDamage) {
            int alpha = (damageEffectTimer % 10 < 5) ? 100 : 255;
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha / 255.0f));
        }

        // Aplicar offset de flutuação
        int drawY = y + hoverOffset;

        // Desenhar efeitos de partículas primeiro (atrás do personagem)
        drawParticleEffects(g2d, drawY);

        // Jetpack (atrás do corpo)
        drawJetpack(g2d, drawY);

        // CORPO PRINCIPAL (torso com gradiente metálico)
        GradientPaint metal = new GradientPaint(x + 6, drawY + 10, bodyColor.brighter(), x + 26, drawY + 40, bodyColor.darker());
        g2d.setPaint(metal);
        g2d.fillRoundRect(x + 6, drawY + 12, 20, 26, 6, 6);

        // Contorno do corpo com leve brilho azul
        g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x + 6, drawY + 12, 20, 26, 6, 6);

        // CABEÇA (formato de capacete futurista)
        drawHead(g2d, drawY);

        // DETALHES DO PEITO (linha central de energia pulsante)
        drawChestEnergyLine(g2d, drawY);

        // BRAÇOS (robóticos retos)
        drawArms(g2d, drawY);

        // PERNAS (propulsores)
        drawLegs(g2d, drawY);

        // EFEITOS VISUAIS (aura, luzes)
        drawVisualEffects(g2d, drawY);

        // HUD (indicador de energia)
        drawEnergyHUD(g2d, drawY);

        // Restaurar configurações originais
        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    private void drawHead(Graphics2D g2d, int drawY) {
        // Base da cabeça (metálica)
        g2d.setColor(bodyColor.brighter());
        g2d.fillRoundRect(x + 6, drawY + 2, 20, 18, 8, 8);

        // Olhos digitais (retângulos luminosos que mudam com facingRight)
        int eyeWidth = 4;
        int eyeHeight = 3;
        int eyeY = drawY + 8;
        int eyeLeftX = facingRight ? x + 12 : x + 9;
        int eyeRightX = facingRight ? x + 18 : x + 15;

        // Glow do olho
        Color eyeGlow = new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 180);
        g2d.setColor(eyeGlow);
        g2d.fillRoundRect(eyeLeftX - 1, eyeY - 1, eyeWidth + 2, eyeHeight + 2, 4, 4);
        g2d.fillRoundRect(eyeRightX - 1, eyeY - 1, eyeWidth + 2, eyeHeight + 2, 4, 4);

        // Núcleo dos olhos
        g2d.setColor(Color.WHITE);
        g2d.fillRect(eyeLeftX, eyeY, eyeWidth, eyeHeight);
        g2d.fillRect(eyeRightX, eyeY, eyeWidth, eyeHeight);

        // Sutil reflexo horizontal
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillRect(x + 8, drawY + 6, 16, 1);
    }

    private void drawChestEnergyLine(Graphics2D g2d, int drawY) {
        // Linha vertical central com pulso e glow azul
        int centerX = x + 16;
        int topY = drawY + 14;
        int bottomY = drawY + 34;

        // Glow externo pulsante
        int glowPhase = (int)(Math.abs(Math.sin(animationTimer * 0.2)) * 40) + 40; // 40-80 alpha
        g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), glowPhase));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(centerX, topY, centerX, bottomY);

        // Núcleo da linha
        g2d.setColor(new Color(200, 240, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(centerX, topY, centerX, bottomY);

        // Pulso correndo pela linha
        double t = (animationTimer * 0.25) % (bottomY - topY);
        int pulseY = (int)(topY + t);
        g2d.setColor(new Color(0, 255, 255, 200));
        g2d.fillOval(centerX - 2, pulseY - 2, 4, 4);
    }

    private void drawArms(Graphics2D g2d, int drawY) {
        // Braços robóticos retos (aparência mecânica)
        g2d.setColor(bodyColor);
        // Segmentos retos verticais ao lado do torso
        g2d.fillRoundRect(x + 2, drawY + 16, 6, 14, 2, 2);   // Esquerdo
        g2d.fillRoundRect(x + 24, drawY + 16, 6, 14, 2, 2);  // Direito

        // Juntas metálicas
        g2d.setColor(bodyColor.darker());
        g2d.fillRect(x + 3, drawY + 22, 4, 2);
        g2d.fillRect(x + 25, drawY + 22, 4, 2);

        // Detalhe neon azul
        g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 180));
        g2d.fillRect(x + 4, drawY + 18, 2, 10);
        g2d.fillRect(x + 26, drawY + 18, 2, 10);
    }

    private void drawJetpack(Graphics2D g2d, int drawY) {
        // Mochila a jato centralizada nas costas
        int packX = x + 10;
        int packY = drawY + 18;

        // Corpo do jetpack (metálico)
        g2d.setColor(bodyColor.darker());
        g2d.fillRoundRect(packX, packY, 12, 12, 3, 3);

        // Bocal duplo inferior
        g2d.setColor(bodyColor);
        g2d.fillRect(packX + 1, packY + 11, 4, 3);
        g2d.fillRect(packX + 7, packY + 11, 4, 3);

        // Chama animada (pulsando com seno)
        double s = Math.abs(Math.sin(animationTimer * 0.25));
        int flameLen = 6 + (int)(s * 4);

        // Gradiente de chama: branco -> ciano -> azul
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(packX + 1, packY + 11 + 2, 4, flameLen / 2);
        g2d.fillOval(packX + 7, packY + 11 + 2, 4, flameLen / 2);

        g2d.setColor(new Color(180, 240, 255, 200));
        g2d.fillOval(packX + 1, packY + 11 + 2 + flameLen / 4, 4, flameLen / 2);
        g2d.fillOval(packX + 7, packY + 11 + 2 + flameLen / 4, 4, flameLen / 2);

        g2d.setColor(new Color(0, 180, 255, 160));
        int[] flameX1 = {packX + 2, packX + 3, packX + 4};
        int[] flameY1 = {packY + 14 + flameLen / 2, packY + 14 + flameLen, packY + 14 + flameLen / 2};
        g2d.fillPolygon(flameX1, flameY1, 3);

        int[] flameX2 = {packX + 8, packX + 9, packX + 10};
        int[] flameY2 = {packY + 14 + flameLen / 2, packY + 14 + flameLen, packY + 14 + flameLen / 2};
        g2d.fillPolygon(flameX2, flameY2, 3);
    }

    private void drawLegs(Graphics2D g2d, int drawY) {
        g2d.setColor(bodyColor);

        if (!isOnGround) {
            // Pose de pulo - pernas dobradas com propulsores ativos
            g2d.fillRoundRect(x + 9, drawY + 35, 5, 8, 2, 2);
            g2d.fillRoundRect(x + 18, drawY + 35, 5, 8, 2, 2);

            // Propulsores nos pés (chamas/energia)
            g2d.setColor(accentColor);
            int[] flameX1 = {x + 10, x + 13, x + 11};
            int[] flameY1 = {drawY + 43, drawY + 47, drawY + 47};
            g2d.fillPolygon(flameX1, flameY1, 3);

            int[] flameX2 = {x + 19, x + 22, x + 20};
            int[] flameY2 = {drawY + 43, drawY + 47, drawY + 47};
            g2d.fillPolygon(flameX2, flameY2, 3);

        } else if (Math.abs(velocityX) > 0) {
            // Animação de corrida
            int legOffset = (animationTimer / 6) % 2;

            if (legOffset == 0) {
                g2d.fillRoundRect(x + 8, drawY + 35, 5, 10, 2, 2);
                g2d.fillRoundRect(x + 19, drawY + 35, 5, 10, 2, 2);
            } else {
                g2d.fillRoundRect(x + 10, drawY + 35, 5, 10, 2, 2);
                g2d.fillRoundRect(x + 17, drawY + 35, 5, 10, 2, 2);
            }
        } else {
            // Parado
            g2d.fillRoundRect(x + 9, drawY + 35, 5, 10, 2, 2);
            g2d.fillRoundRect(x + 18, drawY + 35, 5, 10, 2, 2);
        }

        // Detalhes dos pés (propulsores inativos)
        if (isOnGround) {
            g2d.setColor(primaryColor);
            g2d.fillRect(x + 10, drawY + 42, 3, 2);
            g2d.fillRect(x + 19, drawY + 42, 3, 2);
        }
    }

    private void drawVisualEffects(Graphics2D g2d, int drawY) {
        // Desenhar trails do dash primeiro
        for (DashTrail trail : dashTrails) {
            trail.draw(g2d);
        }

        // Efeito de teleporte
        if (showTeleportEffect) {
            drawTeleportEffect(g2d, drawY);
        }

        // EFEITO VISUAL MAIS INTENSO PARA TELEPORTE
        if (isDashing && isTeleportDash) {
            // Aura de teleporte mais brilhante
            g2d.setColor(new Color(255, 255, 0, 200));
            g2d.fillOval(x - 8, drawY - 8, width + 16, height + 16);

            g2d.setColor(new Color(0, 255, 255, 150));
            g2d.fillOval(x - 4, drawY - 4, width + 8, height + 8);

            // Partículas de teleporte
            for (int i = 0; i < 12; i++) {
                double angle = (i * Math.PI * 2) / 12;
                int particleX = (int)(x + width/2 + Math.cos(angle + animationTimer * 0.2) * 20);
                int particleY = (int)(drawY + height/2 + Math.sin(angle + animationTimer * 0.2) * 20);

                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillOval(particleX - 1, particleY - 1, 3, 3);
            }
        }

        // Aura de energia ao redor do personagem
        if (currentMode.equals("boost") || currentMode.equals("charging") || isDashing) {
            Color auraColor = isDashing ? new Color(255, 255, 0, 100) : new Color(0, 255, 255, 50);
            g2d.setColor(auraColor);
            g2d.fillOval(x - 2, drawY - 2, width + 4, height + 4);

            if (isDashing && !isTeleportDash) {
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillOval(x - 4, drawY - 4, width + 8, height + 8);
            } else if (!isDashing) {
                g2d.setColor(new Color(255, 0, 150, 30));
                g2d.fillOval(x - 4, drawY - 4, width + 8, height + 8);
            }
        }

        // Linhas de velocidade quando correndo ou em dash
        if (Math.abs(velocityX) > 3 || isDashing) {
            Color speedLineColor = isDashing ? new Color(255, 255, 0, 200) : new Color(255, 255, 255, 100);
            g2d.setColor(speedLineColor);
            g2d.setStroke(new BasicStroke(isDashing ? 3 : 2));

            int lineCount = isDashing ? 5 : 3;
            for (int i = 0; i < lineCount; i++) {
                int lineX = facingRight ? x - 10 - (i * 8) : x + width + 10 + (i * 8);
                int lineY = drawY + 15 + (i * 5);
                int lineLength = isDashing ? 12 : 6;

                if (facingRight) {
                    g2d.drawLine(lineX, lineY, lineX + lineLength, lineY);
                } else {
                    g2d.drawLine(lineX, lineY, lineX - lineLength, lineY);
                }
            }
        }

        // Indicador de dash disponível
        if (!canDash && dashCooldown > 0) {
            drawDashCooldownIndicator(g2d, drawY);
        }
    }

    private void drawParticleEffects(Graphics2D g2d, int drawY) {
        // Partículas de energia flutuando
        if (particleTimer % 20 == 0 || currentMode.equals("boost")) {
            g2d.setColor(new Color(0, 255, 255, 150));

            for (int i = 0; i < 3; i++) {
                int particleX = x + (int)(Math.random() * width);
                int particleY = drawY + (int)(Math.random() * height);
                g2d.fillOval(particleX, particleY, 2, 2);
            }
        }
    }

    private void drawEnergyHUD(Graphics2D g2d, int drawY) {
        // Barra de energia principal
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x + 6, drawY - 8, 20, 3);

        // Barra de energia
        Color energyColor = energyLevel > 50 ? primaryColor :
                energyLevel > 25 ? accentColor : secondaryColor;
        g2d.setColor(energyColor);
        g2d.fillRect(x + 7, drawY - 7, (energyLevel * 18) / 100, 1);

        // Indicador de dash disponível (removido)
        // if (canDash && energyLevel >= DASH_ENERGY_COST) {
        //     g2d.setColor(new Color(255, 255, 0, 150));
        //     g2d.fillOval(x + width - 8, drawY - 12, 6, 6);
        //     g2d.setColor(Color.WHITE);
        //     g2d.drawString("D", x + width - 6, drawY - 7);
        // }
    }

    private void drawTeleportEffect(Graphics2D g2d, int drawY) {
        // Círculos de energia expandindo
        float alpha = teleportEffectTimer / 10.0f;
        int radius = (10 - teleportEffectTimer) * 3;

        g2d.setColor(new Color(1.0f, 1.0f, 0.0f, alpha * 0.8f));
        g2d.fillOval(x + width/2 - radius, drawY + height/2 - radius, radius * 2, radius * 2);

        g2d.setColor(new Color(0.0f, 1.0f, 1.0f, alpha * 0.6f));
        g2d.fillOval(x + width/2 - radius/2, drawY + height/2 - radius/2, radius, radius);

        // Partículas de energia
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8;
            int particleX = (int)(x + width/2 + Math.cos(angle) * radius * 0.8);
            int particleY = (int)(drawY + height/2 + Math.sin(angle) * radius * 0.8);

            g2d.setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
            g2d.fillOval(particleX - 2, particleY - 2, 4, 4);
        }
    }

    private void drawDashCooldownIndicator(Graphics2D g2d, int drawY) {
        // Círculo de cooldown
        float progress = (float)(DASH_COOLDOWN_TIME - dashCooldown) / DASH_COOLDOWN_TIME;

        g2d.setColor(new Color(255, 100, 100, 100));
        g2d.fillOval(x + width - 8, drawY - 12, 6, 6);

        g2d.setColor(new Color(255, 255, 0, (int)(255 * progress)));
        g2d.fillArc(x + width - 8, drawY - 12, 6, 6, 90, (int)(360 * progress));
    }

    // Métodos para customizar aparência
    public void changePrimaryColor(Color newColor) {
        this.primaryColor = newColor;
    }

    public void changeSecondaryColor(Color newColor) {
        this.secondaryColor = newColor;
    }

    public void changeAccentColor(Color newColor) {
        this.accentColor = newColor;
    }

    // Métodos especiais para o personagem cyber
    public void activateBoostMode() {
        currentMode = "boost";
        // REMOVIDO: não consome energia para boost mode
    }

    public void rechargeEnergy() {
        // Recarrega energia sempre quando no chão (parado ou andando)
        if (isOnGround) {
            energyLevel = Math.min(100, energyLevel + 1);
        }
        // Recarrega mais rápido quando parado
        if (isOnGround && velocityX == 0) {
            energyLevel = Math.min(100, energyLevel + 2);
        }
    }

    // Getters úteis para colisão
    public int getPreviousY() {
        return previousY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    // Métodos para sistema de vidas
    public void loseLife() {
        // Só pode perder vida se não estiver invulnerável
        if (lives > 0 && !isInvulnerable) {
            lives--;
            
            // Ativar invencibilidade por 1 segundo
            isInvulnerable = true;
            invulnerabilityTimer = INVULNERABILITY_DURATION;
            
            // Ativar efeito visual de dano
            isTakingDamage = true;
            damageEffectTimer = 60; // 1 segundo a 60 FPS
            
            // Efeito de knockback para evitar que fique preso no inimigo
            if (facingRight) {
                velocityX = -8; // Empurrar para a esquerda
            } else {
                velocityX = 8;  // Empurrar para a direita
            }
            velocityY = -6; // Pequeno salto para cima
        }
    }

    public boolean isAlive() {
        return lives > 0;
    }
    
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void resetLives() {
        lives = 3;
    }

    public int getLives() {
        return lives;
    }

    // Classe interna para trails do dash
    private class DashTrail {
        int x, y;
        int life;
        int maxLife;

        public DashTrail(int x, int y, int maxLife) {
            this.x = x;
            this.y = y;
            this.life = maxLife;
            this.maxLife = maxLife;
        }

        public void update() {
            life--;
        }

        public boolean isExpired() {
            return life <= 0;
        }

        public void draw(Graphics2D g2d) {
            float alpha = (float)life / maxLife;

            g2d.setColor(new Color(0.0f, 1.0f, 1.0f, alpha * 0.7f));
            g2d.fillOval(x, y, width, height);

            g2d.setColor(new Color(1.0f, 1.0f, 0.0f, alpha * 0.5f));
            g2d.fillOval(x + 2, y + 2, width - 4, height - 4);
        }
    }
}