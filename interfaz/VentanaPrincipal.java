package interfaz;

import negocio.GestorUsuario;
import negocio.GestorActividades;
import negocio.GestorAdmin;
import modelo.PerfilPreferencias;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    private GestorUsuario gestorUsuario;
    private GestorActividades gestorActividades;
    private GestorAdmin gestorAdmin;

    private FormularioLogin formularioLogin;
    private FormularioRegistro formularioRegistro;
    private PanelUsuario panelUsuario;
    private PanelAdmin panelAdmin;

    public VentanaPrincipal() {
        gestorUsuario = new GestorUsuario();
        gestorActividades = new GestorActividades();
        gestorAdmin = new GestorAdmin(gestorUsuario, gestorActividades);

        cargarActividades();
        inicializarVentana();
        inicializarPaneles();
        mostrarPanel("login");
    }

    private void cargarActividades() {
        //PLAYA
        gestorActividades.registrarActividad("Buceo en arrecife", 70.0, 4, "aventura", "playa");
        gestorActividades.registrarActividad("Kayak en el mar", 30.0, 2, "aventura", "playa");
        gestorActividades.registrarActividad("Visita a pueblo pesquero historico", 15.0, 2, "cultural", "playa");
        gestorActividades.registrarActividad("Tour de arquitectura costera", 18.0, 2, "cultural", "playa");
        gestorActividades.registrarActividad("Degustacion de mariscos", 45.0, 2, "gastronomia", "playa");
        gestorActividades.registrarActividad("Clase de cocina costera", 50.0, 3, "gastronomia", "playa");
        gestorActividades.registrarActividad("Tarde libre en playa", 5.0, 4, "relajacion", "playa");
        gestorActividades.registrarActividad("Masaje frente al mar", 55.0, 2, "relajacion", "playa");
        gestorActividades.registrarActividad("Paseo en moto acuatica", 65.0, 1, "aventura", "playa");
        gestorActividades.registrarActividad("Caminata por la orilla", 8.0, 2, "aventura", "playa");
        gestorActividades.registrarActividad("Tour de faro historico", 12.0, 1, "cultural", "playa");
        gestorActividades.registrarActividad("Cena frente al mar", 60.0, 2, "gastronomia", "playa");
        gestorActividades.registrarActividad("Paseo en catamaran al atardecer", 38.0, 3, "relajacion", "playa");

        // CIUDAD
        gestorActividades.registrarActividad("Recorrido en bicicleta urbana", 15.0, 2, "aventura", "ciudad");
        gestorActividades.registrarActividad("Tour en Segway por la ciudad", 25.0, 2, "aventura", "ciudad");
        gestorActividades.registrarActividad("Tour ciudad historica", 30.0, 3, "cultural", "ciudad");
        gestorActividades.registrarActividad("Visita a museo de arte", 15.0, 2, "cultural", "ciudad");
        gestorActividades.registrarActividad("Tour gastronomico urbano", 45.0, 3, "gastronomia", "ciudad");
        gestorActividades.registrarActividad("Clase de cocina local", 55.0, 3, "gastronomia", "ciudad");
        gestorActividades.registrarActividad("Paseo por parque urbano", 8.0, 2, "relajacion", "ciudad");
        gestorActividades.registrarActividad("Spa en hotel boutique", 60.0, 2, "relajacion", "ciudad");
        gestorActividades.registrarActividad("Escape room tematico", 20.0, 1, "aventura", "ciudad");
        gestorActividades.registrarActividad("Tour en globo aerostatico urbano", 90.0, 2, "aventura", "ciudad");
        gestorActividades.registrarActividad("Recorrido por galerias de arte", 10.0, 2, "cultural", "ciudad");
        gestorActividades.registrarActividad("Cena en restaurante gourmet", 75.0, 2, "gastronomia", "ciudad");
        gestorActividades.registrarActividad("Sesion de yoga en azotea", 18.0, 1, "relajacion", "ciudad");

        // MONTAÑA
        gestorActividades.registrarActividad("Senderismo en montania", 20.0, 5, "aventura", "montania");
        gestorActividades.registrarActividad("Escalada en roca", 55.0, 4, "aventura", "montania");
        gestorActividades.registrarActividad("Visita a sitio arqueologico", 25.0, 3, "cultural", "montania");
        gestorActividades.registrarActividad("Recorrido por pueblos andinos", 20.0, 3, "cultural", "montania");
        gestorActividades.registrarActividad("Visita a finca caficultora", 30.0, 3, "gastronomia", "montania");
        gestorActividades.registrarActividad("Cata de quesos artesanales", 22.0, 1, "gastronomia", "montania");
        gestorActividades.registrarActividad("Visita a aguas termales", 25.0, 3, "relajacion", "montania");
        gestorActividades.registrarActividad("Caminata de bienestar", 8.0, 2, "relajacion", "montania");
        gestorActividades.registrarActividad("Cabalgata en montania", 35.0, 3, "aventura", "montania");
        gestorActividades.registrarActividad("Parapente sobre el valle", 80.0, 2, "aventura", "montania");
        gestorActividades.registrarActividad("Visita a mercado indigena", 12.0, 2, "cultural", "montania");
        gestorActividades.registrarActividad("Fogata con comida tipica", 28.0, 2, "gastronomia", "montania");
        gestorActividades.registrarActividad("Observación de estrellas en altura", 15.0, 2, "relajacion", "montania");

        // SELVA
        gestorActividades.registrarActividad("Rafting en rio", 45.0, 4, "aventura", "selva");
        gestorActividades.registrarActividad("Zipline en bosque", 40.0, 2, "aventura", "selva");
        gestorActividades.registrarActividad("Ceremonia cultural indigena", 20.0, 3, "cultural", "selva");
        gestorActividades.registrarActividad("Visita a comunidad local", 18.0, 2, "cultural", "selva");
        gestorActividades.registrarActividad("Brunch en hacienda local", 28.0, 2, "gastronomia", "selva");
        gestorActividades.registrarActividad("Degustacion de frutas tropicales", 15.0, 1, "gastronomia", "selva");
        gestorActividades.registrarActividad("Picnic en parque natural", 10.0, 2, "relajacion", "selva");
        gestorActividades.registrarActividad("Sesion de spa en naturaleza", 45.0, 2, "relajacion", "selva");
        gestorActividades.registrarActividad("Kayak por rio selvatico", 32.0, 3, "aventura", "selva");
        gestorActividades.registrarActividad("Caminata nocturna en la selva", 22.0, 2, "aventura", "selva");
        gestorActividades.registrarActividad("Visita a reserva natural", 14.0, 2, "cultural", "selva");
        gestorActividades.registrarActividad("Cena con ingredientes de la selva", 42.0, 2, "gastronomia", "selva");
        gestorActividades.registrarActividad("Paseo en canoa por el rio", 20.0, 2, "relajacion", "selva");

        // ══ DESIERTO ══
        gestorActividades.registrarActividad("Paseo en buggy por dunas", 50.0, 2, "aventura", "desierto");
        gestorActividades.registrarActividad("Caminata nocturna", 15.0, 2, "aventura", "desierto");
        gestorActividades.registrarActividad("Tour de oasis historico", 20.0, 2, "cultural", "desierto");
        gestorActividades.registrarActividad("Visita a ruinas del desierto", 25.0, 3, "cultural", "desierto");
        gestorActividades.registrarActividad("Cena bajo las estrellas", 40.0, 2, "gastronomia", "desierto");
        gestorActividades.registrarActividad("Degustacion de dátiles y te", 12.0, 1, "gastronomia", "desierto");
        gestorActividades.registrarActividad("Observacion de estrellas", 18.0, 2, "relajacion", "desierto");
        gestorActividades.registrarActividad("Meditacion en el desierto", 15.0, 1, "relajacion", "desierto");
        gestorActividades.registrarActividad("Sandboarding en las dunas", 35.0, 2, "aventura", "desierto");
        gestorActividades.registrarActividad("Cabalgata en camellos", 48.0, 2, "aventura", "desierto");
        gestorActividades.registrarActividad("Tour de arte rupestre", 16.0, 2, "cultural", "desierto");
        gestorActividades.registrarActividad("Te tradicional en carpa beduina", 22.0, 1, "gastronomia", "desierto");
        gestorActividades.registrarActividad("Amanecer guiado en el desierto", 10.0, 1, "relajacion", "desierto");
    }

    private void inicializarVentana() {
        setTitle("Planificador Inteligente de Viajes");
        setSize(900, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarPaneles() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        formularioLogin    = new FormularioLogin(this, gestorUsuario);
        formularioRegistro = new FormularioRegistro(this, gestorUsuario);
        panelUsuario       = new PanelUsuario(this);
        panelAdmin         = new PanelAdmin(this, gestorAdmin);

        panelPrincipal.add(formularioLogin,    "login");
        panelPrincipal.add(formularioRegistro, "registro");
        panelPrincipal.add(panelUsuario,       "panelUsuario");
        panelPrincipal.add(panelAdmin,         "admin");

        add(panelPrincipal);
    }

    public void mostrarPanel(String nombre) {
        if (nombre.equals("panelUsuario")) {
            panelUsuario.actualizar();
        }
        cardLayout.show(panelPrincipal, nombre);
    }

    public GestorUsuario getGestorUsuario()         { return gestorUsuario; }
    public GestorActividades getGestorActividades() { return gestorActividades; }
    public GestorAdmin getGestorAdmin()             { return gestorAdmin; }

    public PerfilPreferencias getPerfilActual() {
        return gestorUsuario.getPerfilUsuarioActivo();
    }

    public void setPerfilActual(PerfilPreferencias perfil) {
        gestorUsuario.guardarPerfilUsuarioActivo(perfil);
    }

    public PanelUsuario getPanelUsuario() { return panelUsuario; }
    public PanelAdmin getPanelAdmin()     { return panelAdmin; }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}