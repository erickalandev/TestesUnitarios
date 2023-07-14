package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Test
	public void teste() {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 2, 5.0);
				
		//acao
		Locacao locacao = locacaoService.alugarFilme(user, filme);
		
		//verificacao
		assertEquals( 5.0, locacao.getValor(), 0.01);
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(not(5.0)));
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertTrue(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}
}
