package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MJTOlympicsTest {

    @Mock
    private CompetitionResultFetcher competitionResultFetcherMock;

    @Mock
    private Set<Competitor> registeredCompetitorsMock;

    @Mock
    private Map<String, EnumMap<Medal, Integer>> nationsMedalTableMock;

    @InjectMocks
    private MJTOlympics mjtOlimpics;

    @Test
    void testGetTotalMedalsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlimpics.getTotalMedals(null),
            "Should throw IllegalArgumentException when given nationality is null");
    }

    @Test
    void testGetTotalMedalsWithNonRegisteredNation() {
        when(nationsMedalTableMock.containsKey("Bulgaria")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> mjtOlimpics.getTotalMedals("Bulgaria"),
            "Should throw IllegalArgumentException when given nationality is not registered.");
    }

    @Test
    void testGetTotalMedalsWithNonEmptyCollectionOfMedals() {
        EnumMap<Medal, Integer> nationsMedals = new EnumMap<>(Medal.class);
        nationsMedals.put(Medal.GOLD, 3);
        nationsMedals.put(Medal.SILVER, 2);
        nationsMedals.put(Medal.BRONZE, 1);

        when(nationsMedalTableMock.containsKey("Bulgaria")).thenReturn(true);
        when(nationsMedalTableMock.get("Bulgaria")).thenReturn(nationsMedals);

        assertEquals(5, mjtOlimpics.getTotalMedals("Bulgaria"), "Expected the sum of all medals from types.");

        verify(nationsMedalTableMock).get("Bulgaria");
        verify(nationsMedalTableMock, times(1)).get("Bulgaria");
    }

    @Test
    void testGetTotalMedalsWithNullCollectionOMedals() {
        when(nationsMedalTableMock.containsKey("Bulgaria")).thenReturn(true);
        when(nationsMedalTableMock.get("Bulgaria")).thenReturn(null);

        assertEquals(0, mjtOlimpics.getTotalMedals("Bulgaria"), "Expected 0.");

        verify(nationsMedalTableMock).get("Bulgaria");
        verify(nationsMedalTableMock, times(1)).get("Bulgaria");
    }

    @Test
    void testGetTotalMedalsFromEmptyCollectionOfMedals() {
        when(nationsMedalTableMock.containsKey("Bulgaria")).thenReturn(true);
        when(nationsMedalTableMock.get("Bulgaria")).thenReturn(new EnumMap<Medal, Integer>(Medal.class));

        assertEquals(0, mjtOlimpics.getTotalMedals("Bulgaria"), "Expected 0 when there are no medals won for the given nation.");

        verify(nationsMedalTableMock).get("Bulgaria");
        verify(nationsMedalTableMock, times(1)).get("Bulgaria");
    }
}
