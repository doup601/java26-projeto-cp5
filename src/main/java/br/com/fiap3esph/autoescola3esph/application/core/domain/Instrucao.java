package br.com.fiap3esph.autoescola3esph.application.core.domain;

import br.com.fiap3esph.autoescola3esph.shared.vo.enumeration.MotivoCancelamento;

import java.time.LocalDateTime;

public class Instrucao {
    private Long id;
    private Aluno aluno;
    private Instrutor instrutor;
    private LocalDateTime dataHora;
    private boolean cancelada;
    private MotivoCancelamento motivoCancelamento;
    private LocalDateTime dataCancelamento;

    public Instrucao() {
    }

    public Instrucao(
            Long id,
            Aluno aluno,
            Instrutor instrutor,
            LocalDateTime dataHora) {
        this(id, aluno, instrutor, dataHora, false, null, null);
    }

    public Instrucao(
            Long id,
            Aluno aluno,
            Instrutor instrutor,
            LocalDateTime dataHora,
            boolean cancelada,
            MotivoCancelamento motivoCancelamento,
            LocalDateTime dataCancelamento) {
        this.id = id;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.dataHora = dataHora;
        this.cancelada = cancelada;
        this.motivoCancelamento = motivoCancelamento;
        this.dataCancelamento = dataCancelamento;
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public MotivoCancelamento getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void cancelar(MotivoCancelamento motivo, LocalDateTime dataCancelamento) {
        this.cancelada = true;
        this.motivoCancelamento = motivo;
        this.dataCancelamento = dataCancelamento;
    }
}
