package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private Texture image, playerSheet, sheetTiles;
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

    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private TextButton btnReiniciar, btnSalir;

    private int movimientos = 0;
    private volatile double tiempoSegundos = 0;
    private volatile boolean timerActivo = false;
    private Thread hiloTimer;

    private static final int HUD_H = 36;

    public GameScreen(Main game, int numLevel) {
        this.game = game;
        this.numLevel = numLevel;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        image = new Texture("fondo.png");
        playerSheet = new Texture("playerSheet.png");
        sheetTiles = new Texture("sheetTiles.png");
        wall = new TextureRegion(sheetTiles, 0, 2 * 80, 80, 80);
        box = new TextureRegion(sheetTiles, 1 * 80, 1 * 80, 80, 80);
        piso = new TextureRegion(sheetTiles, 2 * 80, 0, 80, 80);
        objetivo = new TextureRegion(sheetTiles, 3 * 80, 1 * 80, 80, 80);
        shape = new ShapeRenderer();
        player = new Player(0, 0);
        player.cargarSprites(playerSheet, playerSheet, playerSheet);
        player.setCabeza(0, 0);
        tiposTiles = TileType.WALL;
        nivelMng = new NivelManager();
        nivelMng.cargar();
        nivelActual = nivelMng.getNivel(numLevel);
        initPlayer = false;

        font = new BitmapFont();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = crearSkin();

        btnReiniciar = new TextButton("Reiniciar", skin, "reiniciar");
        btnSalir = new TextButton("Salir", skin, "salir");

        btnReiniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nivelActual.reiniciar();
                GameScreen.initPlayer = false;
            }
        });

        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                detenerTimer();
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().right().pad(6);
        table.add(btnReiniciar).width(90).height(24).padRight(4);
        table.add(btnSalir).width(90).height(24);
        stage.addActor(table);

        iniciarTimer();
    }

    private Skin crearSkin() {
        Skin s = new Skin();

        BitmapFont btnFont = new BitmapFont();
        btnFont.getData().setScale(0.85f);
        s.add("default-font", btnFont, BitmapFont.class);

        TextButton.TextButtonStyle estiloReiniciar = new TextButton.TextButtonStyle();
        estiloReiniciar.font = btnFont;
        estiloReiniciar.fontColor = new Color(0.8f, 0.8f, 1f, 1f);
        estiloReiniciar.overFontColor = new Color(1f, 1f, 1f, 1f);
        estiloReiniciar.downFontColor = new Color(0.6f, 0.6f, 0.9f, 1f);
        s.add("reiniciar", estiloReiniciar, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle estiloSalir = new TextButton.TextButtonStyle();
        estiloSalir.font = btnFont;
        estiloSalir.fontColor = new Color(1f, 0.6f, 0.6f, 1f);
        estiloSalir.overFontColor = new Color(1f, 0.8f, 0.8f, 1f);
        estiloSalir.downFontColor = new Color(0.8f, 0.3f, 0.3f, 1f);
        s.add("salir", estiloSalir, TextButton.TextButtonStyle.class);

        return s;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        batch.begin();
        dibujarTexturas(batch);
        batch.end();

        dibujarHUD();

        stage.act(delta);
        stage.draw();

        logic();
    }

    private void dibujarTexturas(SpriteBatch batch) {
        batch.draw(image, 0, 0, 832, 640);
        char[][] level = nivelActual.getLevel();
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                int yPos = (level.length - 1 - i) * Constantes.TILE_SIZE;
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
                        batch.draw(TileType.META.getTexture(), j * Constantes.TILE_SIZE, yPos, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
                        tiposTiles = TileType.BOX_EN_SU_LUGAR;
                        break;
                    case 'b':
                        batch.draw(TileType.PISO.getTexture(), j * Constantes.TILE_SIZE, yPos, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
                        tiposTiles = TileType.BOX;
                        break;
                    case '0':
                        tiposTiles = TileType.META;
                        break;
                    case 'p':
                        tiposTiles = TileType.PISO;
                        if (!initPlayer) {
                            player.setCabeza(2, 0);
                            player.x = j * Constantes.TILE_SIZE;
                            player.y = yPos;
                            initPlayer = true;
                        }
                        break;
                }
                batch.draw(tiposTiles.getTexture(), j * Constantes.TILE_SIZE, yPos, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
            }
        }
        player.dibujarPlayer(batch);
    }

    private void logic() {
        player.tecladoInput(nivelActual);
        if (nivelActual.nivelCompletado() && !isGanado) {
            System.out.println("Ganaste");
            isGanado = true;
            detenerTimer();
            game.setScreen(new MenuScreen(game));
            dispose();
        }
    }

    private void dibujarHUD() {
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

        String nombreNivel = nivelActual.getName().replace(".txt", "");
        int mins = (int) (tiempoSegundos / 60);
        int segs = (int) (tiempoSegundos % 60);
        String timer = String.format("%02d:%02d", mins, segs);
        String infoIzq = game.playerManager.getNombreJugador() + "   " + nombreNivel;
        String infoCentro = "Movimientos: " + movimientos + "   " + timer;

        batch.begin();
        font.setColor(0.8f, 0.8f, 1f, 1f);
        font.draw(batch, infoIzq, 10, sh - HUD_H + HUD_H * 0.6f);
        GlyphLayout gl = new GlyphLayout(font, infoCentro);
        font.draw(batch, infoCentro,
                (sw - 200) / 2f - gl.width / 2f,
                sh - HUD_H + HUD_H * 0.6f);
        batch.end();
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
        camera.viewportWidth = w;
        camera.viewportHeight = h;
        camera.update();
        stage.getViewport().update(w, h, true);
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
        image.dispose();
        shape.dispose();
        sheetTiles.dispose();
        playerSheet.dispose();
        font.dispose();
        stage.dispose();
        skin.dispose();
        initPlayer = false;
        detenerTimer();
    }
}
