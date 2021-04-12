package br.com.henrique.formulario.entity;


import br.com.henrique.formulario.entity.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "pessoa")
@NoArgsConstructor
@AllArgsConstructor
public class PessoaVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_generator")
    @SequenceGenerator(name = "pessoa_generator", sequenceName = "seq_pessoa", allocationSize = 1)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Sexo sexo;
    @Email
    private String email;
    @Column(nullable = false)
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;
    @CPF
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private LocalDate dataCriacao;
    @Version
    private LocalDateTime versao;

}
