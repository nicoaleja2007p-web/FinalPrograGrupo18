package interfaz;

import negocio.GestorAdmin;
import modelo.Usuario;
import modelo.Actividad;
import excepciones.SesionException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PanelAdmin extends JPanel {

    private VentanaPrincipal ventana;
    private GestorAdmin gestorAdmin;

    private JPanel panelLogin;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JLabel labelMensajeLogin;

    private JPanel panelGestion;
    private JTabbedPane tabs;

    private JTextArea areaUsuarios;
    private JTextField campoEmailEliminarUsuario;
    private JLabel labelMensajeUsuarios;

    private JTextArea areaActividades;
    private JTextField campoNombreAct;
    private JTextField campoCostoAct;
    private JTextField campoDuracionAct;
    private javax.swing.JComboBox<String> comboCategoriaAct;
    private javax.swing.JComboBox<String> comboTipoDestinoAct;
    private JTextField campoIdEliminarAct;
    private JLabel labelMensajeGestion;

    private JTextArea areaReportes;
    private JButton botonActualizarReportes;

    public PanelAdmin(VentanaPrincipal ventana, GestorAdmin gestorAdmin) {
        this.ventana = ventana;
        this.gestorAdmin = gestorAdmin;
        setLayout(new BorderLayout());
        construirPanelLogin();
        construirPanelGestion();
        add(panelLogin, BorderLayout.CENTER);
    }

    private void construirPanelLogin() {
        panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Acceso de Administrador");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(41, 98, 160));

        JLabel labelUsuario = new JLabel("Usuario:");
        JLabel labelContrasena = new JLabel("Contrasena:");

        campoUsuario = new JTextField(15);
        campoContrasena = new JPasswordField(15);

        JButton botonEntrar = new JButton("Ingresar");
        botonEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        botonEntrar.setBackground(new Color(41, 98, 160));
        botonEntrar.setForeground(Color.WHITE);
        botonEntrar.setFocusPainted(false);

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 13));

        labelMensajeLogin = new JLabel(" ");
        labelMensajeLogin.setForeground(Color.RED);
        labelMensajeLogin.setFont(new Font("Arial", Font.ITALIC, 12));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelLogin.add(titulo, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panelLogin.add(labelUsuario, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelLogin.add(campoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panelLogin.add(labelContrasena, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelLogin.add(campoContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelLogin.add(labelMensajeLogin, gbc);

        gbc.gridy = 4;
        panelLogin.add(botonEntrar, gbc);

        gbc.gridy = 5;
        panelLogin.add(botonVolver, gbc);

        botonEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarLoginAdmin();
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.mostrarPanel("login");
            }
        });
    }

    private void construirPanelGestion() {
        panelGestion = new JPanel(new BorderLayout());
        panelGestion.setBackground(new Color(245, 245, 250));

        JLabel titulo = new JLabel("  Panel de Administrador");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(41, 98, 160));
        panelGestion.add(titulo, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("Usuarios Registrados", construirPestanaUsuarios());
        tabs.addTab("Gestionar Actividades", construirPestanaActividades());
        tabs.addTab("Reportes", construirPestanaReportes());

        panelGestion.add(tabs, BorderLayout.CENTER);

        JButton botonCerrar = new JButton("Cerrar Sesion de Administrador");
        botonCerrar.setBackground(new Color(180, 50, 50));
        botonCerrar.setForeground(Color.WHITE);
        botonCerrar.setFocusPainted(false);
        panelGestion.add(botonCerrar, BorderLayout.SOUTH);

        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestorAdmin.logout();
                campoUsuario.setText("");
                campoContrasena.setText("");
                removeAll();
                add(panelLogin, BorderLayout.CENTER);
                revalidate();
                repaint();
                ventana.mostrarPanel("login");
            }
        });
    }

    private JPanel construirPestanaUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        areaUsuarios = new JTextArea();
        areaUsuarios.setEditable(false);
        areaUsuarios.setFont(new Font("Courier New", Font.PLAIN, 13));
        panel.add(new JScrollPane(areaUsuarios), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        campoEmailEliminarUsuario = new JTextField(18);
        JButton botonEliminar = new JButton("Eliminar Usuario por Email");
        labelMensajeUsuarios = new JLabel(" ");
        labelMensajeUsuarios.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0;
        panelAcciones.add(new JLabel("Email a eliminar:"), gbc);
        gbc.gridx = 1;
        panelAcciones.add(campoEmailEliminarUsuario, gbc);
        gbc.gridx = 2;
        panelAcciones.add(botonEliminar, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        panelAcciones.add(labelMensajeUsuarios, gbc);

        panel.add(panelAcciones, BorderLayout.SOUTH);

        botonEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarEliminarUsuario();
            }
        });

        return panel;
    }

    private JPanel construirPestanaActividades() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        campoNombreAct = new JTextField(12);
        campoCostoAct = new JTextField(6);
        campoDuracionAct = new JTextField(4);
        String[] cats = {"aventura", "cultural", "gastronomia", "relajacion"};
        comboCategoriaAct = new javax.swing.JComboBox<String>(cats);
        String[] tiposDestino = {"playa", "ciudad", "montania", "selva", "desierto"};
        comboTipoDestinoAct = new javax.swing.JComboBox<String>(tiposDestino);
        JButton botonAgregar = new JButton("Agregar Actividad");
        campoIdEliminarAct = new JTextField(5);
        JButton botonEliminar = new JButton("Eliminar por ID");
        labelMensajeGestion = new JLabel(" ");
        labelMensajeGestion.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; form.add(campoNombreAct, gbc);
        gbc.gridx = 2; form.add(new JLabel("Costo:"), gbc);
        gbc.gridx = 3; form.add(campoCostoAct, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Duracion (h):"), gbc);
        gbc.gridx = 1; form.add(campoDuracionAct, gbc);
        gbc.gridx = 2; form.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3; form.add(comboCategoriaAct, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Tipo de destino:"), gbc);
        gbc.gridx = 1; form.add(comboTipoDestinoAct, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER;
        form.add(botonAgregar, gbc);

        gbc.gridy = 4;
        form.add(labelMensajeGestion, gbc);

        gbc.gridy = 5; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("ID a eliminar:"), gbc);
        gbc.gridx = 1; form.add(campoIdEliminarAct, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2; form.add(botonEliminar, gbc);

        areaActividades = new JTextArea();
        areaActividades.setEditable(false);
        areaActividades.setFont(new Font("Courier New", Font.PLAIN, 13));

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaActividades), BorderLayout.CENTER);

        botonAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { manejarAgregarActividad(); }
        });
        botonEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { manejarEliminarActividad(); }
        });

        return panel;
    }

    private JPanel construirPestanaReportes() {
        JPanel panel = new JPanel(new BorderLayout());

        areaReportes = new JTextArea();
        areaReportes.setEditable(false);
        areaReportes.setFont(new Font("Courier New", Font.PLAIN, 13));
        panel.add(new JScrollPane(areaReportes), BorderLayout.CENTER);

        botonActualizarReportes = new JButton("Actualizar Reporte");
        botonActualizarReportes.setBackground(new Color(41, 98, 160));
        botonActualizarReportes.setForeground(Color.WHITE);
        botonActualizarReportes.setFocusPainted(false);
        panel.add(botonActualizarReportes, BorderLayout.SOUTH);

        botonActualizarReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { actualizarReportes(); }
        });

        return panel;
    }

    private void manejarLoginAdmin() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();

        try {
            gestorAdmin.login(usuario, contrasena);
            labelMensajeLogin.setText(" ");
            campoUsuario.setText("");
            campoContrasena.setText("");
            actualizarTodo();
            removeAll();
            add(panelGestion, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SesionException ex) {
            labelMensajeLogin.setText(ex.getMessage());
        }
    }

    private void manejarAgregarActividad() {
        String nombre = campoNombreAct.getText().trim();
        String costoTexto = campoCostoAct.getText().trim();
        String duracionTexto = campoDuracionAct.getText().trim();
        String categoria = (String) comboCategoriaAct.getSelectedItem();
        String tipoDestino = (String) comboTipoDestinoAct.getSelectedItem();

        if (nombre.equals("") || costoTexto.equals("") || duracionTexto.equals("")) {
            labelMensajeGestion.setText("Todos los campos son obligatorios.");
            return;
        }

        double costo;
        int duracion;
        try {
            costo = Double.parseDouble(costoTexto);
            duracion = Integer.parseInt(duracionTexto);
        } catch (NumberFormatException ex) {
            labelMensajeGestion.setText("Costo y duracion deben ser numericos.");
            return;
        }

        boolean exito = gestorAdmin.registrarActividad(nombre, costo, duracion, categoria, tipoDestino);
        if (exito) {
            labelMensajeGestion.setText(" ");
            campoNombreAct.setText("");
            campoCostoAct.setText("");
            campoDuracionAct.setText("");
            comboCategoriaAct.setSelectedIndex(0);
            comboTipoDestinoAct.setSelectedIndex(0);
            actualizarActividades();
        } else {
            labelMensajeGestion.setText("No se pudo registrar. Revise los datos.");
        }
    }

    private void manejarEliminarActividad() {
        String idTexto = campoIdEliminarAct.getText().trim();
        if (idTexto.equals("")) {
            labelMensajeGestion.setText("Ingrese el ID de la actividad.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            labelMensajeGestion.setText("El ID debe ser un numero.");
            return;
        }

        boolean exito = gestorAdmin.eliminarActividad(id);
        if (exito) {
            labelMensajeGestion.setText(" ");
            campoIdEliminarAct.setText("");
            actualizarActividades();
        } else {
            labelMensajeGestion.setText("No se encontró una actividad con ese ID.");
        }
    }

    private void manejarEliminarUsuario() {
        String email = campoEmailEliminarUsuario.getText().trim();
        if (email.equals("")) {
            labelMensajeUsuarios.setText("Ingrese el email del usuario.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(ventana,
                "Eliminar al usuario con email " + email + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) { return; }

        boolean exito = gestorAdmin.eliminarUsuarioPorEmail(email);
        if (exito) {
            labelMensajeUsuarios.setText(" ");
            campoEmailEliminarUsuario.setText("");
            actualizarUsuarios();
        } else {
            labelMensajeUsuarios.setText("No se encontró un usuario con ese email.");
        }
    }

    private void actualizarTodo() {
        actualizarUsuarios();
        actualizarActividades();
        actualizarReportes();
    }

    private void actualizarUsuarios() {
        ArrayList<Usuario> usuarios = gestorAdmin.listarUsuarios();
        String texto = "=== USUARIOS REGISTRADOS (" + usuarios.size() + ") ===\n\n";
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            texto = texto + "ID: " + u.getId() + " | Nombre: " + u.getNombre() + " | Email: " + u.getEmail() + "\n";
        }
        areaUsuarios.setText(texto);
    }

    private void actualizarActividades() {
        ArrayList<Actividad> actividades = gestorAdmin.getGestorActividades().getActividades();
        String texto = "=== ACTIVIDADES (" + actividades.size() + ") ===\n\n";
        for (int i = 0; i < actividades.size(); i++) {
            texto = texto + "ID " + actividades.get(i).getId() + " - " + actividades.get(i).toString() + "\n";
        }
        areaActividades.setText(texto);
    }

    private void actualizarReportes() {
        String texto = "=========================================\n";
        texto = texto + "   ESTADISTICAS GENERALES DEL SISTEMA\n";
        texto = texto + "=========================================\n\n";
        texto = texto + "Usuarios registrados:      " + gestorAdmin.contarUsuarios() + "\n";
        texto = texto + "Actividades en catalogo:   " + gestorAdmin.contarActividades() + "\n";
        texto = texto + "Total viajes planificados: " + gestorAdmin.contarViajesTotales() + "\n";
        texto = texto + "Gasto total acumulado:    $" + String.format("%.2f", gestorAdmin.calcularGastoTotalSistema()) + "\n";
        texto = texto + "Gasto promedio por viaje: $" + String.format("%.2f", gestorAdmin.gastoPromedioPorViaje()) + "\n";
        texto = texto + "Categoria mas elegida:     " + gestorAdmin.categoriaMasUsada() + "\n\n";
        texto = texto + "=== DESTINOS MAS VISITADOS ===\n";
        texto = texto + gestorAdmin.destinosMasVisitados();
        areaReportes.setText(texto);
    }
}