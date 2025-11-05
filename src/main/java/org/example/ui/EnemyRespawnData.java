package org.example.ui;

import org.example.objects.Enemy;

class EnemyRespawnData {
    public Enemy enemy;
    public long deathTime;
    public double originalX;
    public double originalY;
    public double patrolLeft;
    public double patrolRight;

    public EnemyRespawnData(Enemy enemy, double originalX, double originalY, double patrolLeft, double patrolRight) {
        this.enemy = enemy;
        this.deathTime = System.currentTimeMillis();
        this.originalX = originalX;
        this.originalY = originalY;
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
    }

    public boolean shouldRespawn() {
        return System.currentTimeMillis() - deathTime >= GamePanel.ENEMY_RESPAWN_DELAY;
    }
}