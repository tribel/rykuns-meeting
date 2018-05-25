package com.meetings.schedule.rykun_guys_meetings;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LoadBotTextContent {

	public static final String OBSCENCE_WORDS = "obscence_words_list";
	public static final String OBSCENCE_REPLY = "reply_to_obscence_words";
	public static final String PHOTO_REPLY = "reply_to_photo";
	public static final String LAUGH_LIST = "laugh_list";
	public static final String LAUGH_REPLY = "reply_to_laugh";
	
	private static LoadBotTextContent instance;
	private static Properties properties;
	
	private LoadBotTextContent() {}
	
	public static synchronized LoadBotTextContent getInstance() {
		if (instance == null) { 
			instance = new LoadBotTextContent();
			try (var input = new FileInputStream("./text/text-content.properties")){
				properties = new Properties();
				properties.load(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return instance;
	}

	public List<String> getTextTokens(String targetName) {
		return Arrays.asList(properties.getProperty(targetName).split(","));
	}
	
}
