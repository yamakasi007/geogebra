package org.geogebra.common.geogebra3D.euclidian3D.draw;

import java.util.ArrayList;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.euclidian.EuclidianController;
import org.geogebra.common.euclidian.plot.CurvePlotter;
import org.geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import org.geogebra.common.geogebra3D.euclidian3D.Hitting;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterSurface;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoCurveCartesian3D;
import org.geogebra.common.geogebra3D.kernel3D.geos.GeoSurfaceCartesian3D;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.arithmetic.Function;
import org.geogebra.common.kernel.arithmetic.FunctionNVar;
import org.geogebra.common.kernel.arithmetic.FunctionVariable;
import org.geogebra.common.kernel.arithmetic.MyDouble;
import org.geogebra.common.kernel.arithmetic.Traversing.VariableReplacer;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunctionNVar;
import org.geogebra.common.kernel.kernelND.SurfaceEvaluable;
import org.geogebra.common.kernel.kernelND.SurfaceEvaluable.LevelOfDetail;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.kernel.matrix.Coords3;
import org.geogebra.common.plugin.EuclidianStyleConstants;
import org.geogebra.common.util.DoubleUtil;
import org.geogebra.common.util.debug.Log;

/**
 * Class for drawing a 2-var function
 * 
 * @author mathieu
 * 
 */
public class DrawSurface3D extends Drawable3DSurfaces implements HasZPick {

	final static private boolean DEBUG = false;

	/** The function being rendered */
	private SurfaceEvaluable surfaceGeo;

	// number of intervals in root mesh (for each parameters, if parameters
	// delta are equals)
	private static final short ROOT_MESH_INTERVALS_SPEED = 10;
	private static final short ROOT_MESH_INTERVALS_SPEED_SQUARE = ROOT_MESH_INTERVALS_SPEED
			* ROOT_MESH_INTERVALS_SPEED;

	// max split array size ( size +=4 for one last split)
	private static final int MAX_SPLIT_SPEED = 4096;
	private static final int MAX_SPLIT_QUALITY = MAX_SPLIT_SPEED * 2;

	private static final int MAX_SPLIT_IN_ONE_UPDATE_SPEED = 512;
	private static final int MAX_SPLIT_IN_ONE_UPDATE_QUALITY = MAX_SPLIT_IN_ONE_UPDATE_SPEED
			* 2;
	final private static int HIT_SAMPLES = 10;
	final private static double DELTA_SAMPLES = 1.0 / HIT_SAMPLES;

	private SurfaceEvaluable.LevelOfDetail levelOfDetail;

	private int maxSplit;

	// draw array size ( size +=1 for one last draw)
	private int maxDraw;

	CornerBuilder cornerBuilder;
	/**
	 * max splits in one update loop
	 */
	private int maxSplitsInOneUpdate;

	private Corner[] currentSplit;
	private Corner[] nextSplit;

	/**
	 * list of things to draw
	 */
	CornerAndCenter[] drawList;

	private int currentSplitIndex;
	private int nextSplitIndex;
	private int currentSplitStoppedIndex;
	int loopSplitIndex;
	int drawListIndex;
	private boolean drawFromScratch = true;
	private boolean drawUpToDate = false;
	// previously set thickness (-1 means needs update)
	private int oldThickness = -1;

	private DrawWireframe drawWireframe;

	/** Current culling box - set to view3d.(x|y|z)(max|min) */
	private double[] cullingBox = new double[6];

	private Coords3 evaluatedPoint = CornerBuilder.newCoords3();
	private Coords3 evaluatedNormal = CornerBuilder.newCoords3();
	/**
	 * used to draw "still to split" corners
	 */
	Corner[] cornerForStillToSplit;
	Corner[] cornerToDrawStillToSplit;

	/**
	 * max distance in real world from view
	 */
	private double maxRWPixelDistance;
	/**
	 * max distance in real world for splitting
	 */
	private double maxRWDistance;
	/**
	 * max distance in real world under which we don't check angles
	 */
	double maxRWDistanceNoAngleCheck;
	double maxBend;
	int notDrawn;

	private boolean splitsStartedNotFinished;
	private boolean stillRoomLeft;

	private Coords boundsMin = new Coords(3);
	private Coords boundsMax = new Coords(3);

	private CurveHitting curveHitting;

	private ArrayList<GeoCurveCartesian3D> borders = new ArrayList<>();

	private SurfaceParameter uParam = new SurfaceParameter();
	private SurfaceParameter vParam = new SurfaceParameter();

	/**
	 * common constructor
	 * 
	 * @param a_view3d
	 *            view
	 * @param surface
	 *            surface
	 */
	public DrawSurface3D(EuclidianView3D a_view3d, SurfaceEvaluable surface) {
		super(a_view3d, (GeoElement) surface);
		this.surfaceGeo = surface;
		cornerBuilder = new CornerBuilder(this);

		levelOfDetail = null;

		cornerForStillToSplit = new Corner[6];
		cornerToDrawStillToSplit = new Corner[12];
		for (int i = 0; i < 12; i++) {
			cornerToDrawStillToSplit[i] = new Corner(-1, this, cornerBuilder);
		}

		splitsStartedNotFinished = false;

		drawWireframe = new DrawWireframe(cornerBuilder, uParam, vParam);
	}

