package view.utils;

import controller.menu_controllers.GameMenuController;
import view.ParsedLine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameUtils {


    public static void showMap(ParsedLine parsedLine) {
        String X = null, Y = null;
        Pattern patternForCheckStrIsNumber = Pattern.compile("\\d+$");
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-x":
                    X = argument;
                    break;
                case "-y":
                    Y = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "show map -x [x] -y [y]");
                    return;
            }
        }
        if (!patternForCheckStrIsNumber.matcher(X).matches() || !patternForCheckStrIsNumber.matcher(Y).matches()){
            System.out.println("Error: location should be number");
            return;
        }
        int x = Integer.parseInt(X), y = Integer.parseInt(Y);
        switch (GameMenuController.showMap(x, y)) {
            case OK:
                break;
            case INVALID_LOCATION:
                System.out.println("Error: please enter valid location");
                break;

        }



    }
}
