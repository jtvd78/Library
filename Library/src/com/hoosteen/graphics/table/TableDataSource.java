package com.hoosteen.graphics.table;

import java.util.List;

public interface TableDataSource<E extends TableData> {
	
	public E[] getData();

	public int getColumnCount();

}
