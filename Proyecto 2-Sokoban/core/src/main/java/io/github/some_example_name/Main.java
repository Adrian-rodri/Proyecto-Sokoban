package io.github.some_example_name;

import io.github.some_example_name.game.NivelManager;
import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.game.PlayerManager;
import io.github.some_example_name.util.AutoSaver;

public class Main extends Juego {
    public PlayerManager playerManager;
    public NivelManager nivelManager;
    public AutoSaver autoSaver;
    public String idiomaGlobal= "espanol";
    
    @Override
    public void create() {
        playerManager = new PlayerManager();
        nivelManager = new NivelManager();
        nivelManager.cargar();
        autoSaver = new AutoSaver(playerManager);
        autoSaver.start();

        setScreen(new LoginScreen(this));
    }
    @Override
    public void dispose() {

        if (autoSaver != null) {
            autoSaver.stop();
        }
        super.dispose();
    }
}
