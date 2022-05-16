package betterHorseCore;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Style;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BreedingCore extends JavaPlugin {
	@Override
	public void onEnable() {

		getServer().getPluginManager().registerEvents(new HorseBreed(), this);

	}

	public class HorseBreed implements Listener {
	     @EventHandler(priority = EventPriority.LOWEST)
	        public void onDamage(final EntityDamageByEntityEvent event) {
	            
	            Entity entity2 = event.getDamager();
	            if (!(entity2 instanceof Player)) {
	                return;
	            }
	            Player player = (Player) event.getDamager();
	            ItemStack itemInHand = null;
	            itemInHand = player.getInventory().getItemInMainHand();
	            if (itemInHand != null) {
	                //do stuff
	                if (itemInHand.getType() == Material.DIAMOND_SHOVEL)
	                {
	                    ItemMeta meta = itemInHand.getItemMeta();
	                    if (meta == null) {
	                        return;
	                    }
	                    if (meta.getCustomModelData() == 9001) {
	                        if (player.getVehicle() != null && player.getVehicle().getType() == EntityType.HORSE) {
	                            //set damage here
	                          
	                        }
	                        else {
	                            event.setDamage(0);
	                            event.setCancelled(true);
	                            
	                        }
	                    }
	                }
	            }
	        }
		@EventHandler
		public void onEntityClick(PlayerInteractEntityEvent event) {

			Player player = event.getPlayer();

			if (event.getRightClicked() instanceof Horse || event.getRightClicked() instanceof Mule){
				ItemStack itemInHand = player.getInventory().getItemInMainHand();

				if (itemInHand == null) {
					return;
				}
				LivingEntity horse = (LivingEntity) event.getRightClicked();
				double jump = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
				if (jump > 0.3) {
					horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(0.3);
				}
				if (itemInHand.getType() == Material.BOOK) {
					
					double speed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
					double health = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
					
					DecimalFormat df = new DecimalFormat("#.##");
					player.sendMessage("Speed: " + df.format(speed));
					player.sendMessage("Health: " +  df.format(health));
					player.sendMessage("Jump: " +  df.format(jump));
					itemInHand.setAmount(itemInHand.getAmount() - 1);
					event.setCancelled(true);
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void EntityBreedEvent(org.bukkit.event.entity.EntityBreedEvent event)
		{

			LivingEntity dad = event.getFather();
			if (dad.getType() != EntityType.HORSE) {
				return;
			}
			
			
			
			LivingEntity mom = event.getMother();
			LivingEntity child = event.getEntity();

			if (child instanceof Mule) {
				return;
			}
			
			double DadHealth = dad.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			double DadJump = dad.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
			double DadSpeed = dad.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();


			double MomHealth = dad.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			double MomJump = dad.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
			double MomSpeed = dad.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();

			double avgHealth = (DadHealth + MomHealth) / 2;
			double avgSpeed = (DadSpeed + MomSpeed) / 2;
			double avgJump = (DadJump + MomJump) / 2;

			double newHealth = avgHealth * 1.15;
			double newSpeed = avgSpeed * 1.15;
			double newJump = avgJump * 1.15;

			if (newHealth >= 40) {
				newHealth = 40;
			}
			if (newSpeed >= 0.35) {
				newSpeed = 0.35;
			}
			if (newJump >= 0.3) {
				newJump = 0.3;
			}

			child.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHealth);
			child.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(newJump);
			child.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);

			child.setHealth(newHealth);
			//max speed = 0.3375
			//default jump = 	0.7

		}
	}
}
