package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompetitionTest {

    @Mock
    private Set<Competitor> competitorsMock;

    private Competition competition=new Competition("name", "disciplne", competitorsMock);

    @Test
    void testCompetitorsAreUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
            ()->competition.competitors().clear(),
            "Should return an unmodifiable collection of competitors.");
    }

}
