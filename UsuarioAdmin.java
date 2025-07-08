public class UsuarioAdmin extends Usuario {
    public UsuarioAdmin(String usuario, String contrasena) {
        super(usuario, contrasena);
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }
}