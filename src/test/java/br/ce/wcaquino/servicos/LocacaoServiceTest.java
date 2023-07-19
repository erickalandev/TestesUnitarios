package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	private LocacaoService locacaoService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
	}
	
	//1 forma de tratar excecao: mostra que deu uma falha, porem nao se sabe onde.
	@Test
	public void teste() {
		//cenario
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
				
		//acao
		Locacao locacao;
		try {
			locacao = locacaoService.alugarFilme(user, filmes);
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
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		
		//acao
		Locacao locacao = locacaoService.alugarFilme(user, filmes);
		//verificacao
		assertThat(locacao.getValor(), is(equalTo(5.0)));				
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
	}
	//utilizando ErrorCollector
	@Test
	public void testLocacao2() throws Exception {
		//cenario
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
				
		//acao
		Locacao locacao = locacaoService.alugarFilme(user, filmes);
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		//cenario
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));
				
		//acao
		locacaoService.alugarFilme(user, filmes);
	}
	
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
				
		//acao
		try {
			locacaoService.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario Vazio"));
		}

	}
	
	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		locacaoService.alugarFilme(usuario, null);
	}
}
