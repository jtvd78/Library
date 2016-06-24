package com.hoosteen.graphics.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hoosteen.graphics.GraphicsWrapper;
import com.hoosteen.graphics.Rect;

public class TableComp<E extends TableData> extends JScrollPane{
	
	E[] data;
	
	int rowHeight = 25;
	int defaultColWidth = 100;
	
	int[] colWidths;
	
	int selectedRow = -1;
	
	InnerTableComp inner;
	TableDataSource<E> source;
	
	public TableComp(TableDataSource<E> source){
		this.source = source;
		this.inner = new InnerTableComp();
		
		Listener l = new Listener();
		addMouseMotionListener(l);
		addMouseListener(l);
		
		colWidths = new int[source.getColumnCount()];
		for(int i = 0; i < colWidths.length; i++){
			colWidths[i] = defaultColWidth;
		}
		
		setViewportView(inner);
		getVerticalScrollBar().setUnitIncrement(rowHeight);
	}
	
	ArrayList<TableActionListener> listeners = new ArrayList<TableActionListener>();
	
	public void addTableActionListener(TableActionListener listener){
		listeners.add(listener);
	}
	
	class InnerTableComp extends JPanel{
		
		public void paintComponent(Graphics g){
			
			GraphicsWrapper gw = new GraphicsWrapper(g);
			
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			data = source.getData();
			
			//You can't do anything if there's no data
			if(data.length == 0){
				return;
			}		
			
			int col = 0;
			int colWidthSum = 0;
			String[] headers = data[0].getTableHeaders();
			for(String header : headers){
				
				
				
				for(int row = 0; row < data.length + 1; row ++){
					
					String str;
					Rect r = new Rect(colWidthSum, rowHeight * row, colWidths[col], rowHeight);
					
					
					
					
					
					
					
					//header
					if(row == 0){
						str = header;
						gw.fillRect(r, new Color(220,220,220));
					}else{
						str = data[row-1].getTableValues()[col];
						
						if(row -1 == selectedRow){
							gw.fillRect(r, new Color(190,190,220));
						}else{
							gw.fillRect(r, Color.WHITE);
						}
						
						
					}
					
					gw.setColor(Color.BLACK);
					gw.drawCenteredString(str, r);			
					gw.drawRect(r);
				}
				
				colWidthSum += colWidths[col];
				col++;
			}				
		}	
		
		/**
		 * Returns width of component. Helps auto-fit the InnerTreeComp to the
		 * ScrollPane
		 */
		@Override
		public int getWidth() {			
			int w = this.getParent().getWidth();			
			return w;
		}
	}
	
	int mouseX = 0;
	int mouseY = 0;
	
	int draggingCol = -1;
	
	class Listener implements MouseMotionListener, MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			int row = mouseY / rowHeight - 1;	
			if(row > data.length || row < 0){
				selectedRow = -1;
				repaint();
				return;
			}
			
			if(e.getButton() == 3){
				for(TableActionListener l : listeners){
					l.rowRightClicked(row, data[row]);
				}
			}
			
			
			if(e.getClickCount() == 1){
				
				for(TableActionListener l : listeners){
					l.rowSelected(row, data[row]);
				}
				
				selectedRow = row;			
				repaint();
				
			}else if(e.getClickCount() == 2){
				for(TableActionListener l : listeners){
					l.rowDoubleClicked(row, data[row]);
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			int colCtr = 0;
			int widthSum = 0;
			for(Integer i : colWidths){
				widthSum += i;
				
				if(Math.abs(widthSum - mouseX) <= 4 && mouseY < rowHeight){
					draggingCol = colCtr;
				}
				
				colCtr ++;
				
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			draggingCol = -1;
		}		

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
			int dX = e.getX() - mouseX;
			int dY = e.getY() - mouseY;
			
			boolean hovering = false;
			int widthSum = 0;
			for(Integer i : colWidths){
				widthSum += i;
				
				if(Math.abs(widthSum - mouseX) <= 3 && mouseY < rowHeight){
					hovering = true;
					break;
				}							
			}
			
			if(hovering){
				setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			}else{
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
			if(draggingCol > -1){
				colWidths[draggingCol] += dX;
				repaint();
			}
			
			
			mouseX = e.getX();
			mouseY = e.getY();
			
						
		}		
	}
}