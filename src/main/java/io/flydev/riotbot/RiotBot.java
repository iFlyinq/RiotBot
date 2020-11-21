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

package io.flydev.riotbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.flydev.riotbot.commands.RiotCommandInformation;
import io.flydev.riotbot.commands.RiotCommandListener;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;

/**
 * The type Riot bot.
 */
public class RiotBot {

    private static Gson gson = new Gson();
    private static JsonObject secrets;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    @SneakyThrows
    public static void main(String[] args) {
        //Load the secrets from the local file
        secrets = gson.fromJson(new InputStreamReader(Objects.requireNonNull(RiotBot.class.getClassLoader().getResourceAsStream("secrets.json"))), JsonObject.class);

        //Build a new JDA Instance
        JDA jda = JDABuilder.createDefault(secrets.getAsJsonObject("discord").get("bot-token").getAsString())
                .addEventListeners(new RiotCommandListener())
                .build();

        //Wait till the bot is ready
        jda.awaitReady();

        //Disable the logger of Reflections
        Reflections.log = null;

        //Get all classes annotated with the @RiotCommandInformation annotation and put that into a set.
        Set<Class<?>> annotatedClasses = new Reflections("io.flydev", new TypeAnnotationsScanner())
                .getTypesAnnotatedWith(RiotCommandInformation.class, true);

        //Create a new instance of all
        for (Class<?> annotatedClass : annotatedClasses) {
            annotatedClass.newInstance();
        }
    }

    /**
     * Gets secrets.
     *
     * @return the secrets
     */
    public static JsonObject getSecrets() {
        return secrets;
    }
}