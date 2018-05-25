package com.meetings.schedule.rykun_guys_meetings;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.telegram.telegrambots.api.objects.Update;

public enum ConditionalFlag implements Predicate<Update> {

	CONTAINS_OBSCENE_WORDS(upd -> upd.getMessage().hasText() && containsWordsFromTargetList(upd,
			LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.OBSCENCE_WORDS))),
	
	CONTAINS_LAUGH(upd -> upd.getMessage().hasText() && containsWordsFromTargetList(upd,
			LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.LAUGH_LIST)));

	private Predicate<Update> predicate;
	private static List<String> obscenceRepliesToWordsList;

	ConditionalFlag(Predicate<Update> predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean test(Update update) {
		return nonNull(update) && predicate.test(update);
	}

	private static boolean containsWordsFromTargetList(Update upd, List<String> targetList) {
		return !targetList.stream().filter(elem -> upd.getMessage().getText().contains(elem))
				.collect(Collectors.toList()).isEmpty();
	}

	public static String getRandomReplyFromTargetList(List<String> targetList) {
		return targetList.get(new Random().nextInt(targetList.size()));
	}

}
