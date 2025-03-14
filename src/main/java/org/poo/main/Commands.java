package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;

import java.util.ArrayList;
import java.util.Objects;

public class Commands {
    private String command;

    public void interpretCommand(Game game , String command, ObjectMapper mapper, ActionsInput currentAction, GameInput currentGame, ArrayNode output, Table table) {
        // if else switch
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        if (command.equals("endPlayerTurn")) {
            endPlayerTurnFunction(currentGame, player1, player2, game, table);
        } else if (command.equals("placeCard")) {
            placeCardFunction(player1, player2, game, currentAction.getHandIdx(), table, mapper, output);
        } else if (command.equals("cardUsesAttack")) {
            cardUsesAttackFunction(table, output, mapper, currentAction, game, player1, player2);
        } else if (command.equals("cardUsesAbility")) {
            cardUsesAbilityFunction(player1, player2, game, output, mapper, currentAction, table);
        } else if (command.equals("useAttackHero")) {
            useAttackHeroFunction(table, output, mapper, player1, player2, game, currentAction);
        } else if (command.equals("useHeroAbility")) {
            useHeroAbilityFunction(currentAction, table, output, mapper, player1, player2, game);
        } else if (command.equals("getCardsInHand")) {
            getCardsInHandFunction(player1, player2, currentAction.getPlayerIdx(), output, mapper);
        } else if (command.equals("getPlayerDeck")) {
            getPlayerDeckFunction(mapper, currentAction, player1, player2, currentGame, output);
        } else if (command.equals("getCardsOnTable")) {
            getCardsOnTableFunction(table, mapper, output);
        } else if (command.equals("getPlayerTurn")) {
            getPlayerTurnFunction(mapper, output, game);
        } else if (command.equals("getPlayerHero")) {
            getPlayerHeroFunction(mapper, currentAction, player1, player2, currentGame, output);
        } else if (command.equals("getCardAtPosition")) {
            getCardAtPositionFunction(output, mapper, currentAction, table);
        } else if (command.equals("getPlayerMana")) {
            getPlayerManaFunction(player1, player2, currentAction.getPlayerIdx(), output, mapper);
        } else if (command.equals("getFrozenCardsOnTable")) {
            getFrozenCardsOnTableFunction(table, mapper, output);
        } else if (command.equals("getTotalGamesPlayed")) {
            getTotalGamesPlayedFunction(player1, player2, game, output, mapper);
        } else if (command.equals("getPlayerOneWins")) {
            getPlayerWinsFunction(1, player1, output, mapper);
        } else if (command.equals("getPlayerTwoWins")) {
            getPlayerWinsFunction(2, player1, output, mapper);
        }
    }

    public void getPlayerDeckFunction(ObjectMapper mapper, ActionsInput currentAction, Player player1, Player player2, GameInput currentGame, ArrayNode output) {
        ObjectNode object = mapper.createObjectNode();
        ArrayNode array = mapper.createArrayNode();

        object.put("command", "getPlayerDeck");
        object.put("playerIdx", currentAction.getPlayerIdx());

        ArrayList < Card > currentDeck = player1.getDecks().get(player1.getIndexDeck());
        if (currentAction.getPlayerIdx() == 2)
            currentDeck = player2.getDecks().get(player2.getIndexDeck());

        printCardsInDeck(mapper, currentDeck, array);

        object.set("output", array);
        output.add(object);
    }
    public void printCardsInDeck(ObjectMapper mapper, ArrayList < Card > currentDeck, ArrayNode array) {
        for (int i = 0; i < currentDeck.size(); i++) {
            ObjectNode cardNode = mapper.createObjectNode();
            Card currentCard = currentDeck.get(i);

            addCardToOutput(mapper, cardNode, currentCard);
            array.add(cardNode);
        }
    }

    public void getPlayerHeroFunction(ObjectMapper mapper, ActionsInput currentAction, Player player1, Player player2, GameInput currentGame, ArrayNode output) {
        ObjectNode object = mapper.createObjectNode();

        object.put("command", "getPlayerHero");
        object.put("playerIdx", currentAction.getPlayerIdx());

        Hero currentHero = player1.getHero();

        if (currentAction.getPlayerIdx() == 2)
            currentHero = player2.getHero();

        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("mana", currentHero.getCostMana());
        cardNode.put("description", currentHero.getDescription());
        ArrayNode culori = mapper.createArrayNode();
        for (int i = 0; i < currentHero.getColors().size(); i++)
            culori.add(currentHero.getColors().get(i));
        cardNode.set("colors", culori);
        cardNode.put("name", currentHero.getName());
        cardNode.put("health", currentHero.getHealth());

        object.put("output", cardNode);
        output.add(object);
    }

