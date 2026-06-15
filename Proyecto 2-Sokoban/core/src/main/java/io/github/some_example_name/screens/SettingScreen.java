package io.github.some_example_name.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import io.github.some_example_name.Main;

/**
 *
 * @author adria
 */
public class SettingScreen extends BaseScreen{

    public SettingScreen(Main game) {
        super(game);
    }

    @Override
    protected void buildUI() {
        Window panel = new Window("", skin);
    }
    
    
}
