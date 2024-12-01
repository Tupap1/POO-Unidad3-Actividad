
public interface Almacenamiento {
    boolean guardarArchivo(String nombreArchivo, byte[] contenido);
    byte[] recuperarArchivo(String nombreArchivo);
    boolean eliminarArchivo(String nombreArchivo);
    List<String> listarArchivos();
}

public class AlmacenamientoLocal implements Almacenamiento {
    private final String directorioBase;
    
    public AlmacenamientoLocal(String directorioBase) {
        this.directorioBase = directorioBase;
        new File(directorioBase).mkdirs();
    }

    @Override
    public boolean guardarArchivo(String nombreArchivo, byte[] contenido) {
        try {
            Path rutaArchivo = Paths.get(directorioBase, nombreArchivo);
            Files.write(rutaArchivo, contenido);
            System.out.println("Archivo guardado localmente: " + nombreArchivo);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public byte[] recuperarArchivo(String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get(directorioBase, nombreArchivo);
            return Files.readAllBytes(rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al recuperar archivo: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean eliminarArchivo(String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get(directorioBase, nombreArchivo);
            return Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al eliminar archivo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> listarArchivos() {
        try {
            return Files.list(Paths.get(directorioBase))
                       .map(Path::getFileName)
                       .map(Path::toString)
                       .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error al listar archivos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

public class AlmacenamientoNube implements Almacenamiento {
    private final String proveedorNube;
    private final Map<String, byte[]> almacenamiento = new HashMap<>();  

    public AlmacenamientoNube(String proveedorNube) {
        this.proveedorNube = proveedorNube;
    }

    @Override
    public boolean guardarArchivo(String nombreArchivo, byte[] contenido) {
        try {
            almacenamiento.put(nombreArchivo, contenido);
            System.out.println("Archivo guardado en " + proveedorNube + ": " + nombreArchivo);
            return true;
        } catch (InterruptedException e) {
            System.err.println("Error al guardar en la nube: " + e.getMessage());
            return false;
        }
    }

    @Override
    public byte[] recuperarArchivo(String nombreArchivo) {
        try {
            return almacenamiento.get(nombreArchivo);
        } catch (InterruptedException e) {
            System.err.println("Error al recuperar de la nube: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean eliminarArchivo(String nombreArchivo) {
        try {
            return almacenamiento.remove(nombreArchivo) != null;
        } catch (InterruptedException e) {
            System.err.println("Error al eliminar de la nube: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> listarArchivos() {
        return new ArrayList<>(almacenamiento.keySet());
    }
}

public class GestorArchivos {
    private final Almacenamiento almacenamiento;

    public GestorArchivos(Almacenamiento almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public boolean guardarArchivo(String nombreArchivo, String contenido) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        
        return almacenamiento.guardarArchivo(nombreArchivo, contenido.getBytes());
    }

    public String recuperarArchivo(String nombreArchivo) {
        byte[] contenido = almacenamiento.recuperarArchivo(nombreArchivo);
        return contenido != null ? new String(contenido) : null;
    }

    public List<String> obtenerListaArchivos() {
        return almacenamiento.listarArchivos();
    }

    public boolean eliminarArchivo(String nombreArchivo) {
        return almacenamiento.eliminarArchivo(nombreArchivo);
    }
}

public class Main {
    public static void main(String[] args) {
        Almacenamiento almacenamientoLocal = new AlmacenamientoLocal("./archivos");
        GestorArchivos gestorLocal = new GestorArchivos(almacenamientoLocal);
        
        gestorLocal.guardarArchivo("documento1.txt", "Contenido del documento 1");
        System.out.println(gestorLocal.recuperarArchivo("documento1.txt"));

        Almacenamiento almacenamientoNube = new AlmacenamientoNube( "AWS S3");
        GestorArchivos gestorNube = new GestorArchivos(almacenamientoNube);
        
        gestorNube.guardarArchivo("documento2.txt", "Contenido del documento 2");
        System.out.println(gestorNube.recuperarArchivo("documento2.txt"));
    }
}