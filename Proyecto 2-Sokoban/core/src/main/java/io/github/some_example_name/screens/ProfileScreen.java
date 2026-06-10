package io.github.some_example_name.screens;

import io.github.some_example_name.screens.MenuScreen;
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

public class ProfileScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private ArrayList<String[]> ranking;
    private TextButton btnVolver, btnRecords;

    public ProfileScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ranking = game.playerManager.getRanking();

        stage = new Stage(new ScreenViewport());
        skin = crearSkin();

        btnVolver = new TextButton("Volver", skin, "default");
        btnRecords = new TextButton("Records", skin, "default");

        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        btnRecords.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new RecordsScreen(game));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.bottom().center().pad(20);
        table.add(btnVolver).width(155).height(32).padRight(10);
        table.add(btnRecords).width(155).height(32);
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

        Player p = game.playerManager.getPlayerLogeado();
        float panelH = 400, panelW = 350;

        // Panel izquierdo: perfil
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - panelW - 20, cy - panelH / 2f, panelW, panelH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - panelW - 20, cy - panelH / 2f, panelW, panelH);
        shape.end();

        // Panel derecho: ranking
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx + 20, cy - panelH / 2f, panelW, panelH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx + 20, cy - panelH / 2f, panelW, panelH);
        shape.end();

        batch.begin();

        // Titulos
        font.setColor(Color.WHITE);
        GlyphLayout tp = new GlyphLayout(font, "Perfil");
        font.draw(batch, "Perfil",
                cx - panelW / 2f - 20 - tp.width / 2f, cy + panelH / 2f + 22);
        GlyphLayout tr = new GlyphLayout(font, "Ranking");
        font.draw(batch, "Ranking",
                cx + 20 + panelW / 2f - tr.width / 2f, cy + panelH / 2f + 22);

        // Panel de perfil
        if (p != null) {
            float lx = cx - panelW - 10;
            float ly = cy + panelH / 2f - 18;
            float step = 26;

            font.setColor(Color.WHITE);
            font.draw(batch, p.getNombreCompleto(), lx, ly);
            font.setColor(0.5f, 0.5f, 0.75f, 1f);
            font.draw(batch, "@" + p.getUserName(), lx, ly - 16);

            ly -= step + 10;
            font.setColor(0.75f, 0.75f, 0.95f, 1f);
            font.draw(batch, "Partidas: " + p.getPartidasJugadas(), lx, ly);
            font.draw(batch, "Niveles completados: " + p.getNivelesCompletados(), lx, ly -= step);
            font.draw(batch, "Mejor puntaje: " + p.getMejorPuntaje(), lx, ly -= step);
            font.draw(batch, "Puntaje total: " + p.getPuntajeGeneral(), lx, ly -= step);
            font.draw(batch, "Tiempo jugado: " + String.format("%.2f", p.getTiempoJugadoHoras()) + " h", lx, ly -= step);
            font.draw(batch, "Promedio/nivel: " + String.format("%.0f", p.getTiempoPromedioPorNivel()) + " s", lx, ly -= step);
            font.draw(batch, "Desbloqueados: " + (p.getNivelesDesbloqueados() + 1), lx, ly -= step);

            ArrayList<EntradaHistorial> hist = p.getHistorial();
            if (hist != null && !hist.isEmpty()) {
                ly -= 10;
                font.setColor(0.5f, 0.5f, 0.75f, 1f);
                font.draw(batch, "Ultimas partidas:", lx, ly -= step * 0.7f);
                int desde = Math.max(0, hist.size() - 4);
                for (int i = desde; i < hist.size(); i++) {
                    EntradaHistorial e = hist.get(i);
                    font.setColor(0.65f, 0.65f, 0.65f, 1f);
                    String linea = "Nv." + (e.getNivel() + 1)
                            + "  Movs:" + e.getMovimientos()
                            + "  Pts:" + e.getPuntaje();
                    font.draw(batch, linea, lx, ly -= step * 0.8f);
                }
            }
        }

        // Panel de ranking
        float rx = cx + 30;
        float ry = cy + panelH / 2f - 18;
        int top = Math.min(10, ranking.size());

        if (top == 0) {
            font.setColor(0.4f, 0.4f, 0.6f, 1f);
            font.draw(batch, "Sin datos", rx, ry);
        } else {
            for (int i = 0; i < top; i++) {
                String[] entry = ranking.get(i);
                boolean esYo = p != null && entry[0].equals(p.getUserName());
                if (i == 0) {
                    font.setColor(1f, 0.85f, 0.2f, 1f);
                } else if (i == 1) {
                    font.setColor(0.8f, 0.8f, 0.8f, 1f);
                } else if (i == 2) {
                    font.setColor(0.8f, 0.55f, 0.3f, 1f);
                } else if (esYo) {
                    font.setColor(Color.YELLOW);
                } else {
                    font.setColor(Color.LIGHT_GRAY);
                }
                String linea = (i + 1) + ".  " + entry[0] + "  -  " + entry[1] + " pts";
                if (esYo) {
                    linea += "  <";
                }
                font.draw(batch, linea, rx, ry - i * 30);
            }
        }
        batch.end();

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
