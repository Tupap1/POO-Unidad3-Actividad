
public interface GeneradorReporte {
    byte[] generarReporte(DatosReporte datos);
    String getFormatoReporte();
}


public class DatosReporte {
    private String titulo;
    private List<String> encabezados;
    private List<List<String>> datos;
    private String fechaGeneracion;

    public DatosReporte(String titulo, List<String> encabezados, List<List<String>> datos) {
        this.titulo = titulo;
        this.encabezados = encabezados;
        this.datos = datos;
        this.fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getTitulo() { return titulo; }
    public List<String> getEncabezados() { return encabezados; }
    public List<List<String>> getDatos() { return datos; }
    public String getFechaGeneracion() { return fechaGeneracion; }
}


public class ReportePDF implements GeneradorReporte {
    @Override
    public byte[] generarReporte(DatosReporte datos) {
        try {

            String contenido = "PDF Report\n";
            contenido += "Título: " + datos.getTitulo() + "\n";
            contenido += "Fecha: " + datos.getFechaGeneracion() + "\n\n";
            

            contenido += String.join(",", datos.getEncabezados()) + "\n";
    
            for (List<String> fila : datos.getDatos()) {
                contenido += String.join(",", fila) + "\n";
            }
            
            System.out.println("Generando reporte PDF...");
            return contenido.getBytes();
        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public String getFormatoReporte() {
        return "PDF";
    }
}


public class ReporteExcel implements GeneradorReporte {
    @Override
    public byte[] generarReporte(DatosReporte datos) {
        try {
   
            StringBuilder contenido = new StringBuilder();
            contenido.append("EXCEL Report\n");
            contenido.append("Título: ").append(datos.getTitulo()).append("\n");
            contenido.append("Fecha: ").append(datos.getFechaGeneracion()).append("\n\n");
            

            contenido.append(String.join("\t", datos.getEncabezados())).append("\n");
            

            for (List<String> fila : datos.getDatos()) {
                contenido.append(String.join("\t", fila)).append("\n");
            }
            
            System.out.println("Generando reporte Excel...");
            return contenido.toString().getBytes();
        } catch (Exception e) {
            System.err.println("Error al generar Excel: " + e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public String getFormatoReporte() {
        return "Excel";
    }
}

public class GestorReportes {
    private final GeneradorReporte generador;

    public GestorReportes(GeneradorReporte generador) {
        this.generador = generador;
    }

    public byte[] generarReporte(String titulo, List<String> encabezados, List<List<String>> datos) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (encabezados == null || encabezados.isEmpty()) {
            throw new IllegalArgumentException("Los encabezados son requeridos");
        }
        if (datos == null || datos.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para generar el reporte");
        }

        DatosReporte datosReporte = new DatosReporte(titulo, encabezados, datos);
        
        byte[] reporte = generador.generarReporte(datosReporte);
        
        if (reporte.length == 0) {
            throw new RuntimeException("Error al generar el reporte en formato " + generador.getFormatoReporte());
        }
        
        return reporte;
    }

    public String getFormatoActual() {
        return generador.getFormatoReporte();
    }
}


public class Main {
    public static void main(String[] args) {
        
        List<String> encabezados = Arrays.asList("ID", "Nombre", "Edad");
        List<List<String>> datos = Arrays.asList(
            Arrays.asList("1", "Juan", "25"),
            Arrays.asList("2", "María", "30"),
            Arrays.asList("3", "Pedro", "28")
        );

        
        GeneradorReporte generadorPDF = new ReportePDF();
        GestorReportes gestorPDF = new GestorReportes(generadorPDF);
        byte[] reportePDF = gestorPDF.generarReporte("Reporte de Empleados", encabezados, datos);
        System.out.println(new String(reportePDF));

        
        GeneradorReporte generadorExcel = new ReporteExcel();
        GestorReportes gestorExcel = new GestorReportes(generadorExcel);
        byte[] reporteExcel = gestorExcel.generarReporte("Reporte de Empleados", encabezados, datos);
        System.out.println(new String(reporteExcel));
    }
}