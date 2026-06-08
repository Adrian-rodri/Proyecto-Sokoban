package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoginScreen implements Screen, InputProcessor {

    private final Main game;
    private SpriteBatch  batch;
    private BitmapFont   font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private CampoTexto campoUser, campoClave, campoActivo;

    private Boton btnIngresar, btnRegistro, btnIdioma;

    private String msgError = "";
    private float  timerError = 0;

    public LoginScreen(Main game) { this.game = game; }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        campoUser  = new CampoTexto(Textos.LOGIN_CAMPO_USUARIO, cx - 140, cy + 30, 280, 32, false);
        campoClave = new CampoTexto(Textos.LOGIN_CAMPO_CLAVE,   cx - 140, cy - 40, 280, 32, true);
        campoActivo = campoUser;
        campoUser.activo = true;

        // Botones más abajo y con más separación entre ellos
        btnIngresar = new Boton(Textos.LOGIN_BTN_INGRESAR, cx - 148, cy - 118, 130, 32);
        btnRegistro = new Boton(Textos.LOGIN_BTN_REGISTRO, cx + 18,  cy - 118, 130, 32);

        btnIdioma = new Boton(Textos.LOGIN_BTN_IDIOMA, cx + 134, cy + 158, 60, 22);

        Gdx.input.setInputProcessor(this);
    }

    // ── render ────────────────────────────────────────────────────────────────
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        // Panel ligeramente mas alto para acomodar botones mas bajos
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - 195, cy - 135, 390, 315);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - 195, cy - 135, 390, 315);
        shape.end();

        campoUser.dibujar(batch, font, shape);
        campoClave.dibujar(batch, font, shape);
        btnIngresar.dibujar(batch, font, shape);
        btnRegistro.dibujar(batch, font, shape);
        btnIdioma.dibujar(batch, font, shape);

        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", cx - titulo.width / 2, cy + 165);

        font.setColor(0.6f, 0.6f, 0.8f, 1f);
        String sub = " " + Textos.LOGIN_SUBTITULO + " ";
        GlyphLayout glSub = new GlyphLayout(font, sub);
        font.draw(batch, sub, cx - glSub.width / 2, cy + 145);

        if (campoActivo == campoClave) {
            font.setColor(0.4f, 0.4f, 0.6f, 1f);
          //  font.draw(batch, Textos.LOGIN_VER_CLAVE, cx - 140, cy - 70);
        }

        timerError -= delta;
        if (!msgError.isEmpty() && timerError > 0) {
            font.setColor(1f, 0.3f, 0.3f, 1f);
            GlyphLayout glErr = new GlyphLayout(font, msgError);
            font.draw(batch, msgError, cx - glErr.width / 2, cy - 58);
        } else if (timerError <= 0) {
            msgError = "";
        }
        batch.end();

        if (btnIngresar.clicado()) confirmarLogin();
        if (btnRegistro.clicado()) irARegistro();
        if (btnIdioma.clicado())   cambiarIdioma();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) campoClave.toggleVerClave();
    }

    // ── Acciones ──────────────────────────────────────────────────────────────
    private void confirmarLogin() {
        String user  = campoUser.getTexto().trim();
        String clave = campoClave.getTexto();
        if (user.isEmpty() || clave.isEmpty()) { mostrarError(Textos.LOGIN_ERR_CAMPOS); return; }
        if (game.playerManager.logIn(user, clave)) {
            game.setScreen(new MenuScreen(game));
            dispose();
        } else {
            mostrarError(Textos.LOGIN_ERR_CREDENCIALES);
        }
    }

    private void irARegistro() {
        game.setScreen(new RegisterScreen(game));
        dispose();
    }

    private void cambiarIdioma() {
        Textos.aplicar(Textos.siguienteIdioma());
        game.setScreen(new LoginScreen(game));
        dispose();
    }

    private void mostrarError(String msg) { msgError = msg; timerError = 3f; }

    // ── InputProcessor ────────────────────────────────────────────────────────
    @Override public boolean keyDown(int k) {
        if (k == Input.Keys.BACKSPACE) { campoActivo.borrar(); return true; }
        if (k == Input.Keys.TAB) {
            campoActivo.activo = false;
            campoActivo = (campoActivo == campoUser) ? campoClave : campoUser;
            campoActivo.activo = true;
            return true;
        }
        return false;
    }
    @Override public boolean keyUp(int k) { return false; }
    @Override public boolean keyTyped(char c) {
        if (c != '\b' && c != '\n' && c != '\r') campoActivo.escribir(c);
        return true;
    }
    @Override public boolean touchDown(int sx, int sy, int p, int b) {
        float gy = Gdx.graphics.getHeight() - sy;
        if      (campoUser.toca(sx, gy))  { campoActivo.activo = false; campoActivo = campoUser;  campoActivo.activo = true; }
        else if (campoClave.toca(sx, gy)) { campoActivo.activo = false; campoActivo = campoClave; campoActivo.activo = true; }
        return true;
    }
    @Override public boolean touchUp(int sx, int sy, int p, int b)        { return false; }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) { return false; }
    @Override public boolean touchDragged(int sx, int sy, int p)          { return false; }
    @Override public boolean mouseMoved(int sx, int sy)                    { return false; }
    @Override public boolean scrolled(float ax, float ay)                  { return false; }

    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   { Gdx.input.setInputProcessor(null); }
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
