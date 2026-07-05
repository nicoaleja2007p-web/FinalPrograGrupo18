package interfaz;

import modelo.*;
import negocio.*;
import excepciones.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPlanificarViaje extends JPanel {

    private VentanaPrincipal ventana;
    private PanelUsuario panelUsuario;

    private JTabbedPane subTabs;

    private JCheckBox checkAventura, checkCultural, checkGastronomico, checkRelajacion;
    private JLabel labelMsgPref;
    private JTextField campoDestino, campoFechaInicio, campoFechaFin, campoPresupuesto;
    private JComboBox<String> comboPersonas;
    private JComboBox<String> comboTipoDestino;
    private JLabel labelMsgDatos;
    private JTextArea areaItinerario;
    private JTextArea areaReporte;

    private Viaje viajeActual;
    private ArrayList<DiaDeViaje> itinerarioActual;

    private static final Color AZUL  = new Color(41, 98, 160);
    private static final Color VERDE = new Color(34, 139, 80);
    private static final Color FONDO = new Color(245, 246, 252);
    private static final Color BORDE = new Color(200, 212, 232);

    public PanelPlanificarViaje(VentanaPrincipal ventana, PanelUsuario panelUsuario) {
        this.ventana = ventana;
        this.panelUsuario = panelUsuario;
        setLayout(new BorderLayout());
        setBackground(FONDO);
        construirPasos();
    }

    private void construirPasos() {
        subTabs = new JTabbedPane();
        subTabs.setFont(new Font("Arial", Font.PLAIN, 12));

        subTabs.addTab("1. Preferencias", construirPaso1());
        subTabs.addTab("2. Datos del viaje", construirPaso2());
        subTabs.addTab("3. Itinerario", construirPaso3());
        subTabs.addTab("4. Reporte de costos", construirPaso4());

        subTabs.setEnabledAt(1, false);
        subTabs.setEnabledAt(2, false);
        subTabs.setEnabledAt(3, false);

        add(subTabs, BorderLayout.CENTER);
    }

    private JPanel construirPaso1() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);

        JLabel titulo = new JLabel("Elige tus estilos de viaje");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(AZUL);

        checkAventura     = check("Aventura");
        checkCultural     = check("Cultural");
        checkGastronomico = check("Gastronómico");
        checkRelajacion   = check("Relajación");

        JButton btnSiguiente = boton("Siguiente", AZUL, Color.WHITE);
        labelMsgPref = labelMsg();

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        panel.add(titulo, g);

        JLabel sub = new JLabel("Selecciona uno o varios:");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        g.gridy = 1; panel.add(sub, g);

        g.gridwidth = 1; g.gridy = 2; g.anchor = GridBagConstraints.EAST;
        panel.add(checkAventura, g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        panel.add(checkCultural, g);

        g.gridx = 0; g.gridy = 3; g.anchor = GridBagConstraints.EAST;
        panel.add(checkGastronomico, g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        panel.add(checkRelajacion, g);

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        panel.add(labelMsgPref, g);
        g.gridy = 5; panel.add(btnSiguiente, g);

        btnSiguiente.addActionListener(e -> confirmarPreferencias());

        return panel;
    }

    private void confirmarPreferencias() {
        ArrayList<String> estilos = new ArrayList<>();
        if (checkAventura.isSelected())     estilos.add("aventura");
        if (checkCultural.isSelected())     estilos.add("cultural");
        if (checkGastronomico.isSelected()) estilos.add("gastronomia");
        if (checkRelajacion.isSelected())   estilos.add("relajacion");

        if (estilos.size() == 0) {
            labelMsgPref.setText("Selecciona al menos un estilo.");
            return;
        }

        PerfilPreferencias perfil = new PerfilPreferencias(100.0);
        for (int i = 0; i < estilos.size(); i++) {
            perfil.agregarEstilo(estilos.get(i));
            perfil.agregarCategoria(estilos.get(i), 90.0 - (i * 10.0));
        }
        ventana.setPerfilActual(perfil);
        labelMsgPref.setText(" ");

        subTabs.setEnabledAt(1, true);
        subTabs.setSelectedIndex(1);
    }

    private JPanel construirPaso2() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);

        JLabel titulo = new JLabel("Datos del viaje");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(AZUL);

        campoDestino     = new JTextField(18);
        campoFechaInicio = new JTextField(18);
        campoFechaFin    = new JTextField(18);
        campoPresupuesto = new JTextField(18);
        String[] personas = {"1 persona (solo)", "2 personas", "3 personas",
                "4 personas", "5 personas", "6 o mas personas"};
        comboPersonas = new JComboBox<>(personas);
        comboPersonas.setFont(new Font("Arial", Font.PLAIN, 13));

        String[] tipos = {"playa", "ciudad", "montania", "selva", "desierto"};
        comboTipoDestino = new JComboBox<>(tipos);
        comboTipoDestino.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnGenerar  = boton("Generar Itinerario", AZUL, Color.WHITE);
        JButton btnAnterior = boton("← Anterior", new Color(200,200,210), Color.DARK_GRAY);
        labelMsgDatos = labelMsg();

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        panel.add(titulo, g);

        String[] labels = {"Destino:", "Tipo de destino:", "Fecha inicio (dd/mm/aaaa):",
                "Fecha fin (dd/mm/aaaa):", "Presupuesto total ($):", "Viajando con:"};
        Component[] campos = {campoDestino, comboTipoDestino, campoFechaInicio, campoFechaFin, campoPresupuesto, comboPersonas};

        for (int i = 0; i < labels.length; i++) {
            g.gridwidth = 1; g.gridx = 0; g.gridy = i + 1; g.anchor = GridBagConstraints.EAST;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            panel.add(lbl, g);
            g.gridx = 1; g.anchor = GridBagConstraints.WEST;
            panel.add(campos[i], g);
        }

        g.gridx = 0; g.gridy = labels.length + 1; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        panel.add(labelMsgDatos, g);

        g.gridy = labels.length + 2; g.gridwidth = 1; g.anchor = GridBagConstraints.EAST;
        panel.add(btnAnterior, g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        panel.add(btnGenerar, g);

        btnAnterior.addActionListener(e -> subTabs.setSelectedIndex(0));
        btnGenerar.addActionListener(e -> generarItinerario());

        return panel;
    }

    private void generarItinerario() {
        String destino   = campoDestino.getText().trim();
        String fechaIni   = campoFechaInicio.getText().trim();
        String fechaFin   = campoFechaFin.getText().trim();
        String presTexto  = campoPresupuesto.getText().trim();

        if (destino.isEmpty() || fechaIni.isEmpty() || fechaFin.isEmpty() || presTexto.isEmpty()) {
            labelMsgDatos.setText("Error: Todos los campos son obligatorios.");
            return;
        }
        if (!destino.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            labelMsgDatos.setText("Error: El destino no debe contener numeros ni simbolos.");
            return;
        }
        if (!fechaDesdeJulio2026(fechaIni)) {
            labelMsgDatos.setText("Error: La fecha de inicio debe ser a partir de julio 2026.");
            return;
        }

        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presTexto);
        } catch (NumberFormatException ex) {
            labelMsgDatos.setText("Error: El presupuesto debe ser un numero valido.");
            return;
        }
        if (presupuesto <= 0) {
            labelMsgDatos.setText("Error: El presupuesto debe ser mayor a cero.");
            return;
        }

        ArrayList<String> fechas = generarFechas(fechaIni, fechaFin);
        if (fechas.size() == 0) return;

        int dur = fechas.size();

        int personas = comboPersonas.getSelectedIndex() + 1;
        if (personas > 5) personas = 6;

        double minimoBase;
        if (dur <= 5) {
            minimoBase = 300;
        } else if (dur <= 10) {
            minimoBase = 600;
        } else {
            minimoBase = 1000;
        }

        double minimoRequerido = minimoBase * personas;

        if (presupuesto < minimoRequerido) {
            String rango = dur <= 5 ? "1-5 dias" : dur <= 10 ? "6-10 dias" : "mas de 10 dias";
            labelMsgDatos.setText("Error: Para " + rango + " con " + personas + " persona(s), el minimo es $" + String.format("%.2f", minimoRequerido) + ".");
            return;
        }

        String tipoDestino = (String) comboTipoDestino.getSelectedItem();

        PerfilPreferencias perfil = ventana.getPerfilActual();
        viajeActual = new Viaje(
                ventana.getGestorUsuario().obtenerHistorial().obtenerViajes().size() + 1,
                destino, fechaIni, fechaFin, presupuesto);
        viajeActual.setNumeroPersonas(personas);
        viajeActual.setTipoDestino(tipoDestino);

        Planificador planificador = new Planificador(
                viajeActual, ventana.getGestorActividades().getActividades(), perfil);
        try {
            planificador.ajustarPresupuesto(presupuesto);
        } catch (PresupuestoExcedidoException ex) {
            labelMsgDatos.setText("Error: " + ex.getMessage());
            return;
        }

        itinerarioActual = planificador.generarItinerario(fechas);
        ventana.getGestorUsuario().agregarViajeAlHistorial(viajeActual);

        mostrarItinerario();
        labelMsgDatos.setText(" ");
        subTabs.setEnabledAt(2, true);
        subTabs.setSelectedIndex(2);
    }

    private JPanel construirPaso3() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaItinerario = new JTextArea();
        areaItinerario.setEditable(false);
        areaItinerario.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaItinerario.setBackground(Color.WHITE);
        areaItinerario.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaItinerario);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridBagLayout());
        botones.setBackground(FONDO);
        botones.setBorder(new EmptyBorder(8, 0, 0, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);

        JButton btnReporte  = boton("Ver Reporte de Costos →", AZUL, Color.WHITE);
        JButton btnAnterior = boton("← Volver a datos", new Color(200,200,210), Color.DARK_GRAY);

        g.gridx = 0; g.gridy = 0; botones.add(btnAnterior, g);
        g.gridx = 1;              botones.add(btnReporte, g);
        panel.add(botones, BorderLayout.SOUTH);

        btnAnterior.addActionListener(e -> subTabs.setSelectedIndex(1));
        btnReporte.addActionListener(e -> {
            if (viajeActual != null) {
                mostrarReporte();
                subTabs.setEnabledAt(3, true);
                subTabs.setSelectedIndex(3);
            }
        });

        return panel;
    }

    private void mostrarItinerario() {
        StringBuilder sb = new StringBuilder();
        sb.append("Viaje a: ").append(viajeActual.getDestino().toUpperCase()).append("\n");
        sb.append("Tipo de destino: ").append(viajeActual.getTipoDestino()).append("\n");
        sb.append("Fecha: ").append(viajeActual.getFechaInicio()).append(" al ").append(viajeActual.getFechaFin()).append("\n");
        sb.append("Personas: ").append(viajeActual.getNumeroPersonas()).append("\n");
        sb.append("Presupuesto: $").append(String.format("%.2f", viajeActual.getPresupuesto())).append("\n");
        sb.append("==========================================\n\n");

        for (int i = 0; i < itinerarioActual.size(); i++) {
            DiaDeViaje dia = itinerarioActual.get(i);
            sb.append("Dia ").append(i + 1).append(" - ").append(dia.getFecha()).append("\n");
            sb.append("  Costo: $").append(String.format("%.2f", dia.getCostoDelDia()))
                    .append("  |  Duracion: ").append(dia.getDuracionTotal()).append("h\n");
            for (Actividad a : dia.getActividades()) {
                sb.append("    - ").append(a.getNombre())
                        .append(" ($").append(String.format("%.2f", a.getCosto())).append(")\n");
            }
            sb.append("\n");
        }
        sb.append("==========================================\n");
        sb.append("Costo Total: $").append(String.format("%.2f", viajeActual.getCostoTotal())).append("\n");
        sb.append("Presupuesto: $").append(String.format("%.2f", viajeActual.getPresupuesto())).append("\n");

        areaItinerario.setText(sb.toString());
        areaItinerario.setCaretPosition(0);
    }

    private JPanel construirPaso4() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaReporte.setBackground(Color.WHITE);
        areaReporte.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaReporte);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        botones.setBackground(FONDO);

        JButton btnMisViajes = boton("Ir a Mis Viajes", VERDE, Color.WHITE);
        JButton btnNuevo     = boton("Planificar otro viaje", new Color(200,200,210), Color.DARK_GRAY);

        botones.add(btnNuevo);
        botones.add(btnMisViajes);
        panel.add(botones, BorderLayout.SOUTH);

        btnMisViajes.addActionListener(e -> panelUsuario.irAMisViajes());
        btnNuevo.addActionListener(e -> reiniciar());

        return panel;
    }

    private void mostrarReporte() {
        GestorReporte gr = new GestorReporte(viajeActual);
        double gastoDiario = ventana.getPerfilActual().getGastoDiarioMax();
        ArrayList<String> estilos = ventana.getPerfilActual().getEstilosViaje();

        StringBuilder sb = new StringBuilder();
        sb.append(gr.resumenGeneral()).append("\n");

        try {
            ArrayList<String> excesos = gr.detectarExcesos(gastoDiario);
            if (excesos.size() == 0) {
                sb.append("No se detectaron excesos de presupuesto por dia.\n");
            } else {
                sb.append("=== ALERTAS DE EXCESO ===\n");
                for (String e : excesos) sb.append(e).append("\n");
            }
        } catch (PresupuestoExcedidoException ex) {
            sb.append("Error: ").append(ex.getMessage()).append("\n");
        }

        ArrayList<String> alts = gr.sugerirAlternativas(gastoDiario,
                ventana.getGestorActividades().getActividades(), estilos, viajeActual.getTipoDestino());
        if (alts.size() > 0) {
            sb.append("\n=== ALTERNATIVAS ECONOMICAS ===\n");
            for (String a : alts) sb.append(a).append("\n");
        }

        areaReporte.setText(sb.toString());
        areaReporte.setCaretPosition(0);
    }

    private boolean fechaDesdeJulio2026(String fecha) {
        try {
            String[] p = fecha.split("/");
            if (p.length != 3) return false;
            int mes = Integer.parseInt(p[1]);
            int anio = Integer.parseInt(p[2]);
            if (anio > 2026) return true;
            return anio == 2026 && mes >= 7;
        } catch (Exception ex) { return false; }
    }

    private ArrayList<String> generarFechas(String inicio, String fin) {
        ArrayList<String> fechas = new ArrayList<>();
        try {
            String[] pI = inicio.split("/"), pF = fin.split("/");
            if (pI.length != 3 || pF.length != 3) return fechas;

            int dI = Integer.parseInt(pI[0]), mI = Integer.parseInt(pI[1]), aI = Integer.parseInt(pI[2]);
            int dF = Integer.parseInt(pF[0]), mF = Integer.parseInt(pF[1]), aF = Integer.parseInt(pF[2]);

            if (dI < 1 || dI > 31 || dF < 1 || dF > 31) return fechas;
            if (mI < 1 || mI > 12 || mF < 1 || mF > 12) return fechas;

            int tI = aI * 10000 + mI * 100 + dI;
            int tF = aF * 10000 + mF * 100 + dF;

            if (tF <= tI) {
                labelMsgDatos.setText("Error: La fecha fin debe ser posterior a la fecha inicio.");
                return fechas;
            }

            int[] dpm = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int d = dI, m = mI, a = aI, cnt = 0;
            while (cnt < 60) {
                fechas.add(d + "/" + m + "/" + a);
                if (d == dF && m == mF && a == aF) break;
                d++;
                if (d > dpm[m]) { d = 1; m++; }
                if (m > 12) { m = 1; a++; }
                cnt++;
            }
        } catch (NumberFormatException ex) { return new ArrayList<>(); }
        return fechas;
    }

    public void reiniciar() {
        campoDestino.setText("");
        campoFechaInicio.setText("");
        campoFechaFin.setText("");
        campoPresupuesto.setText("");
        comboPersonas.setSelectedIndex(0);
        comboTipoDestino.setSelectedIndex(0);
        checkAventura.setSelected(false);
        checkCultural.setSelected(false);
        checkGastronomico.setSelected(false);
        checkRelajacion.setSelected(false);
        labelMsgPref.setText(" ");
        labelMsgDatos.setText(" ");
        areaItinerario.setText("");
        areaReporte.setText("");
        subTabs.setEnabledAt(1, false);
        subTabs.setEnabledAt(2, false);
        subTabs.setEnabledAt(3, false);
        subTabs.setSelectedIndex(0);
        viajeActual = null;
        itinerarioActual = null;
    }

    private JButton boton(String texto, Color fondo, Color letra) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBackground(fondo);
        b.setForeground(letra);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        return b;
    }

    private JCheckBox check(String texto) {
        JCheckBox cb = new JCheckBox(texto);
        cb.setFont(new Font("Arial", Font.PLAIN, 13));
        cb.setBackground(FONDO);
        return cb;
    }

    private JLabel labelMsg() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("Arial", Font.ITALIC, 12));
        l.setForeground(new Color(180, 50, 50));
        return l;
    }
}