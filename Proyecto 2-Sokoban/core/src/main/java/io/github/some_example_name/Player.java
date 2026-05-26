package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author adria
 */
public class Player extends Entity{
    private Texture sheetCabeza;
    private Texture sheetTorso;
    private Texture sheetAccesorio;
    public Player(int x, int y) {
        super(x, y);
    }
    public void tecladoInput(){
   
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP))
            y+=Constantes.TILE_SIZE;
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            y-=Constantes.TILE_SIZE;
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            x-=Constantes.TILE_SIZE;
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            x+=Constantes.TILE_SIZE;
    }
    public void dibujarPlayer(ShapeRenderer shape){
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }
    public void setCabeza(int col, int fila){
        //Vacio por momento, falta crear el spriteSheet
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
