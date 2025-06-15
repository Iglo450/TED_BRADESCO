package com.bradescoa3.controller;

import com.bradescoa3.dao.DenunciaDAO;
import com.bradescoa3.dao.UsuarioDAO;
import com.bradescoa3.model.Denuncia;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.HashUtil;
import com.bradescoa3.util.SessaoUsuario;
import java.util.List;

/**
 * Controller para operações relacionadas a denúncias.
 */
public class AdminController {
    
    private final DenunciaDAO denunciaDAO;
    private final UsuarioDAO usuarioDAO;
    
    /**
     * Construtor padrão
     */
    public AdminController() {
        this.denunciaDAO = new DenunciaDAO();
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Lista todas as denúncias cadastradas no sistema.
     * 
     * @return Array bidimensional com os dados das denúncias (CPF/CNPJ, Nome, Motivo)
     * @throws Exception Se ocorrer algum erro ao listar as denúncias
     */
    public Object[][] listarDenuncias() throws Exception {
        List<Denuncia> denuncias = denunciaDAO.listarTodas();
        
        // Cria um array bidimensional para armazenar os dados
        Object[][] dados = new Object[denuncias.size()][3];
        
        // Preenche o array com os dados das denúncias
        for (int i = 0; i < denuncias.size(); i++) {
            Denuncia denuncia = denuncias.get(i);
            dados[i][0] = denuncia.getCpfCnpjDenunciado();
            
            // Busca o nome do denunciado
            Usuario denunciado = usuarioDAO.buscarPorCpfCnpj(denuncia.getCpfCnpjDenunciado());
            dados[i][1] = denunciado != null ? denunciado.getNome() : "Desconhecido";
            
            dados[i][2] = denuncia.getMotivo();
        }
        
        return dados;
    }
    
    /**
     * Remove uma denúncia do sistema.
     * 
     * @param cpfCnpj CPF/CNPJ do denunciado
     * @return true se a denúncia foi removida com sucesso, false caso contrário
     * @throws Exception Se ocorrer algum erro ao remover a denúncia
     */
    public boolean removerDenuncia(String cpfCnpj) throws Exception {
        // Verifica se o usuário logado é administrador
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
            throw new Exception("Apenas administradores podem remover denúncias.");
        }
        
        // Remove a denúncia
        return denunciaDAO.remover(cpfCnpj);
    }
}
