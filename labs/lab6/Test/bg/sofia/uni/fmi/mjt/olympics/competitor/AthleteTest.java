package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AthleteTest {

    @Mock
    private Collection<Medal> medalsMock;

    @InjectMocks
    Athlete athlete = new Athlete("id", "name", "nationality");

    @Test
    void testAddMedalThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null),
            "Attempting to add null should throw IllegalArgumentException.");
    }

    @Test
    void testMedalGetsAdded() {
        Collection<Medal> copy = new ArrayList<>();
        Medal medal = Medal.GOLD;

        copy.addAll(athlete.getMedals());
        copy.add(medal);
        athlete.addMedal(medal);

        assertIterableEquals(copy, athlete.getMedals(), "Collection of medals should contain added medal.");
    }

}
