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
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private int opcionSeleccionada = 0;
    private final String[] opciones = {"Jugar", "Salir"};

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Se llama automaticamente al hacer setScreen() — inicializa todo aqui
        batch  = new SpriteBatch();
        font   = new BitmapFont();
        shape  = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        // Se ejecuta cada frame — logica + dibujo van aqui
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.15f, 0.22f, 1f);
        shape.rect(w / 2f - 150, h / 2f - 80, 300, 160);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.4f, 0.4f, 0.6f, 1f);
        shape.rect(w / 2f - 150, h / 2f - 80, 300, 160);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.25f, 0.25f, 0.45f, 1f);
        shape.rect(w / 2f - 140, h / 2f - 60 + (1 - opcionSeleccionada) * 60, 280, 40);
        shape.end();

        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", w / 2f - titulo.width / 2f, h / 2f + 120);

        for (int i = 0; i < opciones.length; i++) {
            font.setColor(i == opcionSeleccionada ? Color.YELLOW : Color.LIGHT_GRAY);
            GlyphLayout gl = new GlyphLayout(font, opciones[i]);
            font.draw(batch, opciones[i], w / 2f - gl.width / 2f, h / 2f - 35 + (1 - i) * 60);
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP))
            opcionSeleccionada = (opcionSeleccionada - 1 + opciones.length) % opciones.length;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            opcionSeleccionada = (opcionSeleccionada + 1) % opciones.length;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcionSeleccionada == 0) { 
                game.setScreen(new GameScreen(game,2));
                dispose(); 
            }
            else Gdx.app.exit();
        }
    }

    @Override
    public void resize(int w, int h) {
        // LibGDX lo llama si el usuario redimensiona la ventana
        camera.viewportWidth  = w;
        camera.viewportHeight = h;
        camera.update();
    }

    @Override public void pause()  {}  // ventana minimizada / perdida de foco
    @Override public void resume() {}  // vuelve al foco
    @Override public void hide()   {}  // se llama justo antes de setScreen() al salir de esta pantalla

    @Override
    public void dispose() {
        // Libera memoria — se llama al cerrar o al cambiar de pantalla manualmente
        batch.dispose();
        font.dispose();
        shape.dispose();
    }
}