	private void setLevelOfDetail() {

		SurfaceEvaluable.LevelOfDetail lod = surfaceGeo.getLevelOfDetail();

		if (levelOfDetail == lod) {
			return;
		}

		levelOfDetail = lod;

		// set sizes
		switch (levelOfDetail) {
		case SPEED:
			maxSplit = MAX_SPLIT_SPEED;
			maxSplitsInOneUpdate = MAX_SPLIT_IN_ONE_UPDATE_SPEED;
			break;
		case QUALITY:
			maxSplit = MAX_SPLIT_QUALITY;
			maxSplitsInOneUpdate = MAX_SPLIT_IN_ONE_UPDATE_QUALITY;
			break;
		}

		maxDraw = maxSplit;
		cornerBuilder.setCornerListSize(maxDraw * 3);

		// create arrays
		currentSplit = new Corner[maxSplit + 4];
		nextSplit = new Corner[maxSplit + 4];
		drawList = new CornerAndCenter[maxDraw + 100];
		cornerBuilder.setCornerArray(new Corner[cornerBuilder.getCornerListSize()]);
	}

	private void setTolerances() {
		maxRWPixelDistance = CurvePlotter.MAX_PIXEL_DISTANCE;

		// set sizes
		switch (levelOfDetail) {
		case SPEED:
			maxRWDistanceNoAngleCheck = 1 * maxRWPixelDistance;
			maxRWDistance = 5 * maxRWPixelDistance;
			maxBend = getView3D().getMaxBendSpeedSurface();
			break;
		case QUALITY:
			maxRWDistanceNoAngleCheck = 1 * maxRWPixelDistance;
			maxRWDistance = 2 * maxRWPixelDistance;
			maxBend = CurvePlotter.MAX_BEND;
			break;
		}

	}

	/**
	 * console debug
	 * 
	 * @param s
	 *            message
	 */
	static protected void debug(String s) {
		if (DEBUG) {
			Log.debug(s);
		}
	}

	@Override
	public void drawGeometry(Renderer renderer) {
		renderer.getRendererImpl().setLayer(getLayer());
		renderer.getGeometryManager().draw(getSurfaceIndex());
		renderer.getRendererImpl().setLayer(Renderer.LAYER_DEFAULT);
	}

	@Override
	protected void drawSurfaceGeometry(Renderer renderer) {
		drawGeometry(renderer);
	}

	@Override
	void drawGeometryHiding(Renderer renderer) {
		drawSurfaceGeometry(renderer);
	}

	@Override
	public void drawGeometryHidden(Renderer renderer) {

		if (wireframeNeeded()) {
			if (!isVisible()) {
				return;
			}

			if (drawWireframe.isWireframeHidden()) {
				return;
			}

			if (getGeoElement()
					.getLineTypeHidden() == EuclidianStyleConstants.LINE_TYPE_HIDDEN_NONE) {
				return;
			}

			setDrawingColor(GColor.DARK_GRAY);
			setLineTextureHidden(renderer);

			renderer.getGeometryManager().draw(getGeometryIndex());
		}

	}

	@Override
	public void drawOutline(Renderer renderer) {
		if (wireframeNeeded()) {
			if (!isVisible()) {
				return;
			}

			if (drawWireframe.isWireframeHidden()) {
				return;
			}

			setDrawingColor(GColor.DARK_GRAY);
			renderer.getTextures()
					.setDashFromLineType(getGeoElement().getLineType());

			renderer.getGeometryManager().draw(getGeometryIndex());
		}
	}

