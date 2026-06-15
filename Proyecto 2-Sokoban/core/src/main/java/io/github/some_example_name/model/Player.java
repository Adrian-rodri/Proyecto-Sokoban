package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Texture playerSheet;
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
    private String avatarFile;

    private int spriteCol;
    private int spriteFila;
    
    //animacion
    private int walkFrame = 0;
    public static final int[] WALK_CYCLE = {0, 1, 2, 1};
    private int offsetPushX = 0;
    private int offsetPushY = 0;
    private boolean persistOffset = false;

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
            int nivelesDesbloqueados, String avatarFile,ArrayList<EntradaHistorial> historial) {
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
        this.avatarFile=avatarFile;
        this.historial = historial;
        this.spriteCol = 2;
        this.spriteFila = 0;
    }

    public int tecladoInput(Nivel nivelActual) {
        char[][] level = nivelActual.getLevel();
        int dirX = 0, dirY = 0;

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
                offsetPushX = estado.offsetPushX;
                offsetPushY = estado.offsetPushY;
                this.resetPushOffset();
                setSprite(spriteCol, spriteFila);
                level = estado.matriz;
                return -1;
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
            spriteCol = 1;
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
        setSprite(spriteCol, spriteFila);
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
                    copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila,offsetPushX,offsetPushY));
                    x += dirX * Constantes.TILE_SIZE;
                    y += dirY * Constantes.TILE_SIZE;
                    if (!persistOffset) {
                        offsetPushX = 0;
                        offsetPushY = 0;
                    } else {
                        offsetPushX /= 2;
                        offsetPushY /= 2;
                    }
                    persistOffset = false;
                    walkFrame++;
                    spriteCol = WALK_CYCLE[walkFrame % 4];
                    this.setSprite(spriteCol, spriteFila);
                    return 1;
                    //empujar una caja
                } else if (destino == 'b' || destino == 'B') {
                    int cajaFila = predictFila - dirY;
                    int cajaCol = predictCol + dirX;

                    if (cajaFila >= 0 && cajaFila < level.length && cajaCol >= 0 && cajaCol < level[cajaFila].length) {
                        char cajaDestino = level[cajaFila][cajaCol];
                        if (cajaDestino == 'a' || cajaDestino == 'p' || cajaDestino == '0') {
                            copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila,offsetPushX,offsetPushY));
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
                            offsetPushX = dirX * Constantes.TILE_SIZE / 2;
                            offsetPushY = (dirY < 0) ? 0 : dirY * Constantes.TILE_SIZE / 2;
                            persistOffset = true;
                            walkFrame++;
                            spriteCol = WALK_CYCLE[walkFrame % 4];
                            this.setSprite(spriteCol, spriteFila);
                            return 1;

                        }
                    }
                }
            }

        }
        return 0;
    }

    public void dibujarPlayer(ShapeRenderer shape) {//metodo temporal-> dibujar un cuadrado en lugar de imagen
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }

    public void dibujarPlayer(SpriteBatch batch, int offsetX, int offsetY) {
        if (playerSprite != null) {
            batch.draw(playerSprite, x + offsetX, y + offsetY, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        }
    }

    public void setSprite(int col, int fila) {
        playerSprite = new TextureRegion(playerSheet, col * 32, fila * 32, 32, 32);
    }

    public void cargarSprites(Texture playerSheet) {
        this.playerSheet=playerSheet;
    }
    public void avanzarWalkFrame(){
        walkFrame= (walkFrame+1)%WALK_CYCLE.length;
    }
    public void resetPushOffset() {
        this.offsetPushX = 0;
        this.offsetPushY = 0;
        this.persistOffset = false;
    }

    public void setAvatarFile(String avatarFile) {
        this.avatarFile = avatarFile;
    }

    public void setWalkFrame(int walkFrame) {
        this.walkFrame = walkFrame;
    }
    

    public String getAvatarFile() {
        return avatarFile;
    }

    public int getWalkFrame() {
        return walkFrame;
    }

    public int getOffsetPushX() {
        return offsetPushX;
    }

    public int getOffsetPushY() {
        return offsetPushY;
    }

    public boolean getPersistOffset() {
        return persistOffset;
    }

    public void setOffsetPushX(int offsetPushX) {
        this.offsetPushX = offsetPushX;
    }

    public void setOffsetPushY(int offsetPushY) {
        this.offsetPushY = offsetPushY;
    }

    public void setPersistOffset(boolean persistOffset) {
        this.persistOffset = persistOffset;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
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
