package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.matcher.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LocacaoServiceTest {

	@InjectMocks
	private LocacaoService locacaoService;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private SPCService spcService;

	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// faz com que esse teste nao seja executado no sabado
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario user = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());

		// acao
		Locacao locacao = locacaoService.alugarFilme(user, filmes);
		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		// error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		// error.checkThat(isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataLocacao(), ehHojeComDiferencaDias(1));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario user = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

		// acao
		locacaoService.alugarFilme(user, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// acao
		try {
			locacaoService.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario Vazio"));
		}

	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		// acao
		locacaoService.alugarFilme(usuario, null);
	}

	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora());
		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// 4+4+(25% 4) = 4+4+3=11
		assertThat(resultado.getValor(), is(11.0));
	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora());
		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// 4+4+(25% 4) + (50% 4) = 4+4+3+2=13
		assertThat(resultado.getValor(), is(13.0));
	}

	@Test
	public void devePagar75PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora());
		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// 4+4+(25% 4) + (50% 4) + (75% 4) = 4+4+3+2+1=14
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void devePagar100PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora(),
				FilmeBuilder.umFilme().agora());
		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// 4+4+(25% 4) + (50% 4) + (75% 4) + (100% 4) = 4+4+3+2+1+0=14
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

		// faz com que esse teste seja executado somente no sabado
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		/*
		 * Formas de melhorar a entendibilidade do metodo
		 * //1
		 * boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(),
		 * Calendar.MONDAY);
		 * Assert.assertTrue(ehSegunda);
		 * //2
		 * assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		 * //3
		 * assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
		 * //4
		 */
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());

	}

	@Test
	public void naoDeveAlugarFilmeNegativadoSPC() throws FilmeSemEstoqueException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//forcar erro com usuario nao esperado
//		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spcService.possuiNegativacao(usuario)).thenReturn(true);
		//acao
		//dessa forma pode ocorrer um falso positivo
//		exception.expect(LocadoraException.class);
//		exception.expectMessage("Usuario negativado");

		//para corrigir o falso positivo, criasse o try catch
		try {
			locacaoService.alugarFilme(usuario,filmes);
			//verificacao
			Assert.fail();//garante que se passar no primeiro erro e nao estourar, ele valida se realmente nao teve erro
		}catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario negativado"));
		}
		Mockito.verify(spcService).possuiNegativacao(usuario);
		//Mockito.verify(spcService).possuiNegativacao(usuario2); //dessa forma estoura o erro gracas ao trycatch pois era esperado o usuario 1, sendo que sem o try catch da o falso positivo
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("usuario 2").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("usuario 3").agora();
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora());
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		//acao
		locacaoService.notificarAtrasos();

		//verificar
		Mockito.verify(emailService, Mockito.times(2)).notificarAtraso(Mockito.any(Usuario.class));
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(emailService);

	}
}
