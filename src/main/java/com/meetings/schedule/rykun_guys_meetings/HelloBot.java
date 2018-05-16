package com.meetings.schedule.rykun_guys_meetings;

import java.util.function.Consumer;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class HelloBot extends AbilityBot{

	public static String BOT_TOKEN = "589560901:AAEENuvndSE60Y6iDjm9vWQTuMEOjzGL2Lc";
	public static String BOT_USERNAME = "@RykunsMeetingBot";

	 public HelloBot() {
	    super(BOT_TOKEN, BOT_USERNAME);
	  }
	
	
	public HelloBot(String botToken, String botUsername) {
		super(botToken, botUsername);
	}

	@Override
	public int creatorId() {
		return 327156282;
	}

	
	public Ability sayHelloWorld() {
	    return Ability
	              .builder()
	              .name("hello")
	              .info("says hello world!")
	              .locality(Locality.ALL)
	              .privacy(Privacy.PUBLIC)
	              .action(ctx -> silent.send("Hello world!", ctx.chatId()))
	              .post(ctx -> silent.send("Bye world!", ctx.chatId()))
	              .build();
	    
	    
	}
	
	
	public Reply sayGivnoOnAnyMessage() {
		Consumer<Update> action = upd -> silent.send("givno", upd.getMessage().getChatId());
		return Reply.of(action, Flag.PHOTO);
	}
	
	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		var telegramBotApi = new TelegramBotsApi();
		try {
			telegramBotApi.registerBot(new HelloBot());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
	}
}
