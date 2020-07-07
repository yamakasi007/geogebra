package org.geogebra.web.html5.util.pdf;

import org.geogebra.common.util.ExternalAccess;
import org.geogebra.common.util.debug.Log;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

import elemental2.dom.File;
import elemental2.dom.FileReader;
import elemental2.promise.Promise;
import jsinterop.base.Js;

/**
 * Wrapper class for pdf.js
 * 
 * @author laszlo
 *
 */
public class PDFWrapper {

	private PDFListener listener;
	private int pageCount;
	private int pageNumber = 1;
	private PDFDocumentProxy document;

	/**
	 * Interface to communicate with PDF Container.
	 *
	 */
	public interface PDFListener {
		/**
		 * Call this to build image from pdf.
		 * 
		 * @param imgSrc
		 *            the image data as source.
		 */
		void onPageDisplay(String imgSrc);

		/**
		 * After the pdf loaded, the progress bar should be finished quickly.
		 * 
		 * @param result
		 *            true if the loading of the pdf was successful
		 */
		void finishLoading(boolean result);

		/**
		 * Sets the value of the progress bar for the given percent.
		 * 
		 * @param percent
		 *            the new value of the progress bar
		 */
		void setProgressBarPercent(double percent);

	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            PDF to handle.
	 * @param listener
	 *            to communicate with PDF container.
	 */
	public PDFWrapper(File file, PDFListener listener) {
		this.listener = listener;
		read(file);
	}

	@ExternalAccess
	private void finishLoading(boolean result) {
		listener.finishLoading(result);
	}
	@ExternalAccess
	private void setProgressBarPercent(double percent) {
		listener.setProgressBarPercent(percent);
	}

	private void read(File file) {
		FileReader reader = new FileReader();
		reader.onprogress = event -> {
			if (event.lengthComputable) {
				double percent = (event.loaded / event.total) * 100;
				setProgressBarPercent(percent);
			}
			return null;
		};
		reader.addEventListener("load", evt -> {
			loadElemental2(reader.result.asString());
		});

		if (Js.isTruthy(file)) {
			reader.readAsDataURL(file);
		}
	}

	private void loadElemental2(String src) {
		PdfDocumentLoadingTask task = PdfJs.getDocument(src);

		task.then(document -> {
			Log.debug("E2: PDF Loaded");
			setDocument(document);
			setPageCount(document.pdfInfo.getNumPages());
	 		finishLoading(true);
		}).catch_(Promise::reject);
	}

	@ExternalAccess
	private native void load(String src) /*-{
		var loadingTask = $wnd.PDFJS.getDocument(src);
		var that = this;

		loadingTask.promise
				.then(
						function(pdf) {
							@org.geogebra.common.util.debug.Log::debug(Ljava/lang/Object;)('PDF loaded');
//							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setDocument(Lcom/google/gwt/core/client/JavaScriptObject;)(pdf);
//							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setPageCount(I)(pdf.numPages);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(true);
						},
						function(reason) {
							// PDF loading error
							@org.geogebra.common.util.debug.Log::error(Ljava/lang/String;)(reason);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(false);
						});
	}-*/;

	private native void renderPage() /*-{
		var that = this;
		var pdf = this.@org.geogebra.web.html5.util.pdf.PDFWrapper::document;
		var pageNumber = this.@org.geogebra.web.html5.util.pdf.PDFWrapper::pageNumber;
		var svgCallback = function(svg) {
			svgs = (new XMLSerializer()).serializeToString(svg);
			// convert to base64 URL for <img>
			var callback = function(svg) {

				var data = "data:image/svg+xml;base64,"
						+ btoa(unescape(encodeURIComponent(svg)));
				that.@org.geogebra.web.html5.util.pdf.PDFWrapper::onPageDisplay(Ljava/lang/String;)(data);
				// convert to base64 URL for <img>
			}

			svgs = that.@org.geogebra.web.html5.util.pdf.PDFWrapper::convertBlobs(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(svgs, callback);

		};
		pdf
				.getPage(pageNumber)
				.then(
						function(page) {
							@org.geogebra.common.util.debug.Log::debug(Ljava/lang/Object;)('Page loaded');

							var scale = 1;
							var viewport = page.getViewport(scale);

							return page
									.getOperatorList()
									.then(
											function(opList) {
												var svgGfx = new $wnd.PDFJS.SVGGraphics(
														page.commonObjs,
														page.objs);
												return svgGfx.getSVG(opList,
														viewport).then(
														svgCallback);
											});
						});
	}-*/;

