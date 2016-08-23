/*
 * This file is part of mypet-api_main
 *
 * Copyright (C) 2011-2016 Keyle
 * mypet-api_main is licensed under the GNU Lesser General Public License.
 *
 * mypet-api_main is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mypet-api_main is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.api.util.animation;

import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.util.location.LocationHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public abstract class Animation {
    int taskID = -1;
    protected int frame = 0;
    protected int length = 0;
    protected int loops = 0;
    protected LocationHolder locationHolder;

    public Animation(int length, LocationHolder locationHolder) {
        this.length = length;
        this.locationHolder = locationHolder;
    }

    public abstract void tick(int frame, Location location);

    public void reset() {
        frame = 0;
    }

    public void once() {
        if (!running()) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MyPetApi.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (locationHolder.isValid()) {
                        tick(frame, locationHolder.getLocation());
                        if (++frame >= length) {
                            stop();
                        }
                    } else {
                        stop();
                    }
                }
            }, 0, 1);
        }
    }

    public void stop() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
            onStop();
        }
    }

    public boolean running() {
        return taskID != -1;
    }

    public void loop() {
        if (!running()) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MyPetApi.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (locationHolder.isValid()) {
                        tick(frame, locationHolder.getLocation());
                        if (++frame >= length) {
                            reset();
                        }
                    } else {
                        stop();
                    }
                }
            }, 0, 1);
        }
    }

    public void loop(int quantity) {
        if (!running()) {
            this.loops = quantity;
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MyPetApi.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (locationHolder.isValid()) {
                        tick(frame, locationHolder.getLocation());
                        if (++frame >= length) {
                            if (--Animation.this.loops > 0) {
                                reset();
                            } else {
                                stop();
                            }
                        }
                    } else {
                        stop();
                    }
                }
            }, 0, 1);
        }
    }

    public void onStop() {
    }
}