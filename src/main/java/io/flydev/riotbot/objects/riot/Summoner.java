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

package io.flydev.riotbot.objects.riot;

import io.flydev.riotbot.API.RiotObject;
import io.flydev.riotbot.API.implementation.RiotRegion;
import io.flydev.riotbot.API.interfaces.RiotRequest;

/**
 * The type Summoner.
 */
@RiotRequest(endpoint = "/lol/summoner/v4/summoners/by-name/%s", cacheTime = 120)
public class Summoner extends RiotObject<Summoner> {

    private static transient final Summoner summonerBluePrint = new Summoner();

    private String id, accountId, puuid, name;
    private int profileIconId, summonerLevel;
    private long revisionDate;

    /**
     * Get summoner.
     *
     * @param region the region
     * @param name   the name
     * @return the summoner
     */
    public static Summoner get(RiotRegion region, String name) {
        return summonerBluePrint.getOrCreate(region, name.toLowerCase());
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets account id.
     *
     * @return the account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Gets puuid.
     *
     * @return the puuid
     */
    public String getPuuid() {
        return puuid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets profile icon id.
     *
     * @return the profile icon id
     */
    public int getProfileIconId() {
        return profileIconId;
    }

    /**
     * Gets summoner level.
     *
     * @return the summoner level
     */
    public int getSummonerLevel() {
        return summonerLevel;
    }

    /**
     * Gets revision date.
     *
     * @return the revision date
     */
    public long getRevisionDate() {
        return revisionDate;
    }

}
