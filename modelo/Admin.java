package modelo;

public class Admin {

    private String usuario;
    private String contrasena;

    public Admin(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean iniciarSesion(String usuarioIngresado, String contrasenaIngresada) {
        if (this.usuario.equals(usuarioIngresado) && this.contrasena.equals(contrasenaIngresada)) {
            return true;
        }
        return false;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String toString() {
        return "Admin [usuario=" + usuario + "]";
    }
}