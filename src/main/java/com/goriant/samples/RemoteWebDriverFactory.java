package com.goriant.samples;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class RemoteWebDriverFactory {

    public static RemoteWebDriver createDriverFromSession(final String id, String url) throws MalformedURLException {
        final SessionId sessionId = new SessionId(id);
        final URL command_executor = new URL(url);
        CommandExecutor executor = new HttpCommandExecutor(command_executor) {

            @Override
            public Response execute(Command command) throws IOException {
                Response response;
                if (command.getName() == "newSession") {
                    response = new Response();
                    response.setSessionId(sessionId.toString());
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field commandCodec = null;
                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        commandCodec.set(this, new W3CHttpCommandCodec());

                        Field responseCodec = null;
                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        responseCodec.set(this, new W3CHttpResponseCodec());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };

        ChromeOptions chromeOptions = new ChromeOptions();

        if (StringUtils.isEmpty(id))
            return new RemoteWebDriver(chromeOptions);
        else
            return new RemoteWebDriver(executor, chromeOptions);
    }
}
