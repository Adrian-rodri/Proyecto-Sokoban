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
    private Player playerLogeado=null;
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
    public boolean logIn(String userName, String password){
        if(arrayUsernames.contains(userName)){
            try(RandomAccessFile rPlayer= new RandomAccessFile(usersFile,"r")){
                while(rPlayer.getFilePointer()<rPlayer.length()){
                    String user=rPlayer.readUTF();
                    if(user.equals(userName)){
                        String passCorrecta= rPlayer.readUTF();
                        if(!password.equals(passCorrecta))
                            return false;
                        int puntos= rPlayer.readInt();
                        cargarUser(userName, password, puntos);
                        return true;
                    }else {
                        rPlayer.readUTF();
                        rPlayer.readInt();
                    }
                    
                }
            }catch (IOException e){
                System.out.println("Error: "+e.getMessage());
            }
        }
        return false;    
    }
    private void cargarUser(String userName, String password, int puntos){
        try{
            RandomAccessFile rUser= new RandomAccessFile("users/"+userName+"/perfil.skb","r");
            //leer perfil.skb 
            String nombreCompleto= rUser.readUTF();
            long fechaRegistro= rUser.readLong();
            long ultimaSesion= rUser.readLong();
            double volumen= rUser.readDouble();
            String idioma= rUser.readUTF();
            String rutaAvatar= rUser.readUTF();
            rUser.close();
            
            //leer stats.skb
            rUser= new RandomAccessFile("users/"+userName+"/stats.skb","r");
            int partidasJugadas= rUser.readInt();
            int nivelesCompletados= rUser.readInt();
            int mejorPuntaje= rUser.readInt();
            int puntajeGeneral= rUser.readInt();
            double tiempoJugadoHoras= rUser.readDouble();
            double tiempoPromedioPorNivel= rUser.readDouble();
            rUser.close();
            
            //leer progreso.skb
            rUser= new RandomAccessFile("users/"+userName+"/progreso.skb","r");
            int nivelesDesbloqueados= rUser.readInt();
            rUser.close();
            
            //leer avatar.skb
            rUser= new RandomAccessFile("users/"+userName+"/avatar.skb","r");
            int colCabeza= rUser.readInt();
            int filaCabeza= rUser.readInt();
            int colTorso= rUser.readInt();
            int filaTorso= rUser.readInt();
            int colAccesorio= rUser.readInt();
            int filaAccesorio= rUser.readInt();
            rUser.close();
            
            //leer amigos.skb
            rUser= new RandomAccessFile("users/"+userName+"/amigos.skb","r");
            ArrayList<String> amigos= new ArrayList<>();
            while(rUser.getFilePointer()<rUser.length()){
                amigos.add(rUser.readUTF());
            }
            rUser.close();
            
            //leer historial.skb
            rUser= new RandomAccessFile("users/"+userName+"/historial.skb","r");
            ArrayList<EntradaHistorial> historial= new ArrayList<>();
            while(rUser.getFilePointer()<rUser.length()){
                int numIntento= rUser.readInt();
                int nivel= rUser.readInt();
                int puntaje= rUser.readInt();
                int movimientos= rUser.readInt();
                double tiempo= rUser.readDouble();
                long fecha= rUser.readLong();
                EntradaHistorial entrada= new EntradaHistorial(numIntento,nivel,puntaje,movimientos,tiempo,fecha);
                historial.add(entrada);
            }
            rUser.close();
            
            playerLogeado= new Player(userName,password,puntos,nombreCompleto,rutaAvatar,fechaRegistro,
                                        ultimaSesion,volumen,idioma,amigos,partidasJugadas,nivelesCompletados,
                                        mejorPuntaje,puntajeGeneral,tiempoJugadoHoras,tiempoPromedioPorNivel,
                                        nivelesDesbloqueados,colCabeza,filaCabeza,colTorso,filaTorso,
                                        colAccesorio,filaAccesorio,historial);
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    private void crearArchivosNuevos(String userName,String nombreCompleto){
        try{
            File playerFiles= new File("users/"+userName);
            playerFiles.mkdirs();
            
            playerFiles= new File("users/"+userName+"/amigos.skb");
            playerFiles.createNewFile();
            
            playerFiles= new File("users/"+userName+"/historial.skb");
            playerFiles.createNewFile();
            
            RandomAccessFile rPlayer= new RandomAccessFile("users/"+userName+"/perfil.skb","rw");//perfil.skb
            rPlayer.writeUTF(nombreCompleto);
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Fecha de inicio de sesion
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Ultima fecha de sesion
            rPlayer.writeDouble(0);//volumen
            rPlayer.writeUTF("espanol");//idioma
            rPlayer.writeUTF("default/avatar.png");//avatar
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
            
            
        } catch (IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }

    @Override
    public Player getActual() {
        return playerLogeado;
    }

    @Override
    public void cargar() {
        try(RandomAccessFile rUsers= new RandomAccessFile(usersFile,"r")){
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
    public synchronized  void guardar() {
        if(playerLogeado==null)
            return;
        try{
            //guardar los puntos
            RandomAccessFile rUser= new RandomAccessFile(usersFile,"rw");
            while(rUser.getFilePointer()<rUser.length()){
                String user= rUser.readUTF();
                if(user.equals(playerLogeado.getUserName())){
                    rUser.readUTF();
                    rUser.writeInt(playerLogeado.getPuntos());
                    break;
                }else{
                    rUser.readUTF();
                    rUser.readInt();
                }
                    
            }
            rUser.close();
            //guardar perfil.skb;
            rUser= new RandomAccessFile("users/"+playerLogeado.getUserName()+"/perfil.skb","rw");
            rUser.setLength(0);
            rUser.writeUTF(playerLogeado.getNombreCompleto());
            rUser.writeLong(playerLogeado.getFechaRegistro());
            rUser.writeLong(playerLogeado.getUltimaSesion());
            rUser.writeDouble(playerLogeado.getVolumen());
            rUser.writeUTF(playerLogeado.getIdioma());
            rUser.writeUTF(playerLogeado.getRutaAvatar());
            rUser.close();
            
            //guardar stats.skb
            rUser= new RandomAccessFile("users/"+playerLogeado.getUserName()+"/stats.skb","rw");
            rUser.setLength(0);
            rUser.writeInt(playerLogeado.getPartidasJugadas());//partidasJugadas
            rUser.writeInt(playerLogeado.getNivelesCompletados());//nivelesCompletados
            rUser.writeInt(playerLogeado.getMejorPuntaje());//mejorPuntaje
            rUser.writeInt(playerLogeado.getPuntajeGeneral());//puntajeGeneral
            rUser.writeDouble(playerLogeado.getTiempoJugadoHoras());//tiempoJugadoHoras
            rUser.writeDouble(playerLogeado.getTiempoPromedioPorNivel());//tiempoPromedioPorNivel
            rUser.close();
            
            //guardar progreso.skb
            rUser= new RandomAccessFile("users/"+playerLogeado.getUserName()+"/progreso.skb","rw");
            rUser.writeInt(playerLogeado.getNivelesDesbloqueados());
            rUser.close();
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    

    @Override
    public int getCantidad() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