	@ExternalAccess
	private void onPageDisplay(String src) {
		if (listener == null) {
			return;
		}
		listener.onPageDisplay(src);
	}

	/**
	 * 
	 * @return the number of pages in the PDF.
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 
	 * @param pageCount
	 *            to set.
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 
	 * @return PDF as PDFDocumentProxy
	 */
	public PDFDocumentProxy getPdf() {
		return document;
	}

	/**
	 * sets PDF as PDFDocumentProxy
	 *
	 * @param document
	 *            the JavaScriptObject to set.
	 */
	public void setDocument(PDFDocumentProxy document) {
		this.document = document;
	}

	/**
	 * load previous page of the PDF if any.
	 */
	public void previousPage() {
		if (pageNumber > 1) {
			setPageNumber(pageNumber - 1);
		}
	}

	/**
	 * load next page of the PDF if any.
	 */
	public void nextPage() {
		if (pageNumber < pageCount) {
			setPageNumber(pageNumber + 1);
		}
	}

	/**
	 * 
	 * @return the current page index.
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 
	 * @param num
	 *            page number to set.
	 * @return if page change was successful.
	 */
	public boolean setPageNumber(int num) {
		if (num > 0 && num <= pageCount) {
			pageNumber = num;
			renderPage();
			return true;
		}
		return false;
	}

	private void renderPageElemental2() {

	}

	// convert something like
	// xlink:href="blob:http://www.example.org/d3872604-2efe-4e3f-94d9-d449d966c20f"
	// to base64 PNG
	@ExternalAccess
	private native void convertBlobs(JavaScriptObject svg,
			JavaScriptObject callback) /*-{

		if (svg.indexOf('xlink:href="blob:') > 0) {

			var index = svg.indexOf('xlink:href="blob:');
			var index2 = svg.indexOf('"', index + 17);
			var blobURI = svg.substr(index + 12, index2 - (index + 12));
			svg = svg
					.replace(
							blobURI,
							this.@org.geogebra.web.html5.util.pdf.PDFWrapper::blobToBase64(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(blobURI, svg, callback));
		} else {
			callback(svg);
		}
	}-*/;

	@ExternalAccess
	private native void blobToBase64(String blobURI, JavaScriptObject svg,
			JavaScriptObject callback) /*-{

		var img = $doc.createElement("img");
		var canvas = $doc.createElement("canvas");
		var that = this;

		// eg img.src = "blob:http://www.example.org/d3872604-2efe-4e3f-94d9-d449d966c20f";
		img.src = blobURI;
		img.onload = function(a) {
			var h = a.target.height;
			var w = a.target.width;
			var c = canvas.getContext('2d');
			canvas.width = w;
			canvas.height = h;

			c.drawImage(img, 0, 0);
			svg = svg.replace(blobURI, canvas.toDataURL());

			// convert next blob (or finish)
			that.@org.geogebra.web.html5.util.pdf.PDFWrapper::convertBlobs(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(svg, callback);
		}
	}-*/;

	private void blobToBase64(String blobUri, String svg0) {
		final Image image = new Image();
		image.setUrl(blobUri);
		Canvas canvas = Canvas.createIfSupported();
		image.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				canvas.setCoordinateSpaceHeight(image.getHeight());
				canvas.setCoordinateSpaceWidth(image.getWidth());
				canvas.getContext2d().drawImage(ImageElement.as(image.getElement()), 0, 0);
				String svg = svg0.replace(blobUri, canvas.toDataUrl());
				convertBlobs(svg, () -> PDFWrapper.this.onConvertBlobs(svg));
			}

		});
	};

	private void convertBlobs(String svg, Runnable runnable) {

		if (svg.indexOf("xlink:href=\"blob:") > 0) {

			int index = svg.indexOf("xlink:href=\"blob:");
			int index2 = svg.indexOf('"', index + 17);
			String blobURI = svg.substring(index + 12, index2 - (index + 12));
//			svg = svg
//					.replace(blobURI, blobToBase64(blobURI, svg));
		} else {
			onConvertBlobs(svg);
		}
	}

	private void onConvertBlobs(String svg) {

	}
	}