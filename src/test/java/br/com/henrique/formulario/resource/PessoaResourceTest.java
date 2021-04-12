package br.com.henrique.formulario.resource;

import br.com.henrique.formulario.entity.enums.Sexo;
import br.com.henrique.formulario.resource.dto.NovaPessoaDTO;
import br.com.henrique.formulario.resource.dto.PessoaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PessoaResourceTest {

    private final String CPF = "cpf";
    private final String BUSCAR_NOME = "endpoint.pessoa.buscar.nome";
    private final String NOME = "nome";
    private final String PESSOA_INSERIR = "endpoint.pessoa.inserir";
    private final String MENSAGEM_INVALIDO = "mensagem.invalido";
    private final RequestPostProcessor HTTP_BASIC = httpBasic("henrique", "1234");
    private final String BUSCAR_ID = "endpoint.pessoa.buscar.id";
    private final String ID = "id";
    private final String PESSOA = "endpoint.pessoa";

    @Autowired
    private PessoaResource pessoaResource;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    @Test
    void contextLoads() {
        assertThat(pessoaResource).isNotNull();
    }

    @Test
    void buscarTodosSemAutorizacao() throws Exception {
        mockMvc.perform(get(env.getProperty(PESSOA))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void buscarTodos() throws Exception {
        mockMvc.perform(get(env.getProperty(PESSOA)).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void buscarPorId() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty(BUSCAR_ID));
        mockMvc.perform(get(path).param(ID, "1").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void buscarPorIdNaoEncontrar() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty(BUSCAR_ID));
        mockMvc.perform(get(path).param(ID, "9515161").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void buscarPorCPF() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.buscar.cpf"));
        mockMvc.perform(get(path).param(CPF, "086.402.199-23").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void buscarPorCPFNaoEncontrar() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.buscar.cpf"));
        mockMvc.perform(get(path).param(CPF, "086.402.1923").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void buscarPorNome() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty(BUSCAR_NOME));
        mockMvc.perform(get(path).param(NOME, "Henrique").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void buscarPorNomeParcial() throws Exception {
        String path = env.getProperty(PESSOA).concat(env.getProperty(BUSCAR_NOME));
        MvcResult mvcResult = mockMvc.perform(get(path).param(NOME, "rique").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, PessoaDTO.class);
        List<PessoaDTO> pessoaDTOList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), typeReference);
        assertEquals("Henrique", pessoaDTOList.get(0).getNome());
    }

    @Test
    void inserirNovo() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty(PESSOA_INSERIR));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        final String novaPessoaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(novaPessoa);
        MvcResult mvcResult = mockMvc.perform(post(path).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        PessoaDTO pessoaDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PessoaDTO.class);
        assertTrue(pessoaDTO.getId() != null);
    }


    @Test
    void inserirNovoCPFInvalido() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty(PESSOA_INSERIR));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        novaPessoa.setCpf("217.306.178-");
        final String novaPessoaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(novaPessoa);
        final String mensagemEsperada = String.format(env.getProperty(MENSAGEM_INVALIDO), "CPF");
        mockMvc.perform(post(path).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(mensagemEsperada))
                .andDo(print());
    }

    @Test
    void inserirNovoCPFDuplicado() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty(PESSOA_INSERIR));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        novaPessoa.setCpf("086.402.199-23");
        final String novaPessoaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(novaPessoa);
        mockMvc.perform(post(path).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(env.getProperty("mensagem.cpf.registrado")))
                .andDo(print());
    }

    @Test
    void inserirNovoEmailInvalido() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty(PESSOA_INSERIR));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        novaPessoa.setEmail("mariaemailcom");
        final String novaPessoaJson = getNovaPessoaJson(novaPessoa);
        final String mensagemEsperada = String.format(env.getProperty(MENSAGEM_INVALIDO), "E-mail");
        mockMvc.perform(post(path).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(mensagemEsperada))
                .andDo(print());
    }

    @Test
    void inserirNovoDataNascimentoInvalido() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty(PESSOA_INSERIR));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        novaPessoa.setDataNascimento(LocalDate.parse("2030-01-01"));
        final String novaPessoaJson = getNovaPessoaJson(novaPessoa);
        final String mensagemEsperada = String.format(env.getProperty(MENSAGEM_INVALIDO), "Data Nascimento");
        mockMvc.perform(post(path).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(mensagemEsperada))
                .andDo(print());
    }


    @Test
    void alterar() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.alterar"));
        final NovaPessoaDTO novaPessoa = this.getNovaPessoaDTO();
        novaPessoa.setCpf("602.305.360-26");
        novaPessoa.setNome("Henrique Lemes Baron");
        final String novaPessoaJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(novaPessoa);
        MvcResult mvcResult = mockMvc.perform(put(path, 5).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON)
                .content(novaPessoaJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        PessoaDTO pessoaDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PessoaDTO.class);
        assertEquals(pessoaDTO.getNome(), novaPessoa.getNome());
    }

    @Test
    void removerUsandoID() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.remover.id"));
        mockMvc.perform(delete(path, 9).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void removerUsandoIDRegistroInexistente() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.remover.id"));
        mockMvc.perform(delete(path, 999999999).with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void removerUsandoCPF() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.remover.cpf"));
        mockMvc.perform(delete(path, "895.754.100-41").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void removerUsandoCPFRegistroInexistente() throws Exception {
        final String path = env.getProperty(PESSOA).concat(env.getProperty("endpoint.pessoa.remover.cpf"));
        mockMvc.perform(delete(path, "9999999999996999").with(HTTP_BASIC)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private String getNovaPessoaJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    private NovaPessoaDTO getNovaPessoaDTO() {
        return NovaPessoaDTO.builder()
                .nome("Henrique")
                .cpf("816.611.855-68")
                .email("henrique@email.com")
                .nacionalidade("Brasileiro")
                .naturalidade("Palhoça - SC")
                .sexo(Sexo.FEMININO)
                .dataNascimento(LocalDate.parse("1994-03-06"))
                .build();
    }
}