package io.github.some_example_name.model;

public class EntradaHistorial {
    private final int numIntento,nivel,puntaje,movimientos;
    private final double tiempo;
    private final long fecha;

    public EntradaHistorial(int numIntento, int nivel, int puntaje, int movimientos, double tiempo, long fecha) {
        this.numIntento = numIntento;
        this.nivel = nivel;
        this.puntaje = puntaje;
        this.movimientos = movimientos;
        this.tiempo = tiempo;
        this.fecha = fecha;
    }

    public int getNumIntento() {
        return numIntento;
    }

    public int getNivel() {
        return nivel;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public double getTiempo() {
        return tiempo;
    }

    public long getFecha() {
        return fecha;
    }
    
}
