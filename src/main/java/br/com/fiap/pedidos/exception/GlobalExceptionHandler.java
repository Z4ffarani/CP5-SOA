package br.com.fiap.pedidos.exception;

import br.com.fiap.pedidos.config.RequestIdFilter;
import br.com.fiap.pedidos.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> pedidoNaoEncontrado(PedidoNaoEncontradoException ex, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, "Pedido não encontrado", List.of(ex.getMessage()), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .sorted()
                .toList();
        log.atWarn()
                .addKeyValue("operacao", "CRIAR_PEDIDO")
                .addKeyValue("detalhes", detalhes)
                .log("Dados do pedido inválidos");
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos", detalhes, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.atWarn().log("Corpo da requisição inválido");
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido",
                List.of("JSON ausente ou malformado"), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> identificadorInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.atWarn().addKeyValue("valor", ex.getValue()).log("Identificador inválido");
        return resposta(HttpStatus.BAD_REQUEST, "Identificador inválido",
                List.of("O identificador deve ser um UUID válido"), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroInterno(Exception ex, HttpServletRequest request) {
        log.error("Erro interno ao processar a requisição", ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                List.of("Ocorreu um erro inesperado"), request);
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String erro, List<String> detalhes,
                                                  HttpServletRequest request) {
        ErroResponse corpo = new ErroResponse(
                OffsetDateTime.now(),
                status.value(),
                erro,
                detalhes,
                request.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY)
        );
        return ResponseEntity.status(status).body(corpo);
    }
}
