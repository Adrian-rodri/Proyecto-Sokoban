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
            
            String avatarRuta;
            try {
                avatarRuta = rUser.readUTF();
            } catch (Exception e) {
                avatarRuta = "1-default.png";
            }
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
                                        nivelesDesbloqueados,avatarRuta,historial);
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
            
            playerFiles= new File("users/"+userName+"/solicitudes.skb");
            playerFiles.createNewFile();
            
            RandomAccessFile rPlayer= new RandomAccessFile("users/"+userName+"/perfil.skb","rw");//perfil.skb
            rPlayer.writeUTF(nombreCompleto);
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Fecha de inicio de sesion
            rPlayer.writeLong(Calendar.getInstance().getTimeInMillis());//Ultima fecha de sesion
            rPlayer.writeDouble(0.4);//volumen
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
            rPlayer.setLength(0);
            rPlayer.writeUTF("1-default.png");
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
    public synchronized void guardar() {
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
            
            //guardar avatar.skb
            rUser= new RandomAccessFile("users/"+playerLogeado.getUserName()+"/avatar.skb","rw");
            rUser.setLength(0);
            rUser.writeUTF(playerLogeado.getAvatarFile() != null ? playerLogeado.getAvatarFile() : "1-default.png");
            rUser.close();
            
            guardarAmigos();
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public boolean enviarSolicitud(String receptor){
        if(!existeUsuario(receptor))
            return false;
        if(receptor.equals(playerLogeado.getUserName()))
            return false;
        if(playerLogeado.getAmigos().contains(receptor))
            return false;
        if(solicitudYaEnviada(receptor))
            return false;
        
        ArrayList<String> misSolis= getSolicitudes();
        for(String soli: misSolis){
            if(soli.equals(receptor)){
                aceptarSolicitud(receptor);
                return true;
            }
        }
        try(RandomAccessFile rSoli= new RandomAccessFile("users/"+receptor+ "/solicitudes.skb","rw")){
            rSoli.seek(rSoli.length());
            rSoli.writeUTF(playerLogeado.getUserName());
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
        return true;
    }
    public ArrayList<String> getSolicitudes() {
        ArrayList<String> solicitudes = new ArrayList<>();
        try {
            File file = new File("users/"+playerLogeado.getUserName() + "/solicitudes.skb");
            if (!file.exists()) 
                return solicitudes;
            RandomAccessFile rSoli= new RandomAccessFile(file, "r");
            while(rSoli.getFilePointer() < rSoli.length()){
                solicitudes.add(rSoli.readUTF());
            }
            rSoli.close();
        } catch (IOException e) {
            System.err.println("Error: "+e.getMessage());
        }
        return solicitudes;
    }
    
    public int getCantSolicitudes() {
        return getSolicitudes().size();
    }
    
    public void aceptarSolicitud(String nuevoAmigo) {
        if(!existeUsuario(nuevoAmigo))
            return;
        try(RandomAccessFile rAmigos= new RandomAccessFile("users/"+playerLogeado.getUserName()+"/amigos.skb","rw")){
            rAmigos.seek(rAmigos.length());
            rAmigos.writeUTF(nuevoAmigo);
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
        try(RandomAccessFile rAmigos= new RandomAccessFile("users/"+nuevoAmigo+"/amigos.skb","rw")){
            rAmigos.seek(rAmigos.length());
            rAmigos.writeUTF(playerLogeado.getUserName());
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
        playerLogeado.getAmigos().add(nuevoAmigo);
        removerSolicitud(playerLogeado.getUserName(),nuevoAmigo);
    }
    public void rechazarSolicitud(String emisor) {
        removerSolicitud(playerLogeado.getUserName(),emisor);
    }
    public void removerSolicitud(String userA, String userB){
        try(RandomAccessFile rSoli= new RandomAccessFile("users/"+userA+"/solicitudes.skb","rw")){
            ArrayList<String> arraySolis= new ArrayList<>();
            while(rSoli.getFilePointer()<rSoli.length()){
                String solicitud=rSoli.readUTF();
                if(!solicitud.equals(userB))
                    arraySolis.add(solicitud);
            }
            rSoli.setLength(0);
            for(String soli: arraySolis){
                rSoli.writeUTF(soli);
            }
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public boolean solicitudYaEnviada(String receptor){
        if(!existeUsuario(receptor))
            return false;
        try(RandomAccessFile rSoli= new RandomAccessFile("users/"+receptor+"/solicitudes.skb","r")){
            if(rSoli.length()==0)
                return false;
            while(rSoli.getFilePointer()<rSoli.length()){
                String soli= rSoli.readUTF();
                if(soli.equals(playerLogeado.getUserName()))
                    return true;
            }
        }catch (IOException e) {
            System.err.println("Error: "+e.getMessage());
        }
        return false;
    }
    
    public boolean eliminarAmigo(String feka){
        if(!existeUsuario(feka))
            return false;
        //borrar feka de logeado
        try(RandomAccessFile rAmigos= new RandomAccessFile("users/"+playerLogeado.getUserName() + "/amigos.skb","rw")){
            ArrayList<String> arrayAmigos= new ArrayList<>();
            while(rAmigos.getFilePointer()<rAmigos.length()){
                String amigo= rAmigos.readUTF();
                if(!amigo.equals(feka)){
                    arrayAmigos.add(amigo);
                }
            }
            rAmigos.setLength(0);
            for(String amigo:arrayAmigos){
                rAmigos.writeUTF(amigo);
            }
            playerLogeado.getAmigos().remove(feka);
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
        //Borrar logeado del otro
        try(RandomAccessFile rAmigos= new RandomAccessFile("users/" + feka+ "/amigos.skb","rw")){
            ArrayList<String> arrayAmigos= new ArrayList<>();
            while(rAmigos.getFilePointer()<rAmigos.length()){
                String amigo= rAmigos.readUTF();
                if(!amigo.equals(playerLogeado.getUserName())){
                    arrayAmigos.add(amigo);
                }
            }
            rAmigos.setLength(0);
            for(String amigo:arrayAmigos){
                rAmigos.writeUTF(amigo);
            }
        }catch(IOException e){
            System.out.println("Error: "+e.getMessage());
        }
        return true;
    }
   
    //-----------JJ
    
    public void actualizarTrasPartida(int nivel, int movimientos, double tiempoSeg, int puntaje) {
        if(playerLogeado==null)
            return;
        boolean yaCompletado=false;
        int mejorPuntajeAnterior=0;
        for(EntradaHistorial entrada: playerLogeado.getHistorial()){
            if(entrada.getNivel()==nivel){
                yaCompletado=true;
                if(entrada.getPuntaje()>mejorPuntajeAnterior){
                    mejorPuntajeAnterior= entrada.getPuntaje();
                }
            }
        }
        playerLogeado.agregarPartidaJugada();
        if(!yaCompletado)
            playerLogeado.agregarNivelCompletado();
        
        if (puntaje > mejorPuntajeAnterior) 
            playerLogeado.sumarPuntajeGeneral(puntaje - mejorPuntajeAnterior);
        
        playerLogeado.compararMjeorPuntaje(puntaje);
        double nuevashoras= playerLogeado.getTiempoJugadoHoras()+(tiempoSeg/3600.0);
        playerLogeado.setTiempoJugadoHoras(nuevashoras);
        
        int totalPartidas= playerLogeado.getPartidasJugadas();
        double promActual= playerLogeado.getTiempoPromedioPorNivel();
        double nuevoPromedio= ((promActual *(totalPartidas-1))+tiempoSeg)/totalPartidas;
        playerLogeado.setTiempoPromedioPorNivel(nuevoPromedio);
        
        int  numIntento= playerLogeado.getHistorial() != null ? playerLogeado.getHistorial().size() + 1 : 1;
        long fecha= System.currentTimeMillis();
        EntradaHistorial entrada= new EntradaHistorial(numIntento, nivel, puntaje, movimientos, tiempoSeg, fecha);
        
        if (playerLogeado.getHistorial()!= null) 
            playerLogeado.getHistorial().add(entrada);
        appendHistorial(entrada);
        
        int siguiente = nivel + 1;
        if(siguiente> playerLogeado.getNivelesDesbloqueados())
            playerLogeado.setNivelesDesbloqueados(siguiente);
        guardar();
    }
    public void cambiarAvatar(String avatarFile) {
        if (playerLogeado==null) 
            return;
        playerLogeado.setAvatarFile(avatarFile);
        try(RandomAccessFile rf =new RandomAccessFile("users/" + playerLogeado.getUserName() + "/avatar.skb", "rw")){
            rf.setLength(0);
            rf.writeUTF(avatarFile);
        } catch (IOException e){
            System.err.println("Error: "+ e.getMessage());
        }
    }
    public void cambiarVolumen(int porcentaje) {
        playerLogeado.setVolumen(porcentaje/100.0);
        guardar();
    }

    public void cambiarIdioma(String idioma) {
        playerLogeado.setIdioma(idioma);
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
    
    public boolean agregarAmigo(String userNameAmigo) {
        if (playerLogeado == null) return false;
        if (!arrayUsernames.contains(userNameAmigo)) return false;      
        if (userNameAmigo.equals(playerLogeado.getUserName())) return false; 
        if (playerLogeado.getAmigos().contains(userNameAmigo)) return false; 

        playerLogeado.getAmigos().add(userNameAmigo);
        guardarAmigos();
        return true;
    }
    
    private void guardarAmigos() {
        if (playerLogeado == null) return;
        try (RandomAccessFile rf = new RandomAccessFile(
                "users/" + playerLogeado.getUserName() + "/amigos.skb", "rw")) {
            rf.setLength(0);
            for (String a : playerLogeado.getAmigos()) {
                rf.writeUTF(a);
            }
        } catch (IOException e) {
            System.err.println("Error guardando amigos: " + e.getMessage());
        }
    }
    
    public String[] obtenerStatsAmigo(String userNameAmigo) {
        String[] stats= new String[7];
        try (RandomAccessFile raf= new RandomAccessFile("users/" + userNameAmigo+"/stats.skb", "r")){
            int partidas= raf.readInt();
            int niveles= raf.readInt();
            int mejorPuntaje= raf.readInt();
            int puntajeGeneral= raf.readInt();
            double tiempoHoras= raf.readDouble();
            double tiempoPromedio= raf.readDouble();

            stats[0]= String.valueOf(partidas);
            stats[1]= String.valueOf(niveles);
            stats[2]= String.valueOf(mejorPuntaje);
            stats[3]= String.valueOf(puntajeGeneral);
            stats[4]= String.format("%.2f",tiempoHoras);
            stats[5]= String.format("%.1f", tiempoPromedio);
            stats[6]= userNameAmigo;
        } catch (IOException e){
            for(int i = 0;i<6; i++)
                stats[i] = "0";
            stats[6]= userNameAmigo;
    }
    return stats;
}
        
    public boolean cambiarNombreCompleto(String nuevoNombre) {
        if (playerLogeado==null) 
            return false;
        if (nuevoNombre==null ||nuevoNombre.isEmpty()) 
            return false;
        playerLogeado.setNombreCompleto(nuevoNombre);
        try(RandomAccessFile rf=new RandomAccessFile("users/" + playerLogeado.getUserName() + "/perfil.skb", "rw")){
            rf.setLength(0);
            rf.writeUTF(playerLogeado.getNombreCompleto());
            rf.writeLong(playerLogeado.getFechaRegistro());
            rf.writeLong(playerLogeado.getUltimaSesion());
            rf.writeDouble(playerLogeado.getVolumen());
            rf.writeUTF(playerLogeado.getIdioma());
            rf.writeUTF(playerLogeado.getRutaAvatar());
        }catch (IOException e){
            System.err.println("Error: "+ e.getMessage());
            return false;
        }
        return true;
    }
    
    public boolean cambiarPassword(String vieja, String nueva){
        if(playerLogeado==null) 
            return false;
        if(!playerLogeado.getPassword().equals(vieja)) 
            return false;
        if(nueva == null || nueva.trim().isEmpty()) 
            return false;
        try(RandomAccessFile rUser= new RandomAccessFile(usersFile, "rw")){
            ArrayList<String> users= new ArrayList<>();
            ArrayList<String> passwords= new ArrayList<>();
            ArrayList<Integer> puntos= new ArrayList<>();

            rUser.seek(0);
            while(rUser.getFilePointer()< rUser.length()){
                users.add(rUser.readUTF());
                passwords.add(rUser.readUTF());
                puntos.add(rUser.readInt());
            }

            for(int i=0; i<users.size(); i++){
                if(users.get(i).equals(playerLogeado.getUserName())){
                    passwords.set(i, nueva);
                    break;
                }
            }

            rUser.setLength(0);
            for(int i=0; i<users.size(); i++){
                rUser.writeUTF(users.get(i));
                rUser.writeUTF(passwords.get(i));
                rUser.writeInt(puntos.get(i));
            }

        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
            return false;
        }

        playerLogeado.setPassword(nueva);
        return true;
    }    
    public boolean cambiarUserName(String nuevoUserName){
        if(playerLogeado==null) 
            return false;
        if(nuevoUserName==null ||nuevoUserName.isEmpty()) 
            return false;
        if(arrayUsernames.contains(nuevoUserName)) 
            return false;
        
        String viejoUserName= playerLogeado.getUserName();
        
        //renombrar la carpeta
        File oldDir= new File("users/"+ viejoUserName);
        File newDir= new File("users/" +nuevoUserName);
        if (!oldDir.exists()) 
            return false;
        if (!oldDir.renameTo(newDir)) 
            return false;
        
        
        try (RandomAccessFile rUser= new RandomAccessFile(usersFile, "rw")){
            ArrayList<String> lines= new ArrayList<>();
            rUser.seek(0);
            while (rUser.getFilePointer()<rUser.length()) {
                String user= rUser.readUTF();
                String pass= rUser.readUTF();
                int pts= rUser.readInt();
                if (user.equals(viejoUserName)) {
                    lines.add(nuevoUserName);
                } else {
                    lines.add(user);
                }
                lines.add(pass);
                lines.add(String.valueOf(pts));
            }
            rUser.setLength(0);
            for (int i=0; i<lines.size(); i+=3){
                rUser.writeUTF(lines.get(i));
                rUser.writeUTF(lines.get(i + 1));
                rUser.writeInt(Integer.parseInt(lines.get(i + 2)));
            }
        } catch (IOException e) {
            System.err.println("Error: "+e.getMessage());
            newDir.renameTo(oldDir);
            return false;
        }
        
        arrayUsernames.remove(viejoUserName);
        arrayUsernames.add(nuevoUserName);
        
        playerLogeado.setUserName(nuevoUserName);
        
        return true;
    }
    //logica Retos
    public boolean enviarReto(String receptor,int nivel){
        if(!existeUsuario(receptor)) 
            return false;
        if(receptor.equals(playerLogeado.getUserName())) 
            return false;
        if(retoYaEnviado(receptor, nivel)) 
            return false;

        try(RandomAccessFile rf= new RandomAccessFile("users/" + receptor + "/retos.skb", "rw")){
            rf.seek(rf.length());
            rf.writeUTF(playerLogeado.getUserName());
            rf.writeInt(nivel);
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }
    
    public boolean retoYaEnviado(String receptor, int nivel){
        if(!existeUsuario(receptor)) 
            return false;
        try{
            File file= new File("users/" + receptor + "/retos.skb");
            if(!file.exists()) 
                return false;
            try(RandomAccessFile rf= new RandomAccessFile(file, "r")){
                while(rf.getFilePointer()<rf.length()){
                    String recep= rf.readUTF();
                    int numLevel= rf.readInt();
                    if (recep.equals(playerLogeado.getUserName()) && numLevel==nivel) 
                        return true;
                }
            }
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    public ArrayList<String[]> getRetos(){
        ArrayList<String[]> retos= new ArrayList<>();
        if(playerLogeado==null) 
            return retos;
        try{
            File file= new File("users/" + playerLogeado.getUserName() + "/retos.skb");
            if(!file.exists()) 
                return retos;
            try(RandomAccessFile rf= new RandomAccessFile(file, "r")){
                while (rf.getFilePointer() <rf.length()){
                    String retador= rf.readUTF();
                    int nivel= rf.readInt();
                    retos.add(new String[]{ retador, String.valueOf(nivel)});
                }
            }
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
        return retos;
    }
    public int getCantRetos(){
        return getRetos().size();
    }
    public void removerReto(String retador, int nivel) {
        if (playerLogeado==null) 
            return;
        File file= new File("users/" + playerLogeado.getUserName() + "/retos.skb");
        if(!file.exists())
            return;
        ArrayList<String[]> restantes= new ArrayList<>();
        boolean removido= false;
        try(RandomAccessFile rRetos= new RandomAccessFile(file, "rw")){
            while (rRetos.getFilePointer() < rRetos.length()) {
                String recep= rRetos.readUTF();
                int numLevel= rRetos.readInt();
                if(!removido && recep.equals(retador) && numLevel==nivel){
                    removido= true;
                }else{
                    restantes.add(new String[]{ recep, String.valueOf(numLevel) });
                }
            }
            rRetos.setLength(0);
            for(String[] entry: restantes){
                rRetos.writeUTF(entry[0]);
                rRetos.writeInt(Integer.parseInt(entry[1]));
            }
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }
    
    public int getMejorPuntajeEnNivel(String userName, int nivel){
        int mejor= -1;
        try(RandomAccessFile rf= new RandomAccessFile("users/" + userName + "/historial.skb", "r")){
            while(rf.getFilePointer()<rf.length()){
                rf.readInt();//numIntento
                int numLevel= rf.readInt();
                int puntaje= rf.readInt();
                rf.readInt();//movimientos
                rf.readDouble();//tiempo
                rf.readLong();//fecha
                if(numLevel==nivel &&puntaje>mejor) 
                    mejor= puntaje;
            }
        } catch (IOException e) {
            System.err.println("Error:"+ e.getMessage());
        }
        return mejor;
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