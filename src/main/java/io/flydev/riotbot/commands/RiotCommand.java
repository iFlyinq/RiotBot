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

import java.util.HashMap;
import java.util.Objects;

/**
 * The type Riot command.
 */
public abstract class RiotCommand {

    private static HashMap<String, RiotCommand> riotCommands = new HashMap<>();

    /**
     * The Command information.
     */
    public RiotCommandInformation commandInformation;

    /**
     * Instantiates a new Riot command.
     */
    public RiotCommand() {
        this.commandInformation = this.getClass().getDeclaredAnnotation(RiotCommandInformation.class);
        Objects.requireNonNull(this.commandInformation, "The command information of " + this.getClass().getName() + " is null!");

        riotCommands.put(commandInformation.command(), this);
        for (String alias : commandInformation.aliases()) riotCommands.putIfAbsent(alias, this);
    }

    /**
     * Find riot command.
     *
     * @param command the command
     * @return the riot command
     */
    public static RiotCommand find(String command) {
        return riotCommands.get(command.toLowerCase());
    }

    /**
     * Execute.
     *
     * @param execution the execution
     */
    public abstract void execute(RiotCommandExecution execution);

    /**
     * Gets command information.
     *
     * @return the command information
     */
    public RiotCommandInformation getCommandInformation() {
        return commandInformation;
    }
}
