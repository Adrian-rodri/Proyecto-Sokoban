package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class VictoryScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    private final int numLevel, movimientos, puntaje;
    private final double tiempoSegundos;
    private final boolean hayNivelSiguiente;

    public VictoryScreen(Main game, int numLevel, int movimientos, double tiempoSegundos, int puntaje) {
        this.game = game;
        this.numLevel = numLevel;
        this.movimientos = movimientos;
        this.tiempoSegundos = tiempoSegundos;
        this.puntaje = puntaje;
        this.hayNivelSiguiente = numLevel + 1 < game.nivelManager.getCantidad();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        skin.add("titulo", new Label.LabelStyle(skin.getFont("title"), new Color(0.4f, 1f, 0.5f, 1f)), Label.LabelStyle.class);
        skin.add("dato", new Label.LabelStyle(skin.getFont("small"), new Color(0.85f, 0.95f, 0.88f, 1f)), Label.LabelStyle.class);
        skin.add("dato-valor", new Label.LabelStyle(skin.getFont("font"), Color.WHITE), Label.LabelStyle.class);
        skin.add("mensaje-final", new Label.LabelStyle(skin.getFont("small"), new Color(1f, 0.85f, 0.25f, 1f)), Label.LabelStyle.class);

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(20f, 30f, 18f, 30f);

        Label lblTitulo = new Label("¡NIVEL COMPLETADO!", skin, "titulo");
        panel.add(lblTitulo).colspan(2).center().padBottom(4).row();

        String strNivel = "Nivel " + (numLevel + 1);
        Label lblNivel = new Label(strNivel, skin, "dato");
        panel.add(lblNivel).colspan(2).center().padBottom(18).row();

        int mins = (int) (tiempoSegundos / 60);
        int segs = (int) (tiempoSegundos % 60);
        String strTiempo = String.format("%02d:%02d", mins, segs);

        Label lblMovLabel = new Label("Movimientos:", skin, "dato");
        Label lblMovValor = new Label(String.valueOf(movimientos), skin, "dato-valor");
        panel.add(lblMovLabel).left().padRight(20).padBottom(6);
        panel.add(lblMovValor).right().padBottom(6).row();

        Label lblTiempoLabel = new Label("Tiempo:", skin, "dato");
        Label lblTiempoValor = new Label(strTiempo, skin, "dato-valor");
        panel.add(lblTiempoLabel).left().padRight(20).padBottom(6);
        panel.add(lblTiempoValor).right().padBottom(6).row();

        Label lblPuntajeLabel = new Label("Puntaje:", skin, "dato");
        Label lblPuntajeValor = new Label(String.valueOf(puntaje) + " pts", skin, "dato-valor");
        panel.add(lblPuntajeLabel).left().padRight(20).padBottom(14);
        panel.add(lblPuntajeValor).right().padBottom(14).row();

        if (!hayNivelSiguiente) {
            Label lblFinal = new Label("¡Completaste todos los niveles!", skin, "mensaje-final");
            panel.add(lblFinal).colspan(2).center().padBottom(14).row();
        }

        Table btnRow = new Table();

        if (hayNivelSiguiente) {
            TextButton btnSiguiente = new TextButton("Siguiente", skin, "big");
            btnSiguiente.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    GameScreen.initPlayer = false;
                    game.setScreen(new GameScreen(game, numLevel + 1));
                    dispose();
                }
            });
            btnRow.add(btnSiguiente).width(130).height(38).padRight(10);
        }

        TextButton btnNiveles = new TextButton("Niveles", skin, "big");
        btnNiveles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        btnRow.add(btnNiveles).width(130).height(38).padRight(10);

        TextButton btnMenu = new TextButton("Menu", skin, "default");
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        btnRow.add(btnMenu).width(130).height(38);

        panel.add(btnRow).colspan(2).center().padTop(4);

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
        ScreenUtils.clear(0.06f, 0.10f, 0.07f, 1f);
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
