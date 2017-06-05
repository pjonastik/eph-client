package sk.fri.uniza.testUtils;

import sk.fri.uniza.SendRequest;

public final class SendRequestHelper {

    public static final String FILING_ROOM_URL = "http://ekpt.posta.sk";
    public static final String SUBJECT = "subject";
    public static final String USER_NAME = "user name";
    public static final char [] PASSWORD = "password".toCharArray();

    private SendRequestHelper() {}

    public static SendRequest getInstance() {
        SendRequest request = new SendRequest();
        request.setFilingRoomURL(FILING_ROOM_URL);
        request.setSubject(SUBJECT);
        request.setUserName(USER_NAME);
        request.setPassword(PASSWORD);

        return request;
    }

}
