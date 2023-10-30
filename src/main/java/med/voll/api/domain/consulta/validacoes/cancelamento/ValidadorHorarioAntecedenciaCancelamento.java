package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedenciaCancelamento implements ValidadorCancelamentoConsulta{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosCancelamentoConsulta dados){
        var agora = LocalDateTime.now();
        var horarioConsulta = repository.getReferenceById(dados.idConsulta()).getData();
        var antecedencia = Duration.between(agora, horarioConsulta).toHours();

        if(antecedencia < 24){
            throw new ValidacaoException("A consulta não pode ser cancelada com menos de 24 horas de antecedência");
        }

    }

}
