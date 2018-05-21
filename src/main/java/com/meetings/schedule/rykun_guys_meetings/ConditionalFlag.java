package com.meetings.schedule.rykun_guys_meetings;

import static java.util.Objects.nonNull;

import java.util.Set;
import java.util.function.Predicate;

import org.telegram.telegrambots.api.objects.Update;

public enum ConditionalFlag implements Predicate<Update> {

	CONTAINS_OBSCENE_WORDS(upd -> upd.getMessage().hasText() && containsObsceneWord(upd));
	private Predicate<Update> predicate;
	private static Set<String> obscenceWordsSet = LoadBotTextContent.getInstance().getObscenceWordsSet();

	ConditionalFlag(Predicate<Update> predicate) {
	    this.predicate = predicate;
	  }

	@Override
	public boolean test(Update update) {
		return nonNull(update) && predicate.test(update);
	}
	
	private static boolean containsObsceneWord(Update upd) {
		obscenceWordsSet = LoadBotTextContent.getInstance().getObscenceWordsSet();
		return obscenceWordsSet.contains(upd.getMessage().getText());
	}

}
