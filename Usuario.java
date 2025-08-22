public abstract class Usuario {
    protected String nombre;
    protected String usuario;
    protected String contrasena;

    public Usuario(String nombre, String usuario, String contrasena) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public abstract boolean login(String usuario, String contrasena);

    public String getNombre() {
        return nombre;
    }
}
