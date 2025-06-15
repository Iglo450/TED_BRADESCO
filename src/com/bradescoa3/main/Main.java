package com.bradescoa3.main;

import com.bradescoa3.dao.UsuarioDAO;
import com.bradescoa3.model.TipoUsuario;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.ConexaoDb;
import com.bradescoa3.util.HashUtil;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe principal do Sistema Anti-Golpe TED.
 * Responsável por iniciar a aplicação e configurar o ambiente.
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            // Configura o Look and Feel para parecer com o sistema operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Testa a conexão com o banco de dados
            if (!ConexaoDb.testarConexao()) {
                JOptionPane.showMessageDialog(null, 
                        "Não foi possível conectar ao banco de dados.\n" +
                        "Verifique as configurações de conexão e tente novamente.",
                        "Erro de Conexão", 
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Verifica se o usuário administrador existe, se não, cria
            verificarAdministrador();
            
            // Inicia a tela de login
            java.awt.EventQueue.invokeLater(() -> {
            });
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao iniciar a aplicação: " + e.getMessage(),
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Verifica se o usuário administrador existe no banco de dados.
     * Se não existir, cria um usuário administrador padrão.
     */
    private static void verificarAdministrador() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Verifica se o usuário admin existe
            if (!usuarioDAO.cpfCnpjJaExiste("admin")) {
                // Cria o usuário administrador padrão
                Usuario admin = new Usuario();
                admin.setCpfCnpj("admin");
                admin.setNome("Administrador do Sistema");
                admin.setEmail("admin@sistema.com");
                admin.setSenhaHash(HashUtil.gerarHash("501515")); // Senha padrão: 501515
                admin.setAgencia("0000");
                admin.setTipoConta("Corrente");
                admin.setCodigoBanco("000");
                admin.setTipoUsuario(TipoUsuario.ADMIN);
                admin.setDataCriacao(LocalDateTime.now());
                
                usuarioDAO.inserir(admin);
                
                System.out.println("Usuário administrador criado com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar/criar usuário administrador: " + e.getMessage());
        }
    }
}
