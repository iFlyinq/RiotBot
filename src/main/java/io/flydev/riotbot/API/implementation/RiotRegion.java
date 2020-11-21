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

package io.flydev.riotbot.API.implementation;

import io.flydev.riotbot.commands.RiotUsageParser;

import java.util.Arrays;

/**
 * The enum Riot region.
 */
public enum RiotRegion {

    /**
     * Euw 1 riot region.
     */
    BR1(new String[0], "https://br1.api.riotgames.com"),
    /**
     * The Eun 1.
     */
    EUN1(new String[]{"EUN", "EUN1", "NE", "ENE"}, "https://eun1.api.riotgames.com"),
    /**
     * The Euw 1.
     */
    EUW1(new String[]{"EUW", "EUW1"}, "https://euw1.api.riotgames.com"),
    /**
     * The Undefined.
     */
    UNDEFINED(new String[0], null);

    private String[] aliases;
    private String baseUrl;

    RiotRegion(String[] aliases, String baseUrl) {
        this.aliases = aliases;
        this.baseUrl = baseUrl;
    }

    /**
     * Parse riot region.
     *
     * @param regionName the region name
     * @return the riot region
     */
    @RiotUsageParser
    public static RiotRegion parse(String regionName) {
        return Arrays.stream(values())
                .filter(region -> region.name().equalsIgnoreCase(regionName) || Arrays.asList(region.aliases).contains(regionName)).findFirst()
                .orElse(null);
    }

    /**
     * Gets base url.
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }
}
