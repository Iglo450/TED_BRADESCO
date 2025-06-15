package com.bradescoa3.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe que representa uma transferência no sistema.
 * Herda de EntidadeBase para ter um ID.
 */
public class Transferencia extends EntidadeBase {
    private int usuarioRemetenteId; // ID do usuário que fez a transferência
    private int destinatarioId; // ID do destinatário
    private BigDecimal valor; // Valor da transferência
    private LocalDateTime dataHoraTransacao; // Data e hora da transferência
    private StatusTransferencia status; // Status da transferência
    private boolean tokenValidado; // Se o token de segurança foi validado
    
    /**
     * Construtor padrão
     */
    public Transferencia() {
        this.dataHoraTransacao = LocalDateTime.now();
        this.status = StatusTransferencia.PENDENTE;
        this.tokenValidado = false;
    }
    
    /**
     * Construtor com parâmetros principais
     */
    public Transferencia(int usuarioRemetenteId, int destinatarioId, BigDecimal valor) {
        this();
        this.usuarioRemetenteId = usuarioRemetenteId;
        this.destinatarioId = destinatarioId;
        this.valor = valor;
    }

    // Getters e Setters
    public int getUsuarioRemetenteId() {
        return usuarioRemetenteId;
    }

    public void setUsuarioRemetenteId(int usuarioRemetenteId) {
        this.usuarioRemetenteId = usuarioRemetenteId;
    }

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(int destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHoraTransacao() {
        return dataHoraTransacao;
    }

    public void setDataHoraTransacao(LocalDateTime dataHoraTransacao) {
        this.dataHoraTransacao = dataHoraTransacao;
    }

    public StatusTransferencia getStatus() {
        return status;
    }

    public void setStatus(StatusTransferencia status) {
        this.status = status;
    }

    public boolean isTokenValidado() {
        return tokenValidado;
    }

    public void setTokenValidado(boolean tokenValidado) {
        this.tokenValidado = tokenValidado;
    }
    
    @Override
    public String toString() {
        return "Transferencia{" + 
                "id=" + id + 
                ", usuarioRemetenteId=" + usuarioRemetenteId + 
                ", destinatarioId=" + destinatarioId + 
                ", valor=" + valor + 
                ", dataHoraTransacao=" + dataHoraTransacao + 
                ", status=" + status + 
                ", tokenValidado=" + tokenValidado + 
                '}';
    }
}

/**
 * Enum que define os possíveis status de uma transferência
 */

