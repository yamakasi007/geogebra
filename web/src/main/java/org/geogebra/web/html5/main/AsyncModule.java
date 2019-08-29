package org.geogebra.web.html5.main;

import org.geogebra.common.geogebra3D.kernel3D.commands.CommandDispatcher3D;
import org.geogebra.common.kernel.commands.CommandDispatcherAdvanced;
import org.geogebra.common.kernel.commands.CommandDispatcherCAS;
import org.geogebra.common.kernel.commands.CommandDispatcherDiscrete;
import org.geogebra.common.kernel.commands.CommandDispatcherScripting;
import org.geogebra.common.kernel.commands.CommandDispatcherStats;
import org.geogebra.common.kernel.commands.CommandDispatcherSteps;
import org.geogebra.common.util.Prover;

import com.google.gwt.core.client.prefetch.RunAsyncCode;
import com.google.gwt.user.client.Window.Location;

/**
 * Enumeration of all modules (fragments) that can be prefetched
 */
public enum AsyncModule {
	STATS(RunAsyncCode.runAsyncCode(CommandDispatcherStats.class)),

	ADVANCED(RunAsyncCode.runAsyncCode(CommandDispatcherAdvanced.class)),

	CAS(RunAsyncCode.runAsyncCode(CommandDispatcherCAS.class)),

	SPATIAL(RunAsyncCode.runAsyncCode(CommandDispatcher3D.class)),

	PROVER(RunAsyncCode.runAsyncCode(Prover.class)),

	SCRIPTING(RunAsyncCode.runAsyncCode(CommandDispatcherScripting.class)),

	DISCRETE(RunAsyncCode.runAsyncCode(CommandDispatcherDiscrete.class)),

	STEPS(RunAsyncCode.runAsyncCode(CommandDispatcherSteps.class));

	private RunAsyncCode asyncCode;

	private AsyncModule(RunAsyncCode splitPoint) {
		this.asyncCode = splitPoint;
	}

	/**
	 * Prefetch the module so that actual fetch can load it from memory
	 */
	public void prefetch() {
		if (!asyncCode.isLoaded()
				&& Location.getProtocol().startsWith("http")) {
			FragmentPrefetcher.prefetch(asyncCode.getSplitPoint());
		}
	}

}