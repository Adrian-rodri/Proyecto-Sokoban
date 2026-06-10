package io.github.some_example_name.screens;

import io.github.some_example_name.screens.MenuScreen;
import io.github.some_example_name.screens.LoginScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class RegisterScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private TextField campoNombre, campoUser, campoClave, campoConfirm;
    private Label lblError;
    private Label lblReqLen, lblReqMay, lblReqNum, lblReqSim;
    private Table tablaRequisitos;

    private Texture texField, texFieldFocus, texBtn, texBtnHov, texPixel;

    public RegisterScreen(Main game) {
        this.game = game;
    }

    private Texture bordeTex(int w, int h, Color fondo, Color borde, int g) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(fondo);
        p.fill();
        p.setColor(borde);
        for (int i = 0; i < g; i++) {
            p.drawRectangle(i, i, w - i * 2, h - i * 2);
        }
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        skin = crearSkin();

        campoNombre = new TextField("", skin);
        campoUser = new TextField("", skin);
        campoClave = new TextField("", skin);
        campoConfirm = new TextField("", skin);

        campoNombre.setMessageText("Nombre completo");
        campoUser.setMessageText("Usuario");
        campoClave.setMessageText("Contrasena");
        campoConfirm.setMessageText("Confirmar contrasena");
        campoClave.setPasswordMode(true);
        campoClave.setPasswordCharacter('*');
        campoConfirm.setPasswordMode(true);
        campoConfirm.setPasswordCharacter('*');

        lblError = new Label("", skin, "error");

        lblReqLen = new Label(". Min 8 caracteres", skin, "req-nok");
        lblReqMay = new Label(". Una mayuscula", skin, "req-nok");
        lblReqNum = new Label(". Un numero", skin, "req-nok");
        lblReqSim = new Label(". Un simbolo", skin, "req-nok");

        tablaRequisitos = new Table();
        tablaRequisitos.add(lblReqLen).left().padRight(16);
        tablaRequisitos.add(lblReqMay).left().row();
        tablaRequisitos.add(lblReqNum).left().padRight(16);
        tablaRequisitos.add(lblReqSim).left();

        campoClave.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                actualizarRequisitos(field.getText());
            }
        });

        TextButton btnRegistrar = new TextButton("Registrarse", skin, "azul");
        TextButton btnVolver = new TextButton("Volver", skin, "gris");
        TextButton btnIdioma = new TextButton("ES/EN", skin, "gris");

        btnRegistrar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmar();
            }
        });
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });
        btnIdioma.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new RegisterScreen(game));
                dispose();
            }
        });

        float fw = 290, fh = 30, bw = 130, bh = 32;

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(campoNombre).width(fw).height(fh).padBottom(14).row();
        table.add(campoUser).width(fw).height(fh).padBottom(14).row();
        table.add(campoClave).width(fw).height(fh).padBottom(6).row();
        table.add(tablaRequisitos).width(fw).padBottom(8).row();
        table.add(campoConfirm).width(fw).height(fh).padBottom(14).row();
        table.add(lblError).width(fw).padBottom(8).row();

        Table btnRow = new Table();
        btnRow.add(btnRegistrar).width(bw).height(bh).padRight(10);
        btnRow.add(btnVolver).width(bw).height(bh);
        table.add(btnRow).padBottom(10).row();
        //table.add(btnIdioma).width(50).height(22);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private Skin crearSkin() {
        Skin s = new Skin();
        BitmapFont f = new BitmapFont();
        s.add("default-font", f, BitmapFont.class);

        texField = bordeTex(16, 16, new Color(0.15f, 0.15f, 0.25f, 1f), new Color(0.35f, 0.35f, 0.55f, 1f), 1);
        texFieldFocus = bordeTex(16, 16, new Color(0.18f, 0.18f, 0.30f, 1f), new Color(0.55f, 0.55f, 0.85f, 1f), 1);
        texBtn = bordeTex(16, 16, new Color(0.20f, 0.38f, 0.76f, 1f), new Color(0.15f, 0.30f, 0.60f, 1f), 1);
        texBtnHov = bordeTex(16, 16, new Color(0.28f, 0.50f, 0.95f, 1f), new Color(0.20f, 0.40f, 0.80f, 1f), 1);
        texPixel = bordeTex(1, 1, Color.CLEAR, Color.CLEAR, 0);

        NinePatchDrawable npField = new NinePatchDrawable(new NinePatch(texField, 4, 4, 4, 4));
        NinePatchDrawable npFieldFocus = new NinePatchDrawable(new NinePatch(texFieldFocus, 4, 4, 4, 4));
        NinePatchDrawable npBtn = new NinePatchDrawable(new NinePatch(texBtn, 4, 4, 4, 4));
        NinePatchDrawable npBtnHov = new NinePatchDrawable(new NinePatch(texBtnHov, 4, 4, 4, 4));
        NinePatchDrawable npClear = new NinePatchDrawable(new NinePatch(texPixel, 0, 0, 0, 0));

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = f;
        tfStyle.fontColor = Color.WHITE;
        tfStyle.messageFontColor = new Color(0.45f, 0.45f, 0.6f, 1f);
        tfStyle.background = npField;
        tfStyle.focusedBackground = npFieldFocus;
        tfStyle.cursor = npFieldFocus;
        s.add("default", tfStyle, TextField.TextFieldStyle.class);

        TextButton.TextButtonStyle tbAzul = new TextButton.TextButtonStyle();
        tbAzul.font = f;
        tbAzul.fontColor = Color.WHITE;
        tbAzul.up = npBtn;
        tbAzul.over = npBtnHov;
        tbAzul.down = npBtnHov;
        s.add("azul", tbAzul, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle tbGris = new TextButton.TextButtonStyle();
        tbGris.font = f;
        tbGris.fontColor = new Color(0.75f, 0.75f, 0.9f, 1f);
        tbGris.up = npClear;
        tbGris.over = npClear;
        tbGris.down = npClear;
        s.add("gris", tbGris, TextButton.TextButtonStyle.class);

        Label.LabelStyle lsError = new Label.LabelStyle(f, new Color(1f, 0.3f, 0.3f, 1f));
        s.add("error", lsError, Label.LabelStyle.class);

        Label.LabelStyle lsOk = new Label.LabelStyle(f, new Color(0.3f, 0.9f, 0.3f, 1f));
        Label.LabelStyle lsNok = new Label.LabelStyle(f, new Color(0.45f, 0.45f, 0.55f, 1f));
        s.add("req-ok", lsOk, Label.LabelStyle.class);
        s.add("req-nok", lsNok, Label.LabelStyle.class);

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

        // Panel de fondo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - 215, cy - 260, 430, 512);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - 215, cy - 260, 430, 512);
        shape.end();

        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", cx - titulo.width / 2, cy + 238);
        font.setColor(0.6f, 0.6f, 0.8f, 1f);
        String sub = "Crear cuenta";
        GlyphLayout glSub = new GlyphLayout(font, sub);
        font.draw(batch, sub, cx - glSub.width / 2, cy + 218);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void actualizarRequisitos(String clave) {
        boolean len = clave.length() >= 8;
        boolean may = false, num = false, sim = false;
        for (char ch : clave.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                may = true;
            }
            if (Character.isDigit(ch)) {
                num = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                sim = true;
            }
        }
        setReq(lblReqLen, len, "v Min 8 caracteres", ". Min 8 caracteres");
        setReq(lblReqMay, may, "v Una mayuscula", ". Una mayuscula");
        setReq(lblReqNum, num, "v Un numero", ". Un numero");
        setReq(lblReqSim, sim, "v Un simbolo", ". Un simbolo");
    }

    private void setReq(Label lbl, boolean ok, String txtOk, String txtNok) {
        lbl.setText(ok ? txtOk : txtNok);
        lbl.setStyle(skin.get(ok ? "req-ok" : "req-nok", Label.LabelStyle.class));
    }

    private void confirmar() {
        String nombre = campoNombre.getText().trim();
        String user = campoUser.getText().trim();
        String clave = campoClave.getText();
        String conf = campoConfirm.getText();

        if (nombre.isEmpty() || user.isEmpty() || clave.isEmpty() || conf.isEmpty()) {
            lblError.setText("Completa todos los campos");
            return;
        }
        if (user.length() < 3) {
            lblError.setText("Usuario muy corto (min 3)");
            return;
        }
        if (!clave.equals(conf)) {
            lblError.setText("Las contrasenas no coinciden");
            return;
        }
        if (!claveValida(clave)) {
            lblError.setText("La contrasena no cumple requisitos");
            return;
        }

        if (game.playerManager.registrarUser(user, clave, nombre)) {
            game.playerManager.logIn(user, clave);
            game.setScreen(new MenuScreen(game));
            dispose();
        } else {
            lblError.setText("El usuario ya existe");
        }
    }

    private boolean claveValida(String c) {
        if (c.length() < 8) {
            return false;
        }
        boolean u = false, d = false, s = false;
        for (char ch : c.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                u = true;
            }
            if (Character.isDigit(ch)) {
                d = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                s = true;
            }
        }
        return u && d && s;
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
        texField.dispose();
        texFieldFocus.dispose();
        texBtn.dispose();
        texBtnHov.dispose();
        texPixel.dispose();
    }
}
