package dev.smartpanda.campoMinado.visao;

import javax.swing.JFrame;

import dev.smartpanda.campoMinado.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipalUI extends JFrame{
	
	public TelaPrincipalUI() {
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 3);
		add (new PainelTabuleiro(tabuleiro));
		
		setTitle("** Campo Minado **");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}		
	public static void main(String[] args) {
		new TelaPrincipalUI();
			
	}
}
