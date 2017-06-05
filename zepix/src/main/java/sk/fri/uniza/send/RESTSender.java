package sk.fri.uniza.send;

import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.SendRequest;
import sk.fri.uniza.Sender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RESTSender implements Sender {
    @Override
    public void send(SendRequest sendRequest) {
        try {
            trySend(sendRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trySend(SendRequest sendRequest) throws IOException {
        URL url = constructURL(sendRequest);
        byte [] representation = constructRepresentation(sendRequest);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(representation);
        outputStream.flush();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
           // do nothing - only simulation
        }
        connection.disconnect();
    }

    /**
     * filing room has to receive postingsheet on this url
     */
    private URL constructURL(SendRequest sendRequest) throws MalformedURLException {
        String url = sendRequest.getFilingRoomURL();
        url += "/subjects/"+ sendRequest.getSubject() + "/postingSheet";
        return new URL(url);
    }

    private byte[] constructRepresentation(SendRequest sendRequest) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PostingSheet postingSheet = sendRequest.getPostingSheet();
        postingSheet.writeSignedContentTo(baos);
        return baos.toByteArray();
    }
}
