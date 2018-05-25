package com.meetings.schedule.rykun_guys_meetings;


import org.junit.jupiter.api.Test;


public class AppTest {
	
	
	private LoadBotTextContent loadBotTextContent;
	
	
	@Test
	public void shouldLoadTextFormPropertieFile() {
		loadBotTextContent = LoadBotTextContent.getInstance();
		
	//	System.out.println(loadBotTextContent.getObscenceWordsSet());
	}


}
