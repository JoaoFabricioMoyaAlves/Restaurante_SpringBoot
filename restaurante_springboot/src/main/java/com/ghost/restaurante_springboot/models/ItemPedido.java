package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "itens_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    // 1. Usamos a chave embutida (EmbeddedId)
    @EmbeddedId
    private ItemPedidoId id = new ItemPedidoId(); // Inicializando para evitar NullPointer

    // 2. Mapeamento do Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId") // Mapeia o campo 'pedidoId' da chave ItemPedidoId
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // 3. Mapeamento do Prato
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pratoId") // Mapeia o campo 'pratoId' da chave ItemPedidoId
    @JoinColumn(name = "prato_id")
    private Prato prato; 

    @Column(nullable = false)
    private Integer quantidade;
    
    // CORRIGIDO: Mapeado para o nome da coluna no seu SQL: 'preco_unitario'
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario; 

    // ✅ ADICIONADO: O campo 'observacao' para resolver o erro no PedidoService
    @Column(name = "observacao", length = 200) 
    private String observacao;
    
    // REMOVIDO: A coluna 'valor_subtotal' NÃO existe na sua DDL SQL, o subtotal deve ser calculado no Java.

    // Método auxiliar para calcular o subtotal (uso do campo corrigido)
    public BigDecimal calcularSubtotal() {
        if (precoUnitario == null) return BigDecimal.ZERO;
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }

    // Métodos utilitários para configurar a relação
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        // Não precisamos verificar 'null' graças à inicialização acima
        this.id.setPedidoId(pedido.getId());
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
        // Não precisamos verificar 'null' graças à inicialização acima
        this.id.setPratoId(prato.getId());
    }
}