package com.umlet.element.experimental.facet.common;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;

public class SeparatorLineFacet extends Facet {

	public static SeparatorLineFacet INSTANCE = new SeparatorLineFacet(false);
	public static SeparatorLineFacet INSTANCE_WITH_HALIGN_CHANGE = new SeparatorLineFacet(true);

	private static final String KEY = "--";
	
	private boolean setHAlignToLeftAfterLine;
	private static final int H_SPACE = 4;

	private SeparatorLineFacet(boolean setHAlignToLeftAfterLine) {
		this.setHAlignToLeftAfterLine = setHAlignToLeftAfterLine;
	}
	
	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		if (setHAlignToLeftAfterLine) {
			propConfig.sethAlign(AlignHorizontal.LEFT);
		}
		double linePos = propConfig.getDividerPos(drawer);
		XValues xPos = propConfig.getXLimits(linePos);
		drawer.drawLine(xPos.getLeft()+0.5, linePos, xPos.getRight()-1, linePos);
		propConfig.addToYPos(H_SPACE);
	}

	@Override
	public boolean checkStart(String line, PropertiesConfig propConfig) {
		return line.equals(KEY);
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY, "draw horizontal line"));
	}

}
