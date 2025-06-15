package com.bradescoa3.model;

import java.time.LocalDateTime;

//Classe que representa usuario -- herda EntidadeBase -> id
public class Usuario extends EntidadeBase {
    private String cpfCnpj;
    private String nome;
    private String email;
    private String senhaHash;
    private String agencia;
    private String tipoConta; // "Corrente" ou "Poupança"
    private String codigoBanco;
    private TipoUsuario tipoUsuario;
    private LocalDateTime dataCriacao;
    
    /**
     * Construtor padrão
     */
    public Usuario() {
        this.tipoUsuario = TipoUsuario.USUARIO;
        this.dataCriacao = LocalDateTime.now();
    }
    
    /**
     * Construtor com parâmetros principais
     */
    public Usuario(String cpfCnpj, String nome, String email, String senhaHash, 
                  String agencia, String tipoConta, String codigoBanco) {
        this();
        this.cpfCnpj = cpfCnpj;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.agencia = agencia;
        this.tipoConta = tipoConta;
        this.codigoBanco = codigoBanco;
    }

    // Getters e Setters
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    /**
     * Verifica se o usuário é administrador
     * @return true se for administrador, false caso contrário
     */
    public boolean isAdmin() {
        return this.tipoUsuario == TipoUsuario.ADMIN;
    }
    
    @Override
    public String toString() {
        return "Usuario{" + 
                "id=" + id + 
                ", cpfCnpj='" + cpfCnpj + '\'' + 
                ", nome='" + nome + '\'' + 
                ", email='" + email + '\'' + 
                ", agencia='" + agencia + '\'' + 
                ", tipoConta='" + tipoConta + '\'' + 
                ", codigoBanco='" + codigoBanco + '\'' + 
                ", tipoUsuario=" + tipoUsuario + 
                ", dataCriacao=" + dataCriacao + 
                '}';
    }
}