package com.bradescoa3.util;

/**
 * Classe utilitária para validação de CPF e CNPJ.
 */
public class ValidadorUtil {
    
    /**
     * Valida um CPF.
     * 
     * @param cpf O CPF a ser validado (apenas números)
     * @return true se o CPF for válido, false caso contrário
     */
    public static boolean validarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (caso inválido)
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }
        
        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica o primeiro dígito verificador
        if (dv1 != (cpf.charAt(9) - '0')) {
            return false;
        }
        
        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica o segundo dígito verificador
        return dv2 == (cpf.charAt(10) - '0');
    }
    
    /**
     * Valida um CNPJ.
     * 
     * @param cnpj O CNPJ a ser validado (apenas números)
     * @return true se o CNPJ for válido, false caso contrário
     */
    public static boolean validarCNPJ(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (caso inválido)
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }
        
        // Calcula o primeiro dígito verificador
        int[] multiplicadores1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores1[i];
        }
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica o primeiro dígito verificador
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }
        
        // Calcula o segundo dígito verificador
        int[] multiplicadores2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * multiplicadores2[i];
        }
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica o segundo dígito verificador
        return dv2 == (cnpj.charAt(13) - '0');
    }
    
    /**
     * Valida um CPF ou CNPJ.
     * 
     * @param documento O CPF ou CNPJ a ser validado (apenas números)
     * @return true se o documento for válido, false caso contrário
     */
    public static boolean validarCpfCnpj(String documento) {
        // Remove caracteres não numéricos
        documento = documento.replaceAll("[^0-9]", "");
        
        // Verifica se é CPF (11 dígitos) ou CNPJ (14 dígitos)
        if (documento.length() == 11) {
            return validarCPF(documento);
        } else if (documento.length() == 14) {
            return validarCNPJ(documento);
        } else {
            return false;
        }
    }
    
    /**
     * Formata um CPF com a máscara padrão (XXX.XXX.XXX-XX).
     * 
     * @param cpf O CPF a ser formatado (apenas números)
     * @return O CPF formatado ou o valor original se não for possível formatar
     */
    public static String formatarCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return cpf;
        }
        
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9);
    }
    
    /**
     * Formata um CNPJ com a máscara padrão (XX.XXX.XXX/XXXX-XX).
     * 
     * @param cnpj O CNPJ a ser formatado (apenas números)
     * @return O CNPJ formatado ou o valor original se não for possível formatar
     */
    public static String formatarCNPJ(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14) {
            return cnpj;
        }
        
        return cnpj.substring(0, 2) + "." + 
               cnpj.substring(2, 5) + "." + 
               cnpj.substring(5, 8) + "/" + 
               cnpj.substring(8, 12) + "-" + 
               cnpj.substring(12);
    }
    
    /**
     * Formata um CPF ou CNPJ com a máscara padrão.
     * 
     * @param documento O CPF ou CNPJ a ser formatado (apenas números)
     * @return O documento formatado ou o valor original se não for possível formatar
     */
    public static String formatarCpfCnpj(String documento) {
        // Remove caracteres não numéricos
        documento = documento.replaceAll("[^0-9]", "");
        
        // Verifica se é CPF (11 dígitos) ou CNPJ (14 dígitos)
        if (documento.length() == 11) {
            return formatarCPF(documento);
        } else if (documento.length() == 14) {
            return formatarCNPJ(documento);
        } else {
            return documento;
        }
    }
}
