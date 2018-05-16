package com.meetings.schedule.rykun_guys_meetings;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

/**
 * Hello world!
 *
 */
public class App extends TelegramLongPollingBot {
	public static void main(String[] args) {
		ApiContextInitializer.init();
		var botApi = new TelegramBotsApi();
		
		try {
			botApi.registerBot(new App());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public String getBotUsername() {
		return "rykunsmeetings";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Message incomeMessage = update.getMessage();
		if(update.hasMessage() && incomeMessage.hasText()) {
			System.out.println(incomeMessage.getText());
			var message = new SendMessage()
					.setChatId(incomeMessage.getChatId())
					.setText(incomeMessage.getText() + " golden rats");
		
			try {
				execute(message);
			} catch (TelegramApiException e) {
				  e.printStackTrace();
			}
		}
	}

	@Override
	public String getBotToken() {
		return "589560901:AAEENuvndSE60Y6iDjm9vWQTuMEOjzGL2Lc";
	}
}
