package br.com.henrique.formulario.repository;

import br.com.henrique.formulario.entity.PessoaVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface que extends {@link JpaRepository}, e adiciona mais alguns metodos
 */
@Repository
public interface PessoaRepository extends JpaRepository<PessoaVO, Long> {

    List<PessoaVO> findByNomeIgnoreCaseContaining(String nome);

    Optional<PessoaVO> findByCpf(String cpf);
}
