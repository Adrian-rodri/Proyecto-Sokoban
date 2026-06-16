package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.model.Player;
import java.util.ArrayList;

public class ProfileScreen extends BaseScreen {


    private TextButton btnVolver;
    private Texture avatarTexture;
    private Label lblError;
    private float errorTimer = 0f;

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

            perfilTable.add(new Label(traducir("Partidas: ", "Games:  ")+ p.getPartidasJugadas(), skin, "dato-perfil")).left().padBottom(3).row();
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

        Table editTable = new Table();
        editTable.top().left();
        float anchoBoton = 260;

        String avatarPath= "texturas/avatares/" + p.getAvatarFile();
        try{
            avatarTexture= new Texture(avatarPath);
        }catch(Exception e){
            avatarTexture= new Texture("texturas/avatares/1-default.png");
            p.setAvatarFile("1-default.png");
        }
        TextureRegion avatarRegion= new TextureRegion(avatarTexture, 0, 0, 32, 32);
        Image avatarImage= new Image(avatarRegion);

        editTable.add(avatarImage).center().size(128, 128).padBottom(8).row();

        TextButton btnCambiarAvatar = new TextButton("Cambiar avatar", skin, "small");
        editTable.add(btnCambiarAvatar).center().padBottom(20).row();
        btnCambiarAvatar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //mostrarSelectorAvatar(avatarImage);
            }
        });

        TextButton btnEditarPerfil= new TextButton("Editar perfil", skin, "default");
        editTable.add(btnEditarPerfil).center().width(anchoBoton).height(32).padBottom(10).row();
        btnEditarPerfil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                mostrarDialogoEditarPerfil();
            }
        });

        lblError= new Label("", skin, "error");
        editTable.add(lblError).width(anchoBoton).padBottom(8).row();

        btnVolver= new TextButton("Volver", skin, "default");

        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Table btnRow= new Table();
        btnRow.add(btnVolver).width(155).height(32);

        Window panel = createWindow();
        panel.add(new Label("Mi Perfil", skin, "title-white")).colspan(2).center().padBottom(16).row();
        panel.add(perfilTable).left().width(260).padRight(20).expandY();
        panel.add(editTable).left().width(anchoBoton).expandY().row();
        panel.add(btnRow).colspan(2).center().padTop(10);
        panel.pack();
        setRoot(panel);
    }
    private void mostrarDialogoEditarPerfil() {
        Player player= game.playerManager.getPlayerLogeado();
        if (player==null) 
            return;

        TextField campoNombre= new TextField(player.getNombreCompleto(), skin);
        TextField campoUsuario= new TextField(player.getUserName(), skin);
        Label lblErrorEdit= new Label("", skin, "error");

        Dialog dialog= new Dialog("", skin, "tool") {
            @Override
            protected void result(Object object){
                if (!(object instanceof Boolean)|| !(Boolean) object) 
                    return;
                onGuardar(campoNombre.getText().trim(), campoUsuario.getText().trim());
            }
        };
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);
        dialog.pad(24, 36, 20, 36);

        float anchoCampo= 320;
        dialog.getContentTable().add(new Label(traducir("Editar perfil","Edit Profile"), skin, "medium-white")).left().padBottom(18).row();
        
        dialog.getContentTable().add(new Label(traducir("Nombre completo:","Full name:"), skin, "small-white")).left().padBottom(5).row();
        
        campoNombre.setMessageText(traducir("Tu nombre completo","Your full name"));
        
        dialog.getContentTable().add(campoNombre).width(anchoCampo).height(34).padBottom(16).row();
        dialog.getContentTable().add(new Label(traducir("Nombre de usuario:","Username:"), skin, "small-white")).left().padBottom(5).row();
        
        campoUsuario.setMessageText(traducir("Minimo 3 caracteres","Min 3 characters"));
        dialog.getContentTable().add(campoUsuario).width(anchoCampo).height(34).padBottom(20).row();
        
        TextButton btnCambiarPass= new TextButton(traducir("Cambiar contrasena","Change password"), skin, "small");
        dialog.getContentTable().add(btnCambiarPass).left().padBottom(12).row();
        
        btnCambiarPass.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                mostrarDialogoPassword();
            }
        });

        TextButton btnEliminar= new TextButton(traducir("Eliminar cuenta","Delete account"), skin, "small");
        btnEliminar.setColor(0.9f, 0.3f, 0.3f, 1f);
        dialog.getContentTable().add(btnEliminar).left().padBottom(18).row();
        
        dialog.getContentTable().add(lblErrorEdit).width(anchoCampo).padBottom(8).row();
        
        dialog.button(traducir("Guardar cambios","Save changes"), true);
        dialog.button(traducir("Cancelar","Cancel"), false);
        dialog.pack();
        dialog.show(stage);
    }
    private void onGuardar(String nombre, String user) {
        Player p= game.playerManager.getPlayerLogeado();
        if(p==null) 
            return;
        
        boolean cambios= false;

        if(!nombre.isEmpty() &&!nombre.equals(p.getNombreCompleto())){
            game.playerManager.cambiarNombreCompleto(nombre);
            cambios= true;
        }

        if(!user.isEmpty()&& !user.equals(p.getUserName())){
            if (user.length()<3){
                mostrarError(traducir("Usuario muy corto (min 3)","Username too short (min 3)"));
                return;
            }
            if (game.playerManager.cambiarUserName(user)) {
                cambios= true;
            }else{
                mostrarError(traducir("El usuario ya existe o no se pudo cambiar","Username taken or cannot change"));
                return;
            }
        }
        if(!cambios){
            mostrarError(traducir("No hay cambios que guardar","No changes to save"));
            return;
        }
        mostrarExito(traducir("Cambios guardados","Changes saved"));
    }
    
    private void mostrarDialogoPassword(){
        Label lblReqLen= new Label(traducir("Min 8 caracteres","Min 8 characters"), skin, "small-white");
        Label lblReqMay= new Label(traducir("Una mayuscula","One uppercase"), skin, "small-white");
        Label lblReqNum= new Label(traducir("Un numero","One number"), skin, "small-white");
        Label lblReqSim= new Label(traducir("Un simbolo","One symbol"), skin, "small-white");

        lblReqLen.setColor(0.55f, 0.55f, 0.70f, 1f);
        lblReqMay.setColor(0.55f, 0.55f, 0.70f, 1f);
        lblReqNum.setColor(0.55f, 0.55f, 0.70f, 1f);
        lblReqSim.setColor(0.55f, 0.55f, 0.70f, 1f);

        Table reqTable= new Table();
        reqTable.add(lblReqLen).left().padRight(24);
        reqTable.add(lblReqMay).left().row();
        reqTable.add(lblReqNum).left().padRight(24);
        reqTable.add(lblReqSim).left();

        TextField campoActual= new TextField("", skin);
        campoActual.setPasswordMode(true);
        campoActual.setPasswordCharacter('*');
        campoActual.setMessageText(traducir("Contrasena actual","Current password"));

        TextField campoNueva= new TextField("", skin);
        campoNueva.setPasswordMode(true);
        campoNueva.setPasswordCharacter('*');
        campoNueva.setMessageText(traducir("Nueva contrasena","New password"));

        TextField campoConfirm= new TextField("", skin);
        campoConfirm.setPasswordMode(true);
        campoConfirm.setPasswordCharacter('*');
        campoConfirm.setMessageText(traducir("Confirmar nueva contrasena","Confirm new password"));
        
        //validar requisitos
        campoNueva.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char caracter) {
                String clave= field.getText();
                boolean len= clave.length() >=8;
                boolean may= false, num= false, sim= false;
                for(char letra :clave.toCharArray()){
                    if(Character.isUpperCase(letra)) 
                        may= true;
                    if(Character.isDigit(letra)) 
                        num= true;
                    if(!Character.isLetterOrDigit(letra)) 
                        sim= true;
                }
                setReqStyle(lblReqLen, len);
                setReqStyle(lblReqMay, may);
                setReqStyle(lblReqNum, num);
                setReqStyle(lblReqSim, sim);
            }
        });

        TextButton btnVer= new TextButton(traducir("Ver contrasenas","Show passwords"), skin, "small");
        btnVer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                boolean oculto= campoActual.isPasswordMode();
                campoActual.setPasswordMode(!oculto);
                campoNueva.setPasswordMode(!oculto);
                campoConfirm.setPasswordMode(!oculto);
                btnVer.setText(oculto
                        ? traducir("Ocultar contrasenas","Hide passwords")
                        : traducir("Ver contrasenas","Show passwords"));
            }
        });

        Label lblErrorPass= new Label("", skin, "error");

        Dialog dialog= new Dialog("", skin, "tool");
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);
        dialog.pad(24, 36, 20, 36);

        float anchoCampo= 340;
        dialog.getContentTable().add(new Label(traducir("Cambiar contrasena","Change password"), skin, "medium-white")).left().padBottom(16).row();

        dialog.getContentTable().add(new Label(traducir("Contrasena actual:","Current password:"), skin, "small-white")).left().padBottom(4).row();
        dialog.getContentTable().add(campoActual).width(anchoCampo).height(34).padBottom(14).row();

        dialog.getContentTable().add(new Label(traducir("Nueva contrasena:","New password:"), skin, "small-white")).left().padBottom(4).row();
        dialog.getContentTable().add(campoNueva).width(anchoCampo).height(34).padBottom(6).row();

        dialog.getContentTable().add(reqTable).left().padBottom(14).row();

        dialog.getContentTable().add(new Label(traducir("Confirmar nueva contrasena:","Confirm new password:"), skin, "small-white")) .left().padBottom(4).row();
        dialog.getContentTable().add(campoConfirm).width(anchoCampo).height(34).padBottom(10).row();

        dialog.getContentTable().add(btnVer).left().padBottom(10).row();

        dialog.getContentTable().add(lblErrorPass).width(anchoCampo).padBottom(6).row();

        TextButton btnGuardarPass= new TextButton(traducir("Guardar contrasena","Save password"), skin, "default");
        btnGuardarPass.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor){
                String actual= campoActual.getText();
                String nueva= campoNueva.getText();
                String conf= campoConfirm.getText();

                if (actual.isEmpty() || nueva.isEmpty() || conf.isEmpty()) {
                    lblErrorPass.setText(traducir("Completa todos los campos","Fill all fields"));
                    return;
                }
                if (!nueva.equals(conf)) {
                    lblErrorPass.setText(traducir("Las contrasenas no coinciden","Passwords do not match"));
                    return;
                }
                if (!claveValida(nueva)) {
                    lblErrorPass.setText(traducir("No cumple los requisitos","Does not meet requirements"));
                    return;
                }
                if (!game.playerManager.cambiarPassword(actual, nueva)) {
                    lblErrorPass.setText(traducir("Contrasena actual incorrecta","Current password is wrong"));
                    return;
                }

                campoActual.setText("");
                campoNueva.setText("");
                campoConfirm.setText("");
                mostrarExito(traducir("Contrasena cambiada","Password changed"));
                dialog.hide();
            }
        });

        dialog.getButtonTable().add(btnGuardarPass).padRight(8);
        dialog.button(traducir("Cancelar","Cancel"), false);
        dialog.pack();
        dialog.show(stage);
    }
    private void setReqStyle(Label lbl, boolean ok){
        if(ok){
            lbl.setColor(0.3f, 0.9f, 0.3f, 1f);
        }else{
            lbl.setColor(0.55f, 0.55f, 0.70f, 1f);
        }
    }

    private boolean claveValida(String clave){
        if (clave.length()<8) 
            return false;
        boolean mayuscula= false, digito= false, simbolo= false;
        for (char letra : clave.toCharArray()){
            if (Character.isUpperCase(letra)) 
                mayuscula= true;
            if (Character.isDigit(letra)) 
                digito= true;
            if (!Character.isLetterOrDigit(letra)) 
                simbolo= true;
        }
        return mayuscula && digito &&simbolo;
    }

    private void mostrarError(String msg){
        lblError.setColor(1f, 0.37f, 0.37f, 1f);
        lblError.setText(msg);
        errorTimer= 3f;
    }

    private void mostrarExito(String msg) {
        lblError.setColor(0f, 1f, 0f, 1f);
        lblError.setText(msg);
        errorTimer= 3f;
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
