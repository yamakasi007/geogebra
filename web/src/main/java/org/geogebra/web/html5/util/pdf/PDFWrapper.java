package org.geogebra.web.html5.util.pdf;

import org.geogebra.common.util.ExternalAccess;

import com.google.gwt.core.client.JavaScriptObject;

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
	private JavaScriptObject pdf = null;

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
	public PDFWrapper(JavaScriptObject file, PDFListener listener) {
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

	private native void read(JavaScriptObject file) /*-{
		var reader = new FileReader();
		var that = this;

		reader.onprogress = function(event) {
			if (event.lengthComputable) {
				var percent = (event.loaded / event.total) * 100;
				that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setProgressBarPercent(D)(percent);
			}
		};

		reader
				.addEventListener(
						"load",
						function() {
							var src = reader.result;
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::load(Ljava/lang/String;)(src);
						}, false);

		if (file) {
			reader.readAsDataURL(file);
		}

	}-*/;

	@ExternalAccess
	private native void load(String src) /*-{
		var loadingTask = $wnd.PDFJS.getDocument(src);
		var that = this;

		loadingTask.promise
				.then(
						function(pdf) {
							@org.geogebra.common.util.debug.Log::debug(Ljava/lang/Object;)('PDF loaded');
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setPdf(Lcom/google/gwt/core/client/JavaScriptObject;)(pdf);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setPageCount(I)(pdf.numPages);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(true);
						},
						function(reason) {
							// PDF loading error
							@org.geogebra.common.util.debug.Log::error(Ljava/lang/String;)(reason);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(false);
						});
	}-*/;

	private native void renderPage() /*-{
	  // Fetch the first page
	  var pageNumber = 1;
	  var that = this;
	  var pdf = this.@org.geogebra.web.html5.util.pdf.PDFWrapper::pdf;
	  pdf.getPage(pageNumber).then(function(page) {
		var scale = 1.5;
		var viewport = page.getViewport({scale: scale});
		// Prepare canvas using PDF page dimensions
		var canvas = document.createElement('canvas');
		document.body.appendChild(canvas);
	  	canvas.height = viewport.height;
		canvas.width = viewport.width;
		// Render PDF page into canvas context
		var context = canvas.getContext('2d');
		var renderContext = {
		  canvasContext: context,
		  viewport: viewport
		};

		var renderTask = page.render(renderContext);
		   renderTask.promise.then(function () {
			  $wnd.console.log('Page rendered: URL is ' + canvas.toDataURL());
			that.@org.geogebra.web.html5.util.pdf.PDFWrapper::onPageDisplay(Ljava/lang/String;)(canvas.toDataURL());
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
	 * @return PDF as JavaScriptObject
	 */
	public JavaScriptObject getPdf() {
		return pdf;
	}

	/**
	 * sets PDF as JavaScriptObject
	 * 
	 * @param pdf
	 *            the JavaScriptObject to set.
	 */
	public void setPdf(JavaScriptObject pdf) {
		this.pdf = pdf;
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
//			svg = svg
//					.replace(
//							blobURI,
//							this.@org.geogebra.web.html5.util.pdf.PDFWrapper::blobToBase64(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(blobURI, svg, callback));
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
//			svg = svg.replace(blobURI, canvas.toDataURL());

			// convert next blob (or finish)
			that.@org.geogebra.web.html5.util.pdf.PDFWrapper::convertBlobs(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(svg, callback);
		}
	}-*/;
}