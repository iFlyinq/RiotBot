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

package io.flydev.riotbot.commands.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.flydev.riotbot.API.implementation.RiotRegion;
import io.flydev.riotbot.commands.RiotCommand;
import io.flydev.riotbot.commands.RiotCommandExecution;
import io.flydev.riotbot.commands.RiotCommandInformation;
import io.flydev.riotbot.objects.riot.Summoner;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

/**
 * This command will fetch the basic object 'Summoner'
 */
@RiotCommandInformation(command = "summoner", usage = "<name> (region=EUW)")
public class SummonerCommand extends RiotCommand {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void execute(RiotCommandExecution execution) {
        String summonerName = execution.getArgument("name");
        RiotRegion riotRegion = execution.getArgument("region", RiotRegion.class);

        Summoner summoner = Summoner.get(riotRegion, summonerName);
        if (summoner == null) execution.fail("I couldn't find any summoner with the name %s in the region %s!",
                summonerName, riotRegion);

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Summoner information: " + summoner.getName())
                .setColor(Color.decode("#B44AB1"))
                .addField("Level", summoner.getSummonerLevel() + "", true)
                .setThumbnail("https://ddragon.leagueoflegends.com/cdn/10.23.1/img/profileicon/" + summoner.getProfileIconId() + ".png")
                .setTimestamp(summoner.getFetchedDate().toInstant());

        execution.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
