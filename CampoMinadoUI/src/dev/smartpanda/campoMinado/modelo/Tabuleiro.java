package dev.smartpanda.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
	
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>>observadores = 
			new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
		
	}
	
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
		.forEach(o -> o.accept(new ResultadoEvento(resultado)));
		
	}
	
	
	public void abrir (int linha, int coluna) {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent (c -> c.abrirCampo());	
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
				Campo campo = new Campo(linhas, colunas);
				campo.registrarObservador(this);
				campos.add(campo);
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
	
	@Override
	public void eventoOcorreu (Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			System.out.println("perdeu");
			notificarObservadores(false);
		} else if (objAalcancado()) {
			System.out.println("Ganhou");
			notificarObservadores(true);
		}
	}
	private void mostrarMinas() {
		campos.stream()
		.filter(c -> c.isMinado());
		campos.forEach(c -> c.setCampoAberto(true));
		
	}
	
}
