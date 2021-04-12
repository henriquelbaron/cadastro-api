package br.com.henrique.formulario.resource;

import br.com.henrique.formulario.resource.dto.SourceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SourceResourceTest {

    @Autowired
    private SourceResource sourceResource;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void contextLoads() {
        assertThat(sourceResource).isNotNull();
    }

    @Test
    void obterCodigoFonte() throws Exception {
        SourceDTO sourceDTO = SourceDTO.builder()
                .autor("Henrique Lemes Baron")
                .fontes("https://github.com/henriquelbaron/cadastro-api")
                .build();
        String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceDTO);
        MvcResult mvcResult = mockMvc.perform(get("/source")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, SourceDTO.class);
        List<SourceDTO> sourceDTOList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), typeReference);
        assertEquals("Henrique Lemes Baron", sourceDTOList.get(0).getAutor());
    }


}