package modelo;

import java.util.ArrayList;

public class Viaje {

    private int id;
    private String destino;
    private String fechaInicio;
    private String fechaFin;
    private double presupuesto;
    private ArrayList<DiaDeViaje> dias;
    private boolean confirmado;
    private int numeroPersonas;
    private String tipoDestino;

    public Viaje(int id, String destino, String fechaInicio, String fechaFin, double presupuesto) {
        this.id = id;
        this.destino = destino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.presupuesto = presupuesto;
        this.dias = new ArrayList<DiaDeViaje>();
        this.confirmado = false;
        this.numeroPersonas = 1;
        this.tipoDestino = "ciudad";
    }

    public void agregarDia(DiaDeViaje dia) {
        dias.add(dia);
    }

    public int calcularDuracion() {
        return dias.size();
    }

    public double getCostoTotal() {
        double total = 0.0;
        for (int i = 0; i < dias.size(); i++) {
            total = total + dias.get(i).getCostoDelDia();
        }
        return total * numeroPersonas;
    }

    public boolean yaOcurrio(int diaHoy, int mesHoy, int anioHoy) {
        try {
            String[] partes = fechaInicio.split("/");
            int diaViaje = Integer.parseInt(partes[0]);
            int mesViaje = Integer.parseInt(partes[1]);
            int anioViaje = Integer.parseInt(partes[2]);

            int totalHoy = anioHoy * 10000 + mesHoy * 100 + diaHoy;
            int totalViaje = anioViaje * 10000 + mesViaje * 100 + diaViaje;

            return totalViaje < totalHoy;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public ArrayList<DiaDeViaje> getDias() { return dias; }

    public boolean isConfirmado() { return confirmado; }
    public void setConfirmado(boolean confirmado) { this.confirmado = confirmado; }

    public int getNumeroPersonas() { return numeroPersonas; }
    public void setNumeroPersonas(int numeroPersonas) { this.numeroPersonas = numeroPersonas; }

    public String getTipoDestino() { return tipoDestino; }
    public void setTipoDestino(String tipoDestino) { this.tipoDestino = tipoDestino; }

    public String toString() {
        return "Viaje a " + destino + " | " + fechaInicio + " - " + fechaFin + " | $" + presupuesto + " | " + numeroPersonas + " persona(s)";
    }
}