package com.bradescoa3.dao;

import com.bradescoa3.model.Transferencia;
import com.bradescoa3.model.StatusTransferencia;
import com.bradescoa3.util.ConexaoDb;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe DAO (Data Access Object) para operações de CRUD da entidade Transferencia.
 */
public class TransferenciaDAO {
    
    /**
     * Insere uma nova transferência no banco de dados.
     * 
     * @param transferencia O objeto Transferencia a ser inserido
     * @return O ID gerado para a transferência inserida ou -1 em caso de erro
     */
    public int inserir(Transferencia transferencia) {
        String sql = "INSERT INTO TRANSFERENCIA (USR_ID_INT, DES_ID_INT, TRF_VALOR_DEC, " +
                     "TRF_DATAHR_DATE, TRF_STATUS_STR, TRF_TOKEN_BOOL) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transferencia.getUsuarioRemetenteId());
            pstmt.setInt(2, transferencia.getDestinatarioId());
            pstmt.setBigDecimal(3, transferencia.getValor());
            pstmt.setTimestamp(4, Timestamp.valueOf(transferencia.getDataHoraTransacao()));
            pstmt.setString(5, transferencia.getStatus().toString());
            pstmt.setBoolean(6, transferencia.isTokenValidado());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transferencia.setId(generatedKeys.getInt(1));
                        return transferencia.getId();
                    }
                }
            }
            
            return -1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao inserir transferência: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    /**
     * Atualiza o status de uma transferência existente no banco de dados.
     * 
     * @param transferenciaId O ID da transferência
     * @param novoStatus O novo status da transferência
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarStatus(int transferenciaId, StatusTransferencia novoStatus) {
        String sql = "UPDATE TRANSFERENCIA SET TRF_STATUS_STR = ? WHERE TRF_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, novoStatus.toString());
            pstmt.setInt(2, transferenciaId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao atualizar status da transferência: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Atualiza o token de validação de uma transferência existente no banco de dados.
     * 
     * @param transferenciaId O ID da transferência
     * @param tokenValidado O novo valor do token de validação
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarTokenValidado(int transferenciaId, boolean tokenValidado) {
        String sql = "UPDATE TRANSFERENCIA SET TRF_TOKEN_BOOL = ? WHERE TRF_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, tokenValidado);
            pstmt.setInt(2, transferenciaId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao atualizar token da transferência: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Busca uma transferência pelo ID.
     * 
     * @param transferenciaId O ID da transferência a ser buscada
     * @return O objeto Transferencia encontrado ou null se não encontrado
     */
    public Transferencia buscarPorId(int transferenciaId) {
        String sql = "SELECT * FROM TRANSFERENCIA WHERE TRF_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transferenciaId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaTransferencia(rs);
                }
            }
            
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao buscar transferência: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Lista todas as transferências de um usuário.
     * 
     * @param usuarioId O ID do usuário remetente
     * @return Lista de objetos Transferencia
     */
    public List<Transferencia> listarPorUsuario(int usuarioId) {
        List<Transferencia> transferencias = new ArrayList<>();
        String sql = "SELECT * FROM TRANSFERENCIA WHERE USR_ID_INT = ? ORDER BY TRF_DATAHR_DATE DESC";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transferencias.add(mapearResultSetParaTransferencia(rs));
                }
            }
            
            return transferencias;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar transferências do usuário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Lista todas as transferências para um destinatário.
     * 
     * @param destinatarioId O ID do destinatário
     * @return Lista de objetos Transferencia
     */
    public List<Transferencia> listarPorDestinatario(int destinatarioId) {
        List<Transferencia> transferencias = new ArrayList<>();
        String sql = "SELECT * FROM TRANSFERENCIA WHERE DES_ID_INT = ? ORDER BY TRF_DATAHR_DATE DESC";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, destinatarioId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transferencias.add(mapearResultSetParaTransferencia(rs));
                }
            }
            
            return transferencias;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar transferências para o destinatário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Lista todas as transferências com um determinado status.
     * 
     * @param status O status das transferências a serem buscadas
     * @return Lista de objetos Transferencia
     */
    public List<Transferencia> listarPorStatus(StatusTransferencia status) {
        List<Transferencia> transferencias = new ArrayList<>();
        String sql = "SELECT * FROM TRANSFERENCIA WHERE TRF_STATUS_STR = ? ORDER BY TRF_DATAHR_DATE DESC";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transferencias.add(mapearResultSetParaTransferencia(rs));
                }
            }
            
            return transferencias;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar transferências por status: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Lista todas as transferências cadastradas.
     * 
     * @return Lista de objetos Transferencia
     */
    public List<Transferencia> listarTodas() {
        List<Transferencia> transferencias = new ArrayList<>();
        String sql = "SELECT * FROM TRANSFERENCIA ORDER BY TRF_DATAHR_DATE DESC";
        
        try (Connection conn = ConexaoDb.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transferencias.add(mapearResultSetParaTransferencia(rs));
            }
            
            return transferencias;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar transferências: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Mapeia um ResultSet para um objeto Transferencia.
     * 
     * @param rs O ResultSet contendo os dados da transferência
     * @return O objeto Transferencia mapeado
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet
     */
    private Transferencia mapearResultSetParaTransferencia(ResultSet rs) throws SQLException {
        Transferencia transferencia = new Transferencia();
        transferencia.setId(rs.getInt("TRF_ID_INT"));
        transferencia.setUsuarioRemetenteId(rs.getInt("USR_ID_INT"));
        transferencia.setDestinatarioId(rs.getInt("DES_ID_INT"));
        transferencia.setValor(rs.getBigDecimal("TRF_VALOR_DEC"));
        
        // Converte o timestamp para LocalDateTime
        Timestamp timestamp = rs.getTimestamp("TRF_DATAHR_DATE");
        if (timestamp != null) {
            transferencia.setDataHoraTransacao(timestamp.toLocalDateTime());
        } else {
            transferencia.setDataHoraTransacao(LocalDateTime.now());
        }
        
        // Converte a string do status para o enum
        String statusStr = rs.getString("TRF_STATUS_STR");
        switch (statusStr) {
            case "CONCLUIDA":
                transferencia.setStatus(StatusTransferencia.CONCLUIDA);
                break;
            case "BLOQUEADA_DENUNCIA":
                transferencia.setStatus(StatusTransferencia.BLOQUEADA_DENUNCIA);
                break;
            case "BLOQUEADA_QUARENTENA":
                transferencia.setStatus(StatusTransferencia.BLOQUEADA_QUARENTENA);
                break;
            case "CANCELADA":
                transferencia.setStatus(StatusTransferencia.CANCELADA);
                break;
            case "EM_ANALISE_ADMIN":
                transferencia.setStatus(StatusTransferencia.EM_ANALISE_ADMIN);
                break;
            case "APROVADA_ADMIN":
                transferencia.setStatus(StatusTransferencia.APROVADA_ADMIN);
                break;
            case "REJEITADA_ADMIN":
                transferencia.setStatus(StatusTransferencia.REJEITADA_ADMIN);
                break;
            default:
                transferencia.setStatus(StatusTransferencia.PENDENTE);
                break;
        }
        
        transferencia.setTokenValidado(rs.getBoolean("TRF_TOKEN_BOOL"));
        
        return transferencia;
    }
}
