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

import io.flydev.riotbot.API.implementation.RiotRegion;
import io.flydev.riotbot.API.interfaces.RiotRequest;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Riot object.
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("ALL")
public abstract class RiotObject<T> implements Serializable {

    private transient static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private transient final RiotRequest riotRequest;
    private transient final HashMap<String, T> dataMap = new HashMap<>();
    private Date fetchedDate = new Date();

    /**
     * Instantiates a new Riot object.
     */
    public RiotObject() {
        this.riotRequest = this.getClass().getDeclaredAnnotation(RiotRequest.class);
    }

    /**
     * Gets or create.
     *
     * @param region The API Region
     * @param params Parameters for the endpoint
     * @return the or create
     */
    @SneakyThrows
    @SuppressWarnings("SuspiciousMethodCalls")
    public T getOrCreate(RiotRegion region, Object... params) {
        String dataKey = region.name() + "-" + params[0];
        String requestUrl = region.getBaseUrl() + String.format(riotRequest.endpoint(), params);

        if (dataMap.containsKey(dataKey)) {
            return dataMap.get(dataKey);
        } else {
            Type t = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Class<?> tClass = Class.forName(t.getTypeName());

            //Remove the data after cacheTime seconds
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    dataMap.remove(dataKey);
                }
            }, riotRequest.cacheTime(), TimeUnit.SECONDS);

            T tObject = (T) RiotAPI.getClass(requestUrl, tClass);
            dataMap.put(dataKey, tObject);
            return tObject;
        }
    }

    /**
     * Gets fetched date.
     *
     * @return the fetched date
     */
    public Date getFetchedDate() {
        return fetchedDate;
    }
}
