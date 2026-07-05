package negocio;

import modelo.Usuario;
import modelo.HistorialViaje;
import modelo.PerfilPreferencias;
import modelo.Viaje;
import excepciones.UsuarioInvalidoException;
import excepciones.SesionException;
import java.util.ArrayList;

public class GestorUsuario {

    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActivo;
    private ArrayList<HistorialViaje> historiales;
    private ArrayList<PerfilPreferencias> perfiles;
    private ArrayList<String> emailsConIntentos;
    private ArrayList<Integer> conteoIntentos;

    public GestorUsuario() {
        this.usuarios = new ArrayList<Usuario>();
        this.historiales = new ArrayList<HistorialViaje>();
        this.perfiles = new ArrayList<PerfilPreferencias>();
        this.usuarioActivo = null;
        this.emailsConIntentos = new ArrayList<String>();
        this.conteoIntentos = new ArrayList<Integer>();
    }

    public static final int LOGIN_EXITOSO = 0;
    public static final int LOGIN_EMAIL_NO_EXISTE = 1;
    public static final int LOGIN_CONTRASENA_INCORRECTA = 2;
    public static final int LOGIN_CUENTA_ELIMINADA = 3;

    private int ultimoResultado = LOGIN_EMAIL_NO_EXISTE;

    public boolean login(String email, String contrasena) {
        if (email == null || email.equals("") || contrasena == null || contrasena.equals("")) {
            ultimoResultado = LOGIN_EMAIL_NO_EXISTE;
            return false;
        }

        Usuario usuario = buscarPorEmail(email);

        if (usuario == null) {
            ultimoResultado = LOGIN_EMAIL_NO_EXISTE;
            return false;
        }

        if (usuario.iniciarSesion(email, contrasena)) {
            usuarioActivo = usuario;
            reiniciarIntentosDeEmail(email);
            ultimoResultado = LOGIN_EXITOSO;
            return true;
        }

        int intentos = incrementarIntentosDeEmail(email);

        if (intentos >= 3) {
            eliminarCuentaBloqueada(email);
            ultimoResultado = LOGIN_CUENTA_ELIMINADA;
        } else {
            ultimoResultado = LOGIN_CONTRASENA_INCORRECTA;
        }
        return false;
    }

    public int getUltimoResultado() {
        return ultimoResultado;
    }

    private int incrementarIntentosDeEmail(String email) {
        for (int i = 0; i < emailsConIntentos.size(); i++) {
            if (emailsConIntentos.get(i).equalsIgnoreCase(email)) {
                int nuevo = conteoIntentos.get(i) + 1;
                conteoIntentos.set(i, nuevo);
                return nuevo;
            }
        }
        emailsConIntentos.add(email);
        conteoIntentos.add(1);
        return 1;
    }

    private void reiniciarIntentosDeEmail(String email) {
        for (int i = 0; i < emailsConIntentos.size(); i++) {
            if (emailsConIntentos.get(i).equalsIgnoreCase(email)) {
                emailsConIntentos.remove(i);
                conteoIntentos.remove(i);
                return;
            }
        }
    }

    public int getIntentosDeEmail(String email) {
        for (int i = 0; i < emailsConIntentos.size(); i++) {
            if (emailsConIntentos.get(i).equalsIgnoreCase(email)) {
                return conteoIntentos.get(i);
            }
        }
        return 0;
    }

