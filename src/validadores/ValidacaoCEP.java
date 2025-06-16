package validadores;

public class ValidacaoCEP {
    public static boolean validar(String cep) {

        if (cep == null || !cep.matches("\\d{8}")) {
            return false;
        }

        String numeros = cep.replaceAll("[^0-9]", "");
        if (numeros.chars().distinct().count() == 1) {
            return false;
        }
        
        return true;
    }
}