package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.util.Constantes;
import io.github.some_example_name.Main;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.game.NivelManager;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.util.TileType;

public class GameScreen implements Screen {

    private final Main game;

    private SpriteBatch batch;
    private Texture playerSheet, sheetTiles;
    private TextureRegion wall, box, objetivo, piso;
    private ShapeRenderer shape;
    private Player player;
    private OrthographicCamera camera;
    public static boolean initPlayer = false;
    private TileType tiposTiles;
    private NivelManager nivelMng;
    private Nivel nivelActual;
    private boolean isGanado = false;
    private int numLevel;

    private Stage stage;
    private Skin skin;
    private TextButton btnReiniciar, btnSalir;
    private Label lblInfo, lblStats;

    private int movimientos = 0;
    private volatile double tiempoSegundos = 0;
    private volatile boolean timerActivo = false;
    private Thread hiloTimer;

    //audio
    private Music gameMusic;
    private Sound boxSound;
    private Sound ganarSound;

    private static final int HUD_H = 36;
    
    private int offsetX, offsetY;
    private int boardW, boardH;
    private static final int PANEL_PAD = 10;

    public GameScreen(Main game, int numLevel) {
        this.game = game;
        this.numLevel = numLevel;
    }

    private void calcularOffsets() {
        int winW = Gdx.graphics.getWidth();
        int winH = Gdx.graphics.getHeight();

        char[][] level = nivelActual.getLevel();
        int cols = 0;
        for (char[] fila : level) {
            if (fila.length > cols) cols = fila.length;
        }
        int rows = level.length;
        boardW = cols * Constantes.TILE_SIZE;
        boardH = rows * Constantes.TILE_SIZE;

        
        int areaY = winH - HUD_H;
        int areaX = winW;

        offsetX = Math.max(PANEL_PAD, (areaX - boardW) / 2);
        offsetY = Math.max(PANEL_PAD, (areaY - boardH) / 2);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        sheetTiles = new Texture("texturas/sheetTiles.png");
        wall = new TextureRegion(sheetTiles, 0, 2 * 80, 80, 80);
        box = new TextureRegion(sheetTiles, 1 * 80, 1 * 80, 80, 80);
        piso = new TextureRegion(sheetTiles, 2 * 80, 0, 80, 80);
        objetivo = new TextureRegion(sheetTiles, 3 * 80, 1 * 80, 80, 80);
        shape = new ShapeRenderer();
        player = new Player(0, 0);
        String avatarFile= game.playerManager.getPlayerLogeado().getAvatarFile();
        try {
            playerSheet= new Texture("texturas/avatares/" + avatarFile);
        } catch(Exception e){
            playerSheet= new Texture("texturas/avatares/1-default.png");
        }
        player.cargarSprites(playerSheet);
        player.setSprite(32, 0);
        tiposTiles = TileType.WALL;
        nivelMng = new NivelManager();
        nivelMng.cargar();
        nivelActual = nivelMng.getNivel(numLevel);
        initPlayer = false;
        
        //cargar audio
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("ui/sonido/musica/gameMusic.mp3"));
        gameMusic.setLooping(true);
        double vol = game.playerManager.getPlayerLogeado().getVolumen();
        gameMusic.setVolume((float) vol);
        gameMusic.play();
        boxSound = Gdx.audio.newSound(Gdx.files.internal("ui/sonido/fx/boxfx.wav"));
        ganarSound = Gdx.audio.newSound(Gdx.files.internal("ui/sonido/fx/ganarfx.mp3"));

        calcularOffsets();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        btnReiniciar = new TextButton("Reiniciar", skin, "small");
        btnSalir = new TextButton("Salir", skin, "small");

        btnReiniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                nivelActual.reiniciar();
                movimientos = 0;
                tiempoSegundos = 0;
                GameScreen.initPlayer = false;
                player.copias.clear();
            }
        });

        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                detenerTimer();
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        lblInfo = new Label("", skin, "small-white");
        lblStats = new Label("", skin, "small-white");

        Table hudBar = new Table();
        hudBar.setFillParent(true);
        hudBar.top();
        hudBar.add(lblInfo).left().pad(8).expandX();
        hudBar.add(lblStats).center().pad(8).expandX();
        hudBar.add(btnReiniciar).width(90).height(24).padRight(4);
        hudBar.add(btnSalir).width(90).height(24);
        stage.addActor(hudBar);

        iniciarTimer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.04f, 0.04f, 0.07f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        // ── Panel de fondo del tablero ──
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.08f, 0.09f, 0.14f, 1f);
        shape.rect(offsetX - PANEL_PAD, offsetY - PANEL_PAD,
                boardW + PANEL_PAD * 2, boardH + PANEL_PAD * 2);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.18f, 0.22f, 0.35f, 1f);
        shape.rect(offsetX - PANEL_PAD, offsetY - PANEL_PAD,
                boardW + PANEL_PAD * 2, boardH + PANEL_PAD * 2);
        shape.end();

        
        batch.begin();
        dibujarTexturas(batch);
        batch.end();
        
        dibujarBarraHUD();

        //actualizar Labels cada frame
        String nombreNivel = (numLevel == 0) ? "Tutorial" : "Nivel " + numLevel;
        int mins = (int) (tiempoSegundos / 60);
        int segs = (int) (tiempoSegundos % 60);
        String timerStr = String.format("%02d:%02d", mins, segs);
        lblInfo.setText(game.playerManager.getNombreJugador() + "   " + nombreNivel);
        lblStats.setText("Movimientos: " + movimientos + "   " + timerStr);

        stage.act(delta);
        stage.draw();

        logic();
    }

    private void dibujarTexturas(SpriteBatch batch) {
        char[][] level = nivelActual.getLevel();
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                int drawX = offsetX + j * Constantes.TILE_SIZE;
                int drawY = offsetY + (level.length - 1 - i) * Constantes.TILE_SIZE;

                switch (level[i][j]) {
                    case '~':
                        tiposTiles = TileType.AIRE;
                        break;
                    case 'a':
                        tiposTiles = TileType.PISO;
                        break;
                    case 'w':
                        tiposTiles = TileType.WALL;
                        break;
                    case 'B':
                        batch.draw(TileType.META.getTexture(), drawX, drawY,
                                Constantes.TILE_SIZE, Constantes.TILE_SIZE);
                        tiposTiles = TileType.BOX_EN_SU_LUGAR;
                        break;
                    case 'b':
                        batch.draw(TileType.PISO.getTexture(), drawX, drawY,
                                Constantes.TILE_SIZE, Constantes.TILE_SIZE);
                        tiposTiles = TileType.BOX;
                        break;
                    case '0':
                        tiposTiles = TileType.META;
                        break;
                    case 'p':
                        tiposTiles = TileType.PISO;
                        if (!initPlayer) {
                            player.setSprite(2, 0);
                            player.x = j * Constantes.TILE_SIZE;
                            player.y = (level.length - 1 - i) * Constantes.TILE_SIZE;
                            initPlayer = true;
                        }
                        break;
                }
                batch.draw(tiposTiles.getTexture(), drawX, drawY,
                        Constantes.TILE_SIZE, Constantes.TILE_SIZE);
            }
        }
        player.dibujarPlayer(batch, offsetX, offsetY);
    }

    private void logic() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nivelActual.reiniciar();
            movimientos = 0;
            tiempoSegundos = 0;
            GameScreen.initPlayer = false;
            player.copias.clear();
        }

        //contar cajas en su sitio antes del movimiento
        int cajasAntes = contarCajasEnSitio(nivelActual.getLevel());
        int resultado = player.tecladoInput(nivelActual);
        if (resultado == 1) {
            movimientos++;
            //si aumento la cantidad de cajas en su sitio, una caja llego a su destino
            int cajasAhora = contarCajasEnSitio(nivelActual.getLevel());
            if (cajasAhora > cajasAntes && boxSound != null) {
                boxSound.play((float) game.playerManager.getPlayerLogeado().getVolumen());
            }
        } else if (resultado == -1 && movimientos > 0) {
            movimientos--;
        }
        if (nivelActual.nivelCompletado() && !isGanado) {
            isGanado = true;
            detenerTimer();
            if (ganarSound != null) {
                ganarSound.play((float) game.playerManager.getPlayerLogeado().getVolumen());
            }
            int puntaje = calcularPuntaje();
            game.playerManager.actualizarTrasPartida(numLevel, movimientos, tiempoSegundos, puntaje);
            game.setScreen(new VictoryScreen(game, numLevel, movimientos, tiempoSegundos, puntaje));
            dispose();
        }
    }

    private int contarCajasEnSitio(char[][] level) {
        int count = 0;
        for (char[] fila : level) {
            for (char c : fila) {
                if (c == 'B') count++;
            }
        }
        return count;
    }

    private int calcularPuntaje() {
        int puntajeBase = 200 * (numLevel + 1) - movimientos;
        int puntajeTiempo = Math.max(0, 30 - (int) tiempoSegundos) * 2;
        return Math.max(10, puntajeBase + puntajeTiempo);
    }

    private void dibujarBarraHUD() {
        int sw = Gdx.graphics.getWidth();
        int sh = Gdx.graphics.getHeight();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.07f, 0.07f, 0.13f, 0.95f);
        shape.rect(0, sh - HUD_H, sw, HUD_H);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.3f, 0.3f, 0.5f, 1f);
        shape.line(0, sh - HUD_H, sw, sh - HUD_H);
        shape.end();
    }

    private void iniciarTimer() {
        timerActivo = true;
        hiloTimer = new Thread(new Runnable() {
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
        if (hiloTimer != null) {
            hiloTimer.interrupt();
        }
    }

    @Override
    public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
        stage.getViewport().update(w, h, true);
        calcularOffsets();
    }

    @Override
    public void pause() {
        detenerTimer();
    }

    @Override
    public void resume() {
        if (!isGanado) {
            iniciarTimer();
        }
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        sheetTiles.dispose();
        playerSheet.dispose();
        stage.dispose();
        skin.dispose();
        initPlayer = false;
        detenerTimer();
        if (gameMusic != null) {
            gameMusic.stop();
            gameMusic.dispose();
        }
        if (boxSound != null) boxSound.dispose();
        //no disponer ganarSound si se gano el nivel para que el sonido termine de reproducirse
        if (!isGanado && ganarSound != null) ganarSound.dispose();
    }
}
