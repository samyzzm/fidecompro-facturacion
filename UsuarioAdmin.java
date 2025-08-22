public class UsuarioAdmin extends Usuario {

    public UsuarioAdmin(String nombre, String usuario, String contrasena) {
        super(nombre, usuario, contrasena);
    }

    @Override
    public boolean login(String usuario, String contrasena) {
        System.out.println("Autenticando como ADMIN...");
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }
}
