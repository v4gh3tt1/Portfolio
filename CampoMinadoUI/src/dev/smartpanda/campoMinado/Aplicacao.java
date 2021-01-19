package dev.smartpanda.campoMinado;

import dev.smartpanda.campoMinado.modelo.Tabuleiro;
import dev.smartpanda.campoMinado.visao.TabuleiroConsole;

public class Aplicacao {

	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro(6, 6, 3);
		
	new TabuleiroConsole(tabuleiro);
	}

}
