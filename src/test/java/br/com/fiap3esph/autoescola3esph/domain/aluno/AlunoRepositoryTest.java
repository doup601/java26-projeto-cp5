package br.com.fiap3esph.autoescola3esph.domain.aluno;

import br.com.fiap3esph.autoescola3esph.adapter.out.repository.AlunoRepositoryImpl;
import br.com.fiap3esph.autoescola3esph.adapter.out.repository.mapper.AlunoEntityMapper;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Aluno;
import br.com.fiap3esph.autoescola3esph.application.port.out.AlunoRepository;
import br.com.fiap3esph.autoescola3esph.shared.vo.endereco.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({AlunoRepositoryImpl.class, AlunoEntityMapper.class})
public class AlunoRepositoryTest {
    @Autowired
    AlunoRepository repository;

    @Test
    @DisplayName("Expectativa: Persistir aluno e recuperá-lo pelo id.")
    void salvarEBuscarPorId() {
        //arrange or given
        Aluno aluno = novoAluno("Aluno Persistido", "12345678901", true);

        //act or when
        Aluno salvo = repository.save(aluno);
        Optional<Aluno> encontrado = repository.findById(salvo.getId());

        //assert or then
        assertThat(salvo.getId()).isNotNull();
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Aluno Persistido");
        assertThat(encontrado.get().getCpf()).isEqualTo("12345678901");
        assertThat(encontrado.get().getEndereco().getCidade()).isEqualTo("Test City");
        assertThat(encontrado.get().isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Expectativa: Listar apenas alunos ativos.")
    void listarSomenteAtivos() {
        //arrange or given
        repository.save(novoAluno("Aluno Ativo", "11111111111", true));
        repository.save(novoAluno("Aluno Inativo", "22222222222", false));

        //act or when
        Page<Aluno> page = repository.findAllByAtivoTrue(
                PageRequest.of(0, 10, Sort.by("nome")));

        //assert or then
        assertThat(page.getContent())
                .extracting(Aluno::getNome)
                .contains("Aluno Ativo")
                .doesNotContain("Aluno Inativo");
    }

    private Aluno novoAluno(String nome, String cpf, boolean ativo) {
        return new Aluno(
                null,
                nome,
                nome.toLowerCase().replace(" ", ".") + "@email.com.br",
                "(11) 98765-4321",
                cpf,
                new Endereco(
                        "Rua Teste",
                        "000",
                        "Casa",
                        "Vila Teste",
                        "Test City",
                        "TS",
                        "01234-567"
                ),
                ativo
        );
    }
}
