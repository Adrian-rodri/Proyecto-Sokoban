package io.github.some_example_name.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;

public class SettingScreen extends BaseScreen {

    private Label lblVolumenValor, lblIdiomaValor;
    private TextButton btnWasd, btnFlechas;
    private Slider sliderVolumen;

    public SettingScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        double vol = game.playerManager.getPlayerLogeado()!=null?game.playerManager.getPlayerLogeado().getVolumen() : 0;
        String idioma = game.playerManager.getPlayerLogeado()!= null? game.playerManager.getPlayerLogeado().getIdioma() : "espanol";
        
        Label lblVolumenTitulo= new Label(traducir("Volumen","Volume"), skin, "medium-white");
        lblVolumenValor= new Label((int)(vol * 100) + "%", skin, "small-white");
        lblVolumenValor.setColor(0.6f, 0.8f, 1f, 1f);

        sliderVolumen= new Slider(0f, 100f, 1f, false, skin);
        sliderVolumen.setValue((float)(vol * 100));
        sliderVolumen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int valor= (int) sliderVolumen.getValue();
                lblVolumenValor.setText(valor + "%");
                game.playerManager.cambiarVolumen(valor);
                
                if (MenuScreen.menuMusic != null) {
                    MenuScreen.menuMusic.setVolume(valor / 100f);
                }
            }
        });

        Table volumenRow= new Table();
        volumenRow.add(lblVolumenTitulo).left().padRight(20);
        volumenRow.add(sliderVolumen).width(180).padRight(10);
        volumenRow.add(lblVolumenValor).width(36).left();
        
        Label lblIdiomaTitulo= new Label(traducir("Idioma","Language"), skin, "medium-white");
        lblIdiomaValor= new Label(formatearIdioma(idioma), skin, "small-white");

        TextButton btnEspanol= new TextButton("Espanol", skin, "small");
        TextButton btnEnglish= new TextButton("English", skin, "small");
        actualizarBotonIdioma(btnEspanol, btnEnglish, idioma);

        btnEspanol.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerManager.cambiarIdioma("espanol");
                lblIdiomaValor.setText("Espanol");
                actualizarBotonIdioma(btnEspanol, btnEnglish, "espanol");
                game.setScreen(new SettingScreen(game));
                dispose();
            }
        });
        btnEnglish.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerManager.cambiarIdioma("english");
                lblIdiomaValor.setText("English");
                actualizarBotonIdioma(btnEspanol, btnEnglish, "english");
                game.setScreen(new SettingScreen(game));
                dispose();
            }
        });

        Table idiomaRow= new Table();
        idiomaRow.add(lblIdiomaTitulo).left().padRight(20);
        idiomaRow.add(btnEspanol).width(100).height(28).padRight(6);
        idiomaRow.add(btnEnglish).width(100).height(28);
        idiomaRow.add().expandX();
        
        boolean usarFlechas= game.playerManager.getPlayerLogeado().isUsarFlechas();

        Label lblControlTitulo = new Label(traducir("Controles", "Controls"), skin, "medium-white");

        btnWasd= new TextButton("WASD", skin, "small");
        btnFlechas= new TextButton(traducir("Flechas", "Arrows"), skin, "small");
        actualizarBotonControl(btnWasd, btnFlechas, usarFlechas);

        btnWasd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerManager.cambiarControl(false);
                actualizarBotonControl(btnWasd, btnFlechas, false);
            }
        });
        btnFlechas.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerManager.cambiarControl(true);
                actualizarBotonControl(btnWasd, btnFlechas, true);
            }
        });

        Table controlRow= new Table();
        controlRow.add(lblControlTitulo).left().padRight(20);
        controlRow.add(btnWasd).width(100).height(28).padRight(6);
        controlRow.add(btnFlechas).width(100).height(28);
        controlRow.add().expandX();
        
        TextButton btnVolver = new TextButton(traducir("Volver","Back"), skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        TextButton btnLogout = new TextButton(traducir("Cerrar sesion","Log Out"), skin, "default");
        btnLogout.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playerManager.cerrarSesion();
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });

        Table btnRow = new Table();
        btnRow.add(btnVolver).width(140).height(32).padRight(10);
        btnRow.add(btnLogout).width(140).height(32);
        
        Window panel = createWindow();
        panel.add(new Label(traducir("Configuracion","Configuration"), skin, "title-white")).center().padBottom(24).row();
        panel.add(volumenRow).left().padBottom(20).row();
        panel.add(idiomaRow).left().padBottom(24).row();
        panel.add(controlRow).left().padBottom(24).row();
        panel.add(btnRow).center();
        panel.pack();
        setRoot(panel);
    }

    private String formatearIdioma(String idioma) {
        if ("espanol".equals(idioma)) return "Espanol";
        if ("english".equals(idioma)) return "English";
        return idioma;
    }

    private void actualizarBotonIdioma(TextButton es, TextButton en, String activo) {
        Color gris = new Color(0.5f, 0.5f, 0.5f, 1f);
        Color blanco = Color.WHITE;
        if ("espanol".equals(activo)) {
            es.getLabel().setColor(blanco);
            en.getLabel().setColor(gris);
        } else {
            es.getLabel().setColor(gris);
            en.getLabel().setColor(blanco);
        }
    }
    private void actualizarBotonControl(TextButton wasd, TextButton flechas, boolean usaFlechas){
        Color gris= new Color(0.5f, 0.5f, 0.5f, 1f);
        Color blanco= Color.WHITE;
        if(usaFlechas){
            wasd.getLabel().setColor(gris);
            flechas.getLabel().setColor(blanco);
        }else{
            wasd.getLabel().setColor(blanco);
            flechas.getLabel().setColor(gris);
        }
    }
}
