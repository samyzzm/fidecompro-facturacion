import javax.swing.JOptionPane;

public class Cliente {
    private String nombre;
    private String telefono;

    public Cliente(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public void guardar() {
       
        JOptionPane.showMessageDialog(null, "Cliente '" + nombre + "' guardado exitosamente.");
    }

    @Override
    public String toString() {
        return nombre + " (" + telefono + ")";
    }
}
