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

public class RegisterScreen implements Screen, InputProcessor {

    private final Main game;
    private SpriteBatch   batch;
    private BitmapFont    font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private CampoTexto campoNombre, campoUser, campoClave, campoConfirm;
    private CampoTexto[] campos;
    private int campoIdx = 0;

    private Boton btnRegistrar, btnVolver, btnIdioma;

    private String msgError = "";
    private float  timerMsg = 0;

    public RegisterScreen(Main game) { this.game = game; }

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
        float aw = 290;

        // Campos con mayor separacion vertical (75px entre cada uno)
        campoNombre  = new CampoTexto(Textos.REG_CAMPO_NOMBRE,    cx - aw/2, cy + 125, aw, 30, false);
        campoUser    = new CampoTexto(Textos.REG_CAMPO_USUARIO,   cx - aw/2, cy + 50,  aw, 30, false);
        campoClave   = new CampoTexto(Textos.REG_CAMPO_CLAVE,     cx - aw/2, cy - 25,  aw, 30, true);
        campoConfirm = new CampoTexto(Textos.REG_CAMPO_CONFIRMAR, cx - aw/2, cy - 100, aw, 30, true);
        campos = new CampoTexto[]{ campoNombre, campoUser, campoClave, campoConfirm };
        campos[0].activo = true;

  
        btnRegistrar = new Boton(Textos.REG_BTN_REGISTRAR, cx - 148, cy - 225, 130, 32);
        btnVolver    = new Boton(Textos.REG_BTN_VOLVER,    cx + 18,  cy - 225, 130, 32);

        // Boton de idioma (esquina superior derecha del panel)
        btnIdioma = new Boton(Textos.REG_BTN_IDIOMA, cx + 165, cy + 230, 50, 22);

        Gdx.input.setInputProcessor(this);
    }

    // ── renderizado
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

       
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - 215, cy - 260, 430, 512);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - 215, cy - 260, 430, 512);
        shape.end();

        for (CampoTexto c : campos) c.dibujar(batch, font, shape);
        btnRegistrar.dibujar(batch, font, shape);
        btnVolver.dibujar(batch, font, shape);
        btnIdioma.dibujar(batch, font, shape);

        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", cx - titulo.width / 2, cy + 238);

        font.setColor(0.6f, 0.6f, 0.8f, 1f);
        String sub = " " + Textos.REG_SUBTITULO + " ";
        GlyphLayout glSub = new GlyphLayout(font, sub);
        font.draw(batch, sub, cx - glSub.width / 2, cy + 218);

        // Requisitos de contrasena
        if (campoIdx == 2 || campoIdx == 3) {
            font.setColor(0.4f, 0.4f, 0.6f, 1f);
          // font.draw(batch, Textos.REG_VER_CLAVE, cx - aw() / 2, cy - 118);
            dibujarRequisitos(campoClave.getTexto(), cx - aw() / 2, cy - 130);
        }

        // Mensaje de error (zona separada de los requisitos)
        timerMsg -= delta;
        if (!msgError.isEmpty() && timerMsg > 0) {
            font.setColor(1f, 0.3f, 0.3f, 1f);
            GlyphLayout g = new GlyphLayout(font, msgError);
            font.draw(batch, msgError, cx - g.width / 2, cy - 112);
        } else if (timerMsg <= 0) {
            msgError = "";
        }
        batch.end();

        if (btnRegistrar.clicado()) confirmar();
        if (btnVolver.clicado())    { game.setScreen(new LoginScreen(game)); dispose(); }
        if (btnIdioma.clicado())    cambiarIdioma();
     
    }

    // Ancho de campo para referenciar en render()
    private float aw() { return 290; }

    // ── Indicadores de requisitos de contrasena ───────────────────────────────
    private void dibujarRequisitos(String clave, float x, float y) {
        boolean len = clave.length() >= 8;
        boolean up  = false, dig = false, sym = false;
        for (char ch : clave.toCharArray()) {
            if (Character.isUpperCase(ch))      up  = true;
            if (Character.isDigit(ch))          dig = true;
            if (!Character.isLetterOrDigit(ch)) sym = true;
        }
        // Dos columnas con separacion amplia
        float col2 = x + 148;
        float step  = 2;
        drawReq(Textos.REG_REQ_LONGITUD,  len, x,    y - step);
        drawReq(Textos.REG_REQ_MAYUSCULA, up,  col2, y - step);
        drawReq(Textos.REG_REQ_NUMERO,    dig, x,    y - step * 8);
        drawReq(Textos.REG_REQ_SIMBOLO,   sym, col2, y - step * 8);
    }

    private void drawReq(String label, boolean ok, float x, float y) {
        font.setColor(ok ? 0.3f : 0.45f, ok ? 0.9f : 0.4f, ok ? 0.3f : 0.4f, 1f);
        font.draw(batch, (ok ? "v " : ". ") + label, x, y);
    }

    // ── Acciones ──────────────────────────────────────────────────────────────
    private void confirmar() {
        String nombre = campoNombre.getTexto().trim();
        String user   = campoUser.getTexto().trim();
        String clave  = campoClave.getTexto();
        String conf   = campoConfirm.getTexto();

        if (nombre.isEmpty() || user.isEmpty() || clave.isEmpty() || conf.isEmpty()) {
            mostrarError(Textos.REG_ERR_CAMPOS); return;
        }
        if (user.length() < 3)   { mostrarError(Textos.REG_ERR_USUARIO_CORTO); return; }
        if (!clave.equals(conf)) { mostrarError(Textos.REG_ERR_CLAVES);        return; }
        if (!claveValida(clave)) { mostrarError(Textos.REG_ERR_REQUISITOS);    return; }

        if (game.playerManager.registrarUser(user, clave, nombre)) {
            // Auto-login: inicia sesion automaticamente y va al menu
            game.playerManager.logIn(user, clave);
            game.setScreen(new MenuScreen(game));
            dispose();
        } else {
            mostrarError(Textos.REG_ERR_EXISTE);
        }
    }

    private boolean claveValida(String c) {
        if (c.length() < 8) return false;
        boolean u = false, d = false, s = false;
        for (char ch : c.toCharArray()) {
            if (Character.isUpperCase(ch))      u = true;
            if (Character.isDigit(ch))          d = true;
            if (!Character.isLetterOrDigit(ch)) s = true;
        }
        return u && d && s;
    }

    private void cambiarIdioma() {
        Textos.aplicar(Textos.siguienteIdioma());
        game.setScreen(new RegisterScreen(game));
        dispose();
    }

    private void mostrarError(String m) { msgError = m; timerMsg = 3.5f; }

    // ── InputProcessor ────────────────────────────────────────────────────────
    @Override public boolean keyDown(int k) {
        if (k == Input.Keys.BACKSPACE) { campos[campoIdx].borrar(); return true; }
        if (k == Input.Keys.TAB) {
            campos[campoIdx].activo = false;
            campoIdx = (campoIdx + 1) % campos.length;
            campos[campoIdx].activo = true;
            return true;
        }
        return false;
    }
    @Override public boolean keyUp(int k) { return false; }
    @Override public boolean keyTyped(char c) {
        if (c != '\b' && c != '\n' && c != '\r') campos[campoIdx].escribir(c);
        return true;
    }
    @Override public boolean touchDown(int sx, int sy, int p, int b) {
        float gy = Gdx.graphics.getHeight() - sy;
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].toca(sx, gy)) {
                campos[campoIdx].activo = false;
                campoIdx = i;
                campos[campoIdx].activo = true;
                break;
            }
        }
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