	@Override
	protected boolean updateForItSelf() {
		if (!surfaceGeo.isDefined()) {
			return false;
		}

		if (((GeoElement) surfaceGeo).isGeoFunctionNVar()) {
			if (((GeoFunctionNVar) surfaceGeo).getVarNumber() != 2) {
				setSurfaceIndex(-1);
				setGeometryIndex(-1);
				return true;
			}
		}

		boolean drawOccurred = false;

		if (drawFromScratch) {
			borders.clear();
			drawUpToDate = false;

			if (levelOfDetail == LevelOfDetail.QUALITY
					&& splitsStartedNotFinished) {
				draw();
				drawOccurred = true;
			}

			// maybe set to null after redefine
			surfaceGeo.setDerivatives();

			// calc min/max values
			uParam.initBorder(surfaceGeo, getView3D(), 0);
			vParam.initBorder(surfaceGeo, getView3D(), 1);

			if (DoubleUtil.isZero(uParam.delta)
					|| DoubleUtil.isZero(vParam.delta)) {
				setSurfaceIndex(-1);
				setWireframeInvisible();
				return true;
			}

			// max values
			setLevelOfDetail();
			setTolerances();

			updateCullingBox();

			initBounds();

			debug("\nmax distances = " + maxRWDistance + ", "
					+ maxRWDistanceNoAngleCheck);

			// create root mesh
			double uOverVFactor = uParam.delta / vParam.delta;
			if (uOverVFactor > ROOT_MESH_INTERVALS_SPEED) {
				uOverVFactor = ROOT_MESH_INTERVALS_SPEED;
			} else if (uOverVFactor < 1.0 / ROOT_MESH_INTERVALS_SPEED) {
				uOverVFactor = 1.0 / ROOT_MESH_INTERVALS_SPEED;
			}
			uParam.n = (int) (ROOT_MESH_INTERVALS_SPEED * uOverVFactor);
			vParam.n = ROOT_MESH_INTERVALS_SPEED_SQUARE / uParam.n;
			uParam.n += 2;
			vParam.n += 2;

			uParam.init(levelOfDetail);
			vParam.init(levelOfDetail);

			debug("grids: " + uParam.n + ", " + vParam.n);
			cornerBuilder.resetCornerListIndex();

			try {
				/*
				  first corner from root mesh
				 */
				Corner firstCorner = drawWireframe.createRootMesh(wireframeNeeded());

				// split root mesh as start
				currentSplitIndex = 0;
				currentSplitStoppedIndex = 0;
				nextSplitIndex = 0;
				drawListIndex = 0;
				notDrawn = 0;
				DrawWireframe.splitRootMesh(firstCorner);
				debug("\nnot drawn after split root mesh: " + notDrawn);

				// now split root mesh is ready
				drawFromScratch = false;
			} catch (Corner.NotEnoughCornersException e) {
				caught(e);
			}
		}

		if (wireframeNeeded()) {
			if (drawUpToDate) {
				// update is called for visual style, i.e. line thickness
				drawWireframe(getView3D().getRenderer());
				return true;
			}
		}

		// start recursive split
		loopSplitIndex = 0;
		// long time = System.currentTimeMillis();
		try {
			stillRoomLeft = split();
		} catch (Corner.NotEnoughCornersException e) {
			caught(e);
		}

		// time = System.currentTimeMillis() - time;
		// if (time > 0){
		// debug("split : "+time);
		// }

		debug("\ndraw size : " + drawListIndex + "\nnot drawn : " + notDrawn
				+ "\nstill to split : "
				+ (currentSplitIndex - currentSplitStoppedIndex)
				+ "\nnext to split : " + nextSplitIndex
				+ "\ncorner list size : " + cornerBuilder.getCornerListIndex()
				+ "\nstill room left : " + stillRoomLeft);

		splitsStartedNotFinished = (currentSplitIndex
				- currentSplitStoppedIndex) + nextSplitIndex > 0;

		// time = System.currentTimeMillis();

		// set old thickness to force wireframe update
		oldThickness = -1;

		switch (levelOfDetail) {
		case SPEED:
		default:
			draw();
			// still room left and still split to do: still to update
			drawUpToDate = !splitsStartedNotFinished || !stillRoomLeft;
			return drawUpToDate;
		case QUALITY:
			splitsStartedNotFinished = splitsStartedNotFinished
					&& stillRoomLeft;
			if (!splitsStartedNotFinished) {
				if (!drawOccurred) {
					// no draw at start: can do the draw now
					draw();
					drawUpToDate = true;
					return true;
				}
				// no room left or no split to do: update is finished, but
				// the
				// object may change
				return false;
			}
			// still room left and still split to do: still to update
			return false;
		}

		// time = System.currentTimeMillis() - time;
		// if (time > 0){
		// debug("draw : "+time);
		// }

	}

	/**
	 * ends geometry
	 * 
	 * @param surface
	 *            surface plotter
	 * 
	 */
	static private void endGeometry(PlotterSurface surface) {
		surface.endGeometryDirect();
	}

	/**
	 * draw all corners and centers
	 * 
	 * @param surface
	 *            surface plotter
	 * 
	 */
	protected void drawCornersAndCenters(PlotterSurface surface) {
		// used with GL.drawElements()
	}

	private void startTriangles(PlotterSurface surface) {
		surface.startTriangles(cornerBuilder.getCornerListIndex() * 16);
	}

	private void draw() {

		Renderer renderer = getView3D().getRenderer();

		// point were already scaled
		renderer.getGeometryManager().setScalerIdentity();

		// draw split, still to split, and next to split
		PlotterSurface surface = renderer.getGeometryManager().getSurface();
		setPackSurface(true);
		surface.start(getReusableSurfaceIndex());

		if (!stillRoomLeft) {
			try {
				for (int i = currentSplitStoppedIndex; i < currentSplitIndex; i++) {
					currentSplit[i].split(true);
				}
				for (int i = 0; i < nextSplitIndex; i++) {
					nextSplit[i].split(true);
				}
			} catch (Corner.NotEnoughCornersException e) {
				caught(e);
			}
			debug("\n--- draw size : " + drawListIndex);
			if (drawListIndex > 0) {
				startTriangles(surface);
				for (int i = 0; i < drawListIndex; i++) {
					drawList[i].draw(surface);
				}

				drawCornersAndCenters(surface);
				endGeometry(surface);

			}

		} else {
			if (drawListIndex > 0 || splitsStartedNotFinished) {
				startTriangles(surface);
				for (int i = 0; i < drawListIndex; i++) {
					drawList[i].draw(surface);
				}

				drawCornersAndCenters(surface);

				for (int i = currentSplitStoppedIndex; i < currentSplitIndex; i++) {
					currentSplit[i].drawAsStillToSplit(surface);
				}
				for (int i = 0; i < nextSplitIndex; i++) {
					nextSplit[i].drawAsStillToSplit(surface);
				}

				endGeometry(surface);
			}
		}

		setSurfaceIndex(surface.end());
		endPacking();
		renderer.getGeometryManager().setScalerView();

		drawWireframe(renderer);
	}

