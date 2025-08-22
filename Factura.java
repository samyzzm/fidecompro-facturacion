import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Factura {
    private int id;
    private Cliente cliente;
    private LocalDateTime fechaCreacion;
    private ArrayList<DetalleFactura> detalles;
    private double total;

    public Factura(Cliente cliente) {
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

    public Factura(int id, Cliente cliente, LocalDateTime fechaCreacion, double total) {
        this.id = id;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.detalles = new ArrayList<>();
        this.total = total;
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        DetalleFactura detalle = new DetalleFactura(producto, cantidad);
        detalles.add(detalle);
        calcularTotal();
    }

    private void calcularTotal() {
        total = detalles.stream().mapToDouble(DetalleFactura::getSubtotal).sum();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public ArrayList<DetalleFactura> getDetalles() { return detalles; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Factura #" + id + " - " + cliente.toString() + " - " + 
               fechaCreacion.format(formatter) + " - Total: ¢" + String.format("%.2f", total);
    }
}