package br.ce.wcaquino.matcher;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemanaInteger) {
		return new DiaSemanaMatcher(diaSemanaInteger);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	public static DiaSemanaMatcher ehHoje() {
		return new DiaSemanaMatcher(0);
	}
	public static DataDiferencaDiasMatcher ehHojeComDiferencaDias(Integer qtdDia) {
		return new DataDiferencaDiasMatcher(qtdDia);
	}
}
