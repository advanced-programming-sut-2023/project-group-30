package view.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public abstract class Utils {
    protected static void invalidFormatError(String correctFormat) {
        System.out.println("Error: This command should have the following format:\n" + correctFormat);
    }

    protected static boolean hasNullString(String... strings) {
        for (String string :
                strings) {
            if (string == null) return true;
        }

        return false;
    }

    protected static boolean areIntegersOrNull(String... strings) {
        Pattern intPattern = Pattern.compile("[\\-+]?\\d+");

        for (String string :
                strings) {
            if (string == null) continue;
            if (!intPattern.matcher(string).matches()) return false;
        }

        return true;
    }

    @Nullable
    protected static HashMap<String, String> formatOptions(@NotNull HashMap<String, String> allUserInputOptions,
                                                           @NotNull String[] allMandatoryOptions,
                                                           @NotNull String[] allOptionalOptions,
                                                           @NotNull String[] allIntegerOptions) {
        //TODO: differentiate "command -u" from "command" and "command -u arg" ("-u" being an optionalOption)
        //Initialize the output:
        HashMap<String, String> output = new HashMap<>();
        for (String option :
                allMandatoryOptions) {
            output.put(option, null);
        }
        for (String option :
                allOptionalOptions) {
            output.put(option, null);
        }

        //fill the output, return null upon encountering an unidentified option:
        for (Map.Entry<String, String> entry :
                allUserInputOptions.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();

            if (output.containsKey(option)) output.replace(option, argument);
            else return null;
        }

        //assert that allMandatoryOptions are present, otherwise return null:
        for (String option :
                allMandatoryOptions)
            if (output.get(option) == null) return null;

        //assert that allIntegerOptions are Integers, otherwise return null:
        for (String option :
                allIntegerOptions)
            if (!areIntegersOrNull(output.get(option))) return null;

        return output;
    }
}
