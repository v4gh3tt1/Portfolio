package dev.smartpanda.campoMinado.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import dev.smartpanda.campoMinado.modelo.Campo;
import dev.smartpanda.campoMinado.modelo.CampoEvento;
import dev.smartpanda.campoMinado.modelo.CampoObservador;

@SuppressWarnings("serial")
public class ButtonCampo extends JButton implements CampoObservador, MouseListener {
	
	private Campo campo;
	private Color BG_PADRAO = new Color(184, 184, 184);
	private Color BG_MARCADO = new Color(8, 179, 247);
	private Color BG_EXPLOSAO = new Color(169, 66, 68);
	private Color BG_VERDE = new Color(0, 100, 0);
	
	public ButtonCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		campo.registrarObservadores(this);
		addMouseListener(this);
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
			case ABRIR:
				aplicarEstiloAbrir();
				break;
			case MARCAR:
				aplicarEstiloMarcar();
				break;
			case EXPLODIR:
				aplicarEstiloExplodir();
				break;
			default: 
				aplicarEstiloPadrao();
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setText("");
		setBorder(BorderFactory.createBevelBorder(0));
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLOSAO);
		setText("X");
		setForeground(Color.WHITE);
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstiloAbrir() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		if(campo.isMinado()) {
			setBackground(BG_EXPLOSAO);
			setText("");
			setForeground(Color.WHITE);
		} else {			
			switch(campo.minasNaVizinhanca()) {
			case 1:
				setForeground(BG_VERDE);
				break;
			case 2:
				setForeground(Color.BLUE);
				break;
			case 3:
				setForeground(Color.YELLOW);
				break;
			case 4:
			case 5:
			case 6:
				setForeground(Color.RED);
				break;
			default:
				setForeground(Color.PINK);
			}
			String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
			setText(valor);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.alternarMarcacao();
		}
	}
	
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	
}
