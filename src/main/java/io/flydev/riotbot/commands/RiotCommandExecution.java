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

import io.flydev.riotbot.commands.exceptions.InvalidUsageException;
import io.flydev.riotbot.commands.exceptions.RiotCommandException;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Riot command execution.
 */
public class RiotCommandExecution extends MessageReceivedEvent {

    private final RiotCommand riotCommand;
    private final String[] arguments;

    /**
     * Instantiates a new Riot command execution.
     *
     * @param api            the api
     * @param responseNumber the response number
     * @param message        the message
     * @param riotCommand    the riot command
     */
    public RiotCommandExecution(@NotNull JDA api, long responseNumber, @NotNull Message message, RiotCommand riotCommand) {
        super(api, responseNumber, message);

        this.arguments = getArguments(message.getContentDisplay());
        this.riotCommand = riotCommand;
    }

    private static String[] getArguments(String args) {
        List<String> arguments = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args);
        while (m.find())
            arguments.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.

        //Remove first one which which is the command
        arguments.remove(0);

        return arguments.toArray(new String[0]);
    }

    /**
     * Gets argument.
     *
     * @param name the name
     * @return the argument
     */
    public String getArgument(String name) {
        String usage = riotCommand.commandInformation.usage();
        String value = null;
        Matcher matcher = Pattern.compile("(<" + name + ">)|(\\(" + name + "+=+.*)\\)").matcher(usage);

        while (matcher.find()) {
            boolean required = false;
            String found = matcher.group();
            int index = Arrays.asList(usage.split(" ")).indexOf(found);
            if (found.startsWith("<")) required = true;

            if (required) {
                if (arguments.length <= index) throw new InvalidUsageException(this);
                else value = arguments[index];
            } else {
                if (arguments.length <= index) value = found.replace("(" + name + "=", "")
                        .replace(")", "");
                else value = arguments[index];
            }
        }

        return value;
    }

    /**
     * Throws an RiotCommandException with the specified message
     *
     * @param reason the reason
     * @param params the params
     */
    public void fail(String reason, Object... params) {
        throw new RiotCommandException(this, String.format(reason, params));
    }

    /**
     * Gets argument.
     *
     * @param <T>    the type parameter
     * @param name   the name
     * @param tClass the t class
     * @return the argument
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T getArgument(String name, Class<? extends T> tClass) {
        Method method;
        Optional<Method> optional = Arrays.stream(tClass.getDeclaredMethods())
                .filter(optionalMethod -> optionalMethod.isAnnotationPresent(RiotUsageParser.class))
                .findFirst();

        if (optional.isPresent()) method = optional.get();
        else method = tClass.getMethod("valueOf", String.class);

        method.setAccessible(true);
        T value = (T) method.invoke(null, getArgument(name));

        if (value == null) throw new InvalidUsageException(this);
        else return value;
    }

    /**
     * Gets string argument.
     *
     * @param position the position
     * @return the string argument
     */
    public String getRequiredStringArgument(int position) {
        if (arguments.length < position) throw new InvalidUsageException(this);
        return arguments[position - 1];
    }

    /**
     * Gets optional argument.
     *
     * @param position the position
     * @return the optional argument
     */
    public String getOptionalArgument(int position) {
        if (arguments.length < position) throw new InvalidUsageException(this);
        return arguments[position - 1];
    }

    /**
     * Gets riot command.
     *
     * @return the riot command
     */
    public RiotCommand getRiotCommand() {
        return riotCommand;
    }
}
