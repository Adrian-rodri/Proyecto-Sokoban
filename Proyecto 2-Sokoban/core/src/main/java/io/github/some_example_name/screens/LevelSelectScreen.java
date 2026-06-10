package io.github.some_example_name.screens;

import io.github.some_example_name.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.model.Player;

public class LevelSelectScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private TextButton btnVolver;

    private int totalNiveles;
    private int nivelesDisponibles;
    private int nivelesDesbloqueados;

    private static final float TILE_W = 88;
    private static final float TILE_H = 64;
    private static final float GAP = 12;
    private static final int COLS = 5;

    public LevelSelectScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        totalNiveles = game.nivelManager.getCantidad();
        Player p = game.playerManager.getPlayerLogeado();
        nivelesDesbloqueados = (p != null) ? p.getNivelesDesbloqueados() : 0;
        nivelesDisponibles = Math.min(nivelesDesbloqueados + 1, totalNiveles);

        stage = new Stage(new ScreenViewport());
        skin = crearSkin();

        btnVolver = new TextButton("Volver", skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.bottom().center().pad(20);
        table.add(btnVolver).width(140).height(32);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private Skin crearSkin() {
        Skin s = new Skin();
        BitmapFont f = new BitmapFont();
        f.getData().setScale(0.9f);
        s.add("default-font", f, BitmapFont.class);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = f;
        style.fontColor = new Color(0.8f, 0.8f, 1f, 1f);
        style.overFontColor = Color.WHITE;
        style.downFontColor = new Color(0.5f, 0.5f, 0.8f, 1f);
        s.add("default", style, TextButton.TextButtonStyle.class);
        return s;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth() / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;
        int cols = Math.min(totalNiveles, COLS);
        float gridW = cols * (TILE_W + GAP) - GAP;
        float startX = cx - gridW / 2f;
        float startY = cy + 150;

        batch.begin();
        font.setColor(Color.WHITE);
        String titulo = "Seleccionar nivel";
        GlyphLayout gl = new GlyphLayout(font, titulo);
        font.draw(batch, titulo, cx - gl.width / 2f, cy + 235);
        batch.end();

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        int hoveredIdx = -1;

        for (int i = 0; i < totalNiveles; i++) {
            int col = i % cols;
            int row = i / cols;
            float tx = startX + col * (TILE_W + GAP);
            float ty = startY - row * (TILE_H + GAP);
            if (mx >= tx && mx <= tx + TILE_W && my >= ty - TILE_H && my <= ty) {
                hoveredIdx = i;
            }
        }

        for (int i = 0; i < totalNiveles; i++) {
            int col = i % cols;
            int row = i / cols;
            float tx = startX + col * (TILE_W + GAP);
            float ty = startY - row * (TILE_H + GAP);

            boolean bloqueado = i >= nivelesDisponibles;
            boolean completado = !bloqueado && i < nivelesDesbloqueados;
            boolean siguiente = !bloqueado && !completado;
            boolean hover = i == hoveredIdx && !bloqueado;

            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (hover && completado) {
                shape.setColor(0.30f, 0.28f, 0.12f, 1f);
            } else if (hover) {
                shape.setColor(0.28f, 0.28f, 0.50f, 1f);
            } else if (completado) {
                shape.setColor(0.20f, 0.18f, 0.08f, 1f);
            } else if (siguiente) {
                shape.setColor(0.18f, 0.18f, 0.30f, 1f);
            } else {
                shape.setColor(0.11f, 0.11f, 0.16f, 1f);
            }
            shape.rect(tx, ty - TILE_H, TILE_W, TILE_H);
            shape.end();

            shape.begin(ShapeRenderer.ShapeType.Line);
            if (hover && completado) {
                shape.setColor(1.0f, 0.85f, 0.25f, 1f);
            } else if (hover) {
                shape.setColor(0.70f, 0.70f, 1.00f, 1f);
            } else if (completado) {
                shape.setColor(0.75f, 0.65f, 0.20f, 1f);
            } else if (siguiente) {
                shape.setColor(0.40f, 0.40f, 0.65f, 1f);
            } else {
                shape.setColor(0.22f, 0.22f, 0.30f, 1f);
            }
            shape.rect(tx, ty - TILE_H, TILE_W, TILE_H);
            shape.end();

            batch.begin();
            String label = "Nivel " + (i + 1);
            if (hover) {
                font.setColor(Color.YELLOW);
            } else if (completado) {
                font.setColor(0.95f, 0.85f, 0.40f, 1f);
            } else if (siguiente) {
                font.setColor(Color.WHITE);
            } else {
                font.setColor(0.30f, 0.30f, 0.30f, 1f);
            }
            GlyphLayout glLabel = new GlyphLayout(font, label);
            font.draw(batch, label, tx + TILE_W / 2f - glLabel.width / 2f, ty - TILE_H / 2f + 7);

            if (bloqueado) {
                font.setColor(0.35f, 0.35f, 0.35f, 1f);
                GlyphLayout lk = new GlyphLayout(font, "Bloqueado");
                font.draw(batch, "Bloqueado", tx + TILE_W / 2f - lk.width / 2f, ty - TILE_H / 2f - 7);
            } else if (completado) {
                font.setColor(hover ? Color.YELLOW : new Color(0.85f, 0.75f, 0.20f, 1f));
                GlyphLayout lc = new GlyphLayout(font, "Completado");
                font.draw(batch, "Completado", tx + TILE_W / 2f - lc.width / 2f, ty - TILE_H / 2f - 7);
            }
            batch.end();
        }

        if (hoveredIdx >= 0 && hoveredIdx < nivelesDisponibles) {
            Nivel nv = game.nivelManager.getNivel(hoveredIdx);
            String nombre = nv.getName().replace(".txt", "");
            String info = nombre + "   -   Cajas: " + nv.getCantidadCajas();
            batch.begin();
            font.setColor(0.65f, 0.65f, 0.9f, 1f);
            GlyphLayout glInfo = new GlyphLayout(font, info);
            font.draw(batch, info, cx - glInfo.width / 2f, cy - 115);
            batch.end();
        }

        batch.begin();
        font.setColor(0.75f, 0.65f, 0.20f, 1f);
        font.draw(batch, "       Completado", cx - 130, cy - 150);
        font.setColor(0.55f, 0.55f, 0.85f, 1f);
        font.draw(batch, "     disponible", cx + 10, cy - 150);
        batch.end();

        boolean clicado = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (clicado && hoveredIdx >= 0 && hoveredIdx < nivelesDisponibles) {
            GameScreen.initPlayer = false;
            game.setScreen(new GameScreen(game, hoveredIdx));
            dispose();
            return;
        }

        stage.act(delta);
        stage.draw();
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shape.dispose();
        stage.dispose();
        skin.dispose();
    }
}
