package br.com.fiap.pedidos.controller;

import br.com.fiap.pedidos.dto.ErroResponse;
import br.com.fiap.pedidos.dto.PedidoRequest;
import br.com.fiap.pedidos.dto.PedidoResponse;
import br.com.fiap.pedidos.model.Pedido;
import br.com.fiap.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pedidos", description = "Criação e consulta de pedidos")
public class PedidoController {

    private static final String ERRO_400_CRIACAO = """
            {
              "timestamp": "2026-10-21T19:30:00.000-03:00",
              "status": 400,
              "erro": "Dados inválidos",
              "detalhes": ["cliente é obrigatório", "valor deve ser maior que zero"],
              "caminho": "/pedidos",
              "requestId": "b7e4a2d1-5c3f-4a8e-9b6d-1e2f3a4b5c6d"
            }
            """;

    private static final String ERRO_400_CONSULTA = """
            {
              "timestamp": "2026-10-21T19:30:00.000-03:00",
              "status": 400,
              "erro": "Identificador inválido",
              "detalhes": ["O identificador deve ser um UUID válido"],
              "caminho": "/pedidos/abc",
              "requestId": "b7e4a2d1-5c3f-4a8e-9b6d-1e2f3a4b5c6d"
            }
            """;

    private static final String ERRO_404 = """
            {
              "timestamp": "2026-10-21T19:30:00.000-03:00",
              "status": 404,
              "erro": "Pedido não encontrado",
              "detalhes": ["Pedido 3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c não encontrado"],
              "caminho": "/pedidos/3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c",
              "requestId": "b7e4a2d1-5c3f-4a8e-9b6d-1e2f3a4b5c6d"
            }
            """;

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cria um pedido", description = "Cria um pedido com status inicial CRIADO.")
    @ApiResponse(responseCode = "201", description = "Pedido criado",
            headers = @Header(name = "Location", description = "URI do pedido criado"),
            content = @Content(schema = @Schema(implementation = PedidoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou JSON malformado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class),
                    examples = @ExampleObject(value = ERRO_400_CRIACAO)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.id())
                .toUri();
        return ResponseEntity.created(location).body(PedidoResponse.de(pedido));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um pedido por identificador")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(schema = @Schema(implementation = PedidoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Identificador em formato inválido",
            content = @Content(schema = @Schema(implementation = ErroResponse.class),
                    examples = @ExampleObject(value = ERRO_400_CONSULTA)))
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
            content = @Content(schema = @Schema(implementation = ErroResponse.class),
                    examples = @ExampleObject(value = ERRO_404)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    public ResponseEntity<PedidoResponse> buscarPorId(
            @Parameter(description = "Identificador do pedido (UUID)", example = "3f2b1c9e-8a4d-4e2b-9c1a-7d5e6f8a9b0c")
            @PathVariable UUID id) {
        return ResponseEntity.ok(PedidoResponse.de(service.buscarPorId(id)));
    }
}
