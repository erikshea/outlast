package fxml;


import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public final class ActionTooltip {

    private ActionTooltip() {}

    public static void setTooltip(Node node, Tooltip tt) {
        Tooltip.install(node, tt);
        tt.setStyle("-fx-show-delay: 50ms;");
    }

    // only here for the FXMLLoader
    @Deprecated
    public static Tooltip getTooltip(Node node) {
        throw new UnsupportedOperationException();
    }

}