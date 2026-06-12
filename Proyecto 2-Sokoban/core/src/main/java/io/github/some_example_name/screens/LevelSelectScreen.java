package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.model.Player;

public class LevelSelectScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    private Label lblInfo;
    private int totalNiveles;
    private int nivelesDisponibles;
    private int nivelesDesbloqueados;

    private static final int COLS = 5;

    public LevelSelectScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        totalNiveles = game.nivelManager.getCantidad();
        Player p = game.playerManager.getPlayerLogeado();
        nivelesDesbloqueados = (p != null) ? p.getNivelesDesbloqueados() : 0;
        nivelesDisponibles = Math.min(nivelesDesbloqueados + 1, totalNiveles);

        stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        TextButton.TextButtonStyle estiloBase = skin.get("small", TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle estiloCompletado = new TextButton.TextButtonStyle(estiloBase);
        estiloCompletado.fontColor = new Color(0.95f, 0.85f, 0.40f, 1f);
        estiloCompletado.overFontColor = new Color(1f, 0.95f, 0.65f, 1f);
        skin.add("nivel-completado", estiloCompletado);

        TextButton.TextButtonStyle estiloDisponible = new TextButton.TextButtonStyle(estiloBase);
        estiloDisponible.fontColor = Color.WHITE;
        estiloDisponible.overFontColor = new Color(0.80f, 0.85f, 1f, 1f);
        skin.add("nivel-disponible", estiloDisponible);

        Table grid = new Table();
        int cols = Math.min(totalNiveles, COLS);
        float tileW = 96f, tileH = 64f;

        for (int i = 0; i < totalNiveles; i++) {
            boolean bloqueado = i >= nivelesDisponibles;
            boolean completado = !bloqueado && i < nivelesDesbloqueados;
            boolean siguiente = !bloqueado && !completado;

            String texto;
            String estilo;
            if (completado) {
                texto = (i == 0 ? "Tutorial" : "Nivel " + i);
                estilo = "nivel-completado";
            } else if (siguiente) {
                texto = (i == 0 ? "Tutorial" : "Nivel " + i);
                estilo = "nivel-disponible";
            } else {
                texto = (i == 0 ? "Tutorial" : "Nivel " + i);
                estilo = "small";
            }

            TextButton btn = new TextButton(texto, skin, estilo);
            final int idx = i;

            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    if (idx < nivelesDisponibles) {
                        GameScreen.initPlayer = false;
                        game.setScreen(new GameScreen(game, idx));
                        dispose();
                    }
                }
            });

            btn.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (idx < nivelesDisponibles) {
                        Nivel nv = game.nivelManager.getNivel(idx);
                        String nombre = (idx == 0) ? "Tutorial" : "Nivel " + idx;
                        lblInfo.setText(nombre + "   -   Cajas: " + nv.getCantidadCajas());
                        lblInfo.setColor(0.65f, 0.65f, 0.9f, 1f);
                    } else {
                        lblInfo.setText("Nivel bloqueado");
                        lblInfo.setColor(0.5f, 0.5f, 0.5f, 1f);
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    lblInfo.setText("");
                }
            });

            grid.add(btn).width(tileW).height(tileH).pad(5);
            if ((i + 1) % cols == 0) {
                grid.row();
            }
        }

        lblInfo = new Label("", skin, "small-white");

        Table leyenda = new Table();
        Label lblCompletado = new Label("  Completado", skin, "small-white");
        lblCompletado.setColor(0.95f, 0.85f, 0.40f, 1f);
        Label lblDisponible = new Label("  Disponible", skin, "small-white");
        lblDisponible.setColor(Color.WHITE);
        Label lblBloqueado = new Label("  Bloqueado", skin, "small-white");
        lblBloqueado.setColor(0.4f, 0.4f, 0.4f, 1f);
        leyenda.add(lblCompletado).padRight(16);
        leyenda.add(lblDisponible).padRight(16);
        leyenda.add(lblBloqueado);

        TextButton btnVolver = new TextButton("Volver", skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        panel.add(new Label("Seleccionar nivel", skin, "title-white")).colspan(cols + 1).center().padBottom(12).row();
        panel.add(grid).colspan(cols + 1).center().row();
        panel.add(lblInfo).colspan(cols + 1).center().padTop(8).padBottom(6).row();
        panel.add(leyenda).colspan(cols + 1).center().padBottom(14).row();
        panel.add(btnVolver).colspan(cols + 1).center().width(140).height(32);
        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
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
        stage.dispose();
        skin.dispose();
    }
}
