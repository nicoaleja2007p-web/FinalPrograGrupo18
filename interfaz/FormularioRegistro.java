package interfaz;

import negocio.GestorUsuario;
import excepciones.UsuarioInvalidoException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioRegistro extends JPanel {

    private VentanaPrincipal ventana;
    private GestorUsuario gestorUsuario;

    private JLabel labelTitulo;
    private JLabel labelNombre;
    private JLabel labelEmail;
    private JLabel labelContrasena;
    private JLabel labelPregunta;
    private JLabel labelRespuesta;
    private JLabel labelMensaje;

    private JTextField campoNombre;
    private JTextField campoEmail;
    private JPasswordField campoContrasena;
    private JComboBox<String> comboPregunta;
    private JTextField campoRespuesta;

    private JButton botonRegistrar;
    private JButton botonVolver;

    private static final String[] PREGUNTAS = {
            "¿Cual es el nombre de tu primera mascota?",
            "¿En que ciudad naciste?",
            "¿Cual es el nombre de tu mejor amigo de la infancia?",
            "¿Cual es tu pelicula favorita?",
            "¿Cual es el apellido de tu madre?"
    };

    public FormularioRegistro(VentanaPrincipal ventana, GestorUsuario gestorUsuario) {
        this.ventana = ventana;
        this.gestorUsuario = gestorUsuario;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setBackground(new Color(245, 245, 250));

        labelTitulo = new JLabel("Crear cuenta nueva");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitulo.setForeground(new Color(41, 98, 160));

        labelNombre = new JLabel("Nombre:");
        labelEmail = new JLabel("Email:");
        labelContrasena = new JLabel("Contraseña (min. 6 caracteres):");
        labelPregunta = new JLabel("Pregunta de seguridad:");
        labelRespuesta = new JLabel("Respuesta:");

        labelNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        labelContrasena.setFont(new Font("Arial", Font.PLAIN, 13));
        labelPregunta.setFont(new Font("Arial", Font.PLAIN, 13));
        labelRespuesta.setFont(new Font("Arial", Font.PLAIN, 13));

        campoNombre = new JTextField(20);
        campoEmail = new JTextField(20);
        campoContrasena = new JPasswordField(20);
        comboPregunta = new JComboBox<String>(PREGUNTAS);
        campoRespuesta = new JTextField(20);

        campoNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        campoContrasena.setFont(new Font("Arial", Font.PLAIN, 13));
        campoRespuesta.setFont(new Font("Arial", Font.PLAIN, 13));

        botonRegistrar = new JButton("Crear cuenta");
        botonRegistrar.setFont(new Font("Arial", Font.BOLD, 13));
        botonRegistrar.setBackground(new Color(41, 98, 160));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);

        botonVolver = new JButton("Volver al Login");
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 13));
        botonVolver.setBackground(new Color(200, 200, 200));
        botonVolver.setFocusPainted(false);

        labelMensaje = new JLabel(" ");
        labelMensaje.setFont(new Font("Arial", Font.ITALIC, 12));
        labelMensaje.setForeground(Color.RED);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        agregarFila(gbc, labelNombre, campoNombre, 1);
        agregarFila(gbc, labelEmail, campoEmail, 2);
        agregarFila(gbc, labelContrasena, campoContrasena, 3);
        agregarFila(gbc, labelPregunta, comboPregunta, 4);
        agregarFila(gbc, labelRespuesta, campoRespuesta, 5);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelMensaje, gbc);

        gbc.gridy = 7; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(botonRegistrar, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(botonVolver, gbc);
    }

    private void agregarFila(GridBagConstraints gbc, JLabel label, java.awt.Component campo, int fila) {
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        add(label, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(campo, gbc);
    }

    private void configurarEventos() {
        botonRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarRegistro();
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
                ventana.mostrarPanel("login");
            }
        });
    }

    private void manejarRegistro() {
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();
        String pregunta = (String) comboPregunta.getSelectedItem();
        String respuesta = campoRespuesta.getText().trim();

        if (nombre.equals("") || email.equals("") || contrasena.equals("") || respuesta.equals("")) {
            labelMensaje.setText("Error: Todos los campos son obligatorios.");
            return;
        }

        if (!respuesta.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            labelMensaje.setText("Error: La respuesta solo debe contener letras.");
            return;
        }

        try {
            gestorUsuario.registrarUsuario(nombre, email, contrasena, pregunta, respuesta);
            JOptionPane.showMessageDialog(ventana,
                    "Cuenta creada exitosamente. Ya puedes iniciar sesion.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            ventana.mostrarPanel("login");
        } catch (UsuarioInvalidoException ex) {
            labelMensaje.setText("Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        campoNombre.setText("");
        campoEmail.setText("");
        campoContrasena.setText("");
        campoRespuesta.setText("");
        comboPregunta.setSelectedIndex(0);
        labelMensaje.setText(" ");
    }
}