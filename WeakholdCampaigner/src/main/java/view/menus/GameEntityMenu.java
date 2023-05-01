package view.menus;

import model.attributes.Attribute;
import model.attributes.building_attributes.*;
import model.attributes.unit_attributes.*;
import model.game.game_entities.Building;
import model.game.game_entities.GameEntity;
import model.game.game_entities.Unit;
import model.game_entities.*;
import view.Command;
import view.utils.GameEntityUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class GameEntityMenu extends AbstractMenu {
    private GameEntityMenu(ArrayList<Command> commands, Scanner scanner, MenuName menuName) {
        super(commands, scanner, menuName);
    }

    public static AppMenu getGameEntityMenu(GameEntity gameEntity, Scanner scanner) {
        ArrayList<Command> commands = new ArrayList<>(getCommonCommands());

        if (gameEntity instanceof Unit) {
            commands.add(new Command("unit", "move_to", GameEntityUtils::moveUnit));
            commands.add(new Command("unit", "patrol", GameEntityUtils::patrolUnit));
            commands.add(new Command("set", "stance", GameEntityUtils::setStance));
            commands.add(new Command("unit", "disband", GameEntityUtils::disbandUnit));

            for (Attribute attribute :
                    gameEntity.getAttributes()) {
                if (attribute instanceof CloseCombat)
                    commands.add(new Command("attack", "melee", GameEntityUtils::meleeAttack));
                else if (attribute instanceof RangedAttack)
                    commands.add(new Command("attack", "ranged", GameEntityUtils::rangedAttack));
                else if (attribute instanceof PourOil)
                    commands.add(new Command("pour", "oil", GameEntityUtils::pourOil));
                else if (attribute instanceof DigTunnel)
                    commands.add(new Command("dig", "tunnel", GameEntityUtils::digTunnel));
                else if (attribute instanceof BuildEquipment)
                    commands.add(new Command("build", "equipment", GameEntityUtils::buildEquipment));
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

        commands.add(new Command("exit", "entity_menu", GameEntityUtils::exitEntityMenu));
        //TODO: combine the many exit/enter menu commands into one thing.

        return new AppMenu(commands, scanner, MenuName.ENTITY_MENU);
    }
}
