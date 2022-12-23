package com.starly.main.SosysicBot.service;

import com.google.common.collect.Lists;
import com.starly.main.SosysicBot.config.ConfigBot;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AdsRepository adsRepository;
    static final String random = "Твое рандомное число = ";
    static final String help = "Тут будет текст /help";
    static final String randomSmile = "Рандомный слайм на сегодня...";
    static final String getRandomSmile = "Твой рандомный смайл...";
    final ConfigBot configBot;
    final int min = 1;
    final int max = 10000;

    public TelegramBot(ConfigBot configBot){
        this.configBot = configBot;

        // Метод добавления комманд в меню
        List<BotCommand> listCommand = new ArrayList<>();
        listCommand.add(new BotCommand("/start", "Начало взаимодействия с ботом"));
        listCommand.add(new BotCommand("/help", "Показывает возможные интерфейсы"));
        listCommand.add(new BotCommand("/score", "Показывает ваше количество очков"));
        listCommand.add(new BotCommand("/settings", "Настройки бота"));
        listCommand.add(new BotCommand("/random", "Хз зачем я это добавил, но если ты нажмешь, то у тебя будет выводиться рандомное число))0))"));
        listCommand.add(new BotCommand("/random_smile", "Рандомный смайл?))"));
        try{
            this.execute(new SetMyCommands(listCommand, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            log.error("Error settings bot command list" + e.getMessage());
        }

    }


    @Override
    public String getBotUsername() {
        return configBot.getBotName();
    }

    @Override
    public String getBotToken() {
        return configBot.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.contains("/send") && messageText.contains("/score")){
                var textSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                sendMessage(chatId, textSend);
            } else {
                switch (messageText){
                    case "/start":
                        startCommandRec(chatId, update.getMessage().getChat().getFirstName());
                        sendMessage(chatId, EmojiParser.parseToUnicode(":sunglasses:"));
                        break;
                    case "/help":
                        sendMessage(chatId, help);
                        break;
                    case "/random":
                        sendMessage(chatId, random + rnd(min, max));
                        break;
                    case "/random_smile":
                        sendMessage(chatId, randomSmile);
                        sendMessage(chatId,EmojiParser.parseToUnicode(givenListener()));
                        break;
                    case "Получить рандомный смайл":
                        sendMessage(chatId, getRandomSmile);
                        sendMessage(chatId, EmojiParser.parseToUnicode(givenListener()));
                        break;
                    default:
                        sendMessage(chatId, "Извини, но я ни**я не понял, что ты написал...");
                }
            }

        }
    }


    private int rnd(int min, int max){
        max -= min;
        return (int) ((Math.random() * ++max) + min);
    }
//    private void registerUser(Message message){
//        if (userRepository.findById(message.getChatId()).isEmpty()){
//            var chatId = message.getChatId();
//            var chat = message.getChat();
//
//            TelegramUser user = new TelegramUser();
//
//            user.setChatId(chatId);
//            user.setFirstName(chat.getFirstName());
//            user.setLastName(chat.getLastName());
//            user.setUserName(chat.getUserName());
//            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
//
//            userRepository.save(user);
//        }
//    }

    private void startCommandRec(long chatId, String name){
        //String answer = "Hi, " + name;
        String answer = EmojiParser.parseToUnicode("Дарова, " + name );
        sendMessage(chatId, answer);
        log.info("Replied to user "+ name);
    }

    public String givenListener(){
        Random rand = new Random();
        List givenList = Lists.newArrayList(":grinning:", ":smiley:", ":smile:", ":grin:", ":laughing:",
                ":sweat_smile:", ":joy:", ":rofl:", ":relaxed:", ":blush:", ":innocent:", ":slight_smile:",
                ":upside_down:", ":wink:", ":relieved:", ":heart_eyes:", ":kissing_heart:", ":kissing:",
                ":kissing_smiling_eyes:", ":kissing_closed_eyes:", ":yum:", ":stuck_out_tongue_winking_eye:",
                ":money_mouth:", ":hugging:", ":nerd:", ":sunglasses:", ":clown:", ":cowboy:", ":smirk:",
                ":pensive:", ":persevere:", ":triumph:", ":angry:", ":rage:", ":scream:", ":fearful:", ":cry:",
                ":sob:", ":thinking:", ":mask:", ":smiling_imp:", ":poop:");
        int randomIndex = rand.nextInt(givenList.size());
        return givenList.get(randomIndex).toString();
    }

    private void sendMessage(long chatId, String textSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textSend);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Получить рандомный смайл");
        row.add("Узнать погоду в своем городе");
        row.add("Узнать свое имя)))00)0)))");

        keyboardRows.add(row);

        markup.setKeyboard(keyboardRows);

        message.setReplyMarkup(markup);


        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error("Error occurred" + e.getMessage());
        }
    }
    private void prepareSendMessage(long chatId, String textSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textSend);
    }
}
