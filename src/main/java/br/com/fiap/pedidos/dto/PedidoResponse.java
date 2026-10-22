package br.com.fiap.pedidos.dto;

import br.com.fiap.pedidos.model.Pedido;
import br.com.fiap.pedidos.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(name = "PedidoResponse", description = "Pedido armazenado")
public record PedidoResponse(
        @Schema(description = "Identificador único do pedido", example = "3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c")
        UUID id,

        @Schema(description = "Nome ou identificação do cliente", example = "Maria Silva")
        String cliente,

        @Schema(description = "Descrição do pedido", example = "Notebook Dell Inspiron 15")
        String descricao,

        @Schema(description = "Valor do pedido", example = "4599.90")
        BigDecimal valor,

        @Schema(description = "Status do pedido", example = "CRIADO")
        StatusPedido status,

        @Schema(description = "Data e hora de criação", example = "2026-10-21T19:30:00.000-03:00")
        OffsetDateTime dataCriacao
) {

    public static PedidoResponse de(Pedido pedido) {
        return new PedidoResponse(
                pedido.id(),
                pedido.cliente(),
                pedido.descricao(),
                pedido.valor(),
                pedido.status(),
                pedido.dataCriacao()
        );
    }
}
