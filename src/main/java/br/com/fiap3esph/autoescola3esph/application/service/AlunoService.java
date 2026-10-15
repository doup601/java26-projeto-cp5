package br.com.fiap3esph.autoescola3esph.application.service;

import br.com.fiap3esph.autoescola3esph.adapter.in.controller.mapper.AlunoMapper;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3esph.autoescola3esph.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3esph.autoescola3esph.application.core.domain.Aluno;
import br.com.fiap3esph.autoescola3esph.application.port.out.AlunoRepository;
import br.com.fiap3esph.autoescola3esph.exception.type.AlunoNotFoundException;
import br.com.fiap3esph.autoescola3esph.shared.vo.endereco.Endereco;
import br.com.fiap3esph.autoescola3esph.shared.vo.endereco.mapper.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final EnderecoMapper enderecoMapper;
    private final EnderecoService enderecoService;

    @Transactional
    public DadosDetalhamentoAluno cadastrarAluno(DadosCadastroAluno dados) {
        Aluno aluno = mapper.toDomain(dados);
        aluno.atualizarEndereco(enderecoService.enriquecer(aluno.getEndereco()));
        Aluno saved = repository.save(aluno);
        return mapper.toDetails(saved);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemAluno> listarAlunos(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno detalharAluno(Long id) {
        Aluno aluno = repository
                .findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
        return mapper.toDetails(aluno);
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = repository
                .findById(dados.id())
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));

        Endereco endereco = dados.endereco() == null
                ? null
                : enderecoService.enriquecer(enderecoMapper.toVo(dados.endereco()));

        aluno.atualizarInformacoes(
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.cpf(),
                endereco
        );
        Aluno saved = repository.save(aluno);
        return mapper.toDetails(saved);
    }

    @Transactional
    public void excluirAluno(Long id) {
        Aluno aluno = repository
                .findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
        aluno.excluir();
        repository.save(aluno);
    }
}
