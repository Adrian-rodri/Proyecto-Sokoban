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
import java.util.ArrayList;

public class RecordsScreen implements Screen {

    private final Main game;
    private SpriteBatch   batch;
    private BitmapFont    font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private Boton btnVolver;
    private int   totalNiveles;

    /**
     * Por cada nivel: {puntaje, movimientos, tiempo_en_segundos}.
     * null si no hay ningún intento para ese nivel.
     */
    private int[][] mejores;

    public RecordsScreen(Main game) { this.game = game; }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        totalNiveles = game.nivelManager.getCantidad();
        mejores      = new int[totalNiveles][];
        computarMejores();

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;
        btnVolver = new Boton(Textos.REC_BTN_VOLVER, cx - 70, cy - 230, 140, 32);
    }

    /**
     * Recorre el historial del jugador y guarda el mejor puntaje por nivel.
     * "Mejor" = mayor puntaje (la formula del juego ya penaliza movimientos y tiempo).
     */
    private void computarMejores() {
        Player p = game.playerManager.getPlayerLogeado();
        if (p == null) return;
        ArrayList<EntradaHistorial> hist = p.getHistorial();
        if (hist == null) return;

        for (EntradaHistorial e : hist) {
            int nv = e.getNivel();
            if (nv < 0 || nv >= totalNiveles) continue;
            if (mejores[nv] == null || e.getPuntaje() > mejores[nv][0]) {
                mejores[nv] = new int[]{ e.getPuntaje(), e.getMovimientos(), (int)e.getTiempo() };
            }
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

        // Panel central
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - 280, cy - 235, 560, 475);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - 280, cy - 235, 560, 475);
        shape.end();

        // Linea separadora del encabezado
        float headerY = cy + 185;
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.line(cx - 265, headerY - 4, cx + 265, headerY - 4);
        shape.end();

        btnVolver.dibujar(batch, font, shape);

        // Columnas: x de inicio de cada columna
        float colNivel   = cx - 260;
        float colPuntaje = cx - 60;
        float colMovs    = cx + 80;
        float colTiempo  = cx + 170;

        batch.begin();

        // Titulo
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, Textos.REC_TITULO);
        font.draw(batch, Textos.REC_TITULO, cx - titulo.width / 2f, cy + 223);

        // Hint
        font.setColor(0.45f, 0.45f, 0.65f, 1f);
        GlyphLayout hint = new GlyphLayout(font, Textos.REC_HINT);
        font.draw(batch, Textos.REC_HINT, cx - hint.width / 2f, cy + 202);

        // Encabezados de columnas
        font.setColor(0.65f, 0.65f, 0.88f, 1f);
        font.draw(batch, Textos.REC_COL_NIVEL,   colNivel,   headerY);
        font.draw(batch, Textos.REC_COL_PUNTAJE, colPuntaje, headerY);
        font.draw(batch, Textos.REC_COL_MOVS,    colMovs,    headerY);
        font.draw(batch, Textos.REC_COL_TIEMPO,  colTiempo,  headerY);

        // Filas por nivel
        float rowStep = 36f;
        float rowY    = headerY - rowStep;

        for (int i = 0; i < totalNiveles; i++) {
            String nombreNivel = game.nivelManager.getNivel(i).getName().replace(".txt", "");

            if (mejores[i] == null) {
                // Sin intentos
                font.setColor(0.40f, 0.40f, 0.50f, 1f);
                font.draw(batch, nombreNivel, colNivel, rowY);
                font.draw(batch, Textos.REC_SIN_DATOS, colPuntaje, rowY);
            } else {
                int pts  = mejores[i][0];
                int movs = mejores[i][1];
                int segs = mejores[i][2];
                String tiempo = String.format("%02d:%02d", segs / 60, segs % 60);

                // Resaltar el mejor nivel con mas puntaje en dorado
                boolean esMejorGeneral = esMejorDeTodos(i);
                if (esMejorGeneral) font.setColor(1f, 0.85f, 0.2f, 1f);
                else                 font.setColor(0.80f, 0.80f, 0.95f, 1f);

                font.draw(batch, nombreNivel,       colNivel,   rowY);
                font.draw(batch, String.valueOf(pts), colPuntaje, rowY);
                font.draw(batch, String.valueOf(movs), colMovs,   rowY);
                font.draw(batch, tiempo,             colTiempo,  rowY);
            }
            rowY -= rowStep;
        }
        batch.end();

        if (btnVolver.clicado()) { game.setScreen(new ProfileScreen(game)); dispose(); }
    }

    /** Verdadero si el nivel i tiene el puntaje mas alto entre todos los niveles con datos. */
    private boolean esMejorDeTodos(int idx) {
        if (mejores[idx] == null) return false;
        int max = mejores[idx][0];
        for (int i = 0; i < totalNiveles; i++) {
            if (i != idx && mejores[i] != null && mejores[i][0] > max) return false;
        }
        return true;
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        btnVolver = new Boton(Textos.REC_BTN_VOLVER, w / 2f - 70, h / 2f - 230, 140, 32);
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
