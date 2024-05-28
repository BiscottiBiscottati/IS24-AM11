package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardArchitectTest {

    @Test
    void buildCornerLines() {
    }

    @Test
    void buildCenterString() {
    }

    @Test
    void buildPointsString() {
    }

    @Test
    void buildRequirementsString() {
    }

    @Test
    void buildTable() {
    }

    @Test
    void buildHand() {

        List<Integer> ids = List.of(1, 2);
        List<String> res = null;

        try {
            res = CardArchitect.buildHand(ids);
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i));
        }
    }

    @Test
    void buildCard() {
    }

    @Test
    void buildObjective() {

        List<String> res = null;
        for (int j = 81; j < 97; j++) {

            try {
                res = CardArchitect.buildObjective(j);
            } catch (IllegalCardBuildException e) {
                System.out.println("not obj cad");
            }

            for (int i = 0; i < 7; i++) {
                System.out.println(res.get(i));
            }
        }


    }

    @Test
    void buildDeck() {
    }

    @Test
    void spaces() {
    }

    @Test
    void buildVertObj() {

        List<String> list;
        try {
            list = CardArchitect.buildVertObj(81, 82, 83);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        } catch (IllegalCardBuildException e) {
            System.out.println("not obj");
        }


    }
}