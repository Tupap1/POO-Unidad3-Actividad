public interface OpBancariaISP {
    void transferencia(String origen, String destino, double monto);
    void retiro(String cuenta, double monto);
    void pago(String cuenta, double monto);
    
}


    interface interTransferencia {
        void transferencia(String origen, String destino, double monto);
    }
    
    interface interRetiro {
        void retiro(String cuenta, double monto);
    }
    
    interface interPago {
        void pago(String cuenta, double monto);
    }



@SuppressWarnings("unused")
class OperacionesBancarias implements interTransferencia, interRetiro, interPago {
    @Override
    public void transferencia(String origen, String destino, double monto) {
        System.out.println("Transferencia de " + monto + " desde " + origen + " a " + destino);
    }        
    @Override
    public void retiro(String cuenta, double monto) {
        System.out.println("Retiro de " + monto + " de la cuenta " + cuenta);
    }
    @Override
    public void pago(String cuenta, double monto) {
        System.out.println("Pago de " + monto + " a la cuenta " + cuenta);
    }
    




}

/* class Main {
    public static void main(String[] args) {
        OperacionesBancarias operacionesBancarias = new OperacionesBancarias();
        operacionesBancarias.transferencia("cuenta1", "cuenta2", 1000);
        operacionesBancarias.retiro("cuenta3", 500);
        operacionesBancarias.pago("cuenta4", 2000);
    }
} */