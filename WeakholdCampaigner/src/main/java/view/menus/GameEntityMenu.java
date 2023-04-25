package view.menus;

import model.attributes.Attribute;
import model.attributes.building_attributes.CreateUnit;
import model.attributes.building_attributes.HasHP;
import model.attributes.unit_attributes.CloseCombat;
import model.game_entities.Building;
import model.game_entities.GameEntity;
import model.game_entities.Unit;
import view.Command;
import view.utils.GameEntityUtils;
import view.utils.MenuUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class GameEntityMenu extends AppMenu{
    private GameEntityMenu(ArrayList<Command> commands, Scanner scanner, MenuName menuName) {
        super(commands, scanner, menuName);
    }

    public static AppMenu getGameEntityMenu(GameEntity gameEntity, Scanner scanner) {
        ArrayList<Command> commands = new ArrayList<>();

        if (gameEntity instanceof Unit) {
            commands.add(new Command("unit", "move_to", GameEntityUtils::moveUnit));
            commands.add(new Command("unit", "patrol", GameEntityUtils::patrolUnit));
            commands.add(new Command("set", "stance", GameEntityUtils::setStance));

            for (Attribute attribute :
                    gameEntity.getAttributes()) {
                if (attribute instanceof CloseCombat) {
                    commands.add(new Command("attack", "melee", GameEntityUtils::attack));
                }
            }
        }

        if (gameEntity instanceof Building) {
            for (Attribute attribute :
                    gameEntity.getAttributes()) {
                if (attribute instanceof CreateUnit)
                    commands.add(new Command("create", "unit", GameEntityUtils::createUnit));
                else if (attribute instanceof HasHP)
                    commands.add(new Command("repair", null, GameEntityUtils::repair));
            }
        }

        commands.add(new Command("show", "current_menu", MenuUtils::showCurrentMenu));
        commands.add(new Command("exit", "entity_menu", GameEntityUtils::exitEntityMenu));
        //TODO: instead of many exit/enter menu commands, only have one in Utils.

        return new AppMenu(commands, scanner, MenuName.ENTITY_MENU);
    }
}
