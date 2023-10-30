package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta{

    // Em uma interface não é necessário dizer que um método é publico, isso ja implícito
    void validar(DadosAgendamentoConsulta dados);
}
