package br.com.fiap3esph.autoescola3esph.adapter.out.repository.persistence;

import br.com.fiap3esph.autoescola3esph.adapter.out.repository.entity.InstrucaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InstrucaoJpaRepository extends JpaRepository<InstrucaoEntity, Long> {
    boolean existsByInstrutorIdAndDataHora(Long idInstrutor, LocalDateTime dataHora);

    long countByAlunoIdAndDataHoraBetween(Long idAluno, LocalDateTime inicioExpediente, LocalDateTime fimExpediente);
}
