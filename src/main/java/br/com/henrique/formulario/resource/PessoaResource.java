package br.com.henrique.formulario.resource;

import br.com.henrique.formulario.entity.PessoaVO;
import br.com.henrique.formulario.resource.dto.NovaPessoaDTO;
import br.com.henrique.formulario.resource.dto.PessoaDTO;
import br.com.henrique.formulario.service.PessoaService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Endpoints de Pessoa
 */
@RestController
@Validated
@RequestMapping(value = "${endpoint.pessoa}", produces = APPLICATION_JSON_VALUE)
@ApiOperation(value = "Pessoa Endpoints")
public class PessoaResource {

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ModelMapper modelMapper;

    private PessoaDTO voToPessoaDTO(PessoaVO pessoaVO) {
        return modelMapper.map(pessoaVO, PessoaDTO.class);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PessoaDTO>> buscarTodos() {
        List<PessoaVO> pessoasList = pessoaService.pesquisarTodos();
        return ResponseEntity.ok(pessoasList.stream().map(this::voToPessoaDTO).collect(Collectors.toList()));
    }

    @GetMapping(value = "${endpoint.pessoa.buscar.id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaDTO> buscarPorId(@Valid @PathParam("id") Long id) {
        Optional<PessoaVO> pessoaVO = pessoaService.buscarPorId(id);
        if (pessoaVO.isPresent()) {
            return ResponseEntity.ok(this.voToPessoaDTO(pessoaVO.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "${endpoint.pessoa.buscar.cpf}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaDTO> buscarPorCPF(@Valid @PathParam("cpf") String cpf) {
        Optional<PessoaVO> pessoaVO = this.pessoaService.buscarPorCPF(cpf);
        if (pessoaVO.isPresent()) {
            return ResponseEntity.ok(this.voToPessoaDTO(pessoaVO.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "${endpoint.pessoa.buscar.nome}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PessoaDTO>> buscarPorNome(@Valid @PathParam("nome") String nome) {
        List<PessoaVO> pessoasList = pessoaService.buscarPorNome(nome);
        if (!pessoasList.isEmpty()) {
            return ResponseEntity.ok(pessoasList.stream().map(this::voToPessoaDTO).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "${endpoint.pessoa.inserir}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaDTO> inserirNovo(@Valid @RequestBody NovaPessoaDTO novaPessoaDTO) {
        PessoaVO pessoaVO = pessoaService.inserirNovaPessoa(novaPessoaDTO);
        return ResponseEntity.ok(this.voToPessoaDTO(pessoaVO));
    }

    @PutMapping(value = "${endpoint.pessoa.alterar}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaDTO> alterar(@RequestBody PessoaDTO pessoaDTO, @PathVariable("id") Long id) {
        PessoaVO pessoaVO = pessoaService.alterarPessoa(pessoaDTO);
        return ResponseEntity.ok(this.voToPessoaDTO(pessoaVO));
    }

    @DeleteMapping(value = "${endpoint.pessoa.remover.id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        this.pessoaService.removerPessoaId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "${endpoint.pessoa.remover.cpf}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> remover(@PathVariable("cpf") String cpf) {
        this.pessoaService.removerPessoaPorCpf(cpf);
        return ResponseEntity.noContent().build();
    }
}
