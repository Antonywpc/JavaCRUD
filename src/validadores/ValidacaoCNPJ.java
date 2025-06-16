package validadores;

public class ValidacaoCNPJ {
    public static boolean validar(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            return false;
        }

       
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
        }

        int primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        if (primeiroDigito != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
        }

        int segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return segundoDigito == Character.getNumericValue(cnpj.charAt(13));
    }
}