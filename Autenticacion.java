public interface ServicioAutenticacion {
    boolean autenticar(String usuario, String credencial);
}

public class AutenticacionLocal implements ServicioAutenticacion {
    @Override
    public boolean autenticar(String usuario, String password) {
        System.out.println("Autenticando localmente al usuario: " + usuario);
        return password != null && password.length() >= 8;
    }
}

public class AutenticacionOAuth implements ServicioAutenticacion {
    private final String proveedorOAuth;

    public AutenticacionOAuth(String proveedorOAuth) {
        this.proveedorOAuth = proveedorOAuth;
    }

    @Override
    public boolean autenticar(String usuario, String token) {
        System.out.println("Autenticando con " + proveedorOAuth + " para usuario: " + usuario);
        return token != null && token.startsWith("oauth_");
    }
}

public class GestorAutenticacion {
    private final ServicioAutenticacion servicioAutenticacion;

    public GestorAutenticacion(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    public boolean iniciarSesion(String usuario, String credencial) {
        // Validaciones básicas
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        
        if (credencial == null || credencial.trim().isEmpty()) {
            throw new IllegalArgumentException("La credencial no puede estar vacía");
        }

        boolean autenticado = servicioAutenticacion.autenticar(usuario, credencial);
        
        if (autenticado) {
            System.out.println("Usuario " + usuario + " autenticado exitosamente");
        } else {
            System.out.println("Fallo en la autenticación para el usuario " + usuario);
        }
        
        return autenticado;
    }
}

public class Main {
    public static void main(String[] args) {

        ServicioAutenticacion authLocal = new AutenticacionLocal();
        GestorAutenticacion gestorLocal = new GestorAutenticacion(authLocal);
        gestorLocal.iniciarSesion("usuario1", "password123");


        ServicioAutenticacion authOAuth = new AutenticacionOAuth("Google");
        GestorAutenticacion gestorOAuth = new GestorAutenticacion(authOAuth);
        gestorOAuth.iniciarSesion("usuario2", "oauth_token123");
    }
}