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

package io.flydev.riotbot.objects;

import net.dv8tion.jda.api.entities.Guild;

/**
 * The type Guild.
 */
public class RiotGuild {

    /**
     * The constant defaultPrefix.
     */
    public static final String defaultPrefix = "!";

    private String prefix = defaultPrefix;
    private long guildId;

    /**
     * Instantiates a new Riot guild.
     *
     * @param guildId the guild id
     */
    public RiotGuild(long guildId) {
        this.guildId = guildId;
    }

    /**
     * Get riot guild.
     *
     * @param guild the guild
     * @return the riot guild
     */
    public static RiotGuild get(Guild guild) {
        return get(guild.getIdLong());
    }

    /**
     * Get riot guild.
     *
     * @param idLong the id long
     * @return the riot guild
     */
    public static RiotGuild get(long idLong) {
        //TODO Make this :)
        return new RiotGuild(idLong);
    }

    /**
     * Gets prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }
}
