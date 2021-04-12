package br.com.henrique.formulario.resource;

import br.com.henrique.formulario.resource.dto.SourceDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${endpoint.source}")
@ApiOperation(value = "Código fonte do Projeto")
public class SourceResource {

    @ApiOperation(value = "Endereco contendo Código fonte do projeto")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SourceDTO> obterCodigoFonte() {
        List<SourceDTO> fontes = new ArrayList<>();
        fontes.add(SourceDTO.builder()
                .autor("Henrique Lemes Baron")
                .fontes("https://github.com/henriquelbaron/cadastro-api")
                .build());
        fontes.add(SourceDTO.builder()
                .autor("Henrique Lemes Baron")
                .fontes("https://github.com/henriquelbaron/cadastro-api-front")
                .build());
        return fontes;
    }


}
