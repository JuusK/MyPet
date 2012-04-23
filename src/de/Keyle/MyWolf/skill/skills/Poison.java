/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyWolf
 *
 * MyWolf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyWolf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyWolf. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyWolf.skill.skills;

import de.Keyle.MyWolf.skill.MyWolfGenericSkill;
import de.Keyle.MyWolf.util.MyWolfLanguage;
import de.Keyle.MyWolf.util.MyWolfUtil;

import java.util.Random;

public class Poison extends MyWolfGenericSkill
{
    public static int ChancePerLevel = 5;
    private int ChanceToPoison = ChancePerLevel;
    private static Random random = new Random();

    public Poison()
    {
        super("Poison");
        ChanceToPoison = ChancePerLevel;
    }

    @Override
    public void upgrade()
    {
        Level++;
        ChanceToPoison = Level * ChancePerLevel;
        MWolf.sendMessageToOwner(MyWolfUtil.setColors(MyWolfLanguage.getString("Msg_PoisonChance")).replace("%wolfname%", MWolf.Name).replace("%chance%", "" + ChanceToPoison));
    }

    public boolean getPoison()
    {
        return random.nextDouble() <= ChanceToPoison / 100;
    }
}
