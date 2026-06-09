package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Arrays;

public class CampoTexto {
    private String texto = "";
    private final String etiqueta;
    private final boolean esClave;
    public boolean activo = false;
    private boolean verClave = false;
    public final float x, y, ancho, alto;

    public CampoTexto(String etiqueta, float x, float y, float ancho, float alto, boolean esClave) {
        this.etiqueta = etiqueta;
        this.x = x; this.y = y;
        this.ancho = ancho; this.alto = alto;
        this.esClave = esClave;
    }

    public void escribir(char c) {
        if (activo && texto.length() < 40 && c >= 32) texto += c;
    }

    public void borrar() {
        if (activo && !texto.isEmpty())
            texto = texto.substring(0, texto.length() - 1);
    }

    public boolean toca(float px, float py) {
        return px >= x && px <= x + ancho && py >= y && py <= y + alto;
    }

    public void toggleVerClave() {
        if (esClave) verClave = !verClave;
    }

    private String maskear(int len) {
        char[] c = new char[len];
        Arrays.fill(c, '*');
        return new String(c);
    }

    public void dibujar(SpriteBatch batch, BitmapFont font, ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(activo ? 0.22f : 0.14f, activo ? 0.22f : 0.14f, activo ? 0.38f : 0.22f, 1f);
        shape.rect(x, y, ancho, alto);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(activo ? 0.55f : 0.32f, activo ? 0.55f : 0.32f, activo ? 0.9f : 0.5f, 1f);
        shape.rect(x, y, ancho, alto);
        shape.end();

        String visible = (esClave && !verClave) ? maskear(texto.length()) : texto;
        batch.begin();
        font.setColor(0.65f, 0.65f, 0.85f, 1f);
        font.draw(batch, etiqueta, x, y + alto + 18);
        font.setColor(Color.WHITE);
        font.draw(batch, visible + (activo ? "|" : ""), x + 8, y + alto * 0.67f);
        batch.end();
    }

    public String getTexto() { return texto; }
    public void limpiar()    { texto = ""; }
}
