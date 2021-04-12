package br.com.henrique.formulario;

import br.com.henrique.formulario.entity.PessoaVO;
import br.com.henrique.formulario.entity.enums.Sexo;
import br.com.henrique.formulario.repository.PessoaRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class FormularioApplication {

    @Autowired
    private PessoaRepository pessoaRepository;

    public static void main(String[] args) {
        SpringApplication.run(FormularioApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            pessoaRepository.save(new PessoaVO(null, "Gilberto", Sexo.MASCULINO, "gilberto@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "895.754.100-41", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Henrique", Sexo.MASCULINO, "hike.lemes@gmail.com", LocalDate.of(1994, 3, 6), "Xanxerê - SC", "Brasileiro", "086.402.199-23", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Jose", Sexo.MASCULINO, "jose@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "030.624.110-23", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Ricardo", Sexo.MASCULINO, "ricardo@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "761.356.660-96", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Maria", Sexo.FEMININO, "maria@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "547.945.560-55", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Bruna", Sexo.FEMININO, "bruna@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "495.126.580-03", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Brenda", Sexo.FEMININO, "brenda@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "591.311.670-47", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Nair", Sexo.FEMININO, "nair@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "602.305.360-26", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Hilton", Sexo.MASCULINO, "hilton@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "926.415.780-85", LocalDate.now(), null));
            pessoaRepository.save(new PessoaVO(null, "Emanuel", Sexo.OUTRO, "emanuel@email.com", LocalDate.of(1994, 3, 6), "Palhoça - SC", "Brasileira", "789.454.380-25", LocalDate.now(), null));
        };
    }

}
