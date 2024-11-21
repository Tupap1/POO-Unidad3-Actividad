interface interfaceLimpieza {
    void limpieza(String lugar);
    
}

interface interfaceReparacion {
    void reparacion(String objeto);
    
}


class Mantenimiento implements interfaceLimpieza, interfaceReparacion {
/*     interface interfaceMantenimiento{
        public void mantenimiento();
        public void reparacion();
        public void limpieza();
    } */

    @Override
    public void limpieza(String lugar) {
        System.out.println("Limpieza en " + lugar);
    }

    @Override
    public void reparacion(String objeto) {
        System.out.println("Reparacion de " + objeto);
    }




    
}

/* class Main {
    public static void main(String[] args) {
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.limpieza("banco");
        mantenimiento.reparacion("computadora");
    }
} */