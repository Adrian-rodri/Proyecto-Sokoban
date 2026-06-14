package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.Player;

public abstract class BaseScreen implements Screen {
    protected final Main game;
    protected Stage stage;
    protected Skin skin;

    public BaseScreen(Main game) {
        this.game = game;
    }
    
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skin/ui/sgx-ui.atlas"));
        skin = new Skin(Gdx.files.internal("ui/skin/ui/sgx-ui.json"), atlas);

        Label.LabelStyle estiloError = new Label.LabelStyle(skin.getFont("small"),new Color(1f, 0.37f, 0.37f, 1f));
        skin.add("error", estiloError, Label.LabelStyle.class);

        buildUI();
    }

    protected abstract void buildUI();

    protected Window createWindow() {
        Window w = new Window("", skin);
        w.setMovable(false);
        w.pad(28f, 32f, 24f, 32f);
        return w;
    }

    protected void setRoot(Table content) {
        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(content);
        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    protected String traducir(String es, String en) {
        String lang = null;
        if (game.playerManager != null) {
            Player p = game.playerManager.getPlayerLogeado();
            if (p != null) lang = p.getIdioma();
        }
        return "en".equals(lang) ? en : es;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.12f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
