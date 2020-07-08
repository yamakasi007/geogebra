package org.geogebra.web.html5.util.pdf;

import org.geogebra.common.util.ExternalAccess;

import elemental2.dom.DomGlobal;
import elemental2.dom.File;
import elemental2.dom.FileReader;
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
		PdfDocumentLoadingTask task = PdfJsLib.get().getDocument(src);

		DomGlobal.console.log(task.getPromise());

		task.getPromise().then(document -> {
			DomGlobal.console.log("E2: PDF Loaded. numPages: ");
			return null;
		});
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