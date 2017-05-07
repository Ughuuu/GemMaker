package com.gemengine.system.helper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.MarkerManager;

import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * String constants used in engine are contained here.
 * 
 * @author Dragos
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.gemengine.system.helper.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			log.debug(MarkerManager.getMarker("gem"), "Missing message", e);
			return '!' + key + '!';
		}
	}

	private Messages() {
	}
}
