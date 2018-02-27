package com.chewychiyu.a_star_algorithm;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class Algorithm_Control_Panel extends JPanel{

	Algorithm_Panel panel_;

	Dimension control_panel_dim_;

	Point _click = new Point();

	public Algorithm_Control_Panel(Algorithm_Panel _panel, Dimension _control_panel_dim){
		panel_ = _panel;
		control_panel_dim_ = _control_panel_dim;
		_make_panel();
	}

	public void _make_panel(){
		this.setPreferredSize(control_panel_dim_);
		_add_ui();
	}

	public void _add_ui(){
		final int _MIN_GRID_LENGTH = 10;
		final int _MAX_GRID_LENGTH = 50;
		final int _GRID_INITIAL_LENGTH = 10;

		JSlider _grid_adjust = new JSlider(JSlider.HORIZONTAL,
				_MIN_GRID_LENGTH, _MAX_GRID_LENGTH, _GRID_INITIAL_LENGTH);
		_grid_adjust.setMajorTickSpacing(10);
		_grid_adjust.setPaintTicks(true);
		_grid_adjust.setPaintLabels(true);
		_grid_adjust.addChangeListener(e -> {
			JSlider source = (JSlider) e.getSource();
			panel_.grid_size_ = (int)source.getValue();
			panel_._adjust_grid();
		});
		this.add(_grid_adjust);


		final int _MIN_RANDOM = 0;
		final int _MAX_RANDOM = 10;
		final int _RANDOM_INITIAL = 3;

		JSlider _grid_random = new JSlider(JSlider.HORIZONTAL,
				_MIN_RANDOM, _MAX_RANDOM, _RANDOM_INITIAL);
		_grid_random.setMajorTickSpacing(1);
		_grid_random.setPaintTicks(true);
		_grid_random.setPaintLabels(true);
		_grid_random.addChangeListener(e -> {
			JSlider source = (JSlider) e.getSource();
			panel_.obstacle_spawn_rate_ = (double) source.getValue() / 10;

		});
		this.add(_grid_random);

		JButton _randomize = new JButton("Randomize");
		_randomize.addActionListener(e -> {
			panel_._randomize_obstacles();
		});
		this.add(_randomize);

		JButton _step = new JButton("Step");
		_step.addActionListener(e -> {
			panel_._step();
		});
		this.add(_step);

		JButton _run = new JButton("Run");
		_run.addActionListener(e -> {
			panel_._run();
		});
		this.add(_run);

		panel_.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				fill_in_at(e,false);
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {				
			}

			@Override
			public void mouseExited(MouseEvent e) {				
			}

		});

		panel_.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				fill_in_at(e,true);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public Point _xy_to_rc(MouseEvent e){
		int _row = e.getY() / (panel_.panel_dim_.height / panel_.cells_.length);
		int _col = e.getX() / (panel_.panel_dim_.height / panel_.cells_.length);
		return new Point(_col,_row);
	}

	public void fill_in_at(MouseEvent e, boolean _mouse_drag){
		_click = _xy_to_rc(e);
		if(!_mouse_drag){
			if(panel_.cells_[_click.y][_click.x].cell_type_ == Cell_Type.UNAVAILABLE){
				panel_.cells_[_click.y][_click.x].cell_type_ = Cell_Type.OTHER;
			}else if(panel_.cells_[_click.y][_click.x].cell_type_ == Cell_Type.OTHER){
				panel_.cells_[_click.y][_click.x].cell_type_ = Cell_Type.UNAVAILABLE;
			}	
		}else{
			if(panel_.cells_[_click.y][_click.x].cell_type_ == Cell_Type.OTHER){
				panel_.cells_[_click.y][_click.x].cell_type_ = Cell_Type.UNAVAILABLE;
			}	
		}
		panel_.repaint();
	}

}
