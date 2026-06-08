package io.github.some_example_name;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;

public class PlayerManager implements Gestionable<Player> {
    private ArrayList<String> arrayUsernames = new ArrayList<>();
    private File usersFile = new File("users.skb");
    private Player playerLogeado = null;

    PlayerManager() {
        cargar();
    }

    // ── Utilidad SHA-256 ──────────────────────────────────────────────────────
    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }

    // ── Registro e inicio de sesion ───────────────────────────────────────────
    public boolean registrarUser(String userName, String password, String nombreCompleto) {
        if (arrayUsernames.contains(userName)) return false;
        try (RandomAccessFile rUsers = new RandomAccessFile(usersFile, "rw")) {
            rUsers.seek(rUsers.length());
            rUsers.writeUTF(userName);
            rUsers.writeUTF(hash(password));
            rUsers.writeInt(0);
            crearArchivosNuevos(userName, nombreCompleto);
            arrayUsernames.add(userName);
            return true;
        } catch (IOException e) {
            System.err.println("Error registro: " + e.getMessage());
        }
        return false;
    }

    public boolean logIn(String userName, String password) {
        if (!arrayUsernames.contains(userName)) return false;
        try (RandomAccessFile rPlayer = new RandomAccessFile(usersFile, "r")) {
            while (rPlayer.getFilePointer() < rPlayer.length()) {
                String user = rPlayer.readUTF();
                if (user.equals(userName)) {
                    String passGuardada = rPlayer.readUTF();
                    if (!hash(password).equals(passGuardada)) return false;
                    int puntos = rPlayer.readInt();
                    cargarUser(userName, password, puntos);
                    return true;
                } else {
                    rPlayer.readUTF();
                    rPlayer.readInt();
                }
            }
        } catch (IOException e) {
            System.err.println("Error login: " + e.getMessage());
        }
        return false;
    }

    public void cerrarSesion() {
        playerLogeado = null;
    }

    // ── Cambio de idioma ──────────────────────────────────────────────────────
    /**
     * Cambia el idioma del jugador logueado y lo persiste en perfil.skb.
     * Si no hay sesion activa solo actualiza Textos (ya hecho por el caller).
     */
    public void cambiarIdioma(String nuevoIdioma) {
        if (playerLogeado == null) return;
        playerLogeado.setIdioma(nuevoIdioma);
        guardar();
    }

    // ── Carga completa del usuario desde sus archivos ─────────────────────────
    private void cargarUser(String userName, String password, int puntos) {
        try {
            RandomAccessFile rf = new RandomAccessFile("users/" + userName + "/perfil.skb", "r");
            String nombreCompleto = rf.readUTF();
            long   fechaRegistro  = rf.readLong();
            long   ultimaSesion   = rf.readLong();
            double volumen        = rf.readDouble();
            String idioma         = rf.readUTF();
            String rutaAvatar     = rf.readUTF();
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/stats.skb", "r");
            int    partidasJugadas        = rf.readInt();
            int    nivelesCompletados     = rf.readInt();
            int    mejorPuntaje           = rf.readInt();
            int    puntajeGeneral         = rf.readInt();
            double tiempoJugadoHoras      = rf.readDouble();
            double tiempoPromedioPorNivel = rf.readDouble();
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/progreso.skb", "r");
            int nivelesDesbloqueados = rf.readInt();
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/avatar.skb", "r");
            int colCabeza    = rf.readInt();
            int filaCabeza   = rf.readInt();
            int colTorso     = rf.readInt();
            int filaTorso    = rf.readInt();
            int colAccesorio = rf.readInt();
            int filaAccesorio= rf.readInt();
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/amigos.skb", "r");
            ArrayList<String> amigos = new ArrayList<>();
            while (rf.getFilePointer() < rf.length()) amigos.add(rf.readUTF());
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/historial.skb", "r");
            ArrayList<EntradaHistorial> historial = new ArrayList<>();
            while (rf.getFilePointer() < rf.length()) {
                int    numIntento = rf.readInt();
                int    nivel      = rf.readInt();
                int    puntajeh   = rf.readInt();
                int    movimientos= rf.readInt();
                double tiempo     = rf.readDouble();
                long   fecha      = rf.readLong();
                historial.add(new EntradaHistorial(numIntento, nivel, puntajeh, movimientos, tiempo, fecha));
            }
            rf.close();

            playerLogeado = new Player(userName, password, puntos, nombreCompleto, rutaAvatar,
                    fechaRegistro, ultimaSesion, volumen, idioma, amigos,
                    partidasJugadas, nivelesCompletados, mejorPuntaje, puntajeGeneral,
                    tiempoJugadoHoras, tiempoPromedioPorNivel, nivelesDesbloqueados,
                    colCabeza, filaCabeza, colTorso, filaTorso, colAccesorio, filaAccesorio, historial);

            // Aplicar el idioma guardado del jugador
            if (idioma != null && !idioma.isEmpty()) {
                Textos.aplicar(idioma);
            }

        } catch (IOException e) {
            System.err.println("Error cargar usuario: " + e.getMessage());
        }
    }

    private void crearArchivosNuevos(String userName, String nombreCompleto) {
        try {
            new File("users/" + userName).mkdirs();
            new File("users/" + userName + "/amigos.skb").createNewFile();
            new File("users/" + userName + "/historial.skb").createNewFile();

            RandomAccessFile rf = new RandomAccessFile("users/" + userName + "/perfil.skb", "rw");
            rf.writeUTF(nombreCompleto);
            rf.writeLong(Calendar.getInstance().getTimeInMillis());
            rf.writeLong(Calendar.getInstance().getTimeInMillis());
            rf.writeDouble(0.8);
            rf.writeUTF(Textos.idioma);   // guarda el idioma actual al registrarse
            rf.writeUTF("default/avatar.png");
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/stats.skb", "rw");
            rf.writeInt(0); rf.writeInt(0); rf.writeInt(0); rf.writeInt(0);
            rf.writeDouble(0); rf.writeDouble(0);
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/progreso.skb", "rw");
            rf.writeInt(0);
            rf.close();

            rf = new RandomAccessFile("users/" + userName + "/avatar.skb", "rw");
            for (int i = 0; i < 6; i++) rf.writeInt(0);
            rf.close();
        } catch (IOException e) {
            System.err.println("Error crear archivos: " + e.getMessage());
        }
    }

    // ── Actualizacion de estadisticas tras completar un nivel ─────────────────
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

    // ── Ranking ───────────────────────────────────────────────────────────────
    public ArrayList<String[]> getRanking() {
        ArrayList<String[]> lista = new ArrayList<>();
        for (String user : arrayUsernames) {
            try (RandomAccessFile rf = new RandomAccessFile("users/" + user + "/stats.skb", "r")) {
                rf.readInt(); rf.readInt(); rf.readInt();
                int puntaje = rf.readInt();
                lista.add(new String[]{ user, String.valueOf(puntaje) });
            } catch (IOException ignored) {}
        }
        // Ordenamiento burbuja descendente
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

    // ── Getters ───────────────────────────────────────────────────────────────
    public Player getPlayerLogeado() { return playerLogeado; }

    public String getNombreJugador() {
        if (playerLogeado == null) return "Invitado";
        String n = playerLogeado.getNombreCompleto();
        return (n != null && !n.isEmpty()) ? n : playerLogeado.getUserName();
    }

    // ── Interfaz Gestionable ──────────────────────────────────────────────────
    @Override
    public void cargar() {
        try (RandomAccessFile rUsers = new RandomAccessFile(usersFile, "r")) {
            while (rUsers.getFilePointer() < rUsers.length()) {
                arrayUsernames.add(rUsers.readUTF());
                rUsers.readUTF();
                rUsers.readInt();
            }
        } catch (IOException e) {
            System.err.println("Sin archivo de usuarios: " + e.getMessage());
        }
    }

    @Override
    public synchronized void guardar() {
        if (playerLogeado == null) return;
        try {
            // Actualizar puntos en users.skb
            RandomAccessFile rf = new RandomAccessFile(usersFile, "rw");
            while (rf.getFilePointer() < rf.length()) {
                String user = rf.readUTF();
                if (user.equals(playerLogeado.getUserName())) {
                    rf.readUTF();
                    rf.writeInt(playerLogeado.getPuntos());
                    break;
                } else { rf.readUTF(); rf.readInt(); }
            }
            rf.close();

            // perfil.skb (incluye idioma)
            rf = new RandomAccessFile("users/" + playerLogeado.getUserName() + "/perfil.skb", "rw");
            rf.setLength(0);
            rf.writeUTF(playerLogeado.getNombreCompleto());
            rf.writeLong(playerLogeado.getFechaRegistro());
            rf.writeLong(System.currentTimeMillis());
            rf.writeDouble(playerLogeado.getVolumen());
            rf.writeUTF(playerLogeado.getIdioma());
            rf.writeUTF(playerLogeado.getRutaAvatar());
            rf.close();

            // stats.skb
            rf = new RandomAccessFile("users/" + playerLogeado.getUserName() + "/stats.skb", "rw");
            rf.setLength(0);
            rf.writeInt(playerLogeado.getPartidasJugadas());
            rf.writeInt(playerLogeado.getNivelesCompletados());
            rf.writeInt(playerLogeado.getMejorPuntaje());
            rf.writeInt(playerLogeado.getPuntajeGeneral());
            rf.writeDouble(playerLogeado.getTiempoJugadoHoras());
            rf.writeDouble(playerLogeado.getTiempoPromedioPorNivel());
            rf.close();

            // progreso.skb
            rf = new RandomAccessFile("users/" + playerLogeado.getUserName() + "/progreso.skb", "rw");
            rf.setLength(0);
            rf.writeInt(playerLogeado.getNivelesDesbloqueados());
            rf.close();

        } catch (IOException e) {
            System.err.println("Error guardar: " + e.getMessage());
        }
    }

    @Override public Player getActual()  { return playerLogeado; }
    @Override public int getCantidad()   { return arrayUsernames.size(); }
}