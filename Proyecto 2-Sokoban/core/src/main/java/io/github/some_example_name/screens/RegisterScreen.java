package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;

public class RegisterScreen extends BaseScreen {

    private TextField campoNombre, campoUser, campoClave, campoConfirm;
    private Label lblError;
    private Label lblReqLen, lblReqMay, lblReqNum, lblReqSim;
    private Table tablaRequisitos;

    public RegisterScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        Label.LabelStyle lsError = new Label.LabelStyle(skin.getFont("small"),
                new Color(1f, 0.3f, 0.3f, 1f));
        Label.LabelStyle lsOk = new Label.LabelStyle(skin.getFont("small"),
                new Color(0.3f, 0.9f, 0.3f, 1f));
        Label.LabelStyle lsNok = new Label.LabelStyle(skin.getFont("small"),
                new Color(0.55f, 0.55f, 0.70f, 1f));
        skin.add("error", lsError, Label.LabelStyle.class);
        skin.add("req-ok", lsOk, Label.LabelStyle.class);
        skin.add("req-nok", lsNok, Label.LabelStyle.class);

        campoNombre = new TextField("", skin);
        campoUser = new TextField("", skin);
        campoClave = new TextField("", skin);
        campoConfirm = new TextField("", skin);

        campoNombre.setMessageText(traducir("Nombre completo","Complete Number"));
        campoUser.setMessageText(traducir("Usuario","User"));
        campoClave.setMessageText(traducir("Contrasena","Password"));
        campoConfirm.setMessageText(traducir("Confirmar contrasena","Confirm Password"));
        campoClave.setPasswordMode(true);
        campoClave.setPasswordCharacter('*');
        campoConfirm.setPasswordMode(true);
        campoConfirm.setPasswordCharacter('*');

        lblError = new Label("", skin, "error");

        lblReqLen = new Label(traducir("Min 8 caracteres","Min 8 characters"), skin, "req-nok");
        lblReqMay = new Label(traducir("Una mayuscula","Upper Case"), skin, "req-nok");
        lblReqNum = new Label(traducir("Un numero","A  number"), skin, "req-nok");
        lblReqSim = new Label(traducir("Un simbolo","A symbol"), skin, "req-nok");

        tablaRequisitos = new Table();
        tablaRequisitos.add(lblReqLen).left().padRight(16);
        tablaRequisitos.add(lblReqMay).left().row();
        tablaRequisitos.add(lblReqNum).left().padRight(16);
        tablaRequisitos.add(lblReqSim).left();

        campoClave.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                actualizarRequisitos(field.getText());
            }
        });

        TextButton btnRegistrar = new TextButton(traducir("Registrarse","Sign Up"), skin, "default");
        TextButton btnVolver = new TextButton(traducir("Volver","Back"), skin, "small");

        btnRegistrar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmar();
            }
        });
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });

        float fieldAncho = 290, fieldAlto = 30, botonAncho = 130, botonAlto = 32;

        Label lblTitulo = new Label("SOKOBAN", skin, "title-white");
        Label lblSubtitulo = new Label(traducir("Crear cuenta","Create Account"), skin, "medium-white");

        Window panel = new Window("", skin);
        panel.setMovable(false);
        panel.pad(28f, 32f, 24f, 32f);

        panel.add(lblTitulo).colspan(2).center().padBottom(2).row();
        panel.add(lblSubtitulo).colspan(2).center().padBottom(20).row();
        panel.add(campoNombre).colspan(2).width(fieldAncho).height(fieldAlto).padBottom(12).row();
        panel.add(campoUser).colspan(2).width(fieldAncho).height(fieldAlto).padBottom(12).row();

        TextButton btnVerPass = new TextButton("Ver", skin, "small");
        Table claveRow = new Table();
        claveRow.add(campoClave).expandX().fillX().height(fieldAlto);
        claveRow.add(btnVerPass).width(48).height(fieldAlto).padLeft(6);
        panel.add(claveRow).colspan(2).fillX().padBottom(4).row();

        panel.add(tablaRequisitos).colspan(2).width(fieldAncho).padBottom(6).row();
        panel.add(campoConfirm).colspan(2).width(fieldAncho).height(fieldAlto).padBottom(10).row();

        btnVerPass.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean oculto = campoClave.isPasswordMode();
                campoClave.setPasswordMode(!oculto);
                campoConfirm.setPasswordMode(!oculto);
                btnVerPass.setText(oculto ?traducir("Ocultar" ,"Hide"): traducir("Ver","Show"));
            }
        });

        panel.add(lblError).colspan(2).width(fieldAncho).padBottom(8).row();

        Table btnRow = new Table();
        btnRow.add(btnRegistrar).width(botonAncho).height(botonAlto).padRight(10);
        btnRow.add(btnVolver).width(botonAncho).height(botonAlto);
        panel.add(btnRow).colspan(2).center().padBottom(4).row();
        panel.pack();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void actualizarRequisitos(String clave) {
        boolean len = clave.length() >= 8;
        boolean upperCase = false, numero = false, simbolo = false;
        for (char ch : clave.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upperCase = true;
            }
            if (Character.isDigit(ch)) {
                numero = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                simbolo = true;
            }
        }
        setRequisito(lblReqLen, len, traducir("Min 8 caracteres","Min 8 characters"), traducir("Min 8 caracteres","Min 8 characters"));
        setRequisito(lblReqMay, upperCase, traducir("Una mayuscula","Upper Case"), traducir("Una mayuscula","Upper Case"));
        setRequisito(lblReqNum, numero, traducir("Un numero","A  number"), traducir("Un numero","A  number"));
        setRequisito(lblReqSim, simbolo, traducir("Un simbolo","A symbol"), traducir("Un simbolo","A symbol"));
    }

    private void setRequisito(Label lbl, boolean ok, String txtOk, String txtNok) {
        lbl.setText(ok ? txtOk : txtNok);
        lbl.setStyle(skin.get(ok ? "req-ok" : "req-nok", Label.LabelStyle.class));
    }

    private void confirmar() {
        String nombre = campoNombre.getText().trim();
        String user = campoUser.getText().trim();
        String clave = campoClave.getText();
        String conf = campoConfirm.getText();

        if (nombre.isEmpty() || user.isEmpty() || clave.isEmpty() || conf.isEmpty()) {
            String txt=traducir("Completa todos los campos","Fill all the fields");
            lblError.setText(txt);
            return;
        }
        if (user.length() < 3) {
            String txt=traducir("Usuario muy corto (min 3)","Username too short (min 3)");
            lblError.setText(txt);
            return;
        }
        if (!clave.equals(conf)) {
            lblError.setText(traducir("Las contrasenas no coinciden","Passwords do not match"));
            return;
        }
        if (!claveValida(clave)) {
            lblError.setText(traducir("No cumple los requisitos","Does not meet requirements"));
            return;
        }

        if (game.playerManager.registrarUser(user, clave, nombre)) {
            game.playerManager.logIn(user, clave);
            MenuScreen menu= new MenuScreen(game);
            game.setScreen(menu);
            menu.mostrarDialogoAyuda();
            dispose();
        } else {
            lblError.setText(traducir("El usuario ya existe","Username taken"));
        }
    }

    private boolean claveValida(String c) {
        if (c.length() < 8) {
            return false;
        }
        boolean upperCase = false, numero = false, simbolo = false;
        for (char ch : c.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upperCase = true;
            }
            if (Character.isDigit(ch)) {
                numero = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                simbolo = true;
            }
        }
        return upperCase && numero && simbolo;
    }   
}
