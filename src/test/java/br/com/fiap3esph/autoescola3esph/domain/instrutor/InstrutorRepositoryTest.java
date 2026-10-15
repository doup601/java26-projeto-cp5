package br.com.fiap3esph.autoescola3esph.domain.instrutor;

import br.com.fiap3esph.autoescola3esph.adapter.out.repository.InstrutorRepositoryImpl;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.entity.AlunoEntity;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.entity.InstrucaoEntity;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.entity.InstrutorEntity;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.mapper.InstrutorEntityMapper;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Instrutor;
import br.com.fiap3esph.autoescola3esph.application.port.out.InstrutorRepository;
import br.com.fiap3esph.autoescola3esph.shared.vo.endereco.Endereco;
import br.com.fiap3esph.autoescola3esph.shared.vo.enumeration.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({InstrutorRepositoryImpl.class, InstrutorEntityMapper.class})
public class InstrutorRepositoryTest {
    @Autowired
    InstrutorRepository repository;

    @Autowired
    TestEntityManager testEntity;

    @Test
    @DisplayName("Expectativa: Retornar null quando instrutor estiver ocupado.")
    void escolherInstrutorAleatorioDisponivelCenario1() {
        LocalDateTime proximaSegundaAs10 = proximaSegundaAs10();

        //cadastar aluno
        AlunoEntity aluno = cadastrarAluno();

        //cadastar instrutor
        InstrutorEntity instrutor = cadastrarInstrutor();

        //agendar instrução
        agendarInstrucao(aluno, instrutor, proximaSegundaAs10);

        //act or when
        Instrutor instrutorDisponivel = repository.escolherInstrutorAleatorioDisponivel(
                Especialidade.CAMINHOES,
                proximaSegundaAs10
        );

        //assert or then
        assertThat(instrutorDisponivel).isNull();
    }

    @Test
    @DisplayName("Expectativa: Retornar instrutor quando instrutor estiver disponivel.")
    void escolherInstrutorAleatorioDisponivelCenario2() {
        LocalDateTime proximaSegundaAs10 = proximaSegundaAs10();

        //cadastar instrutor
        cadastrarInstrutor();

        //act or when
        Instrutor instrutorDisponivel = repository.escolherInstrutorAleatorioDisponivel(
                Especialidade.CAMINHOES,
                proximaSegundaAs10
        );

        //assert or then
        assertThat(instrutorDisponivel).isNotNull();
        assertThat(instrutorDisponivel.getNome()).isEqualTo("Instrutor Teste");
        assertThat(instrutorDisponivel.getEspecialidade()).isEqualTo(Especialidade.CAMINHOES);
        assertThat(instrutorDisponivel.isAtivo()).isTrue();
    }

    private LocalDateTime proximaSegundaAs10() {
        return LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    private Endereco dadosEndereco() {
        return new Endereco(
                "Rua Teste",
                "000",
                "Casa dos Fundos",
                "Vila Teste",
                "Test City",
                "TS",
                "01234-567"
        );
    }

    private AlunoEntity cadastrarAluno() {
        AlunoEntity aluno = new AlunoEntity(
                null,
                "Aluno Teste",
                "alunoteste@email.com.br",
                "(11) 98765-4321",
                "12345678901",
                dadosEndereco(),
                true
        );
        return testEntity.persist(aluno);
    }

    private InstrutorEntity cadastrarInstrutor() {
        InstrutorEntity instrutor = new InstrutorEntity(
                null,
                "Instrutor Teste",
                "instrutorteste@email.com.br",
                "(11) 91234-5678",
                "01234567890",
                Especialidade.CAMINHOES,
                dadosEndereco(),
                true
        );
        return testEntity.persist(instrutor);
    }

    private void agendarInstrucao(
            AlunoEntity aluno,
            InstrutorEntity instrutor,
            LocalDateTime dataHora) {
        InstrucaoEntity instrucao = new InstrucaoEntity(
                null,
                aluno,
                instrutor,
                dataHora,
                false,
                null,
                null
        );
        testEntity.persist(instrucao);
    }
}
