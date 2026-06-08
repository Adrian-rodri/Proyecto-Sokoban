package io.github.some_example_name;

public final class Nivel {
    private char[][] level;
    private final char[][] copiaLevel;
    private final int numNivel;
    private int cantidadCajas;
    private final String name;
    
    public Nivel(char[][] level, String name,int numNivel){
        this.level=level;
        this.numNivel=numNivel;
        this.name=name;        
        copiaLevel=new char[level.length][];
        for(int fila=0;fila<level.length;fila++){
            copiaLevel[fila]= new char[level[fila].length];
            for(int col=0;col<level[fila].length;col++)
                copiaLevel[fila][col]=level[fila][col];
        }
        calcularCajas();
    }
    
    public final boolean nivelCompletado(){
        for(char[] fila :level){
            for(char col:fila){
                if(col=='b') 
                    return false;
            }
        }
        return true;
    }
    public final void calcularCajas(){
        for(char[] fila:copiaLevel){
            for(char col: fila){
                if(col=='b' || col=='B')
                    cantidadCajas+=1;
            }
        }
    }
    public char[][] copiarLevel(char[][] nivel){
        char[][] copia=new char[nivel.length][];
        for(int i=0; i<nivel.length;i++){
            copia[i] = new char[nivel[i].length];
            for(int j=0;j<nivel[i].length;j++)
                copia[i][j]=nivel[i][j];
        }
        return copia;
    }
    public void setLevel(char[][] nuevoLevel) {
        this.level = nuevoLevel;
    }
    
    public final void reiniciar(){
       for(int fila=0;fila<copiaLevel.length;fila++){
            for(int col=0;col<copiaLevel[fila].length;col++)
                level[fila][col]=copiaLevel[fila][col];
        }
    }

    public char[][] getLevel() {
        return level;
    }

    public int getNumNivel() {
        return numNivel;
    }

    public int getCantidadCajas() {
        return cantidadCajas;
    }
    public String getName(){
        return name;
    }
    
    
}


