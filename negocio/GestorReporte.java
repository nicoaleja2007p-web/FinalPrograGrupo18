package negocio;

import modelo.Actividad;
import modelo.ReporteViaje;
import modelo.Viaje;
import excepciones.PresupuestoExcedidoException;
import java.util.ArrayList;

public class GestorReporte {

    private Viaje viaje;

    public GestorReporte(Viaje viaje) {
        this.viaje = viaje;
    }

    public ReporteViaje generarReporte() {
        ReporteViaje reporte = new ReporteViaje(viaje.getDias(), viaje.getNumeroPersonas());
        return reporte;
    }

    public String generarDesglose() {
        ReporteViaje reporte = generarReporte();
        return reporte.generarDesglose();
    }

    public ArrayList<String> detectarExcesos(double gastoDiarioMax) throws PresupuestoExcedidoException {
        if (gastoDiarioMax <= 0) {
            throw new PresupuestoExcedidoException("El limite de gasto diario debe ser mayor a cero.");
        }
        ReporteViaje reporte = generarReporte();
        return reporte.detectarExcesos(gastoDiarioMax);
    }

    public ArrayList<String> sugerirAlternativas(double gastoDiarioMax, ArrayList<Actividad> todasActividades,
                                                 ArrayList<String> estilosPermitidos, String tipoDestino) {
        if (!viajeExcedido()) {
            return new ArrayList<String>();
        }

        double factor = Planificador.factorPorTipoDestino(tipoDestino);
        ArrayList<Actividad> ajustadas = new ArrayList<Actividad>();
        for (int i = 0; i < todasActividades.size(); i++) {
            Actividad act = todasActividades.get(i);
            boolean coincideTipo = tipoDestino == null || act.getTipoDestino().equals(tipoDestino);
            if (coincideTipo) {
                ajustadas.add(act.clonarConCostoAjustado(factor));
            }
        }

        ReporteViaje reporte = generarReporte();
        return reporte.sugerirAlternativas(gastoDiarioMax, ajustadas, estilosPermitidos);
    }

    public boolean viajeExcedido() {
        ReporteViaje reporte = generarReporte();
        if (reporte.getCostoTotal() > viaje.getPresupuesto()) {
            return true;
        }
        return false;
    }

    public String resumenGeneral() {
        ReporteViaje reporte = generarReporte();
        String resumen = " Resumen del Viaje \n";
        resumen = resumen + "Destino: " + viaje.getDestino() + "\n";
        resumen = resumen + "Duración: " + viaje.calcularDuracion() + " dias\n";
        resumen = resumen + "Personas: " + viaje.getNumeroPersonas() + "\n";
        resumen = resumen + "Presupuesto: $" + String.format("%.2f", viaje.getPresupuesto()) + "\n";
        resumen = resumen + "Costo Total: $" + String.format("%.2f", reporte.getCostoTotal()) + "\n";
        resumen = resumen + "Estado: " + (viajeExcedido() ? "ALERTA - El costo total supera el presupuesto." : "Itinerario generado dentro del presupuesto.") + "\n";
        return resumen;
    }

    public Viaje getViaje() {
        return viaje;
    }
    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
}
