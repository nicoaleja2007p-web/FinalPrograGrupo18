package modelo;

public class Actividad {

    private int id;
    private String nombre;
    private double costo;
    private int duracion;
    private String categoria;
    private String tipoDestino;

    public Actividad(int id, String nombre, double costo, int duracion, String categoria, String tipoDestino) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.duracion = duracion;
        this.categoria = categoria;
        this.tipoDestino = tipoDestino;
    }

    public boolean esViable(double presupuesto) {
        if (costo <= presupuesto) {
            return true;
        }
        return false;
    }

    public Actividad clonarConCostoAjustado(double factor) {
        double nuevoCosto = Math.round(this.costo * factor * 100.0) / 100.0;
        return new Actividad(this.id, this.nombre, nuevoCosto, this.duracion, this.categoria, this.tipoDestino);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getTipoDestino() { return tipoDestino; }
    public void setTipoDestino(String tipoDestino) { this.tipoDestino = tipoDestino; }

    public String toString() {
        return nombre + " | $" + costo + " | " + duracion + "h | " + categoria;
    }

    public String descripcionSimple() {
        return nombre + " | $" + costo;
    }
}