package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {
	/*A suite serve pra rodar todos os testes que voce lista na anotacao SuiteClasses, 
	 nao achei muito eficaz pois o proprio STS ja tem sua forma de rodar tudo. 
	 o interessante mesmo dessa suite seria criar variaveis de inicializacao com as anotacoes
	 BeforeClass e AfterClass em caso de criar banco de dados temporarios para teste
	 */
}
