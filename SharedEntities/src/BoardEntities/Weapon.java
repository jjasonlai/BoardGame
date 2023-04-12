package BoardEntities;

public class Weapon {
    // static
    public static String[] weapons = "Rope Candlestick Dagger Wrench Pipe Revolver".split(" ");
    
    // attributes
    private int weaponNum = -1;
    
    public Weapon(int idx) {
       setWeaponNum(idx);
    }
    
   // getters and setters
   public String getWeaponName() {
      if (weaponNum >= 0 && weaponNum < weapons.length) {
         return weapons[weaponNum];
      }
      else return null;

   }   
   public int getWeaponNum() {
      return weaponNum;
   }
   
   private void setWeaponNum(int weaponNum) {
      this.weaponNum = weaponNum;
   }
}