	private void setWireframeInvisible() {
		drawWireframe.setWireframeInvisible();
		if (shouldBePackedForManager()) {
			setGeometryIndexNotVisible();
		}
	}

	private void drawWireframe(Renderer renderer) {

		if (!wireframeNeeded()) {
			return;
		}

		drawWireframe.drawWireframe(this, renderer, oldThickness);
	}

	@Override
	protected void updateForView() {
		if (getView3D().viewChangedByZoom()
				|| getView3D().viewChangedByTranslate()) {
			setWaitForUpdate();
		}
	}

	@Override
	public void setWaitForUpdate() {
		drawFromScratch = true;
		super.setWaitForUpdate();
	}

	@Override
	public int getPickOrder() {
		return DRAW_PICK_ORDER_SURFACE;
	}

	private static boolean wireframeNeeded() {
		return true;
	}

	@Override
	public void addToDrawable3DLists(Drawable3DLists lists) {
		addToDrawable3DLists(lists, DRAW_TYPE_CLIPPED_SURFACES);
		if (wireframeNeeded()) {
			addToDrawable3DLists(lists, DRAW_TYPE_CLIPPED_CURVES);
		}
	}

	@Override
	public void removeFromDrawable3DLists(Drawable3DLists lists) {
		removeFromDrawable3DLists(lists, DRAW_TYPE_CLIPPED_SURFACES);
		if (wireframeNeeded()) {
			removeFromDrawable3DLists(lists, DRAW_TYPE_CLIPPED_CURVES);
		}
	}

	private void updateCullingBox() {
		EuclidianView3D view = getView3D();
		double off = maxRWPixelDistance * 4;
		double offScaled = off / getView3D().getXscale();
		cullingBox[0] = view.getXmin() - offScaled;
		cullingBox[1] = view.getXmax() + offScaled;
		offScaled = off / getView3D().getYscale();
		cullingBox[2] = view.getYmin() - offScaled;
		cullingBox[3] = view.getYmax() + offScaled;
		offScaled = off / getView3D().getZscale();
		cullingBox[4] = view.getZmin() - offScaled;
		cullingBox[5] = view.getZmax() + offScaled;
	}

	private boolean inCullingBox(Coords3 p) {
		// check point is in culling box
		return (p.getXd() > cullingBox[0]) && (p.getXd() < cullingBox[1])
				&& (p.getYd() > cullingBox[2]) && (p.getYd() < cullingBox[3])
				&& (p.getZd() > cullingBox[4]) && (p.getZd() < cullingBox[5]);
	}

	private void initBounds() {
		boundsMin.set(Double.POSITIVE_INFINITY);
		boundsMax.set(Double.NEGATIVE_INFINITY);
	}

	private void updateBounds(Coords3 p) {

		// update bounds
		if (p.getXd() < boundsMin.getX()) {
			boundsMin.setX(p.getXd());
		}
		if (p.getYd() < boundsMin.getY()) {
			boundsMin.setY(p.getYd());
		}
		if (p.getZd() < boundsMin.getZ()) {
			boundsMin.setZ(p.getZd());
		}
		if (p.getXd() > boundsMax.getX()) {
			boundsMax.setX(p.getXd());
		}
		if (p.getYd() > boundsMax.getY()) {
			boundsMax.setY(p.getYd());
		}
		if (p.getZd() > boundsMax.getZ()) {
			boundsMax.setZ(p.getZd());
		}

	}

	@Override
	public void enlargeBounds(Coords min, Coords max, boolean dontExtend) {
		if (!Double.isInfinite(boundsMin.getX())) {
			if (dontExtend) {
				reduceBounds(boundsMin, boundsMax);
			}
			enlargeBounds(min, max, boundsMin, boundsMax);
		}
	}

	private boolean split() throws Corner.NotEnoughCornersException {

		if (currentSplitStoppedIndex == currentSplitIndex) {
			// swap stacks
			Corner[] tmp = currentSplit;
			currentSplit = nextSplit;
			nextSplit = tmp;
			currentSplitIndex = nextSplitIndex;
			nextSplitIndex = 0;
			currentSplitStoppedIndex = 0;
		}

		while (currentSplitStoppedIndex < currentSplitIndex
				&& loopSplitIndex < maxSplitsInOneUpdate) {
			currentSplit[currentSplitStoppedIndex].split(false);
			currentSplitStoppedIndex++;

			if (drawListIndex + (currentSplitIndex - currentSplitStoppedIndex)
					+ nextSplitIndex >= maxDraw) { // no room left for new draw
				return false;
			}

			if (nextSplitIndex >= maxSplit) { // no room left for new split
				return false;
			}
		}

		// debug("nextSplitIndex = " + nextSplitIndex + " , drawListIndex = " +
		// drawListIndex);

		if (loopSplitIndex < maxSplitsInOneUpdate && nextSplitIndex > 0) {
			return split();
		}

		return true; // went to end of loop

	}

