package com.bradescoa3.controller;

import com.bradescoa3.dao.UsuarioDAO;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.HashUtil;
import com.bradescoa3.util.SessaoUsuario;
import com.bradescoa3.util.ValidadorUtil;
import javax.swing.JOptionPane;

/**
 * Controlador para gerenciar operações de login e autenticação.
 */
public class LoginController {
    
    private final UsuarioDAO usuarioDAO;
    
    /**
     * Construtor padrão
     */
    public LoginController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Realiza a autenticação do usuário.
     * 
     * @param cpfCnpj CPF/CNPJ do usuário
     * @param senha Senha do usuário
     * @return true se a autenticação for bem-sucedida, false caso contrário
     */
    public boolean autenticar(String cpfCnpj, String senha) {
        // Validação básica
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ e senha são obrigatórios.",
                    "Erro de Autenticação", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        // Valida o formato do CPF/CNPJ
        if (!ValidadorUtil.validarCpfCnpj(cpfCnpj)) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ inválido.",
                    "Erro de Autenticação", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Tenta autenticar o usuário
        Usuario usuario = usuarioDAO.autenticar(cpfCnpj, senha);
        
        if (usuario != null) {
            // Autenticação bem-sucedida, armazena o usuário na sessão
            SessaoUsuario.getInstancia().setUsuarioLogado(usuario);
            return true;
        } else {
            // Autenticação falhou
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ ou senha incorretos.",
                    "Erro de Autenticação", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Verifica se o usuário é administrador.
     * 
     * @return true se o usuário logado for administrador, false caso contrário
     */
    public boolean isUsuarioAdmin() {
        return SessaoUsuario.getInstancia().isUsuarioAdmin();
    }
    
    /**
     * Realiza o logout do usuário.
     */
    public void logout() {
        SessaoUsuario.getInstancia().encerrarSessao();
    }
    
    /**
     * Verifica se há um usuário logado.
     * 
     * @return true se houver um usuário logado, false caso contrário
     */
    public boolean isUsuarioLogado() {
        return SessaoUsuario.getInstancia().isUsuarioLogado();
    }
    
    /**
     * Obtém o usuário logado.
     * 
     * @return O objeto Usuario logado ou null se não houver usuário logado
     */
    public Usuario getUsuarioLogado() {
        return SessaoUsuario.getInstancia().getUsuarioLogado();
    }
}
