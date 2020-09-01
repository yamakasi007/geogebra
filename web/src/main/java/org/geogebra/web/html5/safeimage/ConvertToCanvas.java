package org.geogebra.web.html5.safeimage;

import org.geogebra.common.util.FileExtensions;
import org.geogebra.common.util.StringUtil;

import com.google.gwt.canvas.client.Canvas;

import elemental2.dom.HTMLImageElement;

public class ConvertToCanvas implements ImagePreprocessor {
	private final Canvas canvas;

	public ConvertToCanvas() {
		canvas = Canvas.createIfSupported();
	}

	@Override
	public boolean match(FileExtensions extension) {
		return FileExtensions.PNG.equals(extension)
				|| FileExtensions.JPG.equals(extension)
				|| FileExtensions.JPEG.equals(extension)
				|| FileExtensions.BMP.equals(extension);
	}

	@Override
	public void process(final ImageFile imageFile, final SafeImageProvider provider) {
		HTMLImageElement image = new HTMLImageElement();

		image.addEventListener("load", (event) -> {
			drawImageToCanvas(image);
			String fileName = StringUtil.changeFileExtension(imageFile.getFileName(),
					FileExtensions.PNG);

			provider.onReady(new ImageFile(fileName, canvas.toDataUrl()));
		});

		image.src = imageFile.getContent();
	}

	private void drawImageToCanvas(HTMLImageElement image)  {
		canvas.setCoordinateSpaceWidth(image.width);
		canvas.setCoordinateSpaceHeight(image.height);
		canvas.getContext2d().drawImage(image, 0, 0);
	}
}
