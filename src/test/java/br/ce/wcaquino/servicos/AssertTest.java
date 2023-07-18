package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		//onde deve retornar a condicao como verdadeira, 
		//se o resultado for falso retorna o teste como errado
		Assert.assertTrue(true);
		//versao inversa
		Assert.assertFalse(false);
		//para ver se sao iguai (valorEsperado, valorAtual)
		Assert.assertEquals(0, 0);
		/*valores com numeros quebrados fica depreciado, ou seja, 
		 * a comparacao(0.512, 0.512) nao fica totalmente certa. 
		 * Pra isso, devemos inserir um medidor de casas decimais
		 * para dizer ate que ponto a comparacao deve ser feita
		 * exemplo (0.512, 0.512, 0.001)
		 *  ele conta quantas casa depois do ponto sera comparado
		 * 
		 */
		Assert.assertEquals(0.5122, 0.512, 0.001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		/*valores do tipo primitivo(int,double,boolean...), nao da pra serem
		 * comparadas com objetos(Integer, Double, Boolean....) 
		 */
		int primitivo = 0;
		Integer objeto = 0;
		Assert.assertEquals(Integer.valueOf(primitivo), objeto);//conversao de primitivo para objeto ou
		Assert.assertEquals(primitivo, objeto.intValue());//conversao de objeto para primitivo
		/* para comparacao string tem que ser exatamente igual*/
		Assert.assertEquals("bola", "bola");
		/*para string ignorar maiusculo e minusculo */
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		/*para string comparar com radicais, ou seja, iniciais das palavras */
		Assert.assertTrue("bola".startsWith("bo"));
		/*para comparar objetos, temos que criar um metodo equals na classe dele
		 * porque senao o Assert vai comparar com o equal do Objeto do proprio Java onde passa o id
		 * do objeto como comparacao e dara sempre falso
		 * vamos pra classe usuario e criar esse metodo equals*/
		Usuario u1 = new Usuario("fulano");
		Usuario u2 = new Usuario("fulano");
		Assert.assertEquals(u1, u2);
		/*Agora quando  e comparacao entre instancias teria que ser a mesma classe mesmo
		 * exemplo o usuario 3 esta recebendo a mesma instancia do usuario 2, e 
		 * nao criando um objeto novo com a mesma caracteristicas como na etapa anterior*/
		Usuario u3 = u2;
		Assert.assertSame(u2, u3);
		/*se  algo e nulo*/
		u3=null;
		Assert.assertTrue(u3==null);
		Assert.assertNull(u3);
	}

}
