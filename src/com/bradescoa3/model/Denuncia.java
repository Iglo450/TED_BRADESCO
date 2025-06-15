package com.bradescoa3.model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma denúncia no sistema.
 * Herda de EntidadeBase para ter um ID.
 */
public class Denuncia extends EntidadeBase {
    private int usuarioDenuncianteId; // ID do usuário que fez a denúncia
    private String cpfCnpjDenunciado; // CPF/CNPJ do denunciado
    private String motivo; // Motivo da denúncia
    private LocalDateTime dataDenuncia; // Data e hora da denúncia
    
    /**
     * Construtor padrão
     */
    public Denuncia() {
        this.dataDenuncia = LocalDateTime.now();
    }
    
    /**
     * Construtor com parâmetros principais
     */
    public Denuncia(int usuarioDenuncianteId, String cpfCnpjDenunciado, String motivo) {
        this();
        this.usuarioDenuncianteId = usuarioDenuncianteId;
        this.cpfCnpjDenunciado = cpfCnpjDenunciado;
        this.motivo = motivo;
    }

    // Getters e Setters
    public int getUsuarioDenuncianteId() {
        return usuarioDenuncianteId;
    }

    public void setUsuarioDenuncianteId(int usuarioDenuncianteId) {
        this.usuarioDenuncianteId = usuarioDenuncianteId;
    }

    public String getCpfCnpjDenunciado() {
        return cpfCnpjDenunciado;
    }

    public void setCpfCnpjDenunciado(String cpfCnpjDenunciado) {
        this.cpfCnpjDenunciado = cpfCnpjDenunciado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(LocalDateTime dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }
    
    @Override
    public String toString() {
        return "Denuncia{" + 
                "id=" + id + 
                ", usuarioDenuncianteId=" + usuarioDenuncianteId + 
                ", cpfCnpjDenunciado='" + cpfCnpjDenunciado + '\'' + 
                ", motivo='" + motivo + '\'' + 
                ", dataDenuncia=" + dataDenuncia + 
                '}';
    }
}
