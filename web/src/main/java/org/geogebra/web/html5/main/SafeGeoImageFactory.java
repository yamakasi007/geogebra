package org.geogebra.web.html5.main;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.geos.GeoImage;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.util.ImageManager;
import org.geogebra.web.html5.safeimage.ImageFile;
import org.geogebra.web.html5.safeimage.SafeImage;
import org.geogebra.web.html5.safeimage.SafeImageProvider;
import org.geogebra.web.html5.util.ImageLoadCallback;
import org.geogebra.web.html5.util.ImageManagerW;
import org.geogebra.web.html5.util.ImageWrapper;

public class SafeGeoImageFactory implements SafeImageProvider, ImageLoadCallback {
	private final ImageManagerW imageManager;
	private final AlgebraProcessor algebraProcessor;
	private AppW app;
	private Construction construction;
	private GeoImage geoImage;
	private ImageFile imageFile;
	private ImageWrapper wrapper;

	public SafeGeoImageFactory(AppW app) {
		this.app = app;
		imageManager = app.getImageManager();
		algebraProcessor = app.getKernel().getAlgebraProcessor();
	}

	public void create(String fileName, String content) {

		ImageFile imageFile = new ImageFile(fileName, content);
		SafeImage safeImage = new SafeImage(imageFile, this);
		safeImage.process();
	}

	@Override
	public void onReady(ImageFile imageFile) {
		this.imageFile = imageFile;
		imageManager.addExternalImage(imageFile.getFileName(),
				imageFile.getContent());
		construction = app.getKernel().getConstruction();
		geoImage = new GeoImage(construction);
		imageManager.triggerSingleImageLoading(imageFile.getFileName(),
				geoImage);
		wrapper = new ImageWrapper(
				imageManager.getExternalImage(imageFile.getFileName(), app, true));
		wrapper.attachNativeLoadHandler(imageManager,this);
	}

	@Override
	public void onLoad() {
		geoImage.setImageFileName(imageFile.getFileName(),
				wrapper.getElement().getWidth(),
				wrapper.getElement().getHeight());

		app.getGuiManager().setImageCornersFromSelection(geoImage);

		if (imageManager.isPreventAuxImage()) {
			geoImage.setAuxiliaryObject(false);
		}
		if (app.isWhiteboardActive()) {
			app.getActiveEuclidianView().getEuclidianController()
					.selectAndShowSelectionUI(geoImage);
		}
		app.setDefaultCursor();
		app.storeUndoInfo();
	}

	private void setManualCorners(String c1, String c2, String c3, String c4) {
		if (c1 != null) {

			GeoPointND corner1 = algebraProcessor
					.evaluateToPoint(c1, null, true);
			geoImage.setCorner(corner1, 0);

			GeoPoint corner2;
			if (c2 != null) {
				corner2 = (GeoPoint) algebraProcessor
						.evaluateToPoint(c2, null, true);
			} else {
				corner2 = new GeoPoint(construction, 0, 0, 1);
				geoImage.calculateCornerPoint(corner2,
						2);
			}
			geoImage.setCorner(corner2, 1);

			// make sure 2nd corner is on screen
			ImageManager.ensure2ndCornerOnScreen(
					corner1.getInhomX(), corner2, app);

			if (c4 != null) {
				GeoPointND corner4 = algebraProcessor
						.evaluateToPoint(c4, null, true);
				geoImage.setCorner(corner4, 2);
			}

		}
	}
}