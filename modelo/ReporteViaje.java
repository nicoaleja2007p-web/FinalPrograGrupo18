package modelo;

import java.util.ArrayList;

public class ReporteViaje {

    private ArrayList<DiaDeViaje> itinerario;
    private double costoTotal;
    private ArrayList<String> alertas;
    private int numeroPersonas;

    public ReporteViaje(ArrayList<DiaDeViaje> itinerario, int numeroPersonas) {
        this.itinerario = itinerario;
        this.alertas = new ArrayList<String>();
        this.costoTotal = 0.0;
        this.numeroPersonas = numeroPersonas;
    }

    public String generarDesglose() {
        String desglose = "     Desglose de Viaje        \n";
        for (int i = 0; i < itinerario.size(); i++) {
            DiaDeViaje dia = itinerario.get(i);
            double costoDiaGrupo = dia.getCostoDelDia() * numeroPersonas;
            desglose = desglose + "Dia " + (i + 1) + " (" + dia.getFecha() + "): $" + String.format("%.2f", costoDiaGrupo) + "\n";
            ArrayList<Actividad> actividades = dia.getActividades();
            for (int j = 0; j < actividades.size(); j++) {
                desglose = desglose + "   - " + actividades.get(j).descripcionSimple() + "\n";
            }
        }
        desglose = desglose + "Total: $" + String.format("%.2f", getCostoTotal());
        return desglose;
    }

    public ArrayList<String> detectarExcesos(double gastoDiarioMax) {
        alertas = new ArrayList<String>();
        for (int i = 0; i < itinerario.size(); i++) {
            double costoDia = itinerario.get(i).getCostoDelDia() * numeroPersonas;
            if (costoDia > gastoDiarioMax) {
                alertas.add("Dia " + (i + 1) + " (" + itinerario.get(i).getFecha() + ") excede el limite: $" + String.format("%.2f", costoDia) + " > $" + String.format("%.2f", gastoDiarioMax));
            }
        }
        return alertas;
    }

    public ArrayList<String> sugerirAlternativas(double gastoDiarioMax, ArrayList<Actividad> todasActividades,
                                                 ArrayList<String> estilosPermitidos) {
        ArrayList<String> sugerencias = new ArrayList<String>();
        for (int i = 0; i < todasActividades.size(); i++) {
            Actividad actividad = todasActividades.get(i);
            boolean categoriaPermitida = estilosPermitidos == null || estilosPermitidos.size() == 0
                    || estilosPermitidos.contains(actividad.getCategoria());
            if (actividad.getCosto() <= gastoDiarioMax && categoriaPermitida) {
                sugerencias.add("Alternativa económica: " + actividad.toString());
            }
        }
        return sugerencias;
    }

    public void exportarReporte() {
        System.out.println(generarDesglose());
        System.out.println("Alertas: " + alertas.size());
        for (int i = 0; i < alertas.size(); i++) {
            System.out.println(alertas.get(i));
        }
    }

    public double getCostoTotal() {
        costoTotal = 0.0;
        for (int i = 0; i < itinerario.size(); i++) {
            costoTotal = costoTotal + itinerario.get(i).getCostoDelDia();
        }
        costoTotal = costoTotal * numeroPersonas;
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public ArrayList<DiaDeViaje> getItinerario() {
        return itinerario;
    }

    public ArrayList<String> getAlertas() {
        return alertas;
    }
}