/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.layouts.pages;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public interface Assistants {

	String WIDTH = "width";
	String HEIGHT = "height";

	// Constants of FillLayout.
	String LAYOUT_FILL_SPACING = "spacing";
	String LAYOUT_FILL_MARGIN_WIDTH = "marginWidth";
	String LAYOUT_FILL_MARGIN_HEIGHT = "marginHeight";
	String LAYOUT_FILL_TYPE = "type";

	// Constants of GridLayout
	String LAYOUT_GRID_NUM_CLUMNS = "numColumns";
	String LAYOUT_GRID_MAKE_COLUMNS_EQUAL_WIDTH = "makeColumnsEqualWidth";
	String LAYOUT_GRID_MARGIN_WIDTH = "marginWidth";
	String LAYOUT_GRID_MARGIN_HEIGHT = "marginHeight";
	String LAYOUT_GRID_MARGIN_LEFT = "marginLeft";
	String LAYOUT_GRID_MARGIN_TOP = "marginTop";
	String LAYOUT_GRID_MARGIN_RIGHT = "marginRight";
	String LAYOUT_GRID_MARGIN_BOTTOM = "marginBottom";
	String LAYOUT_GRID_HORIZONTAL_SPACING = "horizontalSpacing";
	String LAYOUT_GRID_VERTICAL_SPACING = "verticalSpacing";

	// Constants of RowLayout
	String LAYOUT_ROW_TYPE = "type";
	String LAYOUT_ROW_MARGIN_WIDTH = "marginWidth";
	String LAYOUT_ROW_MARGIN_HEIGHT = "marginHeight";
	String LAYOUT_ROW_SPACING = "spacing";
	String LAYOUT_ROW_WRAP = "wrap";
	String LAYOUT_ROW_PACK = "pack";
	String LAYOUT_ROW_FILL = "fill";
	String LAYOUT_ROW_CENTER = "center";
	String LAYOUT_ROW_JUSTIFY = "justify";
	String LAYOUT_ROW_MARGIN_LEFT = "marginLeft";
	String LAYOUT_ROW_MARGIN_TOP = "marginTop";
	String LAYOUT_ROW_MARGIN_RIGHT = "marginRight";
	String LAYOUT_ROW_MARGIN_BOTTOM = "marginBottom";

	// Constants of RowData
	String LAYOUTDATA_ROW_WIDTH = WIDTH;
	String LAYOUTDATA_ROW_HEIGHT = HEIGHT;
	String LAYOUTDATA_ROW_EXCLUDE = "exclude";

	// Constants of GridData.
	String LAYOUTDATA_GRID_VERTICAL_ALIGNMENT = "verticalAlignment";
	String LAYOUTDATA_GRID_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	String LAYOUTDATA_GRID_WIDTH_HINT = "widthHint";
	String LAYOUTDATA_GRID_HEIGHT_HINT = "heightHint";
	String LAYOUTDATA_GRID_HORIZONTAL_INDENT = "horizontalIndent";
	String LAYOUTDATA_GRID_VERTICAL_INDENT = "verticalIndent";
	String LAYOUTDATA_GRID_HORIZONTAL_SPAN = "horizontalSpan";
	String LAYOUTDATA_GRID_VERTICAL_SPAN = "verticalSpan";
	String LAYOUTDATA_GRID_GRAB_EXCESS_HORIZONTAL_SPACE = "grabExcessHorizontalSpace";
	String LAYOUTDATA_GRID_GRAB_EXCESS_VERTICAL_SPACE = "grabExcessVerticalSpace";
	String LAYOUTDATA_GRID_MINIMUM_WIDTH = "minimumWidth";
	String LAYOUTDATA_GRID_MINIMUM_HEIGHT = "minimumHeight";
	String LAYOUTDATA_GRID_EXCLUDE = "exclude";

}
