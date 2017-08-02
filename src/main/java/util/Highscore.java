package util;

import javafx.beans.property.SimpleStringProperty;

public class Highscore {

    //String z javyfx używany w tabeli w HighscoreScene
    private SimpleStringProperty player;

    //String z javyfx używany w tabeli w HighscoreScene
    private SimpleStringProperty prize;

    public Highscore(String player, String prize) {
        this.player = new SimpleStringProperty(player);
        this.prize = new SimpleStringProperty(prize);
    }

    public String getPlayer() {

        return player.get();
    }

    public SimpleStringProperty playerProperty() {
        return player;
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public String getPrize() {
        return prize.get();
    }

    public SimpleStringProperty prizeProperty() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize.set(prize);
    }

}