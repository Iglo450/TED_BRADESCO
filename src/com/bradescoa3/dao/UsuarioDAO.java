package com.bradescoa3.dao;

import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.ConexaoDb;
import com.bradescoa3.util.HashUtil;
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
 * Classe DAO (Data Access Object) para operações de CRUD da entidade Usuario.
 */
public class UsuarioDAO {
    
    /**
     * Insere um novo usuário no banco de dados.
     * 
     * @param usuario O objeto Usuario a ser inserido
     * @return O ID gerado para o usuário inserido ou -1 em caso de erro
     */
    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO USUARIO (USR_CONTA_STR, USR_NOME_STR, USR_EMAIL_STR, " +
                     "USR_SENHA_STR, USR_AGENCIA_STR, USR_TIPO_CONTA_STR, USR_CODIGO_BANCO_STR, " +
                     "USR_TIPO_STR, USR_DATA_CRIACAO) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getCpfCnpj());
            pstmt.setString(2, usuario.getNome());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getSenhaHash());
            pstmt.setString(5, usuario.getAgencia());
            pstmt.setString(6, usuario.getTipoConta());
            pstmt.setString(7, usuario.getCodigoBanco());
            pstmt.setString(8, usuario.getTipoUsuario().toString());
            pstmt.setTimestamp(9, Timestamp.valueOf(usuario.getDataCriacao()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getInt(1));
                        return usuario.getId();
                    }
                }
            }
            
            return -1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao inserir usuário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    /**
     * Atualiza um usuário existente no banco de dados.
     * 
     * @param usuario O objeto Usuario com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE USUARIO SET USR_NOME_STR = ?, USR_EMAIL_STR = ?, " +
                     "USR_AGENCIA_STR = ?, USR_TIPO_CONTA_STR = ?, USR_CODIGO_BANCO_STR = ? " +
                     "WHERE USR_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getAgencia());
            pstmt.setString(4, usuario.getTipoConta());
            pstmt.setString(5, usuario.getCodigoBanco());
            pstmt.setInt(6, usuario.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao atualizar usuário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Atualiza a senha de um usuário existente no banco de dados.
     * 
     * @param usuarioId O ID do usuário
     * @param novaSenhaHash O hash da nova senha
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarSenha(int usuarioId, String novaSenhaHash) {
        String sql = "UPDATE USUARIO SET USR_SENHA_STR = ? WHERE USR_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, novaSenhaHash);
            pstmt.setInt(2, usuarioId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao atualizar senha: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Exclui um usuário do banco de dados.
     * 
     * @param usuarioId O ID do usuário a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int usuarioId) {
        String sql = "DELETE FROM USUARIO WHERE USR_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao excluir usuário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Busca um usuário pelo ID.
     * 
     * @param usuarioId O ID do usuário a ser buscado
     * @return O objeto Usuario encontrado ou null se não encontrado
     */
    public Usuario buscarPorId(int usuarioId) {
        String sql = "SELECT * FROM USUARIO WHERE USR_ID_INT = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, usuarioId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaUsuario(rs);
                }
            }
            
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao buscar usuário: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Busca um usuário pelo CPF/CNPJ.
     * 
     * @param cpfCnpj O CPF/CNPJ do usuário a ser buscado
     * @return O objeto Usuario encontrado ou null se não encontrado
     */
    public Usuario buscarPorCpfCnpj(String cpfCnpj) {
        String sql = "SELECT * FROM USUARIO WHERE USR_CONTA_STR = ?";
        
        try (Connection conn = ConexaoDb.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpfCnpj);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaUsuario(rs);
                }
            }
            
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao buscar usuário por CPF/CNPJ: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Lista todos os usuários cadastrados.
     * 
     * @return Lista de objetos Usuario
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO ORDER BY USR_NOME_STR";
        
        try (Connection conn = ConexaoDb.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearResultSetParaUsuario(rs));
            }
            
            return usuarios;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao listar usuários: " + e.getMessage(),
                    "Erro de Banco de Dados", 
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    
    /**
     * Verifica se as credenciais de login são válidas.
     * 
     * @param cpfCnpj O CPF/CNPJ do usuário
     * @param senha A senha em texto plano
     * @return O objeto Usuario se as credenciais forem válidas, null caso contrário
     */
    public Usuario autenticar(String cpfCnpj, String senha) {
        Usuario usuario = buscarPorCpfCnpj(cpfCnpj);
        
        if (usuario != null && HashUtil.verificarSenha(senha, usuario.getSenhaHash())) {
            return usuario;
        }
        
        return null;
    }
    
    /**
     * Verifica se um CPF/CNPJ já está cadastrado.
     * 
     * @param cpfCnpj O CPF/CNPJ a ser verificado
     * @return true se já existir, false caso contrário
     */
    public boolean cpfCnpjJaExiste(String cpfCnpj) {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE USR_CONTA_STR = ?";
        
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
     * Mapeia um ResultSet para um objeto Usuario.
     * 
     * @param rs O ResultSet contendo os dados do usuário
     * @return O objeto Usuario mapeado
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet
     */
    private Usuario mapearResultSetParaUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("USR_ID_INT"));
        usuario.setCpfCnpj(rs.getString("USR_CONTA_STR"));
        usuario.setNome(rs.getString("USR_NOME_STR"));
        usuario.setEmail(rs.getString("USR_EMAIL_STR"));
        usuario.setSenhaHash(rs.getString("USR_SENHA_STR"));
        usuario.setAgencia(rs.getString("USR_AGENCIA_STR"));
        usuario.setTipoConta(rs.getString("USR_TIPO_CONTA_STR"));
        usuario.setCodigoBanco(rs.getString("USR_CODIGO_BANCO_STR"));
        
        // Converte a string do tipo de usuário para o enum
        String tipoUsuarioStr = rs.getString("USR_TIPO_STR");
        if ("ADMIN".equals(tipoUsuarioStr)) {
            usuario.setTipoUsuario(com.bradescoa3.model.TipoUsuario.ADMIN);
        } else {
            usuario.setTipoUsuario(com.bradescoa3.model.TipoUsuario.USUARIO);
        }
        
        // Converte o timestamp para LocalDateTime
        Timestamp timestamp = rs.getTimestamp("USR_DATA_CRIACAO");
        if (timestamp != null) {
            usuario.setDataCriacao(timestamp.toLocalDateTime());
        } else {
            usuario.setDataCriacao(LocalDateTime.now());
        }
        
        return usuario;
    }
}
