package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author adria
 */
public class Player extends Entity{
    private Texture sheetCabeza,sheetTorso,sheetAccesorio;
    
    
    protected TextureRegion playerSprite;
    public Player(int x, int y) {
        super(x, y);
    }
    public void tecladoInput(char[][] level){
        int dirX=0,diryY=0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            y+=Constantes.TILE_SIZE;
            
            this.setCabeza(1, 3);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            y-=Constantes.TILE_SIZE;
            this.setCabeza(2, 0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            x-=Constantes.TILE_SIZE;
            this.setCabeza(1, 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            x+=Constantes.TILE_SIZE;
            this.setCabeza(1, 2);
        }
    }
    /* 
    public void tecladoInput(char[][] level) {
    int dirX = 0;
    int dirY = 0;

    // 1. Identificar la dirección del intento de movimiento
    if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        dirY = 1;
        this.setCabeza(1, 3);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        dirY = -1;
        this.setCabeza(2, 0);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        dirX = -1;
        this.setCabeza(1, 1);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        dirX = 1;
        this.setCabeza(1, 2);
    }

    // Si hubo intención de movimiento
    if (dirX != 0 || dirY != 0) {
        
        // Posición actual del jugador en la matriz
        int jugadorCol = this.x / Constantes.TILE_SIZE;
        int jugadorFila = (level.length - 1) - (this.y / Constantes.TILE_SIZE);

        // CASILLA 1: Donde el jugador quiere ir (y donde podría estar la caja)
        int destinoCol = jugadorCol + dirX;
        // Invertimos dirY porque en la matriz 'Arriba' es restar filas
        int destinoFila = jugadorFila - dirY; 

        // Validar que el destino esté dentro del mapa
        if (destinoFila >= 0 && destinoFila < level.length && destinoCol >= 0 && destinoCol < level[destinoFila].length) {
            
            char casillaDestino = level[destinoFila][destinoCol];

            // CASO A: Es suelo libre (piso 'a' o meta 'x')
            if (casillaDestino == 'a' || casillaDestino == 'x'||casillaDestino=='p') {
                this.x += dirX * Constantes.TILE_SIZE;
                this.y += dirY * Constantes.TILE_SIZE;
            }
            
            // CASO B: ¡Hay una CAJA! ('b')
            else if (casillaDestino == 'b') {
                
                // CASILLA 2: La posición DETRÁS de la caja
                int detrasCajaCol = destinoCol + dirX;
                int detrasCajaFila = destinoFila - dirY;

                // Validar que la casilla detrás de la caja esté dentro del mapa
                if (detrasCajaFila >= 0 && detrasCajaFila < level.length && detrasCajaCol >= 0 && detrasCajaCol < level[detrasCajaFila].length) {
                    
                    char casillaDetras = level[detrasCajaFila][detrasCajaCol];

                    // La caja SÓLO se mueve si atrás hay piso ('a') o una meta ('x')
                    if (casillaDetras == 'a' || casillaDetras == 'x' ) {
                        
                        // 1. Actualizar la matriz para la nueva posición de la caja
                        level[detrasCajaFila][detrasCajaCol] = 'b'; 
                        
                        // 2. Liberar la casilla vieja de la caja. 
                        // Ojo: Si el jugador estaba en un 'p' inicial, asumimos que abajo hay piso.
                        // Lo ideal es restaurar si era una meta ('x') o piso ('a').
                        // Por ahora, si la caja estaba sobre una meta, al quitarla vuelve a ser meta.
                        // Para este ejemplo básico, si no recuerdas qué había, lo dejamos en piso 'a'.
                        level[destinoFila][destinoCol] = 'a'; 

                        // 3. Como la caja se movió con éxito, ¡el jugador también avanza!
                        this.x += dirX * Constantes.TILE_SIZE;
                        this.y += dirY * Constantes.TILE_SIZE;
                        
                        System.out.println("¡Caja empujada!");
                    } else {
                        System.out.println("No se puede empujar: Hay un muro u otra caja detrás.");
                    }
                }
            }
        }
    }
}
    */
    public void dibujarPlayer(ShapeRenderer shape){//metodo temporal-> dibujar un cuadrado en lugar de imagen
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }
    public void dibujarPlayer(SpriteBatch batch){
        if(playerSprite!=null)
        batch.draw(playerSprite, x, y,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
    }
    public void setCabeza(int col, int fila){
        playerSprite= new TextureRegion(sheetCabeza,col*32,fila*32,32,32);
    }
    public void setTorso(int col, int fila){
     //Vacio por momento, falta crear el spriteSheet
    }
    public void setAccesorio(int col, int fila){
     //Vacio por momento, falta crear el spriteSheet
    }
    public void cargarSprites(Texture cabeza, Texture torso, Texture accesorio){
        this.sheetCabeza=cabeza;
        this.sheetTorso=torso;
        this.sheetAccesorio=accesorio;
    }
}
