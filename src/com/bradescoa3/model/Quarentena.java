package com.bradescoa3.model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma quarentena de destinatário no sistema.
 * Herda de EntidadeBase para ter um ID.
 */
public class Quarentena extends EntidadeBase {
    private String cpfCnpjDestinatario; // CPF/CNPJ do destinatário em quarentena
    private int quantidadeDenuncias; // Quantidade de denúncias recebidas
    private LocalDateTime ultimaDenuncia; // Data e hora da última denúncia
    private String motivoUltimaDenuncia; // Motivo da última denúncia
    private StatusQuarentena status; // Status da quarentena
    
    /**
     * Construtor padrão
     */
    public Quarentena() {
        this.quantidadeDenuncias = 0;
        this.status = StatusQuarentena.NORMAL;
    }
    
    /**
     * Construtor com parâmetros principais
     */
    public Quarentena(String cpfCnpjDestinatario) {
        this();
        this.cpfCnpjDestinatario = cpfCnpjDestinatario;
    }

    // Getters e Setters
    public String getCpfCnpjDestinatario() {
        return cpfCnpjDestinatario;
    }

    public void setCpfCnpjDestinatario(String cpfCnpjDestinatario) {
        this.cpfCnpjDestinatario = cpfCnpjDestinatario;
    }

    public int getQuantidadeDenuncias() {
        return quantidadeDenuncias;
    }

    public void setQuantidadeDenuncias(int quantidadeDenuncias) {
        this.quantidadeDenuncias = quantidadeDenuncias;
    }

    public LocalDateTime getUltimaDenuncia() {
        return ultimaDenuncia;
    }

    public void setUltimaDenuncia(LocalDateTime ultimaDenuncia) {
        this.ultimaDenuncia = ultimaDenuncia;
    }

    public String getMotivoUltimaDenuncia() {
        return motivoUltimaDenuncia;
    }

    public void setMotivoUltimaDenuncia(String motivoUltimaDenuncia) {
        this.motivoUltimaDenuncia = motivoUltimaDenuncia;
    }

    public StatusQuarentena getStatus() {
        return status;
    }

    public void setStatus(StatusQuarentena status) {
        this.status = status;
    }
    
    /**
     * Incrementa o contador de denúncias e atualiza a data e motivo da última denúncia
     * @param motivo Motivo da nova denúncia
     */
    public void adicionarDenuncia(String motivo) {
        this.quantidadeDenuncias++;
        this.ultimaDenuncia = LocalDateTime.now();
        this.motivoUltimaDenuncia = motivo;
        
        // Atualiza o status com base na quantidade de denúncias
        if (this.quantidadeDenuncias >= 5) {
            this.status = StatusQuarentena.BLOQUEADO;
        } else if (this.quantidadeDenuncias > 0) {
            this.status = StatusQuarentena.ALERTA;
        }
    }
    
    /**
     * Verifica se o destinatário está bloqueado (5 ou mais denúncias)
     * @return true se estiver bloqueado, false caso contrário
     */
    public boolean estaBloqueado() {
        return this.status == StatusQuarentena.BLOQUEADO;
    }
    
    /**
     * Verifica se o destinatário está em alerta (pelo menos 1 denúncia)
     * @return true se estiver em alerta, false caso contrário
     */
    public boolean estaEmAlerta() {
        return this.status == StatusQuarentena.ALERTA;
    }
    
    @Override
    public String toString() {
        return "Quarentena{" + 
                "id=" + id + 
                ", cpfCnpjDestinatario='" + cpfCnpjDestinatario + '\'' + 
                ", quantidadeDenuncias=" + quantidadeDenuncias + 
                ", ultimaDenuncia=" + ultimaDenuncia + 
                ", motivoUltimaDenuncia='" + motivoUltimaDenuncia + '\'' + 
                ", status=" + status + 
                '}';
    }
}
