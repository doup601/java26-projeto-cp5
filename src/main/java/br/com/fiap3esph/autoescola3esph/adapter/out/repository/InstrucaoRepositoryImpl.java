package br.com.fiap3esph.autoescola3esph.adapter.out.repository;

import br.com.fiap3esph.autoescola3esph.adapter.out.repository.entity.InstrucaoEntity;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.mapper.InstrucaoEntityMapper;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.persistence.InstrucaoJpaRepository;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Instrucao;
import br.com.fiap3esph.autoescola3esph.application.port.out.InstrucaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstrucaoRepositoryImpl implements InstrucaoRepository {
    private final InstrucaoJpaRepository jpaRepository;
    private final InstrucaoEntityMapper entityMapper;

    @Override
    public Instrucao save(Instrucao instrucao) {
        InstrucaoEntity saved = jpaRepository.save(entityMapper.toEntity(instrucao));
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Instrucao> findById(Long id) {
        return jpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public boolean existsByInstrutorIdAndDataHora(Long idInstrutor, LocalDateTime dataHora) {
        return jpaRepository.existsByInstrutorIdAndDataHora(idInstrutor, dataHora);
    }

    @Override
    public long countByAlunoIdAndDataHoraBetween(Long idAluno, LocalDateTime inicioExpediente, LocalDateTime fimExpediente) {
        return jpaRepository.countByAlunoIdAndDataHoraBetween(idAluno, inicioExpediente, fimExpediente);
    }
}
