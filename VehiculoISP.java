

/* interface interVehiculo{
    void conducir();
    void cargar();

}
 */

interface interConducir {
    void conducir(String destino);
    
}

interface interCargar{
    void cargar(double peso);
}

 class Vehiculo implements interConducir,interCargar {

    @Override
    public void conducir(String destino) {
        System.out.println("Conduciendo a "+destino);
    }

    @Override
    public void cargar(double peso) {
        System.out.println("Carga con un peso de " + peso +"KG");
        
    }
}

public class VehiculoISP {
    public static void main(String[] args) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.conducir("Casa");
        vehiculo.cargar(50);
    }
}