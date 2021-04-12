package br.com.henrique.formulario.resource.dto;

import br.com.henrique.formulario.entity.enums.Sexo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovaPessoaDTO {

    @NotBlank
    private String nome;
    private Sexo sexo;
    private String email;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;
    private String cpf;
}
