package br.ce.wcaquino.servicos;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	@Test
	public void inicio() {
		System.out.println("eu sou primeiro");
	}
	
	@Test
	public void verifica() {
		System.out.println("eu sou segundo");
	}
}
