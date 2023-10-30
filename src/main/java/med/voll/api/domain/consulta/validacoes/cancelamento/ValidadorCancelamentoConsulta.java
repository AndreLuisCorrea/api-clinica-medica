package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.stereotype.Component;

public interface ValidadorCancelamentoConsulta {

    void validar (DadosCancelamentoConsulta dados);
}
