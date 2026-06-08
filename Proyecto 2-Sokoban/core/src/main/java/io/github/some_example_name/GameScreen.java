package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private BitmapFont font;
    private Player player;
    private OrthographicCamera camera;
    public static boolean initPlayer= false;
    private TileType tiposTiles;
    private NivelManager nivelMng;
    private Nivel nivelActual;
    private boolean isGanado=false;
    private int numLevel;

    // ── HUD botones ───────────────────────────────────────────────────────────
    private static final int HUD_H  = 36;   // altura del HUD en pixels
    private static final int BTN_W  = 85;
    private static final int BTN_H  = 24;

    private Boton btnReiniciar, btnSalir;

    // ── Temporizador (hilo de concurrencia) ───────────────────────────────────
    private int movimientos = 0;
    private volatile double  tiempoSegundos = 0;
    private volatile boolean timerActivo    = false;
    private Thread hiloTimer;

    public GameScreen(Main game, int numLevel){
        this.game= game;
        this.numLevel=numLevel;
    }

    @Override
    public void show(){
        camera= new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch= new SpriteBatch();
        font = new BitmapFont();
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
        nivelMng= new NivelManager();
        nivelMng.cargar();
        nivelActual= nivelMng.getNivel(numLevel);
        initPlayer=false;

        crearBotonesHUD();
        iniciarTimer();
    }

    /** Crea/reposiciona los botones del HUD segun el tamano actual de pantalla. */
    private void crearBotonesHUD() {
        int sw = Gdx.graphics.getWidth();
        int sh = Gdx.graphics.getHeight();
        int by = sh - HUD_H + (HUD_H - BTN_H) / 2;

        btnReiniciar = new Boton(Textos.GAME_BTN_REINICIAR, sw - BTN_W * 2 - 10, by, BTN_W, BTN_H);
        btnSalir     = new Boton(Textos.GAME_BTN_SALIR,     sw - BTN_W - 2,      by, BTN_W, BTN_H);

        // Colores sutiles para el HUD
        btnReiniciar.setColorFondo(0.15f, 0.15f, 0.25f, 1f);
        btnReiniciar.setColorHover(0.25f, 0.25f, 0.45f, 1f);
        btnReiniciar.setColorBorde(0.40f, 0.40f, 0.65f, 1f);
        btnSalir.setColorFondo(0.20f, 0.12f, 0.12f, 1f);
        btnSalir.setColorHover(0.38f, 0.18f, 0.18f, 1f);
        btnSalir.setColorBorde(0.55f, 0.30f, 0.30f, 1f);
    }

    // ── Timer ─────────────────────────────────────────────────────────────────
    private void iniciarTimer() {
        timerActivo = true;
        hiloTimer   = new Thread(new Runnable() {
            @Override
            public void run() {
                long ultimo = System.currentTimeMillis();
                while (timerActivo) {
                    try {
                        Thread.sleep(100);
                        long ahora = System.currentTimeMillis();
                        tiempoSegundos += (ahora - ultimo) / 1000.0;
                        ultimo = ahora;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        hiloTimer.setDaemon(true);
        hiloTimer.start();
    }

    private void detenerTimer() {
        timerActivo = false;
        if (hiloTimer != null) hiloTimer.interrupt();
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

        dibujarHUD();
        logic();
    }

    private void dibujarTexturas(SpriteBatch batch){
        
        batch.draw(image, 0, 0, 832, 640);
        char[][] level= nivelActual.getLevel();
        for (int i=0; i<level.length;i++) {
            for (int j= 0; j<level[i].length; j++) {
                int yPos=(level.length-1-i)*Constantes.TILE_SIZE;
                switch(level[i][j]){
                    case '~':
                        tiposTiles=TileType.AIRE;
                        break;
                    case 'a': 
                        tiposTiles=TileType.PISO;
                        break;
                    case 'w':
                        tiposTiles=TileType.WALL;
                        break;
                    case 'B':
                        tiposTiles=TileType.BOX_EN_SU_LUGAR;
                        break;
                    case'b': 
                        tiposTiles=TileType.BOX;
                        break;
                    case '0': 
                        tiposTiles=TileType.META;
                        break;
                    case 'p':
                        tiposTiles=TileType.PISO;
                        if(!initPlayer){
                            player.setCabeza(2, 0);
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

    // ── HUD con botones clickeables ───────────────────────────────────────────
    private void dibujarHUD() {
        int sw = Gdx.graphics.getWidth();
        int sh = Gdx.graphics.getHeight();

        // Fondo del HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.07f, 0.07f, 0.13f, 0.95f);
        shape.rect(0, sh - HUD_H, sw, HUD_H);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.3f, 0.3f, 0.5f, 1f);
        shape.line(0, sh - HUD_H, sw, sh - HUD_H);
        shape.end();

        // Botones del HUD
        btnReiniciar.dibujar(batch, font, shape);
        btnSalir.dibujar(batch, font, shape);

        // Texto informativo del HUD (izquierda y centro)
        String nombreNivel = nivelActual.getName().replace(".txt", "");
        int    mins  = (int)(tiempoSegundos / 60);
        int    segs  = (int)(tiempoSegundos % 60);
        String timer = String.format("%02d:%02d", mins, segs);
        String infoIzq    = game.playerManager.getNombreJugador() + "   " + nombreNivel;
        String infoCentro = Textos.GAME_MOVS + movimientos + "   " + timer;

        batch.begin();
        font.setColor(0.8f, 0.8f, 1f, 1f);
        font.draw(batch, infoIzq, 10, sh - HUD_H + HUD_H * 0.6f);
        GlyphLayout gl = new GlyphLayout(font, infoCentro);
        font.draw(batch, infoCentro,
                (sw - BTN_W * 2 - 12) / 2f - gl.width / 2f,
                sh - HUD_H + HUD_H * 0.6f);
        batch.end();
    }

    private void logic() {
        // Clic en boton Salir
        if (btnSalir.clicado()) {
            detenerTimer();
            game.setScreen(new MenuScreen(game));
            dispose();
            return;
        }

        // Clic en boton Reiniciar
        if (btnReiniciar.clicado()) {
            nivelActual.reiniciar();
            GameScreen.initPlayer = false;
        }

        // Movimiento del jugador
        int prevX = player.x, prevY = player.y;
        player.tecladoInput(nivelActual);
        if (player.x != prevX || player.y != prevY) movimientos++;

        // Verificar victoria (Mantiene tu lógica original hacia MenuScreen)
        if(nivelActual.nivelCompletado() && !isGanado){
            System.out.println("Ganaste");
            isGanado=true;
            detenerTimer();
            game.setScreen(new MenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int w, int h) {
        camera.viewportWidth  = w;
        camera.viewportHeight = h;
        camera.update();
        crearBotonesHUD();
    }

    @Override public void pause()  { detenerTimer(); }
    @Override public void resume() { if (!isGanado) iniciarTimer(); }
    @Override public void hide()   {}

    @Override
    public void dispose() {
        detenerTimer();
        tiposTiles.dispose();
        batch.dispose();
        image.dispose();
        shape.dispose();
        sheetTiles.dispose();
        playerSheet.dispose();
        font.dispose();
        initPlayer=false;
    }
}