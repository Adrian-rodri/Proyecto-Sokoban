package io.github.some_example_name.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.Main;

public class VictoryScreen extends BaseScreen {

    private final int numLevel, movimientos, puntaje;
    private final double tiempoSegundos;
    private final boolean hayNivelSiguiente;

    public VictoryScreen(Main game, int numLevel, int movimientos, double tiempoSegundos, int puntaje) {
        super(game);
        this.numLevel = numLevel;
        this.movimientos = movimientos;
        this.tiempoSegundos = tiempoSegundos;
        this.puntaje = puntaje;
        this.hayNivelSiguiente = numLevel + 1 < game.nivelManager.getCantidad();
    }

    @Override
    protected void buildUI() {
        skin.add("titulo", new Label.LabelStyle(skin.getFont("title"),
                new Color(0.4f, 1f, 0.5f, 1f)), Label.LabelStyle.class);
        skin.add("dato", new Label.LabelStyle(skin.getFont("font"),
                new Color(0.85f, 0.95f, 0.88f, 1f)), Label.LabelStyle.class);
        skin.add("dato-valor", new Label.LabelStyle(skin.getFont("font"),
                Color.WHITE), Label.LabelStyle.class);
        skin.add("mensaje-final", new Label.LabelStyle(skin.getFont("font"),
                new Color(1f, 0.85f, 0.25f, 1f)), Label.LabelStyle.class);

        Window panel = createWindow();
        
        Label lblTitulo = new Label(traducir("NIVEL COMPLETADO!","LEVEL COMPLETED!"), skin, "titulo");
        panel.add(lblTitulo).colspan(2).center().padBottom(4).row();
        String strNivel;
        if(numLevel==0)
            strNivel= "Tutorial";
        else
            strNivel= "Nivel " + (numLevel);
        Label lblNivel = new Label(strNivel, skin, "dato");
        panel.add(lblNivel).colspan(2).center().padBottom(18).row();

        int mins = (int) (tiempoSegundos / 60);
        int segs = (int) (tiempoSegundos % 60);
        String strTiempo = String.format("%02d:%02d", mins, segs);

        Label lblMovLabel = new Label(traducir("Movimientos:","Steps:"), skin, "dato");
        Label lblMovValor = new Label(String.valueOf(movimientos), skin, "dato-valor");
        panel.add(lblMovLabel).left().padRight(20).padBottom(6);
        panel.add(lblMovValor).right().padBottom(6).row();

        Label lblTiempoLabel = new Label(traducir("Tiempo:","Time:"), skin, "dato");
        Label lblTiempoValor = new Label(strTiempo, skin, "dato-valor");
        panel.add(lblTiempoLabel).left().padRight(20).padBottom(6);
        panel.add(lblTiempoValor).right().padBottom(6).row();

        Label lblPuntajeLabel = new Label(traducir("Puntaje: ","Score: "), skin, "dato");
        Label lblPuntajeValor = new Label(String.valueOf(puntaje) + " pts", skin, "dato-valor");
        panel.add(lblPuntajeLabel).left().padRight(20).padBottom(14);
        panel.add(lblPuntajeValor).right().padBottom(14).row();

        if (!hayNivelSiguiente) {
            Label lblFinal = new Label(traducir("Completaste todos los niveles!","You finish all the levels!"), skin, "mensaje-final");
            panel.add(lblFinal).colspan(2).center().padBottom(14).row();
        }

        Table btnRow = new Table();
        if (hayNivelSiguiente) {
            TextButton btnSiguiente = new TextButton(traducir("Siguiente","Next"), skin, "big");
            btnSiguiente.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    GameScreen.initPlayer = false;
                    game.setScreen(new GameScreen(game, numLevel + 1));
                    dispose();
                }
            });
            btnRow.add(btnSiguiente).width(130).height(38).padRight(10);
        }

        TextButton btnNiveles = new TextButton(traducir("Niveles","Levels"), skin, "big");
        btnNiveles.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        btnRow.add(btnNiveles).width(130).height(38).padRight(10);

        TextButton btnMenu = new TextButton("Menu", skin, "default");
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        btnRow.add(btnMenu).width(130).height(38);

        panel.add(btnRow).colspan(2).center().padTop(4);
        panel.pack();
        setRoot(panel);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.06f, 0.10f, 0.07f, 1f);
        stage.act(delta);
        stage.draw();
    }
}
