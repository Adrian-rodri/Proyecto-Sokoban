package io.github.some_example_name.game;

import io.github.some_example_name.model.EntradaHistorial;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.game.Gestionable;
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
    private File usersFile= new File("users/users.skb");
    private Player playerLogeado=null;
    
    
    public PlayerManager(){
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
    public boolean existeUsuario(String userName){
        return arrayUsernames.contains(userName);
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
            rPlayer.writeUTF("espanol");// rPlayer.writeUTF("espanol");//idioma
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
        if (!usersFile.exists()) 
            return;
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
   
    //-----------JJ
    
    public void actualizarTrasPartida(int nivel, int movimientos, double tiempoSeg, int puntaje) {
        if (playerLogeado == null) return;

        int  numIntento = playerLogeado.getHistorial() != null ? playerLogeado.getHistorial().size() + 1 : 1;
        long fecha      = System.currentTimeMillis();
        EntradaHistorial entrada = new EntradaHistorial(numIntento, nivel, puntaje, movimientos, tiempoSeg, fecha);

        if (playerLogeado.getHistorial() != null) playerLogeado.getHistorial().add(entrada);
        appendHistorial(entrada);

        playerLogeado.setPartidasJugadas(playerLogeado.getPartidasJugadas() + 1);
        playerLogeado.setNivelesCompletados(playerLogeado.getNivelesCompletados() + 1);
        playerLogeado.setPuntajeGeneral(playerLogeado.getPuntajeGeneral() + puntaje);
        if (puntaje > playerLogeado.getMejorPuntaje()) playerLogeado.setMejorPuntaje(puntaje);

        double nuevasHoras = playerLogeado.getTiempoJugadoHoras() + tiempoSeg / 3600.0;
        playerLogeado.setTiempoJugadoHoras(nuevasHoras);
        int nc = playerLogeado.getNivelesCompletados();
        if (nc > 0) playerLogeado.setTiempoPromedioPorNivel(nuevasHoras * 3600.0 / nc);

        int siguiente = nivel + 1;
        if (siguiente > playerLogeado.getNivelesDesbloqueados())
            playerLogeado.setNivelesDesbloqueados(siguiente);

        guardar();
    }
    
         public void cambiarIdioma(String nuevoIdioma) {
        if (playerLogeado == null) return;
        playerLogeado.setIdioma(nuevoIdioma);
        guardar();
    }
    
        private void appendHistorial(EntradaHistorial e) {
        if (playerLogeado == null) return;
        try (RandomAccessFile rf = new RandomAccessFile("users/" + playerLogeado.getUserName() + "/historial.skb", "rw")) {
            rf.seek(rf.length());
            rf.writeInt(e.getNumIntento());
            rf.writeInt(e.getNivel());
            rf.writeInt(e.getPuntaje());
            rf.writeInt(e.getMovimientos());
            rf.writeDouble(e.getTiempo());
            rf.writeLong(e.getFecha());
        } catch (IOException ex) {
            System.err.println("Error historial: " + ex.getMessage());
        }
    }
    
    public ArrayList<String[]> getRanking() {
        ArrayList<String[]> lista = new ArrayList<>();
        for (String user : arrayUsernames) {
            try (RandomAccessFile rf = new RandomAccessFile("users/" + user + "/stats.skb", "r")) {
                rf.readInt(); rf.readInt(); rf.readInt();
                int puntaje = rf.readInt();
                lista.add(new String[]{ user, String.valueOf(puntaje) });
            } catch (IOException ignored) {}
        }
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                if (Integer.parseInt(lista.get(j)[1]) < Integer.parseInt(lista.get(j + 1)[1])) {
                    String[] tmp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, tmp);
                }
            }
        }
        return lista;
    }
    
    public Player getPlayerLogeado() { 
        return playerLogeado; 
    }

    public String getNombreJugador() {
        if (playerLogeado == null) 
            return "Invitado";
        String n = playerLogeado.getNombreCompleto();
        return (n != null && !n.isEmpty()) ? n : playerLogeado.getUserName();
    }
   

    @Override public int getCantidad(){ 
        return arrayUsernames.size(); 
    }
    
    public void cerrarSesion(){
        playerLogeado = null;
    }

    
}