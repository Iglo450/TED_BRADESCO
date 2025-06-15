package com.bradescoa3.dao;

import com.bradescoa3.model.Denuncia;
import com.bradescoa3.util.ConexaoDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados para a entidade Denuncia.
 */
public class DenunciaDAO {
    
    /**
     * Registra uma nova denúncia no banco de dados.
     * 
     * @param denuncia Objeto Denuncia a ser registrado
     * @return true se a denúncia foi registrada com sucesso, false caso contrário
     * @throws Exception Se ocorrer algum erro durante o registro
     */
    public boolean registrar(Denuncia denuncia) throws Exception {
        String sql = "INSERT INTO Denuncia (cpf_cnpj_denunciante, cpf_cnpj_denunciado, motivo, data_denuncia) " +
                     "VALUES (?, ?, ?, GETDATE())";
        
        try (Connection conn = ConexaoDb.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, denuncia.getCpfCnpjDenunciante());
            pstmt.setString(2, denuncia.getCpfCnpjDenunciado());
            pstmt.setString(3, denuncia.getMotivo());
            
            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }
    
    /**
     * Conta o número de denúncias para um determinado CPF/CNPJ.
     * 
     * @param cpfCnpj CPF/CNPJ a ser verificado
     * @return Número de denúncias registradas para o CPF/CNPJ
     * @throws Exception Se ocorrer algum erro durante a consulta
     */
    public int contarDenuncias(String cpfCnpj) throws Exception {
        String sql = "SELECT COUNT(*) FROM Denuncia WHERE cpf_cnpj_denunciado = ?";
        
        try (Connection conn = ConexaoDb.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpj);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
    
    /**
     * Lista todas as denúncias registradas no sistema.
     * 
     * @return Lista de objetos Denuncia
     * @throws Exception Se ocorrer algum erro durante a consulta
     */
    public List<Denuncia> listarTodas() throws Exception {
        String sql = "SELECT id, cpf_cnpj_denunciante, cpf_cnpj_denunciado, motivo, data_denuncia " +
                     "FROM Denuncia ORDER BY data_denuncia DESC";
        
        List<Denuncia> denuncias = new ArrayList<>();
        
        try (Connection conn = ConexaoDb.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("id"));
                denuncia.setCpfCnpjDenunciante(rs.getString("cpf_cnpj_denunciante"));
                denuncia.setCpfCnpjDenunciado(rs.getString("cpf_cnpj_denunciado"));
                denuncia.setMotivo(rs.getString("motivo"));
                denuncia.setDataDenuncia(rs.getTimestamp("data_denuncia"));
                
                denuncias.add(denuncia);
            }
        }
        
        return denuncias;
    }
    
    /**
     * Lista as denúncias feitas por um determinado usuário.
     * 
     * @param cpfCnpjDenunciante CPF/CNPJ do denunciante
     * @return Lista de objetos Denuncia
     * @throws Exception Se ocorrer algum erro durante a consulta
     */
    public List<Denuncia> listarPorDenunciante(String cpfCnpjDenunciante) throws Exception {
        String sql = "SELECT id, cpf_cnpj_denunciante, cpf_cnpj_denunciado, motivo, data_denuncia " +
                     "FROM Denuncia WHERE cpf_cnpj_denunciante = ? ORDER BY data_denuncia DESC";
        
        List<Denuncia> denuncias = new ArrayList<>();
        
        try (Connection conn = ConexaoDb.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpjDenunciante);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Denuncia denuncia = new Denuncia();
                    denuncia.setId(rs.getInt("id"));
                    denuncia.setCpfCnpjDenunciante(rs.getString("cpf_cnpj_denunciante"));
                    denuncia.setCpfCnpjDenunciado(rs.getString("cpf_cnpj_denunciado"));
                    denuncia.setMotivo(rs.getString("motivo"));
                    denuncia.setDataDenuncia(rs.getTimestamp("data_denuncia"));
                    
                    denuncias.add(denuncia);
                }
            }
        }
        
        return denuncias;
    }
    
    /**
     * Remove uma denúncia do sistema.
     * 
     * @param cpfCnpj CPF/CNPJ do denunciado
     * @return true se a denúncia foi removida com sucesso, false caso contrário
     * @throws Exception Se ocorrer algum erro durante a remoção
     */
    public boolean remover(String cpfCnpj) throws Exception {
        String sql = "DELETE FROM Denuncia WHERE cpf_cnpj_denunciado = ?";
        
        try (Connection conn = ConexaoDb.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpj);
            
            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }
}
