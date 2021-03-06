/*
 * Copyright (c) 2010 Célio Cidral Junior
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.tomighty.resources;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import org.tomighty.ioc.Inject;
import org.tomighty.resources.cache.Cache;
import org.tomighty.resources.cache.Caches;
import org.tomighty.resources.cache.Icons;
import org.tomighty.time.Time;
import org.tomighty.ui.Tray;
import org.tomighty.ui.state.laf.look.Colors;
import org.tomighty.ui.state.laf.look.Look;
import org.tomighty.ui.util.Canvas;

public class TrayIcons {

	@Inject private Resources resources;
	@Inject private Tray tray;
	@Inject private Look look;
	@Inject private Caches caches;
	
	private int defaultIconSize;

	public void defaultIconSize(int size) {
		defaultIconSize = size;
		
	}

	public Image tomato() {
		int size = tray.iconSize().height;
		Image image = tomato(size);
		if (image == null) {
			image = tomato(defaultIconSize);
		}
		return image;
	}

	public Image time(Time time) {
		String iconName = iconNameFor(time);
		Cache cache = caches.of(Icons.class);
		if (cache.contains(iconName)) {
			return cache.get(iconName);
		}

		Dimension size = tray.iconSize();
		Colors colors = look.colors();
		Canvas canvas = new Canvas(size);
		canvas.fontSize((float) size.height * 0.58f);
		canvas.paintGradient(colors.background());
		canvas.drawBorder(colors.background().darker().darker().darker());
		canvas.drawCentralizedText(time.shortestString());

		cache.store(canvas.image(), iconName);

		return canvas.image();
	}

	private String iconNameFor(Time time) {
		Font font = Canvas.defaultFont();
		Dimension size = tray.iconSize();
		String colorName = look.colors().getClass().getSimpleName();
		return font.getFontName() + "_" + size.width + "x" + size.height + "_"
				+ colorName + "_" + time.shortestString();
	}

	private Image tomato(int size) {
		return resources.image("/tomato-" + size + ".png");
	}

}
