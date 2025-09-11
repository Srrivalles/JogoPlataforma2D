package org.example.world;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
// DESBLOQUEAR ESTES IMPORTS:
import org.example.graphics.AnimationManager;
import org.example.graphics.Animation;
import org.example.graphics.SpriteRenderer;

public class Platform {
    // Posição e dimensões da plataforma
    public int x, y;
    public int width, height;

    // Detectar colisões
    public Rectangle hitbox;

    // Tipo de plataforma para diferentes comportamentos
    public PlatformType type;

    // DESBLOQUEAR ESTES CAMPOS:
    private AnimationManager animationManager;
    private SpriteRenderer spriteRenderer;
    private Animation currentAnimation;
    private String platformId;
    private BufferedImage staticSprite;
    private boolean useSprites = false; // Desativar sprites para renderização legada
    private boolean isAnimated = false;
    // private long lastUpdateTime = 0; // Removido - não utilizado

    // Propriedades específicas para novos tipos de plataforma
    // Moving Platform
    private int moveStartX, moveEndX, moveStartY, moveEndY;
    private float moveSpeed = 1.0f;
    private boolean movingRight = true, movingDown = true;
    
    // Breakable Platform
    private boolean isBroken = false;
    private int breakTimer = 0;
    private final int BREAK_DELAY = 30; // Frames antes de quebrar
    private boolean isSteppedOn = false; // Se o player pisou na plataforma
    
    // Ice Platform
    private float iceFriction = 0.1f; // Reduz atrito
    
    // Bouncy Platform
    private float bounceMultiplier = 1.5f; // Multiplica o pulo
    
    // One-way Platform
    // private boolean playerAbove = false; // Removido - não utilizado

    // Enum para tipos de plataforma
    public enum PlatformType {
        GROUND,      // Chão normal
        BRICK,       // Tijolo destrutível
        PIPE,        // Cano
        CLOUD,       // Nuvem (plataforma temporária)
        MOVING,      // Plataforma móvel
        BREAKABLE,   // Quebra ao pular
        ICE,         // Escorregadia
        BOUNCY,      // Faz pular mais alto
        ONE_WAY      // Só passa por baixo
    }

    // Construtor com tipo
    public Platform(int x, int y, int width, int height, PlatformType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.hitbox = new Rectangle(x, y, width, height);

        // DESBLOQUEAR ESTA LINHA:
        initializeSpriteSystem();
        initializeTypeProperties();
    }

    // Construtor original para compatibilidade
    public Platform(int x, int y, int width, int height) {
        this(x, y, width, height, PlatformType.GROUND);
    }

    // Construtor para Moving Platform
    public Platform(int x, int y, int width, int height, PlatformType type, int moveEndX, int moveEndY, float speed) {
        this(x, y, width, height, type);
        if (type == PlatformType.MOVING) {
            this.moveStartX = x;
            this.moveStartY = y;
            this.moveEndX = moveEndX;
            this.moveEndY = moveEndY;
            this.moveSpeed = speed;
        }
    }

    // Construtor para Bouncy Platform
    public Platform(int x, int y, int width, int height, PlatformType type, float bounceMultiplier) {
        this(x, y, width, height, type);
        if (type == PlatformType.BOUNCY) {
            this.bounceMultiplier = bounceMultiplier;
        }
    }

    // Inicializar propriedades específicas do tipo
    private void initializeTypeProperties() {
        switch (type) {
            case MOVING:
                moveStartX = x;
                moveStartY = y;
                moveEndX = x + 100; // Movimento padrão
                moveEndY = y;
                break;
            case ICE:
                iceFriction = 0.1f;
                break;
            case BOUNCY:
                bounceMultiplier = 1.5f;
                break;
            case BREAKABLE:
                isBroken = false;
                breakTimer = 0;
                break;
            case GROUND:
            case BRICK:
            case CLOUD:
            case ONE_WAY:
            case PIPE:
            default:
                // Não precisam de inicialização especial
                break;
        }
    }

