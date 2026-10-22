package br.com.fiap.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "PedidoRequest", description = "Dados para criação de um pedido")
public record PedidoRequest(
        @Schema(description = "Nome ou identificação do cliente", example = "Maria Silva")
        @NotBlank(message = "cliente é obrigatório")
        String cliente,

        @Schema(description = "Descrição do pedido", example = "Notebook Dell Inspiron 15")
        @NotBlank(message = "descricao é obrigatória")
        String descricao,

        @Schema(description = "Valor do pedido", example = "4599.90")
        @NotNull(message = "valor é obrigatório")
        @DecimalMin(value = "0.01", message = "valor deve ser maior que zero")
        BigDecimal valor
) {
}
