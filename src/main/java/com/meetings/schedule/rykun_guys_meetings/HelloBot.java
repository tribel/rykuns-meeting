package com.meetings.schedule.rykun_guys_meetings;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Ability.AbilityBuilder;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
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
	
	
	public Ability calculateNearestDate() {
	    return Ability
	              .builder()
	              .name("встреча")
	              .info("когда собересмся")
	              .locality(Locality.ALL)
	              .privacy(Privacy.PUBLIC)
	              .action(ctx -> onCurrentChoosen(ctx.chatId()))
	              .build();
	    
	    
	}

	public Reply sayGivnoOnAnyMessage() {
		Consumer<Update> action = upd -> silent.send("givno", upd.getMessage().getChatId());
		return Reply.of(action, Flag.PHOTO);
	}
	
	
	public Reply detectObsceneWords() {
		Consumer<Update> action = upd -> silent.send(ConditionalFlag.getRandomReplyToObsceneWord(), upd.getMessage().getChatId());
		return Reply.of(action, ConditionalFlag.CONTAINS_OBSCENE_WORDS);
	}

	public Ability playWithMe() {
	    String playMessage = "Play with me!";

	    return Ability.builder()
	        .name("play")
	        .info("Do you want to play with me?")
	        .privacy(Privacy.PUBLIC)
	        .locality(Locality.ALL)
	        .input(0)
	        .action(ctx -> silent.forceReply(playMessage, ctx.chatId()))
	        // The signature of a reply is -> (Consumer<Update> action, Predicate<Update>... conditions)
	        // So, we  first declare the action that takes an update (NOT A MESSAGECONTEXT) like the action above
	        // The reason of that is that a reply can be so versatile depending on the message, context becomes an inefficient wrapping
	        .reply(upd -> {
	              // Prints to console
	              System.out.println("I'm in a reply!");
	              // Sends message
	              silent.send("It's been nice playing with you!", upd.getMessage().getChatId());
	            },
	            // Now we start declaring conditions, MESSAGE is a member of the enum Flag class
	            // That class contains out-of-the-box predicates for your replies!
	            // MESSAGE means that the update must have a message
	            // This is imported statically, Flag.MESSAGE
	            Flag.MESSAGE,
	            // REPLY means that the update must be a reply, Flag.REPLY
	            Flag.REPLY,
	            // A new predicate user-defined
	            // The reply must be to the bot
	            isReplyToBot(),
	            // If we process similar logic in other abilities, then we have to make this reply specific to this message
	            // The reply is to the playMessage
	            isReplyToMessage(playMessage)
	        )
	        // You can add more replies by calling .reply(...)
	        .build();
	  }
	  
	    private Predicate<Update> isReplyToMessage(String message) {
	      return upd -> {
	        Message reply = upd.getMessage().getReplyToMessage();
	        return reply.hasText() && reply.getText().equalsIgnoreCase(message);
	      };
	    }
	  
	    private Predicate<Update> isReplyToBot() {
	      return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
	    }

	    
	    private  void onCurrentChoosen(Long chatId) {
	        SendMessage sendMessage = new SendMessage();
	        sendMessage.enableMarkdown(true);

	        ReplyKeyboardMarkup replyKeyboardMarkup = getRecentsKeyboard();
	        sendMessage.setReplyMarkup(replyKeyboardMarkup);
	        sendMessage.setChatId(chatId);
	        if (replyKeyboardMarkup.getKeyboard().size() > 3) {
	            sendMessage.setText("HELLLLOOOOOOO!");
	        } else {
	            sendMessage.setText("AHAHAHAHAHAHAHAH");
	        }

	       
	       try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			System.out.println("@@@@@@KEYBOARD OOO");
			e.printStackTrace();
		}
	    }
	    
	    private static ReplyKeyboardMarkup getRecentsKeyboard() {
	        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
	        replyKeyboardMarkup.setSelective(true);
	        replyKeyboardMarkup.setResizeKeyboard(true);
	        replyKeyboardMarkup.setOneTimeKeyboard(true);

	        List<KeyboardRow> keyboard = new ArrayList<>();
	        var weatherList = List.of("a","b","c","d");
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


enum CultureWods  implements Predicate<Update> {
	TEXT_WITH_WORDS(upd -> upd.getMessage().hasText() && upd.getMessage().getText().contains("meg"));
	 private final Predicate<Update> predicate;

	  CultureWods(Predicate<Update> predicate) {
	    this.predicate = predicate;
	  }

	@Override
	public boolean test(Update update) {
		 return nonNull(update) && predicate.test(update);
	}
	
}
