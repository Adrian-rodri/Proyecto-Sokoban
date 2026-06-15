package io.github.some_example_name.screens;

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
import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.Player;
import java.util.ArrayList;

public class ReportesScreen extends BaseScreen {

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private TextButton btnVolver;
    private int totalNiveles;
    private int[][] mejores;

    public ReportesScreen(Main game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        totalNiveles = game.nivelManager.getCantidad();
        mejores = new int[totalNiveles][];
        computarMejores();

        stage = new Stage(new ScreenViewport());
        skin = crearSkin();

        btnVolver = new TextButton("Volver", skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ProfileScreen(game));
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

    private void computarMejores() {
        Player p = game.playerManager.getPlayerLogeado();
        if (p == null) {
            return;
        }
        ArrayList<EntradaHistorial> hist = p.getHistorial();
        if (hist == null) {
            return;
        }
        for (EntradaHistorial e : hist) {
            int nv = e.getNivel();
            if (nv < 0 || nv >= totalNiveles) {
                continue;
            }
            if (mejores[nv] == null || e.getPuntaje() > mejores[nv][0]) {
                mejores[nv] = new int[]{e.getPuntaje(), e.getMovimientos(), (int) e.getTiempo()};
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth() / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - 280, cy - 235, 560, 475);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - 280, cy - 235, 560, 475);
        shape.end();

        float headerY = cy + 185;
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.line(cx - 265, headerY - 4, cx + 265, headerY - 4);
        shape.end();

        float colNivel = cx - 260;
        float colPuntaje = cx - 60;
        float colMovs = cx + 80;
        float colTiempo = cx + 170;

        batch.begin();

        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "Records");
        font.draw(batch, "Records", cx - titulo.width / 2f, cy + 223);

        font.setColor(0.45f, 0.45f, 0.65f, 1f);
        GlyphLayout hint = new GlyphLayout(font, "Mejor resultado por nivel");
        font.draw(batch, "Mejor resultado por nivel", cx - hint.width / 2f, cy + 202);

        font.setColor(0.65f, 0.65f, 0.88f, 1f);
        font.draw(batch, "Nivel", colNivel, headerY);
        font.draw(batch, "Puntaje", colPuntaje, headerY);
        font.draw(batch, "Movs", colMovs, headerY);
        font.draw(batch, "Tiempo", colTiempo, headerY);

        float rowStep = 36f;
        float rowY = headerY - rowStep;

        for (int i = 0; i < totalNiveles; i++) {
            String nombreNivel = game.nivelManager.getNivel(i).getName().replace(".txt", "");
            if (mejores[i] == null) {
                font.setColor(0.40f, 0.40f, 0.50f, 1f);
                font.draw(batch, nombreNivel, colNivel, rowY);
                font.draw(batch, "-", colPuntaje, rowY);
            } else {
                int pts = mejores[i][0];
                int movs = mejores[i][1];
                int segs = mejores[i][2];
                String tiempo = String.format("%02d:%02d", segs / 60, segs % 60);
                if (esMejorDeTodos(i)) {
                    font.setColor(1f, 0.85f, 0.2f, 1f);
                } else {
                    font.setColor(0.80f, 0.80f, 0.95f, 1f);
                }
                font.draw(batch, nombreNivel, colNivel, rowY);
                font.draw(batch, String.valueOf(pts), colPuntaje, rowY);
                font.draw(batch, String.valueOf(movs), colMovs, rowY);
                font.draw(batch, tiempo, colTiempo, rowY);
            }
            rowY -= rowStep;
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private boolean esMejorDeTodos(int idx) {
        if (mejores[idx] == null) {
            return false;
        }
        int max = mejores[idx][0];
        for (int i = 0; i < totalNiveles; i++) {
            if (i != idx && mejores[i] != null && mejores[i][0] > max) {
                return false;
            }
        }
        return true;
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

    @Override
    protected void buildUI() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
