package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.Main;

public class LoginScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    
    private Texture texPanel, texField, texFieldActive, texBtn, texBtnHover, texPixel;

    private Label lblError;
    private float errorTimer = 0f;

    private boolean esEspanol = true;
    private Label lblTitulo, lblSubtitulo, lblUsuario, lblContrasena;
    private TextButton btnIngresar, btnRegistro, btnIdioma;
    private TextField campoUser, campoClave;

    public LoginScreen(Main game) {
        this.game = game;
    }


    private Texture crearTextura(int w, int h, Color color) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(color);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private Texture crearTexturaBorde(int w, int h, Color fondo, Color borde, int grosor) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(fondo);
        p.fill();
        p.setColor(borde);
        for (int i = 0; i < grosor; i++) {
            p.drawRectangle(i, i, w - i * 2, h - i * 2);
        }
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }


    private Skin construirSkin() {
        Skin s = new Skin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1f);
        s.add("default-font", font);

        Color cFondo       = new Color(0.13f, 0.13f, 0.20f, 1f); 
        Color cCampo       = new Color(0.07f, 0.07f, 0.16f, 1f);   
        Color cCampoActivo = new Color(0.10f, 0.10f, 0.22f, 1f);  
        Color cBorde       = new Color(0.24f, 0.24f, 0.38f, 1f);   
        Color cBordeActivo = new Color(0.44f, 0.44f, 0.67f, 1f);   
        Color cBoton       = new Color(0.17f, 0.17f, 0.27f, 1f); 
        Color cBotonHover  = new Color(0.21f, 0.21f, 0.35f, 1f);   

        texField       = crearTexturaBorde(16, 16, cCampo,       cBorde,       1);
        texFieldActive = crearTexturaBorde(16, 16, cCampoActivo, cBordeActivo, 1);
        texBtn         = crearTexturaBorde(16, 16, cBoton,       cBorde,       1);
        texBtnHover    = crearTexturaBorde(16, 16, cBotonHover,  cBordeActivo, 1);
        texPanel       = crearTexturaBorde(16, 16, cFondo,       cBorde,       1);
        texPixel       = crearTextura(1, 1, Color.CLEAR);

        NinePatchDrawable npField       = new NinePatchDrawable(new NinePatch(texField,       4, 4, 4, 4));
        NinePatchDrawable npFieldActive = new NinePatchDrawable(new NinePatch(texFieldActive, 4, 4, 4, 4));
        NinePatchDrawable npBtn         = new NinePatchDrawable(new NinePatch(texBtn,         4, 4, 4, 4));
        NinePatchDrawable npBtnHover    = new NinePatchDrawable(new NinePatch(texBtnHover,    4, 4, 4, 4));
        NinePatchDrawable npClear       = new NinePatchDrawable(new NinePatch(texPixel,       0, 0, 0, 0));

        TextField.TextFieldStyle tsField = new TextField.TextFieldStyle();
        tsField.font            = font;
        tsField.fontColor       = new Color(0.88f, 0.88f, 0.97f, 1f);
        tsField.background      = npField;
        tsField.focusedBackground = npFieldActive;
        tsField.cursor          = new TextureRegionDrawable(new TextureRegion(crearTextura(2, 20, Color.WHITE)));
        tsField.selection       = new TextureRegionDrawable(new TextureRegion(crearTextura(1, 1, new Color(0.4f, 0.4f, 0.8f, 0.5f))));
        tsField.messageFontColor = new Color(0.5f, 0.5f, 0.7f, 1f);
        s.add("default", tsField, TextField.TextFieldStyle.class);

        TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle();
        tbStyle.font     = font;
        tbStyle.fontColor = new Color(0.75f, 0.75f, 0.91f, 1f);
        tbStyle.up       = npBtn;
        tbStyle.over     = npBtnHover;
        tbStyle.down     = npBtnHover;
        s.add("default", tbStyle, TextButton.TextButtonStyle.class);

        Label.LabelStyle lsWhite = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle lsMuted = new Label.LabelStyle(font, new Color(0.56f, 0.56f, 0.72f, 1f));
        Label.LabelStyle lsError = new Label.LabelStyle(font, new Color(1f, 0.37f, 0.37f, 1f));
        s.add("default", lsWhite, Label.LabelStyle.class);
        s.add("muted",   lsMuted, Label.LabelStyle.class);
        s.add("error",   lsError, Label.LabelStyle.class);

        Window.WindowStyle wsPanel = new Window.WindowStyle();
        wsPanel.titleFont      = font;
        wsPanel.titleFontColor = Color.CLEAR;
        wsPanel.background     = new NinePatchDrawable(new NinePatch(texPanel, 4, 4, 4, 4));
        s.add("default", wsPanel, Window.WindowStyle.class);

        return s;
    }


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin  = construirSkin();

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        lblTitulo = new Label("SOKOBAN", skin);
        lblTitulo.setStyle(new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));
        lblTitulo.setFontScale(1.4f);

        lblSubtitulo = new Label("Iniciar Sesion", skin, "muted");

        lblUsuario    = new Label("Usuario",     skin, "muted");
        lblContrasena = new Label("Contrasena",  skin, "muted");

        campoUser  = new TextField("", skin);
        campoClave = new TextField("", skin);
        campoClave.setPasswordMode(true);
        campoClave.setPasswordCharacter('*');
        
        btnIngresar = new TextButton("Ingresar",    skin);
        btnRegistro = new TextButton("Registrarse", skin);

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
            @Override public void changed(ChangeEvent e, Actor a) {
                onIngresar();
            }
        });

        btnRegistro.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                onRegistro();
            }
        });

        btnIdioma.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                cambiarIdioma();
            }
        });
    }


    private void onIngresar() {
        String user  = campoUser.getText().trim();
        String clave = campoClave.getText();
        if (user.isEmpty() || clave.isEmpty()) {
            mostrarError(esEspanol ? "Completa todos los campos" : "Fill in all fields");
            return;
        }
        System.out.println("[Login] intentando con usuario: " + user);
        game.playerManager.logIn(user, clave) ;
                game.setScreen(new MenuScreen(game));
        mostrarError(esEspanol ? "Usuario o clave incorrectos" : "Wrong username or password");
    }

    private void onRegistro() {
        System.out.println("[Registro] ir a pantalla de registro");
        game.setScreen(new RegisterScreen(game));
    }

    private void cambiarIdioma() {
        esEspanol = !esEspanol;
        System.out.println("[Idioma] cambiado a: " + (esEspanol ? "Español" : "English"));

        lblSubtitulo.setText(esEspanol ? "Iniciar Sesion"  : "Log In");
        lblUsuario.setText   (esEspanol ? "Usuario"         : "Username");
        lblContrasena.setText(esEspanol ? "Contrasena"      : "Password");
        btnIngresar.setText  (esEspanol ? "Ingresar"        : "Log In");
        btnRegistro.setText  (esEspanol ? "Registrarse"     : "Sign Up");
        btnIdioma.setText    (esEspanol ? "English"         : "Español");
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
            if (errorTimer <= 0) lblError.setText("");
        }

        stage.act(delta);
        stage.draw();
    }


    @Override public void resize(int w, int h)  { stage.getViewport().update(w, h, true); }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    { Gdx.input.setInputProcessor(null); }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        texPanel.dispose();
        texField.dispose();
        texFieldActive.dispose();
        texBtn.dispose();
        texBtnHover.dispose();
        texPixel.dispose();
    }
}