import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FidecomproApp {
    private static Usuario usuarioActual;
    
    private static final Color PRIMARY_COLOR = new Color(20, 100, 160);     
    private static final Color SECONDARY_COLOR = new Color(30, 120, 180);   
    private static final Color SUCCESS_COLOR = new Color(50, 150, 50);      
    private static final Color BACKGROUND_COLOR = new Color(30, 34, 38);     
    private static final Color TEXT_COLOR = new Color(230, 230, 230);        
    private static final Color BORDER_COLOR = new Color(60, 65, 70);        
    private static final Color BUTTON_TEXT_COLOR = new Color(10, 10, 10);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        SwingUtilities.invokeLater(FidecomproApp::mostrarLogin);
    }

    private static void mostrarLogin() {
        JFrame frame = new JFrame("Fidecompro - Sistema de Facturación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("FIDECOMPRO", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(TEXT_COLOR);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BACKGROUND_COLOR);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUsuario = new JTextField(15);
        JPasswordField txtContrasena = new JPasswordField(15);
        JButton btnLogin = crearBotonPrimario("Iniciar Sesión");

        // Estilo de campos
        estilizarCampo(txtUsuario);
        estilizarCampo(txtContrasena);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(TEXT_COLOR);
        loginPanel.add(lblUsuario, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        loginPanel.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(TEXT_COLOR);
        loginPanel.add(lblContrasena, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        loginPanel.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText();
            String pass = new String(txtContrasena.getPassword());

            if (validarUsuario(user, pass)) {
                frame.dispose();
                mostrarMenu();
            } else {
                mostrarError(frame, "Credenciales incorrectas");
            }
        });

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        // Pie de página
        JLabel footer = new JLabel("Sistema de Gestión Empresarial v2.0", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        footer.setForeground(TEXT_COLOR);
        mainPanel.add(footer, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    private static boolean validarUsuario(String user, String pass) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT nombre, tipo FROM usuarios WHERE usuario = ? AND contrasena = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                if ("admin".equals(tipo)) {
                    usuarioActual = new UsuarioAdmin(nombre, user, pass);
                } else {
                    usuarioActual = new UsuarioVendedor(nombre, user, pass);
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private static void mostrarMenu() {
        JFrame frame = new JFrame("Fidecompro - Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título con saludo
        JLabel titulo = new JLabel("Bienvenido, " + usuarioActual.getNombre(), JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(TEXT_COLOR);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Panel de botones
        JPanel botonesPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        botonesPanel.setBackground(BACKGROUND_COLOR);

        JButton btnCliente = crearBotonMenu("Registrar Cliente", "Agregar nuevos clientes al sistema");
        JButton btnProducto = crearBotonMenu("Registrar Producto", "Agregar productos al inventario");
        JButton btnFactura = crearBotonMenu("Crear Factura", "Generar nueva factura de venta");
        JButton btnVerFacturas = crearBotonMenu("Ver Facturas", "Consultar historial de ventas");

        btnCliente.addActionListener(e -> registrarCliente());
        btnProducto.addActionListener(e -> registrarProducto());
        btnFactura.addActionListener(e -> crearFactura());
        btnVerFacturas.addActionListener(e -> verFacturas());

        botonesPanel.add(btnCliente);
        botonesPanel.add(btnProducto);
        botonesPanel.add(btnFactura);
        botonesPanel.add(btnVerFacturas);

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(botonesPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void registrarCliente() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtNombre = new JTextField();
        JTextField txtTelefono = new JTextField();
        estilizarCampo(txtNombre);
        estilizarCampo(txtTelefono);
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(TEXT_COLOR);
        panel.add(lblNombre);
        panel.add(txtNombre);
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(TEXT_COLOR);
        panel.add(lblTelefono);
        panel.add(txtTelefono);

        int result = JOptionPane.showConfirmDialog(null, panel, 
            "Registrar Nuevo Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            
            if (!nombre.isEmpty() && !telefono.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO clientes (nombre, telefono) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nombre);
                    stmt.setString(2, telefono);
                    stmt.executeUpdate();
                    mostrarExito("Cliente registrado exitosamente");
                } catch (SQLException e) {
                    mostrarError(null, "Error: " + e.getMessage());
                }
            } else {
                mostrarError(null, "Todos los campos son obligatorios");
            }
        }
    }

    private static void registrarProducto() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();
        
        estilizarCampo(txtNombre);
        estilizarCampo(txtPrecio);
        estilizarCampo(txtStock);
        
        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setForeground(TEXT_COLOR);
        panel.add(lblProducto);
        panel.add(txtNombre);
        JLabel lblPrecio = new JLabel("Precio (₡):");
        lblPrecio.setForeground(TEXT_COLOR);
        panel.add(lblPrecio);
        panel.add(txtPrecio);
        JLabel lblStock = new JLabel("Stock inicial:");
        lblStock.setForeground(TEXT_COLOR);
        panel.add(lblStock);
        panel.add(txtStock);

        int result = JOptionPane.showConfirmDialog(null, panel, 
            "Registrar Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());
                
                if (precio <= 0 || stock < 0) {
                    mostrarError(null, "El precio debe ser mayor a 0 y el stock no puede ser negativo");
                    return;
                }
                
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nombre);
                    stmt.setDouble(2, precio);
                    stmt.setInt(3, stock);
                    stmt.executeUpdate();
                    mostrarExito("Producto registrado exitosamente");
                } catch (SQLException e) {
                    mostrarError(null, "Error: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                mostrarError(null, "Precio y stock deben ser números válidos");
            }
        }
    }

    private static void crearFactura() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            ArrayList<Cliente> clientes = obtenerClientes(conn);
            if (clientes.isEmpty()) {
                mostrarError(null, "Debe registrar al menos un cliente");
                return;
            }

            Cliente cliente = (Cliente) JOptionPane.showInputDialog(null, 
                "Seleccione el cliente:", "Crear Nueva Factura",
                JOptionPane.QUESTION_MESSAGE, null, clientes.toArray(), clientes.get(0));

            if (cliente == null) return;

            Factura factura = new Factura(cliente);
            
            while (true) {
                ArrayList<Producto> productos = obtenerProductos(conn);
                if (productos.isEmpty()) {
                    mostrarError(null, "No hay productos disponibles con stock");
                    return;
                }

                Producto producto = (Producto) JOptionPane.showInputDialog(null, 
                    "Seleccione el producto:", "Agregar Producto",
                    JOptionPane.QUESTION_MESSAGE, null, productos.toArray(), productos.get(0));

                if (producto == null) break;

                String cantidadStr = JOptionPane.showInputDialog(null, 
                    "Cantidad a facturar:\n(Stock disponible: " + producto.getStock() + ")",
                    "Cantidad", JOptionPane.QUESTION_MESSAGE);
                    
                if (cantidadStr == null) break;

                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0) {
                        mostrarError(null, "La cantidad debe ser mayor a 0");
                        continue;
                    }
                    if (cantidad > producto.getStock()) {
                        throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre());
                    }

                    factura.agregarDetalle(producto, cantidad);

                    int opcion = JOptionPane.showConfirmDialog(null, 
                        "Producto agregado correctamente\n\n¿Desea agregar otro producto?", 
                        "Continuar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if (opcion != JOptionPane.YES_OPTION) break;

                } catch (NumberFormatException e) {
                    mostrarError(null, "La cantidad debe ser un número válido");
                } catch (StockInsuficienteException e) {
                    mostrarError(null, e.getMessage());
                }
            }

            if (factura.getDetalles().isEmpty()) {
                mostrarError(null, "La factura debe tener al menos un producto");
                return;
            }

            guardarFactura(conn, factura);

        } catch (Exception e) {
            mostrarError(null, "Error: " + e.getMessage());
        }
    }

    private static void guardarFactura(Connection conn, Factura factura) throws SQLException {
        conn.setAutoCommit(false);
        try {
            int clienteId = obtenerIdCliente(conn, factura.getCliente());

            String sqlFactura = "INSERT INTO facturas (cliente_id, total) VALUES (?, ?)";
            PreparedStatement stmtFactura = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            stmtFactura.setInt(1, clienteId);
            stmtFactura.setDouble(2, factura.getTotal());
            stmtFactura.executeUpdate();

            ResultSet rs = stmtFactura.getGeneratedKeys();
            int facturaId = 0;
            if (rs.next()) {
                facturaId = rs.getInt(1);
                factura.setId(facturaId);
            }

            String sqlDetalle = "INSERT INTO factura_detalles (factura_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";
            
            PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle);
            PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock);

            for (DetalleFactura detalle : factura.getDetalles()) {
                stmtDetalle.setInt(1, facturaId);
                stmtDetalle.setInt(2, detalle.getProducto().getId());
                stmtDetalle.setInt(3, detalle.getCantidad());
                stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                stmtDetalle.setDouble(5, detalle.getSubtotal());
                stmtDetalle.executeUpdate();

                stmtStock.setInt(1, detalle.getCantidad());
                stmtStock.setInt(2, detalle.getProducto().getId());
                stmtStock.executeUpdate();
            }

            conn.commit();
            mostrarFacturaGenerada(factura);

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void mostrarFacturaGenerada(Factura factura) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("FACTURA GENERADA EXITOSAMENTE\n");
        mensaje.append("═══════════════════════════════════\n\n");
        mensaje.append("Factura #").append(factura.getId()).append("\n");
        mensaje.append("Cliente: ").append(factura.getCliente()).append("\n");
        mensaje.append("Fecha: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        mensaje.append("PRODUCTOS:\n");
        mensaje.append("───────────────────────────────────\n");
        for (DetalleFactura detalle : factura.getDetalles()) {
            mensaje.append("• ").append(detalle.toString()).append("\n");
        }
        mensaje.append("───────────────────────────────────\n");
        mensaje.append("TOTAL: ₡").append(String.format("%.2f", factura.getTotal()));

        JOptionPane.showMessageDialog(null, mensaje.toString(), "Factura Creada", JOptionPane.INFORMATION_MESSAGE);
    }

    private static ArrayList<Cliente> obtenerClientes(Connection conn) throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            clientes.add(new Cliente(rs.getString("nombre"), rs.getString("telefono")));
        }
        return clientes;
    }

    private static ArrayList<Producto> obtenerProductos(Connection conn) throws SQLException {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock > 0 ORDER BY nombre";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            productos.add(new Producto(rs.getInt("id"), rs.getString("nombre"), 
                       rs.getDouble("precio"), rs.getInt("stock")));
        }
        return productos;
    }

    private static int obtenerIdCliente(Connection conn, Cliente cliente) throws SQLException {
        String sql = "SELECT id FROM clientes WHERE nombre = ? AND telefono = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cliente.toString().split(" \\(")[0]);
        stmt.setString(2, cliente.toString().split("\\(")[1].replace(")", ""));
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        throw new SQLException("Cliente no encontrado");
    }

    private static void verFacturas() {
        JFrame frame = new JFrame("Registro de Facturas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Historial de Facturas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(TEXT_COLOR);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Crear tabla con estilo
        String[] columnas = {"ID", "Cliente", "Fecha", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setForeground(TEXT_COLOR);
        tabla.setBackground(BACKGROUND_COLOR);
        tabla.setRowHeight(25);
        tabla.setGridColor(BORDER_COLOR);
        tabla.setSelectionBackground(SECONDARY_COLOR);
        tabla.setSelectionForeground(Color.WHITE);

        // Centrar contenido de las columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        centerRenderer.setBackground(BACKGROUND_COLOR);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Llenar tabla
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT f.id, c.nombre, c.telefono, f.fecha_creacion, f.total " +
                        "FROM facturas f JOIN clientes c ON f.cliente_id = c.id " +
                        "ORDER BY f.fecha_creacion DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = {
                    "#" + rs.getInt("id"),
                    rs.getString("nombre") + " (" + rs.getString("telefono") + ")",
                    rs.getTimestamp("fecha_creacion").toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    "₡" + String.format("%.2f", rs.getDouble("total"))
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            mostrarError(frame, "Error al cargar facturas: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.setBackground(BACKGROUND_COLOR);
        JLabel statsLabel = new JLabel("Total de facturas: " + modelo.getRowCount());
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statsLabel.setForeground(TEXT_COLOR);
        statsPanel.add(statsLabel);

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Métodos auxiliares para el estilo
    private static JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.GRAY);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static JButton crearBotonMenu(String titulo, String descripcion) {
        JButton btn = new JButton("<html><div style='text-align: left;'><strong>" + titulo + "</strong><br/>" +
            "<small style='color: " + String.format("#%02x%02x%02x", BUTTON_TEXT_COLOR.getRed(), BUTTON_TEXT_COLOR.getGreen(), BUTTON_TEXT_COLOR.getBlue()) + ";'>" + descripcion + "</small></div></html>");
        btn.setBackground(BACKGROUND_COLOR);
        btn.setForeground(BUTTON_TEXT_COLOR);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    private static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setForeground(TEXT_COLOR);
        campo.setBackground(BACKGROUND_COLOR);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        campo.setEditable(true);
        campo.setEnabled(true);
        campo.setMinimumSize(new Dimension(150, 25));
    }

    private static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarError(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}