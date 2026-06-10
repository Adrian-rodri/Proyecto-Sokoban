package io.github.some_example_name.model;

import io.github.some_example_name.game.EstadoTurno;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.some_example_name.util.Constantes;
import java.util.ArrayList;
import io.github.some_example_name.game.EstadoTurno;
import io.github.some_example_name.game.Nivel;
import io.github.some_example_name.util.Constantes;

/**
 *
 * @author adria
 */
public class Player {

    public ArrayList<EstadoTurno> copias = new ArrayList<>();
    private Texture sheetCabeza, sheetTorso, sheetAccesorio;
    public int x, y;
    //atributos que se van a guardar en users.skb
    private String userName, password;
    private int puntos;

    //atributos guardados en carpetas aparte
    //perfil.skb
    private String nombreCompleto, rutaAvatar;
    private long fechaRegistro, ultimaSesion;
    private double volumen;
    private String idioma;
    //amigos.skb
    private ArrayList<String> amigos; //se escribe uno por uno en el archivo
    // historial.skb
    private ArrayList<EntradaHistorial> historial;

    //stats.skb
    private int partidasJugadas, nivelesCompletados;
    private int mejorPuntaje, puntajeGeneral;
    private double tiempoJugadoHoras;
    private double tiempoPromedioPorNivel;

    //progreso.skb
    private int nivelesDesbloqueados;

    //avatar.skb
    private int colCabeza, filaCabeza;
    private int colTorso, filaTorso;
    private int colAccesorio, filaAccesorio;

    private int spriteCol;
    private int spriteFila;

