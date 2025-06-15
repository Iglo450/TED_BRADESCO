package com.bradescoa3.model;

/**
 * Classe que representa um destinatário de transferência no sistema.
 * Herda de EntidadeBase para ter um ID.
 */
public class Destinatario extends EntidadeBase {
    private String cpfCnpj;
    private String nome;
    private String agencia;
    private String tipoConta; // "Corrente" ou "Poupança"
    private String codigoBanco;
    
    /**
     * Construtor padrão
     */
    public Destinatario() {
    }
    
    /**
     * Construtor com parâmetros principais
     */
    public Destinatario(String cpfCnpj, String nome, String agencia, String tipoConta, String codigoBanco) {
        this.cpfCnpj = cpfCnpj;
        this.nome = nome;
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
    
    @Override
    public String toString() {
        return "Destinatario{" + 
                "id=" + id + 
                ", cpfCnpj='" + cpfCnpj + '\'' + 
                ", nome='" + nome + '\'' + 
                ", agencia='" + agencia + '\'' + 
                ", tipoConta='" + tipoConta + '\'' + 
                ", codigoBanco='" + codigoBanco + '\'' + 
                '}';
    }
}
