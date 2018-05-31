package com.meetings.schedule.rykun_guys_meetings;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class HelloBot extends AbilityBot{

	private static final String DATE_REG_EX = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
	public static String BOT_TOKEN = "589560901:AAEENuvndSE60Y6iDjm9vWQTuMEOjzGL2Lc";
	public static String BOT_USERNAME = "@RykunsMeetingBot";
	public List<LocalDate> dates;
	public boolean waitingForDate;

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
	              .name("info")
	              .info("full information about rykun chat bot")
	              .locality(Locality.ALL)
	              .privacy(Privacy.PUBLIC)
	              .action(ctx -> silent.send(LoadBotTextContent.getInstance().getTargetText(LoadBotTextContent.INFO_REPLY), ctx.chatId()))
	              .build();
	}
	
	public Ability createNewMeetingDate() {
		return Ability
				.builder()
				.name("newMeeting")
				.info("Create new meeting")
			    .input(0)
				.locality(Locality.ALL)
	            .privacy(Privacy.PUBLIC)
				.action(ctx -> createNewMeeting(ctx.chatId()))
				.build();
	}
	
	public Ability chooseConvenientDate() {
	    return Ability
	              .builder()
	              .name("meeting")
	              .info("going to meet")
	              .locality(Locality.ALL)
	              .privacy(Privacy.PUBLIC) 
	              .action(ctx -> silent.send("Create the meeting first", ctx.chatId()))
	              .reply(upd -> pollForTheDate(upd.getMessage().getChatId()), upd -> dates != null && !dates.isEmpty(), upd -> upd.getMessage().getText().contains("/meeting"))
	              .build();
	}

	public Reply detectDate() {
		Consumer<Update> action = upd -> silent.send("I have detect the date", upd.getMessage().getChatId());
		return Reply.of(action, upd -> upd.getMessage().hasText(),upd -> waitingForDate, upd -> dates != null, upd -> isLineContainAnyDate(upd));
	}
	
	public Reply detectPhoto() {
		Consumer<Update> action = upd -> silent.send(
				ConditionalFlag.getRandomReplyFromTargetList(
						LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.PHOTO_REPLY)),
						upd.getMessage().getChatId());
		return Reply.of(action, Flag.PHOTO);
	}
	
	public Reply detectObsceneWords() {
		Consumer<Update> action = upd -> silent.send(
				ConditionalFlag.getRandomReplyFromTargetList(
						LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.OBSCENCE_REPLY)),
						upd.getMessage().getChatId());
		return Reply.of(action, ConditionalFlag.CONTAINS_OBSCENE_WORDS);
	}
	
	public Reply detectLaugh() {
		Consumer<Update> action = upd -> silent.send(
				ConditionalFlag.getRandomReplyFromTargetList(
						LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.LAUGH_REPLY)),
						upd.getMessage().getChatId());
		return Reply.of(action, ConditionalFlag.CONTAINS_LAUGH);
	}

	
	private void createNewMeeting(Long chatId) {
		if (dates == null || dates.isEmpty()) {
			silent.send("go ahead write dates", chatId);
			dates = new ArrayList<>();
			waitingForDate = true;
		}
	}
	
	private void pollForTheDate(Long chatId) {
		silent.send("ENTER THE DATE", chatId);
	}
	
	private void afteDateDetection() {
		
	}
	
    private void chooseAppropriateDate(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = getRecentsKeyboard();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter the description of set date");

	    try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
    }
    
    private ReplyKeyboardMarkup getRecentsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        var weatherList = dates.stream().map(LocalDate::toString).collect(Collectors.toList());
        for (String recentWeather : weatherList) {
            KeyboardRow row = new KeyboardRow();
            row.add(recentWeather);
            keyboard.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }


    private boolean isLineContainAnyDate(Update upd) {
		if(dates.isEmpty()) {
			return isMessageContainsRelevantDates(upd);
			
		} else {
			return checkIfDateExistInDatesList(upd);
		}

    }
    
    private boolean checkIfDateExistInDatesList(Update update) {
    	String date = update.getMessage().getText().split(",")[0];
    	if(date.matches(DATE_REG_EX)) {
			LocalDate selectedDate = LocalDate.parse(date);
			return dates.stream().anyMatch(dt -> dt.isEqual(selectedDate));
		}
    	return false;
    }
    
    private boolean isMessageContainsRelevantDates(Update update) {
    	String[] tokens = update.getMessage().getText().split(",");
    	try {
    		Arrays.stream(tokens).filter(dt -> dt.matches(DATE_REG_EX))
    							 .map(LocalDate::parse)
    							 .forEach(dates::add);
    		
    		return dates.isEmpty() ? false : true;
    	} catch (DateTimeParseException e) {
			System.out.println("!!!!!!!IT IS NO A DATE!!!!");
			//add message like , its not a date to logg
		}
    	return false;
    }
	
	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		var telegramBotApi = new TelegramBotsApi();
		try {
			telegramBotApi.registerBot(new HelloBot());
			//telegramBotApi.registerBot(new LaughHandler(BOT_TOKEN, BOT_USERNAME, 327156282));
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
	}
}
