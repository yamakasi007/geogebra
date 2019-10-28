package org.geogebra.web.test;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.CommandDispatcherAdvanced;
import org.geogebra.common.kernel.commands.CommandDispatcherCAS;
import org.geogebra.common.kernel.commands.CommandDispatcherDiscrete;
import org.geogebra.common.kernel.commands.CommandDispatcherInterface;
import org.geogebra.common.kernel.commands.CommandDispatcherProver;
import org.geogebra.common.kernel.commands.CommandDispatcherScripting;
import org.geogebra.common.kernel.commands.CommandDispatcherStats;
import org.geogebra.common.kernel.commands.CommandDispatcherSteps;
import org.geogebra.web.html5.gui.GeoGebraFrameW;
import org.geogebra.web.html5.kernel.commands.CommandDispatcherW;
import org.geogebra.web.html5.main.AppWsimple;
import org.geogebra.web.html5.util.ArticleElementInterface;
import org.geogebra.web.html5.util.ViewW;

/**
 * Like production, mock async loading of commands
 *
 */
public class AppWSimpleMock extends AppWsimple {

	private ViewW view;


	/**
	 * @param article
	 *            article element
	 * @param frame
	 * 			  GeoGebraFrame
	 * @param undoActive
	 *            if true you can undo by CTRL+Z and redo by CTRL+Y
	 */
	public AppWSimpleMock(ArticleElementInterface article, GeoGebraFrameW frame, boolean undoActive) {
		super(article, frame, undoActive);
	}

	@Override
	public ViewW getViewW() {
		if (view == null) {
			view = new ViewWMock(this);
		}
		return view;
	}

	@Override
	public CommandDispatcherW newCommandDispatcher(Kernel cmdKernel) {
		return new CommandDispatcherW(cmdKernel) {

			@Override
			public CommandDispatcherInterface getStatsDispatcher() {
				return new CommandDispatcherStats();
			}

			@Override
			public CommandDispatcherInterface getDiscreteDispatcher() {
				return new CommandDispatcherDiscrete();
			}

			@Override
			public CommandDispatcherInterface getCASDispatcher() {
				return new CommandDispatcherCAS();
			}

			@Override
			public CommandDispatcherInterface getScriptingDispatcher() {
				return new CommandDispatcherScripting();
			}

			@Override
			public CommandDispatcherInterface getAdvancedDispatcher() {
				return new CommandDispatcherAdvanced();
			}

			@Override
			public CommandDispatcherInterface getStepsDispatcher() {
				return new CommandDispatcherSteps();
			}

			@Override
			public CommandDispatcherInterface getProverDispatcher() {
				return new CommandDispatcherProver();
			}
		};


	}
}
