/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bradescoa3.model;


/**
 * Enum que define os possíveis status de quarentena
 */
public enum StatusQuarentena {
    NORMAL("Normal"),
    ALERTA("Em Alerta"),
    BLOQUEADO("Bloqueado"),
    LIBERADO_ADMIN("Liberado pelo Administrador");
    
    private final String descricao;
    
    StatusQuarentena(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}

