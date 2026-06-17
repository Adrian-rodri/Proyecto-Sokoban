package io.github.some_example_name.util;

import io.github.some_example_name.game.PlayerManager;

/**
 * Auto-guardado periodico del progreso del jugador usando un hilo daemon.
 * <p>
 * Sigue el mismo patron de hilo que el timer de GameScreen:
 * <ul>
 *   <li>Flag volatile para control de estado</li>
 *   <li>Thread con Runnable y bucle while(activo)</li>
 *   <li>Daemon thread (no impide el cierre de la JVM)</li>
 *   <li>Interrupt para limpieza al detener</li>
 * </ul>
 * </p>
 * 
 * <p>
 * El auto-guardado invoca {@link PlayerManager#guardar()} cada cierto intervalo.
 * {@code PlayerManager.guardar()} ya es synchronized, por lo que es seguro
 * llamarlo desde este hilo mientras el hilo principal de LibGDX tambien
 * pueda invocarlo.
 * </p>
 * 
 * @author equipo
 */
public final class AutoSaver {

    /** Intervalo por defecto entre auto-guardados: 30 segundos. */
    public static final long INTERVALO_DEFAULT_MS = 30_000L;

    private volatile boolean activo;
    private Thread hiloAutoSave;
    private final PlayerManager playerManager;
    private final long intervaloMs;

    // ──────────────────────── Constructor/es ────────────────────────

    /**
     * Crea un AutoSaver con el intervalo por defecto (30 s).
     *
     * @param playerManager gestor de jugadores a cuyo {@code guardar()} se llamara
     */
    public AutoSaver(PlayerManager playerManager) {
        this(playerManager, INTERVALO_DEFAULT_MS);
    }

    /**
     * Crea un AutoSaver con un intervalo personalizado.
     *
     * @param playerManager gestor de jugadores
     * @param intervaloMs   milisegundos entre cada auto-guardado
     */
    public AutoSaver(PlayerManager playerManager, long intervaloMs) {
        this.playerManager = playerManager;
        this.intervaloMs = intervaloMs;
        this.activo = false;
    }

    // ──────────────────────── Metodos publicos ────────────────────────

    /**
     * Inicia el hilo de auto-guardado. Si ya esta corriendo, no hace nada.
     * El hilo se crea como daemon y se nombra para facilitar el debugging.
     */
    public void start() {
        if (activo) {
            return; // ya esta en ejecucion
        }
        activo = true;
        hiloAutoSave = new Thread(new Runnable() {
            @Override
            public void run() {
                while (activo) {
                    try {
                        // esperar el intervalo antes del siguiente guardado
                        Thread.sleep(intervaloMs);
                        if (activo) {
                            // guardar() internamente chequea si hay un jugador logueado
                            playerManager.guardar();
                        }
                    } catch (InterruptedException e) {
                        // el hilo fue interrumpido intencionalmente (stop o cierre)
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        hiloAutoSave.setDaemon(true);
        hiloAutoSave.setName("AutoSave-Thread");
        hiloAutoSave.start();
    }

    /**
     * Detiene el hilo de auto-guardado. Es seguro llamarlo varias veces.
     * Marca la flag {@code activo = false} e interrumpe el hilo para
     * que salga del {@code Thread.sleep()} inmediatamente.
     */
    public void stop() {
        activo = false;
        if (hiloAutoSave != null) {
            hiloAutoSave.interrupt();
            hiloAutoSave = null;
        }
    }

    /**
     * Indica si el auto-guardado esta actualmente en ejecucion.
     *
     * @return true si el hilo esta activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }
}
