package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

    private final Main game;
    private SpriteBatch   batch;
    private BitmapFont    font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    // Indices de botones para el switch
    private static final int IDX_JUGAR  = 0;
    private static final int IDX_PERFIL = 1;
    private static final int IDX_IDIOMA = 2;
    private static final int IDX_SESION = 3;
    private static final int IDX_SALIR  = 4;

    private Boton[] botones;

    public MenuScreen(Main game) { this.game = game; }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        crearBotones();
    }

    /** Crea los botones con posiciones relativas al centro. */
    private void crearBotones() {
        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        float bw = 260, bh = 38, gap = 8;
        int   nb = 5;
        float totalH = nb * bh + (nb - 1) * gap;  // 222 px
        float grpTop = cy + totalH / 2f;            // tope del grupo

        String[] labels = {
            Textos.MENU_BTN_JUGAR,
            Textos.MENU_BTN_PERFIL,
            Textos.MENU_BTN_IDIOMA,
            Textos.MENU_BTN_SESION,
            Textos.MENU_BTN_SALIR
        };

        botones = new Boton[nb];
        for (int i = 0; i < nb; i++) {
            float by = grpTop - bh - i * (bh + gap);
            botones[i] = new Boton(labels[i], cx - bw / 2f, by, bw, bh);
        }
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

        // Calcular limites del panel para fondo
        float bw = 260, bh = 38, gap = 8;
        int   nb = botones.length;
        float totalH = nb * bh + (nb - 1) * gap;
        float panelY = cy - totalH / 2f - 14;
        float panelH = totalH + 28;
        float panelW = bw + 20;

        // Panel de fondo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - panelW / 2f, panelY, panelW, panelH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - panelW / 2f, panelY, panelW, panelH);
        shape.end();

        // Botones
        for (Boton b : botones) b.dibujar(batch, font, shape);

        // Titulo y bienvenida
        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", cx - titulo.width / 2f, panelY + panelH + 48);

        font.setColor(0.6f, 0.6f, 0.85f, 1f);
        String bienvenida = Textos.MENU_SALUDO + game.playerManager.getNombreJugador();
        GlyphLayout bl = new GlyphLayout(font, bienvenida);
        font.draw(batch, bienvenida, cx - bl.width / 2f, panelY + panelH + 26);
        batch.end();


        // ── Logica de clics ──────────────────────────────────────────────────
        if (botones[IDX_JUGAR].clicado())  irANiveles();
        if (botones[IDX_PERFIL].clicado()) irAPerfil();
        if (botones[IDX_IDIOMA].clicado()) cambiarIdioma();
        if (botones[IDX_SESION].clicado()) cerrarSesion();
        if (botones[IDX_SALIR].clicado())  Gdx.app.exit();

    }

    // ── Acciones ──────────────────────────────────────────────────────────────
    private void irANiveles() {
        game.setScreen(new LevelSelectScreen(game));
        dispose();
    }

    private void irAPerfil() {
        game.setScreen(new ProfileScreen(game));
        dispose();
    }

    private void cambiarIdioma() {
        String sig = Textos.siguienteIdioma();
        Textos.aplicar(sig);
        game.playerManager.cambiarIdioma(sig);   // persiste en perfil.skb si hay sesion
        game.setScreen(new MenuScreen(game));
        dispose();
    }

    private void cerrarSesion() {
        game.playerManager.cerrarSesion();
        game.setScreen(new LoginScreen(game));
        dispose();
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        crearBotones(); // recalcula posiciones si cambia el tamano
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
