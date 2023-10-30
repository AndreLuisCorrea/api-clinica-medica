package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest // Essa anotação define que queremos testar uma Controller
@AutoConfigureMockMvc // É necessário para poder injetar o MockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    // Esse atributo será utilizado para simular requisições http para nossa controller
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson; // JSON que chega na API

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson; // JSON que a AI devolve

    @MockBean // Essa anotação define que o spring não deve injetar um objeto Agenda de Consultas de verdade, mas sim um MOCK (NÂO VAI BUSCAR NO BANCO)
    private AgendaDeConsultas agendaDeConsultas;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informacoes estao invalidas")
    @WithMockUser // Essa anotação faz um mock de usuário e ignora a autenticação, pois aqui não pe um teste de autenticação/segurança
    void agendar_cenario1 () throws Exception {

        // perform() => irá performar/simular uma requisição
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        // A lógica desse método é: faz uma requisição, guarada no responde e, após, verifica se deu erro, nesse caso: erro 400

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value() /* Devolve o código 400*/);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando informacoes estao validas")
    @WithMockUser // Essa anotação faz um mock de usuário e ignora a autenticação, pois aqui não pe um teste de autenticação/segurança
    void agendar_cenario2 () throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);

        when(agendaDeConsultas.agendar(any())).thenReturn( dadosDetalhamento ); // Quando realizamos um MOCK, o dto devolvido é nulo, logo precisamos fazer esse tratamento para o DTo não vir vazio

        // perform() => irá performar/simular uma requisição
        var response = mvc.perform(
                                    post("/consultas")
                                            .contentType(MediaType.APPLICATION_JSON) // Indica ue o servidor receberá um JSON
                                            .content(dadosAgendamentoConsultaJson.write(
                                                    new DadosAgendamentoConsulta(2l, 5l, data, especialidade)
                                            ).getJson()) // Pega o objeto e converte para JSON com o getJson()
                )
                .andReturn().getResponse();

        // A lógica desse método é: faz uma requisição, guarada no responde e, após, verifica se deu erro, nesse caso: erro 400

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value() /* Devolve o código 200*/);

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
               dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}