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

package io.flydev.riotbot.commands;

import io.flydev.riotbot.commands.exceptions.RiotCommandException;
import io.flydev.riotbot.objects.RiotGuild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Riot bot command listener.
 */
public class RiotCommandListener extends ListenerAdapter {

    private static String[] getArguments(String args) {
        List<String> arguments = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args);
        while (m.find())
            arguments.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.
        return arguments.toArray(new String[0]);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();                    //Raw mentions, required for mention prefix check
        String[] args = getArguments(event.getMessage().getContentDisplay());   //Makes mentions nice

        if (event.isFromGuild()) {
            RiotGuild riotGuild = RiotGuild.get(event.getGuild());
            if (!message.startsWith(riotGuild.getPrefix()) && !message.startsWith(event.getJDA().getSelfUser().getAsMention()))
                return;
            else if (message.startsWith(riotGuild.getPrefix()))
                args[0] = args[0].replaceFirst(riotGuild.getPrefix(), "");
            else args[0] = args[0].replaceFirst(event.getJDA().getSelfUser().getAsMention(), "");
        } else {
            if (!message.startsWith(RiotGuild.defaultPrefix) && !message.startsWith(event.getJDA().getSelfUser().getAsMention()))
                return;
            else args[0] = args[0].replaceFirst(event.getJDA().getSelfUser().getAsMention(), "");
        }

        RiotCommand riotCommand = RiotCommand.find(args[0]);
        RiotCommandExecution execution = new RiotCommandExecution(event.getJDA(), event.getResponseNumber(),
                event.getMessage(), riotCommand);

        assert riotCommand != null;
        try {
            riotCommand.execute(execution);
        } catch (Exception e) {
            if (!(e instanceof RiotCommandException)) e.printStackTrace();
        }
    }

}
