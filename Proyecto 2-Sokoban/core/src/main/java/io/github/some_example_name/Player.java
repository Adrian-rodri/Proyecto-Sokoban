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
 * @author adria
 */
public class Player {
    // Sistema de deshacer movimientos (Undo)
    private ArrayList<EstadoTurno> copias = new ArrayList<>();
    
    // Texturas y Posición
    private Texture sheetCabeza, sheetTorso, sheetAccesorio;
    protected int x, y;

    // Atributos que se van a guardar en users.skb
    private String userName, password;
    private int puntos;

    // Atributos guardados en perfil.skb
    private String nombreCompleto, rutaAvatar;
    private long fechaRegistro, ultimaSesion;
    private double volumen;
    private String idioma;

    // Atributos guardados en amigos.skb
    private ArrayList<String> amigos; 

    // Atributos guardados en historial.skb
    private ArrayList<EntradaHistorial> historial;

    // Atributos guardados en stats.skb
    private int partidasJugadas, nivelesCompletados;
    private int mejorPuntaje, puntajeGeneral;
    private double tiempoJugadoHoras;
    private double tiempoPromedioPorNivel;

    // Atributos guardados en progreso.skb
    private int nivelesDesbloqueados;

    // Coordenadas del Avatar (avatar.skb)
    private int colCabeza, filaCabeza;
    private int colTorso, filaTorso;
    private int colAccesorio, filaAccesorio;

    // Estado actual de la animación/dirección del sprite
    private int spriteCol;
    private int spriteFila;

