package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

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
	
	private int contador;// o incremento nao funciona(1, 1, 1, 1, .... )
	private static int contador2;// o incremento funciona(1, 2, 3, 4, .... )

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		System.out.println("Before");
		locacaoService = new LocacaoService();
		contador++;
		contador2++;
		System.out.println(contador + " - " + contador2);
	}
	
	@After
	public void tearDown() {
		System.out.println("After");
	}
	
	@BeforeClass
	public static void setupClass() {
		System.out.println("Before class");
	}
	
	@After
	public void tearDownClass() {
		System.out.println("After class");
	}
	
	//1 forma de tratar excecao: mostra que deu uma falha, porem nao se sabe onde.
	@Test
	public void teste() {
		//cenario
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
	
	//Forma Elegante -> quando vc tem certeza que aquela excessao eh somente para esse caso
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		//cenario
		Usuario user = new Usuario("Fulano");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
		locacaoService.alugarFilme(user, filme);
	}
	
	//Forma Robusta -> nao precisa passar a excessao esperada no @Test, pois ja compara com a mensagem de erro
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		// cenario
		Filme filme = new Filme("Filme 2", 1, 4.0);
				
		//acao
		try {
			locacaoService.alugarFilme(null, filme);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario Vazio"));
		}

	}
	
	//Forma nova -> espera o erro antes da execucao do metodo
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
