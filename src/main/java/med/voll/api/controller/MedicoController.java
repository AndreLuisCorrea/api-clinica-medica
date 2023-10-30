package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

        @Autowired /* Anotação responsável por definir quem é o responsável por instanciar o atributo, nesse caso, a responsabilidade cai sobre o própio Spring Boot
                    Isso se chama injeção de dependências: o spring conhece as classes e consegue instanciar e devolver para a controller */
        private MedicoRepository repository;  // É criado um objeto repository

        /*
                a requisição é do tipo POST, logo o mapeamento também deve ser definido como POST, isto é: quando a requisição
                Cadastro de Médico que criamos no Insomnia for chamada, ele chamará automaticamente esse método
         */

        //ERRO 404 => a requisição não encontrou a URL
        //MENSAGEM 200 => requisição foi efetuada com sucesso

        //Padrão DTO (Data Transfer Object) -> muito uitlizado em API's para representar os dados que chegam e devolvem da API
        @PostMapping //Requisição do tipo POST
        @Transactional //Esse método será utilizado para fazer um cadastro no banco de dados, logo ele deve ter uma transição ativa
        public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){ //@RequestBody é para a string puxar do corpo da requisição, neste caso: os dados do json
                // O new é responsável por converter o objeto da Record em um objeto Medico que poderá ser gravado no banco
                //Repository é responsável por gravar e recuperar dados necessários para persistir e recuperar os objetos de negócio
                var medico = new Medico(dados);
                repository.save(medico);
                var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
                return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
        }

        @GetMapping
        public ResponseEntity< Page<DadosListagemMedico> > listar(@PageableDefault(size=10, sort={"nome"}) Pageable paginacao){
                //return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
                var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
                return ResponseEntity.ok(page);
        }

        @PutMapping
        @Transactional
        public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
                var medico = repository.getReferenceById(dados.id());
                medico.atualizarInformacoes(dados);
                // A atualização ocorre automaticamente pela JPA no banco somente com essas duas linhas

                return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
        }

        @DeleteMapping("/{id}") // O barra - chaves - parâmetro, indica que o parâmetro é dinâmico
        @Transactional
        // Na exclusão a boa prática é devolver o protocolo 204 => requisição feita com sucesso é retorno sem conteúdo
        public ResponseEntity excluir(@PathVariable Long id){ // A anotação @PathVariable indica que Long id está relacionada com a variável do path, do caminho da URL
                //repository.deleteById(id); //Dessa forma, é feito a exclusão física e não lógica (tornar inativo)
                var medico = repository.getReferenceById(id); //Nessa linha, recupera o médico do campo de dados
                medico.excluir(); // Dessa forma atualizamos o campo "ativo", quando atualizamos um campo a JPA dispara automaticamente o update no banco de dados
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity detalhar(@PathVariable Long id){
                var medico = repository.getReferenceById(id);
                return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
        }
}