    public void getPlayerTurnFunction(ObjectMapper mapper, ArrayNode output, Game game) {
        ObjectNode object = mapper.createObjectNode();
        ArrayNode array = mapper.createArrayNode();

        object.put("command", "getPlayerTurn");
        object.put("output", game.getTurn());

        output.add(object);
    }

    public void endPlayerTurnFunction(GameInput currentGame, Player player1, Player player2, Game game, Table table) {
        if (game.getTurn() == 1) {
            player1.setWasTurn(true);
            game.setTurn(2);
            table.unfreezeRow(2);
            table.unfreezeRow(3);
        } else {
            player2.setWasTurn(true);
            game.setTurn(1);
            table.unfreezeRow(0);
            table.unfreezeRow(1);
        }
        if (player1.isWasTurn() && player2.isWasTurn()) {
            // mai multe eventual
            game.beginRound(player1, player2, currentGame, game, table);
        }
    }

    public void placeCardFunction(Player player1, Player player2, Game game, int index, Table table, ObjectMapper mapper, ArrayNode output) {
        Player currentPlayer = player1;
        if (game.getTurn() == 2)
            currentPlayer = player2;
        if (currentPlayer.getCardsInHand().size() > index) {
            if (currentPlayer.getMana() < currentPlayer.getCardsInHand().get(index).getCostMana()) {
                // System.out.println("Not enough mana to place card on table.");
                ObjectNode object = mapper.createObjectNode();
                object.put("command", "placeCard");
                object.put("handIdx", index);
                object.put("error", "Not enough mana to place card on table.");
                output.add(object);
            } else {
                Card card = currentPlayer.getCardsInHand().get(index);
                int row;
                if (game.getTurn() == 1) {
                    if (card.getName().equals("Goliath") || card.getName().equals("Warden") || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
                        row = 2;
                    } else {
                        row = 3;
                    }
                } else {
                    if (card.getName().equals("Goliath") || card.getName().equals("Warden") || card.getName().equals("Miraj") || card.getName().equals("The Ripper")) {
                        row = 1;
                    } else {
                        row = 0;
                    }
                }

                if (table.getCards().get(row).size() >= 5) {
                    ObjectNode object = mapper.createObjectNode();
                    object.put("command", "placeCard");
                    object.put("handIdx", index);
                    object.put("error", "Cannot place card on table since row is full.");
                    // System.out.println("Cannot place card on table since row is full.");
                    output.add(object);
                } else {
                    table.getCards().get(row).add(card);
                    currentPlayer.getCardsInHand().remove(index);
                    currentPlayer.addMana(-card.getCostMana());
                    if ((row == 1 || row == 2) && (card.getName().equals("Goliath") || card.getName().equals("Warden")))
                        currentPlayer.setHasTank(1);
                }
            }
        }
    }

    public void getCardsInHandFunction(Player player1, Player player2, int playerId, ArrayNode output, ObjectMapper mapper) {
        ObjectNode object = mapper.createObjectNode();
        object.put("command", "getCardsInHand");
        object.put("playerIdx", playerId);
        ArrayNode array = mapper.createArrayNode();
        if (playerId == 1) {
            printCardsInDeck(mapper, player1.getCardsInHand(), array);
        } else {
            printCardsInDeck(mapper, player2.getCardsInHand(), array);
        }
        object.put("output", array);
        output.add(object);
    }

    public void getCardsOnTableFunction(Table table, ObjectMapper mapper, ArrayNode output) {

        ObjectNode object = mapper.createObjectNode();
        object.put("command", "getCardsOnTable");
        ArrayNode arrayRow = mapper.createArrayNode();
        for (int i = 0; i < table.getCards().size(); i++) {
            ArrayNode arrayCollum = mapper.createArrayNode();
            printCardsInDeck(mapper, table.getCards().get(i), arrayCollum);
            arrayRow.add(arrayCollum);
        }

        object.put("output", arrayRow);
        output.add(object);
    }