	private void scaleXYZ(Coords3 p) {
		getView3D().scaleXYZ(p);
	}

	private void scaleAndNormalizeNormalXYZ(Coords3 n) {
		getView3D().scaleAndNormalizeNormalXYZ(n);
	}

	protected Coords3 evaluatePoint(double u, double v) {
		surfaceGeo.evaluatePoint(u, v, evaluatedPoint);

		if (!evaluatedPoint.isDefined()) {
			return Coords3.UNDEFINED;
		}

		updateBounds(evaluatedPoint);

		if (inCullingBox(evaluatedPoint)) {
			scaleXYZ(evaluatedPoint);
			return evaluatedPoint.copyVector();
		}

		return Coords3.UNDEFINED;
	}

	protected Coords3 evaluatePoint(double u, double v, Coords3 p) {

		// p is final value: use evaluatedPoint to compute
		if (p == null || p.isFinalUndefined()) {
			surfaceGeo.evaluatePoint(u, v, evaluatedPoint);

			if (!evaluatedPoint.isDefined()) {
				return Coords3.UNDEFINED;
			}

			updateBounds(evaluatedPoint);

			if (inCullingBox(evaluatedPoint)) {
				scaleXYZ(evaluatedPoint);
				return evaluatedPoint.copyVector();
			}

			return Coords3.UNDEFINED;
		}

		// p is not final value
		surfaceGeo.evaluatePoint(u, v, p);

		if (!p.isDefined()) {
			return Coords3.UNDEFINED;
		}

		updateBounds(p);

		if (inCullingBox(p)) {
			scaleXYZ(p);
			return p;
		}

		return Coords3.UNDEFINED;
	}

	protected Coords3 evaluateNormal(Coords3 p, double u, double v,
			Coords3 normal) {

		boolean defined;
		// normal is final value: use evaluatedNormal to compute
		if (normal == null || normal.isFinalUndefined()) {
			defined = surfaceGeo.evaluateNormal(p, u, v, evaluatedNormal);

			if (!defined) {
				return Coords3.UNDEFINED;
			}

			scaleAndNormalizeNormalXYZ(evaluatedNormal);
			return evaluatedNormal.copyVector();
		}

		// normal is not final value
		defined = surfaceGeo.evaluateNormal(p, u, v, normal);

		if (!defined) {
			return Coords3.UNDEFINED;
		}

		scaleAndNormalizeNormalXYZ(normal);
		return normal;

	}

	/**
	 * set center as barycenter for points
	 * 
	 * @param center
	 *            center
	 * @param normal
	 *            normal for center point
	 * @param c
	 *            corners
	 * 
	 */
	static void setBarycenter(Coords3 center, Coords3 normal,
			Corner... c) {
		setBarycenter(center, normal, c.length, c);
	}

	/**
	 * set center as barycenter for points
	 * 
	 * @param center
	 *            center
	 * @param normal
	 *            normal for center point
	 * @param length
	 *            length of considered corners
	 * @param c
	 *            corners
	 * 
	 */
	static void setBarycenter(Coords3 center, Coords3 normal,
			int length, Corner... c) {
		double f = 1.0 / length;

		// // try first barycenter about parameters
		// double u = 0, v = 0;
		// for (int j = 0; j < length; j++) {
		// u += c[j].u;
		// v += c[j].v;
		// }
		// u *= f;
		// v *= f;
		// Coords3 ret = evaluatePoint(u, v, center);
		// if (ret.isNotFinalUndefined()){ // center is not undefined
		// ret = evaluateNormal(center, u, v, normal);
		// if (ret.isNotFinalUndefined()){ // normal is not undefined
		// return;
		// }
		// }

		// center is undefined : barycenter about coords
		center.set(0, 0, 0);
		normal.set(0, 0, 0);
		// int lengthDefined = 0;
		for (int j = 0; j < length; j++) {
			// if (!center.isFinalUndefined()){
			center.addInside(c[j].p);
			normal.addInside(c[j].normal);
			// lengthDefined ++;
			// }
		}
		// f = 1.0 / lengthDefined;
		center.mulInside(f);
		normal.normalizeIfPossible();

		// if (!center.isDefined()) {
		// App.printStacktrace("!center.isDefined()");
		// }
		// if (!normal.isDefined()) {
		// App.printStacktrace("!normal.isDefined()");
		// }

	}

	/**
	 * 
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @return distance between c1 and c2, or POSITIVE_INFINITY if distance is
	 *         more than maxRWDistance
	 */
	protected double getDistance(Corner c1, Corner c2) {

		double ret = 0;

		double d = Math.abs(c1.p.getXd() - c2.p.getXd());
		if (d > maxRWDistance) {
			return Double.POSITIVE_INFINITY;
		}
		if (d > ret) {
			ret = d;
		}

		d = Math.abs(c1.p.getYd() - c2.p.getYd());
		if (d > maxRWDistance) {
			return Double.POSITIVE_INFINITY;
		}
		if (d > ret) {
			ret = d;
		}

		d = Math.abs(c1.p.getZd() - c2.p.getZd());
		if (d > maxRWDistance) {
			return Double.POSITIVE_INFINITY;
		}
		if (d > ret) {
			ret = d;
		}

		return ret;
	}

