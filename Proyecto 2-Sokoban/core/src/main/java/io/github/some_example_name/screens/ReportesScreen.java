package io.github.some_example_name.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.model.Player;
import java.util.ArrayList;

public class ReportesScreen extends BaseScreen {

    private TextButton btnVolver;
    private int totalNiveles, nivelesDesbloqueados;
    private int[][] mejores;
    private Player player;

    public ReportesScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        totalNiveles = game.nivelManager.getCantidad();
        player= game.playerManager.getPlayerLogeado();
        nivelesDesbloqueados=player.getNivelesDesbloqueados();

        mejores= new int[totalNiveles][];
        computarMejores();

        TextButton.TextButtonStyle btnBase= skin.get("small", TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle estiloCompletado= new TextButton.TextButtonStyle(btnBase);
        estiloCompletado.fontColor= new Color(0.30f, 0.70f, 0.35f, 1f);
        estiloCompletado.overFontColor= new Color(0.45f, 0.85f, 0.50f, 1f);
        skin.add("reporte-completado", estiloCompletado);

        TextButton.TextButtonStyle estiloDisponible= new TextButton.TextButtonStyle(btnBase);
        estiloDisponible.fontColor= Color.WHITE;
        estiloDisponible.overFontColor= new Color(0.80f, 0.85f, 1f, 1f);
        skin.add("reporte-disponible", estiloDisponible);

        Table nivelTable= new Table();
        for(int i=0;i <totalNiveles;i++){
            String nombre=(i==0)? "Tutorial" : traducir("Nivel ","Level ") + i;

            String texto= nombre;
            String estilo;
            if(mejores[i]!= null){
                estilo= "reporte-completado";
            }else if(mejores[i]== null && i <=nivelesDesbloqueados) {
                estilo= "reporte-disponible";
            }else{
                estilo= "small";
            }

            TextButton btnNivel= new TextButton(texto, skin, estilo);
            final int index= i;
            btnNivel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    mostrarDialogoNivel(index,nombre);
                }
            });
            nivelTable.add(btnNivel).width(170).height(22).padBottom(3).row();
        }

        ScrollPane scroll= new ScrollPane(nivelTable, skin);
        scroll.setForceScroll(false, true);

        Table rankingTable = new Table();
        rankingTable.top().left();

        ArrayList<String[]> ranking= game.playerManager.getRanking();
        rankingTable.add(new Label("Ranking", skin, "medium-white")).left().padBottom(8).row();

        int top= Math.min(10, ranking.size());
        if (top== 0){
            Label lblSinDatos = new Label(traducir("Sin datos","No Data"), skin, "small-white");
            lblSinDatos.setColor(0.4f, 0.4f, 0.6f, 1f);
            rankingTable.add(lblSinDatos).left().row();
        }else{
            for(int i=0; i<top; i++){
                String[] entry= ranking.get(i);
                boolean soyYo= player != null && entry[0].equals(player.getUserName());
                
                Color txtColor;
                if(i==0){
                    txtColor= new Color(1f, 0.85f, 0.2f, 1f);
                }else if(i==1){
                    txtColor = new Color(0.8f, 0.8f, 0.8f, 1f);
                }else if(i==2){
                    txtColor= new Color(0.8f, 0.55f, 0.3f, 1f);
                }else if(soyYo){
                    txtColor= Color.YELLOW;
                }else{
                    txtColor= Color.LIGHT_GRAY;
                }

                String linea= (i+1)+".  "+entry[0]+"  -  "+entry[1]+" pts";
                if(soyYo) 
                    linea +="  <";

                Label lblRank= new Label(linea, skin, "small-white");
                lblRank.setColor(txtColor);
                rankingTable.add(lblRank).left().padBottom(3).row();
            }
        }
        
        btnVolver= new TextButton("Volver", skin, "default");
        btnVolver.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        Table btnRow= new Table();
        btnRow.add(btnVolver).width(140).height(32);

        Window panel= createWindow();
        panel.add(new Label(traducir("Reportes","Reports"), skin, "title-white")).colspan(2).center().padBottom(18).row();
        panel.add(scroll).left().width(200).padRight(20);
        panel.add(rankingTable).left().width(260).row();
        panel.add(btnRow).colspan(2).center().padTop(14);
        panel.pack();
        setRoot(panel);
    }
    private void mostrarDialogoNivel(int index, String nombre){
        Dialog dialogo= new Dialog(nombre, skin, "default");
        dialogo.setMovable(false);
        dialogo.setModal(true);
        dialogo.pad(40, 60, 40, 60);

        Table content= dialogo.getContentTable();

        if(mejores[index]!= null){
            int puntaje= mejores[index][0];
            int movimientos= mejores[index][1];
            int segundos= mejores[index][2];
            String tiempo= String.format("%02d:%02d", segundos / 60, segundos % 60);
            
            String[][] filas={
                {traducir("Mejor puntaje","Best score"),String.valueOf(puntaje)},
                {traducir("Pasos","Moves"),String.valueOf(movimientos)},
                {traducir("Tiempo","Time"),tiempo}
            };

            for (String[] f :filas){
                content.add(new Label(f[0], skin, "default")).left().padRight(20).padBottom(6);
                content.add(new Label(f[1], skin, "default")).right().padBottom(6);
                content.row();
            }
        }else{
            content.add(new Label(traducir("Sin partidas registradas", "No games recorded"),skin, "default")).center().row();
        }

        dialogo.button(traducir("Cerrar", "Close"), null);
        dialogo.show(stage);
    }

    private void computarMejores(){
        if (player==null) 
            return;
        ArrayList<EntradaHistorial> hist= player.getHistorial();
        if (hist==null) 
            return;
        for (EntradaHistorial entrada:hist) {
            int numNivel= entrada.getNivel();
            if (numNivel <0 || numNivel >=totalNiveles) 
                continue;
            if (mejores[numNivel] ==null || entrada.getPuntaje() >mejores[numNivel][0]){
                mejores[numNivel]= new int[]{entrada.getPuntaje(), entrada.getMovimientos(), (int) entrada.getTiempo()};
            }
        }
    }
}
