package org.geogebra.web.full.main.activity;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CommandDispatcher;
import org.geogebra.common.kernel.commands.CommandNotLoadedError;
import org.geogebra.common.kernel.commands.selector.CommandFilterFactory;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.settings.AppConfigCas;
import org.geogebra.web.full.css.MaterialDesignResources;
import org.geogebra.web.full.gui.images.SvgPerspectiveResources;
import org.geogebra.web.full.gui.view.algebra.AlgebraViewW;
import org.geogebra.web.full.gui.view.algebra.MenuActionCollection;
import org.geogebra.web.full.gui.view.algebra.contextmenu.AlgebraMenuItemCollectionCAS;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.resources.SVGResource;

/**
 * Specific behavior for CAS app
 */
public class CASActivity extends BaseActivity {

	/**
	 * Graphing activity
	 */
	public CASActivity() {
		super(new AppConfigCas());
	}

	@Override
	public SVGResource getIcon() {
		return SvgPerspectiveResources.INSTANCE.menu_icon_cas();
	}

	@Override
	public boolean useValidInput() {
		return false;
	}

	@Override
	public MenuActionCollection<GeoElement> getAVMenuItems(AlgebraViewW view) {
		return new AlgebraMenuItemCollectionCAS(view);
	}

	@Override
	public void start(AppW app) {
		Kernel kernel = app.getKernel();
		kernel.getGeoGebraCAS().initCurrentCAS();
		kernel.getAlgebraProcessor()
				.addCommandFilter(CommandFilterFactory.createCasCommandFilter());
		kernel.getParser().setHighPrecisionParsing(true);
		CommandDispatcher dispatcher = kernel.getAlgebraProcessor().getCommandDispatcher();

		tryLoadingScriptingDispatcher(dispatcher);
		tryLoadingAdvancedDispatcher(dispatcher);
		tryLoadingStatsDispatcher(dispatcher);
		tryLoadingProverDispatcher(dispatcher);
		tryLoadingCasDispatcher(dispatcher);
	}

	private void tryLoadingScriptingDispatcher(CommandDispatcher dispatcher) {
		try {
			dispatcher.getScriptingDispatcher();
		} catch (CommandNotLoadedError error) {
			//ignore
		}
	}

	private void tryLoadingAdvancedDispatcher(CommandDispatcher dispatcher) {
		try {
			dispatcher.getAdvancedDispatcher();
		} catch (CommandNotLoadedError e) {
			// ignore
		}
	}

	private void tryLoadingStatsDispatcher(CommandDispatcher dispatcher) {
		try {
			dispatcher.getStatsDispatcher();
		} catch (CommandNotLoadedError e) {
			// ignore
		}
	}

	private void tryLoadingProverDispatcher(CommandDispatcher dispatcher) {
		try {
			dispatcher.getProverDispatcher();
		} catch (CommandNotLoadedError error) {
			//ignore
		}
	}

	private void tryLoadingCasDispatcher(CommandDispatcher dispatcher) {
		try {
			dispatcher.getCASDispatcher();
		} catch (CommandNotLoadedError error) {
			//ignore
		}
	}

	@Override
	public SVGResource getExamIcon() {
		return MaterialDesignResources.INSTANCE.exam_cas();
	}
}
