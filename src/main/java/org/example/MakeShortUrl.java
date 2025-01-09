package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MakeShortUrl {
    private final Map<String, Map<String, ShortUrl>> urlsMap = new HashMap<>();

    public Boolean urlCheck(String userId, String shortUrlNew) {
        return urlsMap.get(userId).get(shortUrlNew) != null;
    }

    private void urlSave(String userId, ShortUrl shortUrl) {
        Map<String, ShortUrl> map = urlsMap.get(userId);

        if (map != null) {
            map.put(shortUrl.getUrl(), shortUrl);
            urlsMap.put(userId, map);
        } else {
            Map<String, ShortUrl> mapNew = new HashMap<>();
            mapNew.put(shortUrl.getUrl(), shortUrl);
            urlsMap.put(userId, mapNew);
        }
    }

    public String encode(String originalUrl, String userId, int urlClickLimit) {
        Date dateCreate = new Date();
        String hashValue = generateHash(originalUrl + userId + dateCreate.getTime() + urlClickLimit);
        ShortUrl shortUrl = new ShortUrl(hashValue, originalUrl, dateCreate, urlClickLimit);
        urlSave(userId, shortUrl);

        return shortUrl.getUrl();
    }

    public ShortUrl decode(String userId, String shortUrl) {
        Map<String, ShortUrl> map;
        ShortUrl originalUrl = null;

        try {
            map = urlsMap.get(userId);
            originalUrl = map.get(shortUrl);

            if (originalUrl.getClickValue()) {
                System.out.println("Превышено количество переходов, ссылка была удалена");
                urlsMap.get(userId).remove(shortUrl);
                return null;
            }
            if (new Date().getTime() - originalUrl.getDate().getTime() > 100000) {
                System.out.println("Устаревшая ссылка");
                urlsMap.get(userId).remove(shortUrl);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Короткой ссылки: " + shortUrl + " не существует");
        }
        return originalUrl;
    }

    private String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes());
            String hash = Base64.getUrlEncoder().encodeToString(hashInBytes);

            return hash.substring(0, 7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
