package dev.smartpanda.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
		
	}
	
	
	public void abrir (int linha, int coluna) {
		try {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent (c -> c.abrirCampo());
			
		} catch (Exception e) {
			// FIXME Ajustar implementação metodo abrir
			campos.forEach(c -> c.setCampoAberto(true));
			throw e;
			
		}

	}
	

	public void alterarMarcacao (int linha, int coluna) {
		campos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst()
		.ifPresent (c -> c.alternarMarcacao());
	}


	private void gerarCampos() {
		for (int l = 0; l < linhas; l++) {
			for (int c = 0; c < colunas; c++) {
				campos.add(new Campo(l, c));
			}
			
		}
		
	}

	private void associarVizinhos() {
		for (Campo c1: campos) {
			for(Campo c2: campos) {
				c1.addVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate <Campo> minado = c -> c.isMinado();
		
		do {
			int aleatorio = (int)(Math.random() * campos.size());
			campos.get(aleatorio).minarCampo();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas);
		
	}
	
	public boolean objAalcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarTab() {
		campos.stream().forEach(c -> c.reiniciarCampo());
		sortearMinas();
	}		
}
