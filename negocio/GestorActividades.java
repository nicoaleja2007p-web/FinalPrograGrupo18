package negocio;

import modelo.Actividad;
import excepciones.ActividadInvalidaException;
import java.util.ArrayList;

public class GestorActividades {

    private ArrayList<Actividad> actividades;

    public GestorActividades() {
        this.actividades = new ArrayList<Actividad>();
    }

    public boolean registrarActividad(String nombre, double costo, int duracion, String categoria, String tipoDestino) {
        try {
            validarDatosActividad(nombre, costo, duracion, categoria, tipoDestino);
        } catch (ActividadInvalidaException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        int nuevoId = actividades.size() + 1;
        Actividad nueva = new Actividad(nuevoId, nombre, costo, duracion, categoria, tipoDestino);
        actividades.add(nueva);
        System.out.println("Actividad registrada: " + nombre);
        return true;
    }

    public boolean actualizarActividad(int id, String nombre, double costo, int duracion, String categoria, String tipoDestino) {
        try {
            validarDatosActividad(nombre, costo, duracion, categoria, tipoDestino);
        } catch (ActividadInvalidaException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId() == id) {
                actividades.get(i).setNombre(nombre);
                actividades.get(i).setCosto(costo);
                actividades.get(i).setDuracion(duracion);
                actividades.get(i).setCategoria(categoria);
                actividades.get(i).setTipoDestino(tipoDestino);
                System.out.println("Actividad actualizada: " + nombre);
                return true;
            }
        }
        System.out.println("Error: Actividad no encontrada.");
        return false;
    }

    private void validarDatosActividad(String nombre, double costo, int duracion, String categoria, String tipoDestino) throws ActividadInvalidaException {
        if (nombre == null || nombre.equals("")) {
            throw new ActividadInvalidaException("El nombre de la actividad es obligatorio.");
        }
        if (costo < 0) {
            throw new ActividadInvalidaException("El costo no puede ser negativo.");
        }
        if (duracion <= 0) {
            throw new ActividadInvalidaException("La duración debe ser mayor a cero.");
        }
        if (categoria == null || categoria.equals("")) {
            throw new ActividadInvalidaException("La categoría es obligatoria.");
        }
        if (tipoDestino == null || tipoDestino.equals("")) {
            throw new ActividadInvalidaException("El tipo de destino es obligatorio.");
        }
    }

    public boolean eliminarActividad(int id) {
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId() == id) {
                actividades.remove(i);
                System.out.println("Actividad eliminada.");
                return true;
            }
        }
        System.out.println("Error: Actividad no encontrada.");
        return false;
    }

    public ArrayList<Actividad> filtrarPorPresupuesto(double presupuesto) {
        ArrayList<Actividad> resultado = new ArrayList<Actividad>();
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).esViable(presupuesto)) {
                resultado.add(actividades.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Actividad> filtrarPorCategoria(String categoria) {
        ArrayList<Actividad> resultado = new ArrayList<Actividad>();
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getCategoria().equals(categoria)) {
                resultado.add(actividades.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Actividad> filtrarPorTipoDestino(String tipoDestino) {
        ArrayList<Actividad> resultado = new ArrayList<Actividad>();
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getTipoDestino().equals(tipoDestino)) {
                resultado.add(actividades.get(i));
            }
        }
        return resultado;
    }

    public ArrayList<Actividad> filtrarInteligente(double presupuesto, String categoria) {
        ArrayList<Actividad> resultado = new ArrayList<Actividad>();
        for (int i = 0; i < actividades.size(); i++) {
            Actividad actividad = actividades.get(i);
            if (actividad.esViable(presupuesto) && actividad.getCategoria().equals(categoria)) {
                resultado.add(actividad);
            }
        }
        return resultado;
    }

    public Actividad buscarPorId(int id) {
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId() == id) {
                return actividades.get(i);
            }
        }
        return null;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }
}