	/**
	 * 
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @param c4
	 *            fourth corner
	 * @return max distance between c1-c2 / c2-c3 / c3-c4 / c4-c1, or
	 *         POSITIVE_INFINITY if distance is more than maxRWDistance
	 */
	protected double getDistance(Corner c1, Corner c2, Corner c3, Corner c4) {
		double ret;
		double d;

		d = getDistance(c1, c2);
		if (Double.isInfinite(d)) {
			return d;
		}
		ret = d;

		d = getDistance(c2, c3);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		d = getDistance(c3, c4);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		d = getDistance(c4, c1);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		return ret;
	}

	/**
	 * 
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @param c4
	 *            fourth corner
	 * @return max distance between c1-c2 / c2-c3 / c3-c4, or POSITIVE_INFINITY
	 *         if distance is more than maxRWDistance
	 */
	double getDistanceNoLoop(Corner c1, Corner c2, Corner c3,
			Corner c4) {
		double ret;
		double d;

		d = getDistance(c1, c2);
		if (Double.isInfinite(d)) {
			return d;
		}
		ret = d;

		d = getDistance(c2, c3);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		d = getDistance(c3, c4);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		return ret;
	}

	/**
	 * 
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @return max distance between c1-c2 / c2-c3 / c3-c1, or POSITIVE_INFINITY
	 *         if distance is more than maxRWDistance
	 */
	protected double getDistance(Corner c1, Corner c2, Corner c3) {
		double ret;
		double d;

		d = getDistance(c1, c2);
		if (Double.isInfinite(d)) {
			return d;
		}
		ret = d;

		d = getDistance(c2, c3);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		d = getDistance(c3, c1);
		if (Double.isInfinite(d)) {
			return d;
		}
		if (d > ret) {
			ret = d;
		}

		return ret;
	}

	double getDistance(Corner... corners) {
		switch (corners.length) {
		case 2:
			return getDistance(corners[0], corners[1]);
		case 3:
			return getDistance(corners[0], corners[1], corners[2]);
		case 4:
			return getDistance(corners[0], corners[1], corners[2], corners[3]);
		}
		return 0;
	}

	boolean isAngleOK(double bend, boolean loops, Corner... corners) {
		if (loops) {
			return isAngleOKNoLoop(bend, corners[0], corners[1], corners[2], corners[3]);
		}
		switch (corners.length) {
		case 2:
			return isAngleOK(bend, corners[0], corners[1]);
		case 3:
			return isAngleOK(bend, corners[0], corners[1], corners[2]);
		case 4:
			return isAngleOK(bend, corners[0], corners[1], corners[2], corners[3]);
		}
		return false;
	}

	/**
	 * Returns whether the angle between the vectors (vx, vy) and (wx, wy) is
	 * smaller than MAX_BEND, where MAX_BEND = tan(MAX_ANGLE).
	 */
	private static boolean isAngleOK(Coords3 v, Coords3 w, double bend) {
		// |v| * |w| * sin(alpha) = |det(v, w)|
		// cos(alpha) = v . w / (|v| * |w|)
		// tan(alpha) = sin(alpha) / cos(alpha)
		// tan(alpha) = |det(v, w)| / v . w

		// small angle: tan(alpha) < MAX_BEND
		// |det(v, w)| / v . w < MAX_BEND
		// |det(v, w)| < MAX_BEND * (v . w)

		double innerProduct = v.getXd() * w.getXd() + v.getYd() * w.getYd()
				+ v.getZd() * w.getZd();

		if (innerProduct <= 0) {
			// angle >= 90 degrees
			return false;
		}

		// angle < 90 degrees
		// small angle: |det(v, w)| < MAX_BEND * (v . w)
		double d1 = v.getXd() * w.getYd() - v.getYd() * w.getXd();
		double d2 = v.getYd() * w.getZd() - v.getZd() * w.getYd();
		double d3 = v.getZd() * w.getXd() - v.getXd() * w.getZd();
		double det = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
		return det < bend * innerProduct;
	}

	/**
	 * 
	 * @param bend
	 *            tan of max angle
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @return true if angle is ok between c1-c2
	 */
	static boolean isAngleOK(double bend, Corner c1, Corner c2) {
		return isAngleOK(c1.normal, c2.normal, bend);
	}

	/**
	 * 
	 * @param bend
	 *            tan of max angle
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @return true if angle is ok between c1-c2 and c2-c3 and c3-c1
	 */
	static boolean isAngleOK(double bend, Corner c1, Corner c2,
			Corner c3) {
		return isAngleOK(c1.normal, c2.normal, bend)
				&& isAngleOK(c2.normal, c3.normal, bend)
				&& isAngleOK(c3.normal, c1.normal, bend);
	}

	/**
	 * 
	 * @param bend
	 *            tan of max angle
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @param c4
	 *            fourth corner
	 * @return true if angle is ok between c1-c2 and c2-c3 and c3-c4 and c4-c1
	 */
	static boolean isAngleOK(double bend, Corner c1, Corner c2,
			Corner c3, Corner c4) {
		return isAngleOK(c1.normal, c2.normal, bend)
				&& isAngleOK(c2.normal, c3.normal, bend)
				&& isAngleOK(c3.normal, c4.normal, bend)
				&& isAngleOK(c4.normal, c1.normal, bend);
	}

