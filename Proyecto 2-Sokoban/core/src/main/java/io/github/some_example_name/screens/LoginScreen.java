package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;

public class LoginScreen extends BaseScreen {

    private Label lblError;
    private float errorTimer = 0f;

    private boolean esEspanol;
    private Label lblTitulo, lblSubtitulo, lblUsuario, lblContrasena;
    private TextButton btnIngresar, btnRegistro, btnIdioma, btnVerClave;
    private TextField campoUser, campoClave;

    public LoginScreen(Main game) {
        super(game);
    }


    @Override
    protected void buildUI() {
        esEspanol = !"english".equals(game.idiomaGlobal);
        Label.LabelStyle errorStyle = new Label.LabelStyle(skin.getFont("font"),
                new Color(1f, 0.37f, 0.37f, 1f));
        skin.add("error", errorStyle, Label.LabelStyle.class);

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        lblTitulo = new Label("SOKOBAN", skin, "title-white");
        lblSubtitulo = new Label(esEspanol ? "Iniciar Sesion" : "Log In", skin, "medium-white");
        lblUsuario = new Label(esEspanol ? "Usuario" : "Username", skin, "small-white");
        lblContrasena = new Label(esEspanol ? "Contrasena" : "Password", skin, "small-white");
        
        campoUser = new TextField("", skin);
        campoClave = new TextField("", skin);
        campoClave.setPasswordMode(true);
        campoClave.setPasswordCharacter('*');

        btnIngresar = new TextButton(esEspanol ? "Ingresar" : "Log In", skin, "default");
        btnRegistro = new TextButton(esEspanol ? "Registrarse" : "Sign Up", skin, "default");
        lblError = new Label("", skin, "error");

        panel.add(lblTitulo).colspan(2).center().padBottom(2).row();
        panel.add(lblSubtitulo).colspan(2).center().padBottom(20).row();
        panel.add(lblUsuario).colspan(2).left().padBottom(4).row();
        panel.add(campoUser).colspan(2).width(280).height(32).padBottom(14).row();
        panel.add(lblContrasena).colspan(2).left().padBottom(4).row();

        btnVerClave = new TextButton(esEspanol ? "Ver" : "Show", skin, "small");
        Table claveRow = new Table();
        claveRow.add(campoClave).expandX().fillX().height(32);
        claveRow.add(btnVerClave).width(50).height(32).padLeft(6);
        panel.add(claveRow).colspan(2).fillX().padBottom(22).row();

        btnVerClave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean oculto = campoClave.isPasswordMode();
                campoClave.setPasswordMode(!oculto);
                btnVerClave.setText(oculto
                        ? (esEspanol ? "Ocultar" : "Hide")
                        : (esEspanol ? "Ver" : "Show"));
            }
        });

        panel.add(btnIngresar).width(130).height(32).padRight(10);
        panel.add(btnRegistro).width(130).height(32).row();
        panel.add(lblError).colspan(2).center().padTop(8).row();
        panel.pack();

        btnIdioma = new TextButton(esEspanol ? "English" : "Espanol", skin);
        Table topRight = new Table();
        topRight.setFillParent(true);
        topRight.top().right().pad(12);
        topRight.add(btnIdioma).width(70).height(26);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        stage.addActor(topRight);
        Gdx.input.setInputProcessor(stage);

        btnIngresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                onIngresar();
            }
        });
        btnRegistro.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                onRegistro();
            }
        });
        btnIdioma.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
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
        if (game.playerManager.logIn(user, clave)) {
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
        game.idiomaGlobal = esEspanol ? "espanol" : "english";
        lblSubtitulo.setText(esEspanol ? "Iniciar Sesion" : "Log In");
        lblUsuario.setText(esEspanol ? "Usuario" : "Username");
        lblContrasena.setText(esEspanol ? "Contrasena" : "Password");
        btnIngresar.setText(esEspanol ? "Ingresar" : "Log In");
        btnRegistro.setText(esEspanol ? "Registrarse" : "Sign Up");
        btnIdioma.setText(esEspanol ? "English" : "Espanol");
        btnVerClave.setText(campoClave.isPasswordMode()
                ? (esEspanol ? "Ver" : "Show")
                : (esEspanol ? "Ocultar" : "Hide"));
    }

    private void mostrarError(String msg) {
        lblError.setText(msg);
        errorTimer = 3f;
    }

}
