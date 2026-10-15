package br.com.fiap3esph.autoescola3esph.application.port.out;

import br.com.fiap3esph.autoescola3esph.shared.vo.endereco.dto.DadosEnderecoCep;

import java.util.Optional;

public interface ConsultaCepPort {
    Optional<DadosEnderecoCep> consultar(String cep);
}
