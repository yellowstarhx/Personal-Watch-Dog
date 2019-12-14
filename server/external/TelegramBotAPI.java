package external;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;

import entity.Image;

public class TelegramBotAPI {
	private static final long chatId = 859831345;
	private static final String token = "1006887305:AAFOwe0NnAv0voTk2Pj9AE7d8OQHbQpYaQI";
	
	// Create your bot passing the token received from @BotFather
	private TelegramBot bot = new TelegramBot(token);
	
	public void sendToTelegram(Image image) {
		// Send messages to telegram
		String msg = "Captured: " + image.getCaptureTime();	
//		bot.execute(new SendMessage(chatId, msg));
		SendPhoto message = new SendPhoto(chatId, image.getUrl()).caption(msg);
		bot.execute(message);
	}
}
