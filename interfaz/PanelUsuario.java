package interfaz;

import modelo.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelUsuario extends JPanel {

    private VentanaPrincipal ventana;
    private JTabbedPane tabs;
    private PanelPlanificarViaje panelPlanificar;
    private JTextArea areaViajes;
    private JLabel labelMsgViajes;
    private JTextArea areaHistorial;

    private static final Color AZUL  = new Color(41, 98, 160);
    private static final Color AZUL_CLARO = new Color(230, 241, 255);
    private static final Color VERDE = new Color(34, 139, 80);
    private static final Color ROJO  = new Color(180, 50, 50);
    private static final Color FONDO = new Color(245, 246, 252);
    private static final Color BORDE = new Color(200, 212, 232);

    public PanelUsuario(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new BorderLayout());
        setBackground(FONDO);
        construirEncabezado();
        construirTabs();
    }

    private void construirEncabezado() {
        JPanel enc = new JPanel(new BorderLayout());
        enc.setBackground(AZUL);
        enc.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel titulo = new JLabel(" Planificador de Viajes");
        titulo.setFont(new Font("Arial", Font.BOLD, 17));
        titulo.setForeground(Color.WHITE);

        JButton btnSalir = new JButton("Cerrar Sesion");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.setBackground(ROJO);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            ventana.getGestorUsuario().logout();
            ventana.mostrarPanel("login");
        });

        enc.add(titulo,   BorderLayout.WEST);
        enc.add(btnSalir, BorderLayout.EAST);
        add(enc, BorderLayout.NORTH);
    }

    private void construirTabs() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));

        panelPlanificar = new PanelPlanificarViaje(ventana, this);

        tabs.addTab("  Mis Viajes  ", construirPestanaMisViajes());
        tabs.addTab("  Planificar  ", panelPlanificar);
        tabs.addTab("  Historial   ", construirPestanaHistorial());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel construirPestanaMisViajes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaViajes = new JTextArea();
        areaViajes.setEditable(false);
        areaViajes.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaViajes.setBackground(Color.WHITE);
        areaViajes.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaViajes);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridBagLayout());
        botones.setBackground(FONDO);
        botones.setBorder(new EmptyBorder(8, 0, 0, 0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);

        JButton botonEditarAct = boton("Editar actividad de viaje futuro", AZUL_CLARO, AZUL);
        JButton botonConfirmar = boton("Confirmar viaje", VERDE, Color.WHITE);
        labelMsgViajes = labelMsg();

        g.gridx = 0; g.gridy = 0; botones.add(botonEditarAct, g);
        g.gridx = 1;              botones.add(botonConfirmar, g);
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        botones.add(labelMsgViajes, g);

        panel.add(botones, BorderLayout.SOUTH);

        botonEditarAct.addActionListener(e -> manejarEditarActividad());
        botonConfirmar.addActionListener(e -> manejarConfirmarViaje());

        return panel;
    }

    private JPanel construirPestanaHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaHistorial.setBackground(Color.WHITE);
        areaHistorial.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void manejarEditarActividad() {
        ArrayList<Viaje> viajes = ventana.getGestorUsuario().obtenerHistorial().obtenerViajes();
        ArrayList<Viaje> futuros = new ArrayList<>();
        for (Viaje v : viajes) {
            if (!v.yaOcurrio(4, 7, 2026)) futuros.add(v);
        }

        if (futuros.size() == 0) {
            labelMsgViajes.setText("No tienes viajes futuros para editar.");
            return;
        }

        String[] nombresViajes = new String[futuros.size()];
        for (int i = 0; i < futuros.size(); i++) {
            String estado = futuros.get(i).isConfirmado() ? " [CONFIRMADO]" : "";
            nombresViajes[i] = futuros.get(i).getDestino() + " (" + futuros.get(i).getFechaInicio() + ")" + estado;
        }

        String viajeEleg = (String) JOptionPane.showInputDialog(ventana,
                "Selecciona el viaje:", "Editar actividad",
                JOptionPane.PLAIN_MESSAGE, null, nombresViajes, nombresViajes[0]);
        if (viajeEleg == null) return;

        int idxViaje = 0;
        for (int i = 0; i < nombresViajes.length; i++) {
            if (nombresViajes[i].equals(viajeEleg)) { idxViaje = i; break; }
        }

        Viaje viajeSeleccionado = futuros.get(idxViaje);

        if (viajeSeleccionado.isConfirmado()) {
            JOptionPane.showMessageDialog(ventana,
                    "Este viaje ya fue confirmado y no se pueden modificar sus actividades.",
                    "Viaje confirmado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        continuarEdicionActividad(viajeSeleccionado);
    }

    private void continuarEdicionActividad(Viaje viaje) {
        ArrayList<DiaDeViaje> dias = viaje.getDias();

        String[] nombresDias = new String[dias.size()];
        for (int i = 0; i < dias.size(); i++) {
            nombresDias[i] = "Dia " + (i + 1) + " - " + dias.get(i).getFecha();
        }

        String diaEleg = (String) JOptionPane.showInputDialog(ventana,
                "Selecciona el dia:", "Editar actividad",
                JOptionPane.PLAIN_MESSAGE, null, nombresDias, nombresDias[0]);
        if (diaEleg == null) return;

        int idxDia = 0;
        for (int i = 0; i < nombresDias.length; i++) {
            if (nombresDias[i].equals(diaEleg)) { idxDia = i; break; }
        }

        DiaDeViaje dia = dias.get(idxDia);
        ArrayList<Actividad> acts = dia.getActividades();
        if (acts.size() == 0) {
            JOptionPane.showMessageDialog(ventana, "Este dia no tiene actividades.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombresActs = new String[acts.size()];
        for (int i = 0; i < acts.size(); i++) {
            nombresActs[i] = acts.get(i).getNombre() + " ($" + acts.get(i).getCosto() + ")";
        }

        String actEleg = (String) JOptionPane.showInputDialog(ventana,
                "Selecciona la actividad a cambiar:", "Editar actividad",
                JOptionPane.PLAIN_MESSAGE, null, nombresActs, nombresActs[0]);
        if (actEleg == null) return;

        int idxAct = 0;
        for (int i = 0; i < nombresActs.length; i++) {
            if (nombresActs[i].equals(actEleg)) { idxAct = i; break; }
        }

        Actividad actActual = acts.get(idxAct);
        double disponible = viaje.getPresupuesto() - viaje.getCostoTotal() + actActual.getCosto();

        ArrayList<Actividad> todas = ventana.getGestorActividades().getActividades();
        ArrayList<String> optsStr = new ArrayList<>();
        ArrayList<Actividad> alts = new ArrayList<>();

        for (Actividad c : todas) {
            if (c.getCosto() <= disponible
                    && !c.getNombre().equals(actActual.getNombre())
                    && c.getCategoria().equals(actActual.getCategoria())) {
                optsStr.add(c.getNombre() + " ($" + c.getCosto() + ")");
                alts.add(c);
            }
        }

        if (optsStr.size() == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay alternativas dentro del presupuesto.", "Sin opciones", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] optsArr = optsStr.toArray(new String[0]);
        String nueva = (String) JOptionPane.showInputDialog(ventana,
                "Selecciona la nueva actividad:", "Editar actividad",
                JOptionPane.PLAIN_MESSAGE, null, optsArr, optsArr[0]);
        if (nueva == null) return;

        int idxNueva = 0;
        for (int i = 0; i < optsArr.length; i++) {
            if (optsArr[i].equals(nueva)) { idxNueva = i; break; }
        }

        acts.set(idxAct, alts.get(idxNueva));
        labelMsgViajes.setText("Actividad cambiada exitosamente.");
        actualizarMisViajes();
    }

    private void manejarConfirmarViaje() {
        ArrayList<Viaje> viajes = ventana.getGestorUsuario().obtenerHistorial().obtenerViajes();
        ArrayList<Viaje> sinConf = new ArrayList<>();
        for (Viaje v : viajes) {
            if (!v.isConfirmado()) sinConf.add(v);
        }

        if (sinConf.size() == 0) {
            labelMsgViajes.setText("Todos tus viajes ya estan confirmados.");
            return;
        }

        String[] nombres = new String[sinConf.size()];
        for (int i = 0; i < sinConf.size(); i++) {
            nombres[i] = sinConf.get(i).getDestino() + " (" + sinConf.get(i).getFechaInicio() + ")";
        }

        String eleg = (String) JOptionPane.showInputDialog(ventana,
                "Selecciona el viaje a confirmar:", "Confirmar viaje",
                JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (eleg == null) return;

        int idx = 0;
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equals(eleg)) { idx = i; break; }
        }

        Viaje v = sinConf.get(idx);
        int op = JOptionPane.showConfirmDialog(ventana,
                "¿Confirmas que realizaras el viaje a " + v.getDestino() + "?\n" +
                        "Fechas: " + v.getFechaInicio() + " al " + v.getFechaFin() + "\n" +
                        "Personas: " + v.getNumeroPersonas() + "\n" +
                        "Costo total: $" + v.getCostoTotal() + "\n\n" +
                        "Una vez confirmado no podras editar las actividades de este viaje.",
                "Confirmar viaje", JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            v.setConfirmado(true);
            labelMsgViajes.setText("Viaje a " + v.getDestino() + " confirmado.");
            actualizarMisViajes();
        }
    }

    public void irAMisViajes() {
        actualizar();
        panelPlanificar.reiniciar();
        tabs.setSelectedIndex(0);
    }

    public void actualizar() {
        actualizarMisViajes();
        actualizarHistorial();
        labelMsgViajes.setText(" ");
    }

    private void actualizarMisViajes() {
        ArrayList<Viaje> viajes = ventana.getGestorUsuario().obtenerHistorial().obtenerViajes();
        if (viajes.size() == 0) {
            areaViajes.setText("No tienes viajes planificados.\nVe a la pestana 'Planificar' para crear uno.");
            return;
        }

        StringBuilder sb = new StringBuilder("=== TUS VIAJES ===\n\n");
        for (int i = 0; i < viajes.size(); i++) {
            Viaje v = viajes.get(i);
            boolean futuro = !v.yaOcurrio(4, 7, 2026);
            sb.append("Viaje ").append(i + 1).append(": ").append(v.getDestino().toUpperCase()).append("\n");
            sb.append("  Fechas:      ").append(v.getFechaInicio()).append(" al ").append(v.getFechaFin()).append("\n");
            sb.append("  Duracion:    ").append(v.calcularDuracion()).append(" dias\n");
            sb.append("  Personas:    ").append(v.getNumeroPersonas()).append("\n");
            sb.append("  Presupuesto: $").append(v.getPresupuesto()).append("\n");
            sb.append("  Costo total: $").append(String.format("%.2f", v.getCostoTotal())).append("\n");
            sb.append("  Estado:      ").append(
                    v.isConfirmado() ? "Confirmado" : futuro ? "Pendiente de confirmacion" : "Realizado").append("\n");

            ArrayList<DiaDeViaje> dias = v.getDias();
            for (int d = 0; d < dias.size(); d++) {
                DiaDeViaje dia = dias.get(d);
                sb.append("  Dia ").append(d + 1).append(" (").append(dia.getFecha()).append("): $").append(dia.getCostoDelDia()).append("\n");
                for (Actividad a : dia.getActividades()) {
                    sb.append("    - ").append(a.getNombre()).append(" ($").append(a.getCosto()).append(")\n");
                }
            }
            sb.append("\n");
        }
        areaViajes.setText(sb.toString());
        areaViajes.setCaretPosition(0);
    }

    private void actualizarHistorial() {
        ArrayList<Viaje> viajes = ventana.getGestorUsuario().obtenerHistorial().obtenerViajes();
        if (viajes.size() == 0) {
            areaHistorial.setText("No hay viajes en tu historial todavia.");
            return;
        }

        StringBuilder sb = new StringBuilder(" Historial de Viajes\n\n");
        double total = 0;
        for (int i = 0; i < viajes.size(); i++) {
            Viaje v = viajes.get(i);
            total += v.getCostoTotal();
            sb.append("Viaje ").append(i + 1).append(": ").append(v.getDestino())
                    .append(" | ").append(v.getFechaInicio()).append(" - ").append(v.getFechaFin()).append("\n");
            sb.append("  Duracion: ").append(v.calcularDuracion()).append(" dias")
                    .append(" | Personas: ").append(v.getNumeroPersonas()).append("\n");
            sb.append("  Costo: $").append(v.getCostoTotal())
                    .append(" | ").append(v.isConfirmado() ? "Confirmado" : "Sin confirmar").append("\n\n");
        }
        sb.append("==========================================\n");
        sb.append("Total viajes:    ").append(viajes.size()).append("\n");
        sb.append("Gasto acumulado: $").append(total).append("\n");
        areaHistorial.setText(sb.toString());
        areaHistorial.setCaretPosition(0);
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

    private JLabel labelMsg() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("Arial", Font.ITALIC, 12));
        l.setForeground(ROJO);
        return l;
    }
}