package br.com.fiap.pedidos.exception;

import java.util.UUID;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(UUID id) {
        super("Pedido " + id + " não encontrado");
    }
}
