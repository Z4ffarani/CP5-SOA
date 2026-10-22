package br.com.fiap.pedidos.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record Pedido(
        UUID id,
        String cliente,
        String descricao,
        BigDecimal valor,
        StatusPedido status,
        OffsetDateTime dataCriacao
) {
}
