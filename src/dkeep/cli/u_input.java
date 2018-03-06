package dkeep.cli;
import java.util.Random;
import java.util.Scanner;
import java.lang.String;
import java.util.ArrayList;
import dkeep.logic.Hero;
import dkeep.logic.Guard;
import dkeep.logic.Ogre;
import dkeep.logic.Map;
import dkeep.logic.GameState;


public class u_input 
{
	public static void main(String[] args) 
	{
		String move = "start";
		Scanner in = new Scanner(System.in);
		Hero hero = new Hero(1,1);
		Guard guard = new Guard(8,1, Guard.Guard_Type.Drunken);
		Map map = new Map("1");
		map.print_map();
		GameState gs = new GameState(1);
		ArrayList<Ogre> Ogres = new ArrayList<Ogre>();
		int i;
		
		while(!move.equalsIgnoreCase("exit") && gs.level_no == 1)
		{
			move =  in.next();
			hero.heroMove(move);
			hero.action(map, gs, guard);
				
			map.level[hero.y][hero.x] = "H";
			
			if(guard.asleep)
				map.level[guard.y][guard.x] = "g";
			else
				map.level[guard.y][guard.x] = "G";
			
			map.print_map();
			
			if(gs.test_collision(map.level, "G", hero))
			{
				System.out.print("Game Over\n");
				in.close();
				return; 
			}
		}
		
		if(gs.level_no == 2)
			map.next_level();
		else
		{
			System.out.print("Forced exit\n");
			in.close();
			return;
		}
		
		map.print_map();
		
		Random rand = new Random();
		Ogre ogre = new Ogre(4, 1);
		Ogre ogre2 = new Ogre(7,5);
		Ogre ogre3 = new Ogre(1, 4);
		
		Ogres.add(ogre);
		Ogres.add(ogre2);
		Ogres.add(ogre3);
		
		hero.x = 1;
		hero.y = 7;
		hero.ny = 7;
		hero.nx = 1;
		hero.armed = true;
		
		while(!move.equalsIgnoreCase("exit") && gs.level_no == 2)
		{
			move = in.next();
			hero.heroMove(move);
			hero.action(map, gs);
					
			for(i = 0; i < Ogres.size(); i++)
				{
					if(Ogres.get(i).turns_stunned == 0)
					{
						Ogres.get(i).move( rand.nextInt(4) + 1);
						Ogres.get(i).action(map);
						Ogres.get(i).swing_club(rand.nextInt(4) + 1);
						Ogres.get(i).smash(map);
					}
					else
					{
						if(Ogres.get(i).turns_stunned == 1)
						{
							Ogres.get(i).turns_stunned++;
							Ogres.get(i).swing_club(rand.nextInt(4) + 1);
							Ogres.get(i).smash(map);
						}
						else
						{
							Ogres.get(i).turns_stunned = 0;
							Ogres.get(i).swing_club(rand.nextInt(4) + 1);
							Ogres.get(i).smash(map);
						}
					}
						
				}
						
			if(map.key)
				map.level[hero.y][hero.x] = "K";
			else
				map.level[hero.y][hero.x] = "A";
		
			map.print_map();
			
			if(gs.test_collision(map.level, "O", hero) 
					&& hero.armed)
			{
				for(i = 0; i < Ogres.size(); i++)
				{
					if(Ogres.get(i).turns_stunned == 0)
					{
						Ogres.get(i).turns_stunned++;
						map.level[Ogres.get(i).y][Ogres.get(i).x] = "8";
						break;
					}
			
				}
			}
			
			if(gs.test_collision(map.level, "*", hero) 
					|| gs.test_collision(map.level, "$", hero))
			{
				System.out.print("Game Over\n");
				in.close();
				return;
			}
			
			if(gs.victory)
			{
				System.out.print("Victory!\n");
				in.close();
				return;
			}
		}
		
		in.close();
	}

}

