package org.geogebra.web.html5.util.pdf;

import org.geogebra.common.util.ExternalAccess;
import org.geogebra.common.util.debug.Log;

import com.google.gwt.canvas.client.Canvas;

import elemental2.dom.File;
import elemental2.dom.FileReader;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

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
		PdfDocumentLoadingTask task = PdfJsLib.get().getDocument(src);

		task.promise.then(document -> {
			Log.debug("E2: PDF Loaded. numPages: " + document.numPages);
			setDocument(document);
			setPageCount(document.numPages);
			finishLoading(true);
			return document.getPage(pageNumber);
		}).then(p -> {
			Log.debug("PAGE LOADED");
			JsPropertyMap<Object> viewportOptions = JsPropertyMap.of();
			viewportOptions.set("scale", 1);
			PageViewPort viewport = p.getViewport(viewportOptions);
			Canvas canvas = Canvas.createIfSupported();
			canvas.setCoordinateSpaceWidth(viewport.width);
			canvas.setCoordinateSpaceHeight(viewport.height);
			JsPropertyMap<Object> rendererContext = JsPropertyMap.of();
			rendererContext.set("canvasContext", canvas.getContext2d());
			rendererContext.set("viewport", viewport);
			p.render(rendererContext);
			return null;
		}).catch_(Promise::reject);
	}

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
			return true;
		}
		return false;
		}
	}