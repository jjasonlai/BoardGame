package Cards;

import BoardEntities.*;

public class WeaponCard extends Card {

   private Weapon weapon;
    
   public WeaponCard(Weapon weapon) {
      super(weapon.getWeaponName(), CardType.Weapon);
      setIndex(weapon.getWeaponNum());
   }

   public Weapon getWeapon() {
      return weapon;
   }

   public void setWeapon(Weapon weapon) {
      this.weapon = weapon;
   }
}
