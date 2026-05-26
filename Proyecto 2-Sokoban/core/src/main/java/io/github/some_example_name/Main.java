package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image,playerSheet;
    ShapeRenderer shape;
    private Player player;
    OrthographicCamera camera;
    char[][] level;
    

    @Override
    public void create() {
        camera= new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        image = new Texture("fondo.png");
        playerSheet=new Texture("playerSheet.png");
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
        player.dibujarPlayer(batch);
        for(int i=0;i<level.length;i++){
        }
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
