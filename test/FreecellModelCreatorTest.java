import org.junit.Test;

import cs3500.hw02.FreecellModel;
import cs3500.hw02.FreecellOperations;
import cs3500.hw04.FreecellModelCreator;
import cs3500.hw04.MultiMoveFreecellModel;

import static org.junit.Assert.assertEquals;

public class FreecellModelCreatorTest {

  @Test
  public void testCreateSingleMove() {
    FreecellOperations model =
            FreecellModelCreator.create(FreecellModelCreator.GameType.SINGLEMOVE);

    assertEquals(true, model instanceof FreecellModel);
    assertEquals(false, model instanceof MultiMoveFreecellModel);
  }

  @Test
  public void testCreateMultiMove() {
    FreecellOperations model =
            FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);

    assertEquals(true, model instanceof FreecellModel);
    assertEquals(true, model instanceof MultiMoveFreecellModel);
  }
}
