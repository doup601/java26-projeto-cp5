package br.com.fiap3esph.autoescola3esph.application.port.out;

import br.com.fiap3esph.autoescola3esph.application.core.domain.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlunoRepository {
    boolean existsByIdAndAtivoFalse(Long id);

    boolean existsById(Long id);

    Aluno getReferenceById(Long id);

    Aluno save(Aluno aluno);

    Optional<Aluno> findById(Long id);

    Page<Aluno> findAllByAtivoTrue(Pageable paginacao);
}
