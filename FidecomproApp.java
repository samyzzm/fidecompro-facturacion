import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FidecomproApp {
    private static Usuario usuarioActual;
    private static final ArrayList<Cliente> clientes = new ArrayList<>();
    private static final ArrayList<Producto> productos = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FidecomproApp::mostrarLogin);
    }

    private static void mostrarLogin() {
        JFrame frame = new JFrame("Login - Fidecompro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();
        JButton btnLogin = new JButton("Ingresar");

        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContrasena);
        panel.add(new JLabel());
        panel.add(btnLogin);

        btnLogin.addActionListener(e -> {
            UsuarioAdmin admin = new UsuarioAdmin("admin", "1234");
            if (admin.login(txtUsuario.getText(), new String(txtContrasena.getPassword()))) {
                usuarioActual = admin;
                frame.dispose();
                mostrarMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Credenciales incorrectas");
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void mostrarMenu() {
        JFrame frame = new JFrame("Menú Principal - Fidecompro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton btnCliente = new JButton("Registrar Cliente");
        JButton btnProducto = new JButton("Registrar Producto");
        JButton btnFactura = new JButton("Crear Factura");

        btnCliente.addActionListener(e -> registrarCliente());
        btnProducto.addActionListener(e -> registrarProducto());
        btnFactura.addActionListener(e -> crearFactura());

        panel.add(btnCliente);
        panel.add(btnProducto);
        panel.add(btnFactura);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void registrarCliente() {
        String nombre = JOptionPane.showInputDialog("Nombre del cliente:");
        String telefono = JOptionPane.showInputDialog("Teléfono del cliente:");
        if (nombre != null && telefono != null) {
            clientes.add(new Cliente(nombre, telefono));
            JOptionPane.showMessageDialog(null, "Cliente registrado con éxito.");
        }
    }

    private static void registrarProducto() {
        String nombre = JOptionPane.showInputDialog("Nombre del producto:");
        String precioStr = JOptionPane.showInputDialog("Precio:");
        String stockStr = JOptionPane.showInputDialog("Stock inicial:");
        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);
            productos.add(new Producto(nombre, precio, stock));
            JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Datos inválidos.");
        }
    }

    private static void crearFactura() {
        if (clientes.isEmpty() || productos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe registrar al menos un cliente y un producto.");
            return;
        }
        Cliente cliente = (Cliente) JOptionPane.showInputDialog(null, "Seleccione cliente:",
                "Cliente", JOptionPane.QUESTION_MESSAGE, null,
                clientes.toArray(), clientes.get(0));

        Producto producto = (Producto) JOptionPane.showInputDialog(null, "Seleccione producto:",
                "Producto", JOptionPane.QUESTION_MESSAGE, null,
                productos.toArray(), productos.get(0));

        String cantidadStr = JOptionPane.showInputDialog("Cantidad a comprar:");
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            producto.restarStock(cantidad);
            double total = cantidad * producto.getPrecio();
            JOptionPane.showMessageDialog(null, "Factura creada para " + cliente +
                    "\nProducto: " + producto.getNombre() +
                    "\nCantidad: " + cantidad +
                    "\nTotal: ¢" + total);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}