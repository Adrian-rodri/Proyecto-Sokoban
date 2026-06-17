package io.github.some_example_name.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.model.Player;
import java.util.ArrayList;

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

            String texto = (i == 0 ? "Tutorial" : traducir("Nivel ","Level ") + i);
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
                      mostrarJugarDialogo(idx);
                        //GameScreen.initPlayer = false;
                        //game.setScreen(new GameScreen(game, idx));
   
                    }
                }
            });

            btn.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (idx < nivelesDisponibles) {
                        Nivel nv = game.nivelManager.getNivel(idx);
                        String nombre = (idx == 0) ? "Tutorial" : traducir("Nivel ","Level ") + idx;
                        lblInfo.setText(nombre + "   -   "+traducir("Cajas: ","Boxes: ") + nv.getCantidadCajas());
                        lblInfo.setColor(0.65f, 0.65f, 0.9f, 1f);
                    } else {
                        lblInfo.setText(traducir("Nivel bloqueado","Locked Level"));
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
        Label lblCompletado = new Label(traducir("  Completado"," Completed"), skin, "small-white");
        lblCompletado.setColor(0.95f, 0.85f, 0.40f, 1f);
        Label lblDisponible = new Label(traducir("  Disponible","  Unlocked"), skin, "small-white");
        lblDisponible.setColor(Color.WHITE);
        Label lblBloqueado = new Label(traducir("  Bloqueado","  Blocked"), skin, "small-white");
        lblBloqueado.setColor(0.4f, 0.4f, 0.4f, 1f);
        leyenda.add(lblCompletado).padRight(16);
        leyenda.add(lblDisponible).padRight(16);
        leyenda.add(lblBloqueado);

        TextButton btnVolver = new TextButton(traducir("Volver","Back"), skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Window panel = createWindow();
        panel.add(new Label(traducir("Seleccionar nivel","Select Level"), skin, "title-white")).colspan(cols + 1).center().padBottom(12).row();
        panel.add(grid).colspan(cols + 1).center().row();
        panel.add(lblInfo).colspan(cols + 1).center().padTop(8).padBottom(6).row();
        panel.add(leyenda).colspan(cols + 1).center().padBottom(14).row();
        panel.add(btnVolver).colspan(cols + 1).center().width(140).height(32);
        panel.pack();
        setRoot(panel);
    }
    private void mostrarJugarDialogo(int idx) {
        Dialog dialogo= new Dialog("", skin, "tool");
        dialogo.setMovable(false);
        dialogo.setModal(true);
        dialogo.pad(24);

        String nombreNivel= (idx==0)?"Tutorial":traducir("Nivel #", "Level #")+idx;

        Label lblTitulo= new Label(nombreNivel, skin, "title-white");
        dialogo.getContentTable().add(lblTitulo).colspan(2).center().padBottom(16).row();

        Table colBotones= new Table();

        TextButton btnRanked= new TextButton(traducir("Jugar Ranked", "Play Ranked"), skin, "default");
        TextButton btnAmigo= new TextButton(traducir("Retar", "Challenge"), skin, "default");
        TextButton btnVolver= new TextButton(traducir("Volver", "Back"), skin, "default");

        btnRanked.addListener(new ChangeListener(){
            @Override public void changed(ChangeEvent e, Actor a) {
                dialogo.hide();
                GameScreen.initPlayer= false;
                game.setScreen(new GameScreen(game, idx));
            }
        });

        btnAmigo.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                dialogo.hide();
                mostrarDialogoRetar(idx);
            }
        });

        btnVolver.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                dialogo.hide();
            }
        });

        float btnAncho = 160f, btnAlto = 36f;
        colBotones.add(btnRanked).width(btnAncho).height(btnAlto).padBottom(10).row();
        colBotones.add(btnAmigo ).width(btnAncho).height(btnAlto).padBottom(10).row();
        colBotones.add(btnVolver).width(btnAncho).height(btnAlto);
        
        //datos de la mejor partida
        Player player= game.playerManager.getActual();
        EntradaHistorial mejorPartida= null;
        if(player!=null && player.getHistorial()!=null){
            for(EntradaHistorial game:player.getHistorial()){
                if(game.getNivel()==idx){
                    if(mejorPartida==null || mejorPartida.getPuntaje()<game.getPuntaje())
                        mejorPartida=game;
                }
            }
        }
        boolean completado= mejorPartida != null;
        String pasos= completado?mejorPartida.getMovimientos() + traducir(" pasos", " steps") : "";
        String secs= completado? String.format("%.1f secs", mejorPartida.getTiempo()) : "";
        
        Table colStats = new Table();
        String txtBest= completado?traducir("Mejor Juego", "Best Game"):traducir("Aun sin completar","Not completed yet");
        Label lblMejorTitulo= new Label(txtBest, skin, "default");
        
        Label lblPasos= new Label(pasos, skin, "small-white");
        Label lblSecs= new Label(secs,skin, "small-white");

        Label lblDifTitulo= new Label(traducir("Dificultad: ", "Difficulty: "), skin, "default");
        
        Label lblDifValor= new Label(getDificultad(idx), skin, "small-white");

        Table filaDif= new Table();
        filaDif.add(lblDifTitulo);
        filaDif.add(lblDifValor).padLeft(4);

        colStats.left();
        colStats.add(lblMejorTitulo).left().row();
        colStats.add(lblPasos).left().padTop(4).row();
        colStats.add(lblSecs).left().padTop(2).row();
        colStats.add(filaDif).left().padTop(8);

        dialogo.getContentTable().add(colBotones).padRight(32).top();
        dialogo.getContentTable().add(colStats).top();

        dialogo.pack();
        dialogo.show(stage);
    }
    private String getDificultad(int idx){
        if(idx <=2) 
            return traducir("Facil", "Easy");
        if(idx<=5) 
            return traducir("Medio", "Medium");
        return traducir("Dificil", "Hard");
    }
    private void mostrarDialogoRetar(int idx) {
        String nombreNivel = (idx == 0) ? "Tutorial" : traducir("Nivel ", "Level ") + idx;

        Dialog dialogoRetar = new Dialog(traducir("Retar en ", "Challenge at ") + nombreNivel, skin, "tool");
        dialogoRetar.setMovable(false);
        dialogoRetar.setModal(true);
        dialogoRetar.pad(20);

        Table contenido = new Table();
        contenido.top().left();
        ScrollPane scroll = new ScrollPane(contenido, skin);

        Label lblMsg = new Label("", skin, "small-white");

        ArrayList<String> amigos = game.playerManager.getPlayerLogeado().getAmigos();
        if (amigos == null || amigos.isEmpty()) {
            contenido.add(new Label(traducir("No tienes amigos aun.", "No friends yet."), skin, "small-white"))
                     .left().row();
        } else {
            for (String amigo : amigos) {
                Table fila = new Table();
                fila.add(new Label(amigo, skin, "small-white")).left().expandX();
                TextButton btnRetar = new TextButton(traducir("Retar", "Challenge"), skin, "small");
                btnRetar.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        boolean ok = game.playerManager.enviarReto(amigo, idx);
                        if (ok) {
                            lblMsg.setText(traducir("Reto enviado a ", "Challenge sent to ") + amigo + "!");
                            lblMsg.setColor(0.4f, 1f, 0.5f, 1f);
                        } else {
                            lblMsg.setText(traducir("Ya retaste a ", "Already challenged ") + amigo);
                            lblMsg.setColor(1f, 0.5f, 0.5f, 1f);
                        }
                    }
                });
                fila.add(btnRetar).width(80).height(26);
                contenido.add(fila).expandX().fillX().padBottom(4).row();
            }
        }

        dialogoRetar.getContentTable().add(scroll).width(280).height(180).padBottom(6).row();
        dialogoRetar.getContentTable().add(lblMsg).center().padBottom(4).row();
        dialogoRetar.button(traducir("Cerrar", "Close"), null);
        dialogoRetar.show(stage);
    }
}
