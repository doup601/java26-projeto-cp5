package br.com.fiap3esph.autoescola3esph.application.port.out;

import br.com.fiap3esph.autoescola3esph.application.core.domain.Instrucao;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InstrucaoRepository {
    Instrucao save(Instrucao instrucao);

    Optional<Instrucao> findById(Long id);

    boolean existsByInstrutorIdAndDataHora(Long idInstrutor, LocalDateTime dataHora);

    long countByAlunoIdAndDataHoraBetween(Long idAluno, LocalDateTime inicioExpediente, LocalDateTime fimExpediente);
}
