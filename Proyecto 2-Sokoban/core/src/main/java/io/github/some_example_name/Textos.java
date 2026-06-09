package io.github.some_example_name;

public class Textos {

    public static String idioma = "espanol";

    // ── LoginScreen ───────────────────────────────────────────────────────────
    public static String LOGIN_SUBTITULO;
    public static String LOGIN_CAMPO_USUARIO;
    public static String LOGIN_CAMPO_CLAVE;
    public static String LOGIN_BTN_INGRESAR;
    public static String LOGIN_BTN_REGISTRO;
    public static String LOGIN_BTN_IDIOMA;
    public static String LOGIN_VER_CLAVE;
    public static String LOGIN_ERR_CAMPOS;
    public static String LOGIN_ERR_CREDENCIALES;

    // ── RegisterScreen ────────────────────────────────────────────────────────
    public static String REG_SUBTITULO;
    public static String REG_CAMPO_NOMBRE;
    public static String REG_CAMPO_USUARIO;
    public static String REG_CAMPO_CLAVE;
    public static String REG_CAMPO_CONFIRMAR;
    public static String REG_BTN_REGISTRAR;
    public static String REG_BTN_VOLVER;
    public static String REG_BTN_IDIOMA;
    public static String REG_VER_CLAVE;
    public static String REG_REQ_LONGITUD;
    public static String REG_REQ_MAYUSCULA;
    public static String REG_REQ_NUMERO;
    public static String REG_REQ_SIMBOLO;
    public static String REG_ERR_CAMPOS;
    public static String REG_ERR_USUARIO_CORTO;
    public static String REG_ERR_CLAVES;
    public static String REG_ERR_REQUISITOS;
    public static String REG_ERR_EXISTE;
    public static String REG_OK_CREADO;

    // ── MenuScreen ────────────────────────────────────────────────────────────
    public static String MENU_SALUDO;
    public static String MENU_BTN_JUGAR;
    public static String MENU_BTN_PERFIL;
    public static String MENU_BTN_IDIOMA;
    public static String MENU_BTN_SESION;
    public static String MENU_BTN_SALIR;

    // ── LevelSelectScreen ─────────────────────────────────────────────────────
    public static String NIVEL_TITULO;
    public static String NIVEL_PREFIJO;
    public static String NIVEL_BLOQUEADO;
    public static String NIVEL_COMPLETADO;
    public static String NIVEL_CAJAS;
    public static String NIVEL_BTN_VOLVER;
    public static String NIVEL_HINT_REPLAY;

    // ── GameScreen HUD ────────────────────────────────────────────────────────
    public static String GAME_MOVS;
    public static String GAME_BTN_REINICIAR;
    public static String GAME_BTN_SALIR;

    // ── VictoryScreen ─────────────────────────────────────────────────────────
    public static String VIC_TITULO;
    public static String VIC_PREFIJO_NIVEL;
    public static String VIC_MOVS;
    public static String VIC_TIEMPO;
    public static String VIC_PUNTAJE;
    public static String VIC_TODOS;
    public static String VIC_BTN_SIGUIENTE;
    public static String VIC_BTN_NIVELES;
    public static String VIC_BTN_MENU;

    // ── ProfileScreen ─────────────────────────────────────────────────────────
    public static String PERFIL_TITULO;
    public static String PERFIL_RANKING;
    public static String PERFIL_PARTIDAS;
    public static String PERFIL_NIV_COMP;
    public static String PERFIL_MEJOR_PTS;
    public static String PERFIL_PTS_TOTAL;
    public static String PERFIL_TIEMPO;
    public static String PERFIL_PROMEDIO;
    public static String PERFIL_DESBLOQUEADOS;
    public static String PERFIL_ULTIMAS;
    public static String PERFIL_NIV_ABREV;
    public static String PERFIL_MOVS_ABREV;
    public static String PERFIL_PTS_ABREV;
    public static String PERFIL_SIN_DATOS;
    public static String PERFIL_BTN_VOLVER;
    public static String PERFIL_BTN_RECORDS;

