package com.meetings.schedule.rykun_guys_meetings;

import java.util.function.Consumer;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.api.objects.Update;

public class LaughHandler extends AbilityBot{

	private int creatorId;
	
	public LaughHandler(String botToken, String botUsername, int creatorId) {
		super(botToken, botUsername);
		this.creatorId = creatorId;
	}

	@Override
	public int creatorId() {
		return this.creatorId;
	}
	
	public Reply detectLaugh() {
		Consumer<Update> action = upd -> silent.send(
				ConditionalFlag.getRandomReplyFromTargetList(
						LoadBotTextContent.getInstance().getTextTokens(LoadBotTextContent.LAUGH_REPLY)),
						upd.getMessage().getChatId());
		return Reply.of(action, ConditionalFlag.CONTAINS_LAUGH);
	}

}