    // DESBLOQUEAR ESTE MÉTODO:
    private void initializeSpriteSystem() {
        try {
            this.animationManager = AnimationManager.getInstance();
            this.spriteRenderer = SpriteRenderer.getInstance();
            this.platformId = "platform_" + type.name().toLowerCase() + "_" + System.currentTimeMillis();

            loadPlatformSprite();

        } catch (Exception e) {
            System.err.println("Erro ao inicializar sprites da plataforma: " + e.getMessage());
            this.useSprites = false;
        }
    }

    /**
     * Carrega o sprite apropriado baseado no tipo da plataforma
     */
    private void loadPlatformSprite() {
        switch (type) {
            case GROUND:
                staticSprite = loadStaticSprite("platforms_basic", 0, 0);
                break;

            case BRICK:
                staticSprite = loadStaticSprite("platforms_basic", 1, 0);
                break;

            case PIPE:
                staticSprite = loadStaticSprite("platforms_basic", 2, 0);
                break;

            case CLOUD:
                // Plataforma nuvem usa animação flutuante
                currentAnimation = animationManager.getAnimation("platform_normal");
                isAnimated = true;
                break;

            case MOVING:
                // Plataforma móvel usa brilho animado
                currentAnimation = animationManager.getAnimation("platform_moving");
                isAnimated = true;
                break;

            default:
                staticSprite = loadStaticSprite("platforms_basic", 0, 0);
                break;
        }
    }

    /**
     * Carrega um sprite estático da sprite sheet
     * IMPLEMENTAR ESTE MÉTODO QUANDO SpriteSheet ESTIVER PRONTO
     */
    private BufferedImage loadStaticSprite(String sheetName, int col, int row) {
        try {
            // SpriteSheet sheet = animationManager.getSpriteSheet(sheetName);
            // return sheet.getSprite(col, row);

            // Por enquanto retorna null para usar fallback
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao carregar sprite estático: " + e.getMessage());
            return null;
        }
    }

    // Getter para a hitbox
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Atualiza a plataforma (principalmente para animações e comportamentos especiais)
     */
    public void update(long deltaTime) {
        // DESBLOQUEAR ESTA SEÇÃO:
        if (useSprites && isAnimated && currentAnimation != null) {
            currentAnimation.update();
        }
        
        // Atualizar comportamentos específicos baseados no tipo
        switch (type) {
            case MOVING:
                updateMovingPlatform();
                break;
            case BREAKABLE:
                updateBreakablePlatform();
                break;
            case ICE:
            case BOUNCY:
            case ONE_WAY:
            case GROUND:
            case BRICK:
            case CLOUD:
            case PIPE:
            default:
                // Não precisam de update especial
                break;
        }
    }

    // Atualizar plataforma móvel
    private void updateMovingPlatform() {
        if (isBroken) return; // Não mover se quebrada
        
        // Movimento horizontal
        if (moveStartX != moveEndX) {
            if (movingRight) {
                x += moveSpeed;
                if (x >= moveEndX) {
                    x = moveEndX;
                    movingRight = false;
                }
            } else {
                x -= moveSpeed;
                if (x <= moveStartX) {
                    x = moveStartX;
                    movingRight = true;
                }
            }
        }
        
        // Movimento vertical
        if (moveStartY != moveEndY) {
            if (movingDown) {
                y += moveSpeed;
                if (y >= moveEndY) {
                    y = moveEndY;
                    movingDown = false;
                }
            } else {
                y -= moveSpeed;
                if (y <= moveStartY) {
                    y = moveStartY;
                    movingDown = true;
                }
            }
        }
        
        // Atualizar hitbox
        hitbox.setLocation(x, y);
    }

