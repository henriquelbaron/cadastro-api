package br.com.henrique.formulario.resource.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SourceDTO {

    private String projeto;
    private String autor;
    private String fontes;

}

