package com.bradescoa3.integration;

import com.bradescoa3.controller.LoginController;
import com.bradescoa3.controller.CadastroController;
import com.bradescoa3.controller.DenunciaController;
import com.bradescoa3.controller.TransferenciaController;
import com.bradescoa3.controller.AdminController;
import com.bradescoa3.model.Usuario;
import com.bradescoa3.util.SessaoUsuario;
import com.bradescoa3.util.ComponentesUtil;
import com.bradescoa3.util.ValidadorUtil;
import java.awt.Component;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Classe de integração para conectar as telas do GUI Builder com os controllers.
 * Esta classe serve como ponte entre a interface gráfica e a lógica de negócio.
 */
public class TelaIntegration {
    
    // Controllers
    private final LoginController loginController;
    private final CadastroController cadastroController;
    private final DenunciaController denunciaController;
    private final TransferenciaController transferenciaController;
    private final AdminController adminController;
    
    /**
     * Construtor padrão
     */
    public TelaIntegration() {
        this.loginController = new LoginController();
        this.cadastroController = new CadastroController();
        this.denunciaController = new DenunciaController();
        this.transferenciaController = new TransferenciaController();
        this.adminController = new AdminController();
    }
    
    /**
     * Realiza o login do usuário.
     * 
     * @param cpfCnpj CPF/CNPJ do usuário
     * @param senha Senha do usuário
     * @param componente Componente pai para exibir mensagens
     * @return true se o login for bem-sucedido, false caso contrário
     */
    public boolean realizarLogin(String cpfCnpj, String senha, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpj.isEmpty() || senha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, "CPF/CNPJ e senha são obrigatórios.", "Erro de Login");
                return false;
            }
            
            // Tenta realizar o login
            boolean sucesso = loginController.autenticar(cpfCnpj, senha);
            
            if (sucesso) {
                Usuario usuario = SessaoUsuario.getInstancia().getUsuarioLogado();
                ComponentesUtil.exibirInformacao(componente, 
                        "Bem-vindo(a), " + usuario.getNome() + "!", 
                        "Login");
                return true;
            } else {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ ou senha incorretos.", 
                        "Erro de Login");
                return false;
            }
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao realizar login: " + e.getMessage(), 
                    "Erro de Login");
            return false;
        }
    }
    
    /**
     * Realiza o cadastro de um novo usuário.
     * 
     * @param cpfCnpj CPF/CNPJ do usuário
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param agencia Agência bancária
     * @param tipoConta Tipo de conta (Corrente ou Poupança)
     * @param codigoBanco Código do banco
     * @param componente Componente pai para exibir mensagens
     * @return true se o cadastro for bem-sucedido, false caso contrário
     */
    public boolean realizarCadastro(String cpfCnpj, String nome, String email, 
                                   String senha, String agencia, String tipoConta, 
                                   String codigoBanco, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpj.isEmpty() || nome.isEmpty() || senha.isEmpty() || 
                agencia.isEmpty() || tipoConta.isEmpty() || codigoBanco.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "Todos os campos são obrigatórios, exceto email.", 
                        "Erro de Cadastro");
                return false;
            }
            
            // Valida o formato do CPF/CNPJ
            if (!ValidadorUtil.validarCpfCnpj(cpfCnpj)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ inválido.", 
                        "Erro de Cadastro");
                return false;
            }
            
            // Verifica se o CPF/CNPJ já está cadastrado
            if (cadastroController.cpfCnpjJaExiste(cpfCnpj)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ já cadastrado no sistema.", 
                        "Erro de Cadastro");
                return false;
            }
            
            // Tenta realizar o cadastro
            boolean sucesso = cadastroController.cadastrarUsuario(
                    cpfCnpj, nome, email, senha, agencia, tipoConta, codigoBanco);
            
            if (sucesso) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Usuário cadastrado com sucesso!", 
                        "Cadastro");
                return true;
            } else {
                ComponentesUtil.exibirErro(componente, 
                        "Falha ao cadastrar usuário. Tente novamente.", 
                        "Erro de Cadastro");
                return false;
            }
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao realizar cadastro: " + e.getMessage(), 
                    "Erro de Cadastro");
            return false;
        }
    }
    
    /**
     * Realiza uma transferência para um destinatário.
     * 
     * @param cpfCnpjDestinatario CPF/CNPJ do destinatário
     * @param nomeDestinatario Nome do destinatário
     * @param agenciaDestinatario Agência do destinatário
     * @param tipoContaDestinatario Tipo de conta do destinatário
     * @param codigoBancoDestinatario Código do banco do destinatário
     * @param valorStr Valor da transferência (como string)
     * @param senha Senha do usuário para confirmação
     * @param componente Componente pai para exibir mensagens
     * @return true se a transferência for iniciada com sucesso, false caso contrário
     */
    public boolean realizarTransferencia(String cpfCnpjDestinatario, String nomeDestinatario,
                                        String agenciaDestinatario, String tipoContaDestinatario,
                                        String codigoBancoDestinatario, String valorStr, 
                                        String senha, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpjDestinatario = cpfCnpjDestinatario.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpjDestinatario.isEmpty() || nomeDestinatario.isEmpty() || 
                agenciaDestinatario.isEmpty() || tipoContaDestinatario.isEmpty() || 
                codigoBancoDestinatario.isEmpty() || valorStr.isEmpty() || senha.isEmpty()) {
                
                ComponentesUtil.exibirErro(componente, 
                        "Todos os campos são obrigatórios.", 
                        "Erro de Transferência");
                return false;
            }
            
            // Valida o formato do CPF/CNPJ
            if (!ValidadorUtil.validarCpfCnpj(cpfCnpjDestinatario)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ do destinatário inválido.", 
                        "Erro de Transferência");
                return false;
            }
            
            // Converte o valor para BigDecimal
            BigDecimal valor;
            try {
                // Remove caracteres não numéricos e converte para BigDecimal
                valorStr = valorStr.replaceAll("[^0-9.,]", "").replace(",", ".");
                valor = new BigDecimal(valorStr);
                
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    ComponentesUtil.exibirErro(componente, 
                            "O valor da transferência deve ser maior que zero.", 
                            "Erro de Transferência");
                    return false;
                }
            } catch (NumberFormatException e) {
                ComponentesUtil.exibirErro(componente, 
                        "Valor inválido para transferência.", 
                        "Erro de Transferência");
                return false;
            }
            
            // Tenta realizar a transferência
            boolean sucesso = transferenciaController.realizarTransferencia(
                    cpfCnpjDestinatario, nomeDestinatario, agenciaDestinatario, 
                    tipoContaDestinatario, codigoBancoDestinatario, valor, senha);
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao realizar transferência: " + e.getMessage(), 
                    "Erro de Transferência");
            return false;
        }
    }
    
    /**
     * Registra uma nova denúncia no sistema.
     * 
     * @param cpfCnpjDenunciado CPF/CNPJ do denunciado
     * @param motivo Motivo da denúncia
     * @param senha Senha do usuário para confirmação
     * @param componente Componente pai para exibir mensagens
     * @return true se a denúncia for registrada com sucesso, false caso contrário
     */
    public boolean registrarDenuncia(String cpfCnpjDenunciado, String motivo, 
                                    String senha, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpjDenunciado = cpfCnpjDenunciado.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpjDenunciado.isEmpty() || motivo.isEmpty() || senha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "Todos os campos são obrigatórios.", 
                        "Erro de Denúncia");
                return false;
            }
            
            // Valida o formato do CPF/CNPJ
            if (!ValidadorUtil.validarCpfCnpj(cpfCnpjDenunciado)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ do denunciado inválido.", 
                        "Erro de Denúncia");
                return false;
            }
            
            // Tenta registrar a denúncia
            boolean sucesso = denunciaController.registrarDenuncia(
                    cpfCnpjDenunciado, motivo, senha);
            
            return sucesso;
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao registrar denúncia: " + e.getMessage(), 
                    "Erro de Denúncia");
            return false;
        }
    }
    
    /**
     * Configura os campos de texto da interface gráfica.
     * 
     * @param cpfCnpjField Campo de texto para CPF/CNPJ
     * @param valorField Campo de texto para valor monetário
     */
    public void configurarCampos(JTextField cpfCnpjField, JTextField valorField) {
        if (cpfCnpjField != null) {
            ComponentesUtil.configurarCampoCpfCnpj(cpfCnpjField);
        }
        
        if (valorField != null) {
            ComponentesUtil.configurarCampoMonetario(valorField);
        }
    }
    
    /**
     * Verifica se o usuário atual é administrador.
     * 
     * @return true se o usuário for administrador, false caso contrário
     */
    public boolean isUsuarioAdmin() {
        return loginController.isUsuarioAdmin();
    }
    
    /**
     * Realiza o logout do usuário atual.
     */
    public void realizarLogout() {
        loginController.logout();
    }
    
    /**
     * Obtém o usuário logado atualmente.
     * 
     * @return O objeto Usuario logado ou null se não houver usuário logado
     */
    public Usuario getUsuarioLogado() {
        return loginController.getUsuarioLogado();
    }
    
    /**
     * Atualiza a senha de um usuário existente.
     * 
     * @param cpfCnpj CPF/CNPJ do usuário
     * @param novaSenha Nova senha do usuário
     * @param componente Componente pai para exibir mensagens
     * @return true se a senha for atualizada com sucesso, false caso contrário
     */
    public boolean atualizarSenha(String cpfCnpj, String novaSenha, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
            
            // Valida os campos
            if (cpfCnpj.isEmpty() || novaSenha.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ e nova senha são obrigatórios.", 
                        "Erro de Recuperação");
                return false;
            }
            
            // Valida o formato do CPF/CNPJ
            if (!ValidadorUtil.validarCpfCnpj(cpfCnpj)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ inválido.", 
                        "Erro de Recuperação");
                return false;
            }
            
            // Verifica se o CPF/CNPJ existe
            if (!cadastroController.cpfCnpjJaExiste(cpfCnpj)) {
                ComponentesUtil.exibirErro(componente, 
                        "CPF/CNPJ não encontrado no sistema.", 
                        "Erro de Recuperação");
                return false;
            }
            
            // Tenta atualizar a senha
            boolean sucesso = cadastroController.atualizarSenha(cpfCnpj, novaSenha);
            
            if (sucesso) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Senha atualizada com sucesso!", 
                        "Recuperação de Senha");
                return true;
            } else {
                ComponentesUtil.exibirErro(componente, 
                        "Falha ao atualizar senha. Tente novamente.", 
                        "Erro de Recuperação");
                return false;
            }
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao atualizar senha: " + e.getMessage(), 
                    "Erro de Recuperação");
            return false;
        }
    }
    
    /**
     * Carrega as denúncias para a tabela de administrador.
     * 
     * @param componente Componente pai para exibir mensagens
     * @return Array bidimensional com os dados das denúncias ou null em caso de erro
     */
    public Object[][] carregarDenuncias(Component componente) {
        try {
            return adminController.listarDenuncias();
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao carregar denúncias: " + e.getMessage(), 
                    "Erro de Administração");
            return null;
        }
    }
    
    /**
     * Remove uma denúncia do sistema.
     * 
     * @param cpfCnpj CPF/CNPJ do denunciado
     * @param componente Componente pai para exibir mensagens
     * @return true se a denúncia for removida com sucesso, false caso contrário
     */
    public boolean removerDenuncia(String cpfCnpj, Component componente) {
        try {
            // Remove caracteres não numéricos do CPF/CNPJ
            cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
            
            // Valida o CPF/CNPJ
            if (cpfCnpj.isEmpty()) {
                ComponentesUtil.exibirErro(componente, 
                        "Selecione uma denúncia para remover.", 
                        "Erro de Administração");
                return false;
            }
            
            // Tenta remover a denúncia
            boolean sucesso = adminController.removerDenuncia(cpfCnpj);
            
            if (sucesso) {
                ComponentesUtil.exibirInformacao(componente, 
                        "Denúncia removida com sucesso!", 
                        "Administração");
                return true;
            } else {
                ComponentesUtil.exibirErro(componente, 
                        "Falha ao remover denúncia. Tente novamente.", 
                        "Erro de Administração");
                return false;
            }
        } catch (Exception e) {
            ComponentesUtil.exibirErro(componente, 
                    "Erro ao remover denúncia: " + e.getMessage(), 
                    "Erro de Administração");
            return false;
        }
    }
}
