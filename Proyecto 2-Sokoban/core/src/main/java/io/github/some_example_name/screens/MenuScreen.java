package io.github.some_example_name.screens;

import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.screens.LevelSelectScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class MenuScreen extends BaseScreen {
    private TextButton btnNotif;
    
    public MenuScreen(Main game) {
        super(game);
    }
    
    @Override
    protected void buildUI() {
        String nombreJugador = game.playerManager.getNombreJugador();

        Label lblTitulo = new Label("SOKOBAN", skin, "title-white");
        Label lblSaludo = new Label("Hola, " + nombreJugador, skin, "medium-white");

        TextButton btnJugar = new TextButton(traducir("Jugar","Play"), skin, "big");
        TextButton btnPerfil = new TextButton(traducir("Mi perfil", "My profile"), skin, "big");
        TextButton btnReporte = new TextButton(traducir("Reportes", "Reports"), skin, "big");
        TextButton btnSalir = new TextButton(traducir("Salir", "Exit"), skin, "default");
        TextButton btnConfig = new TextButton(traducir("Config", "Config"), skin, "small");
        TextButton btnAmigos = new TextButton(traducir("Amigos", "Friends"), skin, "small");
        TextButton btnAyuda = new TextButton("  ?  ", skin, "small");
        btnNotif = new TextButton("Notif", skin, "small");

        float bw = 240, bh = 42, iconW = 64, iconH = 32;

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f); 
        panel.add(lblTitulo).colspan(4).center().padBottom(4).row();
        panel.add(lblSaludo).colspan(4).center().padBottom(18).row();
        panel.add(btnJugar).colspan(4).width(bw).height(bh).padBottom(8).row();
        panel.add(btnPerfil).colspan(4).width(bw).height(bh).padBottom(8).row();
        panel.add(btnReporte).colspan(4).width(bw).height(bh).padBottom(8).row();
        panel.add(btnSalir).colspan(4).width(bw).height(bh).padBottom(14).row();
        panel.add(btnConfig).width(iconW).height(iconH).padRight(6);
        panel.add(btnAmigos).width(iconW).height(iconH).padRight(6);
        panel.add(btnAyuda).width(iconW).height(iconH).row();
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
                game.playerManager.cerrarSesion();
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
        
        /*
        btnAmigos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                game.playerManager.cerrarSesion();
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });*/
        
        btnAyuda.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent e, Actor a) {
                System.out.println("[Menu] Ayuda");
            }
        });
    }

    private void mostrarDialogoAmigos() {
        final Window ventana = new Window("Amigos", skin);
        ventana.setMovable(false);
        ventana.setModal(true);
        ventana.pad(20);

        final Table contenido = new Table();

        final TextField txtAmigo = new TextField("", skin);
        txtAmigo.setMessageText("Username del amigo...");
        final TextButton btnAgregar = new TextButton("Agregar", skin, "default");
        final Label lblMsg = new Label("", skin, "small-white");
        btnAgregar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                String nombre = txtAmigo.getText().trim();
                if (nombre.isEmpty()) return;
                if (game.playerManager.agregarAmigo(nombre)) {
                    lblMsg.setText( nombre + " agregado");
                    txtAmigo.setText("");
                } else {
                    lblMsg.setText(" No se pudo agregar (no existe / ya es amigo)");
                }
                refrescarListaAmigos(contenido, ventana, txtAmigo, btnAgregar, lblMsg);
            }
        });

        TextButton btnCerrar = new TextButton("Cerrar", skin, "default");
        btnCerrar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                ventana.remove();
            }
        });

        contenido.add(txtAmigo).width(180).padRight(6);
        contenido.add(btnAgregar).padRight(6);
        contenido.row();
        contenido.add(lblMsg).colspan(2).padBottom(10);
        contenido.row();

        refrescarListaAmigos(contenido, ventana, txtAmigo, btnAgregar, lblMsg);

        contenido.add(btnCerrar).colspan(2).padTop(10);

        ventana.add(contenido);
        ventana.pack();

        Table root = (Table) stage.getActors().first();
        root.addActor(ventana);
        ventana.setPosition(
            (stage.getWidth() - ventana.getWidth()) / 2f,
            (stage.getHeight() - ventana.getHeight()) / 2f
        );
    }
    
    
     private void refrescarListaAmigos(Table contenedor, Window ventana,
                                       TextField txtAmigo, TextButton btnAgregar, Label lblMsg) {
        // Limpiar todo y reconstruir desde cero
        contenedor.clearChildren();

        contenedor.add(txtAmigo).width(180).padRight(6);
        contenedor.add(btnAgregar).padRight(6);
        contenedor.row();
        contenedor.add(lblMsg).colspan(2).padBottom(10);
        contenedor.row();

        java.util.ArrayList<String> amigos = game.playerManager.getPlayerLogeado().getAmigos();
        if (amigos == null || amigos.isEmpty()) {
            contenedor.add(new Label("(sin amigos aún)", skin, "small-white"))
                     .colspan(2).padBottom(6);
            contenedor.row();
            ventana.pack();
            return;
        }

        for (final String amigo : amigos) {
            TextButton btnAmigo = new TextButton(amigo, skin, "default");
            btnAmigo.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                    mostrarComparativa(amigo);
                }
            });
            contenedor.add(btnAmigo).colspan(2).fillX().padBottom(4);
            contenedor.row();
        }
        ventana.pack();
    }
    
    private void mostrarComparativa(String amigo) {
        String[] statsYo = new String[]{
            game.playerManager.getPlayerLogeado().getUserName(),
            String.valueOf(game.playerManager.getPlayerLogeado().getPartidasJugadas()),
            String.valueOf(game.playerManager.getPlayerLogeado().getNivelesCompletados()),
            String.valueOf(game.playerManager.getPlayerLogeado().getMejorPuntaje()),
            String.valueOf(game.playerManager.getPlayerLogeado().getPuntajeGeneral()),
            String.format("%.2f", game.playerManager.getPlayerLogeado().getTiempoJugadoHoras()),
            String.format("%.2f", game.playerManager.getPlayerLogeado().getTiempoPromedioPorNivel())
        };
        String[] statsEl = game.playerManager.obtenerStatsAmigo(amigo);
        if (statsEl == null) return;

        final Window w = new Window("Comparativa: " + amigo, skin);
        w.setModal(true);
        w.pad(16);

        Table t = new Table();
        t.add("",          "bold").width(100);
        t.add("Tú",         "bold").width(100);
        t.add(amigo,        "bold").width(100);
        t.row();

        String[][] filas = {
            {"Partidas",      statsYo[1], statsEl[1]},
            {"Niveles Complet.", statsYo[2], statsEl[2]},
            {"Mejor Puntaje", statsYo[3], statsEl[3]},
            {"Puntaje Gral.",  statsYo[4], statsEl[4]},
            {"Tiempo (h)",    statsYo[5], statsEl[5]},
            {"Tiempo prom.",   statsYo[6], statsEl[6]}
        };
        for (String[] f : filas) {
            t.add(f[0]).width(100);
            t.add(f[1]).width(100);
            t.add(f[2]).width(100);
            t.row();
        }

        TextButton btnOk = new TextButton("Cerrar", skin, "default");
        btnOk.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ev, Actor a) {
                w.remove();
            }
        });
        t.add(btnOk).colspan(3).padTop(12);

        w.add(t);
        w.pack();
        Table root = (Table) stage.getActors().first();
        root.addActor(w);
        w.setPosition(
            (stage.getWidth() - w.getWidth()) / 2f,
            (stage.getHeight() - w.getHeight()) / 2f
        );
    }
    
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        stage.act(delta);
        stage.draw();
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
