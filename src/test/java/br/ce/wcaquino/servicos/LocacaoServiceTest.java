package br.ce.wcaquino.servicos;

import java.util.Date;

import org.junit.Assert;
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
		Assert.assertTrue(locacao.getValor() == 5.0);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
}
