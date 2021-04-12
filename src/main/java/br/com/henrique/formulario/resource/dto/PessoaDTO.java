package br.com.henrique.formulario.resource.dto;

import br.com.henrique.formulario.entity.enums.Sexo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO implements Serializable {

    private Long id;
    @NotBlank
    private String nome;
    private Sexo sexo;
    private String email;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;
    @NotEmpty
    private String cpf;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dataCriacao;

}
