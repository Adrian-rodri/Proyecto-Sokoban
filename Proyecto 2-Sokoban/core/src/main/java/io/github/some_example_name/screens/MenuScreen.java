package io.github.some_example_name.screens;

import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.screens.LevelSelectScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class MenuScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;

    private Texture texPanel, texBtnAzul, texBtnAzulHover,
            texBtnRojo, texBtnRojoHover, texBtnGris, texBtnGrisHover, texPixel;

    public MenuScreen(Main game) {
        this.game = game;
    }

    private Texture solidTex(int w, int h, Color c) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(c);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private Texture bordeTex(int w, int h, Color fondo, Color borde, int g) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(fondo);
        p.fill();
        p.setColor(borde);
        for (int i = 0; i < g; i++) {
            p.drawRectangle(i, i, w - i * 2, h - i * 2);
        }
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    private Skin construirSkin() {
        Skin s = new Skin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.1f);
        s.add("default-font", font);

        BitmapFont fontTitle = new BitmapFont();
        fontTitle.getData().setScale(2.8f);
        s.add("title-font", fontTitle);

        BitmapFont fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.9f);
        s.add("small-font", fontSmall);

        texBtnAzul = bordeTex(16, 16, new Color(0.20f, 0.38f, 0.76f, 1f), new Color(0.15f, 0.38f, 0.75f, 1f), 1);
        texBtnAzulHover = bordeTex(16, 16, new Color(0.28f, 0.58f, 1.00f, 1f), new Color(0.20f, 0.48f, 0.90f, 1f), 1);
        texBtnRojo = bordeTex(16, 16, new Color(0.85f, 0.35f, 0.28f, 1f), new Color(0.65f, 0.22f, 0.16f, 1f), 1);
        texBtnRojoHover = bordeTex(16, 16, new Color(0.95f, 0.42f, 0.33f, 1f), new Color(0.75f, 0.28f, 0.20f, 1f), 1);
        texBtnGris = bordeTex(16, 16, new Color(0.25f, 0.25f, 0.38f, 1f), new Color(0.18f, 0.18f, 0.30f, 1f), 1);
        texBtnGrisHover = bordeTex(16, 16, new Color(0.32f, 0.32f, 0.48f, 1f), new Color(0.25f, 0.25f, 0.40f, 1f), 1);
        texPanel = bordeTex(16, 16, new Color(0.12f, 0.15f, 0.25f, 1f), new Color(0.20f, 0.25f, 0.40f, 1f), 1);
        texPixel = solidTex(1, 1, Color.CLEAR);

        NinePatchDrawable npAzul = new NinePatchDrawable(new NinePatch(texBtnAzul, 4, 4, 4, 4));
        NinePatchDrawable npAzulHov = new NinePatchDrawable(new NinePatch(texBtnAzulHover, 4, 4, 4, 4));
        NinePatchDrawable npRojo = new NinePatchDrawable(new NinePatch(texBtnRojo, 4, 4, 4, 4));
        NinePatchDrawable npRojoHov = new NinePatchDrawable(new NinePatch(texBtnRojoHover, 4, 4, 4, 4));
        NinePatchDrawable npGris = new NinePatchDrawable(new NinePatch(texBtnGris, 4, 4, 4, 4));
        NinePatchDrawable npGrisHov = new NinePatchDrawable(new NinePatch(texBtnGrisHover, 4, 4, 4, 4));
        NinePatchDrawable npPanel = new NinePatchDrawable(new NinePatch(texPanel, 4, 4, 4, 4));

        TextButton.TextButtonStyle tbAzul = new TextButton.TextButtonStyle();
        tbAzul.font = font;
        tbAzul.fontColor = Color.WHITE;
        tbAzul.up = npAzul;
        tbAzul.over = npAzulHov;
        tbAzul.down = npAzulHov;
        s.add("azul", tbAzul, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle tbRojo = new TextButton.TextButtonStyle();
        tbRojo.font = font;
        tbRojo.fontColor = Color.WHITE;
        tbRojo.up = npRojo;
        tbRojo.over = npRojoHov;
        tbRojo.down = npRojoHov;
        s.add("rojo", tbRojo, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle tbGris = new TextButton.TextButtonStyle();
        tbGris.font = fontSmall;
        tbGris.fontColor = new Color(0.75f, 0.75f, 0.90f, 1f);
        tbGris.up = npGris;
        tbGris.over = npGrisHov;
        tbGris.down = npGrisHov;
        s.add("gris", tbGris, TextButton.TextButtonStyle.class);

        s.add("titulo", new Label.LabelStyle(fontTitle, Color.WHITE), Label.LabelStyle.class);
        s.add("saludo", new Label.LabelStyle(fontSmall, new Color(0.70f, 0.75f, 0.95f, 1f)), Label.LabelStyle.class);

        Window.WindowStyle ws = new Window.WindowStyle();
        ws.titleFont = font;
        ws.titleFontColor = Color.CLEAR;
        ws.background = npPanel;
        s.add("default", ws, Window.WindowStyle.class);

        return s;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = construirSkin();

        String nombreJugador = game.playerManager.getNombreJugador();

        Label lblTitulo = new Label("SOKOBAN", skin, "titulo");
        Label lblSaludo = new Label("Hola, " + nombreJugador, skin, "saludo");

        TextButton btnJugar = new TextButton("Jugar", skin, "azul");
        TextButton btnPerfil = new TextButton("Mi perfil", skin, "azul");
        TextButton btnReporte = new TextButton("Reportes", skin, "azul");
        TextButton btnSalir = new TextButton("Salir", skin, "rojo");
        TextButton btnConfig = new TextButton("[Config]", skin, "gris");
        TextButton btnAmigos = new TextButton("[Log Out]", skin, "gris");
        TextButton btnAyuda = new TextButton("  ?  ", skin, "gris");

        float bw = 240, bh = 42, iconW = 64, iconH = 32;

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(24f, 28f, 20f, 28f);
        panel.add(lblTitulo).colspan(3).center().padBottom(4).row();
        panel.add(lblSaludo).colspan(3).center().padBottom(18).row();
        panel.add(btnJugar).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnPerfil).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnReporte).colspan(3).width(bw).height(bh).padBottom(8).row();
        panel.add(btnSalir).colspan(3).width(bw).height(bh).padBottom(14).row();
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
            public void changed(ChangeEvent e, Actor a) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        btnPerfil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                game.setScreen(new ProfileScreen(game));
                dispose();
            }
        });
        btnReporte.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                System.out.println("[Menu] Reportes");
            }
        });
        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });
        btnConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                System.out.println("[Menu] Config");
            }
        });
        btnAmigos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                game.playerManager.cerrarSesion();
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });
        btnAyuda.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor a) {
                System.out.println("[Menu] Ayuda");
            }
        });
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
        texPanel.dispose();
        texBtnAzul.dispose();
        texBtnAzulHover.dispose();
        texBtnRojo.dispose();
        texBtnRojoHover.dispose();
        texBtnGris.dispose();
        texBtnGrisHover.dispose();
        texPixel.dispose();
    }
}
