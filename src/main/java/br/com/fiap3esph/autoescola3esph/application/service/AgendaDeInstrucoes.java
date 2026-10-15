package br.com.fiap3esph.autoescola3esph.application.service;

import br.com.fiap3esph.autoescola3esph.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.request.instrucao.DadosCancelamento;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.response.instrucao.DetalhamentoAgendamento;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Instrucao;
import br.com.fiap3esph.autoescola3esph.application.port.out.InstrucaoRepository;
import br.com.fiap3esph.autoescola3esph.exception.type.ValidacaoException;
import br.com.fiap3esph.autoescola3esph.application.core.specification.contract.ValidadorAgendamento;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Aluno;
import br.com.fiap3esph.autoescola3esph.exception.type.AlunoNotFoundException;
import br.com.fiap3esph.autoescola3esph.application.port.out.AlunoRepository;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Instrutor;
import br.com.fiap3esph.autoescola3esph.exception.type.InstrutorNotFoundException;
import br.com.fiap3esph.autoescola3esph.application.port.out.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaDeInstrucoes {
    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final List<ValidadorAgendamento> validadoresAgendamento;

    @Transactional
    public DetalhamentoAgendamento agendar(DadosAgendamento dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("Id do aluno informado não existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new InstrutorNotFoundException("Id do instrutor informado não existe!");
        }

        //Validações
        validadoresAgendamento.forEach(validador -> validador.validar(dados));

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);
        if (instrutor == null) {
            throw new ValidacaoException("Não há instrutor disponível para a data/hora escolhida!");
        }

        Instrucao instrucao = new Instrucao(null, aluno, instrutor, dados.dataHora());
        Instrucao salvo = repository.save(instrucao);
        return new DetalhamentoAgendamento(salvo);
    }

    @Transactional
    public void cancelar(DadosCancelamento dados) {
        Instrucao instrucao = repository
                .findById(dados.idInstrucao())
                .orElseThrow(() ->
                        new ValidacaoException("Id da instrução informado não existe!"));

        if (instrucao.isCancelada()) {
            throw new ValidacaoException("Esta instrução já foi cancelada!");
        }

        long horasAntecedencia = Duration.between(LocalDateTime.now(), instrucao.getDataHora()).toHours();
        if (horasAntecedencia < 24) {
            throw new ValidacaoException("O cancelamento deve ser feito com antecedência mínima de 24 horas!");
        }

        instrucao.cancelar(dados.motivo(), LocalDateTime.now());
        repository.save(instrucao);
    }

    private Instrutor escolherInstrutor(DadosAgendamento dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória, caso o instrutor não seja informado!");
        }
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(
                dados.especialidade(),
                dados.dataHora()
        );
    }
}