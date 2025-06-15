package com.bradescoa3.controller;

import com.bradescoa3.dao.DenunciaDAO;
import com.bradescoa3.dao.UsuarioDAO;
import com.bradescoa3.model.Denuncia;
import com.bradescoa3.model.Quarentena;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.HashUtil;
import com.bradescoa3.util.SessaoUsuario;
import com.bradescoa3.util.ValidadorUtil;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controlador para gerenciar operações de denúncias.
 */
public class DenunciaController {
    
    private final DenunciaDAO denunciaDAO;
    private final UsuarioDAO usuarioDAO;
    
    /**
     * Construtor padrão
     */
    public DenunciaController() {
        this.denunciaDAO = new DenunciaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Registra uma nova denúncia no sistema.
     * 
     * @param cpfCnpjDenunciado CPF/CNPJ do denunciado
     * @param motivo Motivo da denúncia
     * @param senha Senha do usuário para confirmação
     * @return true se a denúncia for registrada com sucesso, false caso contrário
     */
    public boolean registrarDenuncia(String cpfCnpjDenunciado, String motivo, String senha) {
        // Validação dos campos obrigatórios
        if (cpfCnpjDenunciado == null || cpfCnpjDenunciado.trim().isEmpty() ||
            motivo == null || motivo.trim().isEmpty() ||
            senha == null || senha.trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(null, 
                    "Todos os campos são obrigatórios.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpjDenunciado = cpfCnpjDenunciado.replaceAll("[^0-9]", "");
        
        // Valida o formato do CPF/CNPJ
        if (!ValidadorUtil.validarCpfCnpj(cpfCnpjDenunciado)) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ inválido.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o usuário está logado
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(null, 
                    "Você precisa estar logado para registrar uma denúncia.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se a senha está correta
        if (!HashUtil.verificarSenha(senha, usuarioLogado.getSenhaHash())) {
            JOptionPane.showMessageDialog(null, 
                    "Senha incorreta.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o usuário está tentando denunciar a si mesmo
        if (cpfCnpjDenunciado.equals(usuarioLogado.getCpfCnpj())) {
            JOptionPane.showMessageDialog(null, 
                    "Você não pode denunciar a si mesmo.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Cria o objeto Denuncia
        Denuncia denuncia = new Denuncia();
        denuncia.setUsuarioDenuncianteId(usuarioLogado.getId());
        denuncia.setCpfCnpjDenunciado(cpfCnpjDenunciado);
        denuncia.setMotivo(motivo);
        denuncia.setDataDenuncia(LocalDateTime.now());
        
        // Insere a denúncia no banco de dados
        int id = denunciaDAO.inserir(denuncia);
        
        if (id > 0) {
            JOptionPane.showMessageDialog(null, 
                    "Denúncia registrada com sucesso!",
                    "Denúncia", 
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao registrar denúncia. Tente novamente.",
                    "Erro de Denúncia", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Lista todas as denúncias feitas pelo usuário logado.
     * 
     * @return Lista de objetos Denuncia
     */
    public List<Denuncia> listarDenunciasDoUsuario() {
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuarioLogado == null) {
            return null;
        }
        
        return denunciaDAO.listarPorUsuario(usuarioLogado.getId());
    }
    
    /**
     * Lista todas as denúncias contra um CPF/CNPJ.
     * 
     * @param cpfCnpj O CPF/CNPJ do denunciado
     * @return Lista de objetos Denuncia
     */
    public List<Denuncia> listarDenunciasPorCpfCnpj(String cpfCnpj) {
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        return denunciaDAO.listarPorCpfCnpj(cpfCnpj);
    }
    
    /**
     * Lista todas as denúncias (apenas para administradores).
     * 
     * @return Lista de objetos Denuncia ou null se o usuário não for administrador
     */
    public List<Denuncia> listarTodasDenuncias() {
        if (!SessaoUsuario.getInstancia().isUsuarioAdmin()) {
            JOptionPane.showMessageDialog(null, 
                    "Apenas administradores podem acessar todas as denúncias.",
                    "Acesso Negado", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        return denunciaDAO.listarTodas();
    }
    
    /**
     * Verifica se um CPF/CNPJ está em quarentena.
     * 
     * @param cpfCnpj O CPF/CNPJ a ser verificado
     * @return O objeto Quarentena se estiver em quarentena, null caso contrário
     */
    public Quarentena verificarQuarentena(String cpfCnpj) {
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        return denunciaDAO.verificarQuarentena(cpfCnpj);
    }
    
    /**
     * Conta o número de denúncias contra um CPF/CNPJ.
     * 
     * @param cpfCnpj O CPF/CNPJ do denunciado
     * @return O número de denúncias
     */
    public int contarDenunciasPorCpfCnpj(String cpfCnpj) {
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        return denunciaDAO.contarDenunciasPorCpfCnpj(cpfCnpj);
    }
}
