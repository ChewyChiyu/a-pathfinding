package com.chewychiyu.a_star_algorithm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class A_Star_Launcher extends JFrame{
	
	Dimension panel_dim = new Dimension(800,700);
	Dimension control_panel_dim = new Dimension(800,100);
	
	public static void main(String[] args){
		new A_Star_Launcher();
	}
	
	A_Star_Launcher(){
		super("A* Algorithm");
		Algorithm_Panel _panel = new Algorithm_Panel(panel_dim);
		Algorithm_Control_Panel _control_panel = new Algorithm_Control_Panel(_panel,control_panel_dim);
		_make_frame(_panel,_control_panel);
	}
	
	public void _make_frame(JPanel _panel, JPanel _control_panel){
		this.add(_panel, BorderLayout.NORTH);
		this.add(_control_panel, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
