package br.com.fiap3esph.autoescola3esph.adapter.in.controller.response.usuario;

import br.com.fiap3esph.autoescola3esph.shared.vo.enumeration.Role;

public record DadosListagemUsuario(
        Long id,
        String login,
        Role perfil) {
}
