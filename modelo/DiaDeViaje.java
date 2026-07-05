package modelo;

import java.util.ArrayList;

public class DiaDeViaje {

    private String fecha;
    private ArrayList<Actividad> actividades;
    private double costoDelDia;

    public DiaDeViaje(String fecha) {
        this.fecha = fecha;
        this.actividades = new ArrayList<Actividad>();
        this.costoDelDia = 0.0;
    }

    public void agregarActividad(Actividad actividad) {
        actividades.add(actividad);
        costoDelDia = costoDelDia + actividad.getCosto();
    }

    public double getCostoDelDia() {
        costoDelDia = 0.0;
        for (int i = 0; i < actividades.size(); i++) {
            costoDelDia = costoDelDia + actividades.get(i).getCosto();
        }
        return costoDelDia;
    }

    public int getDuracionTotal() {
        int total = 0;
        for (int i = 0; i < actividades.size(); i++) {
            total = total + actividades.get(i).getDuracion();
        }
        return total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public String toString() {
        return "Dia: " + fecha + " | Costo: $" + getCostoDelDia() + " | Duración: " + getDuracionTotal() + "h";
    }
}
