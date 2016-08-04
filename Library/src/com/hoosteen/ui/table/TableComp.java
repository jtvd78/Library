package com.hoosteen.ui.table;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
	
	Color bgColor = Color.WHITE;
	
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
		this.setBackground(bgColor);
		getVerticalScrollBar().setUnitIncrement(rowHeight);
	}
	
	ArrayList<TableActionListener> listeners = new ArrayList<TableActionListener>();
	
	public void addTableActionListener(TableActionListener listener){
		listeners.add(listener);
	}
	
	class InnerTableComp extends JPanel{
		
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			
			GraphicsWrapper gw = new GraphicsWrapper(g);
			
			g.setColor(bgColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
	//		data = source.getData();
			
			//You can't do anything if there's no data
			if(data == null || data.length == 0){
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
				//	gw.drawCenteredString(str, r);	
					gw.drawCenteredString(str, r.offset(5, 0), GraphicsWrapper.HorizAlign.LEFT, GraphicsWrapper.VertAlign.MIDDLE);
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
			int widthSum = 0;
			for(Integer i : colWidths){
				widthSum += i;			
			}
			return widthSum+1;
		}
		
		@Override
		public int getHeight(){
			
			if(data == null){
				return 0;
			}
			
			return (data.length+1)*rowHeight+1;
		}
		
		public void updatePreferredSize(){
			System.out.println("Height " + getHeight());
			setPreferredSize(new Dimension(getWidth(), getHeight()));
		}
	}
	
	public void dataChanged(){
		
		System.out.println("Data changed");
		
		data = source.getData();
		
		inner.updatePreferredSize();
		repaint();
		
	}
	
	int mouseX = 0;
	int mouseY = 0;
	
	int draggingCol = -1;
	
	class Listener implements MouseMotionListener, MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			//The user clicked under the table, there are no rows there
			int row = mouseY / rowHeight - 1;	
			if(row > data.length - 1 || row < 0 || mouseX > inner.getWidth()){
				selectedRow = -1;
				repaint();
				return;
			}
			
			
			
			if(e.getButton() == 3){
				for(TableActionListener l : listeners){
					l.rowRightClicked(row, data[row]);
				}	
				dataChanged();
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
				dataChanged();
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
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