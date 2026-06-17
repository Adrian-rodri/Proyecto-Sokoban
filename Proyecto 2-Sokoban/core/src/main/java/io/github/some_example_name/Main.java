package io.github.some_example_name;

import io.github.some_example_name.game.NivelManager;
import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.game.PlayerManager;
import com.badlogic.gdx.Game;

public class Main extends Game {
    public PlayerManager playerManager;
    public NivelManager nivelManager;
    public String idiomaGlobal= "espanol";
    @Override
    public void create() {
        playerManager = new PlayerManager();
        nivelManager = new NivelManager();
        
        nivelManager.cargar();
        setScreen(new LoginScreen(this));
    }
}
