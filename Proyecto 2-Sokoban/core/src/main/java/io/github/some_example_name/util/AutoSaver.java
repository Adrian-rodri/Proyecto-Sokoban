package io.github.some_example_name.util;

import io.github.some_example_name.game.PlayerManager;

public final class AutoSaver {

    public static final long INTERVALO_DEFAULT_MS = 30_000L;
    private volatile boolean activo;
    private Thread hiloAutoSave;
    private final PlayerManager playerManager;
    private final long intervaloMs;
    public AutoSaver(PlayerManager playerManager) {
        this(playerManager, INTERVALO_DEFAULT_MS);
    }

    public AutoSaver(PlayerManager playerManager, long intervaloMs) {
        this.playerManager = playerManager;
        this.intervaloMs = intervaloMs;
        this.activo = false;
    }

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
                        Thread.sleep(intervaloMs);
                        if (activo) {
                            playerManager.guardar();
                        }
                    } catch (InterruptedException e) {
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

    public void stop() {
        activo = false;
        if (hiloAutoSave != null) {
            hiloAutoSave.interrupt();
            hiloAutoSave = null;
        }
    }

    public boolean isActivo() {
        return activo;
    }
}