    public void getPlayerManaFunction(Player player1, Player player2, int playerId, ArrayNode output, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", playerId);

        if (playerId == 1) {
            objectNode.put("output", player1.getMana());
        } else {
            objectNode.put("output", player2.getMana());
        }

        output.add(objectNode);
    }

    public void getTotalGamesPlayedFunction(Player player1, Player player2, Game game, ArrayNode output, ObjectMapper mapper) {
        ObjectNode object = mapper.createObjectNode();

        object.put("command", "getPlayerTurn");
        object.put("output", game.getNumberOfGames());

        output.add(object);
    }

    public void getPlayerWinsFunction(int index, Player player, ArrayNode output, ObjectMapper mapper) {
        ObjectNode object = mapper.createObjectNode();

        if (index == 1) {
            object.put("command", "getPlayerOneWins");
        }

        if (index == 2) {
            object.put("command", "getPlayerTwoWins");
        }

        object.put("output", player.getWins());
        output.add(object);
    }

    public void getFrozenCardsOnTableFunction(Table table, ObjectMapper mapper, ArrayNode output) {
        ObjectNode object = mapper.createObjectNode();
        object.put("command", "getFrozenCardsOnTable");
        ArrayNode arrayRow = mapper.createArrayNode();
        for (int i = 0; i < table.getCards().size(); i++) {
            printFrozenCardsInDeck(mapper, table.getCards().get(i), arrayRow);
        }

        object.put("output", arrayRow);
        output.add(object);
    }
    public void printFrozenCardsInDeck(ObjectMapper mapper, ArrayList < Card > currentDeck, ArrayNode array) {
        for (int i = 0; i < currentDeck.size(); i++) {
            ObjectNode cardNode = mapper.createObjectNode();
            Card currentCard = currentDeck.get(i);

            if (currentCard.isFrozen()) {
                addCardToOutput(mapper, cardNode, currentCard);
                array.add(cardNode);
            }
        }
    }
    public void addCardToOutput(ObjectMapper mapper, ObjectNode cardNode, Card currentCard) {
        cardNode.put("mana", currentCard.getCostMana());
        cardNode.put("attackDamage", currentCard.getAttack());
        cardNode.put("health", currentCard.getHealth());
        cardNode.put("description", currentCard.getDescription());

        ArrayNode culori = mapper.createArrayNode();
        for (int j = 0; j < currentCard.getColors().size(); j++)
            culori.add(currentCard.getColors().get(j));
        cardNode.set("colors", culori);

        cardNode.put("name", currentCard.getName());
    }

    public void getCardAtPositionFunction(ArrayNode output, ObjectMapper mapper, ActionsInput actionInput, Table table) {
        int x = actionInput.getX();
        int y = actionInput.getY();

        ObjectNode cardNode = mapper.createObjectNode();

        cardNode.put("command", "getCardAtPosition");
        cardNode.put("x", x);
        cardNode.put("y", y);

        if (table.getCards().size() <= x || table.getCards().get(x).size() <= y) {
            cardNode.put("output", "No card available at that position.");
        } else {
            Card card = table.getCards().get(x).get(y);
            ObjectNode node = mapper.createObjectNode();
            addCardToOutput(mapper, node, card);
            cardNode.set("output", node);
        }

        output.add(cardNode);
    }

