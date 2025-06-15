package com.bradescoa3.util;

import com.bradescoa3.model.Usuario;

/**
 * Classe utilitária para gerenciar a sessão do usuário logado.
 * Implementa o padrão Singleton para garantir uma única instância.
 */
public class SessaoUsuario {
    
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    
    /**
     * Construtor privado para impedir instanciação direta
     */
    private SessaoUsuario() {
        this.usuarioLogado = null;
    }
    
    /**
     * Obtém a instância única da classe SessaoUsuario (Singleton)
     * 
     * @return A instância de SessaoUsuario
     */
    public static synchronized SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }
    
    /**
     * Define o usuário logado na sessão
     * 
     * @param usuario O usuário que fez login
     */
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    /**
     * Obtém o usuário logado na sessão
     * 
     * @return O usuário logado ou null se não houver usuário logado
     */
    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }
    
    /**
     * Verifica se há um usuário logado na sessão
     * 
     * @return true se houver um usuário logado, false caso contrário
     */
    public boolean isUsuarioLogado() {
        return this.usuarioLogado != null;
    }
    
    /**
     * Verifica se o usuário logado é um administrador
     * 
     * @return true se o usuário logado for administrador, false caso contrário
     */
    public boolean isUsuarioAdmin() {
        return isUsuarioLogado() && this.usuarioLogado.isAdmin();
    }
    
    /**
     * Encerra a sessão do usuário (logout)
     */
    public void encerrarSessao() {
        this.usuarioLogado = null;
    }
    
    /**
     * Obtém o ID do usuário logado
     * 
     * @return O ID do usuário logado ou -1 se não houver usuário logado
     */
    public int getUsuarioLogadoId() {
        return isUsuarioLogado() ? this.usuarioLogado.getId() : -1;
    }
}
