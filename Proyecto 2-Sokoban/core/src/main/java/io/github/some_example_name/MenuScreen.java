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
    //COMENTADO NADA MAS
    //private int opcionSeleccionada = 0;
   // private final String[] opciones = {"Jugar", "Salir"};
    
    
    //añadido
    private static final int IDX_JUGAR  = 0;
    private static final int IDX_PERFIL = 1;
    private static final int IDX_IDIOMA = 2;
    private static final int IDX_SESION = 3;
    private static final int IDX_SALIR  = 4;
    
    private Boton[] botones;
    //end
    
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
        
        crearBotones();//añadido
    }

    @Override
    public void render(float delta) {
        // Se ejecuta cada frame — logica + dibujo van aqui
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        float w = Gdx.graphics.getWidth()  / 2f;
        float h = Gdx.graphics.getHeight() / 2f;
        
       // int w = Gdx.graphics.getWidth();//ORIGINALES
        //int h = Gdx.graphics.getHeight();//ORIGINALES
        
        //añadido
        float bw = 260, bh = 38, gap = 8;
        int   nb = botones.length;
        float totalH = nb * bh + (nb - 1) * gap;
        float panelY = h - totalH / 2f - 14;
        float panelH = totalH + 28;
        float panelW = bw + 20;
        //END
        
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.15f, 0.22f, 1f);
        shape.rect(w - panelW / 2f, panelY, panelW, panelH);
        //shape.rect(w / 2f - 150, h / 2f - 80, 300, 160);//ORIGINAL
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.4f, 0.4f, 0.6f, 1f);
        shape.rect(w - panelW / 2f, panelY, panelW, panelH);
        //shape.rect(w / 2f - 150, h / 2f - 80, 300, 160);//ORIGINAL
        shape.end();
        
        for (Boton b : botones) b.dibujar(batch, font, shape); //Añadido
        /*
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.25f, 0.25f, 0.45f, 1f);
        shape.rect(w / 2f - 140, h / 2f - 60 + (1 - opcionSeleccionada) * 60, 280, 40);
        shape.end();*/ //SOLO LO COMENTE

        batch.begin();
        font.setColor(Color.WHITE);
        GlyphLayout titulo = new GlyphLayout(font, "SOKOBAN");
        font.draw(batch, "SOKOBAN", w - titulo.width / 2f, panelY + panelH + 48);
        //font.draw(batch, "SOKOBAN", w / 2f - titulo.width / 2f, h / 2f + 120); //ORIGINAL

        
         font.setColor(0.6f, 0.6f, 0.85f, 1f);
        String bienvenida = Textos.MENU_SALUDO + game.playerManager.getNombreJugador();
        GlyphLayout bl = new GlyphLayout(font, bienvenida);
        font.draw(batch, bienvenida, w - bl.width / 2f, panelY + panelH + 26);
        batch.end();
        /*
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
                game.setScreen(new GameScreen(game,1));
                dispose(); 
            }
            else Gdx.app.exit();
        }*/
        
        //añadido
        if (botones[IDX_JUGAR].clicado())  irANiveles();
        if (botones[IDX_PERFIL].clicado()) irAPerfil();
        if (botones[IDX_IDIOMA].clicado()) cambiarIdioma();
        if (botones[IDX_SESION].clicado()) cerrarSesion();
        if (botones[IDX_SALIR].clicado())  Gdx.app.exit();
        //End
    }

    @Override
    public void resize(int w, int h) {
        // LibGDX lo llama si el usuario redimensiona la ventana
        camera.viewportWidth  = w;
        camera.viewportHeight = h;
        camera.update();
        
        crearBotones(); //añadido // recalcula posiciones si cambia el tamano
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
    
    //-----------------------JJ
    
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
    
}