    public void cardUsesAttackFunction(Table table, ArrayNode output, ObjectMapper mapper, ActionsInput actionInput, Game game, Player player1, Player player2) {
        int xOfAttacker = actionInput.getCardAttacker().getX();
        int yOfAttacker = actionInput.getCardAttacker().getY();
        if (table.getCards().size() <= xOfAttacker || table.getCards().get(xOfAttacker).size() <= yOfAttacker) {
            return;
        }
        Card attacker = table.getCards().get(xOfAttacker).get(yOfAttacker);

        int xOfAttacked = actionInput.getCardAttacked().getX();
        int yOfAttacked = actionInput.getCardAttacked().getY();
        if (table.getCards().size() <= xOfAttacked || table.getCards().get(xOfAttacked).size() <= yOfAttacked) {
            return;
        }
        Card attacked = table.getCards().get(xOfAttacked).get(yOfAttacked);

        ObjectNode node = mapper.createObjectNode();
        node.put("command", "cardUsesAttack");

        ObjectNode attackerNode = mapper.createObjectNode();
        attackerNode.put("x", xOfAttacker);
        attackerNode.put("y", yOfAttacker);
        node.set("cardAttacker", attackerNode);

        ObjectNode attackedNode = mapper.createObjectNode();
        attackedNode.put("x", xOfAttacked);
        attackedNode.put("y", yOfAttacked);
        node.set("cardAttacked", attackedNode);

        if (game.getTurn() == 1 && xOfAttacked > 1) {
            node.put("error", "Attacked card does not belong to the enemy.");
            output.add(node);
            return;
        }

        if (game.getTurn() == 2 && xOfAttacked < 2) {
            node.put("error", "Attacked card does not belong to the enemy.");
            output.add(node);
            return;
        }

        if (attacker.isHasAttacked()) {
            node.put("error", "Attacker card has already attacked this turn.");
            output.add(node);
            return;
        }

        if (attacker.isFrozen()) {
            node.put("error", "Attacker card is frozen.");
            output.add(node);
            return;
        }

        if (game.getTurn() == 1 && player2.getHasTank() == 1 && xOfAttacked != 1) {
            node.put("error", "Attacked card is not of type 'Tank'.");
            output.add(node);
            return;
        }

        if (game.getTurn() == 2 && player1.getHasTank() == 1 && xOfAttacked != 2) {
            node.put("error", "Attacked card is not of type 'Tank'.");
            output.add(node);
            return;
        }

        attacker.setHasAttacked(true);
        attacked.addHealth(-attacker.getAttack());
        if (attacked.getHealth() < 1) {
            table.getCards().get(xOfAttacked).remove(yOfAttacked);

            Player currentPlayer = player1;
            if (game.getTurn() == 2)
                currentPlayer = player2;

            int tank = 0;
            for (int i = 0; i < table.getCards().get(xOfAttacked).size(); i++) {
                Card card = table.getCards().get(xOfAttacked).get(i);
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    tank = 1;
                    break;
                }
            }

            currentPlayer.setHasTank(tank);

            game.analyzeTank(player1, player2, table);
        }
    }

    public void useAttackHeroFunction(Table table, ArrayNode output, ObjectMapper mapper, Player player1, Player player2, Game game, ActionsInput actionInput) {
        Player currentPlayer = player1;
        if (game.getTurn() == 2) {
            currentPlayer = player2;
        }

        int xOfAttacker = actionInput.getCardAttacker().getX();
        int yOfAttacker = actionInput.getCardAttacker().getY();
        if (table.getCards().size() <= xOfAttacker || table.getCards().get(xOfAttacker).size() <= yOfAttacker) {
            return;
        }

        Card attacker = table.getCards().get(xOfAttacker).get(yOfAttacker);

        ObjectNode node = mapper.createObjectNode();
        node.put("command", "useAttackHero");

        ObjectNode attackerNode = mapper.createObjectNode();
        attackerNode.put("x", xOfAttacker);
        attackerNode.put("y", yOfAttacker);
        node.set("cardAttacker", attackerNode);

        if (table.getCards().get(xOfAttacker).get(yOfAttacker).isFrozen()) {
            node.put("error", "Attacker card is frozen.");
            output.add(node);
            return;
        }

        if (table.getCards().get(xOfAttacker).get(yOfAttacker).isHasAttacked()) {
            // plus verificare daca si-a folosit abilitatea deja tot aici
            node.put("error", "Attacker card has already attacked this turn.");
            output.add(node);
            return;
        }

        if (game.getTurn() == 1) {
            int tank = 0;
            for (int i = 0; i < table.getCards().get(1).size(); i++) {
                Card card = table.getCards().get(1).get(i);
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    tank = 1;
                    break;
                }
            }

            if (tank == 1) {
                node.put("error",  "Attacked card is not of type 'Tank'.");
                output.add(node);
                return;
            }

            // ataca-l
            attacker.setHasAttacked(true);
            player2.getHero().addHealth(-attacker.getAttack());
            // verifica daca a murit
            if (player2.getHero().getHealth() < 1) {
                ObjectNode statusnode = mapper.createObjectNode();
                statusnode.put("gameEnded",  "Player one killed the enemy hero.");
                output.add(statusnode);
            }
        } else if (game.getTurn() == 2) {
            int tank = 0;
            for (int i = 0; i < table.getCards().get(2).size(); i++) {
                Card card = table.getCards().get(2).get(i);
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    tank = 1;
                    break;
                }
            }

            if (tank == 1) {
                node.put("error",  "Attacked card is not of type 'Tank'.");
                output.add(node);
                return;
            }

            // ataca-l
            attacker.setHasAttacked(true);
            player1.getHero().addHealth(-attacker.getAttack());
            // verifica daca a murit
            if (player1.getHero().getHealth() < 1) {
                ObjectNode statusnode = mapper.createObjectNode();
                statusnode.put("gameEnded",  "Player two killed the enemy hero.");
                output.add(statusnode);
            }
        }
    }

    public void cardUsesAbilityFunction(Player player1, Player player2, Game game, ArrayNode output, ObjectMapper mapper, ActionsInput actionInput, Table table) {

        int xOfAttacker = actionInput.getCardAttacker().getX();
        int yOfAttacker = actionInput.getCardAttacker().getY();
        if (table.getCards().size() <= xOfAttacker || table.getCards().get(xOfAttacker).size() <= yOfAttacker) {
            return;
        }

        Card attacker = table.getCards().get(xOfAttacker).get(yOfAttacker);

        int xOfAttacked = actionInput.getCardAttacked().getX();
        int yOfAttacked = actionInput.getCardAttacked().getY();
        if (table.getCards().size() <= xOfAttacked || table.getCards().get(xOfAttacked).size() <= yOfAttacked) {
            return;
        }

        Card attacked = table.getCards().get(xOfAttacked).get(yOfAttacked);

        ObjectNode node = errorfunction(mapper, xOfAttacker, yOfAttacker, xOfAttacked, yOfAttacked);

        if (attacker.isFrozen()) {
            node.put("error", "Attacked card is frozen.");
            output.add(node);
            return;
        }

        if (attacker.isHasAttacked()) {
            node.put("error", "Attacker card has already attacked this turn.");
            output.add(node);
            return;
        }

        if (attacker.getName().equals("Disciple")) {
            if (game.getTurn() == 1 && xOfAttacked < 2) {
                node.put("error", "Attacked card does not belong to the current player.");
                output.add(node);
                return;
            } else if (game.getTurn() == 2 && xOfAttacked > 1) {
                node.put("error", "Attacked card does not belong to the current player.");
                output.add(node);
                return;
            }
        }

        if (attacker.getName().equals("Miraj") || attacker.getName().equals("The Ripper") || attacker.getName().equals("The Cursed One")) {
            if (game.getTurn() == 1 && xOfAttacked > 1) {
                node.put("error", "Attacked card does not belong to the enemy.");
                output.add(node);
                return;
            } else if (game.getTurn() == 2 && xOfAttacked < 2) {
                node.put("error", "Attacked card does not belong to the enemy.");
                output.add(node);
                return;
            }

            if (game.getTurn() == 1 && player2.getHasTank() == 1 && !attacked.getName().equals("Goliath") && !attacked.getName().equals("Warden")) {
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.add(node);
                return;
            }

            if (game.getTurn() == 2 && player1.getHasTank() == 1 && !attacked.getName().equals("Goliath") && !attacked.getName().equals("Warden")) {
                node.put("error", "Attacked card is not of type 'Tank'.");
                output.add(node);
                return;
            }
        }

        //attacker.useAbility(attacker, attacked);
        if (attacker.getName().equals("Disciple")) {
            attacked.addHealth(2);
        } else if (attacker.getName().equals("Miraj")) {
            int attackerNewHealth = attacker.getHealth();
            int attackedNewHealth = attacked.getHealth();
            attacker.setHealth(attackedNewHealth);
            attacked.setHealth(attackerNewHealth);
        } else if (attacker.getName().equals("The Ripper")) {
            attacked.setAttack(attacked.getAttack() - 2);
            if (attacked.getAttack() < 0)
                attacked.setAttack(0);
        } else if (attacker.getName().equals("The Cursed One")) {
            int health = attacked.getHealth();
            int attack = attacked.getAttack();
            attacked.setAttack(health);
            attacked.setHealth(attack);
        }

        attacker.setHasAttacked(true);

        if (attacked.getHealth() < 1) {
            table.getCards().get(xOfAttacked).remove(yOfAttacked);

            Player currentPlayer = player1;
            if (game.getTurn() == 2)
                currentPlayer = player2;

            int tank = 0;
            for (int i = 0; i < table.getCards().get(xOfAttacked).size(); i++) {
                Card card = table.getCards().get(xOfAttacked).get(i);
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    tank = 1;
                    break;
                }
            }

            currentPlayer.setHasTank(tank);

            game.analyzeTank(player1, player2, table);
        }
    }
    public ObjectNode errorfunction(ObjectMapper mapper, int xOfAttacker, int yOfAttacker, int xOfAttacked, int yOfAttacked) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "cardUsesAbility");

        ObjectNode attackedNode = mapper.createObjectNode();
        attackedNode.put("x", xOfAttacked);
        attackedNode.put("y", yOfAttacked);
        node.set("cardAttacked", attackedNode);

        ObjectNode attackerNode = mapper.createObjectNode();
        attackerNode.put("x", xOfAttacker);
        attackerNode.put("y", yOfAttacker);
        node.set("cardAttacker", attackerNode);

        return node;
    }

    public void useHeroAbilityFunction(ActionsInput actionInput, Table table, ArrayNode output, ObjectMapper mapper, Player player1, Player player2, Game game) {
        Player currentPlayer = player1;
        if (game.getTurn() == 2)
            currentPlayer = player2;
        Hero hero = currentPlayer.getHero();

        ObjectNode node = mapper.createObjectNode();
        node.put("command", "useHeroAbility");
        node.put("affectedRow", actionInput.getAffectedRow());

        if (currentPlayer.getMana() < hero.getCostMana()) {
            node.put("error", "Not enough mana to use hero's ability.");
            output.add(node);
            return;
        }

        if (hero.isHasAttacked()) {
            node.put("error", "Hero has already attacked this turn.");
            output.add(node);
            return;
        }

        if (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina")) {
            if (game.getTurn() == 1 && actionInput.getAffectedRow() > 1) {
                node.put("error", "Selected row does not belong to the enemy.");
                output.add(node);
                return;
            } else if (game.getTurn() == 2 && actionInput.getAffectedRow() < 2) {
                node.put("error", "Selected row does not belong to the enemy.");
                output.add(node);
                return;
            }
        }

        if (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface")) {
            if (game.getTurn() == 1 && actionInput.getAffectedRow() < 2) {
                node.put("error", "Selected row does not belong to the current player.");
                output.add(node);
                return;
            } else if (game.getTurn() == 2 && actionInput.getAffectedRow() > 1) {
                node.put("error", "Selected row does not belong to the current player.");
                output.add(node);
                return;
            }
        }

        // hero.useAbility(table, actionInput.getAffectedRow());
        int row = actionInput.getAffectedRow();
        if (hero.getName().equals("Empress Thorina")) {
            Card destroy = table.getCards().get(row).get(0);
            for (int i = 0; i < table.getCards().get(row).size(); i++) {
                Card card = table.getCards().get(row).get(i);
                if (card.getHealth() > destroy.getHealth()) {
                    destroy = card;
                }
            }
            table.getCards().get(row).remove(destroy);
        } else if (hero.getName().equals("Lord Royce")) {
            for (int i = 0; i < table.getCards().get(row).size(); i++) {
                Card card = table.getCards().get(row).get(i);
                card.setFrozen(true);
            }
        } else if (hero.getName().equals("General Kocioraw")) {
            for (int i = 0; i < table.getCards().get(row).size(); i++) {
                Card card = table.getCards().get(row).get(i);
                card.setAttack(card.getAttack() + 1);
            }
        } else if (hero.getName().equals("King Mudface")) {
            for (int i = 0; i < table.getCards().get(row).size(); i++) {
                Card card = table.getCards().get(row).get(i);
                card.setHealth(card.getHealth() + 1);
            }
        }

        hero.setHasAttacked(true);
        currentPlayer.addMana(-hero.getCostMana());

        if (hero.getName().equals("Empress Thorina")) {
            currentPlayer = player1;
            if (game.getTurn() == 2)
                currentPlayer = player2;

            int tank = 0;
            for (int i = 0; i < table.getCards().get(actionInput.getAffectedRow()).size(); i++) {
                Card card = table.getCards().get(actionInput.getAffectedRow()).get(i);
                if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
                    tank = 1;
                    break;
                }
            }

            currentPlayer.setHasTank(tank);
            game.analyzeTank(player1, player2, table);
        }
    }

    public Commands() {
        this.command = "";
    }

    public Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commands commands = (Commands) o;
        return Objects.equals(command, commands.command);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(command);
    }

    @Override
    public String toString() {
        return "Commands{" +
                "command='" + command + '\'' +
                '}';
    }
}
