package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

public class OrdemTest {
	
	//chamada aleatoria
	@Test
	public void inicio() {
		System.out.println("eu sou primeiro");
	}
	
	@Test
	public void verifica() {
		System.out.println("eu sou segundo");
	}
	
	//chamada ordenada
	@Test
	public void testGeralOrdenado() {
		inicio();
		verifica();
	}
}
