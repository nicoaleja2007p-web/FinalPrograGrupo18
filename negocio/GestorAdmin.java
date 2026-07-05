package negocio;

import modelo.Admin;
import modelo.Usuario;
import modelo.Viaje;
import modelo.DiaDeViaje;
import modelo.Actividad;
import modelo.HistorialViaje;
import excepciones.SesionException;
import java.util.ArrayList;

public class GestorAdmin {

    private Admin admin;
    private Admin adminActivo;
    private GestorUsuario gestorUsuario;
    private GestorActividades gestorActividades;

    public GestorAdmin(GestorUsuario gestorUsuario, GestorActividades gestorActividades) {
        this.admin = new Admin("adminviaje", "adminviaje123");
        this.adminActivo = null;
        this.gestorUsuario = gestorUsuario;
        this.gestorActividades = gestorActividades;
    }

    public boolean login(String usuario, String contrasena) throws SesionException {
        if (usuario == null || usuario.equals("") || contrasena == null || contrasena.equals("")) {
            throw new SesionException("Usuario y contraseña son obligatorios.");
        }
        if (admin.iniciarSesion(usuario, contrasena)) {
            adminActivo = admin;
            return true;
        }
        throw new SesionException("Usuario o contraseña de administrador incorrectos.");
    }

    public void logout() { adminActivo = null; }
    public boolean haySesionActiva() { return adminActivo != null; }

    public ArrayList<Usuario> listarUsuarios() { return gestorUsuario.getUsuarios(); }
    public int contarUsuarios() { return gestorUsuario.getUsuarios().size(); }
    public int contarActividades() { return gestorActividades.getActividades().size(); }

    public boolean registrarActividad(String nombre, double costo, int duracion, String categoria, String tipoDestino) {
        return gestorActividades.registrarActividad(nombre, costo, duracion, categoria, tipoDestino);
    }

    public boolean eliminarActividad(int id) { return gestorActividades.eliminarActividad(id); }

    public boolean eliminarUsuarioPorEmail(String email) {
        return gestorUsuario.eliminarUsuarioPorEmail(email);
    }

    public int contarViajesTotales() {
        int total = 0;
        for (int i = 0; i < gestorUsuario.getUsuarios().size(); i++) {
            total = total + gestorUsuario.obtenerHistorialDeUsuario(i).obtenerViajes().size();
        }
        return total;
    }

    public double calcularGastoTotalSistema() {
        double total = 0.0;
        for (int i = 0; i < gestorUsuario.getUsuarios().size(); i++) {
            total = total + gestorUsuario.obtenerHistorialDeUsuario(i).calcularGastoTotal();
        }
        return total;
    }

    public String categoriaMasUsada() {
        ArrayList<String> nombres = new ArrayList<String>();
        ArrayList<Integer> conteos = new ArrayList<Integer>();

        for (int u = 0; u < gestorUsuario.getUsuarios().size(); u++) {
            ArrayList<Viaje> viajes = gestorUsuario.obtenerHistorialDeUsuario(u).obtenerViajes();
            for (int v = 0; v < viajes.size(); v++) {
                ArrayList<DiaDeViaje> dias = viajes.get(v).getDias();
                for (int d = 0; d < dias.size(); d++) {
                    ArrayList<Actividad> acts = dias.get(d).getActividades();
                    for (int a = 0; a < acts.size(); a++) {
                        String cat = acts.get(a).getCategoria();
                        int idx = nombres.indexOf(cat);
                        if (idx == -1) { nombres.add(cat); conteos.add(1); }
                        else { conteos.set(idx, conteos.get(idx) + 1); }
                    }
                }
            }
        }

        if (nombres.size() == 0) { return "Sin datos aun"; }
        int max = 0;
        for (int i = 1; i < conteos.size(); i++) {
            if (conteos.get(i) > conteos.get(max)) { max = i; }
        }
        return nombres.get(max) + " (" + conteos.get(max) + " veces)";
    }

    public String destinosMasVisitados() {
        ArrayList<String> destinos = new ArrayList<String>();
        ArrayList<Integer> conteos = new ArrayList<Integer>();

        for (int u = 0; u < gestorUsuario.getUsuarios().size(); u++) {
            ArrayList<Viaje> viajes = gestorUsuario.obtenerHistorialDeUsuario(u).obtenerViajes();
            for (int v = 0; v < viajes.size(); v++) {
                String dest = viajes.get(v).getDestino();
                int idx = destinos.indexOf(dest);
                if (idx == -1) { destinos.add(dest); conteos.add(1); }
                else { conteos.set(idx, conteos.get(idx) + 1); }
            }
        }

        if (destinos.size() == 0) { return "Sin datos aun"; }

        for (int i = 0; i < destinos.size() - 1; i++) {
            for (int j = 0; j < destinos.size() - 1 - i; j++) {
                if (conteos.get(j) < conteos.get(j + 1)) {
                    int tmpC = conteos.get(j); conteos.set(j, conteos.get(j+1)); conteos.set(j+1, tmpC);
                    String tmpD = destinos.get(j); destinos.set(j, destinos.get(j+1)); destinos.set(j+1, tmpD);
                }
            }
        }

        String resultado = "";
        int limite = Math.min(3, destinos.size());
        for (int i = 0; i < limite; i++) {
            resultado = resultado + (i + 1) + ". " + destinos.get(i) + " (" + conteos.get(i) + " viaje(s))\n";
        }
        return resultado;
    }

    public double gastoPromedioPorViaje() {
        int total = contarViajesTotales();
        if (total == 0) { return 0.0; }
        return calcularGastoTotalSistema() / total;
    }

    public GestorUsuario getGestorUsuario() { return gestorUsuario; }
    public GestorActividades getGestorActividades() { return gestorActividades; }
}