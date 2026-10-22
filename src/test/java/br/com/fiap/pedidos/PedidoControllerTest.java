package br.com.fiap.pedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    private static final String PEDIDO_VALIDO = """
            {
              "cliente": "Maria Silva",
              "descricao": "Notebook Dell Inspiron 15",
              "valor": 4599.90
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /pedidos cria o pedido e retorna 201 com status CRIADO")
    void deveCriarPedido() throws Exception {
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PEDIDO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/pedidos/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.cliente").value("Maria Silva"))
                .andExpect(jsonPath("$.descricao").value("Notebook Dell Inspiron 15"))
                .andExpect(jsonPath("$.valor").value(4599.90))
                .andExpect(jsonPath("$.status").value("CRIADO"));
    }

    @Test
    @DisplayName("GET /pedidos/{id} retorna 200 com o pedido criado")
    void deveConsultarPedidoCriado() throws Exception {
        MvcResult criado = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PEDIDO_VALIDO))
                .andExpect(status().isCreated())
                .andReturn();
        String location = criado.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente").value("Maria Silva"))
                .andExpect(jsonPath("$.status").value("CRIADO"));
    }

    @Test
    @DisplayName("GET /pedidos/{id} retorna 404 para pedido inexistente")
    void deveRetornar404ParaPedidoInexistente() throws Exception {
        UUID inexistente = UUID.randomUUID();

        mockMvc.perform(get("/pedidos/{id}", inexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Pedido não encontrado"))
                .andExpect(jsonPath("$.requestId", notNullValue()));
    }

    @Test
    @DisplayName("POST /pedidos retorna 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "cliente": "", "descricao": "Mouse", "valor": -10 }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Dados inválidos"));
    }
}
