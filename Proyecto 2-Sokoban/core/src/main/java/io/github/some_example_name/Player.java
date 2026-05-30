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
    /*
            (0,+1y)
            |
            |
            |
            |
            |
            |
            |
            |
            |
       (-1,0)x+--------------------------------------- (+1,0)x
            (0,-1)y
    */
    public void tecladoInput(char[][] level){
        int dirX=0,dirY=0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            dirY+=1;
            //y+=Constantes.TILE_SIZE;
            this.setCabeza(1, 3);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            dirY-=1;            
            //y-=Constantes.TILE_SIZE;
            this.setCabeza(2, 0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            dirX-=1;            
            //x-=Constantes.TILE_SIZE;
            this.setCabeza(1, 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            dirX+=1;
            //x+=Constantes.TILE_SIZE;
            this.setCabeza(1, 2);
        }
        if(dirX!=0||dirY!=0){
            int posX= x/Constantes.TILE_SIZE;
            int posY= (level.length-1)-(y/Constantes.TILE_SIZE);
            
            //prediccion de las casillas segun la direccion
            int predictCol= posX+dirX;
            int predictFila= posY-dirY;
            
            if(predictFila>=0 &&predictFila<level.length && predictCol>=0 && predictCol<level[predictFila].length){
                char destino= level[predictFila][predictCol];
                if(destino=='a'|| destino=='x' || destino =='p'){
                    x+= dirX *Constantes.TILE_SIZE;
                    y+= dirY *Constantes.TILE_SIZE;
                }else if(destino=='b' || destino=='B'){
                    int cajaFila= predictFila-dirY;
                    int cajaCol= predictCol+dirX;
                    
                    if(cajaFila >=0 && cajaFila<level.length&& cajaCol>=0 && cajaCol<level[cajaFila].length){
                        char cajaDestino=level[cajaFila][cajaCol];
                        if(cajaDestino=='a' || cajaDestino=='p'|| cajaDestino=='x'){
                            if(destino =='B')
                                level[predictFila][predictCol]='x';
                            else
                                level[predictFila][predictCol]='a';
                            
                            if(cajaDestino =='x'){
                                level[cajaFila][cajaCol]='B';
                                System.out.println("Una caja en su lugar");
                            }else
                                level[cajaFila][cajaCol]='b';
                            
                            
                            this.x += dirX * Constantes.TILE_SIZE;
                            this.y += dirY * Constantes.TILE_SIZE;
                            
                                
                        }
                    }
                }
            }
            
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
