package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	//1 forma de tratar excecao: mostra que deu uma falha, porem nao se sabe onde.
	@Test
	public void teste() {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 2, 5.0);
				
		//acao
		Locacao locacao;
		try {
			locacao = locacaoService.alugarFilme(user, filme);
			//verificacao
			assertThat(locacao.getValor(), is(equalTo(5.0)));
			assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Nao deveria lancar excessao");
		}	
	}
	
	//2 forma de tratar excecao: tratativa do proprio JUnit, porem ao inves de retornar falha, retorna erro
	@Test
	public void testLocacao() throws Exception {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 2, 5.0);
				
		//acao
		Locacao locacao;
		locacao = locacaoService.alugarFilme(user, filme);
		//verificacao
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
	}
	
	/*Esse teste do proprio JUnit seria caso estivessemos esperando a excessao e retornando como sucesso, 
	 * porem caso nao estourasse excessao retornaria como falha.
	 * Que esta correto por sinal, pois o nosso metodo "testLocacao_filmeSemEstoque()" espera um filme sem estoque*/
	@Test(expected = Exception.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
		locacaoService.alugarFilme(user, filme);
		
	}
	
	/*Esse teste feito manual seria caso estivessemos esperando a excessao e retornando mensagem de ok,
	porem caso nao estourasse excessao ainda retornaria como sucesso
	Que esta errado, pois o nosso metodo "testLocacao_filmeSemEstoque2()" espera um filme sem estoque*/
	@Test
	public void testLocacao_filmeSemEstoque2() {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
		try {
			locacaoService.alugarFilme(user, filme);
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
		
	}
	
	/*correcao do testLocacao_filmeSemEstoque2() que retorna um falso positivo(um teste que nao deria passar, mas passou)*/
	@Test
	public void testLocacao_filmeSemEstoque3() {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
		try {
			locacaoService.alugarFilme(user, filme);
			fail("Deveria ter lancado uma excessao");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
		
	}
	
	@Test
	public void testLocacao_filmeSemEstoque4() throws Exception {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
				
		//acao
		locacaoService.alugarFilme(user, filme);
	}
}
