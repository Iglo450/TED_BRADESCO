package com.bradescoa3.integration;

import com.bradescoa3.controller.AdminController;
import com.bradescoa3.model.Transferencia;
import com.bradescoa3.model.StatusTransferencia;
import com.bradescoa3.util.ComponentesUtil;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Classe de integração para conectar as telas administrativas do GUI Builder com os controllers.
 * Esta classe serve como ponte entre a interface gráfica administrativa e a lógica de negócio.
 */
public class AdminIntegration {
    
    // Controller
    private final AdminController adminController;
    
    /**
     * Construtor padrão
     */
    public AdminIntegration() {
        this.adminController = new AdminController();
    }
    
    /**
     * Autentica um administrador.
     * 
     * @param cpfCnpj CPF/CNPJ do administrador
     * @param senha Senha do administrador
     * @param componente Componente pai para exibir mensagens
     * @return true se a autenticação for bem-sucedida e o usuário for administrador, false caso contrário
     */
    public boolean autenticarAdmin(String cpfCnpj, String senha, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpj.isEmpty() || senha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ e senha são obrigatórios.", 
                        "Erro de Autenticação");
                return false;
            }
            
            // Tenta autenticar o administrador
            boolean sucesso = adminController.autenticarAdmin(cpfCnpj, senha);
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao autenticar administrador: " + e.getMessage(), 
                    "Erro de Autenticação");
            return false;
        }
    }
    
    /**
     * Carrega as transferências em análise em uma tabela.
     * 
     * @param tabela A tabela onde as transferências serão exibidas
     * @param componente Componente pai para exibir mensagens
     * @return true se as transferências forem carregadas com sucesso, false caso contrário
     */
    public boolean carregarTransferenciasEmAnalise(JTable tabela, Component componente) {
        try {
            // Verifica se o usuário é administrador
            if (!adminController.isAdmin()) {
                ComponentesUtil.exibirErro(componente, 
                        "Apenas administradores podem acessar transferências em análise.", 
                        "Acesso Negado");
                return false;
            }
            
            // Obtém as transferências em análise
            List<Transferencia> transferencias = adminController.listarTransferenciasEmAnalise();
            
            if (transferencias == null || transferencias.isEmpty()) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Não há transferências em análise no momento.", 
                        "Transferências em Análise");
                
                // Limpa a tabela
                DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
                modelo.setRowCount(0);
                
                return true;
            }
            
            // Configura o modelo da tabela
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            modelo.setRowCount(0);
            
            // Adiciona as transferências à tabela
            for (Transferencia transferencia : transferencias) {
                modelo.addRow(new Object[]{
                    transferencia.getId(),
                    transferencia.getUsuarioRemetenteId(),
                    transferencia.getDestinatarioId(),
                    transferencia.getValor(),
                    transferencia.getDataHoraTransacao(),
                    transferencia.getStatus().toString()
                });
            }
            
            return true;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao carregar transferências em análise: " + e.getMessage(), 
                    "Erro");
            return false;
        }
    }
    
    /**
     * Aprova uma transferência bloqueada.
     * 
     * @param transferenciaId O ID da transferência a ser aprovada
     * @param senha Senha do administrador para confirmação
     * @param componente Componente pai para exibir mensagens
     * @return true se a aprovação for bem-sucedida, false caso contrário
     */
    public boolean aprovarTransferencia(int transferenciaId, String senha, Component componente) {
        try {
            // Verifica se o usuário é administrador
            if (!adminController.isAdmin()) {
                ComponentesUtil.exibirErro(componente, 
                        "Apenas administradores podem aprovar transferências.", 
                        "Acesso Negado");
                return false;
            }
            
            // Valida os campos
            if (senha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "A senha é obrigatória para confirmar a aprovação.", 
                        "Erro de Aprovação");
                return false;
            }
            
            // Tenta aprovar a transferência
            boolean sucesso = adminController.aprovarTransferencia(transferenciaId, senha);
            
            if (sucesso) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Transferência aprovada com sucesso!", 
                        "Aprovação");
            }
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao aprovar transferência: " + e.getMessage(), 
                    "Erro de Aprovação");
            return false;
        }
    }
    
    /**
     * Rejeita uma transferência bloqueada.
     * 
     * @param transferenciaId O ID da transferência a ser rejeitada
     * @param senha Senha do administrador para confirmação
     * @param componente Componente pai para exibir mensagens
     * @return true se a rejeição for bem-sucedida, false caso contrário
     */
    public boolean rejeitarTransferencia(int transferenciaId, String senha, Component componente) {
        try {
            // Verifica se o usuário é administrador
            if (!adminController.isAdmin()) {
                ComponentesUtil.exibirErro(componente, 
                        "Apenas administradores podem rejeitar transferências.", 
                        "Acesso Negado");
                return false;
            }
            
            // Valida os campos
            if (senha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "A senha é obrigatória para confirmar a rejeição.", 
                        "Erro de Rejeição");
                return false;
            }
            
            // Tenta rejeitar a transferência
            boolean sucesso = adminController.rejeitarTransferencia(transferenciaId, senha);
            
            if (sucesso) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Transferência rejeitada com sucesso!", 
                        "Rejeição");
            }
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao rejeitar transferência: " + e.getMessage(), 
                    "Erro de Rejeição");
            return false;
        }
    }
    
    /**
     * Gera um relatório de transferências suspeitas.
     * 
     * @param componente Componente pai para exibir mensagens
     * @return true se o relatório for gerado com sucesso, false caso contrário
     */
    public boolean gerarRelatorioTransferenciasSuspeitas(Component componente) {
        try {
            // Verifica se o usuário é administrador
            if (!adminController.isAdmin()) {
                ComponentesUtil.exibirErro(componente, 
                        "Apenas administradores podem gerar relatórios.", 
                        "Acesso Negado");
                return false;
            }
            
            // Tenta gerar o relatório
            boolean sucesso = adminController.gerarRelatorioTransferenciasSuspeitas();
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao gerar relatório: " + e.getMessage(), 
                    "Erro de Relatório");
            return false;
        }
    }
}
