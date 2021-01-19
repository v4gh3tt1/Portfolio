package dev.smartpanda.campoMinado.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import dev.smartpanda.campoMinado.excecao.ExplosaoException;
import dev.smartpanda.campoMinado.excecao.SairException;
import dev.smartpanda.campoMinado.modelo.Tabuleiro;

public class TabuleiroConsole {
				
		private Tabuleiro tabuleiro;
		private Scanner entrada = new Scanner (System.in);
		
		public TabuleiroConsole(Tabuleiro tabuleiro) {
			this.tabuleiro = tabuleiro;
		
			executarJogo();
		}

		private void executarJogo() {
			try {
				boolean continuar = true;
				
				while (continuar) {
					cicloDoJogo();
					
					System.out.println("Outra partida? (S/n) ");
					String resposta = entrada.nextLine()
;					
					if ("n".equalsIgnoreCase(resposta)) {
						continuar = false;
					} else {
						tabuleiro.reiniciarTab();
					}
					
				}
			} catch (SairException e) {
				System.out.println("Fim de jogo");
			} finally {
				entrada.close();
			}
			
		}

		private void cicloDoJogo() {
			try {
				while (!tabuleiro.objAalcancado() ) {
					System.out.println(tabuleiro.toString());
					
					String digitado = capturarValorDigitado("Digite [linha, coluna]: ");
				
					Iterator<Integer> xy = Arrays.stream(digitado.split(","))
							.map(e -> Integer.parseInt(e.trim())).iterator();
					digitado = capturarValorDigitado ("1 - Abrir ou 2 - (des)Marcar");
					
					if ("1".equals(digitado)) {
						tabuleiro.abrir(xy.next(), xy.next());
					} else if ("2".equals(digitado)) {
						tabuleiro.alterarMarcacao(xy.next(), xy.next());
					}
				}
				System.out.println(tabuleiro);
				System.out.println("Ganhou!! ;p");
				
			} catch (ExplosaoException e ) {
				System.out.println(tabuleiro);
				System.out.println("Perdeu !! :/");
			}			
		}

		private String capturarValorDigitado (String texto) {
			System.out.print(texto);
			String digitado = entrada.nextLine();
			
			if ("sair".equalsIgnoreCase(digitado)) {
				throw new SairException();
				
			}
			return digitado;
		}
}