    // Atualizar plataforma quebrável
    private void updateBreakablePlatform() {
        if (isBroken) return;
        
        if (breakTimer > 0) {
            breakTimer--;
            if (breakTimer <= 0) {
                isBroken = true;
                // Criar efeito de quebra
                createBreakEffect();
            }
        }
    }

    // Criar efeito visual de quebra
    private void createBreakEffect() {
        System.out.println("Plataforma quebrou em: " + x + ", " + y);
    }

    public void draw(Graphics2D g2d) {
        if (useSprites) {
            drawWithSprites(g2d);
        } else {
            drawFallback(g2d);
        }
    }

    /**
     * Renderização usando sprites
     */
    private void drawWithSprites(Graphics2D g2d) {
        try {
            if (isAnimated && currentAnimation != null) {
                // Renderizar plataforma animada
                BufferedImage currentFrame = currentAnimation.getCurrentFrame();
                if (currentFrame != null) {
                    spriteRenderer.renderSprite(g2d, currentFrame, x, y, 1, false);
                } else {
                    drawFallback(g2d);
                }

            } else if (staticSprite != null) {
                // Renderizar sprite estático
                spriteRenderer.renderSprite(g2d, staticSprite, x, y, 1, false);

            } else {
                // Fallback se não tem sprite
                drawFallback(g2d);
            }

            // Adicionar efeitos visuais baseados no tipo
            drawPlatformEffects(g2d);

        } catch (Exception e) {
            System.err.println("Erro ao renderizar sprite da plataforma: " + e.getMessage());
            drawFallback(g2d);
        }
    }

    /**
     * Renderização de fallback melhorada
     */
    private void drawFallback(Graphics2D g2d) {
        // Cores baseadas no tipo
        Color platformColor = getPlatformColor();
        Color borderColor = getBorderColor();

        // Renderizar a plataforma
        g2d.setColor(platformColor);
        g2d.fillRect(x, y, width, height);

        // Borda da plataforma
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);

        // Padrão específico do tipo
        drawTypePattern(g2d);

