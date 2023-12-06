package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    // esta privado para que niguem crie instancias do builder externamente ao
    // proprio builder
    private UsuarioBuilder() {
    }

    // o metodo ficou publico e estatico para que ele possa ser acessado
    // externamente sem a necessidade
    // de uma instancia, ela eh a porta de entrada para criar um usuario
    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario("Usuario 1");
        return builder;
    }

    public Usuario agora() {
        return usuario;
    }
}
