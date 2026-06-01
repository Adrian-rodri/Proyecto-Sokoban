package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NivelManager implements Gestionable<Nivel>{
    public final ArrayList<Nivel> arrayLevels= new ArrayList<>();
    public final ArrayList<String> arrayLevelsNames= new ArrayList<>();
    
    
    private void cargarLevels(){ //cargar los niveles de los .txt de la carpeta /assets/levels/ 
        String ruta= Gdx.files.internal("levels").file().getAbsolutePath();
        File folderNiveles= new File(ruta);
        File[] niveles= folderNiveles.listFiles();
        
        Arrays.sort(niveles,(a,b)-> a.getName().compareTo(b.getName()));
        int numLevel=0;
        
        for(File level: niveles){
            boolean esNivel= level.getName().startsWith("nivel") && level.getName().endsWith(".txt");
            //System.out.println(level.getName());
            
            if(!arrayLevelsNames.contains(level.getName()) && !level.getName().equals("index.skb") && esNivel){
                String contenido= Gdx.files.internal("levels/"+level.getName()).readString();
                String[] filas= contenido.split("\n");
                char[][] map= new char[filas.length][];
                
                for(int i=0; i<filas.length;i++){
                    map[i]= filas[i].trim().toCharArray();
                }
                
                Nivel newLevel= new Nivel(map,level.getName(),numLevel);
                arrayLevels.add(newLevel);
                arrayLevelsNames.add(level.getName());
                numLevel++;
            }
        }
    }
    public Nivel getNivel(int numLevel){
       return arrayLevels.get(numLevel);
    }
    @Override
    public Nivel getActual() {
        return new Nivel(null,"",1);
    }

    @Override
    public void cargar() { 
        cargarLevels();
    }

    @Override
    public void guardar() {//metodo para guardar en archivos binarios los niveles que carga el metodo cargarLevels() aun no se implementa
        
    }

    @Override
    public int getCantidad() {
        return arrayLevels.size();
    }
    
}
