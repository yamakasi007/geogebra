package org.geogebra.web.html5.gui.voiceInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.geogebra.common.util.StringUtil;
import org.geogebra.web.html5.gui.voiceInput.questResErr.QuestResErrConstants;
import org.geogebra.web.html5.gui.voiceInput.questResErr.QuestResErrInterface;
import org.geogebra.web.html5.gui.voiceInput.questResErr.RadiusQuestResErr;
import org.geogebra.web.html5.gui.voiceInput.questResErr.XCoordQuestResErr;
import org.geogebra.web.html5.gui.voiceInput.questResErr.YCoordQuestResErr;

/**
 * @author Csilla
 *
 */
public class NaturalLanguageProcessor {
	
	private VoiceInputOutputController controller;
	private static final HashSet<String> actWords = new HashSet<>(Arrays
			.asList("create", "draw", "construct", "build", "produce", "make"));
	private static final HashSet<String> toolWords = new HashSet<>(Arrays
			.asList("circle", "point", "segment"));
	private static final HashSet<String> parameterWords = new HashSet<>(
			Arrays.asList("coordinates", "radius", "center"));
	private static final HashSet<String> bindWords = new HashSet<>(
			Arrays.asList("with", "it", "in", "to", "a", "an", "and"));
	private String tool;
	private String act;
	private boolean xCoordSet;
	private String parameterWord;

	/**
	 * @param controller
	 *            voice input-output controller
	 */
	public NaturalLanguageProcessor(VoiceInputOutputController controller) {
		this.controller = controller;
	}

	/**
	 * @param input
	 *            user voice input
	 */
	public void processInput(String input) {
		tool = "";
		act = "";
		xCoordSet = false;
		parameterWord = "";
		String pointTest1 = "I would like to create a point with coordinates 1 and 2.";
		String pointTest2 = "I might want to draw a point with coordinates 1.";
		String pointTest3 = "I would prefer to construct a point with coordinates.";
		String pointTest4 = "Please build a point.";
		String circleTest1 = "I would like to create a circle with coordinates 1 and 2 and radius 5.";
		String circleTest2 = "I might want to draw a circle with coordinates 1 and 2.";
		String circleTest3 = "I would prefer to construct a circle with coordinates 3.";
		String circleTest4 = "I would like to create a circle with coordinates 1 and radius 5.";
		String circleTest5 = "I might want to draw a circle with radius 10.";
		String circleTest6 = "Please build a circle.";
		String segmentTest1 = "I would like to create a segment with coordinates 1 and 2 and coordinates 3 and 4.";
		String segmentTest2 = "I might want to draw a segment with coordinates 1 and 2 and coordinates 3.";
		String segmentTest3 = "I would prefer to construct a segment with coordinates 1 and 2.";
		String segmentTest4 = "I would like to create a segment with coordinates 1.";
		String segmentTest5 = "I might want to draw a segment with coordinates 10 and coordinates 12.";
		String segmentTest6 = "Please build a segment.";
		String text = cleanText(input);
		if (text != "") {
			tokenize(text);
			// act and tool was recognized
			if (!"".equals(act) && !"".equals(tool)) {
				controller.collectInput();
			} else {
				controller.initSpeechSynth(
						"I couldn't interpret it. Please repeat command. Must contain create and tool name.",
						QuestResErrConstants.COMMAND);
			}
		} else {
			controller.initSpeechSynth(
					"I couldn't interpret it. Please repeat command. Must contain create and tool name.",
					QuestResErrConstants.COMMAND);
		}
	}

	private static String cleanText(String sentence) {
		// remove whitespace from start and end of sentence
		String cleanedText = sentence.trim();
		// turn upper case letters to lower case
		cleanedText = cleanedText.toLowerCase();
		// remove unnecessary tabs
		cleanedText.replace("\t", " ");
		// remove double spaces
		while (cleanedText.contains("  ")) {
			cleanedText = cleanedText.replace("  ", " ");
		}
		return cleanedText;
	}

	private ArrayList<String> tokenize(String sentence) {
		ArrayList<String> tokenList = new ArrayList<>();
		// get words by splitting the sentence by whitespace
		String[] tokenArray = sentence.split(" ");
		for (String token : tokenArray) {
			tokenList.add(token);
		}
		// filter words by looking for important words
		tokenList = filterTokens(tokenList);
		return tokenList;
	}

	private ArrayList<String> filterTokens(ArrayList<String> tokens) {
		ArrayList<String> filteredTokens = new ArrayList<>();
		for (String token : tokens) {
			// cleaned token(no , or . at the end)
			String processedToken = processToken(token);
			if (processedToken != null) {
				filteredTokens.add(processedToken);
			}
		}
		return filteredTokens;
	}

	private String processToken(String token) {
		String cleanedToken = token;
		// handle "word," or "sentence end."
		if (token.charAt(token.length() - 1) == '.'
				|| token.charAt(token.length() - 1) == ',') {
			cleanedToken = token.substring(0, token.length() - 1);
		}
		// recognize intention of creating something
		if (actWords.contains(cleanedToken)) {
			this.act = cleanedToken;
		}
		// recognize which mathematical object
		else if (toolWords.contains(cleanedToken)) {
			this.tool = cleanedToken;
			// get tool id
			int commandID = controller.getDispatcher().getCommandID(tool);
			// get needed input list for tool
			controller.getDispatcher().processCommand(commandID);
		}
		// specific parameter words, like coordinates or radius
		else if (parameterWords.contains(cleanedToken)) {
			parameterWord = cleanedToken;
		}
		// parameter inputs, like numbers for x and y coordinate
		else if (StringUtil.isNumber(cleanedToken)) {
			if (!"".equals(parameterWord)) {
				setParameterInput(cleanedToken);
			}
		}
		// remove bind words, not needed
		else if (bindWords.contains(cleanedToken)) {
			return null;
		}
		// collect rest, might be useful
		return cleanedToken;
	}

	private void setParameterInput(String input) {
		for (QuestResErrInterface questResErr : controller.getDispatcher()
				.getQuestList()) {
			switch (parameterWord) {
				case "coordinates":
				case "center":
				if (questResErr instanceof XCoordQuestResErr
						&& "".equals(questResErr.getResponse())
						&& !xCoordSet) {
					questResErr.setResponse(input);
					xCoordSet = true;
					return;
				}
				if (questResErr instanceof YCoordQuestResErr
						&& "".equals(questResErr.getResponse()) && xCoordSet) {
					questResErr.setResponse(input);
					xCoordSet = false;
					parameterWord = "";
					return;
				}
					break;
				case "radius":
				if (questResErr instanceof RadiusQuestResErr
						&& "".equals(questResErr.getResponse())) {
					questResErr.setResponse(input);
					parameterWord = "";
					return;
				}
			default:
				break;
			}
		}
	}
}
