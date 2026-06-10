package io.github.some_example_name.game;

public interface Gestionable<COMODIN> {
    public COMODIN getActual();
    public void cargar();
    public void guardar();
    public int getCantidad();
}
