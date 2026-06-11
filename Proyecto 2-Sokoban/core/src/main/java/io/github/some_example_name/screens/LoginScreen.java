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

public class LoginScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    private Label lblError;
    private float errorTimer = 0f;

    private boolean esEspanol = true;
    private Label lblTitulo, lblSubtitulo, lblUsuario, lblContrasena;
    private TextButton btnIngresar, btnRegistro, btnIdioma;
    private TextField campoUser, campoClave;

    public LoginScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        Label.LabelStyle mutedStyle = new Label.LabelStyle(skin.getFont("small"),
                new Color(0.56f, 0.56f, 0.72f, 1f));
        Label.LabelStyle errorStyle = new Label.LabelStyle(skin.getFont("small"),
                new Color(1f, 0.37f, 0.37f, 1f));
        skin.add("muted", mutedStyle, Label.LabelStyle.class);
        skin.add("error", errorStyle, Label.LabelStyle.class);

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        lblTitulo = new Label("SOKOBAN", skin, "title-white");
        lblTitulo.setFontScale(1.4f);

        lblSubtitulo = new Label("Iniciar Sesion", skin, "muted");

        lblUsuario = new Label("Usuario", skin, "muted");
        lblContrasena = new Label("Contrasena", skin, "muted");

        campoUser = new TextField("", skin);
        campoClave = new TextField("", skin);
        campoClave.setPasswordMode(true);
        campoClave.setPasswordCharacter('*');

        btnIngresar = new TextButton("Ingresar", skin, "default");
        btnRegistro = new TextButton("Registrarse", skin, "default");

        lblError = new Label("", skin, "error");

        panel.add(lblTitulo).colspan(2).center().padBottom(2).row();
        panel.add(lblSubtitulo).colspan(2).center().padBottom(20).row();

        panel.add(lblUsuario).colspan(2).left().padBottom(4).row();
        panel.add(campoUser).colspan(2).width(280).height(32).padBottom(14).row();

        panel.add(lblContrasena).colspan(2).left().padBottom(4).row();
        panel.add(campoClave).colspan(2).width(280).height(32).padBottom(22).row();

        panel.add(btnIngresar).width(130).height(32).padRight(10);
        panel.add(btnRegistro).width(130).height(32).row();

        panel.add(lblError).colspan(2).center().padTop(8).row();

        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);

        btnIdioma = new TextButton("English", skin);
        Table topRight = new Table();
        topRight.setFillParent(true);
        topRight.top().right().pad(12);
        topRight.add(btnIdioma).width(70).height(26);

        stage.addActor(root);
        stage.addActor(topRight);

        Gdx.input.setInputProcessor(stage);

        btnIngresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                onIngresar();
            }
        });

        btnRegistro.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                onRegistro();
            }
        });

        btnIdioma.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                cambiarIdioma();
            }
        });
    }

    private void onIngresar() {
        String user = campoUser.getText().trim();
        String clave = campoClave.getText();
        if (user.isEmpty() || clave.isEmpty()) {
            mostrarError(esEspanol ? "Completa todos los campos" : "Fill in all fields");
            return;
        }
        boolean loginExitoso = game.playerManager.logIn(user, clave);
        if (loginExitoso) {
            game.setScreen(new MenuScreen(game));
            dispose();
        } else {
            mostrarError(esEspanol ? "Usuario o clave incorrectos" : "Wrong username or password");
        }
    }

    private void onRegistro() {
        game.setScreen(new RegisterScreen(game));
    }

    private void cambiarIdioma() {
        esEspanol = !esEspanol;

        lblSubtitulo.setText(esEspanol ? "Iniciar Sesion" : "Log In");
        lblUsuario.setText(esEspanol ? "Usuario" : "Username");
        lblContrasena.setText(esEspanol ? "Contrasena" : "Password");
        btnIngresar.setText(esEspanol ? "Ingresar" : "Log In");
        btnRegistro.setText(esEspanol ? "Registrarse" : "Sign Up");
        btnIdioma.setText(esEspanol ? "English" : "Espanol");
    }

    private void mostrarError(String msg) {
        lblError.setText(msg);
        errorTimer = 3f;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);

        if (errorTimer > 0) {
            errorTimer -= delta;
            if (errorTimer <= 0) {
                lblError.setText("");
            }
        }

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
