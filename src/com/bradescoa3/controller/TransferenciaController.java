package com.bradescoa3.controller;

import com.bradescoa3.dao.DestinatarioDAO;
import com.bradescoa3.dao.DenunciaDAO;
import com.bradescoa3.dao.TransferenciaDAO;
import com.bradescoa3.model.Destinatario;
import com.bradescoa3.model.Quarentena;
import com.bradescoa3.model.StatusTransferencia;
import com.bradescoa3.model.Transferencia;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.HashUtil;
import com.bradescoa3.util.SessaoUsuario;
import com.bradescoa3.util.ValidadorUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

/**
 * Controlador para gerenciar operações de transferência.
 */
public class TransferenciaController {
    
    private final TransferenciaDAO transferenciaDAO;
    private final DestinatarioDAO destinatarioDAO;
    private final DenunciaDAO denunciaDAO;
    
    /**
     * Construtor padrão
     */
    public TransferenciaController() {
        this.transferenciaDAO = new TransferenciaDAO();
        this.destinatarioDAO = new DestinatarioDAO();
        this.denunciaDAO = new DenunciaDAO();
    }
    
    /**
     * Realiza uma transferência para um destinatário.
     * 
     * @param cpfCnpjDestinatario CPF/CNPJ do destinatário
     * @param nomeDestinatario Nome do destinatário
     * @param agenciaDestinatario Agência do destinatário
     * @param tipoContaDestinatario Tipo de conta do destinatário
     * @param codigoBancoDestinatario Código do banco do destinatário
     * @param valor Valor da transferência
     * @param senha Senha do usuário para confirmação
     * @return true se a transferência for iniciada com sucesso, false caso contrário
     */
    public boolean realizarTransferencia(String cpfCnpjDestinatario, String nomeDestinatario,
                                        String agenciaDestinatario, String tipoContaDestinatario,
                                        String codigoBancoDestinatario, BigDecimal valor, String senha) {
        // Validação dos campos obrigatórios
        if (cpfCnpjDestinatario == null || cpfCnpjDestinatario.trim().isEmpty() ||
            nomeDestinatario == null || nomeDestinatario.trim().isEmpty() ||
            agenciaDestinatario == null || agenciaDestinatario.trim().isEmpty() ||
            tipoContaDestinatario == null || tipoContaDestinatario.trim().isEmpty() ||
            codigoBancoDestinatario == null || codigoBancoDestinatario.trim().isEmpty() ||
            valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 ||
            senha == null || senha.trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(null, 
                    "Todos os campos são obrigatórios e o valor deve ser maior que zero.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Remove caracteres não numéricos do CPF/CNPJ
        cpfCnpjDestinatario = cpfCnpjDestinatario.replaceAll("[^0-9]", "");
        
        // Valida o formato do CPF/CNPJ
        if (!ValidadorUtil.validarCpfCnpj(cpfCnpjDestinatario)) {
            JOptionPane.showMessageDialog(null, 
                    "CPF/CNPJ do destinatário inválido.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o usuário está logado
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(null, 
                    "Você precisa estar logado para realizar uma transferência.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se a senha está correta
        if (!HashUtil.verificarSenha(senha, usuarioLogado.getSenhaHash())) {
            JOptionPane.showMessageDialog(null, 
                    "Senha incorreta.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o usuário está tentando transferir para si mesmo
        if (cpfCnpjDestinatario.equals(usuarioLogado.getCpfCnpj())) {
            JOptionPane.showMessageDialog(null, 
                    "Você não pode transferir para si mesmo.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica se o destinatário está em quarentena
        Quarentena quarentena = denunciaDAO.verificarQuarentena(cpfCnpjDestinatario);
        
        // Se o destinatário estiver bloqueado, impede a transferência
        if (quarentena != null && quarentena.estaBloqueado()) {
            JOptionPane.showMessageDialog(null, 
                    "Este destinatário está bloqueado devido a múltiplas denúncias.\n" +
                    "A transferência não pode ser realizada.",
                    "Transferência Bloqueada", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Se o destinatário tiver denúncias, exibe um alerta
        if (quarentena != null && quarentena.estaEmAlerta()) {
            int resposta = JOptionPane.showConfirmDialog(null, 
                    "ATENÇÃO: Este destinatário possui " + quarentena.getQuantidadeDenuncias() + 
                    " denúncia(s) no sistema.\n" +
                    "Último motivo: " + quarentena.getMotivoUltimaDenuncia() + "\n\n" +
                    "Deseja realmente continuar com esta transferência?",
                    "Alerta de Segurança", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            
            if (resposta != JOptionPane.YES_OPTION) {
                return false;
            }
            
            // Se o usuário insistir, pede a senha novamente
            String confirmacaoSenha = JOptionPane.showInputDialog(null, 
                    "Por segurança, digite sua senha novamente para confirmar a transferência:",
                    "Confirmação de Segurança", 
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirmacaoSenha == null || !HashUtil.verificarSenha(confirmacaoSenha, usuarioLogado.getSenhaHash())) {
                JOptionPane.showMessageDialog(null, 
                        "Senha incorreta. Transferência cancelada.",
                        "Erro de Transferência", 
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // Busca ou cria o destinatário
        Destinatario destinatario = destinatarioDAO.buscarPorCpfCnpj(cpfCnpjDestinatario);
        
        if (destinatario == null) {
            destinatario = new Destinatario();
            destinatario.setCpfCnpj(cpfCnpjDestinatario);
            destinatario.setNome(nomeDestinatario);
            destinatario.setAgencia(agenciaDestinatario);
            destinatario.setTipoConta(tipoContaDestinatario);
            destinatario.setCodigoBanco(codigoBancoDestinatario);
            
            int destinatarioId = destinatarioDAO.inserir(destinatario);
            if (destinatarioId <= 0) {
                JOptionPane.showMessageDialog(null, 
                        "Erro ao registrar destinatário. Tente novamente.",
                        "Erro de Transferência", 
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            destinatario.setId(destinatarioId);
        }
        
        // Cria o objeto Transferencia
        Transferencia transferencia = new Transferencia();
        transferencia.setUsuarioRemetenteId(usuarioLogado.getId());
        transferencia.setDestinatarioId(destinatario.getId());
        transferencia.setValor(valor);
        transferencia.setDataHoraTransacao(LocalDateTime.now());
        
        // Define o status da transferência
        if (quarentena != null && quarentena.getQuantidadeDenuncias() >= 5) {
            transferencia.setStatus(StatusTransferencia.BLOQUEADA_QUARENTENA);
        } else {
            transferencia.setStatus(StatusTransferencia.CONCLUIDA);
        }
        
        // Insere a transferência no banco de dados
        int id = transferenciaDAO.inserir(transferencia);
        
        if (id > 0) {
            if (transferencia.getStatus() == StatusTransferencia.BLOQUEADA_QUARENTENA) {
                JOptionPane.showMessageDialog(null, 
                        "Transferência enviada para análise do administrador devido ao alto número de denúncias do destinatário.",
                        "Transferência em Análise", 
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                        "Transferência realizada com sucesso!",
                        "Transferência", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Erro ao registrar transferência. Tente novamente.",
                    "Erro de Transferência", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Aprova uma transferência bloqueada (apenas para administradores).
     * 
     * @param transferenciaId O ID da transferência a ser aprovada
     * @return true se a aprovação for bem-sucedida, false caso contrário
     */
    public boolean aprovarTransferencia(int transferenciaId) {
        if (!SessaoUsuario.getInstancia().isUsuarioAdmin()) {
            JOptionPane.showMessageDialog(null, 
                    "Apenas administradores podem aprovar transferências.",
                    "Acesso Negado", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return transferenciaDAO.atualizarStatus(transferenciaId, StatusTransferencia.APROVADA_ADMIN);
    }
    
    /**
     * Rejeita uma transferência bloqueada (apenas para administradores).
     * 
     * @param transferenciaId O ID da transferência a ser rejeitada
     * @return true se a rejeição for bem-sucedida, false caso contrário
     */
    public boolean rejeitarTransferencia(int transferenciaId) {
        if (!SessaoUsuario.getInstancia().isUsuarioAdmin()) {
            JOptionPane.showMessageDialog(null, 
                    "Apenas administradores podem rejeitar transferências.",
                    "Acesso Negado", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return transferenciaDAO.atualizarStatus(transferenciaId, StatusTransferencia.REJEITADA_ADMIN);
    }
}