    protected TextureRegion playerSprite;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.spriteCol = 2;
        this.spriteFila = 0;
    }

    public Player(String userName, String password, String nombreCompleto) {
        this.x = 0; 
        this.y = 0;
        this.userName = userName;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.spriteCol = 2;
        this.spriteFila = 0;
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

    // ── Lógica de Control y Movimiento ────────────────────────────────────────
    public void tecladoInput(Nivel nivelActual) {
        char[][] level = nivelActual.getLevel();
        int dirX = 0, dirY = 0;

        // Reiniciar Nivel
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nivelActual.reiniciar();
            GameScreen.initPlayer = false;
        }

        // Deshacer Movimiento (Undo)
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

        // Procesar Direcciones y cambiar Sprites
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            dirY += 1; 
            spriteCol = 1; 
            spriteFila = 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            dirY -= 1; 
            spriteCol = 2; 
            spriteFila = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            dirX -= 1; 
            spriteCol = 1; 
            spriteFila = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            dirX += 1; 
            spriteCol = 1; 
            spriteFila = 2;
        }

        this.setCabeza(spriteCol, spriteFila);

        // Lógica de colisión y empuje de cajas
        if (dirX != 0 || dirY != 0) {
            int posX = x / Constantes.TILE_SIZE;
            int posY = (level.length - 1) - (y / Constantes.TILE_SIZE);
            
            int predictCol = posX + dirX;
            int predictFila = posY - dirY;

            if (predictFila >= 0 && predictFila < level.length && predictCol >= 0 && predictCol < level[predictFila].length) {
                char destino = level[predictFila][predictCol];

                // Caminar a casilla vacía, meta o posición inicial
                if (destino == 'a' || destino == '0' || destino == 'p') {
                    copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila));
                    x += dirX * Constantes.TILE_SIZE;
                    y += dirY * Constantes.TILE_SIZE;
                    
                // Empujar una caja estándar o en meta
                } else if (destino == 'b' || destino == 'B') {
                    int cajaFila = predictFila - dirY;
                    int cajaCol = predictCol + dirX;

                    if (cajaFila >= 0 && cajaFila < level.length && cajaCol >= 0 && cajaCol < level[cajaFila].length) {
                        char cajaDestino = level[cajaFila][cajaCol];

                        if (cajaDestino == 'a' || cajaDestino == 'p' || cajaDestino == '0') {
                            // Guardar estado antes de alterar la matriz y mover el jugador
                            copias.add(new EstadoTurno(nivelActual.copiarLevel(level), x, y, spriteCol, spriteFila));

                            // Actualizar casilla donde estaba la caja
                            level[predictFila][predictCol] = (destino == 'B') ? '0' : 'a';
                            
                            // Actualizar casilla a donde va la caja
                            if (cajaDestino == '0') {
                                level[cajaFila][cajaCol] = 'B';
                                System.out.println("Una caja en su lugar");
                            } else {
                                level[cajaFila][cajaCol] = 'b';
                            }

                            // Mover al jugador
                            x += dirX * Constantes.TILE_SIZE;
                            y += dirY * Constantes.TILE_SIZE;
                        }
                    }
                }
            }
        }
    }

    // ── Métodos de Renderizado ────────────────────────────────────────────────
    public void dibujarPlayer(ShapeRenderer shape) {
        shape.setColor(Color.BLUE);
        shape.rect(x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
    }

    public void dibujarPlayer(SpriteBatch batch) {
        if (playerSprite != null) {
            batch.draw(playerSprite, x, y, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        }
    }

    // ── Gestión de Sprites ────────────────────────────────────────────────────
    public void setCabeza(int col, int fila) {
        playerSprite = new TextureRegion(sheetCabeza, col * 32, fila * 32, 32, 32);
    }

    public void setTorso(int col, int fila) {
        // Vacío por el momento, falta crear el spriteSheet
    }

    public void setAccesorio(int col, int fila) {
        // Vacío por el momento, falta crear el spriteSheet
    }

    public void cargarSprites(Texture cabeza, Texture torso, Texture accesorio) {
        this.sheetCabeza = cabeza;
        this.sheetTorso = torso;
        this.sheetAccesorio = accesorio;
    }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setPuntos(int puntos)                         { this.puntos = puntos; }
    public void setIdioma(String idioma)                      { this.idioma = idioma; }
    public void setPartidasJugadas(int n)                     { this.partidasJugadas = n; }
    public void setNivelesCompletados(int n)                  { this.nivelesCompletados = n; }
    public void setMejorPuntaje(int n)                        { this.mejorPuntaje = n; }
    public void setPuntajeGeneral(int n)                      { this.puntajeGeneral = n; }
    public void setTiempoJugadoHoras(double t)                { this.tiempoJugadoHoras = t; }
    public void setTiempoPromedioPorNivel(double t)           { this.tiempoPromedioPorNivel = t; }
    public void setNivelesDesbloqueados(int n)                { this.nivelesDesbloqueados = n; }
    public void setUltimaSesion(long t)                       { this.ultimaSesion = t; }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Texture getSheetCabeza()                           { return sheetCabeza; }
    public Texture getSheetTorso()                            { return sheetTorso; }
    public Texture getSheetAccesorio()                        { return sheetAccesorio; }
    public int getX()                                         { return x; }
    public int getY()                                         { return y; }
    public String getUserName()                               { return userName; }
    public String getPassword()                               { return password; }
    public int getPuntos()                                    { return puntos; }
    public String getNombreCompleto()                         { return nombreCompleto; }
    public String getRutaAvatar()                             { return rutaAvatar; }
    public long getFechaRegistro()                            { return fechaRegistro; }
    public long getUltimaSesion()                             { return ultimaSesion; }
    public double getVolumen()                                { return volumen; }
    public String getIdioma()                                 { return idioma; }
    public ArrayList<String> getAmigos()                      { return amigos; }
    public ArrayList<EntradaHistorial> getHistorial()         { return historial; }
    public int getPartidasJugadas()                           { return partidasJugadas; }
    public int getNivelesCompletados()                        { return nivelesCompletados; }
    public int getMejorPuntaje()                              { return mejorPuntaje; }
    public int getPuntajeGeneral()                            { return puntajeGeneral; }
    public double getTiempoJugadoHoras()                      { return tiempoJugadoHoras; }
    public double getTiempoPromedioPorNivel()                 { return tiempoPromedioPorNivel; }
    public int getNivelesDesbloqueados()                      { return nivelesDesbloqueados; }
    public int getColCabeza()                                 { return colCabeza; }
    public int getFilaCabeza()                                { return filaCabeza; }
    public int getColTorso()                                  { return colTorso; }
    public int getFilaTorso()                                 { return filaTorso; }
    public int getColAccesorio()                              { return colAccesorio; }
    public int getFilaAccesorio()                             { return filaAccesorio; }
    public TextureRegion getPlayerSprite()                    { return playerSprite; }
}