package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import static br.ce.wcaquino.matcher.MatchersProprios.*;
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
	
	@Test
	public void deveAlugarFilme() throws Exception {
		//faz com que esse teste nao seja executado no sabado
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
				
		//acao
		Locacao locacao = locacaoService.alugarFilme(user, filmes);
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataLocacao(), ehHojeComDiferencaDias(1));
		
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		Usuario user = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));
				
		//acao
		locacaoService.alugarFilme(user, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
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
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		locacaoService.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0), 
											new Filme("Filme 2", 2, 4.0), 
											new Filme("Filme 3", 2, 4.0));		
		//acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		// 4+4+(25% 4) = 4+4+3=11
		assertThat(resultado.getValor(), is(11.0));
	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0), 
											new Filme("Filme 2", 2, 4.0), 
											new Filme("Filme 3", 2, 4.0),	
											new Filme("Filme 4", 2, 4.0));		
		//acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		// 4+4+(25% 4) + (50% 4) = 4+4+3+2=13
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar75PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0), 
											new Filme("Filme 2", 2, 4.0), 
											new Filme("Filme 3", 2, 4.0),	
											new Filme("Filme 4", 2, 4.0),		
											new Filme("Filme 5", 2, 4.0));		
		//acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		// 4+4+(25% 4) + (50% 4) + (75% 4) = 4+4+3+2+1=14
		assertThat(resultado.getValor(), is(14.0));
	}
	@Test
	public void devePagar100PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0), 
											new Filme("Filme 2", 2, 4.0), 
											new Filme("Filme 3", 2, 4.0),	
											new Filme("Filme 4", 2, 4.0),		
											new Filme("Filme 5", 2, 4.0),		
											new Filme("Filme 6", 2, 4.0));		
		//acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		// 4+4+(25% 4) + (50% 4) + (75% 4) + (100% 4) = 4+4+3+2+1+0=14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		
		//faz com que esse teste seja executado somente no sabado
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Fulano");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 4, 5.0));
		
		//acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		/*Formas de melhorar a entendibilidade do metodo
		//1
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		//2
		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//3
		assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
		//4*/
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