    // ── RecordsScreen ─────────────────────────────────────────────────────────
    public static String REC_TITULO;
    public static String REC_COL_NIVEL;
    public static String REC_COL_PUNTAJE;
    public static String REC_COL_MOVS;
    public static String REC_COL_TIEMPO;
    public static String REC_SIN_DATOS;
    public static String REC_BTN_VOLVER;
    public static String REC_HINT;

    // ── Idiomas disponibles ───────────────────────────────────────────────────
    public static final String[] IDIOMAS = { "espanol", "ingles" };

    // ── Aplicar idioma ────────────────────────────────────────────────────────
    public static void aplicar(String nuevoIdioma) {
        idioma = nuevoIdioma;
        boolean es = "espanol".equals(nuevoIdioma);

        LOGIN_SUBTITULO        = es ? "Iniciar Sesion"              : "Log In";
        LOGIN_CAMPO_USUARIO    = es ? "Usuario"                     : "Username";
        LOGIN_CAMPO_CLAVE      = es ? "Contrasena"                  : "Password";
        LOGIN_BTN_INGRESAR     = es ? "Ingresar"                    : "Log In";
        LOGIN_BTN_REGISTRO     = es ? "Registrarse"                 : "Register";
        LOGIN_BTN_IDIOMA       = es ? "English"                     : "Espanol";
        LOGIN_VER_CLAVE        = es ? "F1: ver contrasena"          : "F1: show password";
        LOGIN_ERR_CAMPOS       = es ? "Completa todos los campos"   : "Fill in all fields";
        LOGIN_ERR_CREDENCIALES = es ? "Usuario o clave incorrectos" : "Invalid user or password";

        REG_SUBTITULO          = es ? "Crear Cuenta"                : "Create Account";
        REG_CAMPO_NOMBRE       = es ? "Nombre completo"             : "Full name";
        REG_CAMPO_USUARIO      = es ? "Usuario"                     : "Username";
        REG_CAMPO_CLAVE        = es ? "Contrasena"                  : "Password";
        REG_CAMPO_CONFIRMAR    = es ? "Confirmar clave"             : "Confirm password";
        REG_BTN_REGISTRAR      = es ? "Registrar"                   : "Register";
        REG_BTN_VOLVER         = es ? "Volver"                      : "Back";
        REG_BTN_IDIOMA         = es ? "English"                     : "Espanol";
        REG_VER_CLAVE          = es ? "F1: ver clave"               : "F1: show password";
        REG_REQ_LONGITUD       = es ? "8+ chars"                    : "8+ chars";
        REG_REQ_MAYUSCULA      = es ? "Mayuscula"                   : "Uppercase";
        REG_REQ_NUMERO         = es ? "Numero"                      : "Number";
        REG_REQ_SIMBOLO        = es ? "Simbolo"                     : "Symbol";
        REG_ERR_CAMPOS         = es ? "Completa todos los campos"   : "Fill in all fields";
        REG_ERR_USUARIO_CORTO  = es ? "Usuario muy corto (min. 3)" : "Username too short (min 3)";
        REG_ERR_CLAVES         = es ? "Las claves no coinciden"     : "Passwords do not match";
        REG_ERR_REQUISITOS     = es ? "La clave no cumple los requisitos" : "Password requirements not met";
        REG_ERR_EXISTE         = es ? "Ese usuario ya existe"       : "Username already taken";
        REG_OK_CREADO          = es ? "Cuenta creada."              : "Account created.";

        MENU_SALUDO            = es ? "Hola, "                      : "Hello, ";
        MENU_BTN_JUGAR         = es ? "Jugar"                       : "Play";
        MENU_BTN_PERFIL        = es ? "Mi Perfil"                   : "My Profile";
        MENU_BTN_IDIOMA        = es ? "English"                     : "Espanol";
        MENU_BTN_SESION        = es ? "Cerrar Sesion"               : "Log Out";
        MENU_BTN_SALIR         = es ? "Salir"                       : "Exit";

        NIVEL_TITULO           = es ? "Seleccionar Nivel"           : "Select Level";
        NIVEL_PREFIJO          = es ? "Nivel "                      : "Level ";
        NIVEL_BLOQUEADO        = es ? "[ bloq ]"                    : "[ lock ]";
        NIVEL_COMPLETADO       = es ? "[hecho]"                     : "[done]";
        NIVEL_CAJAS            = es ? "Cajas: "                     : "Boxes: ";
        NIVEL_BTN_VOLVER       = es ? "Volver"                      : "Back";
        

        GAME_MOVS              = es ? "Movs: "                      : "Moves: ";
        GAME_BTN_REINICIAR     = es ? "Reiniciar"                   : "Restart";
        GAME_BTN_SALIR         = es ? "Salir"                       : "Exit";

        VIC_TITULO             = es ? "Nivel completado!"           : "Level complete!";
        VIC_PREFIJO_NIVEL      = es ? "Nivel "                      : "Level ";
        VIC_MOVS               = es ? "Movimientos : "              : "Moves       : ";
        VIC_TIEMPO             = es ? "Tiempo      : "              : "Time        : ";
        VIC_PUNTAJE            = es ? "Puntaje     : "              : "Score       : ";
        VIC_TODOS              = es ? "Has completado todos los niveles!" : "You completed all levels!";
        VIC_BTN_SIGUIENTE      = es ? "Siguiente"                   : "Next";
        VIC_BTN_NIVELES        = es ? "Niveles"                     : "Levels";
        VIC_BTN_MENU           = es ? "Menu"                        : "Menu";

        PERFIL_TITULO          = es ? "Mi Perfil"                   : "My Profile";
        PERFIL_RANKING         = es ? "Clasificacion"               : "Rankings";
        PERFIL_PARTIDAS        = es ? "Partidas jugadas    "        : "Games played        ";
        PERFIL_NIV_COMP        = es ? "Niveles completados "        : "Levels completed    ";
        PERFIL_MEJOR_PTS       = es ? "Mejor puntaje       "        : "Best score          ";
        PERFIL_PTS_TOTAL       = es ? "Puntaje total       "        : "Total score         ";
        PERFIL_TIEMPO          = es ? "Tiempo jugado       "        : "Time played         ";
        PERFIL_PROMEDIO        = es ? "Prom. por nivel     "        : "Avg. per level      ";
        PERFIL_DESBLOQUEADOS   = es ? "Niveles desbloq.    "        : "Levels unlocked     ";
        PERFIL_ULTIMAS         = es ? "Ultimas partidas:"           : "Recent games:";
        PERFIL_NIV_ABREV       = es ? "Nv."                         : "Lv.";
        PERFIL_MOVS_ABREV      = es ? "  Movs:"                     : "  Moves:";
        PERFIL_PTS_ABREV       = es ? "  Pts:"                      : "  Pts:";
        PERFIL_SIN_DATOS       = es ? "Sin datos aun"               : "No data yet";
        PERFIL_BTN_VOLVER      = es ? "Volver al menu"              : "Back to menu";
        PERFIL_BTN_RECORDS     = es ? "Mis Records"                 : "My Records";

        REC_TITULO             = es ? "Mis Records por Nivel"       : "My Records by Level";
        REC_COL_NIVEL          = es ? "Nivel"                       : "Level";
        REC_COL_PUNTAJE        = es ? "Puntaje"                     : "Score";
        REC_COL_MOVS           = es ? "Movs"                        : "Moves";
        REC_COL_TIEMPO         = es ? "Tiempo"                      : "Time";
        REC_SIN_DATOS          = es ? "Sin intentos"                : "No attempts";
        REC_BTN_VOLVER         = es ? "Volver"                      : "Back";
        REC_HINT               = es ? "Mejor puntaje por nivel (todos los intentos)" : "Best score per level (all attempts)";
    }

    public static String siguienteIdioma() {
        for (int i = 0; i < IDIOMAS.length; i++) {
            if (IDIOMAS[i].equals(idioma)) return IDIOMAS[(i + 1) % IDIOMAS.length];
        }
        return IDIOMAS[0];
    }

    static { aplicar("espanol"); }
}
