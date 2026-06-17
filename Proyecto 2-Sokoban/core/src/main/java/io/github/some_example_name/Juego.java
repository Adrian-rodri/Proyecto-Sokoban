package io.github.some_example_name;

import com.badlogic.gdx.Game;
import io.github.some_example_name.game.PlayerManager;

public abstract class Juego extends Game{
    protected boolean inicializado = false;

    @Override
    public void create() {
        inicializarRecursos();
        mostrarPantallaInicial();
        inicializado = true;
    }
    
    protected abstract void inicializarRecursos();
    
    protected abstract void mostrarPantallaInicial();

    protected abstract void guardarEstadoAntesDeSalir();

    public boolean estaInicializado() {
        return inicializado;
    }

    @Override
    public void pause() {
        super.pause();
        guardarEstadoAntesDeSalir();
    }

    @Override
    public void dispose() {
        guardarEstadoAntesDeSalir();
        super.dispose();
    }
}