        // Efeitos visuais baseados no tipo
        drawPlatformEffects(g2d);
    }

    /**
     * Retorna cor baseada no tipo da plataforma
     */
    private Color getPlatformColor() {
        // Verificar se estamos na fase infinita (baseado na coordenada X)
        boolean isInfinitePhase = x > 3000; // Após as 3 fases iniciais
        
        if (isInfinitePhase) {
            return getInfinitePhasePurpleColor();
        }
        
        switch (type) {
            case GROUND: return new Color(101, 67, 33);      // Marrom terra
            case BRICK: return new Color(139, 69, 19);       // Marrom tijolo
            case PIPE: return new Color(34, 139, 34);        // Verde cano
            case CLOUD: return new Color(248, 248, 255);     // Branco nuvem
            case MOVING: return new Color(255, 215, 0);      // Dourado
            case BREAKABLE: return new Color(160, 82, 45);   // Marrom claro
            case ICE: return new Color(173, 216, 230);       // Azul gelo
            case BOUNCY: return new Color(255, 20, 147);     // Rosa vibrante
            case ONE_WAY: return new Color(50, 205, 50);     // Verde lima
            default: return Color.DARK_GRAY;
        }
    }
    
    /**
     * Cores temáticas roxo futurista para a fase infinita
     */
    private Color getInfinitePhasePurpleColor() {
        switch (type) {
            case GROUND: return new Color(75, 0, 130);       // Índigo profundo
            case BRICK: return new Color(106, 90, 205);      // Slate azul roxo
            case PIPE: return new Color(138, 43, 226);       // Azul violeta
            case CLOUD: return new Color(186, 85, 211);      // Orquídea média
            case MOVING: return new Color(148, 0, 211);      // Violeta escuro (pulsa)
            case BREAKABLE: return new Color(147, 112, 219); // Púrpura médio
            case ICE: return new Color(221, 160, 221);       // Ameixa clara
            case BOUNCY: return new Color(255, 0, 255);      // Magenta vibrante
            case ONE_WAY: return new Color(153, 50, 204);    // Roxo escuro
            default: return new Color(72, 61, 139);          // Cinza ardósia escuro
        }
    }

    /**
     * Retorna cor da borda baseada no tipo
     */
    private Color getBorderColor() {
        // Verificar se estamos na fase infinita
        boolean isInfinitePhase = x > 3000;
        
        if (isInfinitePhase) {
            return getInfinitePhasePurpleBorder();
        }
        
        switch (type) {
            case GROUND: return new Color(80, 50, 20);       // Marrom escuro
            case BRICK: return new Color(101, 67, 33);       // Marrom médio
            case PIPE: return new Color(0, 100, 0);          // Verde escuro
            case CLOUD: return new Color(200, 200, 200);     // Cinza claro
            case MOVING: return new Color(255, 165, 0);      // Laranja
            case BREAKABLE: return new Color(139, 69, 19);   // Marrom tijolo
            case ICE: return new Color(135, 206, 235);       // Azul céu
            case BOUNCY: return new Color(255, 105, 180);    // Rosa quente
            case ONE_WAY: return new Color(34, 139, 34);     // Verde floresta
            default: return Color.BLACK;
        }
    }
    
    /**
     * Bordas temáticas roxo futurista para a fase infinita
     */
    private Color getInfinitePhasePurpleBorder() {
        switch (type) {
            case GROUND: return new Color(25, 0, 51);        // Índigo muito escuro
            case BRICK: return new Color(75, 0, 130);        // Índigo
            case PIPE: return new Color(102, 0, 153);        // Roxo escuro
            case CLOUD: return new Color(128, 0, 128);       // Púrpura
            case MOVING: return new Color(255, 20, 147);     // Rosa shocking (brilhante)
            case BREAKABLE: return new Color(106, 90, 205);  // Slate azul
            case ICE: return new Color(186, 85, 211);        // Orquídea
            case BOUNCY: return new Color(255, 105, 180);    // Rosa quente
            case ONE_WAY: return new Color(72, 61, 139);     // Cinza ardósia escuro
            default: return new Color(25, 25, 112);          // Azul meia-noite
        }
    }

    /**
     * Desenha padrões específicos do tipo na renderização fallback
     */
    private void drawTypePattern(Graphics2D g2d) {
        switch (type) {
            case BRICK:
                // Desenhar padrão de tijolos
                g2d.setColor(new Color(0, 0, 0, 100));
                for (int i = 0; i < height; i += 16) {
                    for (int j = (i / 16) % 2 * 16; j < width; j += 32) {
                        g2d.drawLine(x + j, y + i, x + j + 16, y + i);
                    }
                }
                break;

            case PIPE:
                // Desenhar detalhes do cano
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.drawLine(x + 2, y + 2, x + width - 2, y + 2);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawLine(x + 2, y + height - 2, x + width - 2, y + height - 2);
                break;

            default:
                break;
        }
    }

    /**
     * Adiciona efeitos visuais específicos do tipo de plataforma
     */
    private void drawPlatformEffects(Graphics2D g2d) {
        // Verificar se estamos na fase infinita
        boolean isInfinitePhase = x > 3000;
        
        if (isInfinitePhase) {
            drawInfinitePhasePurpleEffects(g2d);
            return;
        }
        
        switch (type) {
            case MOVING:
                // Adicionar brilho nas bordas
                g2d.setColor(new Color(255, 255, 0, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x-1, y-1, width+2, height+2);
                break;

            case CLOUD:
                // Adicionar suave sombra embaixo
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillOval(x + 5, y + height, width - 10, 5);
                break;

            case BRICK:
                // Adicionar linha de juntas verticais extras
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.setStroke(new BasicStroke(1));
                for (int i = 32; i < width; i += 32) {
                    g2d.drawLine(x + i, y, x + i, y + height);
                }
                break;

            case BREAKABLE:
                // Adicionar rachaduras se está prestes a quebrar
                if (breakTimer > 0) {
                    g2d.setColor(new Color(255, 0, 0, 100 + (breakTimer * 5)));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(x + 5, y + 5, x + width - 5, y + height - 5);
                    g2d.drawLine(x + width - 5, y + 5, x + 5, y + height - 5);
                }
                break;

            case ICE:
                // Adicionar brilho gelado
                g2d.setColor(new Color(200, 255, 255, 80));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(x, y, width, height);
                break;

            case BOUNCY:
                // Adicionar efeito de mola
                g2d.setColor(new Color(255, 100, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x + 2, y + 2, width - 4, height - 4);
                break;

            case ONE_WAY:
                // Adicionar seta indicando direção
                g2d.setColor(new Color(100, 255, 100, 150));
                g2d.setStroke(new BasicStroke(2));
                int arrowY = y + height/2;
                g2d.drawLine(x + 5, arrowY, x + width - 5, arrowY);
                g2d.drawLine(x + width - 10, arrowY - 3, x + width - 5, arrowY);
                g2d.drawLine(x + width - 10, arrowY + 3, x + width - 5, arrowY);
                break;

            default:
                break;
        }
    }

    // MÉTODOS DE CONTROLE

    /**
     * Ativa/desativa o uso de sprites
     */
    public void setUseSprites(boolean useSprites) {
        this.useSprites = useSprites;
    }

    /**
     * Verifica se a plataforma está usando sprites
     */
    public boolean isUsingSprites() {
        return useSprites && (staticSprite != null || currentAnimation != null);
    }

    /**
     * Efeitos visuais especiais para a fase infinita roxo futurista
     */
    private void drawInfinitePhasePurpleEffects(Graphics2D g2d) {
        // Efeito de pulsação baseado no tempo
        long time = System.currentTimeMillis();
        float pulse = (float)(Math.sin(time * 0.01) * 0.5 + 0.5); // Oscila entre 0 e 1
        
        switch (type) {
            case MOVING:
                // Brilho roxo pulsante nas bordas
                int pulseAlpha = (int)(150 * pulse + 50);
                g2d.setColor(new Color(255, 0, 255, pulseAlpha));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(x-2, y-2, width+4, height+4);
                
                // Partículas de energia
                g2d.setColor(new Color(186, 85, 211, 100));
                for (int i = 0; i < 3; i++) {
                    int px = x + (int)(Math.random() * width);
                    int py = y - 5 - (int)(Math.random() * 10);
                    g2d.fillOval(px, py, 2, 2);
                }
                break;

            case CLOUD:
                // Aura roxa suave
                g2d.setColor(new Color(186, 85, 211, 30));
                g2d.fillOval(x - 10, y - 5, width + 20, height + 10);
                
                // Brilho nas bordas
                g2d.setColor(new Color(221, 160, 221, 80));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(x, y, width, height);
                break;

            case BOUNCY:
                // Anel de energia magenta pulsante
                int ringAlpha = (int)(200 * pulse + 55);
                g2d.setColor(new Color(255, 0, 255, ringAlpha));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x - 5, y - 5, width + 10, height + 10);
                
                // Núcleo brilhante
                g2d.setColor(new Color(255, 20, 147, 120));
                g2d.fillOval(x + width/4, y + height/4, width/2, height/2);
                break;

            case PIPE:
                // Circuitos de energia
                g2d.setColor(new Color(138, 43, 226, 150));
                g2d.setStroke(new BasicStroke(1));
                for (int i = 0; i < width; i += 20) {
                    g2d.drawLine(x + i, y, x + i, y + height);
                }
                break;

            case BRICK:
                // Juntas energizadas
                g2d.setColor(new Color(106, 90, 205, 100));
                g2d.setStroke(new BasicStroke(1));
                for (int i = 0; i < height; i += 16) {
                    for (int j = (i / 16) % 2 * 16; j < width; j += 32) {
                        g2d.drawLine(x + j, y + i, x + j + 16, y + i);
                    }
                }
                break;

            case GROUND:
                // Base energizada com padrão de circuito
                g2d.setColor(new Color(75, 0, 130, 80));
                for (int i = 0; i < width; i += 40) {
                    g2d.drawLine(x + i, y, x + i + 20, y + height);
                }
                break;

            default:
                // Brilho roxo básico para outros tipos
                g2d.setColor(new Color(153, 50, 204, 60));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(x, y, width, height);
                break;
        }
        
        // Efeito de "energia" flutuante para todas as plataformas na fase infinita
        if (Math.random() < 0.1) { // 10% de chance por frame
            g2d.setColor(new Color(255, 0, 255, 50));
            int sparkX = x + (int)(Math.random() * width);
            int sparkY = y - (int)(Math.random() * 20);
            g2d.fillOval(sparkX, sparkY, 3, 3);
        }
    }

    /**
     * Cleanup quando a plataforma é destruída
     */
    public void cleanup() {
        // DESBLOQUEAR ESTA SEÇÃO:
        if (animationManager != null && platformId != null) {
            // Limpeza de animações - não há método específico no AnimationManager
            // As animações são gerenciadas globalmente
        }
    }

    // === GETTERS ORIGINAIS (SEM MODIFICAÇÃO) ===

    public PlatformType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public PlatformType getPlatformType() {
        return type;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public void updatePosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.hitbox.setLocation(newX, newY);
    }

    // MÉTODOS DE INTERAÇÃO COM O PLAYER

    /**
     * Verifica se o player pode colidir com esta plataforma
     */
    public boolean canCollideWithPlayer(org.example.objects.Player player) {
        if (isBroken) return false;
        
        switch (type) {
            case ONE_WAY:
                // Só colide se o player estiver acima da plataforma
                return player.y + player.height <= y + 5; // 5px de tolerância
            default:
                return true;
        }
    }

    /**
     * Aplica efeitos especiais quando o player pisa na plataforma
     */
    public void onPlayerLanded(org.example.objects.Player player) {
        switch (type) {
            case BREAKABLE:
                if (!isSteppedOn) {
                    isSteppedOn = true;
                    breakTimer = BREAK_DELAY;
                }
                break;
            case BOUNCY:
                // Aumentar velocidade de pulo do player
                player.velocityY *= -bounceMultiplier;
                break;
            case ICE:
                // Reduzir atrito do player
                player.velocityX *= (1.0f - iceFriction);
                break;
            case GROUND:
            case BRICK:
            case CLOUD:
            case MOVING:
            case ONE_WAY:
            case PIPE:
            default:
                // Não têm efeitos especiais
                break;
        }
    }

    /**
     * Aplica efeitos especiais quando o player está em contato com a plataforma
     */
    public void onPlayerContact(org.example.objects.Player player) {
        switch (type) {
            case ICE:
                // Aplicar atrito reduzido continuamente
                player.velocityX *= (1.0f - iceFriction * 0.1f);
                break;
            case GROUND:
            case BRICK:
            case CLOUD:
            case MOVING:
            case BREAKABLE:
            case BOUNCY:
            case ONE_WAY:
            case PIPE:
            default:
                // Não têm efeitos especiais
                break;
        }
    }

    /**
     * Verifica se a plataforma está quebrada
     */
    public boolean isBroken() {
        return isBroken;
    }

    // Método removido - playerAbove não é mais utilizado

    /**
     * Obtém o multiplicador de pulo (para bouncy platforms)
     */
    public float getBounceMultiplier() {
        return bounceMultiplier;
    }

    /**
     * Obtém o atrito do gelo (para ice platforms)
     */
    public float getIceFriction() {
        return iceFriction;
    }
}