
package com.bradescoa3.model;
    public enum StatusTransferencia {
    PENDENTE("Pendente"),
    CONCLUIDA("Concluída"),
    BLOQUEADA_DENUNCIA("Bloqueada por Denúncia"),
    BLOQUEADA_QUARENTENA("Bloqueada por Quarentena"),
    CANCELADA("Cancelada"),
    EM_ANALISE_ADMIN("Em Análise pelo Administrador"),
    APROVADA_ADMIN("Aprovada pelo Administrador"),
    REJEITADA_ADMIN("Rejeitada pelo Administrador");
    
    private final String descricao;
    
    StatusTransferencia(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
