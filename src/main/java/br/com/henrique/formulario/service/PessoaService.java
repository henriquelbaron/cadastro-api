package br.com.henrique.formulario.service;

import br.com.henrique.formulario.entity.PessoaVO;
import br.com.henrique.formulario.exceptions.ValidacaoException;
import br.com.henrique.formulario.repository.PessoaRepository;
import br.com.henrique.formulario.resource.dto.NovaPessoaDTO;
import br.com.henrique.formulario.resource.dto.PessoaDTO;
import br.com.henrique.formulario.utils.ValidadorCPF;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Classe service responsavel pelas validações envolvendo operaçoes de Pessoa
 */
@Service
public class PessoaService {

    private final Logger log = LoggerFactory.getLogger(PessoaService.class);

    @Value("${mensagem.invalido}")
    private String valorInvalido;
    @Value("${mensagem.cpf.registrado}")
    private String cpfDuplicado;
    @Value("${pattern.email}")
    private String emailPattern;
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<PessoaVO> pesquisarTodos() {
        return pessoaRepository.findAll();
    }

    public Optional<PessoaVO> buscarPorId(final Long id) {
        return pessoaRepository.findById(id);
    }

    public List<PessoaVO> buscarPorNome(final String nome) {
        return pessoaRepository.findByNomeIgnoreCaseContaining(nome);
    }

    public Optional<PessoaVO> buscarPorCPF(final String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    public PessoaVO inserirNovaPessoa(final NovaPessoaDTO pessoaDTO) {
        PessoaVO pessoaVO = modelMapper.map(pessoaDTO, PessoaVO.class);
        this.validarNovaPessoa(pessoaVO);
        pessoaVO.setDataCriacao(LocalDate.now());
        pessoaRepository.save(pessoaVO);
        log.info("Resgistro inserido {}", pessoaVO);
        return pessoaVO;
    }

    public PessoaVO alterarPessoa(final PessoaDTO pessoaDTO) {
        Optional<PessoaVO> pessoaOptinal = this.buscarPorCPF(pessoaDTO.getCpf());
        if (pessoaOptinal.isEmpty()) {
            return this.inserirNovaPessoa(modelMapper.map(pessoaDTO, NovaPessoaDTO.class));
        }
        PessoaVO pessoaPersistir = pessoaOptinal.get();
        this.copiarPessoa(pessoaDTO, pessoaPersistir);
        pessoaRepository.save(pessoaPersistir);
        log.info("Pessoa alterada : {}", pessoaPersistir);
        return pessoaPersistir;
    }

    public PessoaVO removerPessoaId(final Long id) {
        return this.removerPessoa(this.buscarPorId(id));
    }

    public PessoaVO removerPessoaPorCpf(String cpf) {
        return this.removerPessoa(this.buscarPorCPF(cpf));
    }

    private PessoaVO removerPessoa(Optional<PessoaVO> pessoaOptional) {
        PessoaVO pessoaVO = pessoaOptional.orElseThrow(() -> new ValidacaoException("Não foi possivel Deletar o registro pois não a registro com a chave fornecida!"));
        pessoaRepository.delete(pessoaVO);
        log.info("Pessoa Deletada : {}", pessoaVO);
        return pessoaVO;
    }


    private void copiarPessoa(final PessoaDTO dto, final PessoaVO vo) {
        vo.setEmail(dto.getEmail());
        vo.setNacionalidade(dto.getNacionalidade());
        vo.setNaturalidade(dto.getNaturalidade());
        vo.setSexo(dto.getSexo());
        vo.setNome(dto.getNome());
        vo.setDataNascimento(dto.getDataNascimento());
    }

    private void validarNovaPessoa(final PessoaVO pessoaVO) {
        this.validarPessoa(pessoaVO, true);
    }

    private void validarPessoa(final PessoaVO pessoaVO, final boolean validarDuplicidade) {
        this.validarNome(pessoaVO);
        this.validarDataNascimento(pessoaVO);
        this.validarEmail(pessoaVO);
        this.validarCPF(pessoaVO, validarDuplicidade);
    }

    private void validarNome(PessoaVO pessoaVO) {
        if (null == pessoaVO.getNome() || pessoaVO.getNome().isBlank()) {
            throw new ValidacaoException(format(valorInvalido, "Nome"));
        }
    }

    private void validarEmail(PessoaVO pessoaVO) {
        final String email = pessoaVO.getEmail();
        if (null == email || email.isBlank()) {
            throw new ValidacaoException(format(valorInvalido, "E-mail"));
        }
        Pattern pattern = Pattern.compile(emailPattern);
        if (!pattern.matcher(email).matches()) {
            throw new ValidacaoException(format(valorInvalido, "E-mail"));
        }
    }

    private void validarDataNascimento(final PessoaVO pessoaVO) {
        if (null == pessoaVO.getDataNascimento() || LocalDate.now().isBefore(pessoaVO.getDataNascimento())) {
            throw new ValidacaoException(format(valorInvalido, "Data Nascimento"));
        }
    }

    private void validarCPF(final PessoaVO pessoaVO, final boolean validarDuplicidade) {
        String cpf = pessoaVO.getCpf();
        if (!ValidadorCPF.isCPF(cpf)) {
            throw new ValidacaoException(format(valorInvalido, "CPF"));
        }

        if (14 != cpf.length()) {
            pessoaVO.setCpf(ValidadorCPF.aplicarMascaraCPF(cpf));
        }

        if (validarDuplicidade && this.buscarPorCPF(cpf).isPresent()) {
            throw new ValidacaoException(cpfDuplicado);
        }
    }
}
