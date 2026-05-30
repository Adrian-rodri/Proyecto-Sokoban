package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class NivelManager implements Gestionable<Nivel>{
    ArrayList<Nivel> arrayLevels= new ArrayList<>();
    RandomAccessFile indexFile;
    
    
    public int getIndex() throws IOException{
        boolean esNuevo=!Gdx.files.local("index.skb").exists();
        String ruta= Gdx.files.local("index.skb").file().getAbsolutePath();
        indexFile= new RandomAccessFile(ruta,"rw");
        if(esNuevo || indexFile.length()==0){
            indexFile.seek(0);
            indexFile.writeInt(0);
            return 0;
        }else{
            indexFile.seek(0);
            int indice= indexFile.readInt();
            indexFile.seek(0);
            indexFile.writeInt(indice+1);
            return indice;
        }
        
    }
    @Override
    public Nivel getActual() {
        return new Nivel(null,1);
    }

    @Override
    public void cargar() {
        
    }

    @Override
    public void guardar() {
        
    }

    @Override
    public int getCantidad() {
        return 1;
    }
    
}
