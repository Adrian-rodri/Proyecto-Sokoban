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

public class ProfileScreen implements Screen {

    private final Main game;
    private SpriteBatch   batch;
    private BitmapFont    font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    private ArrayList<String[]> ranking;
    private Boton btnVolver, btnRecords;

    public ProfileScreen(Main game) { this.game = game; }

    // ── show ──────────────────────────────────────────────────────────────────
    @Override
    public void show() {
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ranking = game.playerManager.getRanking();

        float cx = Gdx.graphics.getWidth()  / 2f;
        float cy = Gdx.graphics.getHeight() / 2f;
        btnVolver  = new Boton(Textos.PERFIL_BTN_VOLVER,  cx - 170, cy - 248, 155, 32);
        btnRecords = new Boton(Textos.PERFIL_BTN_RECORDS, cx + 15,  cy - 248, 155, 32);
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

        Player p     = game.playerManager.getPlayerLogeado();
        float panelH = 400, panelW = 350;

        // Panel izquierdo: perfil
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx - panelW - 20, cy - panelH / 2f, panelW, panelH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx - panelW - 20, cy - panelH / 2f, panelW, panelH);
        shape.end();

        // Panel derecho: ranking
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.13f, 0.13f, 0.2f, 1f);
        shape.rect(cx + 20, cy - panelH / 2f, panelW, panelH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.35f, 0.35f, 0.55f, 1f);
        shape.rect(cx + 20, cy - panelH / 2f, panelW, panelH);
        shape.end();

        btnVolver.dibujar(batch, font, shape);
        btnRecords.dibujar(batch, font, shape);

        batch.begin();

        // Titulos de paneles
        font.setColor(Color.WHITE);
        GlyphLayout tp = new GlyphLayout(font, Textos.PERFIL_TITULO);
        font.draw(batch, Textos.PERFIL_TITULO,
                cx - panelW / 2f - 20 - tp.width / 2f, cy + panelH / 2f + 22);

        GlyphLayout tr = new GlyphLayout(font, Textos.PERFIL_RANKING);
        font.draw(batch, Textos.PERFIL_RANKING,
                cx + 20 + panelW / 2f - tr.width / 2f, cy + panelH / 2f + 22);

        // ── Panel de perfil ──────────────────────────────────────────────────
        if (p != null) {
            float lx   = cx - panelW - 10;
            float ly   = cy + panelH / 2f - 18;
            float step = 26;

            font.setColor(Color.WHITE);
            font.draw(batch, p.getNombreCompleto(), lx, ly);
            font.setColor(0.5f, 0.5f, 0.75f, 1f);
            font.draw(batch, "@" + p.getUserName(), lx, ly - 16);

            ly -= step + 10;
            font.setColor(0.75f, 0.75f, 0.95f, 1f);
            font.draw(batch, Textos.PERFIL_PARTIDAS     + p.getPartidasJugadas(),                   lx, ly);
            font.draw(batch, Textos.PERFIL_NIV_COMP     + p.getNivelesCompletados(),                lx, ly -= step);
            font.draw(batch, Textos.PERFIL_MEJOR_PTS    + p.getMejorPuntaje(),                      lx, ly -= step);
            font.draw(batch, Textos.PERFIL_PTS_TOTAL    + p.getPuntajeGeneral(),                    lx, ly -= step);
            font.draw(batch, Textos.PERFIL_TIEMPO       + String.format("%.2f", p.getTiempoJugadoHoras()) + " h",   lx, ly -= step);
            font.draw(batch, Textos.PERFIL_PROMEDIO     + String.format("%.0f", p.getTiempoPromedioPorNivel()) + " s", lx, ly -= step);
            font.draw(batch, Textos.PERFIL_DESBLOQUEADOS + (p.getNivelesDesbloqueados() + 1),       lx, ly -= step);

            ArrayList<EntradaHistorial> hist = p.getHistorial();
            if (hist != null && !hist.isEmpty()) {
                ly -= 10;
                font.setColor(0.5f, 0.5f, 0.75f, 1f);
                font.draw(batch, Textos.PERFIL_ULTIMAS, lx, ly -= step * 0.7f);
                int desde = Math.max(0, hist.size() - 4);
                for (int i = desde; i < hist.size(); i++) {
                    EntradaHistorial e = hist.get(i);
                    font.setColor(0.65f, 0.65f, 0.65f, 1f);
                    String linea = Textos.PERFIL_NIV_ABREV   + (e.getNivel() + 1)
                                 + Textos.PERFIL_MOVS_ABREV  + e.getMovimientos()
                                 + Textos.PERFIL_PTS_ABREV   + e.getPuntaje();
                    font.draw(batch, linea, lx, ly -= step * 0.8f);
                }
            }
        }

        // ── Panel de ranking ─────────────────────────────────────────────────
        float rx  = cx + 30;
        float ry  = cy + panelH / 2f - 18;
        int   top = Math.min(10, ranking.size());

        if (top == 0) {
            font.setColor(0.4f, 0.4f, 0.6f, 1f);
            font.draw(batch, Textos.PERFIL_SIN_DATOS, rx, ry);
        } else {
            for (int i = 0; i < top; i++) {
                String[] entry = ranking.get(i);
                boolean  esYo  = p != null && entry[0].equals(p.getUserName());
                if      (i == 0) font.setColor(1f,    0.85f, 0.2f,  1f);
                else if (i == 1) font.setColor(0.8f,  0.8f,  0.8f,  1f);
                else if (i == 2) font.setColor(0.8f,  0.55f, 0.3f,  1f);
                else if (esYo)   font.setColor(Color.YELLOW);
                else              font.setColor(Color.LIGHT_GRAY);
                String linea = (i + 1) + ".  " + entry[0] + "  -  " + entry[1] + " pts";
                if (esYo) linea += "  <";
                font.draw(batch, linea, rx, ry - i * 30);
            }
        }
        batch.end();

        // ── Logica de clics ───────────────────────────────────────────────────
        if (btnVolver.clicado())  { game.setScreen(new MenuScreen(game));   dispose(); }
        if (btnRecords.clicado()) { game.setScreen(new RecordsScreen(game)); dispose(); }
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────
    @Override public void resize(int w, int h) {
        camera.viewportWidth = w; camera.viewportHeight = h; camera.update();
        btnVolver  = new Boton(Textos.PERFIL_BTN_VOLVER,  w / 2f - 170, h / 2f - 248, 155, 32);
        btnRecords = new Boton(Textos.PERFIL_BTN_RECORDS, w / 2f + 15,  h / 2f - 248, 155, 32);
    }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { batch.dispose(); font.dispose(); shape.dispose(); }
}
