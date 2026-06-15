package io.github.some_example_name.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.model.Player;

public class LevelSelectScreen extends BaseScreen {

    private Label lblInfo;
    private int totalNiveles;
    private int nivelesDisponibles;
    private int nivelesDesbloqueados;

    private static final int COLS = 5;

    public LevelSelectScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        totalNiveles = game.nivelManager.getCantidad();
        Player p = game.playerManager.getPlayerLogeado();
        nivelesDesbloqueados = (p != null) ? p.getNivelesDesbloqueados() : 0;
        nivelesDisponibles = Math.min(nivelesDesbloqueados + 1, totalNiveles);

        
        TextButton.TextButtonStyle estiloBase = skin.get("small", TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle estiloCompletado = new TextButton.TextButtonStyle(estiloBase);
        estiloCompletado.fontColor = new Color(0.95f, 0.85f, 0.40f, 1f);
        estiloCompletado.overFontColor = new Color(1f, 0.95f, 0.65f, 1f);
        skin.add("nivel-completado", estiloCompletado);

        TextButton.TextButtonStyle estiloDisponible = new TextButton.TextButtonStyle(estiloBase);
        estiloDisponible.fontColor = Color.WHITE;
        estiloDisponible.overFontColor = new Color(0.80f, 0.85f, 1f, 1f);
        skin.add("nivel-disponible", estiloDisponible);
        
        Table grid = new Table();
        int cols = Math.min(totalNiveles, COLS);
        float tileAncho = 96f, tileAlto = 64f;

        for (int i = 0; i < totalNiveles; i++) {
            boolean bloqueado = i >= nivelesDisponibles;
            boolean completado = !bloqueado && i < nivelesDesbloqueados;
            boolean siguiente = !bloqueado && !completado;

            String texto = (i == 0 ? "Tutorial" : "Nivel " + i);
            String estilo;
            if (completado) {
                estilo = "nivel-completado";
            } else if (siguiente) {
                estilo = "nivel-disponible";
            } else {
                estilo = "small";
            }

            TextButton btn = new TextButton(texto, skin, estilo);
            final int idx = i;

            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (idx < nivelesDisponibles) {
                        GameScreen.initPlayer = false;
                        game.setScreen(new GameScreen(game, idx));
                        dispose();
                    }
                }
            });

            btn.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (idx < nivelesDisponibles) {
                        Nivel nv = game.nivelManager.getNivel(idx);
                        String nombre = (idx == 0) ? "Tutorial" : "Nivel " + idx;
                        lblInfo.setText(nombre + "   -   Cajas: " + nv.getCantidadCajas());
                        lblInfo.setColor(0.65f, 0.65f, 0.9f, 1f);
                    } else {
                        lblInfo.setText("Nivel bloqueado");
                        lblInfo.setColor(0.5f, 0.5f, 0.5f, 1f);
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    lblInfo.setText("");
                }
            });

            grid.add(btn).width(tileAncho).height(tileAlto).pad(5);
            if ((i + 1) % cols == 0) {
                grid.row();
            }
        }

        lblInfo = new Label("", skin, "small-white");

        Table leyenda = new Table();
        Label lblCompletado = new Label("  Completado", skin, "small-white");
        lblCompletado.setColor(0.95f, 0.85f, 0.40f, 1f);
        Label lblDisponible = new Label("  Disponible", skin, "small-white");
        lblDisponible.setColor(Color.WHITE);
        Label lblBloqueado = new Label("  Bloqueado", skin, "small-white");
        lblBloqueado.setColor(0.4f, 0.4f, 0.4f, 1f);
        leyenda.add(lblCompletado).padRight(16);
        leyenda.add(lblDisponible).padRight(16);
        leyenda.add(lblBloqueado);

        TextButton btnVolver = new TextButton("Volver", skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Window panel = createWindow();
        panel.add(new Label("Seleccionar nivel", skin, "title-white")).colspan(cols + 1).center().padBottom(12).row();
        panel.add(grid).colspan(cols + 1).center().row();
        panel.add(lblInfo).colspan(cols + 1).center().padTop(8).padBottom(6).row();
        panel.add(leyenda).colspan(cols + 1).center().padBottom(14).row();
        panel.add(btnVolver).colspan(cols + 1).center().width(140).height(32);
        panel.pack();
        setRoot(panel);
    }
}
