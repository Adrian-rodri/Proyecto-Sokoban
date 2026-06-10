package io.github.some_example_name.screens;

import io.github.some_example_name.screens.MenuScreen;
import io.github.some_example_name.screens.LevelSelectScreen;
import io.github.some_example_name.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class VictoryScreen implements Screen {

    private final Main   game;
    private SpriteBatch  batch;
    private BitmapFont   font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin  skin;

    private final int    numLevel, movimientos, puntaje;
    private final double tiempoSegundos;
    private final boolean hayNivelSiguiente;

    // btnSiguiente puede ser null si no hay nivel siguiente
    private TextButton btnSiguiente, btnNiveles, btnMenu;

    public VictoryScreen(Main game, int numLevel, int movimientos, double tiempoSegundos, int puntaje) {
        this.game             = game;
        this.numLevel         = numLevel;
        this.movimientos      = movimientos;
        this.tiempoSegundos   = tiempoSegundos;
        this.puntaje          = puntaje;
        this.hayNivelSiguiente = numLevel + 1 < game.nivelManager.getCantidad();
    }

    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        skin  = crearSkin();
        crearBotones();
        Gdx.input.setInputProcessor(stage);
    }

    private Skin crearSkin() {
        Skin s = new Skin();
        BitmapFont f = new BitmapFont();
        f.getData().setScale(0.9f);
        s.add("default-font", f, BitmapFont.class);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font          = f;
        style.fontColor     = new Color(0.65f, 1f, 0.7f, 1f);
        style.overFontColor = Color.WHITE;
        style.downFontColor = new Color(0.3f, 0.7f, 0.4f, 1f);
        s.add("default", style, TextButton.TextButtonStyle.class);
        return s;
    }

    private void crearBotones() {
        // Limpiar actores previos si se llama desde resize
        stage.clear();

        btnNiveles = new TextButton("Niveles",  skin, "default");
        btnMenu    = new TextButton("Menu",     skin, "default");
        if (hayNivelSiguiente)
            btnSiguiente = new TextButton("Siguiente", skin, "default");

        btnNiveles.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelSelectScreen(game)); dispose();
            }
        });
        btnMenu.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game)); dispose();
            }
        });
        if (btnSiguiente != null) {
            btnSiguiente.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.initPlayer = false;
                    game.setScreen(new GameScreen(game, numLevel + 1));
                    dispose();
                }
            });
        }

        Table table = new Table();
        table.setFillParent(true);
        // Posicionar los botones en la misma zona que antes (cy - 132 ≈ 28% desde abajo)
        table.bottom().center().padBottom(Gdx.graphics.getHeight() * 0.28f);
        if (hayNivelSiguiente)
            table.add(btnSiguiente).width(90).height(30).padRight(12);
        table.add(btnNiveles).width(90).height(30).padRight(12);
        table.add(btnMenu).width(90).height(30);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.06f, 0.1f, 0.07f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        // Panel
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.18f, 0.12f, 1f);
        shape.rect(cx - 200, cy - 150, 400, 330);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.3f, 0.65f, 0.4f, 1f);
        shape.rect(cx - 200, cy - 150, 400, 330);
        shape.end();

        int  mins = (int)(tiempoSegundos / 60);
        int  segs = (int)(tiempoSegundos % 60);
        float lx  = cx - 150;

        batch.begin();
        font.setColor(0.35f, 1f, 0.5f, 1f);
        GlyphLayout win = new GlyphLayout(font, "¡Nivel completado!");
        font.draw(batch, "¡Nivel completado!", cx - win.width / 2f, cy + 158);

        font.setColor(Color.WHITE);
        String strNivel = "Nivel " + (numLevel + 1);
        GlyphLayout nv  = new GlyphLayout(font, strNivel);
        font.draw(batch, strNivel, cx - nv.width / 2f, cy + 128);

        font.setColor(0.75f, 0.9f, 0.78f, 1f);
        font.draw(batch, "Movimientos: " + movimientos,                             lx, cy + 88);
        font.draw(batch, "Tiempo: "      + String.format("%02d:%02d", mins, segs), lx, cy + 60);
        font.draw(batch, "Puntaje: "     + puntaje,                                lx, cy + 32);

        if (!hayNivelSiguiente) {
            font.setColor(1f, 0.85f, 0.25f, 1f);
            GlyphLayout fin = new GlyphLayout(font, "¡Completaste todos los niveles!");
            font.draw(batch, "¡Completaste todos los niveles!", cx - fin.width / 2f, cy - 55);
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        stage.getViewport().update(w, h, true);
        crearBotones();
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   { Gdx.input.setInputProcessor(null); }
    @Override public void dispose() {
        batch.dispose(); font.dispose(); shape.dispose();
        stage.dispose(); skin.dispose();
    }
}