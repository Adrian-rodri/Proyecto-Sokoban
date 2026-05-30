package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private Texture image, playerSheet, sheetTiles;
    private TextureRegion wall, box, objetivo, piso;
    private ShapeRenderer shape;
    private Player player;
    private OrthographicCamera camera;
    private char[][] level;
    private boolean initPlayer= false;
    TileType tiposTiles;

    public GameScreen(Main game){
        this.game= game;
    }

    @Override
    public void show(){
        camera= new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch= new SpriteBatch();
        image= new Texture("fondo.png");
        playerSheet= new Texture("playerSheet.png");
        sheetTiles= new Texture("sheetTiles.png");
        wall= new TextureRegion(sheetTiles, 0,      2*80, 80, 80);
        box= new TextureRegion(sheetTiles, 1*80,   1*80, 80, 80);
        piso= new TextureRegion(sheetTiles, 2*80,   0,    80, 80);
        objetivo= new TextureRegion(sheetTiles, 3*80,   1*80, 80, 80);
        shape= new ShapeRenderer();
        player= new Player(0, 0);
        player.cargarSprites(playerSheet, playerSheet, playerSheet);
        player.setCabeza(0, 0);
        tiposTiles= TileType.WALL;
        level= new char[][]{
            {'a','a','w','w','w','a','a','a'},
            {'a','a','w','x','w','a','a','a'},
            {'a','a','a','a','a','w','w','w'},
            {'a','a','x','b','a','b','x','w'},
            {'a','x','x','b','p','w','w','w'},
            {'a','w','w','w','b','w','a','a'},
            {'a','a','a','w','x','w','a','a'},
            {'a','a','a','w','w','w','a','a'}
        };
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        batch.begin();
        dibujarTexturas(batch);
        batch.end();

        logic();
    }

    private void dibujarTexturas(SpriteBatch batch){
        
        batch.draw(image, 0, 0, 832, 640);
        for (int i=0; i<level.length;i++) {
            for (int j= 0; j<level[i].length; j++) {
                int yPos=(level.length-1-i)*Constantes.TILE_SIZE;
                switch(level[i][j]){
                    case 'a': 
                        tiposTiles=TileType.PISO;
                        break;
                    case 'w':
                        tiposTiles=TileType.WALL;
                        break;
                    case 'B':
                    case'b': 
                        tiposTiles=TileType.BOX;
                        break;
                    case 'x': 
                        tiposTiles=TileType.META;
                        break;
                    case 'p':
                        tiposTiles=TileType.PISO;
                        if(!initPlayer){
                            player.x= j * Constantes.TILE_SIZE;
                            player.y= yPos;
                            initPlayer= true;
                        }
                        break;
                }
                TextureRegion texutra= tiposTiles.getTexture();
                batch.draw(texutra, j*Constantes.TILE_SIZE, yPos,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
            }
        }
        player.dibujarPlayer(batch);
    }

    private void logic() {
        player.tecladoInput(level);
        if(nivelCompleto(level))
            System.out.println("Ganastes");
    }

    @Override
    public void resize(int w, int h) {
        camera.viewportWidth  = w;
        camera.viewportHeight = h;
        camera.update();
    }
    public boolean nivelCompleto(char[][] level) {
        for (char[] fila : level)
            for (char c : fila)
                if (c == 'b') return false; // queda una caja sin posición
        return true;
}

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        tiposTiles.dispose();
        batch.dispose();
        image.dispose();
        shape.dispose();
        sheetTiles.dispose();
        playerSheet.dispose();
    }
}