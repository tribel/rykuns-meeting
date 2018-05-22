package com.meetings.schedule.rykun_guys_meetings;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.telegram.telegrambots.api.objects.Update;

public enum ConditionalFlag implements Predicate<Update> {

	CONTAINS_OBSCENE_WORDS(upd -> upd.getMessage().hasText() && containsObsceneWord(upd));
	private Predicate<Update> predicate;
	private static List<String> obscenceWordsList;
	private static List<String> obscenceRepliesToWordsList;
	
	ConditionalFlag(Predicate<Update> predicate) {
	    this.predicate = predicate;
	  }

	@Override
	public boolean test(Update update) {
		return nonNull(update) && predicate.test(update);
	}
	
	private static boolean containsObsceneWord(Update upd) {
		obscenceWordsList = LoadBotTextContent.getInstance().getObscenceWordsSet();
		return !obscenceWordsList.stream()
				.filter(elem -> upd.getMessage().getText().contains(elem))
				.collect(Collectors.toList())
				.isEmpty();
	}

	public static String getRandomReplyToObsceneWord() {
		obscenceRepliesToWordsList = LoadBotTextContent.getInstance().getRepliesToObscenceWords();
		return obscenceRepliesToWordsList.get(new Random().nextInt(obscenceRepliesToWordsList.size()));
	}
	
}
