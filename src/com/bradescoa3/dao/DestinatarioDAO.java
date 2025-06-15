package com.bradescoa3.dao;

import com.bradescoa3.model.Destinatario;
import com.bradescoa3.util.ConexaoDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe DAO (Data Access Object) para operações de CRUD da entidade Destinatario.
 */
public class DestinatarioDAO {
    
    /**
     * Insere um novo destinatário no banco de dados.
     * 
     * @param destinatario O objeto Destinatario a ser inserido
     * @return O ID gerado para o destinatário inserido ou -1 em caso de erro
     */
    public int inserir(Destinatario destinatario) {
        String sql = "INSERT INTO DESTINATARIO (DES_CONTA_STR, DES_NOME_STR, DES_AGENCIA_STR, " +
                     "DES_TIPO_CONTA_STR, DES_BANCO_STR) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, destinatario.getCpfCnpj());
            pstmt.setString(2, destinatario.getNome());
            pstmt.setString(3, destinatario.getAgencia());
            pstmt.setString(4, destinatario.getTipoConta());
            pstmt.setString(5, destinatario.getCodigoBanco());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        destinatario.setId(generatedKeys.getInt(1));
                        return destinatario.getId();
                    }
                }
            }
            
            return -1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao inserir destinatário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    /**
     * Atualiza um destinatário existente no banco de dados.
     * 
     * @param destinatario O objeto Destinatario com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Destinatario destinatario) {
        String sql = "UPDATE DESTINATARIO SET DES_NOME_STR = ?, DES_AGENCIA_STR = ?, " +
                     "DES_TIPO_CONTA_STR = ?, DES_BANCO_STR = ? WHERE DES_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, destinatario.getNome());
            pstmt.setString(2, destinatario.getAgencia());
            pstmt.setString(3, destinatario.getTipoConta());
            pstmt.setString(4, destinatario.getCodigoBanco());
            pstmt.setInt(5, destinatario.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao atualizar destinatário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Busca um destinatário pelo ID.
     * 
     * @param destinatarioId O ID do destinatário a ser buscado
     * @return O objeto Destinatario encontrado ou null se não encontrado
     */
    public Destinatario buscarPorId(int destinatarioId) {
        String sql = "SELECT * FROM DESTINATARIO WHERE DES_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, destinatarioId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaDestinatario(rs);
                }
            }
            
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao buscar destinatário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Busca um destinatário pelo CPF/CNPJ.
     * 
     * @param cpfCnpj O CPF/CNPJ do destinatário a ser buscado
     * @return O objeto Destinatario encontrado ou null se não encontrado
     */
    public Destinatario buscarPorCpfCnpj(String cpfCnpj) {
        String sql = "SELECT * FROM DESTINATARIO WHERE DES_CONTA_STR = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpj);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaDestinatario(rs);
                }
            }
            
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao buscar destinatário por CPF/CNPJ: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Lista todos os destinatários cadastrados.
     * 
     * @return Lista de objetos Destinatario
     */
    public List<Destinatario> listarTodos() {
        List<Destinatario> destinatarios = new ArrayList<>();
        String sql = "SELECT * FROM DESTINATARIO ORDER BY DES_NOME_STR";
        
        try (Connection conn = ConexaoDb.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                destinatarios.add(mapearResultSetParaDestinatario(rs));
            }
            
            return destinatarios;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar destinatários: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Verifica se um CPF/CNPJ já está cadastrado como destinatário.
     * 
     * @param cpfCnpj O CPF/CNPJ a ser verificado
     * @return true se já existir, false caso contrário
     */
    public boolean cpfCnpjJaExiste(String cpfCnpj) {
        String sql = "SELECT COUNT(*) FROM DESTINATARIO WHERE DES_CONTA_STR = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpj);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao verificar CPF/CNPJ: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Mapeia um ResultSet para um objeto Destinatario.
     * 
     * @param rs O ResultSet contendo os dados do destinatário
     * @return O objeto Destinatario mapeado
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet
     */
    private Destinatario mapearResultSetParaDestinatario(ResultSet rs) throws SQLException {
        Destinatario destinatario = new Destinatario();
        destinatario.setId(rs.getInt("DES_ID_INT"));
        destinatario.setCpfCnpj(rs.getString("DES_CONTA_STR"));
        destinatario.setNome(rs.getString("DES_NOME_STR"));
        destinatario.setAgencia(rs.getString("DES_AGENCIA_STR"));
        destinatario.setTipoConta(rs.getString("DES_TIPO_CONTA_STR"));
        destinatario.setCodigoBanco(rs.getString("DES_BANCO_STR"));
        
        return destinatario;
    }
}
