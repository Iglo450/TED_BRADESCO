package com.bradescoa3.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe utilitária para gerenciar conexões com o banco de dados.
 */
public class ConexaoDb {
    
    // Configurações de conexão com o banco de dados
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=AntiGolpeDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa"; // Altere para o seu usuário do SQL Server
    private static final String DB_PASSWORD = "SuaSenha123"; // Altere para a sua senha do SQL Server
    
    /**
     * Obtém uma conexão com o banco de dados.
     * 
     * @return Uma conexão com o banco de dados
     * @throws SQLException Se ocorrer um erro ao conectar ao banco de dados
     */
    public static Connection getConexao() throws SQLException {
        try {
            // Carrega o driver JDBC do SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Estabelece a conexão com o banco de dados
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                    "Driver JDBC do SQL Server não encontrado: " + e.getMessage(),
                    "Erro de Conexão", 
                    JOptionPane.ERROR_MESSAGE);
            throw new SQLException("Driver JDBC não encontrado", e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao conectar ao banco de dados: " + e.getMessage(),
                    "Erro de Conexão", 
                    JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
    
    /**
     * Testa a conexão com o banco de dados.
     * 
     * @return true se a conexão for bem-sucedida, false caso contrário
     */
    public static boolean testarConexao() {
        try (Connection conn = getConexao()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exibe as informações de conexão para depuração.
     * 
     * @return String contendo as informações de conexão
     */
    public static String getInfoConexao() {
        try (Connection conn = getConexao()) {
            if (conn != null && !conn.isClosed()) {
                return "Conectado a: " + conn.getMetaData().getURL() + 
                       "\nUsuário: " + conn.getMetaData().getUserName() +
                       "\nVersão do banco: " + conn.getMetaData().getDatabaseProductVersion();
            } else {
                return "Não foi possível estabelecer conexão.";
            }
        } catch (SQLException e) {
            return "Erro ao obter informações de conexão: " + e.getMessage();
        }
    }
}
