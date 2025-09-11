package org.example.ui;

import java.awt.Color;

public class GameConfig {

    // === CONFIGURAÇÕES DA TELA ===
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int TILE_SIZE = 32;

    // === CONFIGURAÇÕES DE FÍSICA ===
    public static final double GRAVITY = 0.5;
    public static final double TERMINAL_VELOCITY = 15.0;
    public static final double FRICTION = 0.8;

    // === CONFIGURAÇÕES DO PLAYER ===
    public static final double PLAYER_SPEED = 5.0;
    public static final double PLAYER_JUMP_STRENGTH = 12.0;
    public static final double PLAYER_DASH_SPEED = 15.0;
    public static final int PLAYER_MAX_ENERGY = 100;
    public static final int PLAYER_DASH_COST = 20;
    public static final int PLAYER_ENERGY_RECHARGE_RATE = 2;

    // === CORES DO PLAYER ===
    public static final Color PRIMARY_COLOR = new Color(0, 255, 255);    // Cyan
    public static final Color SECONDARY_COLOR = new Color(255, 0, 255);  // Magenta
    public static final Color ACCENT_COLOR = new Color(255, 255, 0);     // Yellow

    // === CONFIGURAÇÕES DE MUNDO ===
    public static final int WORLD_WIDTH = 5000;
    public static final int WORLD_HEIGHT = 2000;
    public static final int GROUND_LEVEL = 600;

    // === CONFIGURAÇÕES DE CÂMERA ===
    public static final double CAMERA_SMOOTHING = 0.1;
    public static final int CAMERA_OFFSET_Y = 200;
    public static final int CAMERA_BOUNDARY_LEFT = 100;
    public static final int CAMERA_BOUNDARY_RIGHT = WORLD_WIDTH - SCREEN_WIDTH - 100;

    // === CONFIGURAÇÕES DE INIMIGOS ===
    public static final double ENEMY_SPEED = 2.0;
    public static final int ENEMY_PATROL_DISTANCE = 100;
    public static final int ENEMY_SIZE = 32;

    // === CONFIGURAÇÕES DE ORBS ===
    public static final int ORB_RESPAWN_TIME = 300; // frames (5 segundos a 60 FPS)
    public static final int ORB_POINTS = 10;
    public static final int ORB_ENERGY_RESTORE = 25;
    public static final int ORB_SIZE = 16;
    public static final double ORB_ATTRACTION_RANGE = 100.0;

    // === CONFIGURAÇÕES DE PLATAFORMAS ===
    public static final Color PLATFORM_COLOR = new Color(100, 100, 100);
    public static final int PLATFORM_HEIGHT = 16;

    // === CONFIGURAÇÕES DE PONTUAÇÃO ===
    public static final int ENEMY_DEFEAT_POINTS = 50;
    public static final int HEIGHT_BONUS_MULTIPLIER = 1;
    public static final int SURVIVAL_BONUS_INTERVAL = 300; // frames
    public static final int SURVIVAL_BONUS_POINTS = 5;

    // === FPS E TIMING ===
    public static final int TARGET_FPS = 60;
    public static final double NANOSECONDS_PER_FRAME = 1000000000.0 / TARGET_FPS;

    // === CONFIGURAÇÕES DE HUD ===
    public static final Color HUD_BACKGROUND = new Color(0, 0, 0, 150);
    public static final Color HUD_TEXT_COLOR = Color.WHITE;
    public static final Color HUD_ACCENT_COLOR = new Color(0, 255, 255);

    // === CONFIGURAÇÕES DE ANIMAÇÃO E SPRITES ===
    public static final boolean ANIMATIONS_ENABLED = false;
    public static final float ANIMATION_SPEED_MULTIPLIER = 1.0f;
    public static final int SPRITE_SCALE = 1; // Multiplicador de escala para sprites
    
    // Caminhos para sprite sheets
    public static final String SPRITES_PATH = "sprites/";
    public static final String PLAYER_SPRITES_PATH = SPRITES_PATH + "sprites/player/";
    public static final String ENEMY_SPRITES_PATH = SPRITES_PATH + "enemies/";
    public static final String OBJECT_SPRITES_PATH = SPRITES_PATH + "objects/";
    public static final String WORLD_SPRITES_PATH = SPRITES_PATH + "world/";
    public static final String EFFECTS_SPRITES_PATH = SPRITES_PATH + "effects/";
    
    // Configurações de FPS de animação
    public static final float PLAYER_ANIMATION_FPS = 12.0f;
    public static final float ENEMY_ANIMATION_FPS = 8.0f;
    public static final float ORB_ANIMATION_FPS = 10.0f;
    public static final float EFFECT_ANIMATION_FPS = 15.0f;
    
    // Tamanhos de sprites
    public static final int PLAYER_SPRITE_WIDTH = 32;
    public static final int PLAYER_SPRITE_HEIGHT = 48;
    public static final int ENEMY_SPRITE_WIDTH = 30;
    public static final int ENEMY_SPRITE_HEIGHT = 40;
    public static final int FLYING_ENEMY_SPRITE_WIDTH = 35;
    public static final int FLYING_ENEMY_SPRITE_HEIGHT = 25;
    public static final int ORB_SPRITE_WIDTH = 24;
    public static final int ORB_SPRITE_HEIGHT = 24;
    public static final int PLATFORM_SPRITE_WIDTH = 64;
    public static final int PLATFORM_SPRITE_HEIGHT = 32;
    public static final int EFFECT_SPRITE_WIDTH = 32;
    public static final int EFFECT_SPRITE_HEIGHT = 32;

    // === CONFIGURAÇÕES DE DEBUG ===
    public static final boolean DEBUG_MODE = false;
    public static final boolean SHOW_HITBOXES = false;
    public static final boolean SHOW_FPS = true;
    public static final boolean SHOW_ANIMATION_INFO = false; // Mostrar informações de animação no debug
}