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

public class LevelSelectScreen implements Screen {

    private final Main game;
    private SpriteBatch   batch;
    private BitmapFont    font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private int totalNiveles;
    private int nivelesDisponibles;   // cuantos puede jugar (includes replay)
    private int nivelesDesbloqueados; // cuantos han sido completados al menos una vez

    private Boton btnVolver;

    private static final float TILE_W = 88;
    private static final float TILE_H = 64;
    private static final float GAP    = 12;
    private static final int   COLS   = 5;

    public LevelSelectScreen(Main game) { this.game = game; }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        totalNiveles = game.nivelManager.getCantidad();

        Player p = game.playerManager.getPlayerLogeado();
        nivelesDesbloqueados = (p != null) ? p.getNivelesDesbloqueados() : 0;
        // nivelesDisponibles = todos los completados + el siguiente a jugar
        nivelesDisponibles = Math.min(nivelesDesbloqueados + 1, totalNiveles);

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;
        btnVolver = new Boton(Textos.NIVEL_BTN_VOLVER, cx - 70, cy - 225, 140, 32);
    }

    // ── render ────────────────────────────────────────────────────────────────
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float cx   = Gdx.graphics.getWidth()  / 2f;
        float cy   = Gdx.graphics.getHeight() / 2f;
        int   cols = Math.min(totalNiveles, COLS);
        float gridW  = cols * (TILE_W + GAP) - GAP;
        float startX = cx - gridW / 2f;
        float startY = cy + 150;

        // Titulo
        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, Textos.NIVEL_TITULO);
        font.draw(batch, Textos.NIVEL_TITULO, cx - titulo.width / 2f, cy + 235);
        batch.end();

        // Detectar nivel bajo el mouse
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        int   hoveredIdx = -1;

        for (int i = 0; i < totalNiveles; i++) {
            int   col = i % cols;
            int   row = i / cols;
            float tx  = startX + col * (TILE_W + GAP);
            float ty  = startY - row * (TILE_H + GAP);
            if (mx >= tx && mx <= tx + TILE_W && my >= ty - TILE_H && my <= ty)
                hoveredIdx = i;
        }

        // Dibujar tiles
        for (int i = 0; i < totalNiveles; i++) {
            int   col = i % cols;
            int   row = i / cols;
            float tx  = startX + col * (TILE_W + GAP);
            float ty  = startY - row * (TILE_H + GAP);

            // Tres estados: completado / siguiente-disponible / bloqueado
            boolean bloqueado  = i >= nivelesDisponibles;
            boolean completado = !bloqueado && i < nivelesDesbloqueados;
            boolean siguiente  = !bloqueado && !completado; // i == nivelesDesbloqueados
            boolean hover      = i == hoveredIdx && !bloqueado;

            // Fondo del tile
            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (hover && completado)    shape.setColor(0.30f, 0.28f, 0.12f, 1f); // dorado hover
            else if (hover)             shape.setColor(0.28f, 0.28f, 0.50f, 1f); // azul hover
            else if (completado)        shape.setColor(0.20f, 0.18f, 0.08f, 1f); // dorado oscuro
            else if (siguiente)         shape.setColor(0.18f, 0.18f, 0.30f, 1f); // azul normal
            else                        shape.setColor(0.11f, 0.11f, 0.16f, 1f); // bloqueado
            shape.rect(tx, ty - TILE_H, TILE_W, TILE_H);
            shape.end();

            // Borde del tile
            shape.begin(ShapeRenderer.ShapeType.Line);
            if (hover && completado)    shape.setColor(1.0f, 0.85f, 0.25f, 1f); // borde dorado hover
            else if (hover)             shape.setColor(0.70f, 0.70f, 1.00f, 1f); // borde azul hover
            else if (completado)        shape.setColor(0.75f, 0.65f, 0.20f, 1f); // borde dorado
            else if (siguiente)         shape.setColor(0.40f, 0.40f, 0.65f, 1f); // borde azul
            else                        shape.setColor(0.22f, 0.22f, 0.30f, 1f); // borde bloq
            shape.rect(tx, ty - TILE_H, TILE_W, TILE_H);
            shape.end();

            // Etiqueta del tile
            batch.begin();
            String label = Textos.NIVEL_PREFIJO + (i + 1);
            if (hover)        font.setColor(Color.YELLOW);
            else if (completado) font.setColor(0.95f, 0.85f, 0.40f, 1f); // dorado
            else if (siguiente)  font.setColor(Color.WHITE);
            else                 font.setColor(0.30f, 0.30f, 0.30f, 1f); // gris bloq

            GlyphLayout gl = new GlyphLayout(font, label);
            font.draw(batch, label, tx + TILE_W / 2f - gl.width / 2f, ty - TILE_H / 2f + 7);

            // Indicador de estado bajo el numero
            if (bloqueado) {
                font.setColor(0.35f, 0.35f, 0.35f, 1f);
                GlyphLayout lk = new GlyphLayout(font, Textos.NIVEL_BLOQUEADO);
                font.draw(batch, Textos.NIVEL_BLOQUEADO, tx + TILE_W / 2f - lk.width / 2f, ty - TILE_H / 2f - 7);
            } else if (completado) {
                font.setColor(hover ? Color.YELLOW : new Color(0.85f, 0.75f, 0.20f, 1f));
                GlyphLayout lc = new GlyphLayout(font, Textos.NIVEL_COMPLETADO);
                font.draw(batch, Textos.NIVEL_COMPLETADO, tx + TILE_W / 2f - lc.width / 2f, ty - TILE_H / 2f - 7);
            }
            batch.end();
        }

        // Info del nivel bajo el cursor + hint de replay si es nivel completado
        if (hoveredIdx >= 0 && hoveredIdx < nivelesDisponibles) {
            Nivel  nv    = game.nivelManager.getNivel(hoveredIdx);
            String nombre = nv.getName().replace(".txt", "");
            String info   = nombre + "   -   " + Textos.NIVEL_CAJAS + nv.getCantidadCajas();
            batch.begin();
            font.setColor(0.65f, 0.65f, 0.9f, 1f);
            GlyphLayout glInfo = new GlyphLayout(font, info);
            font.draw(batch, info, cx - glInfo.width / 2f, cy - 115);

          
            batch.end();
        }

        // Leyenda de colores
        batch.begin();
        font.setColor(0.75f, 0.65f, 0.20f, 1f);
        font.draw(batch, "       " + (Textos.NIVEL_COMPLETADO.trim()), cx - 130, cy - 150);
        font.setColor(0.55f, 0.55f, 0.85f, 1f);
        font.draw(batch, "     disponible", cx + 10, cy - 150);
        batch.end();

        btnVolver.dibujar(batch, font, shape);

        // Logica de clics
        boolean clicado = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        if (clicado && hoveredIdx >= 0 && hoveredIdx < nivelesDisponibles) {
            GameScreen.initPlayer = false;
            game.setScreen(new GameScreen(game, hoveredIdx));
            dispose();
            return;
        }
        if (btnVolver.clicado()) { game.setScreen(new MenuScreen(game)); dispose(); }
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        btnVolver = new Boton(Textos.NIVEL_BTN_VOLVER, w / 2f - 70, h / 2f - 225, 140, 32);
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
