package io.github.some_example_name;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author adria
 */
public class PlayerManager implements Gestionable<Player>{
    private ArrayList<String> arrayUsernames= new ArrayList<>();
    private File usersFile= new File("users.skb");
    PlayerManager(){
        cargar();
    }
    
    public boolean registrarUser(String userName, String password, String nombreCompleto){
        if(arrayUsernames.contains(userName))
            return false;
        try(RandomAccessFile rUsers= new RandomAccessFile(usersFile,"rw")){
            rUsers.seek(rUsers.length());
            rUsers.writeUTF(userName);
            rUsers.writeUTF(password);
            rUsers.writeInt(0); //puntos
            crearArchivosNuevos(userName,nombreCompleto);
            arrayUsernames.add(userName);
            return true;
        }catch (IOException e){
            System.err.println("Error: "+ e.getMessage());
        }
        return false;
    }
    private void crearArchivosNuevos(String userName,String nombreCompleto){
        try{
            File playerFiles= new File("users/"+userName);
            playerFiles.mkdirs();
            
            playerFiles= new File("users/"+userName+"/amigos.skb");
            playerFiles.createNewFile();
            
            
            RandomAccessFile rPlayer= new RandomAccessFile("users/"+userName+"/perfil.skb","rw");//perfil.skb
            rPlayer.writeUTF(nombreCompleto);
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Fecha de inicio de sesion
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Ultima fecha de sesion
            rPlayer.writeDouble(0);//volumen
            rPlayer.writeUTF("espanol");//idioma
            rPlayer.writeUTF("defaul/avatar.png");//avatar
            rPlayer.close();
            
            rPlayer= new RandomAccessFile("users/"+userName+"/stats.skb","rw");//stats.skb
            rPlayer.writeInt(0);//partidasJugadas
            rPlayer.writeInt(0);//nivelesCompletados
            rPlayer.writeInt(0);//mejorPuntaje
            rPlayer.writeInt(0);//puntajeGeneral
            rPlayer.writeDouble(0);//tiempoJugadoHoras
            rPlayer.writeDouble(0);//tiempoPromedioPorNivel
            rPlayer.close();
            
            rPlayer= new RandomAccessFile("users/"+userName+"/progreso.skb","rw");//progreso.skb
            rPlayer.writeInt(0);//nivelesDesbloqueados
            rPlayer.close();
            
            rPlayer= new RandomAccessFile("users/"+userName+"/avatar.skb","rw");//avatar.skb
            rPlayer.writeInt(0);//colCabeza
            rPlayer.writeInt(0);//filaCabeza
            rPlayer.writeInt(0);//colTorso
            rPlayer.writeInt(0);//filaTorso
            rPlayer.writeInt(0);//colAccesorio
            rPlayer.writeInt(0);//filaAccesorio
            rPlayer.close();
            
            
        } catch (IOException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }

    @Override
    public Player getActual() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cargar() {
        try(RandomAccessFile rUsers= new RandomAccessFile(usersFile,"rw")){
            while(rUsers.getFilePointer()<rUsers.length()){
                arrayUsernames.add(rUsers.readUTF());
                rUsers.readUTF();
                rUsers.readInt();
            }
        }catch(IOException e){
            System.err.println("Error: "+ e.getMessage());
        }
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getCantidad() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
