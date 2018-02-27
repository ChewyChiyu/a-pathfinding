package com.chewychiyu.a_star_algorithm;

import java.awt.Point;

public class Cell {

	public Cell_Type cell_type_;
	public Point location_;
	public Cell parent_;
	public int h_cost_;
	public int g_cost_;
	public int f_cost_;
	
	public Cell(Point _location, Cell_Type _cell_type){
		cell_type_ = _cell_type;
		location_ = _location;
	}
	
	public String toString(){
		return " locaiton : " + location_ + " f(x) : " + f_cost_ + " g(x) : " + g_cost_ +" h(x) : " + h_cost_;
	}
	
	public void open(int _g_cost, int _h_cost, Cell _parent){
		if(cell_type_ != Cell_Type.START && cell_type_ != Cell_Type.TARGET)
		cell_type_ = Cell_Type.OPEN;
		parent_ = _parent;
		g_cost_ = _g_cost;
		h_cost_ = _h_cost;
		f_cost_ = g_cost_ + h_cost_;
	}
	
	public void close(){
		if(cell_type_ != Cell_Type.START && cell_type_ != Cell_Type.TARGET)
		cell_type_ = Cell_Type.CLOSED;
	}
	
}
