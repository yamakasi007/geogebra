package org.geogebra.common.plugin;

import java.util.ArrayList;

import org.geogebra.common.kernel.geos.GeoElement;

/**
 * Event to be handled by EventListener
 * 
 * @author Arnaud
 */
public class Event {
	/** event type */
	public final EventType type;
	/** primary target */
	public final GeoElement target;
	/** generic argument, e.g. macro name */
	public final String argument;
	/** secondary target */
	public final ArrayList<GeoElement> targets;
	/** details */
	public final String details;
	private boolean alwaysDispatched;

	public Event(EventType type) {
		this(type, null);
	}

	/**
	 * @param type
	 *            type
	 * @param target
	 *            target
	 */
	public Event(EventType type, GeoElement target) {
		this(type, target, target == null ? null : target.getLabelSimple());
	}

	/**
	 * @param type
	 *            event type
	 * @param target
	 *            target
	 * @param argument
	 *            extra info
	 */
	public Event(EventType type, GeoElement target, String argument) {
		this( type, target, argument, null);
	}

	/**
	 * @param type
	 *            event type
	 * @param target
	 *            target
	 * @param argument
	 *            extra info
	 * @param targets
	 *            extra targets
	 */
	public Event(
			EventType type, GeoElement target, String argument, ArrayList<GeoElement> targets) {
		this(type, target, argument, targets, null);
	}

	public Event(
			EventType type,
			GeoElement target,
			String argument,
			ArrayList<GeoElement> targets,
			String details) {
		this.type = type;
		this.target = target;
		this.argument = argument;
		this.targets = targets;
		this.details = details;
	}

	/**
	 * @param type
	 *            event type
	 * @param target
	 *            target
	 * @param alwaysDispatch
	 *            whether to override scripting block
	 */
	public Event(EventType type, GeoElement target,
			boolean alwaysDispatch) {
		this(type, target);
		this.alwaysDispatched = alwaysDispatch;
	}

	/**
	 * @return whether to override blocked scripting
	 */
	boolean isAlwaysDispatched() {
		return this.alwaysDispatched;
	}

	/**
	 * @return event type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * 
	 * @return primary target
	 */
	public GeoElement getTarget() {
		return target;
	}

	/**
	 * 
	 * @return secondary target
	 */
	public ArrayList<GeoElement> getTargets() {
		return targets;
	}

	/**
	 * 
	 * @return argument
	 */
	public String getArgument() {
		return argument;
	}
}
