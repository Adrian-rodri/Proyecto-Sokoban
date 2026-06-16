package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.model.Player;
import java.util.ArrayList;

public class ProfileScreen extends BaseScreen {


    private TextButton btnVolver;

    public ProfileScreen(Main game) {
        super(game);
    }
    @Override
    protected void buildUI() {
        Player p = game.playerManager.getPlayerLogeado();

        Table perfilTable = new Table();
        perfilTable.top().left();

        if (p != null) {
            Label lblNombre = new Label(p.getNombreCompleto(), skin, "medium-white");
            perfilTable.add(lblNombre).left().padBottom(2).row();

            Label lblUser = new Label("@" + p.getUserName(), skin, "small-white");
            lblUser.setColor(0.5f, 0.5f, 0.75f, 1f);
            perfilTable.add(lblUser).left().padBottom(12).row();

            Label.LabelStyle estiloDato = new Label.LabelStyle(skin.getFont("small"),
                    new Color(0.75f, 0.75f, 0.95f, 1f));
            skin.add("dato-perfil", estiloDato, Label.LabelStyle.class);

            perfilTable.add(new Label(traducir("Partidas:", "Games:  ")+ p.getPartidasJugadas(), skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Niveles completados: ","Completed Levels: ") + p.getNivelesCompletados(), skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Mejor puntaje: ","Best puntutation: ") + p.getMejorPuntaje(), skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Puntaje total: ","Total Points: ") + p.getPuntajeGeneral(), skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Tiempo jugado: ","Played Time:  ") + String.format("%.2f", p.getTiempoJugadoHoras()) + " h", skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Promedio/nivel: ","Average/level") + String.format("%.0f", p.getTiempoPromedioPorNivel()) + " s", skin, "dato-perfil")).left().padBottom(3).row();
            perfilTable.add(new Label(traducir("Desbloqueados: ","Unlocked: ") + (p.getNivelesDesbloqueados() + 1), skin, "dato-perfil")).left().padBottom(10).row();

            ArrayList<EntradaHistorial> hist = p.getHistorial();
            if (hist != null && !hist.isEmpty()) {
                Label lblUltimas = new Label(traducir("Ultimas partidas:","Last games:"), skin, "small-white");
                lblUltimas.setColor(0.5f, 0.5f, 0.75f, 1f);
                perfilTable.add(lblUltimas).left().padBottom(4).row();

                int desde = Math.max(0, hist.size() - 4);
                for (int i = desde; i < hist.size(); i++) {
                    EntradaHistorial e = hist.get(i);
                    String linea = traducir("Nv.","Lvl.") + (e.getNivel() + 1)
                            + traducir("  Movs:" ,"  Steps")+ e.getMovimientos()
                            + "  Pts:" + e.getPuntaje();
                    Label lblLinea = new Label(linea, skin, "small-white");
                    lblLinea.setColor(0.65f, 0.65f, 0.65f, 1f);
                    perfilTable.add(lblLinea).left().padBottom(2).row();
                }
            }
        }

        Table avatarTable = new Table();
        avatarTable.top().center();

        Label lblAvatarTitle = new Label(traducir("Personalizar avatar","Change Avatar"), skin, "medium-white");
        avatarTable.add(lblAvatarTitle).center().padBottom(16).row();

        Label lblPlaceholder = new Label("[ proximamente ]", skin, "small-white");
        lblPlaceholder.setColor(0.5f, 0.5f, 0.6f, 1f);
        avatarTable.add(lblPlaceholder).center().row();
        
        btnVolver = new TextButton(traducir("Volver","Back"), skin, "default");

        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Table btnRow = new Table();
        btnRow.add(btnVolver).width(155).height(32);

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        panel.add(new Label(traducir("Mi Perfil","My Profile"), skin, "title-white")).colspan(2).center().padBottom(16).row();
        panel.add(perfilTable).left().width(250).padRight(20).expandY();
        panel.add(avatarTable).left().width(250).expandY().row();
        panel.add(btnRow).colspan(2).center().padTop(10);
        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }
    


    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    
}