	/**
	 * 
	 * @param bend
	 *            tan of max angle
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 * @param c3
	 *            third corner
	 * @param c4
	 *            fourth corner
	 * @return true if angle is ok between c1-c2 and c2-c3 and c3-c4
	 */
	static boolean isAngleOKNoLoop(double bend, Corner c1, Corner c2,
			Corner c3, Corner c4) {
		return isAngleOK(c1.normal, c2.normal, bend)
				&& isAngleOK(c2.normal, c3.normal, bend)
				&& isAngleOK(c3.normal, c4.normal, bend);
	}

	/**
	 * add the corner to next split array
	 * 
	 * @param corner
	 *            corner
	 */
	void addToNextSplit(Corner corner) {
		nextSplit[nextSplitIndex] = corner;
		nextSplitIndex++;
	}

	@Override
	public boolean hit(Hitting hitting) {

		if (waitForReset) { // prevent NPE
			return false;
		}

		if (getGeoElement()
				.getAlphaValue() < EuclidianController.MIN_VISIBLE_ALPHA_VALUE) {
			return false;
		}

		if (((GeoElement) surfaceGeo).isGeoFunctionNVar()) {
			return hitFunction2Var(hitting);
		}

		if (!(getGeoElement() instanceof GeoSurfaceCartesian3D)) {
			return false;
		}

		if (curveHitting == null) {
			curveHitting = new CurveHitting(this, getView3D());
		}
		resetZPick();
		boolean isHit = false;
		if (borders.isEmpty()) {
			calculateBorders();
		}
		for (GeoCurveCartesian3D border : borders) {
			isHit = curveHitting.hit(hitting, border,
					Math.max(5, getGeoElement().getLineThickness())) || isHit;

		}
		return isHit;
	}

	private void calculateBorders() {
		for (int axis = 0; axis < 2; axis++) {
			double[] paramValues = new double[] {
					surfaceGeo.getMinParameter(axis),
					surfaceGeo.getMaxParameter(axis) };
			for (int borderIndex = 0; borderIndex < 2; borderIndex++) {
				GeoCurveCartesian3D border = setHitting(axis,
						paramValues[borderIndex]);
				borders.add(border);
			}
		}
	}

	private GeoCurveCartesian3D setHitting(int axis, double paramValue) {
		GeoCurveCartesian3D border = new GeoCurveCartesian3D(
				getGeoElement().getConstruction());
		GeoSurfaceCartesian3D geoSurface3D = (GeoSurfaceCartesian3D) getGeoElement();
		FunctionNVar[] functions = geoSurface3D.getFunctions();
		Function[] borderFunctions = new Function[functions.length];
		for (int i = 0; i < functions.length; i++) {
			Kernel kernel = geoSurface3D.getKernel();
			ExpressionNode expr = functions[i].getFunctionExpression()
					.deepCopy(kernel);
			FunctionVariable fVar = new FunctionVariable(kernel, "u");
			expr = expr.traverse(VariableReplacer.getReplacer(
					functions[i].getVarString(axis,
							StringTemplate.defaultTemplate),
					new MyDouble(kernel, paramValue), kernel)).wrap();
			expr = expr
					.traverse(VariableReplacer.getReplacer(
							functions[i].getVarString(1 - axis,
									StringTemplate.defaultTemplate),
							fVar, kernel))
					.wrap();
			borderFunctions[i] = new Function(expr, fVar);
		}
		border.setFun(borderFunctions);
		border.setInterval(geoSurface3D.getMinParameter(1 - axis),
				geoSurface3D.getMaxParameter(1 - axis));
		return border;

	}

