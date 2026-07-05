package modelo;

import java.util.ArrayList;

public class PerfilPreferencias {

    private ArrayList<String> categorias;
    private ArrayList<String> estilosViaje;
    private double gastoDiarioMax;
    private ArrayList<Double> puntajesAfinidad;

    public PerfilPreferencias(double gastoDiarioMax) {
        this.gastoDiarioMax = gastoDiarioMax;
        this.categorias = new ArrayList<String>();
        this.estilosViaje = new ArrayList<String>();
        this.puntajesAfinidad = new ArrayList<Double>();
    }

    public void agregarEstilo(String estilo) {
        if (!estilosViaje.contains(estilo)) {
            estilosViaje.add(estilo);
        }
    }

    public void agregarCategoria(String categoria, double puntaje) {
        categorias.add(categoria);
        puntajesAfinidad.add(puntaje);
    }

    public double calcularAfinidad(String categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).equals(categoria)) {
                return puntajesAfinidad.get(i);
            }
        }
        return 0.0;
    }

    public void actualizarPerfil(ArrayList<String> nuevosEstilos, double nuevoGasto) {
        this.estilosViaje = nuevosEstilos;
        this.gastoDiarioMax = nuevoGasto;
        this.categorias = new ArrayList<String>();
        this.puntajesAfinidad = new ArrayList<Double>();
    }

    public ArrayList<String> obtenerCategorias() {
        return categorias;
    }

    public ArrayList<String> getEstilosViaje() {
        return estilosViaje;
    }

    public double getGastoDiarioMax() {
        return gastoDiarioMax;
    }

    public void setGastoDiarioMax(double gastoDiarioMax) {
        this.gastoDiarioMax = gastoDiarioMax;
    }

    public ArrayList<Double> getPuntajesAfinidad() {
        return puntajesAfinidad;
    }

    public String toString() {
        return "PerfilPreferencias [estilos=" + estilosViaje + ", gastoDiarioMax=" + gastoDiarioMax + "]";
    }
}