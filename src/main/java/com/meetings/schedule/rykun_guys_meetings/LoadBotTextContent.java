package com.meetings.schedule.rykun_guys_meetings;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class LoadBotTextContent {

	private static final String OBSCENCE_WORDS = "obscence_words_list";
	private static final String OBSCENCE_REPLY = "reply_to_obscence_words";
	
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

	
	public List<String> getObscenceWordsSet() {
		return Arrays.asList(properties.getProperty(OBSCENCE_WORDS).split(","));
	}
	
	public List<String> getRepliesToObscenceWords() {
		return  Arrays.asList(properties.getProperty(OBSCENCE_REPLY).split(","));
	}
}
