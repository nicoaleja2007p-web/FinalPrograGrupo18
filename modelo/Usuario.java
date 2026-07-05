package modelo;

public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private PerfilPreferencias perfil;
    private HistorialViaje historial;
    private String preguntaSeguridad;
    private String respuestaSeguridad;

    public Usuario(int id, String nombre, String email, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.perfil = new PerfilPreferencias(100.0);
        this.historial = new HistorialViaje();
        this.preguntaSeguridad = "";
        this.respuestaSeguridad = "";
    }

    public boolean iniciarSesion(String emailIngresado, String contrasenaIngresada) {
        if (this.email.equals(emailIngresado) && this.contrasena.equals(contrasenaIngresada)) {
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        System.out.println("Sesion cerrada para el usuario: " + nombre);
    }

    public void agregarViaje(Viaje viaje) {
        historial.agregarViaje(viaje);
    }

    public boolean verificarRespuestaSeguridad(String respuesta) {
        if (respuesta == null) { return false; }
        return this.respuestaSeguridad.equalsIgnoreCase(respuesta.trim());
    }

    public static boolean nombreValido(String nombre) {
        if (nombre == null || nombre.trim().length() == 0) { return false; }
        char primera = nombre.trim().charAt(0);
        return Character.isLetter(primera);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public PerfilPreferencias getPerfil() { return perfil; }
    public void setPerfil(PerfilPreferencias perfil) { this.perfil = perfil; }

    public HistorialViaje getHistorial() { return historial; }

    public String getPreguntaSeguridad() { return preguntaSeguridad; }
    public void setPreguntaSeguridad(String p) { this.preguntaSeguridad = p; }

    public String getRespuestaSeguridad() { return respuestaSeguridad; }
    public void setRespuestaSeguridad(String r) { this.respuestaSeguridad = r; }

    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email + "]";
    }
}