package br.com.fiap.pedidos.repository;

import br.com.fiap.pedidos.model.Pedido;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PedidoRepository {

    private final Map<UUID, Pedido> pedidos = new ConcurrentHashMap<>();

    public Pedido salvar(Pedido pedido) {
        pedidos.put(pedido.id(), pedido);
        return pedido;
    }

    public Optional<Pedido> buscarPorId(UUID id) {
        return Optional.ofNullable(pedidos.get(id));
    }
}
