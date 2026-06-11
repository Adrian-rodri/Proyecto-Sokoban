package io.github.some_example_name.screens;

import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.screens.LevelSelectScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class MenuScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        String nombreJugador = game.playerManager.getNombreJugador();

        Label lblTitulo = new Label("SOKOBAN", skin, "title-white");
        Label lblSaludo = new Label("Hola, " + nombreJugador, skin, "medium-white");

        TextButton btnJugar = new TextButton("Jugar", skin, "big");
        TextButton btnPerfil = new TextButton("Mi perfil", skin, "big");
        TextButton btnReporte = new TextButton("Reportes", skin, "big");
        TextButton btnSalir = new TextButton("Salir", skin, "default");
        TextButton btnConfig = new TextButton("[Config]", skin, "small");
        TextButton btnAmigos = new TextButton("[Log Out]", skin, "small");
        TextButton btnAyuda = new TextButton("  ?  ", skin, "small");

        float bw = 240, bh = 42, iconW = 64, iconH = 32;

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f); 
        panel.add(lblTitulo).colspan(3).center().padBottom(4).row();
        panel.add(lblSaludo).colspan(3).center().padBottom(18).row();
        panel.add(btnJugar).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnPerfil).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnReporte).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnSalir).colspan(3).width(bw).height(bh).padBottom(14).row();
        panel.add(btnConfig).width(iconW).height(iconH).padRight(6);
        panel.add(btnAmigos).width(iconW).height(iconH).padRight(6);
        panel.add(btnAyuda).width(iconW).height(iconH).row();
        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);

        btnJugar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        btnPerfil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.setScreen(new ProfileScreen(game));
                dispose();
            }
        });
        btnReporte.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("Reportes");
//                game.setScreen(new ReportesScreen(game));
//                dispose();
            }
        });
        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });
        btnConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("[Menu] Config");
            }
        });
        btnAmigos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.playerManager.cerrarSesion();
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });
        btnAyuda.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("[Menu] Ayuda");
            }
        });
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
