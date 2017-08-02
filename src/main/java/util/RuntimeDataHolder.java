package util;

//przechowuje nazwę grającego użytkownika
public class RuntimeDataHolder {

    private static RuntimeDataHolder runtimeDataHolder = null;
    private String playerName = null;

    private RuntimeDataHolder() {
    }

    public static RuntimeDataHolder getInstance() {
        if (runtimeDataHolder == null) {
            runtimeDataHolder = new RuntimeDataHolder();
        }
        return runtimeDataHolder;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
