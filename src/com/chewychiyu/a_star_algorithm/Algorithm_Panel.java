package com.chewychiyu.a_star_algorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Algorithm_Panel extends JPanel{

	Dimension panel_dim_;
	int grid_size_ = 20;
	double obstacle_spawn_rate_ = 0.3;
	Cell[][] cells_;
	Cell target_;
	Cell start_;

	final int ADJACENT = 10;
	final int DIAGONAL = 14;

	ArrayList<Cell> open_;
	ArrayList<Cell> close_;

	Timer _run_time;

	public Algorithm_Panel(Dimension _panel_dim){
		panel_dim_ = _panel_dim;
		_make_panel();
		_adjust_grid();

		_run_time = new Timer(10, e -> {
			if(!_step()){
				_run_time.stop();
			}
		});

	}

	public void _adjust_grid(){
		open_ = new ArrayList<Cell>();
		close_ = new ArrayList<Cell>();
		cells_ = new Cell[grid_size_][grid_size_];
		start_ = new Cell(new Point(0,0),Cell_Type.START);
		target_ = new Cell(new Point(cells_.length-1,cells_[0].length-1),Cell_Type.TARGET);
		for(int _r = 0; _r < cells_.length; _r++){
			for(int _c = 0; _c < cells_[0].length; _c++){
				if(_r==start_.location_.y && _c==start_.location_.x){
					cells_[_r][_c] = start_;
				}else if(_r==target_.location_.y && _c==target_.location_.x){
					cells_[_r][_c] = target_;
				}else{
					cells_[_r][_c] = new Cell(new Point(_c,_r), Cell_Type.OTHER);
				}
			}
		}
		start_.open( get_g_cost_of(start_,null), get_h_cost_of(start_,target_), null);
		open_.add(start_);
		repaint();
	}

	public int get_h_cost_of(Cell _cell, Cell _target){
		return ADJACENT * (Math.abs(_cell.location_.x - _target.location_.x) + Math.abs(_cell.location_.y - _target.location_.y));
	}

	public int get_g_cost_of(Cell _cell, Cell _parent){
		if(_parent == null) { return 0; }
		//adjacent
		if(Math.abs(_cell.location_.x-_parent.location_.x) == 0 || Math.abs(_cell.location_.y-_parent.location_.y) == 0){
			return _parent.g_cost_ + ADJACENT;
		}else{ //diagonal
			return _parent.g_cost_ + DIAGONAL;
		}
	}

	public void _randomize_obstacles(){
		_run_time.stop();
		_adjust_grid();
		for(int _r = 0; _r < cells_.length; _r++){
			for(int _c = 0; _c < cells_[0].length; _c++){
				Cell _cell = cells_[_r][_c];
				if(Math.random() < obstacle_spawn_rate_ && _cell.cell_type_ == Cell_Type.OTHER){
					_cell.cell_type_ = Cell_Type.UNAVAILABLE;
				}
			}
		}
		repaint();
	}

	public boolean _step(){

		//get optimal cell
		Cell _optimal = _get_optimal_cell();

		if (_optimal == null || _optimal.equals(target_) || open_.isEmpty() ){ return false; }

		//open surrounding cells and add to collection of open
		ArrayList<Cell> _cells_to_open = _get_possible_open_from(_optimal);
		for(int _index = 0; _index < _cells_to_open.size(); _index++){
			Cell _cell = _cells_to_open.get(_index);
			if(_cell.cell_type_ == Cell_Type.UNAVAILABLE && _cell.cell_type_ == Cell_Type.CLOSED){ continue; }
			if(_cell.cell_type_ == Cell_Type.OTHER || _cell.cell_type_ == Cell_Type.TARGET){
				_cell.open(get_g_cost_of(_cell,_optimal), get_h_cost_of(_cell,target_), _optimal);
				open_.add(_cell);
			}
			if(_cell.cell_type_ == Cell_Type.OPEN){ //check for faster path
				int _possible_optimal = get_g_cost_of(_cell,_optimal);
				if(_possible_optimal + _cell.h_cost_ < _cell.f_cost_){
					_cell.g_cost_ = _possible_optimal;
					_cell.f_cost_ = _cell.g_cost_ + _cell.h_cost_;
					_cell.parent_ = _optimal;
				}
			}
		}

		//remove _opimal from open and move to close
		_optimal.close();
		open_.remove(_optimal);
		close_.remove(_optimal);
		//repaint
		repaint();

		return true;
	}

	public void _run(){
		if(!_run_time.isRunning()){
			_run_time.start();
		}else{
			_run_time.stop();
		}
	}


	public ArrayList<Cell> _get_possible_open_from(Cell _cell){
		ArrayList<Cell> _cells = new ArrayList<Cell>();
		try{
			_cells.add(cells_[_cell.location_.y+1][_cell.location_.x]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y-1][_cell.location_.x]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y][_cell.location_.x+1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y][_cell.location_.x-1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y+1][_cell.location_.x+1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y-1][_cell.location_.x-1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y+1][_cell.location_.x-1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		try{
			_cells.add(cells_[_cell.location_.y-1][_cell.location_.x+1]);
		}catch(Exception e) { }//catching array index out of bounds exception 
		return _cells;
	}

	public Cell _get_optimal_cell(){	
		Cell _optimal_cell = null;
		for(int _index = 0; _index < open_.size(); _index++){
			Cell _cell = open_.get(_index);
			if(_optimal_cell == null || _optimal_cell.f_cost_ > _cell.f_cost_){
				_optimal_cell = _cell;
			}else if(_optimal_cell.f_cost_ == _cell.f_cost_ && _optimal_cell.h_cost_ > _cell.h_cost_){
				_optimal_cell = _cell;
			}
		}
		return _optimal_cell;
	}

	public void _make_panel(){
		this.setPreferredSize(panel_dim_);
	}

	public void paintComponent(Graphics g){
		_draw_grid(g);
		_draw_path_from(target_,g);
	}

	public void _draw_grid(Graphics g){
		int _x_buffer = 0;
		int _y_buffer = 0;
		final int _SPACER = (panel_dim_.height / cells_.length);
		for(int _r = 0; _r < cells_.length; _r++){
			for(int _c = 0; _c < cells_[0].length; _c++){

				Cell _cell = cells_[_r][_c];
				switch(_cell.cell_type_){
				case CLOSED:
					g.setColor(Color.RED);
					break;
				case OPEN:
					g.setColor(Color.GREEN);
					break;
				case OTHER:
					g.setColor(Color.WHITE);
					break;
				case UNAVAILABLE:
					g.setColor(Color.BLACK);
					break;
				case START:
					g.setColor(Color.BLUE);
					break;
				case TARGET:
					g.setColor(Color.BLUE);
					break;
				default:
					break;
				}

				g.fillRect(_x_buffer, _y_buffer, _SPACER, _SPACER);
				g.setColor(Color.BLACK);
				g.drawRect(_x_buffer, _y_buffer, _SPACER, _SPACER);
				_x_buffer+=_SPACER;
			}
			_x_buffer = 0;
			_y_buffer+=_SPACER;
		}
	}
	
	public void _draw_path_from(Cell _parent, Graphics g){
		 Graphics2D g2 = (Graphics2D) g;
         g2.setStroke(new BasicStroke(10));
		if(_parent.parent_ != null){
			final int _SPACER = (panel_dim_.height / cells_.length);
			g.setColor(Color.PINK);
			int _draw_from_x = _parent.location_.x * _SPACER;
			int _draw_from_y = _parent.location_.y * _SPACER;
			int _draw_to_x = _parent.parent_.location_.x * _SPACER;
			int _draw_to_y = _parent.parent_.location_.y * _SPACER;
             g2.draw(new Line2D.Float(_draw_from_x, _draw_from_y, _draw_to_x, _draw_to_y));
			_draw_path_from(_parent.parent_ , g);
		}
	}

}
