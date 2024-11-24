package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NationMedalComparatorTest {

    @Mock
    private MJTOlympics mjtOlympicsMock;

    @InjectMocks
    private NationMedalComparator nationMedalComparator;

    @Test
    void testCompareNationsWithEqualTotalMedals() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(15);
        when(mjtOlympicsMock.getTotalMedals("Macedonia")).thenReturn(15);

        assertEquals(-1, nationMedalComparator.compare("Bulgaria", "Macedonia"),
            "Should return -1  for nations with the same amount of medals and first nation alphabetically before second.");

        verify(mjtOlympicsMock).getTotalMedals("Bulgaria");
        verify(mjtOlympicsMock).getTotalMedals("Macedonia");

        verify(mjtOlympicsMock, times(1)).getTotalMedals("Macedonia");
        verify(mjtOlympicsMock, times(1)).getTotalMedals("Bulgaria");
    }

    @Test
    void testCompareANationWithItself() {
        String nation = "Bulgaria";

        assertEquals(0, nationMedalComparator.compare(nation, nation),
            "Comparing a nation with itself should return 0.");
    }

    @Test
    void testCompareMoreMedalsWithLessMedals() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(15);
        when(mjtOlympicsMock.getTotalMedals("Macedonia")).thenReturn(10);

        assertEquals(-1, nationMedalComparator.compare("Bulgaria", "Macedonia"),
            "Comparing a nation with more medals with a nation with less medals should return -1.");

        verify(mjtOlympicsMock).getTotalMedals("Bulgaria");
        verify(mjtOlympicsMock).getTotalMedals("Macedonia");

        verify(mjtOlympicsMock, times(1)).getTotalMedals("Macedonia");
        verify(mjtOlympicsMock, times(1)).getTotalMedals("Bulgaria");
    }

    @Test
    void testCompareLessMedalsWithMoreMedals() {
        when(mjtOlympicsMock.getTotalMedals("Bulgaria")).thenReturn(10);
        when(mjtOlympicsMock.getTotalMedals("Macedonia")).thenReturn(15);

        assertEquals(1, nationMedalComparator.compare("Bulgaria", "Macedonia"),
            "Comparing a nation with less medals with a nation with more medals should return 1.");

        verify(mjtOlympicsMock).getTotalMedals("Bulgaria");
        verify(mjtOlympicsMock).getTotalMedals("Macedonia");

        verify(mjtOlympicsMock, times(1)).getTotalMedals("Macedonia");
        verify(mjtOlympicsMock, times(1)).getTotalMedals("Bulgaria");
    }
}