    private void eliminarCuentaBloqueada(String email) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(email)) {
                historiales.remove(i);
                perfiles.remove(i);
                usuarios.remove(i);
                break;
            }
        }
        reiniciarIntentosDeEmail(email);
    }

    public void logout() {
        if (usuarioActivo != null) {
            usuarioActivo.cerrarSesion();
            usuarioActivo = null;
        }
    }

    public boolean registrarUsuario(String nombre, String email, String contrasena) throws UsuarioInvalidoException {
        return registrarUsuario(nombre, email, contrasena, "", "");
    }

    public boolean registrarUsuario(String nombre, String email, String contrasena,
                                    String preguntaSeguridad, String respuestaSeguridad) throws UsuarioInvalidoException {

        if (nombre == null || nombre.trim().equals("")) {
            throw new UsuarioInvalidoException("El nombre es obligatorio.");
        }
        if (!modelo.Usuario.nombreValido(nombre)) {
            throw new UsuarioInvalidoException("El nombre debe empezar con una letra.");
        }
        if (nombre.trim().length() < 3) {
            throw new UsuarioInvalidoException("El nombre debe tener al menos 3 caracteres.");
        }
        if (email == null || email.trim().equals("")) {
            throw new UsuarioInvalidoException("El email es obligatorio.");
        }
        if (!esEmailValido(email.trim())) {
            throw new UsuarioInvalidoException("El email no tiene un formato valido (ejemplo: nombre@correo.com).");
        }
        if (contrasena == null || contrasena.equals("")) {
            throw new UsuarioInvalidoException("La contraseña es obligatoria.");
        }
        if (contrasena.length() < 6) {
            throw new UsuarioInvalidoException("La contraseña debe tener al menos 6 caracteres.");
        }

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(email.trim())) {
                throw new UsuarioInvalidoException("Ya existe un usuario registrado con ese email.");
            }
        }

        int nuevoId = usuarios.size() + 1;
        Usuario nuevoUsuario = new Usuario(nuevoId, nombre.trim(), email.trim(), contrasena);

        if (preguntaSeguridad != null && !preguntaSeguridad.trim().equals("")
                && respuestaSeguridad != null && !respuestaSeguridad.trim().equals("")) {
            nuevoUsuario.setPreguntaSeguridad(preguntaSeguridad.trim());
            nuevoUsuario.setRespuestaSeguridad(respuestaSeguridad.trim());
        }

        usuarios.add(nuevoUsuario);
        historiales.add(new HistorialViaje());
        perfiles.add(new PerfilPreferencias(100.0));
        return true;
    }

    public boolean esEmailValido(String email) {
        if (email == null) { return false; }
        return email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    public Usuario buscarPorEmail(String email) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(email)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    public String recuperarContrasena(String email, String respuesta) throws SesionException {
        Usuario usuario = buscarPorEmail(email);
        if (usuario == null) {
            throw new SesionException("No existe un usuario registrado con ese email.");
        }
        if (!usuario.verificarRespuestaSeguridad(respuesta)) {
            throw new SesionException("La respuesta de seguridad es incorrecta.");
        }
        return usuario.getContrasena();
    }

    public void agregarViajeAlHistorial(Viaje viaje) {
        int indice = usuarios.indexOf(usuarioActivo);
        if (indice >= 0 && indice < historiales.size()) {
            historiales.get(indice).agregarViaje(viaje);
        }
    }

    public HistorialViaje obtenerHistorial() {
        int indice = usuarios.indexOf(usuarioActivo);
        if (indice >= 0 && indice < historiales.size()) {
            return historiales.get(indice);
        }
        return new HistorialViaje();
    }

    public HistorialViaje obtenerHistorialDeUsuario(int indice) {
        if (indice >= 0 && indice < historiales.size()) {
            return historiales.get(indice);
        }
        return new HistorialViaje();
    }

    public PerfilPreferencias getPerfilUsuarioActivo() {
        int indice = usuarios.indexOf(usuarioActivo);
        if (indice >= 0 && indice < perfiles.size()) {
            return perfiles.get(indice);
        }
        return new PerfilPreferencias(100.0);
    }

    public void guardarPerfilUsuarioActivo(PerfilPreferencias perfil) {
        int indice = usuarios.indexOf(usuarioActivo);
        if (indice >= 0 && indice < perfiles.size()) {
            perfiles.set(indice, perfil);
        }
    }

    public boolean eliminarUsuarioPorEmail(String email) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(email)) {
                historiales.remove(i);
                perfiles.remove(i);
                usuarios.remove(i);
                return true;
            }
        }
        return false;
    }

    public Usuario getUsuarioActivo() { return usuarioActivo; }
    public ArrayList<Usuario> getUsuarios() { return usuarios; }
    public boolean haySesionActiva() { return usuarioActivo != null; }

    public boolean tienePerfilConfigurado() {
        PerfilPreferencias perfil = getPerfilUsuarioActivo();
        return perfil.getEstilosViaje().size() > 0;
    }
}