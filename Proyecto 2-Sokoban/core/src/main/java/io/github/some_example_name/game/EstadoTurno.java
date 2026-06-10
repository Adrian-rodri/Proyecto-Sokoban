
package io.github.some_example_name.game;

/**
 *
 * @author adria
 */
public class EstadoTurno {
    public char[][] matriz;
    public int playerX;
    public int playerY;
    public int spriteCol;
    public int spriteFila;

    public EstadoTurno(char[][] matriz, int playerX, int playerY,int spriteCol,int spriteFila) {
        this.matriz = matriz;
        this.playerX = playerX;
        this.playerY = playerY;
        this.spriteCol=spriteCol;
        this.spriteFila=spriteFila;
    }
}
