
package io.github.some_example_name;

/**
 *
 * @author adria
 */
public class EstadoTurno {
    char[][] matriz;
    int playerX;
    int playerY;
    int spriteCol;
    int spriteFila;

    EstadoTurno(char[][] matriz, int playerX, int playerY,int spriteCol,int spriteFila) {
        this.matriz = matriz;
        this.playerX = playerX;
        this.playerY = playerY;
        this.spriteCol=spriteCol;
        this.spriteFila=spriteFila;
    }
}
