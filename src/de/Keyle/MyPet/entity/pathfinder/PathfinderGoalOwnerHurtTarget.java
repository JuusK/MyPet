/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.pathfinder;

import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.skill.skills.Behavior;
import de.Keyle.MyPet.util.MyPetUtil;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.PathfinderGoalTarget;
import org.bukkit.entity.Player;

public class PathfinderGoalOwnerHurtTarget extends PathfinderGoalTarget
{

    EntityTameableAnimal pet;
    EntityLiving target;
    MyPet MPet;

    public PathfinderGoalOwnerHurtTarget(MyPet MPet)
    {
        super(MPet.getPet().getHandle(), 32.0F, false);
        this.pet = MPet.getPet().getHandle();
        this.MPet = MPet;
        this.a(1);
    }

    public boolean a()
    {
        if (!this.pet.isTamed())
        {
            return false;
        }
        else
        {
            EntityLiving owner = this.pet.getOwner();

            if (owner == null)
            {
                return false;
            }
            else
            {
                if (MPet.getSkillSystem().hasSkill("Behavior"))
                {
                    Behavior behavior = (Behavior) MPet.getSkillSystem().getSkill("Behavior");
                    if (behavior.getLevel() > 0)
                    {
                        if (behavior.getBehavior() == Behavior.BehaviorState.Friendly)
                        {
                            return false;
                        }
                    }
                }
                this.target = ((EntityMyPet) this.pet).Goaltarget;
                ((EntityMyPet) this.pet).Goaltarget = null;

                if (this.target instanceof EntityPlayer)
                {
                    String playerName = ((EntityPlayer) this.target).name;
                    if (!MyPetUtil.getOfflinePlayer(playerName).isOnline())
                    {
                        this.target = null;
                        return false;
                    }
                    Player target = MyPetUtil.getOfflinePlayer(playerName).getPlayer();

                    if (MPet.getOwner().equals(target))
                    {
                        this.target = null;
                        return false;
                    }
                    else if (!MyPetUtil.canHurtFactions(MPet.getOwner().getPlayer(), target))
                    {
                        this.target = null;
                        return false;
                    }
                    else if (!MyPetUtil.canHurtTowny(MPet.getOwner().getPlayer(), target))
                    {
                        this.target = null;
                        return false;
                    }
                    else if (!MyPetUtil.canHurtWorldGuard(target))
                    {
                        this.target = null;
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public void c()
    {
        this.d.b(this.target);
        super.c();
    }
}