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
				if(j < i) continue; //Skip groups already checked
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
					if(k < j) continue;
					if(collisionExact(group.get(j), group.get(k))) {
						group.get(j).hit(group.get(k));
						group.get(k).hit(group.get(j));
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
				WarpedObject member = primaryGroup.getMember(j);
				for(int k = 1; k < collisionSet.length; k++) {
					for(int l = 0; l < collisionSet[k].size(); l++) {
						if(collisionExact(member, collisionSet[k].getMember(l))) {
							member.hit(collisionSet[k].getMember(l));
							collisionSet[k].getMember(l).hit(member);
						}
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
					if(k < j) continue;
					if(collisionQuick(group.get(j), group.get(k))) {
						group.get(j).hit(group.get(k));
						group.get(k).hit(group.get(j));
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
				WarpedObject member = primaryGroup.getMember(j);
				for(int k = 1; k < collisionSet.length; k++) {
					for(int l = 0; l < collisionSet[k].size(); l++) {
						if(collisionQuick(member, collisionSet[k].getMember(l))) {
							member.hit(collisionSet[k].getMember(l));
							collisionSet[k].getMember(l).hit(member);
							Console.blueln("WarpedCollisionManager -> updateCrossGroupCollisionQuick() -> collision");
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
		if(a.getPosition().x() + a.getWidth() <= b.getPosition().x()  || //The tight of A is to the left of B
		   a.getPosition().y() + a.getHeight() <= b.getPosition().y() || //The bottom of A is above the top of B
		   a.getPosition().x() >= b.getPosition().x() + b.getWidth()  || //The left of A is to the right of B
		   a.getPosition().y() >= b.getPosition().y() + b.getHeight()) //The top of A is bellow the bottom of B
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
			int ax1; //The coordinates for the overlapping area of a's raster
			int ay1;			
			int ax2;
			int ay2;
			
			int bx1;//The coordinates for the overlapping area of b's raster
			int by1;			
			
			if(a.getPosition().x() >= b.getPosition().x()) {
				ax1 = 0;
				bx1 = (int) (a.getPosition().x() - b.getPosition().x());
			} else {
				bx1 = 0;
				ax1 = (int) (b.getPosition().x() - a.getPosition().x());
			}
			
			if(a.getPosition().y() >= b.getPosition().y()) {
				ay1 = 0;
				by1 = (int) (a.getPosition().y() - b.getPosition().y());
			} else {
				by1 = 0;
				ay1 = (int) (b.getPosition().y() - a.getPosition().y());
			}
			
			if(a.getPosition().x() + a.getWidth() <= b.getPosition().x() + b.getWidth()) {
				ax2 = a.getWidth();
			} else {
				ax2 = (int) (b.getPosition().x() + b.getWidth() - a.getPosition().x());				
			}
			
			if(a.getPosition().y() + a.getHeight() <= b.getPosition().y() + b.getHeight()) {
				ay2 = a.getHeight();
			} else {
				ay2 = (int) (b.getPosition().y() + b.getHeight() - a.getPosition().y());
			}
			
			int ax = ax1 - 1;
			int ay = ay1 - 1;
			int bx = bx1 - 1;
			int by = by1 - 1;
			
			for(int y = 0; y < ay2 - ay1; y++) {
				ay++;
				by++;
				
				for(int x = 0; x < ax2 - ax1; x++) { //Cross check each overlapping pixel, if both opacity are above threshold collision has occurred 
					ax++;
					bx++;
					
					int aAlpha = UtilsImage.getAlpha(a.raster().getRGB(ax, ay));
					int bAlpha = UtilsImage.getAlpha(b.raster().getRGB(bx, by));
					
					if(aAlpha > WarpedProperties.ALPHA_THRESHOLD && bAlpha > WarpedProperties.ALPHA_THRESHOLD) return true;	
				}
			}
			return false; //No overlapping pixels in the overlapping area
		} else return false;
	}	
	
	
	
}
