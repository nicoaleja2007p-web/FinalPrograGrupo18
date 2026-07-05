package modelo;

import java.util.ArrayList;

public class HistorialViaje {

    private ArrayList<Viaje> viajes;
    private double gastoTotal;

    public HistorialViaje() {
        this.viajes = new ArrayList<Viaje>();
        this.gastoTotal = 0.0;
    }

    public void agregarViaje(Viaje viaje) {
        viajes.add(viaje);
        gastoTotal = gastoTotal + viaje.getCostoTotal();
    }

    public double calcularGastoTotal() {
        gastoTotal = 0.0;
        for (int i = 0; i < viajes.size(); i++) {
            gastoTotal = gastoTotal + viajes.get(i).getCostoTotal();
        }
        return gastoTotal;
    }

    public ArrayList<Viaje> obtenerViajes() {
        return viajes;
    }

    public String toString() {
        return "HistorialViaje [viajes=" + viajes.size() + ", gastoTotal=$" + calcularGastoTotal() + "]";
    }
}