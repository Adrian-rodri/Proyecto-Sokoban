package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
    WALL(0,5),PISO(2,0),META(1,5),AIRE(2,3),BOX_EN_SU_LUGAR(2,2)
    ,BOX(1,1),BOX_1(0,3),BOX_2(0,4),Meta_1(1,4),Meta_2(1,3);
    
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
    public static void dispose(){
        if (sheetTiles!= null){
            sheetTiles.dispose();
            sheetTiles= null;
        }
        for (TileType t :values())
            t.textura= null;
        
}
    
}

