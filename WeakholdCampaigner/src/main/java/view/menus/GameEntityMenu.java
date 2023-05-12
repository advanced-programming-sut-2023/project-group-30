package view.menus;

import model.attributes.Attribute;
import model.attributes.building_attributes.*;
import model.attributes.building_attributes.Process;
import model.attributes.unit_attributes.*;
import model.game.game_entities.Building;
import model.game.game_entities.GameEntity;
import model.game.game_entities.Unit;
import view.Command;
import view.utils.GameEntityUtils;
import view.utils.GameUtils;

import java.util.ArrayList;

public class GameEntityMenu extends AbstractMenu {
    private GameEntityMenu(ArrayList<Command> commands, MenuName menuName) {
        super(commands, menuName);
    }

    public static GameEntityMenu getGameEntityMenu(final GameEntity gameEntity) {
        ArrayList<Command> commands = new ArrayList<>(getCommonCommands());

        if (gameEntity instanceof Unit) {
            commands.add(new Command("unit", "move_to", GameEntityUtils::moveUnit));
            commands.add(new Command("unit", "patrol", GameEntityUtils::patrolUnit));
            commands.add(new Command("unit", "halt", GameEntityUtils::halt));
            commands.add(new Command("set", "stance", GameEntityUtils::setStance));
            commands.add(new Command("unit", "disband", GameEntityUtils::disbandUnit));

            for (Attribute attribute :
                    gameEntity.getAttributes()) {
                if (attribute instanceof CloseCombat)
                    commands.add(new Command("attack", "melee", GameEntityUtils::attack));
                else if (attribute instanceof RangedAttack)
                    commands.add(new Command("attack", "ranged", GameEntityUtils::attack));
                else if (attribute instanceof PourOil)
                    commands.add(new Command("pour", "oil", GameEntityUtils::pourOil));
                else if (attribute instanceof DigTunnel)
                    commands.add(new Command("dig", "tunnel", GameEntityUtils::digTunnel));
                else if (attribute instanceof BuildEquipment)
                    commands.add(new Command("build", "equipment", GameEntityUtils::buildEquipment));
            }
        }

        if (gameEntity instanceof Building) {
            commands.add(new Command("repair", null, GameEntityUtils::repair));
            for (Attribute attribute :
                    gameEntity.getAttributes()) {
                if (attribute instanceof CreateUnit)
                    commands.add(new Command("create", "unit", GameEntityUtils::createUnit));
                else if (attribute instanceof ChangeTaxRate)
                    commands.add(new Command("tax", "rate", GameUtils::taxRate));
                else if (attribute instanceof Shop)
                    commands.add(new Command("enter", "shop_menu", GameUtils::enterShopMenu));
                else if (attribute instanceof DrinkServing)
                    commands.add(new Command("serve", "drink", GameEntityUtils::serveDrink));
                else if (attribute instanceof Process)
                    commands.add(new Command("process", null, GameEntityUtils::process));
                else if (attribute instanceof Capacity)
                    commands.add(new Command("show", "condition", GameEntityUtils::showCondition));
                else if (attribute instanceof ChangeFoodRate)
                    commands.add(new Command("food", "rate", GameUtils::setFoodRate));


            }
        }

        commands.add(new Command("exit", "entity_menu", GameEntityUtils::exitEntityMenu));
        //TODO: combine the many exit/enter menu commands into one thing.

        return new GameEntityMenu(commands, MenuName.ENTITY_MENU);
    }
}
