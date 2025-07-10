public class UsuarioVendedor extends Usuario {

    public UsuarioVendedor(String nombre, String usuario, String contrasena) {
        super(nombre, usuario, contrasena);
    }

    @Override
    public boolean login(String usuario, String contrasena) {
        System.out.println("Autenticando como VENDEDOR...");
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }
}
