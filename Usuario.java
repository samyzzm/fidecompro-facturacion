public class Usuario {
    protected String usuario;
    protected String contrasena;

    public Usuario(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean login(String u, String c) {
        return usuario.equals(u) && contrasena.equals(c);
    }

    public String getRol() {
        return "GENÉRICO";
    }
}