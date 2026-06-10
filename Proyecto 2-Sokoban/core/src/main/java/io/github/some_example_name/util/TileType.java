package io.github.some_example_name.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
    WALL(0,0),PISO(0,1),META(0,2),AIRE(0,5),BOX_EN_SU_LUGAR(0,4)
    ,BOX(0,3),BOX_1(0,3),BOX_2(0,4),Meta_1(1,4),Meta_2(1,3);
    
    private static int PIXELS_TILE=32;
    
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
    public static void dispose(){
        if (sheetTiles!= null){
            sheetTiles.dispose();
            sheetTiles= null;
        }
        for (TileType t :values())
            t.textura= null;
        
}
    
}

