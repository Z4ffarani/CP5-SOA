package br.com.fiap.pedidos.service;

import br.com.fiap.pedidos.dto.PedidoRequest;
import br.com.fiap.pedidos.exception.PedidoNaoEncontradoException;
import br.com.fiap.pedidos.model.Pedido;
import br.com.fiap.pedidos.model.StatusPedido;
import br.com.fiap.pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public Pedido criar(PedidoRequest request) {
        Pedido pedido = new Pedido(
                UUID.randomUUID(),
                request.cliente(),
                request.descricao(),
                request.valor(),
                StatusPedido.CRIADO,
                OffsetDateTime.now()
        );
        repository.salvar(pedido);

        try (var ignored = MDC.putCloseable("pedidoId", pedido.id().toString())) {
            log.atInfo()
                    .addKeyValue("operacao", "CRIAR_PEDIDO")
                    .addKeyValue("status", pedido.status())
                    .log("Pedido criado com sucesso");
        }
        return pedido;
    }

    public Pedido buscarPorId(UUID id) {
        try (var ignored = MDC.putCloseable("pedidoId", id.toString())) {
            return repository.buscarPorId(id)
                    .map(pedido -> {
                        log.atInfo()
                                .addKeyValue("operacao", "CONSULTAR_PEDIDO")
                                .log("Pedido consultado com sucesso");
                        return pedido;
                    })
                    .orElseThrow(() -> {
                        log.atWarn()
                                .addKeyValue("operacao", "CONSULTAR_PEDIDO")
                                .log("Pedido não encontrado");
                        return new PedidoNaoEncontradoException(id);
                    });
        }
    }
}
