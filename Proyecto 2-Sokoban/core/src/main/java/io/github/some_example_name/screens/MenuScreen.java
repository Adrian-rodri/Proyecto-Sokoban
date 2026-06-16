package io.github.some_example_name.screens;

import io.github.some_example_name.screens.LevelSelectScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.Main;
import java.util.ArrayList;

public class MenuScreen extends BaseScreen {
    private TextButton btnNotif;
    
    public MenuScreen(Main game) {
        super(game);
    }
    
    @Override
    protected void buildUI() {
        String nombreJugador = game.playerManager.getNombreJugador();

        Label lblTitulo = new Label("SOKOBAN", skin, "title-white");
        Label lblSaludo = new Label(traducir("Hola, ","Hello, ")+ nombreJugador, skin, "medium-white");

        TextButton btnJugar = new TextButton(traducir("Jugar","Play"), skin, "big");
        TextButton btnPerfil = new TextButton(traducir("Mi perfil", "My profile"), skin, "big");
        TextButton btnReporte = new TextButton(traducir("Reportes", "Reports"), skin, "big");
        TextButton btnSalir = new TextButton(traducir("Salir", "Exit"), skin, "default");
        TextButton btnConfig = new TextButton(traducir("Config", "Config"), skin, "small");
        TextButton btnAmigos = new TextButton(traducir("Amigos", "Friends"), skin, "small");
        TextButton btnAyuda = new TextButton("  ?  ", skin, "small");
        btnNotif = new TextButton("Notif", skin, "small");

        float botonAncho = 240, botonAlto = 42, iconAncho = 64, iconAlto = 32;

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f); 
        panel.add(lblTitulo).colspan(4).center().padBottom(4).row();
        panel.add(lblSaludo).colspan(4).center().padBottom(18).row();
        panel.add(btnJugar).colspan(4).width(botonAncho).height(botonAlto).padBottom(8).row();
        panel.add(btnPerfil).colspan(4).width(botonAncho).height(botonAlto).padBottom(8).row();
        panel.add(btnReporte).colspan(4).width(botonAncho).height(botonAlto).padBottom(8).row();
        panel.add(btnSalir).colspan(4).width(botonAncho).height(botonAlto).padBottom(14).row();
        panel.add(btnConfig).width(iconAncho).height(iconAlto).padRight(6);
        panel.add(btnAmigos).width(iconAncho).height(iconAlto).padRight(6);
        panel.add(btnNotif).width(iconAncho).height(iconAlto).padRight(6);
        panel.add(btnAyuda).width(iconAncho).height(iconAlto).row();
        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);

        btnJugar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        btnPerfil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.setScreen(new ProfileScreen(game));
                dispose();
            }
        });
        btnReporte.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("Reportes");
                game.setScreen(new ReportesScreen(game));
                dispose();
            }
        });
        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });
        
        btnConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.setScreen(new SettingScreen(game));
                dispose();
            }
        });
        
        btnAmigos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                mostrarDialogoAmigos();
            }
        });
        
        btnNotif.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                mostrarDialogoNotificaciones();
            }
        });
        btnAyuda.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("[Menu] Ayuda");
            }
        });
    }
    private void actualizarBotonNotificaciones() {
        int cant= game.playerManager.getCantSolicitudes();
        String txt= traducir("Notif", "Notif");
        if (cant>0){
            txt+= " (" + cant + ")";
        }
        btnNotif.setText(txt);
    }

    private void mostrarDialogoAmigos() {
        Dialog dialogo= new Dialog(traducir("Amigos","Friends"), skin,"tool");
        dialogo.setMovable(false);
        dialogo.setModal(true);
        dialogo.pad(20);
        
        TextField txtAmigo= new TextField("", skin);
        txtAmigo.setMessageText(traducir("Username del amigo...", "Friend's username..."));
        TextButton btnAgregar= new TextButton(traducir("Enviar solicitud", "Send request"), skin, "default");
        Label lblMsg= new Label("", skin, "small-white");
        
        Table scrollContent= new Table();
        ScrollPane scroll= new ScrollPane(scrollContent, skin);

        btnAgregar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                String nombre = txtAmigo.getText().trim();
                if (nombre.isEmpty())
                    return;
                boolean ok= game.playerManager.enviarSolicitud(nombre);
                if (ok){
                    lblMsg.setText(traducir("Solicitud enviada a ", "Request sent to ") + nombre);
                    txtAmigo.setText("");
                }else{
                    lblMsg.setText(traducir("No se pudo enviar", "Could not send"));
                }
                refrescarDialogo(scrollContent, dialogo, txtAmigo, btnAgregar, lblMsg, scroll);
            }
        });
        Table topRow= new Table();
        topRow.add(txtAmigo).width(200).padRight(6);
        topRow.add(btnAgregar).row();
        topRow.add(lblMsg).colspan(2).padTop(4);

        dialogo.getContentTable().add(topRow).padBottom(8).row();
        dialogo.getContentTable().add(scroll).width(340).height(260).padBottom(8).row();
        dialogo.button(traducir("Cerrar", "Close"), null);

        refrescarDialogo(scrollContent, dialogo, txtAmigo, btnAgregar, lblMsg, scroll);

        dialogo.pack();
        dialogo.show(stage);
    }
    
    
    private void refrescarDialogo(Table scrollContent, Window ventana,TextField txtAmigo, TextButton btnAgregar,Label lblMsg, ScrollPane scroll) {
        scrollContent.clearChildren();
        scrollContent.top().left();

        java.util.ArrayList<String> solicitudes= game.playerManager.getSolicitudes();
        if (solicitudes!=null &&!solicitudes.isEmpty()){
            Label lblSec= new Label("* " + traducir("Solicitudes pendientes", "Pending requests") + " *", skin, "small-white");
            lblSec.setColor(0.6f, 0.8f, 0.4f, 1f);
            scrollContent.add(lblSec).colspan(3).left().padBottom(6);
            scrollContent.row();

            for (String sol: solicitudes) {
                scrollContent.add(new Label(sol, skin, "small-white")).left().padRight(8).padBottom(4);

                TextButton btnAceptar= new TextButton(traducir("Aceptar", "Accept"), skin, "small");
                btnAceptar.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                        game.playerManager.aceptarSolicitud(sol);
                        actualizarBotonNotificaciones();
                        refrescarDialogo(scrollContent, ventana, txtAmigo, btnAgregar, lblMsg, scroll);
                    }
                });
                scrollContent.add(btnAceptar).width(60).height(24).padRight(4);

                TextButton btnRechazar= new TextButton(traducir("Rechazar", "Reject"), skin, "small");
                btnRechazar.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                        game.playerManager.rechazarSolicitud(sol);
                        actualizarBotonNotificaciones();
                        refrescarDialogo(scrollContent, ventana, txtAmigo, btnAgregar, lblMsg, scroll);
                    }
                });
                scrollContent.add(btnRechazar).width(70).height(24).padBottom(4);
                scrollContent.row();
            }
            Label lblSep= new Label("***", skin, "small-white");
            lblSep.setColor(0.4f, 0.4f, 0.4f, 1f);
            scrollContent.add(lblSep).colspan(3).padBottom(6);
            scrollContent.row();
        }

        java.util.ArrayList<String> amigos= game.playerManager.getPlayerLogeado().getAmigos();
        if (amigos== null|| amigos.isEmpty()) {
            scrollContent.add(new Label(traducir("(sin amigos aun)", "(no friends yet)"), skin, "small-white")).colspan(3).padBottom(6);
            scrollContent.row();
            ventana.pack();
            scroll.layout();
            return;
        }

        for (String amigo: amigos) {
            TextButton btnAmigo= new TextButton(amigo, skin, "default");
            btnAmigo.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent ev, Actor a) {
                    mostrarComparativa(amigo);
                }
            });
            scrollContent.add(btnAmigo).colspan(3).fillX().padBottom(4);
            scrollContent.row();
        }

        ventana.pack();
        scroll.layout();
    }
    
    private void mostrarComparativa(String amigo) {
        String[] statsYo = {
            String.valueOf(game.playerManager.getPlayerLogeado().getPartidasJugadas()),
            String.valueOf(game.playerManager.getPlayerLogeado().getNivelesCompletados()),
            String.valueOf(game.playerManager.getPlayerLogeado().getMejorPuntaje()),
            String.valueOf(game.playerManager.getPlayerLogeado().getPuntajeGeneral()),
            String.format("%.2f", game.playerManager.getPlayerLogeado().getTiempoJugadoHoras()),
            String.format("%.2f", game.playerManager.getPlayerLogeado().getTiempoPromedioPorNivel())
        };
        String[] statsEl = game.playerManager.obtenerStatsAmigo(amigo);
        if (statsEl == null) return;

        String[][] filas={
            {traducir("Partidas","Games"),statsYo[0],statsEl[0]},
            {traducir("Niveles Complet.","Complet. Levels"), statsYo[1], statsEl[1]},
            {traducir("Mejor Puntaje","Best Score"),statsYo[2], statsEl[2]},
            {traducir("Puntaje total","Total Score"),statsYo[3], statsEl[3]},
            {traducir("Tiempo (h)","Time (h)"),statsYo[4], statsEl[4]},
            {traducir("Tiempo prom.","Average Time"),statsYo[5], statsEl[5]}
        };

        Dialog dialogo= new Dialog(traducir("Comparar con ","Compare with ") + amigo, skin);
        dialogo.setMovable(false);
        dialogo.pad(28, 20, 16, 20);

        Table tabla= dialogo.getContentTable();
        tabla.add("").width(150).padTop(12);
        tabla.add(new Label(traducir("TU", "YOU"), skin)).width(70);
        tabla.add(amigo).width(70);
        tabla.row();

        for (String[] f: filas){
            tabla.add(lbl(f[0])).width(150);
            tabla.add(lbl(f[1])).width(70);
            tabla.add(lbl(f[2])).width(70);
            tabla.row();
        }

        dialogo.button("Cerrar");
        dialogo.show(stage);
    }
    private Label lbl(String texto){
        return new Label(texto, skin, "small-white");
    }
    private void mostrarDialogoNotificaciones() {
        ArrayList<String> solicitudes = game.playerManager.getSolicitudes();

        Dialog dialogo= new Dialog(traducir("Notificaciones", "Notifications"), skin, "default");
        dialogo.setModal(true);
        dialogo.setMovable(false);
        dialogo.pad(20);

        Table content= dialogo.getContentTable();
        content.pad(10, 16, 10, 16);

        if(solicitudes.isEmpty()){
            content.add(new Label(traducir("No tienes solicitudes", "No pending requests"),skin, "small-white")).left();
        }else{
            content.add(new Label(traducir("Solicitudes de amistad:", "Friend requests:"),skin, "small-white")).left();
            for(String from :solicitudes){
                Table fila= new Table();
                fila.add(new Label(from, skin, "small-white")).left().expandX();

                TextButton btnAceptar= new TextButton(traducir("Aceptar", "Accept"), skin, "small");
                TextButton btnRechazar= new TextButton(traducir("Rechazar", "Decline"), skin, "small");

                btnAceptar.addListener(new ChangeListener() {
                    public void changed(ChangeEvent e, Actor a) {
                        game.playerManager.aceptarSolicitud(from);
                        dialogo.hide();
                        mostrarDialogoNotificaciones();
                        actualizarBotonNotificaciones();
                    }
                });
                btnRechazar.addListener(new ChangeListener() {
                    public void changed(ChangeEvent e, Actor a) {
                        game.playerManager.rechazarSolicitud(from);
                        dialogo.hide();
                        mostrarDialogoNotificaciones();
                        actualizarBotonNotificaciones();
                    }
                });

                fila.add(btnAceptar).width(70).padRight(4);
                fila.add(btnRechazar).width(70);
                content.add(fila).fillX().padBottom(3).row();
            }
        }

        dialogo.button(traducir("Cerrar", "Close"), null);
        dialogo.show(stage);
    }
    
    

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

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
