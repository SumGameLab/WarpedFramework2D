package warped.application.state;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import warped.WarpedProperties;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class WarpedCollisionManager {
	
	/* WarpedCollisionManager provides passive collision to groups,
	 * Passive collision is updated in parallel with object positions so it is not exact.
	 * Although not exact it is efficient and good enough for most purposes. 
	 * Update frequency can be set between 10hz - 60hz.
	 * More frequent checks will consume more system resources but it will increase the precision of collisions.
	 * Less frequent checks will use less resources but increases the chance of collisions to be missed. 
	 * Collision can be set to quick, or precise.
	 * Quick collision is based on the rectangular bounds of the rasters, if the bounds overlap a hit() will result on all objects involved with the collision.
	 * Precise collision checks for pixel overlap, (pixel perfect collision). Collision will result in a hit() on all objects involved with the collision.
	 * Collisions groups are checked subsequent to each other, this means each group added will decrease the overall accuracy of collisions detected by the manager.
	 * Accuracy can be improved by increasing the frequency of collision checks or changing the collision detection to quick.	 *  
	 * */

	private static final int MIN_FREQUENCY = 10;
	private static final int MAX_FREQUENCY = 60;
	
	private static Timer collisionTimer = new Timer("Timer Thread : Passive Collision");
	
	private static TimerTask updateInternalCollisions = new TimerTask() {public void run() {updateInternalCollisionPrecise();}};
	private static TimerTask updateCrossGroupCollisions = new TimerTask() {public void run() {updateCrossGroupCollisionPrecise();}};
	
	static {
		collisionTimer.scheduleAtFixedRate(updateCrossGroupCollisions, 0, UtilsMath.convertHzToMillis(30));
		collisionTimer.scheduleAtFixedRate(updateInternalCollisions, 0, UtilsMath.convertHzToMillis(30));
	}
	
	private static boolean internalPrecision   = true;
	private static boolean crossGroupPrecision = true;
	private static int internalFrequency	   = 30;
	private static int crossGroupFrequency 	   = 30;
	
	private static ArrayList<WarpedGroup<?>> internalGroups = new ArrayList<>();
	private static ArrayList<WarpedGroup<?>[]> crossGroups = new ArrayList<>();
	
	
	/**Set the frequency of internal collision checks.
	 * @param frequency - the frequency in Hz of updates.
	 * @apiNote will fail if the frequency is out of bounds {@code MIN_FREQUENCY} < frequency < {@code MAX_FREQUENCY}
	 * @author 5som3*/
	public static void setInternalCollisionFrequency(int frequency) {
		if(frequency < MIN_FREQUENCY) {
			Console.err("WarpedCollisionManager -> setCollisionFrequency() -> frequency is too small : " + frequency + ", will be set to the minimum : " + MIN_FREQUENCY);
			frequency = MIN_FREQUENCY;
		} else if(frequency > MAX_FREQUENCY) {
			Console.err("WarpedCollisionManager -> setCollisionFrequency() -> frequency is too large : " + frequency + ", will be set to the maximum : " + MAX_FREQUENCY);
			frequency = MAX_FREQUENCY;
		} 
		
		internalFrequency = frequency;
		setInternalCollisionMode(internalPrecision);
	}
	
	/**Set the frequency of internal collision checks.
	 * @param frequency - the frequency in Hz of updates.
	 * @apiNote will fail if the frequency is out of bounds {@code MIN_FREQUENCY} < frequency < {@code MAX_FREQUENCY}
	 * @author 5som3*/
	public static void setCrossGroupCollisionFrequency(int frequency) {
		if(frequency < MIN_FREQUENCY) {
			Console.err("WarpedCollisionManager -> setCollisionFrequency() -> frequency is too small : " + frequency + ", will be set to the minimum : " + MIN_FREQUENCY);
			frequency = MIN_FREQUENCY;
		} else if(frequency > MAX_FREQUENCY) {
			Console.err("WarpedCollisionManager -> setCollisionFrequency() -> frequency is too large : " + frequency + ", will be set to the maximum : " + MAX_FREQUENCY);
			frequency = MAX_FREQUENCY;
		} 
		
		crossGroupFrequency = frequency;
		setCrossGroupCollisionMode(crossGroupPrecision);		
	}
	
	/**Set the mode of internal collision.
	 * @param isPrecise - if true internal collision will use pixel perfect collision, else it will use rectangular bounded collision.
	 * @author 5som3*/
	public static void setInternalCollisionMode(boolean isPrecise) {
		internalPrecision = isPrecise;
		updateInternalCollisions.cancel();
		if(isPrecise) updateInternalCollisions = new TimerTask() {public void run() {updateInternalCollisionPrecise();}};
		else updateInternalCollisions = new TimerTask() {public void run() {updateInternalCollisionQuick();}};
		collisionTimer.scheduleAtFixedRate(updateInternalCollisions, 0, UtilsMath.convertHzToMillis(internalFrequency));
	}
	
	/**Set the mode of cross group collision.
	 * @param isPrecise - if true internal collision will use pixel perfect collision, else it will use rectangular bounded collision.
	 * @apiNote Using precise collision for large sets of cross groups could consume a lot of system resources.
	 * @author 5som3*/
	public static void setCrossGroupCollisionMode(boolean isPrecise) {
		crossGroupPrecision = isPrecise;
		updateCrossGroupCollisions.cancel();
		if(isPrecise) updateCrossGroupCollisions = new TimerTask() {public void run() {updateCrossGroupCollisionPrecise();}};
		else updateCrossGroupCollisions = new TimerTask() {public void run() {updateCrossGroupCollisionQuick();}};
		collisionTimer.scheduleAtFixedRate(updateCrossGroupCollisions, 0, UtilsMath.convertHzToMillis(crossGroupFrequency));
	}
	
	/**Add collision between two groups. (Does not include internal collisions within a group)
	 * @param groups - a list of groups to add collision between.
	 * @return int - the index of the group. You will need to keep this index if you intend to remove the collision groups later.
	 * @implNote Checks for collisions between objects in the first group and objects in all subsequent groups.
	 * @implNote Subsequent groups are not check for collision against each other
	 * @implNote Does not check for collision within a group, only checks for collisions between objects in one group and another.
	 * @implNote Collisions will become less accurate with every group added, this is due to parallel processing of positions and collisions.
	 * @apiNote For internal passive collision addInternalPassiveCollision.
	 * @apiNote Uses pixel perfect collision by default, change to quick collision for faster (but not pixel perfect) collision. 
	 * @apiNote Example use case : Check a group of projectiles for collision against any number of other groups.
	 * @author 5som3*/
	public static int addCrossGroupCollision(WarpedGroup<?>... groups) {
		int index = crossGroups.size();
		
		for(int i = 0; i < groups.length; i++) {
			for(int j = 0; j < groups.length; j++) {
				if(j <= i) continue; //Skip groups already checked
				if(groups[i].getGroupID().isEqual(groups[j].getGroupID())) {
					Console.err("WarpedCollisionManager -> addCrossGroupCollision() -> group list contains the same group more than once, for internal group collisions use addInternalPassiveCollision");
					return -1;
				}
			}
		}
				
		crossGroups.add(groups);
		return index;
	}
	
	/**Add a group to apply internal collision to its members.
	 * @param group - the group to add collision to.
	 * @implNote Collision will occur when any objects in the group have overlapping pixels (pixels perfect collision)
	 * @apiNote Uses pixel perfect collision by default, change to quick collision for faster (but not pixel perfect) collision.
	 * @author 5som3*/
	public static int addInternalCollisionGroups(WarpedGroup<?> group) {
		int index = internalGroups.size();
		
		for(int i = 0; i < internalGroups.size(); i++) {
			if(group.getGroupID().isEqual(internalGroups.get(i).getGroupID())) {
				Console.err("WarpedCollisionManager -> addInternalCollisionGroups() -> group is already colliding");
				return -1;
			}
		}
		
		internalGroups.add(group);
		return index; 
	}
	
	/**Remove collision from the group at the specified index.
	 * @param index - the index of the group to removed (this is the index returned when the group is added)
	 * @apiNote will return with error if index is out of bounds. 
	 * @author 5som3*/
	public static void removeInternalCollision(int index) {
		if(index < 0 || index > internalGroups.size()) {
			Console.err("WarpedCollisionManager -> removeInternalCollision() -> index is out of bounds : " + index);
			return;
		} else internalGroups.remove(index);	
	}
	
	/**Remove cross group collision from the specified set of groups.
	 * @param index - the index of the set to remove ( this is the index returned when the set cross group collision was added)
	 * @apiNote will return with error if the index is out of bounds.
	 * @author 5som3*/
	public static void removeCrossGroupCollision(int index) {
		if(index < 0 || index > crossGroups.size()) {
			Console.err("WarpedCollisionManager -> removeInternalCollision() -> index is out of bounds : " + index);
			return;
		} else internalGroups.remove(index);
	}
	
	/**Update any internal passive collision
	 * @implNote Uses pixel perfect collision 
	 * @implNote For each group added collision will be checked internally between the objects.
	 * @implNote Collision is not checked between members of different groups. 
	 * @author 5som3*/
	private static void updateInternalCollisionPrecise() {
		for(int i = 0; i < internalGroups.size(); i++) {
			ArrayList<? extends WarpedObject> group = internalGroups.get(i).getMembers();
			for(int j = 0; j < group.size(); j++) {
				for(int k = 0; k < group.size(); k++) {
					if(k <= j) continue;
					WarpedObject memberA = group.get(j);
					WarpedObject memberB = group.get(k);
					if(!memberA.isSolid() || !memberB.isSolid()) continue;
					if(collisionExact(memberA, memberB)) {
						memberA.hit(memberB);
						memberB.hit(memberA);
					}
				}	
			}
		}
	}
	
	/**Update any internal passive collision
	 * @implNote Uses pixel perfect collision 
	 * @implNote For each group added collision will be checked internally between the objects.
	 * @implNote Collision is not checked between members of different groups. 
	 * @author 5som3*/
	private static void updateInternalCollisionQuick() {
		for(int i = 0; i < internalGroups.size(); i++) {
			ArrayList<? extends WarpedObject> group = internalGroups.get(i).getMembers();
			for(int j = 0; j < group.size(); j++) {
				for(int k = 0; k < group.size(); k++) {
					if(k <= j) continue;
					WarpedObject memberA = group.get(j);
					WarpedObject memberB = group.get(k);
					if(!memberA.isSolid() || !memberB.isSolid()) continue;
					if(collisionQuick(memberA, memberB)) {
						memberA.hit(memberB);;
						memberB.hit(memberA);
					}
				}	
			}
		}
	}
	
	
	/**Update any cross group passive collision
	 * @implNote For each collision set; checks for collisionQuick() between the objects in the first group and all subsequent groups in the set. 
	 * @author 5som3*/
	private static void updateCrossGroupCollisionPrecise() {
		for(int i = 0; i < crossGroups.size(); i++) {
			WarpedGroup<? extends WarpedObject>[] collisionSet = crossGroups.get(i);
			WarpedGroup<? extends WarpedObject> primaryGroup = collisionSet[0];
			for(int j = 0; j < primaryGroup.size(); j++) {
				WarpedObject memberA = primaryGroup.getMember(j);
				if(!memberA.isSolid()) continue;
				for(int k = 1; k < collisionSet.length; k++) {
					for(int l = 0; l < collisionSet[k].size(); l++) {
						WarpedObject memberB = collisionSet[k].getMember(l);
						if(!memberB.isSolid()) continue;
						if(collisionExact(memberA, memberB)) {
							memberA.hit(memberB);
							memberB.hit(memberA);
						}
					}
				}
			}
		}
	}
	
	
	/**Update any cross group passive collision
	 * @implNote For each collision set; checks for collisionQuick() between the objects in the first group and all subsequent groups in the set. 
	 * @author 5som3*/
	private static void updateCrossGroupCollisionQuick() {
		for(int i = 0; i < crossGroups.size(); i++) {
			WarpedGroup<? extends WarpedObject>[] collisionSet = crossGroups.get(i);
			WarpedGroup<? extends WarpedObject> primaryGroup = collisionSet[0];
			for(int j = 0; j < primaryGroup.size(); j++) {
				WarpedObject memberA = primaryGroup.getMember(j);
				if(!memberA.isSolid()) continue;
				for(int k = 1; k < collisionSet.length; k++) {
					for(int l = 0; l < collisionSet[k].size(); l++) {
						WarpedObject memberB = collisionSet[k].getMember(l);
						if(!memberB.isSolid()) continue;
						if(collisionQuick(memberA, memberB)) {
							memberA.hit(memberB);
							memberB.hit(memberA);
						}
					}
				}
			}
		}
	}
	
	
	/**Check if the objects rectangle bounds overlap.
	 * @param a - one of the objects to compare.
	 * @param b - the other object to compare against
	 * @return boolean - return true if a the objects rectangular bounds overlap.
	 * @implNote - Example, if both object rasters were filled with a circle collision would occur when the rectangular bounds of the circles overlap.
	 * @author 5som3*/
	private static boolean collisionQuick(WarpedObject a, WarpedObject b) {
		if(a.x() + a.getWidth()  < b.x()  || //The right of A is to the left of B
		   a.y() + a.getHeight() < b.y()  || //The bottom of A is above the top of B
		   a.x() >= b.x() + b.getWidth()  || //The left of A is to the right of B
		   a.y() >= b.y() + b.getHeight()) //The top of A is bellow the bottom of B
			return false; else return true;			
	}
	
	/**This method is executed by the CollisionManager when checking for collisions;
	 * @param a - one of the objects to check for collision.
	 * @param b - the other object to compare with a.
	 * @return boolean - true if any pixels of the two objects overlap.
	 * @implNote - first checks collisionQuick, then checks if the rasters have any overlapping pixels
	 * @apiNote - For large rasters this could be much slower than collisionQuick()
	 * @author 5som3*/
	private static boolean collisionExact(WarpedObject a, WarpedObject b) {
		if(collisionQuick(a,b)) {
			
			int a1x = (int)a.x();
			int a1y = (int)a.y();// top left corner of a
			int a2x = (int)a.x() + a.getWidth();
			int a2y = (int)a.y() + a.getHeight(); //botom right corner of a
			
			int b1x = (int)b.x();
			int b1y = (int)b.y();//top left corner of b
			int b2x = (int)b.x() + b.getWidth();
			int b2y = (int)b.y() + b.getHeight(); //bottom right corner of b
			
			int c1x = a1x >= b1x ? a1x : b1x; 
			int c1y = a1y >= b1y ? a1y : b1y; //Top left corner of intersection in global space
			int c2x = a2x <= b2x ? a2x : b2x;
			int c2y = a2y <= b2y ? a2y : b2y; //Bottom right corner of intersection in global space
			
			int intersectionWidth = c2x - c1x;
			int intersectionHeight = c2y - c1y;
			
			int ca1x = c1x - a1x;
			int ca1y = c1y - a1y; //Top left corner of intersection relative to A raster
			//int ca2x = ca1x + intersectionWidth;
			//int ca2y = ca1y + intersectionHeight; //Bottom right corner of intersection relative to A Raster
			
			int cb1x = c1x - b1x;
			int cb1y = c1y - b1y;//Top left corner of intersection relative to B raster
			//int cb2x = cb1x + intersectionWidth;
			//int cb2y = cb1y + intersectionHeight;//Bottom right corner of intersection relative to A Raster
			
			int ax = ca1x;
			int ay = ca1y;
			int bx = cb1x;
			int by = cb1y;
			
			for(int y = 0; y < intersectionHeight - 1; y++) { //Compare pixels from A and B rasters	
				ax = ca1x;
				bx = cb1x;
				for(int x = 0; x < intersectionWidth - 1; x++) {
					
					int aAlpha = UtilsImage.getAlpha(a.raster().getRGB(ax, ay));
					int bAlpha = UtilsImage.getAlpha(b.raster().getRGB(bx, by));
					
					if(aAlpha > WarpedProperties.ALPHA_THRESHOLD && bAlpha > WarpedProperties.ALPHA_THRESHOLD) return true;
					
					ax++;
					bx++;
				}
				ay++;
				by++;
			}
			return false; //No pixel overlap
		} else return false;
	}	
	
	
	
}
