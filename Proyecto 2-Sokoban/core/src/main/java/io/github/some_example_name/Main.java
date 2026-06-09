package io.github.some_example_name;

import com.badlogic.gdx.Game;

public class Main extends Game {
    public PlayerManager playerManager;
    public NivelManager nivelManager;

    @Override
    public void create() {
        playerManager = new PlayerManager();
        nivelManager  = new NivelManager();
        nivelManager.cargar();
        setScreen(new LoginScreen(this));
    }
}
