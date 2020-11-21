/*
 * Licensed under the MIT License
 *
 * Copyright (c) 2020 iFlyinq (Niels W.)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.flydev.riotbot.API;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.flydev.riotbot.RiotBot;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * The type Riot api.
 */
public class RiotAPI {

    private static final Gson GSON = new Gson();
    private static final OkHttpClient client = new OkHttpClient();
    private static final String token = RiotBot.getSecrets().getAsJsonObject("riot").get("api-token").getAsString();

    @SneakyThrows
    private static Response getResponse(String baseUrl) {
        String url = baseUrl + "?api_key=" + token;
        return client.newCall(new Request.Builder().url(url).build()).execute();
    }

    /**
     * Gets class.
     *
     * @param <T>   the type parameter
     * @param url   the url
     * @param clazz the clazz
     * @return the class
     * @throws IOException the io exception
     */
    public static <T> T getClass(String url, Class<? extends T> clazz) throws IOException {
        Response response = getResponse(url);
        if (response.code() != 200) {
            System.out.println("Something went wrong while fetching a object (" + clazz.getName() + ")! Response: " + response.body().string());
            return null;
        }

        String stringResponse = response.body().string();
        return GSON.fromJson(new JsonParser().parse(stringResponse), clazz);
    }

}
