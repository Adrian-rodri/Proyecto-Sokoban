package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
    WALL(0,2),PISO(2,0),META(1,4),BOX(1,1);
    
    private static int PIXELS_TILE=80;
    
    private static Texture sheetTiles= null;
    private TextureRegion textura;
    private int fila, columna;
    
    TileType(int fila, int col){
        this.fila=fila;
        this.columna=col;
    }
    public TextureRegion getTexture(){
        if(textura==null){
            if(sheetTiles==null)
                sheetTiles= new Texture("sheetTiles.png");
            int x= columna*PIXELS_TILE;
            int y= fila*PIXELS_TILE;
            textura= new TextureRegion(sheetTiles,x,y,PIXELS_TILE,PIXELS_TILE);
        }
        return textura;
    }
    
}

