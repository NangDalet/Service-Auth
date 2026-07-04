package com.dt.student.register.happer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Component
public class TelegramUtils {

    private static String apiToken;

    /**
     * Constructor for TelegramUtils, called by Spring to initialize the static API token.
     *
     * @param token The API token injected from the application properties.
     */
    public TelegramUtils(@Value("${telegram.api.token}") String token) {
        TelegramUtils.apiToken = token;
    }

    /**
     * Sends a message to a specified Telegram chat ID.
     *
     * @param sendMessage The message to send.
     * @param chatId      The chat ID to send the message to.
     * @return Boolean indicating success or failure.
     */
    public static Boolean sendMessage(String sendMessage, String chatId) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=%s";

        urlString = String.format(urlString, apiToken, chatId, sendMessage, "html");

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            StringBuilder sb = new StringBuilder();
            try (InputStream is = new BufferedInputStream(conn.getInputStream());
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a file to a specified Telegram chat ID.
     *
     * @param file   The file to send.
     * @param chatId The chat ID to send the file to.
     * @return Boolean indicating success or failure.
     */
    public static Boolean sendMessageWithFile(String file, String chatId) {
        String urlString = "https://api.telegram.org/bot%s/sendDocument?chat_id=%s&document=%s";
        urlString = String.format(urlString, apiToken, chatId, file);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            StringBuilder sb = new StringBuilder();
            try (InputStream is = new BufferedInputStream(conn.getInputStream());
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
