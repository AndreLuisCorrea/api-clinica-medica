package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest // Essa anotação define que queremos testar uma Controller
@AutoConfigureMockMvc // É necessário para poder injetar o MockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson; // JSON que chega na API

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson; // JSON que a AI devolve

    @MockBean
    private MedicoRepository repository;

    @Test
    @DisplayName("Deveria retornar erro 400 quando dados estão inválidos")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {

        var response = mvc.perform(post("/medicos")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria retornar erro 200 quando dados estão válidos")
    @WithMockUser
    void cadastrar_cenario2() throws Exception{

        var endereco = new DadosEndereco("rua 1", "bairro", "12345678", "Foz do Iguacu", "PR", null, null);

        var dadosCadastroMedico = new DadosCadastroMedico("Luis Silva", "luis.silva@voll.med", "991672234", "123456", Especialidade.GINECOLOGIA, endereco);

        when(repository.save(any())).thenReturn(new Medico(dadosCadastroMedico));

        var response = mvc.perform(
                                post("/medicos")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(dadosCadastroMedicoJson.write(dadosCadastroMedico).getJson())
        ).andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoMedico(
                null,
                dadosCadastroMedico.nome(),
                dadosCadastroMedico.email(),
                dadosCadastroMedico.crm(),
                dadosCadastroMedico.telefone(),
                dadosCadastroMedico.especialidade(),
                new Endereco(dadosCadastroMedico.endereco())
        );

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}