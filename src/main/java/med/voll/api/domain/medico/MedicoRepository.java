package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

//Interface
// Padrão repository:
//Entre os sinais<>, passamos os generics. Ou seja, dois tipos de objetos: 1° tipo da entidade, 2° tipo do atributo da chave primária
//Além disso, a classe Repository herda de JpaRepository para poder utilizar as funcionalidades dessa dependência
public interface MedicoRepository extends JpaRepository< Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
            select m from Medico m 
            where 
            m.ativo = true
            and
            m.especialidade = :especialidade
            and
            m.id not in(
                select c.medico.id from Consulta c 
                where 
                c.data = :data
            )
            order by rand()
            limit 1
            """)
    Medico escolherMedicoLivreNaData(Especialidade especialidade, LocalDateTime data);

    @Query("""
            select m.ativo 
            from Medico m
            where
            m.id = :idMedico
            """)
    Boolean findAtivoById(Long idMedico);
}