	private boolean hitFunction2Var(Hitting hitting) {
		GeoFunctionNVar geoF = (GeoFunctionNVar) surfaceGeo;

		hitting.calculateClippedValues();
		if (Double.isNaN(hitting.x0)) { // hitting doesn't intersect
										// clipping box
			resetLastHitParameters(geoF);
			return false;
		}

		double[][] xyzf = geoF.getXYZF();

		// compute samples from xyz0 to xyz1, try to find consecutive +/-
		geoF.setXYZ(hitting.x0, hitting.y0, hitting.z0,
				xyzf[GeoFunctionNVar.DICHO_LAST]);
		boolean isLessZ0 = false, isLessZ1;
		isLessZ1 = GeoFunctionNVar.isLessZ(xyzf[GeoFunctionNVar.DICHO_LAST]);
		double t;

		for (int i = 1; i <= HIT_SAMPLES; i++) {
			double[] tmp = xyzf[GeoFunctionNVar.DICHO_FIRST];
			xyzf[GeoFunctionNVar.DICHO_FIRST] = xyzf[GeoFunctionNVar.DICHO_LAST];
			xyzf[GeoFunctionNVar.DICHO_LAST] = tmp;
			t = i * DELTA_SAMPLES;
			geoF.setXYZ(hitting.x0 * (1 - t) + hitting.x1 * t,
					hitting.y0 * (1 - t) + hitting.y1 * t,
					hitting.z0 * (1 - t) + hitting.z1 * t,
					xyzf[GeoFunctionNVar.DICHO_LAST]);
			isLessZ0 = isLessZ1;
			isLessZ1 = GeoFunctionNVar
					.isLessZ(xyzf[GeoFunctionNVar.DICHO_LAST]);
			if (isLessZ0 ^ isLessZ1) {
				break; // found
			}
		}

		// set - as first value, + as second value, or return false
		if (isLessZ0) {
			if (isLessZ1) {
				resetLastHitParameters(geoF);
				return false;
			}
			double dx = xyzf[GeoFunctionNVar.DICHO_FIRST][0]
					- hitting.origin.getX();
			double dy = xyzf[GeoFunctionNVar.DICHO_FIRST][1]
					- hitting.origin.getY();
			double dz = xyzf[GeoFunctionNVar.DICHO_FIRST][2]
					- hitting.origin.getZ();
			double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
			setZPick(-d, -d, hitting.discardPositiveHits(), d);
			setLastHitParameters(geoF, false);
			return true;
		}

		if (isLessZ1) {
			double dx = xyzf[GeoFunctionNVar.DICHO_FIRST][0]
					- hitting.origin.getX();
			double dy = xyzf[GeoFunctionNVar.DICHO_FIRST][1]
					- hitting.origin.getY();
			double dz = xyzf[GeoFunctionNVar.DICHO_FIRST][2]
					- hitting.origin.getZ();
			double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
			setZPick(-d, -d, hitting.discardPositiveHits(), d);
			setLastHitParameters(geoF, true);
			return true;
		}

		resetLastHitParameters(geoF);
		return false;
	}

	private static void setLastHitParameters(GeoFunctionNVar geoF,
			boolean swap) {
		geoF.setLastHitParameters(swap);
	}

	private static void resetLastHitParameters(GeoFunctionNVar geoF) {
		geoF.resetLastHitParameters();
	}

	@Override
	protected void updateForViewVisible() {
		updateGeometriesVisibility();
		updateForView();
	}

	@Override
	public void setWaitForUpdateVisualStyle(GProperty prop) {
		super.setWaitForUpdateVisualStyle(prop);
		if (prop == GProperty.LINE_STYLE) {
			// also update for line width (e.g when translated)
			setWaitForUpdate();
		} else if (prop == GProperty.COLOR) {
			setWaitForUpdateColor();
		} else if (prop == GProperty.HIGHLIGHT) {
			setWaitForUpdateColor();
		} else if (prop == GProperty.VISIBLE) {
			setWaitForUpdateVisibility();
		}
	}

	@Override
	protected GColor getObjectColorForOutline() {
		return GColor.DARK_GRAY;
	}

	/**
	 * set there is no more corner available
	 */
	private void setNoRoomLeft() {
		drawFromScratch = false;
		stillRoomLeft = false;
	}

	@Override
	public void setZPickIfBetter(double zNear, double zFar,
			boolean discardPositive, double positionOnHitting) {
		if (!needsDiscardZPick(discardPositive, zNear, zFar)
				&& (zNear > getZPickNear())) {
			setZPickValue(zNear, zFar);
			setPositionOnHitting(positionOnHitting);
		}
	}

	/**
	 * draw triangle with surface plotter
	 *
	 * @param surface
	 *            surface plotter
	 * @param p0
	 *            first point
	 * @param n0
	 *            first point normal
	 *
	 * @param c1
	 *            second point
	 * @param c2
	 *            third point
	 */
	protected void drawTriangle(PlotterSurface surface, Coords3 p0, Coords3 n0,
			Corner c1, Corner c2) {

		surface.normalDirect(n0);
		surface.vertexDirect(p0);
		surface.normalDirect(c2.normal);
		surface.vertexDirect(c2.p);
		surface.normalDirect(c1.normal);
		surface.vertexDirect(c1.p);

	}

	/**
	 * draws triangle between center and two corners
	 *
	 * @param surface
	 *            surface plotter
	 * @param cc
	 *            center
	 * @param c1
	 *            first corner
	 * @param c2
	 *            second corner
	 */
	protected void drawTriangle(PlotterSurface surface, CornerAndCenter cc,
			Corner c1, Corner c2) {
		drawTriangle(surface, cc.center, cc.centerNormal, c1, c2);
	}

	/**
	 * draw triangle with surface plotter, check if second and third points are
	 * defined
	 *
	 * @param surface
	 *            surface plotter
	 * @param cc
	 *            first point and normal
	 *
	 * @param c1
	 *            second point
	 * @param c2
	 *            third point
	 */
	void drawTriangleCheckCorners(PlotterSurface surface,
			CornerAndCenter cc, Corner c1, Corner c2) {
		if (c1.p.isFinalUndefined()) {
			return;
		}
		if (c2.p.isFinalUndefined()) {
			return;
		}

		drawTriangle(surface, cc, c1, c2);
	}

	CornerBuilder getCornerBuilder() {
		return cornerBuilder;
	}

	private void caught(Exception e) {
		e.printStackTrace();
		setNoRoomLeft();
	}
}
