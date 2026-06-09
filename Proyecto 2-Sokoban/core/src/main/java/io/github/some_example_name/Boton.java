package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Boton {

    private String texto;
    public float x, y, ancho, alto;

    // Colores (RGBA)
    private float[] rFondo = { 0.18f, 0.18f, 0.30f, 1f };
    private float[] rBorde = { 0.50f, 0.50f, 0.75f, 1f };
    private float[] rHover = { 0.28f, 0.28f, 0.50f, 1f };

    // ── Constructor ───────────────────────────────────────────────────────────
    public Boton(String texto, float x, float y, float ancho, float alto) {
        this.texto = texto;
        this.x = x;   this.y = y;
        this.ancho = ancho; this.alto = alto;
    }

    // ── Texto ─────────────────────────────────────────────────────────────────
    public void setTexto(String t) { this.texto = t; }
    public String getTexto()       { return texto; }

    // ── Deteccion de mouse ────────────────────────────────────────────────────
    /** Coordenada Y en espacio LibGDX (origen abajo-izquierda). */
    private float mouseY() { return Gdx.graphics.getHeight() - Gdx.input.getY(); }
    private float mouseX() { return Gdx.input.getX(); }

    public boolean encima() {
        float mx = mouseX(), my = mouseY();
        return mx >= x && mx <= x + ancho && my >= y && my <= y + alto;
    }

    /** Verdadero solo el frame en que el usuario hizo clic sobre el boton. */
    public boolean clicado() {
        return encima() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    // ── Dibujo ────────────────────────────────────────────────────────────────
    /**
     * Dibuja el boton completo (fondo + borde + texto).
     * Requiere que NO haya un SpriteBatch o ShapeRenderer abierto al llamar.
     */
    public void dibujar(SpriteBatch batch, BitmapFont font, ShapeRenderer shape) {
        boolean h = encima();

        // Fondo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (h) shape.setColor(rHover[0], rHover[1], rHover[2], rHover[3]);
        else   shape.setColor(rFondo[0], rFondo[1], rFondo[2], rFondo[3]);
        shape.rect(x, y, ancho, alto);
        shape.end();

        // Borde
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(rBorde[0], rBorde[1], rBorde[2], rBorde[3]);
        shape.rect(x, y, ancho, alto);
        shape.end();

        // Texto centrado horizontalmente, a ~68 % del alto del boton
        batch.begin();
        font.setColor(h ? Color.YELLOW : Color.LIGHT_GRAY);
        GlyphLayout gl = new GlyphLayout(font, texto);
        font.draw(batch, texto,
                x + ancho / 2f - gl.width / 2f,
                y + alto * 0.68f + gl.height / 2f);
        batch.end();
    }

    // ── Colores personalizables ───────────────────────────────────────────────
    public void setColorFondo(float r, float g, float b, float a) { rFondo = new float[]{r,g,b,a}; }
    public void setColorBorde(float r, float g, float b, float a) { rBorde = new float[]{r,g,b,a}; }
    public void setColorHover(float r, float g, float b, float a) { rHover = new float[]{r,g,b,a}; }
}
