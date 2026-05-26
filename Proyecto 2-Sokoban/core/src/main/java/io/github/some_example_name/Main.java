package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image,playerSheet,sheetTiles;
    private TextureRegion wall,box,objetivo,piso;
    ShapeRenderer shape;
    private Player player;
    OrthographicCamera camera;
    char[][] level;
    boolean initPlayer=false;
    

    @Override
    public void create() {
        camera= new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        image = new Texture("fondo.png");
        playerSheet=new Texture("playerSheet.png");
        sheetTiles=new Texture("sheetTiles.png");
        wall=new TextureRegion(sheetTiles,0,2*80,80,80);
        box=new TextureRegion(sheetTiles,1*80,1*80,80,80);
        piso=new TextureRegion(sheetTiles,2*80,0*80,80,80);
        objetivo=new TextureRegion(sheetTiles,3*80,1*80,80,80);
        shape=new ShapeRenderer();
        player= new Player(0,0);
        player.cargarSprites(playerSheet, playerSheet, playerSheet);
        player.setCabeza(0, 0);
        
        //level de prueba
        /*
        a= aire
        w=pared
        b=box
        x=objetivo
        p=player
        */
        level= new char[][]{
            {'a','a','w','w','w','a','a','a'},
            {'a','a','w','x','w','a','a','a'},
            {'a','a','w','a','w','w','w','w'},
            {'w','w','w','b','a','b','x','w'},
            {'w','x','a','b','p','w','w','w'},
            {'w','w','w','w','b','w','a','a'},
            {'a','a','a','w','x','w','a','a'},
            {'a','a','a','w','w','w','a','a'}
        };
        
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1f); //resetea la pantalla a negro cda frame
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        
        batch.begin();//Trabajar texturas entre aqui
        dibujarTexturas(batch);
        batch.end();//y aqui
        //Logica
        logic();
        //Player temporal con figura
//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        player.dibujarPlayer(shape);
//        shape.end();

    }
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth=width;
        camera.viewportHeight=height;
        camera.update();
    }
    public void dibujarTexturas(SpriteBatch batch){ //dibujar todas las texturas aqui
        batch.draw(image, 0, 0,832,640);
        
        
        for(int i=0;i<level.length;i++){
            for(int j=0;j<level[i].length;j++){
                int yPos= (level.length-1-i)*Constantes.TILE_SIZE;
                switch(level[i][j]){
                    case 'a':
                        batch.draw(piso, j*Constantes.TILE_SIZE, yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
                        break;
                    case 'w':
                        batch.draw(wall, j*Constantes.TILE_SIZE,yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
                        break;
                    case 'b':
                        batch.draw(box, j*Constantes.TILE_SIZE, yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
                        break;
                    case 'x':
                        batch.draw(objetivo,j*Constantes.TILE_SIZE, yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
                        break;
                    case 'p':
                        batch.draw(piso, j*Constantes.TILE_SIZE, yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
                        if(!initPlayer){
                            player.x=j*Constantes.TILE_SIZE;
                            player.y=yPos;
                            initPlayer=true;
                        }
                        break;
                }
            }
        }
        player.dibujarPlayer(batch);
    }
    public void logic(){//aqui se configura toda la logicaa del juego
        player.tecladoInput();
    }

    @Override
    public void dispose(){
        batch.dispose();
        image.dispose();
        shape.dispose();
    }
    
}
