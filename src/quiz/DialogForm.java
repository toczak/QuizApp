package quiz;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogForm {

    public static String showAndGetString(String text) {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Quiz");
        dialog.setHeaderText(text);
        Optional<String> result;
        String value = "";
        boolean isString;

        do {
            isString = true;
            result = dialog.showAndWait();

            if (result.isPresent() && result.get().length() > 0) {
                value = result.get();
            } else {
                isString = false;
            }
        } while (!isString);
        return value;

    }
}
