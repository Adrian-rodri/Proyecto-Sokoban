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
    int contador=4;
    
    protected TextureRegion playerSprite;
    public Player(int x, int y) {
        super(x, y);
    }
    public void tecladoInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            y+=Constantes.TILE_SIZE;
            
            this.setCabeza(1, 3);
//            if(contador>0){
//                Constantes.TILE_SIZE-=6;
//                contador--;
//            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            y-=Constantes.TILE_SIZE;
            this.setCabeza(2, 0);
//              Constantes.TILE_SIZE+=6;
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