    protected TextureRegion playerSprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Player(String userName, String password, String nombreCompleto) {
        this.x = 0;
        this.y = 0;
        this.userName = userName;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    public Player(String userName, String password, int puntos, String nombreCompleto,
            String rutaAvatar, long fechaRegistro, long ultimaSesion, double volumen,
            String idioma, ArrayList<String> amigos, int partidasJugadas, int nivelesCompletados,
            int mejorPuntaje, int puntajeGeneral, double tiempoJugadoHoras, double tiempoPromedioPorNivel,
            int nivelesDesbloqueados, int colCabeza, int filaCabeza, int colTorso, int filaTorso,
            int colAccesorio, int filaAccesorio, ArrayList<EntradaHistorial> historial) {
        this.x = 0;
        this.y = 0;
        this.userName = userName;
        this.password = password;
        this.puntos = puntos;
        this.nombreCompleto = nombreCompleto;
        this.rutaAvatar = rutaAvatar;
        this.fechaRegistro = fechaRegistro;
        this.ultimaSesion = ultimaSesion;
        this.volumen = volumen;
        this.idioma = idioma;
        this.amigos = amigos;
        this.partidasJugadas = partidasJugadas;
        this.nivelesCompletados = nivelesCompletados;
        this.mejorPuntaje = mejorPuntaje;
        this.puntajeGeneral = puntajeGeneral;
        this.tiempoJugadoHoras = tiempoJugadoHoras;
        this.tiempoPromedioPorNivel = tiempoPromedioPorNivel;
        this.nivelesDesbloqueados = nivelesDesbloqueados;
        this.colCabeza = colCabeza;
        this.filaCabeza = filaCabeza;
        this.colTorso = colTorso;
        this.filaTorso = filaTorso;
        this.colAccesorio = colAccesorio;
        this.filaAccesorio = filaAccesorio;
        this.historial = historial;
        this.spriteCol = 2;
        this.spriteFila = 0;
    }

    public void tecladoInput(Nivel nivelActual) {
        char[][] level = nivelActual.getLevel();
        int dirX = 0, dirY = 0;

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nivelActual.reiniciar();
            //level=nivelActual.getLevel();
            GameScreen.initPlayer = false;
        }
        //undo
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (!copias.isEmpty()) {
                int indexAnterior = copias.size() - 1;
                EstadoTurno estado = copias.get(indexAnterior);
                copias.remove(indexAnterior);
                nivelActual.setLevel(estado.matriz);
                x = estado.playerX;
                y = estado.playerY;
                spriteCol = estado.spriteCol;
                spriteFila = estado.spriteFila;
                level = estado.matriz;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            dirY += 1;
            //y+=Constantes.TILE_SIZE;
            spriteCol = 1;
            spriteFila = 3;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            dirY -= 1;
            //y-=Constantes.TILE_SIZE;
            spriteCol = 2;
            spriteFila = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dirX -= 1;
            //x-=Constantes.TILE_SIZE;
            spriteCol = 1;
            spriteFila = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dirX += 1;
            //x+=Constantes.TILE_SIZE;
            spriteCol = 1;
            spriteFila = 2;
        }
        this.setCabeza(spriteCol, spriteFila);
        if (dirX != 0 || dirY != 0) {
            int posX = x / Constantes.TILE_SIZE;
            int posY = (level.length - 1) - (y / Constantes.TILE_SIZE);

            //prediccion de las casillas segun la direccion
            int predictCol = posX + dirX;
            int predictFila = posY - dirY;

            if (predictFila >= 0 && predictFila < level.length && predictCol >= 0 && predictCol < level[predictFila].length) {
                char destino = level[predictFila][predictCol];
                //caminar a casilla vacio o una meta
                if (destino == 'a' || destino == '0' || destino == 'p') {
                    copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila));
                    x += dirX * Constantes.TILE_SIZE;
                    y += dirY * Constantes.TILE_SIZE;
                    //empujar una caja
                } else if (destino == 'b' || destino == 'B') {
                    int cajaFila = predictFila - dirY;
                    int cajaCol = predictCol + dirX;

                    if (cajaFila >= 0 && cajaFila < level.length && cajaCol >= 0 && cajaCol < level[cajaFila].length) {
                        char cajaDestino = level[cajaFila][cajaCol];
                        if (cajaDestino == 'a' || cajaDestino == 'p' || cajaDestino == '0') {
                            copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila));
                            if (destino == 'B') {
                                level[predictFila][predictCol] = '0';
                            } else {
                                level[predictFila][predictCol] = 'a';
                            }

                            if (cajaDestino == '0') {
                                level[cajaFila][cajaCol] = 'B';
                                System.out.println("Una caja en su lugar");
                            } else {
                                level[cajaFila][cajaCol] = 'b';
                            }

                            this.x += dirX * Constantes.TILE_SIZE;
                            this.y += dirY * Constantes.TILE_SIZE;

                        }
                    }
                }
            }

        }
    }

    public void dibujarPlayer(ShapeRenderer shape) {//metodo temporal-> dibujar un cuadrado en lugar de imagen
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }

    public void dibujarPlayer(SpriteBatch batch) {
        if (playerSprite != null) {
            batch.draw(playerSprite, x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        }
    }

    public void setCabeza(int col, int fila) {
        playerSprite = new TextureRegion(sheetCabeza, col * 32, fila * 32, 32, 32);
    }

    public void setTorso(int col, int fila) {
        //Vacio por momento, falta crear el spriteSheet
    }

    public void setAccesorio(int col, int fila) {
        //Vacio por momento, falta crear el spriteSheet
    }

    public void cargarSprites(Texture cabeza, Texture torso, Texture accesorio) {
        this.sheetCabeza = cabeza;
        this.sheetTorso = torso;
        this.sheetAccesorio = accesorio;
    }

    public Texture getSheetCabeza() {
        return sheetCabeza;
    }

    public Texture getSheetTorso() {
        return sheetTorso;
    }

    public Texture getSheetAccesorio() {
        return sheetAccesorio;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRutaAvatar() {
        return rutaAvatar;
    }

    public long getFechaRegistro() {
        return fechaRegistro;
    }

    public long getUltimaSesion() {
        return ultimaSesion;
    }

    public double getVolumen() {
        return volumen;
    }

    public String getIdioma() {
        return idioma;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public ArrayList<EntradaHistorial> getHistorial() {
        return historial;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public int getNivelesCompletados() {
        return nivelesCompletados;
    }

    public int getMejorPuntaje() {
        return mejorPuntaje;
    }

    public int getPuntajeGeneral() {
        return puntajeGeneral;
    }

    public double getTiempoJugadoHoras() {
        return tiempoJugadoHoras;
    }

    public double getTiempoPromedioPorNivel() {
        return tiempoPromedioPorNivel;
    }

    public int getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    public int getColCabeza() {
        return colCabeza;
    }

    public int getFilaCabeza() {
        return filaCabeza;
    }

    public int getColTorso() {
        return colTorso;
    }

    public int getFilaTorso() {
        return filaTorso;
    }

    public int getColAccesorio() {
        return colAccesorio;
    }

    public int getFilaAccesorio() {
        return filaAccesorio;
    }

    public TextureRegion getPlayerSprite() {
        return playerSprite;
    }

    @Override
    public String toString() {
        return "Player{"
                + "userName='" + userName + '\''
                + ", nombreCompleto='" + nombreCompleto + '\''
                + ", fechaRegistro=" + fechaRegistro
                + ", nivelesDesbloqueados=" + nivelesDesbloqueados
                + ", partidasJugadas=" + partidasJugadas
                + ", puntajeGeneral=" + puntajeGeneral
                + '}';
    }

    //-------------------JJ
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setPartidasJugadas(int n) {
        this.partidasJugadas = n;
    }

    public void setNivelesCompletados(int n) {
        this.nivelesCompletados = n;
    }

    public void setMejorPuntaje(int n) {
        this.mejorPuntaje = n;
    }

    public void setPuntajeGeneral(int n) {
        this.puntajeGeneral = n;
    }

    public void setTiempoJugadoHoras(double t) {
        this.tiempoJugadoHoras = t;
    }

    public void setTiempoPromedioPorNivel(double t) {
        this.tiempoPromedioPorNivel = t;
    }

    public void setNivelesDesbloqueados(int n) {
        this.nivelesDesbloqueados = n;
    }

    public void setUltimaSesion(long t) {
        this.ultimaSesion = t;
    }

}
