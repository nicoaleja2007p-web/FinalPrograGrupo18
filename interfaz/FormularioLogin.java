package interfaz;

import negocio.GestorUsuario;
import excepciones.SesionException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioLogin extends JPanel {

    private VentanaPrincipal ventana;
    private GestorUsuario gestorUsuario;

    private JLabel labelTitulo;
    private JLabel labelSubtitulo;
    private JLabel labelEmail;
    private JLabel labelContrasena;
    private JLabel labelMensaje;
    private JTextField campoEmail;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JButton botonRegistro;
    private JButton botonOlvideContrasena;
    private JButton botonAdmin;

    public FormularioLogin(VentanaPrincipal ventana, GestorUsuario gestorUsuario) {
        this.ventana = ventana;
        this.gestorUsuario = gestorUsuario;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setBackground(new Color(240, 244, 252));

        labelTitulo = new JLabel("✈  Planificador de Viajes");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 25));
        labelTitulo.setForeground(new Color(30, 90, 160));

        labelSubtitulo = new JLabel("Inicia sesion para continuar");
        labelSubtitulo.setFont(new Font("Arial", Font.ITALIC, 13));
        labelSubtitulo.setForeground(new Color(100, 100, 120));

        labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Arial", Font.PLAIN, 14));

        labelContrasena = new JLabel("Contraseña:");
        labelContrasena.setFont(new Font("Arial", Font.PLAIN, 14));

        campoEmail = new JTextField(20);
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 13));

        campoContrasena = new JPasswordField(20);
        campoContrasena.setFont(new Font("Arial", Font.PLAIN, 13));

        botonLogin = new JButton("Iniciar Sesion");
        botonLogin.setFont(new Font("Arial", Font.BOLD, 13));
        botonLogin.setBackground(new Color(30, 90, 160));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFocusPainted(false);

        botonRegistro = new JButton("Crear cuenta");
        botonRegistro.setFont(new Font("Arial", Font.PLAIN, 13));
        botonRegistro.setBackground(new Color(210, 224, 245));
        botonRegistro.setFocusPainted(false);

        botonOlvideContrasena = new JButton("Olvidé mi contraseña");
        botonOlvideContrasena.setFont(new Font("Arial", Font.PLAIN, 12));
        botonOlvideContrasena.setBackground(new Color(240, 244, 252));
        botonOlvideContrasena.setFocusPainted(false);
        botonOlvideContrasena.setBorderPainted(false);
        botonOlvideContrasena.setForeground(new Color(30, 90, 160));

        botonAdmin = new JButton("Acceso Administrador");
        botonAdmin.setFont(new Font("Arial", Font.PLAIN, 11));
        botonAdmin.setBackground(new Color(240, 244, 252));
        botonAdmin.setFocusPainted(false);
        botonAdmin.setBorderPainted(false);
        botonAdmin.setForeground(new Color(120, 120, 120));

        labelMensaje = new JLabel(" ");
        labelMensaje.setFont(new Font("Arial", Font.ITALIC, 12));
        labelMensaje.setForeground(new Color(190, 40, 40));
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        gbc.gridy = 1;
        add(labelSubtitulo, gbc);

        gbc.gridwidth = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(labelEmail, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(labelContrasena, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(campoContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelMensaje, gbc);

        gbc.gridy = 5; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(botonLogin, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(botonRegistro, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonOlvideContrasena, gbc);

        gbc.gridy = 7;
        add(botonAdmin, gbc);
    }

    private void configurarEventos() {
        botonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarLogin();
            }
        });

        botonRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.mostrarPanel("registro");
            }
        });

        botonOlvideContrasena.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarRecuperarContrasena();
            }
        });

        botonAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.mostrarPanel("admin");
            }
        });
    }

    private void manejarLogin() {
        String email = campoEmail.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();

        if (email.equals("") || contrasena.equals("")) {
            labelMensaje.setText("Error: Todos los campos son obligatorios.");
            return;
        }

        if (!gestorUsuario.esEmailValido(email)) {
            labelMensaje.setText("Error: Formato de email invalido (ejemplo: nombre@correo.com).");
            return;
        }

        boolean exito = gestorUsuario.login(email, contrasena);

        if (exito) {
            labelMensaje.setText(" ");
            campoEmail.setText("");
            campoContrasena.setText("");
            ventana.mostrarPanel("panelUsuario");
            return;
        }

        int resultado = gestorUsuario.getUltimoResultado();

        if (resultado == GestorUsuario.LOGIN_EMAIL_NO_EXISTE) {
            labelMensaje.setText("No existe una cuenta registrada con ese email.");
        } else if (resultado == GestorUsuario.LOGIN_CUENTA_ELIMINADA) {
            labelMensaje.setText("Cuenta eliminada por demasiados intentos fallidos. Debe registrarse de nuevo.");
            campoEmail.setText("");
            campoContrasena.setText("");
        } else {
            int intentos = gestorUsuario.getIntentosDeEmail(email);
            labelMensaje.setText("Contraseña incorrecta. Intento " + intentos + "/3");
        }
    }

    private void manejarRecuperarContrasena() {
        String email = JOptionPane.showInputDialog(ventana, "Ingrese su email registrado:");
        if (email == null || email.trim().equals("")) { return; }

        modelo.Usuario usuario = gestorUsuario.buscarPorEmail(email.trim());
        if (usuario == null) {
            JOptionPane.showMessageDialog(ventana, "No existe un usuario con ese email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuario.getPreguntaSeguridad() == null || usuario.getPreguntaSeguridad().equals("")) {
            JOptionPane.showMessageDialog(ventana, "Este usuario no tiene pregunta de seguridad configurada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String respuesta = JOptionPane.showInputDialog(ventana, usuario.getPreguntaSeguridad());
        if (respuesta == null) { return; }

        try {
            String contrasena = gestorUsuario.recuperarContrasena(email.trim(), respuesta);
            JOptionPane.showMessageDialog(ventana, "Su contraseña es: " + contrasena, "Recuperacion exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (SesionException ex) {
            JOptionPane.showMessageDialog(ventana, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}