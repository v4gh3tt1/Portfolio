package dev.smartpanda.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador {
	private final int linhas;
	private final int colunas;
	private final int minas;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		super();
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		construirTabuleiro();
	}
	
	public void registrarObservador(Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(resultado));
	}

	private void sortearMinas() {
		long minasArmadas = 0;
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter( c -> c.isMinado()).count();
		} while (minasArmadas < minas);
//		System.out.println(tabuleiroMinado());
	}

	public boolean objetivoAlcancado() {
		return campos.stream()
				.allMatch(c -> c.objetivoAlcancado());
	}

	private void associarOsVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}

	}

	public void abrir(int linha, int coluna) {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha 
			&& c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());
	}
	
	private void mostrarMinas() {
		campos.stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado())
			.forEach(c -> c.setAberto(true));
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
		.filter(c -> c.getLinha() == linha 
		&& c.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.alternarMarcacao());
	}

	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservadores(this);
				campos.add(campo);
				
			}
		}
	}

	public void reiniciar() {
		campos.forEach(c -> c.reiniciar());
		sortearMinas();
//		construirTabuleiro();
	}
	
	private void construirTabuleiro() {
		System.out.println("entrou");
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}
	
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if(objetivoAlcancado()){
			System.out.println("Ganhou...");
			notificarObservadores(true);
		} 
	}

	public int getLinhas() {
		return linhas;
	}
	public int getColunas() {
		return colunas;
	}

	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}

	public int getMinas() {
		return minas;
	}

	public List<Campo> getCampos() {
		return campos;
	}
}
