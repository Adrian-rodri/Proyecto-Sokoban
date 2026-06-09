package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class VictoryScreen implements Screen {

    private final Main   game;
    private SpriteBatch  batch;
    private BitmapFont   font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private final int    numLevel, movimientos, puntaje;
    private final double tiempoSegundos;
    private final boolean hayNivelSiguiente;

    // btnSiguiente puede ser null si no hay nivel siguiente
    private Boton btnSiguiente, btnNiveles, btnMenu;

    public VictoryScreen(Main game, int numLevel, int movimientos, double tiempoSegundos, int puntaje) {
        this.game            = game;
        this.numLevel        = numLevel;
        this.movimientos     = movimientos;
        this.tiempoSegundos  = tiempoSegundos;
        this.puntaje         = puntaje;
        this.hayNivelSiguiente = numLevel + 1 < game.nivelManager.getCantidad();
    }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

       // game.playerManager.actualizarTrasPartida(numLevel, movimientos, tiempoSegundos, puntaje);

        crearBotones();
    }

    private void crearBotones() {
        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        float bw  = 90, bh = 30, sep = 12;
        int   nb  = hayNivelSiguiente ? 3 : 2;
        float totalW = nb * bw + (nb - 1) * sep;
        float startX = cx - totalW / 2f;
        float byBtn  = cy - 132;

        if (hayNivelSiguiente) {
            btnSiguiente = new Boton(Textos.VIC_BTN_SIGUIENTE, startX,           byBtn, bw, bh);
            btnNiveles   = new Boton(Textos.VIC_BTN_NIVELES,   startX + 102,     byBtn, bw, bh);
            btnMenu      = new Boton(Textos.VIC_BTN_MENU,      startX + 204,     byBtn, bw, bh);
        } else {
            btnSiguiente = null;
            btnNiveles   = new Boton(Textos.VIC_BTN_NIVELES, startX,       byBtn, bw, bh);
            btnMenu      = new Boton(Textos.VIC_BTN_MENU,    startX + 102, byBtn, bw, bh);
        }

        // Tema verde para esta pantalla
        aplicarTemaVerde(btnNiveles);
        aplicarTemaVerde(btnMenu);
        if (btnSiguiente != null) aplicarTemaVerde(btnSiguiente);
    }

    private void aplicarTemaVerde(Boton b) {
        b.setColorFondo(0.14f, 0.23f, 0.17f, 1f);
        b.setColorHover(0.22f, 0.38f, 0.27f, 1f);
        b.setColorBorde(0.30f, 0.65f, 0.40f, 1f);
    }

    // ── render ────────────────────────────────────────────────────────────────
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.06f, 0.1f, 0.07f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;

        // Panel
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.18f, 0.12f, 1f);
        shape.rect(cx - 200, cy - 150, 400, 330);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.3f, 0.65f, 0.4f, 1f);
        shape.rect(cx - 200, cy - 150, 400, 330);
        shape.end();

        // Botones
        if (btnSiguiente != null) btnSiguiente.dibujar(batch, font, shape);
        btnNiveles.dibujar(batch, font, shape);
        btnMenu.dibujar(batch, font, shape);

        // Textos de resultados
        int  mins = (int)(tiempoSegundos / 60);
        int  segs = (int)(tiempoSegundos % 60);
        float lx  = cx - 150;

        batch.begin();
        font.setColor(0.35f, 1f, 0.5f, 1f);
        GlyphLayout win = new GlyphLayout(font, Textos.VIC_TITULO);
        font.draw(batch, Textos.VIC_TITULO, cx - win.width / 2f, cy + 158);

        font.setColor(Color.WHITE);
        String strNivel = Textos.VIC_PREFIJO_NIVEL + (numLevel + 1);
        GlyphLayout nv  = new GlyphLayout(font, strNivel);
        font.draw(batch, strNivel, cx - nv.width / 2f, cy + 128);

        font.setColor(0.75f, 0.9f, 0.78f, 1f);
        font.draw(batch, Textos.VIC_MOVS    + movimientos,                                lx, cy + 88);
        font.draw(batch, Textos.VIC_TIEMPO  + String.format("%02d:%02d", mins, segs),   lx, cy + 60);
        font.draw(batch, Textos.VIC_PUNTAJE + puntaje,                                   lx, cy + 32);

        if (!hayNivelSiguiente) {
            font.setColor(1f, 0.85f, 0.25f, 1f);
            GlyphLayout fin = new GlyphLayout(font, Textos.VIC_TODOS);
            font.draw(batch, Textos.VIC_TODOS, cx - fin.width / 2f, cy - 55);
        }
        batch.end();

        // ── Logica de clics ──────────────────────────────────────────────────
        if (btnSiguiente != null && btnSiguiente.clicado()) {
            GameScreen.initPlayer = false;
            game.setScreen(new GameScreen(game, numLevel + 1));
            dispose();
        }
        if (btnNiveles.clicado()) { game.setScreen(new LevelSelectScreen(game)); dispose(); }
        if (btnMenu.clicado())    { game.setScreen(new MenuScreen(game));         dispose(); }
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        crearBotones();
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
