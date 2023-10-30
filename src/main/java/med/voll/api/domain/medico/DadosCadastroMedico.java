package med.voll.api.domain.medico;

/* Records: recurso das últimas versãos do Java, um Record é como se fosse uma classe imutável, mais simples que uma Classe comum*/

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

// É aqui que faremos a validação dos dados com o Bean Validation
public record DadosCadastroMedico(
        //O Bean Validation possui várias anotações para validação, que evitam de ficar utilizando diversos IF's
        //@NotNull //O nome não pode ser nulo
        @NotBlank // Verifica tanto se está nulo e vazio
        String nome,
        @NotBlank // Verifica tanto se está nulo e vazio
        @Email //Valida o email, com @ .
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}") //Essa anotação serve para especificar o padrão de um determinado dado (com expressão regular), nesse cano, o CRM contém somente dígitos, no minímo 4 e no máximo 6
        String crm,

        @NotNull
        Especialidade especialidade,

        @NotNull
        @Valid // Informa que outro objeto também deve ser validado
        DadosEndereco endereco) { }
