package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Essa anotação indica que essa classe lidará com tratamento de erros
@RestControllerAdvice
public class TratadorDeErros {

    //Erro 404, servidor nã conseguiu processar a requisição, Exemplo: testou listar um cadastro passando id inválido
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity TratarErro404(){
        return ResponseEntity.notFound().build();
    }

    //Erro 400, servidor nã conseguiu processar a requisição, Exemplo: testou cadastrar um usuário com dados inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity TratarErro400( MethodArgumentNotValidException ex ){ // Podemos receber um objeto que contém a descrição dos erros
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity TratarErroRegradeNegocio( ValidacaoException ex ){ // Podemos receber um objeto que contém a descrição dos erros
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private record DadosErroValidacao(String campo, String mensagem){
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
