package dev.smartpanda.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private final int linha;
	private final int coluna;
	
	private boolean campoAberto = false;
	private boolean campoMinado = false;
	private boolean campoMarcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	
	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;		
		}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
		
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream()
		.forEach(o -> o.eventoOcorreu(this, evento));
		
	}
	
		
		boolean addVizinho (Campo vizinho) {
			boolean isLinhaDiferente = linha != vizinho.linha; // teste se a linha é diferete, se for igual é horizontal
			boolean isColunaDiferente = coluna !=vizinho.coluna; // teste se a coluna é diferete, se for igual é vertical
			boolean isDiagonal = isLinhaDiferente && isColunaDiferente; /*sera diagonal se nao for mesma linha e nao for mesma coluna
																		* isDiagonal = false => vertical/horizontal (DeltaGeral=1) 
																		* na horizontal/vertical, para ser vizinho, o deltaGeral (soma de daltas) deve ser sempre 01 (um)
																		* isDiagonal = true => (DeltaGeral=2) 
																		* na diagonal, para ser vizinho, o deltaGeral (soma de daltas) deve ser sempre 02 (dois)
																		*/
			int deltaLinha = Math.abs(linha - vizinho.linha); // testa diferenca (distancia) absoluta entre as celulas em linha
			int deltaColuna = Math.abs(coluna - vizinho.coluna); // testa diferenca (distancia) absoluta entre as celulas em colunas
			
			int deltaGeral = deltaColuna + deltaLinha; /* dois cenarios possiveis para ser vizinho
														* a) diagonal e deltaGeral = 2 => vizinho:true
														* b) horizontal/vertical e deltaGeral = 1 => vizinho:true
														*/
			// testando se é vizinho, adiciona caso true
			if (!isDiagonal && deltaGeral ==1) {
				vizinhos.add(vizinho);
				return true;
			} else if (isDiagonal && deltaGeral == 2) {
				vizinhos.add(vizinho);
				return true;
			}else {
			return false;
			}
		}

		// metodo para aternar entre campos marcados (bandeira) quando ainda fechados
		void alternarMarcacao() {
			if (!campoAberto) {
				campoMarcado = !campoMarcado;
			}
		}
		
		boolean abrirCampo() {
			if (!campoAberto && !campoMarcado) {
				campoAberto = true;
				
				if(campoMinado) { // caso marque uma mina
				//TODO IMPLEMENTAR NOVA VERSAO
				}
				
				if (vizinhacaSegura()) { // se o campo nao esta minado, o metodo abre os vizinhos, que testam novamente se é minado sucessivamente
					vizinhos.forEach(v -> v.abrirCampo());
				}
				
				return true; // caso tenha aberto o campo, retornara true
				
			} else {
			
			return false; // enquanto nao marcado para abrir, retorna false
			}
		}
		
		boolean vizinhacaSegura() { // verifica se nao há campo minado nas proximidades (??)
			return vizinhos.stream().noneMatch(v -> v.campoMinado);
		}
		
		void minarCampo() {
			campoMinado = true;
		}
		
		public boolean isMarcado() { // funciona como get... para retornar se esta marcado ou nao
			return campoMarcado;
		}
		
				
		void setCampoAberto(boolean campoAberto) {
			this.campoAberto = campoAberto;
		}

		public boolean isCampoAberto() {
			return campoAberto;
		}
		
		public boolean isCampoFechado() {
			return !isCampoAberto();
		}
		
		public boolean isMinado() {
			return campoMinado;
		}

		public int getLinha() {
			return linha;
		}

		public int getColuna() {
			return coluna;
		}
		
		boolean objetivoAlcancado() {
			boolean desvendado = !campoMinado && campoAberto;
			boolean protegido = campoMinado && campoMarcado;
						
			return desvendado || protegido;
			
		}
		
		long minasNaVizinhanca() {
			return vizinhos.stream().filter(v -> v.campoMinado).count();
		}
		
		void reiniciarCampo() {
			campoAberto = false;
			campoMarcado = false;
			campoMinado = false;
		}
		
}







