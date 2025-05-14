package warped.application.state.managers.gameObjectManagers;

@FunctionalInterface
public interface WarpedObjectCheckAction<T> {
	public boolean action(T obj);
}
