package com.bradescoa3.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.swing.JOptionPane;

/**
 * Classe utilitária para operações relacionadas a senhas.
 * Implementa hash SHA-256 para senhas.
 */
public class HashUtil {
    
    /**
     * Gera um hash SHA-256 para a senha fornecida.
     * 
     * @param senha A senha em texto plano
     * @return String contendo o hash da senha em formato hexadecimal
     */
    public static String gerarHash(String senha) {
        try {
            // Cria uma instância do algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Converte a senha para bytes e gera o hash
            byte[] hashBytes = digest.digest(senha.getBytes());
            
            // Converte os bytes do hash para representação hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao gerar hash da senha: " + e.getMessage(),
                    "Erro de Segurança", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public static boolean verificarSenha(String senhaPlana, String hashArmazenado) {
        if (senhaPlana == null || hashArmazenado == null) {
            return false;
        }
        
        // Gera o hash da senha fornecida e compara com o hash armazenado
        String hashSenhaFornecida = gerarHash(senhaPlana);
        return hashArmazenado.equals(hashSenhaFornecida);
    }
    
    /**
     * Gera uma senha aleatória para recuperação de conta.
     * 
     * @param tamanho Tamanho da senha a ser gerada
     * @return String contendo a senha gerada
     */
    public static String gerarSenhaAleatoria(int tamanho) {
        // Caracteres permitidos na senha (apenas números conforme requisito)
        String caracteresPermitidos = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(tamanho);
        
        // Gera a senha aleatória
        for (int i = 0; i < tamanho; i++) {
            int indiceAleatorio = random.nextInt(caracteresPermitidos.length());
            senha.append(caracteresPermitidos.charAt(indiceAleatorio));
        }
        
        return senha.toString();
    }
}
