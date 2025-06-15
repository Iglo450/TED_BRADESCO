package com.bradescoa3.controller;

import com.bradescoa3.dao.UsuarioDAO;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.HashUtil;
import com.bradescoa3.util.ValidadorUtil;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

/**
 * Controlador para gerenciar operações de cadastro de usuários.
 */
public class CadastroController {
    
    private final UsuarioDAO usuarioDAO;
    
    /**
     * Construtor padrão
     */
    public CadastroController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Cadastra um novo usuário no sistema.
     * 
     * @param cpfCnpj CPF/CNPJ do usuário
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário (será convertida para hash)
     * @param agencia Agência bancária
     * @param tipoConta Tipo de conta (Corrente ou Poupança)
     * @param codigoBanco Código do banco
     * @return true se o cadastro for bem-sucedido, false caso contrário
     */
    public boolean cadastrarUsuario(String cpfCnpj, String nome, String email, 
                                   String senha, String agencia, String tipoConta, 
                                   String codigoBanco) {
        // Validação dos campos obrigatórios
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty() ||
            nome == null || nome.trim().isEmpty() ||
            senha == null || senha.trim().isEmpty() ||
            agencia == null || agencia.trim().isEmpty() ||
            tipoConta == null || tipoConta.trim().isEmpty() ||
            codigoBanco == null || codigoBanco.trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(null, 
                    "Todos os campos são obrigatórios.",
                    "Erro de Cadastro", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        // Valida o formato do CPF/CNPJ
        if (!ValidadorUtil.validarCpfCnpj(cpfCnpj)) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ inválido.",
                    "Erro de Cadastro", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o CPF/CNPJ já está cadastrado
        if (usuarioDAO.cpfCnpjJaExiste(cpfCnpj)) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ já cadastrado no sistema.",
                    "Erro de Cadastro", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Valida o formato do email (básico)
        if (email != null && !email.trim().isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(null, 
                    "Email inválido.",
                    "Erro de Cadastro", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Cria o objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setCpfCnpj(cpfCnpj);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenhaHash(HashUtil.gerarHash(senha));
        usuario.setAgencia(agencia);
        usuario.setTipoConta(tipoConta);
        usuario.setCodigoBanco(codigoBanco);
        usuario.setDataCriacao(LocalDateTime.now());
        
        // Insere o usuário no banco de dados
        int id = usuarioDAO.inserir(usuario);
        
        if (id > 0) {
            JOptionPane.showMessageDialog(null, 
                    "Usuário cadastrado com sucesso!",
                    "Cadastro", 
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao cadastrar usuário. Tente novamente.",
                    "Erro de Cadastro", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Verifica se um CPF/CNPJ já está cadastrado.
     * 
     * @param cpfCnpj O CPF/CNPJ a ser verificado
     * @return true se já existir, false caso contrário
     */
    public boolean cpfCnpjJaExiste(String cpfCnpj) {
        // Remove caracteres não numéricos
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        return usuarioDAO.cpfCnpjJaExiste(cpfCnpj);
    }
    
    /**
     * Valida o formato de um CPF/CNPJ.
     * 
     * @param cpfCnpj O CPF/CNPJ a ser validado
     * @return true se for válido, false caso contrário
     */
    public boolean validarCpfCnpj(String cpfCnpj) {
        // Remove caracteres não numéricos
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        return ValidadorUtil.validarCpfCnpj(cpfCnpj);
    }
}
