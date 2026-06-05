package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

/**
 *
 * @author adria
 */
public class Player{
    private Texture sheetCabeza,sheetTorso,sheetAccesorio;
    protected int x,y;
    //atributos que se van a guardar en users.skb
    private String userName, password;
    private int puntos;
    
    //atributos guardados en carpetas aparte
    //perfil.skb
    private String nombreCompleto,rutaAvatar;
    private long fechaRegistro,ultimaSesion;
    private double volumen;
    private String idioma;
    //amigos.skb
    private ArrayList<String> amigos; //se escribe uno por uno en el archivo
    
    //stats.skb
    private int partidasJugadas,nivelelesCompletados;
    private int mejorPuntaje,puntajeGeneral ;
    private double tiempoJugadoHoras;
    private double tiempoPromedioPorNivel;
    
    //progreso.skb
    private int nivelesDesbloqueados;
    
    //avatar.skb
    private int colCabeza,filaCabeza;
    private int colTorso,filaTorso;
    private int colAccesorio,filaAccesorio;
    
    protected TextureRegion playerSprite;
    public Player(int x, int y) {
        this.x=x;
        this.y=y;
    }
    public Player(String userName, String password, String nombreCompleto) {
        this.x=0;
        this.y=0;
        this.userName=userName;
        this.password=password;
        this.nombreCompleto=nombreCompleto;
    }
    public Player(String userName, String password, String nombreCompleto,String idioma) {
        this.x=0;
        this.y=0;
        this.userName=userName;
        this.password=password;
        this.nombreCompleto=nombreCompleto;
        this.idioma=idioma;
    }

    public Player(String userName, String password, int puntos, String nombreCompleto, String rutaAvatar, long fechaRegistro, long ultimaSesion, double volumen, String idioma, ArrayList<String> amigos, int partidasJugadas, int nivelelesCompletados, int mejorPuntaje, int puntajeGeneral, double tiempoJugadoHoras, double tiempoPromedioPorNivel, int nivelesDesbloqueados, int colCabeza, int filaCabeza, int colTorso, int filaTorso, int colAccesorio, int filaAccesorio) {
        this.x=0;
        this.y=0;
        this.userName= userName;
        this.password= password;
        this.puntos= puntos;
        this.nombreCompleto= nombreCompleto;
        this.rutaAvatar= rutaAvatar;
        this.fechaRegistro= fechaRegistro;
        this.ultimaSesion= ultimaSesion;
        this.volumen= volumen;
        this.idioma= idioma;
        this.amigos= amigos;
        this.partidasJugadas= partidasJugadas;
        this.nivelelesCompletados= nivelelesCompletados;
        this.mejorPuntaje= mejorPuntaje;
        this.puntajeGeneral= puntajeGeneral;
        this.tiempoJugadoHoras= tiempoJugadoHoras;
        this.tiempoPromedioPorNivel= tiempoPromedioPorNivel;
        this.nivelesDesbloqueados= nivelesDesbloqueados;
        this.colCabeza= colCabeza;
        this.filaCabeza= filaCabeza;
        this.colTorso= colTorso;
        this.filaTorso= filaTorso;
        this.colAccesorio= colAccesorio;
        this.filaAccesorio= filaAccesorio;
    }
    
    
    public void tecladoInput(Nivel nivelActual){
        char [][] level= nivelActual.getLevel();
        int dirX=0,dirY=0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            nivelActual.reiniciar();
            //level=nivelActual.getLevel();
            GameScreen.initPlayer=false;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            dirY+=1;
            //y+=Constantes.TILE_SIZE;
            this.setCabeza(1, 3);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            dirY-=1;            
            //y-=Constantes.TILE_SIZE;
            this.setCabeza(2, 0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            dirX-=1;            
            //x-=Constantes.TILE_SIZE;
            this.setCabeza(1, 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            dirX+=1;
            //x+=Constantes.TILE_SIZE;
            this.setCabeza(1, 2);
        }
        if(dirX!=0||dirY!=0){
            int posX= x/Constantes.TILE_SIZE;
            int posY= (level.length-1)-(y/Constantes.TILE_SIZE);
            
            //prediccion de las casillas segun la direccion
            int predictCol= posX+dirX;
            int predictFila= posY-dirY;
            
            if(predictFila>=0 &&predictFila<level.length && predictCol>=0 && predictCol<level[predictFila].length){
                char destino= level[predictFila][predictCol];
                if(destino=='a'|| destino=='0' || destino =='p'){
                    x+= dirX *Constantes.TILE_SIZE;
                    y+= dirY *Constantes.TILE_SIZE;
                }else if(destino=='b' || destino=='B'){
                    int cajaFila= predictFila-dirY;
                    int cajaCol= predictCol+dirX;
                    
                    if(cajaFila >=0 && cajaFila<level.length&& cajaCol>=0 && cajaCol<level[cajaFila].length){
                        char cajaDestino=level[cajaFila][cajaCol];
                        if(cajaDestino=='a' || cajaDestino=='p'|| cajaDestino=='0'){
                            if(destino =='B')
                                level[predictFila][predictCol]='0';
                            else
                                level[predictFila][predictCol]='a';
                            
                            if(cajaDestino =='0'){
                                level[cajaFila][cajaCol]='B';
                                System.out.println("Una caja en su lugar");
                            }else
                                level[cajaFila][cajaCol]='b';
                            
                            
                            this.x += dirX * Constantes.TILE_SIZE;
                            this.y += dirY * Constantes.TILE_SIZE;
                            
                                
                        }
                    }
                }
            }
            
        }
    }
    
    public void dibujarPlayer(ShapeRenderer shape){//metodo temporal-> dibujar un cuadrado en lugar de imagen
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }
    public void dibujarPlayer(SpriteBatch batch){
        if(playerSprite!=null)
        batch.draw(playerSprite, x, y,Constantes.TILE_SIZE,Constantes.TILE_SIZE);
    }
    public void setCabeza(int col, int fila){
        playerSprite= new TextureRegion(sheetCabeza,col*32,fila*32,32,32);
    }
    public void setTorso(int col, int fila){
     //Vacio por momento, falta crear el spriteSheet
    }
    public void setAccesorio(int col, int fila){
     //Vacio por momento, falta crear el spriteSheet
    }
    public void cargarSprites(Texture cabeza, Texture torso, Texture accesorio){
        this.sheetCabeza=cabeza;
        this.sheetTorso=torso;
        this.sheetAccesorio=accesorio;
    }
}
