package negocio;

import modelo.Actividad;
import modelo.DiaDeViaje;
import modelo.PerfilPreferencias;
import modelo.Viaje;
import excepciones.PresupuestoExcedidoException;
import java.util.ArrayList;

public class Planificador {

    private Viaje viaje;
    private ArrayList<Actividad> actividades;
    private PerfilPreferencias perfil;

    public Planificador(Viaje viaje, ArrayList<Actividad> actividades, PerfilPreferencias perfil) {
        this.viaje = viaje;
        this.actividades = actividades;
        this.perfil = perfil;
    }

    public static double factorPorTipoDestino(String tipoDestino) {
        if (tipoDestino == null) { return 1.0; }
        switch (tipoDestino) {
            case "playa":    return 1.4;
            case "ciudad":   return 1.6;
            case "montania": return 1.0;
            case "selva":    return 0.9;
            case "desierto": return 1.1;
            default:         return 1.0;
        }
    }

    public ArrayList<DiaDeViaje> generarItinerario(ArrayList<String> fechas) {
        ArrayList<DiaDeViaje> itinerario = new ArrayList<DiaDeViaje>();

        double gastoDiario = perfil.getGastoDiarioMax();
        double presupuestoTotal = viaje.getPresupuesto();
        double gastoAcumuladoViaje = 0.0;

        ArrayList<Actividad> disponibles = filtrarActividades(gastoDiario);

        if (disponibles.size() == 0) {
            for (int i = 0; i < fechas.size(); i++) {
                DiaDeViaje dia = new DiaDeViaje(fechas.get(i));
                itinerario.add(dia);
                viaje.agregarDia(dia);
            }
            return itinerario;
        }

        int indiceInicio = 0;

        for (int i = 0; i < fechas.size(); i++) {
            DiaDeViaje dia = new DiaDeViaje(fechas.get(i));
            double gastoAcumuladoDia = 0.0;
            int activadesAgregadas = 0;
            int intentos = 0;
            int j = indiceInicio;

            double restanteTotal = presupuestoTotal - gastoAcumuladoViaje;
            double limiteDelDia = Math.min(gastoDiario, restanteTotal);

            while (intentos < disponibles.size() && limiteDelDia > 0) {
                if (j >= disponibles.size()) {
                    j = 0;
                }
                Actividad actividad = disponibles.get(j);
                if (gastoAcumuladoDia + actividad.getCosto() <= limiteDelDia) {
                    dia.agregarActividad(actividad);
                    gastoAcumuladoDia = gastoAcumuladoDia + actividad.getCosto();
                    activadesAgregadas = activadesAgregadas + 1;
                    if (activadesAgregadas >= 2) {
                        indiceInicio = j + 1;
                        if (indiceInicio >= disponibles.size()) {
                            indiceInicio = 0;
                        }
                        break;
                    }
                }
                j = j + 1;
                intentos = intentos + 1;
            }

            if (activadesAgregadas == 0 && limiteDelDia > 0) {
                Actividad candidata = disponibles.get(indiceInicio % disponibles.size());
                if (candidata.getCosto() <= limiteDelDia) {
                    dia.agregarActividad(candidata);
                    gastoAcumuladoDia = gastoAcumuladoDia + candidata.getCosto();
                }
                indiceInicio = indiceInicio + 1;
                if (indiceInicio >= disponibles.size()) {
                    indiceInicio = 0;
                }
            }
            gastoAcumuladoViaje = gastoAcumuladoViaje + gastoAcumuladoDia;
            itinerario.add(dia);
            viaje.agregarDia(dia);
        }
        return itinerario;
    }

    public void ajustarPresupuesto(double nuevoPresupuesto) throws PresupuestoExcedidoException {
        if (nuevoPresupuesto <= 0) {
            throw new PresupuestoExcedidoException("El presupuesto debe ser mayor a cero.");
        }
        viaje.setPresupuesto(nuevoPresupuesto);
        perfil.setGastoDiarioMax(nuevoPresupuesto / viaje.calcularDuracion());
        System.out.println("Presupuesto ajustado a: $" + nuevoPresupuesto);
    }

    public ArrayList<Actividad> filtrarActividades(double presupuestoDiario) {
        ArrayList<Actividad> filtradas = new ArrayList<Actividad>();
        ArrayList<String> estilos = perfil.getEstilosViaje();
        String tipoDestino = viaje.getTipoDestino();
        double factor = factorPorTipoDestino(tipoDestino);

        for (int i = 0; i < actividades.size(); i++) {
            Actividad act = actividades.get(i);
            boolean coincideEstilo = estilos.contains(act.getCategoria());
            boolean coincideTipo = tipoDestino == null || act.getTipoDestino().equals(tipoDestino);

            if (coincideEstilo && coincideTipo) {
                Actividad ajustada = act.clonarConCostoAjustado(factor);
                if (ajustada.esViable(presupuestoDiario)) {
                    filtradas.add(ajustada);
                }
            }
        }
        return filtradas;
    }

    public boolean validarHorarios(DiaDeViaje dia) {
        int duracionTotal = dia.getDuracionTotal();
        if (duracionTotal <= 12) {
            return true;
        }
        System.out.println("Advertencia: El dia " + dia.getFecha() + " supera 12 horas de actividades.");
        return false;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public PerfilPreferencias getPerfil() {
        return perfil;
    }
}