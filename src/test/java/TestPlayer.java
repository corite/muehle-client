import backend.entities.Player;
import backend.entities.StoneState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TestPlayer {
    @Test
    public void testPlayer() {
        try {
            new Player("a", StoneState.NONE);
            fail();
        } catch (IllegalArgumentException e) {}

        new Player("b",StoneState.WHITE);
        new Player("c",StoneState.BLACK);
    }
}
